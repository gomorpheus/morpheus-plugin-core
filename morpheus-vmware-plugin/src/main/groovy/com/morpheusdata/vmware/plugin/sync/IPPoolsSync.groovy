package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Cloud
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*

@Slf4j
class IPPoolsSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public HostsSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def execute() {
		log.debug "execute: ${cloud}"

		try {
			def listResults = VmwareCloudProvider.listIpPools(cloud)
			if(listResults.success == true) {
				
				def masterItems = listResults?.ipPools
				
				Observable domainRecords = morpheusContext.cloud.pool.listSyncProjections(cloud.id).filter { NetworkPoolIdentityProjection projection ->
					return (projection.accountId == cloud.account.id && projection.category ==  "vmware.vsphere.ipPool.${cloud.id}")
				}
				SyncTask<NetworkPoolIdentityProjection, Map, NetworkPool> syncTask = new SyncTask<>(domainRecords, masterItems)
				syncTask.addMatchFunction { NetworkPoolIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref.toString()
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheusContext.cloud.pool.listById(updateItems?.collect { it.existingItem.id }).map { NetworkPool networkPool ->
						SyncTask.UpdateItemDto<NetworkPoolIdentityProjection, Map> matchItem = updateItemMap[networkPool.id]
						return new SyncTask.UpdateItem<NetworkPool, Map>(existingItem: networkPool, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
//					addNetworkPools(itemsToAdd)
				}.onUpdate { List<SyncTask.UpdateItem<NetworkPool, Map>> updateItems ->
//					updateNetworkPools(updateItems)
				}.onDelete { removeItems ->
//					removeNetworkPools(removeItems)
				}.start()


//				def poolType = NetworkPoolType.findByCode('vmware')
//
//				//add adds
//				addList?.each {
//					def ipRanges = it.ipRanges?.tokenize(',')
//					def addConfig = [account:opts.zone.account, code:"vmware.vsphere.ipPool.${opts.zone.id}.${it.ref}", category:"vmware.vsphere.ipPool.${opts.zone.id}",
//					                 name:it.name, externalId:it.ref, ipCount:it.ipv4Count ?: 0, ipFreeCount:it.ipv4Available ?: 0, dnsDomain:it.dnsDomain, dnsSearchPath:it.dnsSearchPath,
//					                 hostPrefix:it.hostPrefix, httpProxy:it.httpProxy, dhcpServer:it.dhcpServer == true, dnsServers:it.dnsServers, gateway:it.gateway,
//					                 poolEnabled:it.ipPoolEnabled == true, netmask:it.netmask, subnetAddress:it.subnetAddress,
//					                 type:poolType, refType:'ComputeZone', refId:"${opts.zone.id}"]
//					def add = new NetworkPool(addConfig)
//					add.save()
//					ipRanges?.each { ipRange ->
//						def rangeList = ipRange.tokenize('#')
//						if(rangeList.size() > 1) {
//							def startAddress = rangeList[0]
//							def ipCount = rangeList[1].toInteger()
//							def endAddress = NetworkPoolService.getIpRangeEnd(startAddress, ipCount)
//							def newRange = new NetworkPoolRange(networkPool:add, startAddress:startAddress, endAddress:endAddress, addressCount:ipCount, externalId:ipRange)
//							newRange.save()
//							log.debug("vmware new range: ${newRange.errors}")
//							add.addToIpRanges(newRange)
//						}
//					}
//					add.save(flush:true)
//				}
//				//remove removes
//				removeList?.each {
//					it.delete(flush:true)
//				}
//				matchList?.each {
//					def existingItem = existingItems.find{item -> item.externalId == it.ref}
//					log.debug("match: ${existingItem} ranges: ${it.ipRanges}")
//					if(existingItem) {
//						def doSave = false
//						if(existingItem.ipRanges == null || existingItem.ipRanges?.size() == 0) {
//							def ipRanges = it.ipRanges?.tokenize(',')
//							if(ipRanges.size() > 0) {
//								ipRanges?.each { ipRange ->
//									def rangeList = ipRange.tokenize('#')
//									if(rangeList.size() > 1) {
//										def startAddress = rangeList[0]
//										def ipCount = rangeList[1].toInteger()
//										def endAddress = NetworkPoolService.getIpRangeEnd(startAddress, ipCount)
//										def newRange = new NetworkPoolRange(networkPool:existingItem, startAddress:startAddress, endAddress:endAddress, addressCount:ipCount, externalId:ipRange)
//										newRange.save()
//										log.debug("vmware new range: ${newRange.errors}")
//										existingItem.addToIpRanges(newRange)
//									}
//								}
//								doSave = true
//							}
//						}
//						if(existingItem.type == null) {
//							existingItem.type = poolType
//							doSave = true
//						}
//						if(doSave == true)
//							existingItem.save(flush:true)
//					}
//				}
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}


}
