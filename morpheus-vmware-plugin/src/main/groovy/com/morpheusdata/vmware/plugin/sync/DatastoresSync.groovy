package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Datastore
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.projection.DatastoreIdentityProjection
import com.morpheusdata.core.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*

@Slf4j
class DatastoresSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public DatastoresSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def blockingExecute() {
		log.debug "blockingExecute DatastoresSync: ${cloud}"

		try {
			def clusters = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return projection.type == 'Cluster' && projection.internalId != null
			}.blockingSubscribe { clusters << it }

			clusters?.each { ComputeZonePoolIdentityProjection cluster ->
				def listResults = VmwareCloudProvider.listDatastores(cloud, cluster.internalId)
				if(listResults.success == true) {

					Observable domainRecords = morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection projection ->
						return projection.type != 'cluster' || projection.type == null
					}
					SyncTask<DatastoreIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, listResults?.datastores)
					syncTask.addMatchFunction { DatastoreIdentityProjection domainObject, Map cloudItem ->
						domainObject.externalId == cloudItem?.ref
					}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<DatastoreIdentityProjection, Map>> updateItems ->
						Map<Long, SyncTask.UpdateItemDto<DatastoreIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
						morpheusContext.cloud.datastore.listById(updateItems?.collect { it.existingItem.id }).map { Datastore datastore ->
							SyncTask.UpdateItemDto<DatastoreIdentityProjection, Map> matchItem = updateItemMap[datastore.id]
							return new SyncTask.UpdateItem<Datastore, Map>(existingItem: datastore, masterItem: matchItem.masterItem)
						}
					}.onAdd { itemsToAdd ->
						def adds = []
						itemsToAdd?.each { cloudItem ->
							def type = cloudItem.summary?.type ?: 'generic'
							def datastoreConfig = [
									owner       : new Account(id: cloud.owner.id),
									name        : cloudItem.name,
									externalId  : cloudItem.ref,
									cloud       : cloud,
									storageSize : cloudItem.summary.capacity,
									freeSpace   : cloudItem.freeSpace,
									zonePool    : new ComputeZonePool(id: cluster.id),
									type        : type,
									category    : "vmware.vsphere.datastore.${cloud.id}",
									drsEnabled  : false,
									online      : cloudItem.accessible,
									externalPath: cloudItem.info.remotePath
							]
							Datastore add = new Datastore(datastoreConfig)
							add.assignedZonePools = [new ComputeZonePool(id: cluster.id)]
							adds << add

						}
						morpheusContext.cloud.datastore.create(adds).blockingGet()
					}.onUpdate { List<SyncTask.UpdateItem<Datastore, Map>> updateItems ->
						for(item in updateItems) {
							def masterItem = item.masterItem
							Datastore existingItem = item.existingItem
							def save = false
							def type = masterItem.summary?.type ?: 'generic'
							if(masterItem.accessible != existingItem.online) {
								existingItem.online = masterItem.accessible
								save = true
							}
							if(masterItem.info.remotePath != existingItem.externalPath) {
								existingItem.externalPath = masterItem.info.remotePath
								save = true
							}
							if(existingItem.type != type) {
								existingItem.type = type
								save = true
							}
							if(existingItem.name != masterItem.name) {
								existingItem.name = masterItem.name
								save = true
							}
							if(masterItem.freeSpace != existingItem.freeSpace) {
								existingItem.freeSpace = masterItem.freeSpace
								save = true
							}
							if(masterItem.summary.capacity != existingItem.storageSize) {
								existingItem.storageSize = masterItem.summary.capacity
								save = true
							}
							if(existingItem.zonePool?.id != cluster.id) {
								existingItem.zonePool = new ComputeZonePool(id: cluster.id)
								save = true
							}
							if(!existingItem.assignedZonePools?.find{it.id == cluster.id}) {
								existingItem.assignedZonePools += new ComputeZonePool(id: cluster.id)
								save=true
							}
							if(save) {
								morpheusContext.cloud.datastore.save([existingItem]).blockingGet()
							}
						}
					}.onDelete { removeItems ->
						morpheusContext.cloud.datastore.remove(removeItems, cluster).blockingGet()
					}.blockingStart()


				}
			}
		} catch(e) {
			log.error "Error in execute of DatastoresSync: ${e}", e
		}
	}

}
