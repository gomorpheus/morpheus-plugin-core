package com.morpheusdata.vmware.plugin.sync

import com.morpheusdata.model.Account
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Datastore
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.projection.DatastoreIdentityProjection
import com.morpheusdata.model.projection.NetworkIdentityProjection
import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.Cloud
import com.morpheusdata.core.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*

@Slf4j
class NetworksSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private Collection<NetworkType> networkTypes

	public NetworksSync(Cloud cloud, MorpheusContext morpheusContext,  Collection<NetworkType> networkTypes) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
		this.networkTypes = networkTypes
	}

	def execute() {
		log.debug "cacheNetworks: ${cloud}"

		try {
			def clusters = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return projection.type == 'Cluster' && projection.internalId != null
			}.blockingSubscribe { clusters << it }
			def allResults = [:]
			clusters?.each { ComputeZonePoolIdentityProjection cluster ->
				def listResults = VmwareCloudProvider.listNetworks(cloud)
				if (listResults.success == true) {
					allResults[cluster.id] = listResults
				}
			}
			Observable domainRecords = morpheusContext.cloud.network.listSyncProjections(cloud.id).filter { NetworkIdentityProjection ->
				return projection.type.code != 'childNetwork' && projection.category ==  "vmware.network.${cloud.id}"
			}
			domainRecords.subscribe({NetworkIdentityProjection existingNetwork ->
				println "AC Log - NetworksSync:execute printExistingNetwork - ${existingNetwork}"
			})
			for(clusterId in allResults.keySet()) {
				SyncTask<NetworkIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, allResults[clusterId].networks)
				syncTask.addMatchFunction { NetworkIdentityProjection domainObject, Map cloudItem ->
					domainObject[0] == cloudItem?.ref || (domainObject[3] == 'nsxt' && domainObject[1] == cloudItem.name)
				}.onAdd { itemsToAdd ->
					def adds = []
					def alreadyAddedRefs = []
					itemsToAdd?.each { cloudItem ->
						if(cloudItem.ref && !alreadyAddedRefs.contains(cloudItem.ref)) {
							def networkType = networkTypes?.find{it.externalType == cloudItem.type}
							def networkConfig = [
									owner: new Account(id: cloud.owner.id),
									category:" vmware.network.${cloud.id}",
									name: cloudItem.name,
									displayName: cloudItem.name,
									code: "vmware.network.${cloud.id}.${cloudItem.ref}",
									uniqueId: cloudItem.ref,
									externalId: cloudItem.ref,
									dhcpServer: true,
									externalType: cloudItem.type,
									type: networkType,
									refType: 'ComputeZone',
									refId: cloud.id,
									zonePoolId: clusterId,
									active:true
							]
							println "AC Log - NetworksSync:execute add - ${networkConfig}"
							if(cloudItem.switchUuid)
								networkConfig.internalId = cloudItem.switchUuid
							Network add = new Network(networkConfig)
							networkConfig.assignedZonePools = [new ComputeZonePool(id: clusterId)]
							adds << add
							alreadyAddedRefs << cloudItem.ref
						}
					}
					morpheusContext.cloud.network.create(adds).blockingGet()
				}.onUpdate { List<SyncTask.UpdateItem<Network, Map>> updateItems ->
					println "AC Log - NetworksSync:execute updates - ${updateItems}"
				}.onDelete { removeItems ->
					println "AC Log - NetworksSync:execute removes - ${removeItems}"

				}.start()
			}
