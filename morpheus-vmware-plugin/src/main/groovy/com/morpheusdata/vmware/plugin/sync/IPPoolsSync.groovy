package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Cloud
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.core.util.NetworkUtility
import io.reactivex.*

@Slf4j
class IPPoolsSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private VmwarePlugin vmwarePlugin

	public IPPoolsSync(VmwarePlugin vmwarePlugin, Cloud cloud) {
		this.vmwarePlugin = vmwarePlugin
		this.cloud = cloud
		this.morpheusContext = vmwarePlugin.morpheusContext
	}

	def execute() {
		log.debug "execute: ${cloud}"

		try {
			def listResults = vmwarePlugin.cloudProvider.listIpPools(cloud)
			if(listResults.success == true) {
				
				def masterItems = listResults?.ipPools
				
				Observable domainRecords = morpheusContext.network.pool.listIdentityProjections().filter { NetworkPoolIdentityProjection projection ->
					return (projection.accountId == cloud.account.id && projection.category ==  "vmware.vsphere.ipPool.${cloud.id}")
				}
				SyncTask<NetworkPoolIdentityProjection, Map, NetworkPool> syncTask = new SyncTask<>(domainRecords, masterItems)
				syncTask.addMatchFunction { NetworkPoolIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref.toString()
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheusContext.network.pool.listById(updateItems?.collect { it.existingItem.id }).map { NetworkPool networkPool ->
						SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map> matchItem = updateItemMap[networkPool.id]
						return new SyncTask.UpdateItem<NetworkPool, Map>(existingItem: networkPool, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
					addIPPools(itemsToAdd)
				}.onUpdate { List<SyncTask.UpdateItem<NetworkPool, Map>> updateItems ->
					updateIPPools(updateItems)
				}.onDelete { removeItems ->
					removeIPPools(removeItems)
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	private addIPPools(cloudIpPools) {
		log.debug "addIPPools: ${cloudIpPools}"

		def ipPools = []

		def poolType = new NetworkPoolType(code: 'vmware-plugin-ipam')

		cloudIpPools?.each { cloudIpPool ->
			try {
				def addConfig = [
						account      : cloud.account,
						code         : "vmware.vsphere.ipPool.${cloud.id}.${cloudIpPool.ref}",
						category     : "vmware.vsphere.ipPool.${cloud.id}",
						name         : cloudIpPool.name,
						externalId   : cloudIpPool.ref,
						ipCount      : cloudIpPool.ipv4Count ?: 0,
						ipFreeCount  : cloudIpPool.ipv4Available ?: 0,
						dnsDomain    : cloudIpPool.dnsDomain,
						dnsSearchPath: cloudIpPool.dnsSearchPath,
						hostPrefix   : cloudIpPool.hostPrefix,
						httpProxy    : cloudIpPool.httpProxy,
						dhcpServer   : cloudIpPool.dhcpServer == true,
						dnsServers   : cloudIpPool.dnsServers,
						gateway      : cloudIpPool.gateway,
						poolEnabled  : cloudIpPool.ipPoolEnabled == true,
						netmask      : cloudIpPool.netmask,
						subnetAddress: cloudIpPool.subnetAddress,
						type         : poolType,
						refType      : 'ComputeZone',
						refId        : "${cloud.id}"
				]
				def ipPool = new NetworkPool(addConfig)

				def ipRanges = cloudIpPool.ipRanges?.tokenize(',')
				ipRanges?.each { ipRange ->
					def rangeList = ipRange.tokenize('#')
					if(rangeList.size() > 1) {
						def startAddress = rangeList[0]
						def ipCount = rangeList[1].toInteger()
						def endAddress = NetworkUtility.getNextIpAddress(startAddress, ipCount)
						def newRange = new NetworkPoolRange(
								networkPool:ipPool,
								startAddress:startAddress,
								endAddress:endAddress,
								addressCount:ipCount,
								externalId:ipRange)
						ipPool.addToIpRanges(newRange)
					}
				}

				ipPools << ipPool
			} catch (e) {
				log.error "Error adding ip Pool: ${cloudIpPool} ${e}", e
			}
		}

		if(ipPools) {
			log.debug "Adding ${ipPools.size()} NetworkPools"
			morpheusContext.network.pool.create(ipPools).blockingGet()
		}
	}

	private updateIPPools(updateMaps) {
		log.debug "updateIPPools: ${updateMaps}"

		def poolType = new NetworkPoolType(code: 'vmware-plugin')

		def itemsToSave = []

		updateMaps.each { it ->
			try {
				def cloudPool = it.masterItem
				NetworkPool existingItem = it.existingItem

				def doSave = false
				if(existingItem.ipRanges == null || existingItem.ipRanges?.size() == 0) {
					def ipRanges = cloudPool.ipRanges?.tokenize(',')
					if(ipRanges.size() > 0) {
						ipRanges?.each { ipRange ->
							def rangeList = ipRange.tokenize('#')
							if(rangeList.size() > 1) {
								def startAddress = rangeList[0]
								def ipCount = rangeList[1].toInteger()
								def endAddress = NetworkUtility.getNextIpAddress(startAddress, ipCount)
								def newRange = new NetworkPoolRange(
										networkPool:existingItem,
										startAddress:startAddress,
										endAddress:endAddress,
										addressCount:ipCount,
										externalId:ipRange
								)
								existingItem.addToIpRanges(newRange)
							}
						}
						doSave = true
					}
				}
				if(existingItem.type == null) {
					existingItem.type = poolType
					doSave = true
				}
				if(doSave == true)
					itemsToSave << existingItem

			} catch (e) {
				log.error "error updating NetworkPool ${it}, ${e}", e
			}
		}

		if(itemsToSave) {
			log.debug "Saving ${itemsToSave.size()} NetworkPools"
			morpheusContext.network.pool.save(itemsToSave).blockingGet()
		}
	}

	private removeIPPools(ipPools) {
		log.debug "removeIPPools: ${ipPools?.size()}"
		morpheusContext.network.pool.remove(ipPools).blockingGet()
	}
}
