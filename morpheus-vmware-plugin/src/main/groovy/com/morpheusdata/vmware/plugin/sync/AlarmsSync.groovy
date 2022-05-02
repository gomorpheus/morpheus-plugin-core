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
			def listResults = VmwareCloudProvider.listAlarms(cloud)
			if(listResults.success) {
				Observable<OperationNotificationIdentityProjection> domainRecords = morpheusContext.operationNotification.listSyncProjections("vwmare.alarm.${cloud.id}")
				SyncTask<OperationNotificationIdentityProjection, Map, OperationNotification> syncTask = new SyncTask<>(domainRecords, listResults?.alarms ?: [])
				syncTask.addMatchFunction { OperationNotificationIdentityProjection domainObject, Map apiItem ->
					domainObject.externalId == apiItem.externalId
				}.onDelete { removeItems ->
					removeMissingAlarms(removeItems)
				}.onUpdate { List<SyncTask.UpdateItem<OperationNotification, Map>> updateItems ->
					updateMatchedAlarms(updateItems)
				}.onAdd { itemsToAdd ->
					addMissingAlarms(itemsToAdd)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<OperationNotificationIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<OperationNotificationIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.operationNotification.listById(updateItems.collect { it.existingItem.id } as List<Long>).map {OperationNotification operationNotification ->
						SyncTask.UpdateItemDto<OperationNotification, Map> matchItem = updateItemMap[operationNotification.id]
						return new SyncTask.UpdateItem<OperationNotification,Map>(existingItem:operationNotification, masterItem:matchItem.masterItem)
					}
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	private addMissingAlarms(addList) {
		log.debug "addMissingAlarms: ${addList?.size()}"
		def zoneCategory = "vwmare.alarm.${cloud.id}"
		def itemsToAdd = []
		addList?.each { cloudItem ->
			//configure it and send to ops service
			def alarmConfig = [
					account           : cloud.owner,
					category          : zoneCategory,
					name              : cloudItem.alarm.name,
					eventKey          : cloudItem.key,
					externalId        : cloudItem.externalId,
					acknowledged      : cloudItem.acknowledged,
					acknowledgedDate  : cloudItem.acknowledgedTime,
					acknowledgedByUser: cloudItem.acknowledgedByUser,
					status            : translateStatus(cloudItem.status),
					statusMessage     : cloudItem.alarm.description,
					startDate         : cloudItem.time,
					resourceName      : cloudItem.entity?.name,
					uniqueId          : cloudItem.key
			]
			//lookup ref type and id?
			def refMatch = findManagedObject(cloudItem.entity?.type, cloudItem.entity?.id)
			if(refMatch && refMatch.refType && refMatch.refId) {
				alarmConfig.refType = refMatch.refType
				alarmConfig.refId = refMatch.refId
			}
			itemsToAdd << new OperationNotification(alarmConfig)
		}
		if(itemsToAdd) {
			morpheusContext.operationNotification.create(itemsToAdd).blockingGet()
		}
	}

	private updateMatchedAlarms(updateList) {
		log.debug "updateMatchedAlarms ${updateList?.size()}"
		def zoneCategory = "vwmare.alarm.${cloud.id}"

		def updateItems = []
		updateList.each { update ->
			def existingItem = update.existingItem
			def masterItem = update.masterItem
			def doSave = false
			if(existingItem) {
				if(existingItem.refType == null) {
					def refMatch = findManagedObject(masterItem.entity?.type, masterItem.entity?.id)
					if(refMatch && refMatch.refType && refMatch.refId) {
						existingItem.refType = refMatch.refType
						existingItem.refId = refMatch.refId
						doSave = true
					}
				}
				if(existingItem.acknowledged != true && existingItem.acknowledged != masterItem.acknowledged) {
					existingItem.acknowledged = masterItem.acknowledged
					doSave = true
				}
				if(existingItem.acknowledgedDate != masterItem.acknowledgedTime) {
					existingItem.acknowledgedDate = masterItem.acknowledgedTime
					doSave = true
				}
				if(existingItem.acknowledgedByUser != masterItem.acknowledgedByUser) {
					existingItem.acknowledgedByUser = masterItem.acknowledgedByUser
					doSave = true
				}
				def newStatus = translateStatus(masterItem.status)
				if(existingItem.status != newStatus) {
					existingItem.status = newStatus
					doSave = true
				}
				if(doSave == true) {
					updateItems << existingItem
				}
			}
		}

		log.debug "Alarms to update ${updateItems?.size()}"
		if(updateItems) {
			morpheusContext.operationNotification.save(updateItems).blockingGet()
		}

	}

	private removeMissingAlarms(removeList) {
		def zoneCategory = "vwmare.alarm.${cloud.id}"
		morpheusContext.operationNotification.remove(removeList).blockingGet()
	}

	private translateStatus(String status) {
		def rtn
		if(status == 'yellow')
			rtn = 'warning'
		else if(status == 'red')
			rtn = 'error'
		else if(status == 'green')
			rtn = 'ok'
		else
			rtn = 'unknown'
		return rtn
	}

	private findManagedObject(String type, String id) {
		//find the matching item in our db
		def rtn = [refType:null, refId:null]
		if(type && id) {
			def assignToZone = false
			switch(type) {
				case 'StoragePod':
					def match
					morpheusContext.cloud.datastore.listSyncProjections(cloud.id)
							.filter { it.externalId == id && it.type == 'cluster' }
							.blockingSubscribe { match == it }
					if(match) {
						rtn.refType = 'datastore'
						rtn.refId = match.id
					} else {
						assignToZone = true
					}
					break
				case 'Datastore':
					def match
					morpheusContext.cloud.datastore.listSyncProjections(cloud.id)
							.filter { it.externalId == id && it.type != 'cluster' }
							.blockingSubscribe { match == it }
					if(match) {
						rtn.refType = 'datastore'
						rtn.refId = match.id
					} else {
						assignToZone = true
					}
					break
				case 'VirtualMachine':
				case 'HostSystem':
					def match
					morpheusContext.computeServer.listSyncProjections(cloud.id)
							.filter { it.externalId == id }
							.blockingSubscribe { match == it }
					if(match) {
						rtn.refType = 'computeServer'
						rtn.refId = match.id
					} else {
						assignToZone = true
					}
					break
			}
			//fallback to zone
			if(assignToZone) {
				rtn.refType = 'computeZone'
				rtn.refId = cloud.id
			}
		}
		return rtn
	}
}
