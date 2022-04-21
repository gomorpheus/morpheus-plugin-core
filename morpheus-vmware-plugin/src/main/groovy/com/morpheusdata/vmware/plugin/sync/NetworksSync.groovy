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
		log.debug "execute NetworksSync: ${cloud}"

		try {
			def clusters = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return projection.type == 'Cluster' && projection.internalId != null
			}.blockingSubscribe { clusters << it }
			def allResults = [:]
			clusters?.each { ComputeZonePoolIdentityProjection cluster ->
				def listResults = VmwareCloudProvider.listNetworks(cloud, cluster.internalId)
				if (listResults.success == true) {
					allResults[cluster.id] = listResults
				}
			}
			Observable domainRecords = morpheusContext.cloud.network.listSyncProjections(cloud.id).filter { NetworkIdentityProjection projection ->
				return projection.typeCode != 'childNetwork'
			}
			for(clusterId in allResults.keySet()) {
				SyncTask<NetworkIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, allResults[clusterId].networks)
				syncTask.addMatchFunction { NetworkIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref || (domainObject.typeCode == 'nsxt' && domainObject.name == cloudItem.name)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<NetworkIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<NetworkIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheusContext.cloud.network.listById(updateItems?.collect { it.existingItem.id }).map { Network network ->
						SyncTask.UpdateItemDto<NetworkIdentityProjection, Map> matchItem = updateItemMap[network.id]
						return new SyncTask.UpdateItem<NetworkIdentityProjection, Map>(existingItem: network, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
					def adds = []
					def alreadyAddedRefs = []
					itemsToAdd?.each { cloudItem ->
						if(cloudItem.ref && !alreadyAddedRefs.contains(cloudItem.ref)) {
							def networkType = networkTypes?.find{it.externalType == cloudItem.type}
							def networkConfig = [
									owner: new Account(id: cloud.owner.id),
									category: "vmware.network.${cloud.id}",
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
					List<Network> itemsToUpdate = []
					for(item in updateItems) {
						def masterItem = item.masterItem
						Network existingItem = item.existingItem
						def save = false
						if(existingItem) {
							if(existingItem.zonePoolId != clusterId) {
								existingItem.zonePoolId = clusterId
								save = true
							}
							if(existingItem.name != masterItem.name) {
								existingItem.name = masterItem.name
								save = true
							}
							if(existingItem.externalId != masterItem.ref) {
								existingItem.externalId = masterItem.ref
								save = true
							}
							if(existingItem.description == masterItem.name) {
								existingItem.description = null
								save = true
							}
							if(existingItem.externalType != masterItem.type && existingItem.externalType != 'nsxtLogicalSwitch') {
								existingItem.externalType = masterItem.type
								save = true
							}
							if(!existingItem.type) {
								existingItem.type = networkTypes?.find{it.externalType == masterItem.type}
								save = true
							}
							if(!existingItem.assignedZonePools?.find{it.id == clusterId}) {
								existingItem.assignedZonePools += new ComputeZonePool(id: clusterId)
								save = true
							}
							if(masterItem.switchUuid && masterItem.switchUuid != existingItem.internalId) {
								existingItem.internalId = masterItem.switchUuid
								save = true
							}
							if(save) {
								itemsToUpdate << existingItem
							}
						}
					}
					if(itemsToUpdate.size() > 0) {
						morpheusContext.cloud.network.save(itemsToUpdate).blockingGet()
					}

				}.onDelete { removeItems ->
					morpheusContext.cloud.network.remove(removeItems).blockingGet()
				}.start()
			}

		} catch(e) {
			log.error "Error in execute of NetworksSync: ${e}", e
		}
	}

}