//			SyncTask<NetworkIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, a?.datastores)
//			syncTask.addMatchFunction { DatastoreIdentityProjection domainObject, Map cloudItem ->
//				domainObject.externalId == cloudItem?.ref
//			}.onAdd { itemsToAdd ->
//				def adds = []
//				itemsToAdd?.each { cloudItem ->
//					def type = cloudItem.summary?.type ?: 'generic'
//					def datastoreConfig = [
//							owner       : new Account(id: cloud.owner.id),
//							name        : cloudItem.name,
//							externalId  : cloudItem.ref,
//							cloud       : cloud,
//							storageSize : cloudItem.summary.capacity,
//							freeSpace   : cloudItem.freeSpace,
//							zonePool    : new ComputeZonePool(id: cluster.id),
//							type        : type,
//							category    : "vmware.vsphere.datastore.${cloud.id}",
//							drsEnabled  : false,
//							online      : cloudItem.accessible,
//							externalPath: cloudItem.info.remotePath
//					]
//					Datastore add = new Datastore(datastoreConfig)
//					add.assignedZonePools = [new ComputeZonePool(id: cluster.id)]
//					adds << add
//
//				}
//				morpheusContext.cloud.datastore.create(adds).blockingGet()
//			}.onUpdate { List<SyncTask.UpdateItem<Datastore, Map>> updateItems ->
//				for(item in updateItems) {
//					def masterItem = item.masterItem
//					Datastore existingItem = item.existingItem
//					def save = false
//					def type = masterItem.summary?.type ?: 'generic'
//					if(masterItem.accessible != existingItem.online) {
//						existingItem.online = masterItem.accessible
//						save = true
//					}
//					if(masterItem.info.remotePath != existingItem.externalPath) {
//						existingItem.externalPath = masterItem.info.remotePath
//						save = true
//					}
//					if(existingItem.type != type) {
//						existingItem.type = type
//						save = true
//					}
//					if(existingItem.name != masterItem.name) {
//						existingItem.name = masterItem.name
//						save = true
//					}
//					if(masterItem.freeSpace != existingItem.freeSpace) {
//						existingItem.freeSpace = masterItem.freeSpace
//						save = true
//					}
//					if(masterItem.summary.capacity != existingItem.storageSize) {
//						existingItem.storageSize = masterItem.summary.capacity
//						save = true
//					}
//					if(existingItem.zonePool?.id != cluster.id) {
//						existingItem.zonePool = new ComputeZonePool(id: cluster.id)
//						save = true
//					}
//					if(!existingItem.assignedZonePools?.find{it.id == cluster.id}) {
//						existingItem.assignedZonePools += new ComputeZonePool(id: cluster.id)
//						save=true
//					}
//					if(save) {
//						morpheusContext.cloud.datastore.save([existingItem]).blockingGet()
//					}
//				}
//			}.onDelete { removeItems ->
//				morpheusContext.cloud.datastore.remove(removeItems, cluster).blockingGet()
//			}.start()
//
//
//		}
//	}
//			}.then { syncResults ->
//				syncResults.existingItemsByCluster = []
//				Network.withNewSession { session ->
//
//					syncResults.existingItemsByCluster = Network.withCriteria {
//						createAlias('zonePool','zonePool', CriteriaSpecification.LEFT_JOIN)
//						createAlias('type', 'type', CriteriaSpecification.LEFT_JOIN)
//						createAlias('networkServer', 'networkServer', CriteriaSpecification.LEFT_JOIN)
//						ne('type.code', 'childNetwork')
//
//						eq('refType','ComputeZone')
//						eq('refId', opts.zone.id)
//						eq('category', "vmware.network.${opts.zone.id}")
//
//						projections {
//							property('externalId')
//							property('name')
//							property('id')
//							property('networkSource')
//							property('networkServer.id')
//						}
//					}
//
//				}
//				return syncResults
//			}.then { syncResults ->
//				def syncListsByCluster = [:]
//				for(clusterId in syncResults.listResults.keySet()) {
//					def listResults = syncResults.listResults[clusterId]
//					if(listResults.success) {
//						def objList = listResults.networks
//						def existingItems = syncResults.existingItemsByCluster
//						//sync it
//						def matchFunction = { morpheusItem, Map cloudItem ->
//							morpheusItem[0] == cloudItem?.ref || (morpheusItem[3] == 'nsxt' && morpheusItem[1] == cloudItem.name)
//						}
//						syncListsByCluster[clusterId] = ComputeUtility.buildSyncLists(existingItems, objList, matchFunction)
//					}
//				}
//				return syncListsByCluster
//			}.then { syncListsByCluster ->
//				def networksAdded = []
//				Network.withNewSession { session ->
//					for(clusterId in syncListsByCluster.keySet()) {
//						def syncLists = syncListsByCluster[clusterId]
//						while(syncLists.addList?.size() > 0) {
//							List chunkedAddList = syncLists.addList.take(50)
//							syncLists.addList = syncLists.addList.drop(50)
//							networksAdded += addMissingNetworks(opts.zone,clusterId, chunkedAddList, networksAdded)
//						}
//					}
//				}
//				return syncListsByCluster
//			}.then { syncListsByCluster ->
//				//update list
//				Network.withNewSession { session ->
//					for(clusterId in syncListsByCluster.keySet()) {
//						def syncLists = syncListsByCluster[clusterId]
//						while(syncLists.updateList?.size() > 0) {
//							List chunkedUpdateList = syncLists.updateList.take(50)
//							syncLists.updateList = syncLists.updateList.drop(50)
//							updateMatchedNetworks(opts.zone,clusterId, chunkedUpdateList)
//						}
//					}
//				}
//				return syncListsByCluster
//			}.then { syncListsByCluster ->
//				Network.withNewSession { session ->
//					for(clusterId in syncListsByCluster.keySet()) {
//						def syncLists = syncListsByCluster[clusterId]
//						while(syncLists.removeList?.size() > 0) {
//							List chunkedRemoveList = syncLists.removeList.take(50)
//							syncLists.removeList = syncLists.removeList.drop(50)
//							removeMissingNetworks(opts.zone,clusterId, chunkedRemoveList)
//						}
//					}
//				}
//				return syncListsByCluster
//			}.onError { Exception ex ->
//				log.error("Error Caching Networks in Vmware Cloud {} - {}",opts.zone.id,ex.message,ex)
//				return false
//			}

		} catch(e) {
			log.error "Error in execute of NetworksSync: ${e}", e
		}
	}



