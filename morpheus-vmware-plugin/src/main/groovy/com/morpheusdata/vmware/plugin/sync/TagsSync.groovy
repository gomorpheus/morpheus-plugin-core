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
class TagsSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private HttpApiClient client

	public TagsSync(Cloud cloud, MorpheusContext morpheusContext, HttpApiClient client) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
		this.client = client
	}

	def execute() {
		log.debug "blockexecuteingExecute TagsSync: ${cloud}"
		try {
			def opts = [:]
			def listResults = listTags(client, [:])
			if(listResults.success) {
				def existingCategories = [:]
				morpheusContext.metadataTag.metadataTagType.listSyncProjections('ComputeZone', cloud.id).blockingSubscribe { existingCategories[it.externalId] = it }

				Observable<MetadataTagIdentityProjection> domainRecords = morpheusContext.metadataTag.listSyncProjections('ComputeZone', cloud.id)
				SyncTask<MetadataTagIdentityProjection, Map, MetadataTag> syncTask = new SyncTask<>(domainRecords, listResults?.tags ?: [])
				syncTask.addMatchFunction { MetadataTagIdentityProjection domainObject, Map apiItem ->
					domainObject.externalId == apiItem?.externalId
				}.onDelete { removeItems ->
					removeTags(removeItems)
				}.onUpdate { List<SyncTask.UpdateItem<MetadataTag, Map>> updateItems ->
					// Nothing to do
				}.onAdd { itemsToAdd ->
					addTags(itemsToAdd, existingCategories)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<MetadataTagIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<MetadataTagIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.metadataTag.listById(updateItems.collect { it.existingItem.id } as List<Long>).map {MetadataTag tag ->
						SyncTask.UpdateItemDto<MetadataTag, Map> matchItem = updateItemMap[tag.id]
						return new SyncTask.UpdateItem<MetadataTag,Map>(existingItem:tag, masterItem:matchItem.masterItem)
					}
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	def listTags(HttpApiClient client, opts) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listTags(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, client, opts)
		return rtn
	}

	private addTags(addList, existingCategories) {
		log.debug "addTags: ${addList?.size()}"
		def itemsToAdd = []

		addList?.each { Map cloudItem ->
			log.debug("tag add: {}", cloudItem)
			def categoryObj = existingCategories[cloudItem.categoryId]
			//configure it and send to ops service
			if(categoryObj) {
				def add = new MetadataTag([
						type      : new MetadataTagType(id: categoryObj.id),
						name      : categoryObj.name,
						value     : cloudItem.name,
						externalId: cloudItem.externalId,
						refType   : 'ComputeZone',
						refId     : cloud.id])
				itemsToAdd << add
			}
		}

		if(itemsToAdd) {
			morpheusContext.metadataTag.create(itemsToAdd).blockingGet()
		}
	}

	private removeTags(removeList) {
		log.debug "removeTags: ${removeList?.size()}"
		morpheusContext.metadataTag.remove(removeList).blockingGet()
	}
}
