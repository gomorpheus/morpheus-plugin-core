package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Cloud
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.core.util.ComputeUtility
import io.reactivex.*

@Slf4j
class VmwareSyncUtils {

	static buildSyncLists(existingItems, masterItems, matchExistingToMasterFunc, secondaryMatchExistingToMasterFunc=null) {
		log.info "buildSyncLists: ${existingItems}, ${masterItems}"
		def rtn = [addList:[], updateList: [], removeList: []]
		try {
			existingItems?.each { existing ->
				def matches = masterItems?.findAll { matchExistingToMasterFunc(existing, it) }
				if(!matches && secondaryMatchExistingToMasterFunc != null) {
					matches = masterItems?.findAll { secondaryMatchExistingToMasterFunc(existing, it) }
				}
				if(matches?.size() > 0) {
					matches?.each { match ->
						rtn.updateList << [existingItem:existing, masterItem:match]
					}
				} else {
					rtn.removeList << existing
				}
			}
			masterItems?.each { masterItem ->
				def match = rtn?.updateList?.find {
					it.masterItem == masterItem
				}
				if(!match) {
					rtn.addList << masterItem
				}
			}
		} catch(e) {
			log.error "buildSyncLists error: ${e}", e
		}
		return rtn
	}

	static deDupeVirtualImageLocations(List<VirtualImageLocationIdentityProjection> allLocations, Cloud cloud, MorpheusContext morpheusContext) {
		log.debug "deDupeVirtualImageLocations for ${cloud}"
		try {
			def groupedLocations = allLocations.groupBy { it.externalId }
			def dupedLocations = groupedLocations.findAll { key, value -> value.size() > 1 }
			def dupeCleanup = []
			if (dupedLocations?.size() > 0)
				log.warn("removing duplicate image locations: {}", dupedLocations.collect { it.key })
			dupedLocations?.each { key, value ->
				value.eachWithIndex { row, index ->
					if (index > 0)
						dupeCleanup << row
				}
			}
			morpheusContext.virtualImage.location.remove(dupeCleanup).blockingGet()
		} catch(e) {
			log.error "Error in removingDuplicates: ${e}", e
		}
	}

	static deDupeVirtualImages(List<VirtualImage> existingItems, List<VirtualImageLocation> existingLocations, Cloud cloud, MorpheusContext morpheusContext) {
		log.debug "deDupeVirtualImages for ${cloud}"
		try {
			def groupedImages = existingItems.groupBy({ row -> row.externalId })
			def dupedImages = groupedImages.findAll{ key, value -> key != null && value.size() > 1 }
			if(dupedImages?.size() > 0)
				log.warn("removing duplicate images: {}", dupedImages.collect{ it.key })
			dupedImages?.each { key, value ->
				//each pass is set of all the images with the same external id
				def dupeCleanup = []
				value.eachWithIndex { row, index ->
					def locationMatch = existingLocations.find{ it.virtualImage.id == row.id }
					if(locationMatch == null) {
						dupeCleanup << row
						existingItems.remove(row)
					}
				}
				//cleanup
				log.info("duplicate key: ${key} total: ${value.size()} remove count: ${dupeCleanup.size()}")
				//remove the dupes
				morpheusContext.virtualImage.remove([row], cloud).blockingGet()
			}
		} catch(e) {
			log.error "Error in removingDuplicates: ${e}", e
		}
	}

