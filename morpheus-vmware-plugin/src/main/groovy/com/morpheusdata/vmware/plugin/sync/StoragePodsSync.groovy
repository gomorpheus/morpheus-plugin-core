package com.morpheusdata.vmware.plugin.sync

import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.projection.ComputeServerIdentityProjection
import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Datastore
import com.morpheusdata.model.projection.DatastoreIdentityProjection
import com.morpheusdata.core.*
import com.morpheusdata.core.util.SyncTask
import io.reactivex.*

@Slf4j
class StoragePodsSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public StoragePodsSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def execute() {
		log.debug "cacheStoragePods: ${cloud}"

		try {
			def listResults = VmwareCloudProvider.listStoragePods(cloud)
			if(listResults.success == true) {


				Observable domainRecords = morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection projection ->
					return projection.type == 'cluster'
				}
				SyncTask<DatastoreIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, listResults?.storagePods)
				syncTask.addMatchFunction { DatastoreIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<DatastoreIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<DatastoreIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheusContext.cloud.datastore.listById(updateItems?.collect { it.existingItem.id }).map { Datastore datastore ->
						SyncTask.UpdateItemDto<DatastoreIdentityProjection, Map> matchItem = updateItemMap[datastore.id]
						return new SyncTask.UpdateItem<Datastore, Map>(existingItem: datastore, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
					addStoragePods(itemsToAdd)
				}.onUpdate { List<SyncTask.UpdateItem<Datastore, Map>> updateItems ->
					updateStoragePods(updateItems)
				}.onDelete { removeItems ->
					removeStoragePods(removeItems)
				}.start()

			}
		} catch(e) {
			log.error("cacheStoragePods error: ${e}", e)
		}

	}

	private addStoragePods(storagePods) {
		log.debug "addStoragePod: ${storagePods}"

		def datastores = []

		def servers = loadServers()

		storagePods.each { storagePod ->
			try {
				def datastoreConfig = [
						owner     : cloud.owner,
						cloud     : cloud,
						name      : storagePod.name,
						externalId: storagePod.ref,
						type      : 'cluster',
						online    : false,
						category  : "vmware.vsphere.storagePod.${cloud.id}",
						drsEnabled: storagePod.drsEnabled,
						freeSpace : storagePod.freeSpace
				]

				def datastore = new Datastore(datastoreConfig)

				storagePod.datastores?.each { store ->
					def accessible = store.summary.isAccessible()
					datastore.online = datastore.online || accessible
				}

				if (datastore.online && servers?.size() > 0) {
					def serversFound = servers.find { ComputeServer server ->
						server.volumes?.find { it.externalId in storagePod.datastoreRefs }
					}
					if (!serversFound) {
						datastore.online = false
						datastore.active = false
					}
				}

				datastores << datastore
			} catch (e) {
				log.error "Error adding storagePod: ${storagePod} ${e}", e
			}
		}

		if(datastores) {
			log.debug "Saving ${datastores.size()} storage pods"
			morpheusContext.cloud.datastore.create(datastores).blockingGet()

			syncDatastoresToPods(storagePods)
		}
	}

	private updateStoragePods(storagePods) {
		log.debug "updateStoragePods: ${storagePods}"

		def datastores = []

		def servers = loadServers()

		for(storagePod in storagePods) {
			try {
				Datastore existingItem = storagePod.existingItem
				def masterItem = storagePod.masterItem

				boolean save = false
				if(existingItem) {
					def matchOnline = false

					masterItem.datastores?.each { store ->
						def accessible = store.summary.isAccessible()
						matchOnline = matchOnline || accessible
					}

					if(matchOnline) {
						println "BOBW : StoragePodsSync.groovy:135 : masterItem.datastoreRefs"
						def serversFound = servers.find { ComputeServer server ->
							server.volumes?.find { it.externalId in masterItem.datastoreRefs }
						}

						println "BOBW : StoragePodsSync.groovy:140 : seversfoud ${serversFound}"

						if(!serversFound) {
							matchOnline = false
						}
					}

					if(existingItem.online != matchOnline) {
						existingItem.online = matchOnline
						save = true
					}
					if(existingItem.freeSpace != masterItem.freeSpace) {
						existingItem.freeSpace = masterItem.freeSpace
						save = true
					}
					if(existingItem.name != masterItem.name) {
						existingItem.name = masterItem.name
						save = true
					}
					if(save) {
						datastores << existingItem
					}
				}
			} catch (e) {
				log.error "Error updating storagePod: ${storagePod}, ${e}", e
			}
		}

		if(datastores) {
			log.debug "Saving ${datastores.size()} storage pods"
			morpheusContext.cloud.datastore.save(datastores).blockingGet()

			syncDatastoresToPods(storagePods.collect { it.masterItem })
		}

	}

	private removeStoragePods(storagePods) {
		log.debug "removeStoragePods: ${storagePods}"
		def itemsToDelete = storagePods.collect { it ->
			new DatastoreIdentityProjection(id: it.id, externalId: it.externalId)
		}
		log.debug "removing the following ${itemsToDelete.size()} ${itemsToDelete}"
		morpheusContext.cloud.datastore.remove(itemsToDelete, null).blockingGet()
	}

	def syncDatastoresToPods(storagePods) {
		log.debug "syncDatastoresToPods: ${storagePods.size()}"
		try {
			if (storagePods) {
				// Build up some lookups
				def datastoreIds = []
				def storagePodsMap = [:]
				def externalIds = []
				storagePods.each { storagePod ->
					externalIds << storagePod.ref
					storagePodsMap[storagePod.ref] = storagePod
				}

				// Fetch the datastore for these storage pods
				def datastores = []
				def datastoreProjectionIds = []
				morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection projection ->
					return projection.type == 'cluster' && projection.externalId in externalIds
				}.blockingSubscribe { datastoreProjectionIds << it.id }
				morpheusContext.cloud.datastore.listById(datastoreProjectionIds).blockingSubscribe { datastores << it }

				// Gather up all the datastores for all datastoreRefs related to the storagePods
				def datastoreRefsExternalIds = storagePods.collect { it.datastoreRefs }?.flatten()?.unique()
				def datastoreRefs = [:]
				morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter { DatastoreIdentityProjection projection ->
					return projection.externalId in datastoreRefsExternalIds
				}.blockingSubscribe { datastoreRefs[it.externalId] = it }

				// Lets associate the appropriate datastores to the DRS Cluster
				def itemsToSave = []
				datastores?.each { Datastore drsCluster ->
					def masterDatastores = []
					def cloudDatastoreRefs = storagePodsMap[drsCluster.externalId].datastoreRefs
					cloudDatastoreRefs?.each { datastoreRef ->
						def ds = datastoreRefs[datastoreRef]
						if(ds) {
							masterDatastores << ds
						}
					}

					def save = false
					def itemsToRemove = drsCluster.datastores?.findAll { ds -> !masterDatastores.find { it.id == ds.id } }
					itemsToRemove?.each { ds ->
						save = true
						List<Datastore> tmpDatastores = drsCluster.datastores
						tmpDatastores.remove(ds)
						drsCluster.datastores = tmpDatastores
					}
					def itemsToAdd = masterDatastores?.findAll { ds -> !drsCluster.datastores?.find { it.id == ds.id } }
					itemsToAdd?.each { ds ->
						save = true
						List<Datastore> tmpDatastores = drsCluster.datastores
						tmpDatastores.add(ds)
						drsCluster.datastores = tmpDatastores
					}

					if (save) {
						itemsToSave << drsCluster
					}
				}
				if (itemsToSave) {
					log.debug "Have ${itemsToSave.size()} datastores to save"
					morpheusContext.cloud.datastore.save(itemsToSave).blockingGet()
				}
			}
		} catch(e) {
			log.error "Error syncing storagepods: ${e}", e
		}
	}

	private loadServers() {
		def projs = []
		morpheusContext.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection proj ->
			proj.category == "vmware.vsphere.host.${cloud.id}"
		}.blockingSubscribe { projs << it }
		def servers = []
		morpheusContext.computeServer.listById(projs.collect {it.id}).blockingSubscribe { servers << it }
		servers
	}
}
