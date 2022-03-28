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
class HostsSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public HostsSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def execute() {
		log.debug "execute: ${cloud}"

		try {
			def statsData = []

			def queryResults = [:]

			queryResults.serverType = new ComputeServerType(code: 'vmware-plugin-hypervisor')
			queryResults.serverOs = new OsType(code: 'esxi.6')

			def poolListProjections = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { poolProjection ->
				return poolProjection.internalId != null && poolProjection.type == 'Cluster'
			}.blockingSubscribe { poolListProjections << it }
			queryResults.clusters = []
			morpheusContext.cloud.pool.listById(poolListProjections.collect { it.id }).blockingSubscribe { queryResults.clusters << it }

			def cloudItems = []
			def listResultSuccess = false
			queryResults.clusters?.each { cluster ->
				def listResults = VmwareCloudProvider.listHosts(cloud, cluster.internalId)
				if (listResults.success) {
					listResultSuccess = true
					cloudItems += listResults?.hosts
				}
			}

			if (listResultSuccess) {
				Observable domainRecords = morpheusContext.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection projection ->
					if (projection.category == "vmware.vsphere.host.${cloud.id}") {
						return true
					}
					false
				}
				SyncTask<ComputeServerIdentityProjection, Map, ComputeServer> syncTask = new SyncTask<>(domainRecords, cloudItems)
				syncTask.addMatchFunction { ComputeServerIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref.toString()
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheusContext.computeServer.listById(updateItems?.collect { it.existingItem.id }).map { ComputeServer server ->
						SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map> matchItem = updateItemMap[server.id]
						return new SyncTask.UpdateItem<ComputeServer, Map>(existingItem: server, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
					addMissingHosts(cloud, queryResults.clusters, itemsToAdd)
				}.onUpdate { List<SyncTask.UpdateItem<ComputeServer, Map>> updateItems ->
					updateMatchedHosts(cloud, queryResults.clusters, updateItems)
				}.onDelete { removeItems ->
					removeMissingHosts(cloud, removeItems)
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	def removeMissingHosts(Cloud cloud, List removeList) {
		log.debug "removeMissingHosts: ${cloud} ${removeList.size()}"
		morpheusContext.computeServer.remove(removeList).blockingGet()
	}

	def updateMatchedHosts(Cloud cloud, List clusters, List updateList) {
		log.debug "updateMatchedHosts: ${cloud} ${updateList.size()}"
//		def volumeType = StorageVolumeType.findByCode('vmware-datastore')

//		List<ComputeZonePool> zoneClusters = ComputeZonePool.where{zone.id == currentZone.id &&  type == 'Cluster' && internalId in updateList.collect{it.masterItem.cluster}.unique()}.list()
		def statsData = []
//		def matchedServersByExternalId = matchedServers?.collectEntries { [(it.externalId): it] }
		for(update in updateList) {
			ComputeServer currentServer = update.existingItem
			def matchedServer = update.masterItem
			if(currentServer) {
				def save = false
//				def clusterObj = zoneClusters?.find { pool -> pool.internalId == update.masterItem.cluster }
//				if(currentServer.resourcePool?.id != clusterObj.id) {
//					currentServer.resourcePool = clusterObj
//					save = true
//				}
//				def hostUuid = matchedServer.uuid
//				if(hostUuid && currentServer.uniqueId != hostUuid) {
//					currentServer.uniqueId = hostUuid
//					save = true
//				}
//				if(save) {
//					currentServer.save(flush: true)
//				}
//				syncHostDatastoreVolumes(currentServer,matchedServer,volumeType)
//				statsData += updateHostStats(currentServer, matchedServer)
			}
		}
//		if(statsData) {
//			def msg = [refId:1, jobType:'statsUpdate', data:statsData]
//			sendRabbitMessage('main', '', ApplianceJobService.applianceMonitorQueue, msg)
//		}
	}

	def addMissingHosts(Cloud cloud, List clusters, List addList) {
		log.debug "addMissingHosts: ${cloud} ${addList.size()}"
//		def volumeType = StorageVolumeType.findByCode('vmware-datastore')

		def serverType = new ComputeServerType(code: 'vmware-plugin-hypervisor')
		def serverOs = new OsType(code: 'esxi.6')
		for(cloudItem in addList) {
			def clusterObj = clusters?.find{ pool -> pool.internalId == cloudItem.cluster }

			def serverConfig = [account:cloud.owner, category:"vmware.vsphere.host.${cloud.id}", cloud: cloud,
			                    name:cloudItem.name, resourcePool: clusterObj, externalId:cloudItem.ref, uniqueId:cloudItem.uuid, sshUsername:'root', status:'provisioned',
			                    provision:false, serverType:'hypervisor', computeServerType:serverType, serverOs:serverOs,
			                    osType:'esxi', hostname:cloudItem.hostname]
			def newServer = new ComputeServer(serverConfig)
			newServer.maxMemory = cloudItem.memorySize
			newServer.maxStorage = 0
			newServer.capacityInfo = new ComputeCapacityInfo(maxMemory:cloudItem.memorySize)
			if(!morpheusContext.computeServer.create([newServer]).blockingGet()){
				log.error "Error in creating host server ${newServer}"
			}
//			syncHostDatastoreVolumes(newServer,cloudItem,volumeType)
		}
	}

}