	static syncVolumes(locationOrServer, ArrayList externalVolumes, Cloud cloud, MorpheusContext morpheusContext, opts=[:] ) {
		log.debug "syncVolumes for ${locationOrServer} ${externalVolumes?.size} ${cloud}"
		try {

			def serverVolumes = locationOrServer.volumes?.sort{it.displayOrder}

			def matchFunction = { morpheusItem, Map cloudItem ->
				(morpheusItem.externalId && morpheusItem.externalId == "${cloudItem.key}".toString())
			}

			def syncLists = buildSyncLists(serverVolumes, externalVolumes, matchFunction)

			def saveList = []
			syncLists.updateList?.each { updateMap ->
				log.debug "Updating ${updateMap}"
				StorageVolume existingVolume = updateMap.existingItem
				def volume = updateMap.masterItem

				volume.maxStorage = volume.size * ComputeUtility.ONE_KILOBYTE
				def save = false
				if(existingVolume.maxStorage != volume.maxStorage) {
					//update it
					existingVolume.maxStorage = volume.maxStorage
					save = true
				}
				if(volume.fileName != existingVolume.internalId) {
					existingVolume.internalId = volume.fileName
					save = true
				}
				if(volume.unitNumber != null && existingVolume.unitNumber != "${volume.unitNumber}") {
					existingVolume.unitNumber = "${volume.unitNumber}"
					save = true
				}
				if(existingVolume.datastore?.externalId != volume.datastore) {
					existingVolume.datastore = new DatastoreIdentityProjection(cloud.id, volume.datastore)
					save = true
				}
				// TODO : Controller
//				def matchController = matchStorageVolumeController(server, volume)
//				if(matchController?.id != existingVolume.controller?.id) {
//					existingVolume.controller = matchController
//					save = true
//				}
				if(save) {
					saveList << existingVolume
				}
			}

			if(saveList) {
				log.debug "Found ${saveList?.size()} volumes to update"
				morpheusContext.storageVolume.save(saveList).blockingGet()
			}

			// The removes
			if(syncLists.removeList) {
				morpheusContext.storageVolume.remove(syncLists.removeList, locationOrServer).blockingGet()
			}

			// The adds
			def newVolumes = buildNewStorageVolumes(syncLists.addList, cloud, locationOrServer, null, opts)
			if(newVolumes) {
				morpheusContext.storageVolume.create(newVolumes, locationOrServer).blockingGet()
			}
		} catch(e) {
			log.error "Error in syncVolumes: ${e}", e
		}
	}

	static buildNewStorageVolumes(volumes, cloud, locationOrServer, account, opts = [:]) {
		log.debug "buildNewStorageVolumes: ${volumes?.size()} ${cloud} ${locationOrServer} ${account} ${opts}"
		def rtn = []
		def newCounter = 0
		def existingVolumes = locationOrServer?.volumes
		def newIndex = existingVolumes?.size() ?: 0
		newCounter = newIndex

		volumes?.eachWithIndex { volume, index ->
			volume.maxStorage = volume.size * ComputeUtility.ONE_KILOBYTE
			DatastoreIdentityProjection datastore = volume.datastore ? new DatastoreIdentityProjection(cloud.id, volume.datastore) : null
			def volName = (newIndex + index) == 0 ? 'root' : 'data'
			if ((newIndex + index) > 0)
				volName = volName + ' ' + (index + newIndex)
			def volumeConfig = [
					name      : volName,
					size      : volume.maxStorage,
					rootVolume: (newIndex + index) == 0,
					deviceName: volume.deviceName ? volume.deviceName : null,
					externalId: volume.key,
					internalId: volume.fileName,
					unitNumber: "${volume.unitNumber}",
					datastore : datastore
			]

			def newVolume = buildStorageVolume(account ?: cloud.account, locationOrServer, volumeConfig, newCounter)

			// TODO : Handle controllers
//			def matchController = matchStorageVolumeController(locationOrServer, volume)
//			if (matchController) {
//				newVolume.controller = matchController
//			}

			if (opts.setDisplayOrder) {
				newVolume.displayOrder = (index + newIndex)
			}

			rtn << newVolume
			newCounter++
		}
		return rtn
	}


	static StorageVolume buildStorageVolume(account, server, volume, index, size = null) {
		log.debug "buildStorageVolume: ${account} ${server} ${volume} ${index}"
		StorageVolume storageVolume = new StorageVolume()
		storageVolume.name = volume.name
		storageVolume.account = account
		storageVolume.maxStorage = size?.toLong() ?: volume.maxStorage?.toLong() ?: volume.size?.toLong()
		if(volume.storageType)
			storageVolume.type = new StorageVolumeType(id: volume.storageType?.toLong())
		else
			storageVolume.type = new StorageVolumeType(code: 'vmware-plugin-disk')
		if(volume.externalId)
			storageVolume.externalId = volume.externalId
		if(volume.internalId)
			storageVolume.internalId = volume.internalId
		if(volume.unitNumber)
			storageVolume.unitNumber = volume.unitNumber
		storageVolume.removable = storageVolume.rootVolume != true
		storageVolume.displayOrder = volume.displayOrder ?: server?.volumes?.size() ?: 0
		storageVolume.diskIndex = index
		return storageVolume
	}

}
