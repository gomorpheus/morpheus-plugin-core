package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.model.ComputeZoneFolder
import com.morpheusdata.model.projection.ComputeZoneFolderIdentityProjection
import com.morpheusdata.model.Cloud
import com.morpheusdata.vmware.plugin.utils.VmwareComputeUtility
import com.morpheusdata.vmware.plugin.VmwareProvisionProvider
import com.morpheusdata.core.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*

@Slf4j
class NetworksSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public NetworksSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def execute() {
		log.debug "cacheNetworks: ${cloud}"

		try {
			def clusters = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return projection.type == 'Cluster' && projection.internalId != null
			}.blockingSubscribe { clusters << it }
			clusters?.each { ComputeZonePoolIdentityProjection cluster ->
				def listResults = VmwareCloudProvider.listNetworks(cloud, cluster.internalId)

			}

			}.then { syncResults ->
				syncResults.existingItemsByCluster = []
				Network.withNewSession { session ->

					syncResults.existingItemsByCluster = Network.withCriteria {
						createAlias('zonePool','zonePool', CriteriaSpecification.LEFT_JOIN)
						createAlias('type', 'type', CriteriaSpecification.LEFT_JOIN)
						createAlias('networkServer', 'networkServer', CriteriaSpecification.LEFT_JOIN)
						ne('type.code', 'childNetwork')

						eq('refType','ComputeZone')
						eq('refId', opts.zone.id)
						eq('category', "vmware.network.${opts.zone.id}")

						projections {
							property('externalId')
							property('name')
							property('id')
							property('networkSource')
							property('networkServer.id')
						}
					}

				}
				return syncResults
			}.then { syncResults ->
				def syncListsByCluster = [:]
				for(clusterId in syncResults.listResults.keySet()) {
					def listResults = syncResults.listResults[clusterId]
					if(listResults.success) {
						def objList = listResults.networks
						def existingItems = syncResults.existingItemsByCluster
						//sync it
						def matchFunction = { morpheusItem, Map cloudItem ->
							morpheusItem[0] == cloudItem?.ref || (morpheusItem[3] == 'nsxt' && morpheusItem[1] == cloudItem.name)
						}
						syncListsByCluster[clusterId] = ComputeUtility.buildSyncLists(existingItems, objList, matchFunction)
					}
				}
				return syncListsByCluster
			}.then { syncListsByCluster ->
				def networksAdded = []
				Network.withNewSession { session ->
					for(clusterId in syncListsByCluster.keySet()) {
						def syncLists = syncListsByCluster[clusterId]
						while(syncLists.addList?.size() > 0) {
							List chunkedAddList = syncLists.addList.take(50)
							syncLists.addList = syncLists.addList.drop(50)
							networksAdded += addMissingNetworks(opts.zone,clusterId, chunkedAddList, networksAdded)
						}
					}
				}
				return syncListsByCluster
			}.then { syncListsByCluster ->
				//update list
				Network.withNewSession { session ->
					for(clusterId in syncListsByCluster.keySet()) {
						def syncLists = syncListsByCluster[clusterId]
						while(syncLists.updateList?.size() > 0) {
							List chunkedUpdateList = syncLists.updateList.take(50)
							syncLists.updateList = syncLists.updateList.drop(50)
							updateMatchedNetworks(opts.zone,clusterId, chunkedUpdateList)
						}
					}
				}
				return syncListsByCluster
			}.then { syncListsByCluster ->
				Network.withNewSession { session ->
					for(clusterId in syncListsByCluster.keySet()) {
						def syncLists = syncListsByCluster[clusterId]
						while(syncLists.removeList?.size() > 0) {
							List chunkedRemoveList = syncLists.removeList.take(50)
							syncLists.removeList = syncLists.removeList.drop(50)
							removeMissingNetworks(opts.zone,clusterId, chunkedRemoveList)
						}
					}
				}
				return syncListsByCluster
			}.onError { Exception ex ->
				log.error("Error Caching Networks in Vmware Cloud {} - {}",opts.zone.id,ex.message,ex)
				return false
			}

		} catch(e) {
			log.error "Error in execute of NetworksSync: ${e}", e
		}
	}




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

}