/*
	protected addMissingNetworks(ComputeZone zone, clusterId, List addList, List alreadyAdded) {

		ComputeZonePool cluster = ComputeZonePool.get(clusterId)
		Collection<NetworkType> networkTypes = NetworkType.where{ code =~ 'vmware%'}.list(cache:true, readOnly:true)
		def networksAdded = []

		for(cloudItem in addList) {
			if(!alreadyAdded.contains(cloudItem.ref)) {
				if(cloudItem.ref) {
					def networkType = loadNetworkType(zone,cloudItem.type, networkTypes)
					def networkConfig = [owner:zone.owner, category:"vmware.network.${zone.id}", name:cloudItem.name,
										 code:"vmware.network.${zone.id}.${cloudItem.ref}", uniqueId:cloudItem.ref, externalId:cloudItem.ref, dhcpServer:true,
										 externalType:cloudItem.type, type:networkType, refType:'ComputeZone', refId:zone.id, zone:zone,
										 zonePool:cluster, active:true]
					if(cloudItem.switchUuid)
						networkConfig.internalId = cloudItem.switchUuid
					networkConfig.assignedZonePools = [cluster]
					networkService.createSyncedNetwork(networkConfig, zone.account)
					networksAdded << cloudItem.ref
				}
			} else {

			}
		}
		return networksAdded
	}

	protected updateMatchedNetworks(ComputeZone currentZone, clusterId, List updateList) {
		ComputeZonePool cluster = ComputeZonePool.get(clusterId)
		Collection<NetworkType> networkTypes = NetworkType.where{ code =~ 'vmware%'}.list(cache:true, readOnly:true)
		Map<Long,Network> existingItems = Network.where{zone.id == currentZone.id && id in updateList.collect{it.existingItem[2]}}.list()?.collectEntries{ [(it.id):it] }
		for(update in updateList) {
			def doSave = false
			Network existingItem = existingItems[update.existingItem[2]] as Network
			if(existingItem) {
				if(existingItem.zonePool != cluster) {
					existingItem.zonePool = cluster
					doSave = true
				}
				if(!['nsxt', 'nsx'].contains(existingItem.networkSource)) {
					if(existingItem.name != update.masterItem.name) {
						existingItem.name = update.masterItem.name
						doSave = true
					}
				}
				if(existingItem.externalId != update.masterItem.ref) {
					existingItem.externalId = update.masterItem.ref
					doSave = true
				}
				if(existingItem.description == update.masterItem.name) {
					existingItem.description = null
					doSave = true
				}
				if(existingItem.externalType != update.masterItem.type && existingItem.externalType != 'nsxtLogicalSwitch') {
					existingItem.externalType = update.masterItem.type
					doSave = true
				}
				if(!existingItem.type) {
					existingItem.type = loadNetworkType(zone,update.masterItem.type, networkTypes)
					doSave = true
				}
				if(!existingItem.assignedZonePools?.find{it.id == cluster.id}) {
					existingItem.addToAssignedZonePools(cluster)
					doSave=true
				}
				if(update.masterItem.switchUuid && update.masterItem.switchUuid != existingItem.internalId) {
					existingItem.internalId = update.masterItem.switchUuid
					doSave = true
				}

				if(doSave == true) {
					existingItem.save(flush:true)
				}
			}

		}
	}

	def syncNetworkSwitchId(Network network, ComputeZone zone) {
		def authConfig = vmwareProvisionService.getAuthConfig(zone)
		def switchResults = VmwareComputeUtility.getSwitchUuidForPortGroup(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, network.externalId)
		if(switchResults.success) {
			network.internalId = switchResults.switchUuid
		}
		network.save()
	}

	protected removeMissingNetworks(ComputeZone currentZone, clusterId, List removeList) {
		ComputeZonePool cluster = ComputeZonePool.get(clusterId)
		Network.where{zone.id == currentZone.id && id in removeList.collect{it[2]}}.list()?.each { removeItem ->
			if(!['nsxt', 'nsx'].contains(removeItem.networkSource)) {
				def removeCluster = removeItem.assignedZonePools?.find{it.id == cluster.id} //dont remove - fixes GORM removeFrom
				if(removeCluster) {
					log.info("removing cluster {} from network {} assigned resource pools", removeCluster, removeItem)
					removeItem.removeFromAssignedZonePools(removeCluster)
				}
				if(removeItem.zonePool?.id == cluster.id) {
					if(!removeItem.assignedZonePools?.size()) {
						log.info("removing network: {}", removeItem.dump())
						networkService.removeNetwork(removeItem, false)
					}
				} else if(removeItem.zonePool == null && (removeItem.assignedZonePools == null || removeItem.assignedZonePools.size() == 0)) {
					//shouldn't hit this unless clusters and networks changed
					log.warn("found network with no zone pool: {}", removeItem)
					networkService.removeNetwork(removeItem,false)
				}
			}
		}
	}
*/
}
