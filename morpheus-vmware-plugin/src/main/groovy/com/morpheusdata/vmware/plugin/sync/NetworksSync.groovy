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
			Observable domainRecords = morpheusContext.cloud.network.listSyncProjections(cloud.id).filter { NetworkIdentityProjection projection ->
				return projection.type.code != 'childNetwork' && projection.category ==  "vmware.network.${cloud.id}"
			}
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
								def assignedZonePools = existingItem.assignedZonePools
								assignedZonePools << new ComputeZonePool(id: clusterId)
								existingItem.assignedZonePools = assignedZonePools
								save=true
							}
							if(masterItem.switchUuid && masterItem.switchUuid != existingItem.internalId) {
								existingItem.internalId = masterItem.switchUuid
								save = true
							}

							if(save) {
								morpheusContext.cloud.network.save([existingItem]).blockingGet()
							}
						}

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
