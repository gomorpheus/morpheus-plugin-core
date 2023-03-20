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
class CustomSpecSync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private VmwarePlugin vmwarePlugin

	public CustomSpecSync(VmwarePlugin vmwarePlugin, Cloud cloud) {
		this.vmwarePlugin = vmwarePlugin
		this.cloud = cloud
		this.morpheusContext = vmwarePlugin.morpheusContext
	}

	def execute() {
		log.debug "execute: ${cloud}"

		try {
			def listResults = vmwarePlugin.cloudProvider.listCustomizationSpecs(cloud)
			if(listResults.success == true) {
				Observable<ReferenceDataSyncProjection> domainRecords = morpheusContext.cloud.listReferenceDataByCategory(cloud, "vmware.vsphere.customizationSpec.${cloud.id}")
				SyncTask<ReferenceDataSyncProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, listResults?.customSpecs)
				syncTask.addMatchFunction { ReferenceDataSyncProjection domainObject, Map apiItem ->
					domainObject.name == apiItem.name
				}.onDelete { removeItems ->
					log.debug "onDelete ${removeItems}"
					morpheusContext.cloud.remove(removeItems).blockingGet()
				}.onUpdate { List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems ->

				}.onAdd { itemsToAdd ->
					log.debug "onAdd ${itemsToAdd}"
					def newItems = []
					itemsToAdd.each { it ->
						def add = new ReferenceData(
								code:"vmware.vsphere.customizationSpec.${cloud.id}.${it.name}",
								category:"vmware.vsphere.customizationSpec.${cloud.id}",
								name:it.name,
								keyValue:it.name,
								value:it.version,
								description:it.description,
								//xref:it.type  // Doesn't look like xref is used
						)
						newItems << add
					}
					morpheusContext.cloud.create(newItems, cloud, "vmware.vsphere.customizationSpec.${cloud.id}").blockingGet()
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.cloud.listReferenceDataById(updateItems.collect { it.existingItem.id } as List<Long>).map {ReferenceData referenceData ->
						SyncTask.UpdateItemDto<ReferenceData, Map> matchItem = updateItemMap[referenceData.id]
						return new SyncTask.UpdateItem<ReferenceData,Map>(existingItem:referenceData, masterItem:matchItem.masterItem)
					}
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}


}
