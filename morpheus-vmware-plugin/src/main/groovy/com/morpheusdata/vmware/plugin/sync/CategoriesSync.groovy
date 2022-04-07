package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Cloud
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.core.util.HttpApiClient
import io.reactivex.*

@Slf4j
class CategoriesSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private HttpApiClient client

	public CategoriesSync(Cloud cloud, MorpheusContext morpheusContext, HttpApiClient client) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
		this.client = client
	}

	def execute() {
		log.debug "execute: ${cloud}"
		try {
			def opts = [:]
			def listResults = listTagCategories(client, [:])
			if(listResults.success) {
				Observable<MetadataTagTypeIdentityProjection> domainRecords = morpheusContext.metadataTag.metadataTagType.listSyncProjections('ComputeZone', cloud.id)
				SyncTask<MetadataTagTypeIdentityProjection, Map, MetadataTagType> syncTask = new SyncTask<>(domainRecords, listResults?.categories ?: [])
				syncTask.addMatchFunction { MetadataTagTypeIdentityProjection domainObject, Map apiItem ->
					domainObject.externalId == apiItem?.externalId
				}.onDelete { removeItems ->
					removeCategories(removeItems)
				}.onUpdate { List<SyncTask.UpdateItem<MetadataTagType, Map>> updateItems ->
					// Nothing to do
				}.onAdd { itemsToAdd ->
					addCategories(itemsToAdd)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<MetadataTagTypeIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<MetadataTagTypeIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.metadataTag.metadataTagType.listById(updateItems.collect { it.existingItem.id } as List<Long>).map {MetadataTagType tagType ->
						SyncTask.UpdateItemDto<MetadataTagType, Map> matchItem = updateItemMap[tagType.id]
						return new SyncTask.UpdateItem<MetadataTagType,Map>(existingItem:tagType, masterItem:matchItem.masterItem)
					}
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	private listTagCategories(HttpApiClient client, opts) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listCategories(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, client, opts)
		return rtn
	}

	private addCategories(addList) {
		log.debug "addCategories: ${addList?.size()}"
		def itemsToAdd = []
		addList?.each { Map cloudItem ->
			def add = new MetadataTagType([
					name      : cloudItem.name,
					externalId: cloudItem.externalId,
					refType   : 'ComputeZone',
					refId     : cloud.id,
					valueType : 'fixed'
			])
			itemsToAdd << add
		}

		if(itemsToAdd) {
			morpheusContext.metadataTag.metadataTagType.create(itemsToAdd).blockingGet()
		}
	}

	private removeCategories(removeList) {
		log.debug "removeCategories: ${removeList?.size()}"
		morpheusContext.metadataTag.metadataTagType.remove(removeList).blockingGet()
	}
}
