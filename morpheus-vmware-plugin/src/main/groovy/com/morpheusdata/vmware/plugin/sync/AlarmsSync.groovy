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
class AlarmsSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public AlarmsSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def execute() {
		log.debug "execute: ${cloud}"
		try {
//			def listResults = VmwareCloudProvider.listAlarms(cloud)
//			if(listResults.success) {
//
//				Observable<ReferenceDataSyncProjection> domainRecords = morpheusContext.cloud.listReferenceDataByCategory(cloud, "vmware.vsphere.customizationSpec.${cloud.id}")
//				SyncTask<ReferenceDataSyncProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, listResults?.customSpecs)
//				syncTask.addMatchFunction { ReferenceDataSyncProjection domainObject, Map apiItem ->
//					domainObject.name == apiItem.name
//				}.onDelete { removeItems ->
//
//				}.onUpdate { List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems ->
//
//				}.onAdd { itemsToAdd ->
//
//				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItems ->
//					Map<Long, SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
//					morpheusContext.cloud.listReferenceDataById(updateItems.collect { it.existingItem.id } as List<Long>).map {ReferenceData referenceData ->
//						SyncTask.UpdateItemDto<ReferenceData, Map> matchItem = updateItemMap[referenceData.id]
//						return new SyncTask.UpdateItem<ReferenceData,Map>(existingItem:referenceData, masterItem:matchItem.masterItem)
//					}
//				}.start()
//
//			}
//
//
//				def queryResults = [listResults: listResults, existingItems: []]
//				if (listResults.success) {
//					def zoneCategory = "vwmare.alarm.${opts.zone.id}"
//					OperationNotification.withNewSession { session ->
//						queryResults.existingItems = OperationNotification.withCriteria {
//							eq('category', zoneCategory)
//							eq('active', true)
//							projections {
//								property('externalId')
//							}
//						}
//					}
//				}
//				return queryResults
//			}.then { queryResults ->
//				def listResults = queryResults.listResults
//				def existingItems = queryResults.existingItems
//				def syncLists = [:]
//				if(listResults.success) {
//
//					def matchFunction = { morpheusItem, Map cloudItem ->
//						morpheusItem == cloudItem?.externalId
//					}
//					def objList = listResults?.alarms
//					syncLists = ComputeUtility.buildSyncLists(existingItems, objList, matchFunction)
//				}
//				return syncLists
//			}.then { syncLists ->
//				OperationNotification.withNewSession { session ->
//					while(syncLists.addList?.size() > 0) {
//						List chunkedAddList = syncLists.addList.take(50)
//						syncLists.addList = syncLists.addList.drop(50)
//						addMissingAlarms(opts.zone, chunkedAddList)
//					}
//				}
//				return syncLists
//			}.then { syncLists ->
//				//update list
//				OperationNotification.withNewSession { session ->
//					while (syncLists.updateList?.size() > 0) {
//						List chunkedUpdateList = syncLists.updateList.take(50)
//						syncLists.updateList = syncLists.updateList.drop(50)
//						//log.info("Updating Vms for VMWare Cloud")
//						updateMatchedAlarms(opts.zone, chunkedUpdateList)
//					}
//				}
//				return syncLists
//			}.then { syncLists ->
//				OperationNotification.withNewSession { session ->
//					while (syncLists.removeList?.size() > 0) {
//						List chunkedRemoveList = syncLists.removeList.take(50)
//						syncLists.removeList = syncLists.removeList.drop(50)
//						removeMissingAlarms(opts.zone, chunkedRemoveList)
//					}
//				}
//				return syncLists
//			}.onError{ Exception ex ->
//				log.error("Error Caching Alarms in Vmware Cloud {} - {}",opts.zone.id,ex.message,ex)
//				return false
//			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected addMissingAlarms(ComputeZone zone, addList) {
//		def zoneCategory = "vwmare.alarm.${zone.id}"
//		addList?.each { cloudItem ->
//			//configure it and send to ops service
//			def alarmConfig = [account:zone.owner, category:zoneCategory, name:cloudItem.alarm.name,
//			                   eventKey:cloudItem.key, externalId:cloudItem.externalId, acknowledged:cloudItem.acknowledged,
//			                   acknowledgedDate:cloudItem.acknowledgedTime, acknowledgedByUser:cloudItem.acknowledgedByUser,
//			                   status:translateStatus(cloudItem.status), statusMessage:cloudItem.alarm.description, startDate:cloudItem.time,
//			                   resourceName:cloudItem.entity?.name, resolveMessage:cloudItem.alarm.action,
//			                   refStatus:translateStatus(cloudItem.entity?.status), configStatus:translateStatus(cloudItem.entity?.configStatus),
//			                   uniqueId:cloudItem.key, zoneId:zone.id, zoneName:zone.name]
//			//lookup ref type and id?
//			def refMatch = findManagedObject(zone, cloudItem.entity?.type, cloudItem.entity?.id)
//			if(refMatch && refMatch.refType && refMatch.refId) {
//				alarmConfig.refType = refMatch.refType
//				alarmConfig.refId = refMatch.refId
//			}
//			def results = operationEventService.createNotification(alarmConfig.account, alarmConfig)
//		}
//	}
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected updateMatchedAlarms(ComputeZone zone, updateList) {
//		def zoneCategory = "vwmare.alarm.${zone.id}"
//		def alarms = OperationNotification.where{category == zoneCategory && active == true && externalId in updateList.collect{it.existingItem}}.list()?.collectEntries{[(it.externalId):it]}
//
//		updateList.each { update ->
//			def existingItem = alarms[update.existingItem]
//			def doSave = false
//			if(existingItem) {
//				if(existingItem.refType == null) {
//					def refMatch = findManagedObject(zone, update.masterItem.entity?.type, update.masterItem.entity?.id)
//					if(refMatch && refMatch.refType && refMatch.refId) {
//						existingItem.refType = refMatch.refType
//						existingItem.refId = refMatch.refId
//						doSave = true
//					}
//				}
//				if(existingItem.acknowledged != true && existingItem.acknowledged != update.masterItem.acknowledged) {
//					existingItem.acknowledged = update.masterItem.acknowledged
//					doSave = true
//				}
//				if(existingItem.acknowledgedDate != update.masterItem.acknowledgedTime) {
//					existingItem.acknowledgedDate = update.masterItem.acknowledgedTime
//					doSave = true
//				}
//				if(existingItem.acknowledgedByUser != update.masterItem.acknowledgedByUser) {
//					existingItem.acknowledgedByUser = update.masterItem.acknowledgedByUser
//					doSave = true
//				}
//				if(existingItem.resolveMessage != update.masterItem.alarm.action) {
//					existingItem.resolveMessage = update.masterItem.alarm.action
//					doSave = true
//				}
//				if(existingItem.zoneId != zone.id) {
//					existingItem.zoneId = zone.id
//					existingItem.zoneName = zone.name
//					doSave = true
//				}
//				def newStatus = translateStatus(update.masterItem.status)
//				if(existingItem.status != newStatus) {
//					existingItem.status = newStatus
//					doSave = true
//				}
//				if(doSave == true) {
//					existingItem.save(flush:true)
//				}
//			}
//
//		}
//
//	}
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected removeMissingAlarms(ComputeZone zone, removeList) {
//		def zoneCategory = "vwmare.alarm.${zone.id}"
//		def removeItems = OperationNotification.where{category == zoneCategory && active == true && externalId in removeList}.list()
//		removeItems.each { removeItem ->
//			removeItem.active = false
//			removeItem.save(flush:true)
//		}
//	}
}
