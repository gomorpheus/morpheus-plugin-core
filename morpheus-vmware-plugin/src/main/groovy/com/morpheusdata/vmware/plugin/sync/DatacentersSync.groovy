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
class DatacentersSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private VmwarePlugin vmwarePlugin

	public DatacentersSync(VmwarePlugin vmwarePlugin, Cloud cloud) {
		this.vmwarePlugin = vmwarePlugin
		this.cloud = cloud
		this.morpheusContext = vmwarePlugin.morpheusContext
	}

	def execute() {
		log.debug "execute DatacentersSync: ${cloud}"
		try {
			def listResults = listDatacenters(cloud)
			if(listResults.success) {
				Observable<ComputeZonePoolIdentityProjection> domainRecords = morpheusContext.cloud.pool.listSyncProjections(cloud.id, "vwmare.datacenter.${cloud.id}").filter { it ->
					it.ownerId == cloud.owner.id
				}
				SyncTask<ComputeZonePoolIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, listResults?.datacenters ?: [])
				syncTask.addMatchFunction { ComputeZonePoolIdentityProjection domainObject, Map apiItem ->
					domainObject.externalId == apiItem?.ref
				}.onDelete { removeItems ->
					removeDatacenters(removeItems)
				}.onUpdate { List<SyncTask.UpdateItem<ComputeZonePool, Map>> updateItems ->
					updateDatacenters(updateItems)
				}.onAdd { itemsToAdd ->
					addDatacenters(itemsToAdd)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.cloud.pool.listById(updateItems.collect { it.existingItem.id } as List<Long>).map {ComputeZonePool zonePool ->
						SyncTask.UpdateItemDto<ComputeZonePool, Map> matchItem = updateItemMap[zonePool.id]
						return new SyncTask.UpdateItem<ComputeZonePool,Map>(existingItem:zonePool, masterItem:matchItem.masterItem)
					}
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	private listDatacenters(Cloud cloud) {
		log.debug "listDatacenters: ${cloud}"
		def rtn = [success:false]
		def authConfig = vmwarePlugin.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listDatacenters(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [:])
		return rtn
	}

	private addDatacenters(addList) {
		log.debug "addDatacenters: ${addList?.size()}"
		def itemsToAdd = []
		def category = "vwmare.datacenter.${cloud.id}"
		addList?.each { Map cloudItem ->
			def addConfig = [
					owner     : cloud.owner,
					cloud     : cloud,
					category  : category,
					code      : "${category}.${cloudItem.ref}",
					externalId: cloudItem.ref,
					name      : cloudItem.name,
					type      : 'Datacenter',
					readOnly  : true,
					refType   : 'ComputeZone',
					refId     : cloud.id
			]
			def add = new ComputeZonePool(addConfig)
			add.rawData = cloudItem
			itemsToAdd << add
		}

		if(itemsToAdd) {
			morpheusContext.cloud.pool.create(itemsToAdd).blockingGet()
		}
	}

	private updateDatacenters(updateList) {
		log.debug "updateDatacenters: ${updateList?.size()}"
		def itemsToUpdate = []
		updateList?.each { updateMap ->
			def localItem = updateMap.existingItem
			def cloudItem = updateMap.masterItem
			def doSave = false

			if(!localItem.refId) {
				localItem.refType = "ComputeZone"
				localItem.refId = cloud.id
				doSave = true
			}

			if(localItem.name != cloudItem.name) {
				localItem.name = cloudItem.name
				doSave = true
			}

			if(doSave == true) {
				itemsToUpdate << localItem
			}
		}
		if(itemsToUpdate) {
			morpheusContext.cloud.pool.save(itemsToUpdate).blockingGet()
		}
	}

	private removeDatacenters(removeList) {
		log.debug "removeDatacenters: ${removeList?.size()}"
		morpheusContext.cloud.pool.remove(removeList).blockingGet()
	}
}
