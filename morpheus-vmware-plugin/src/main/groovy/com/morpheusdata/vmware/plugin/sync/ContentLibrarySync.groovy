package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.core.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.core.util.HttpApiClient
import io.reactivex.*

@Slf4j
class ContentLibrarySync {

	private Cloud cloud
	private MorpheusContext morpheusContext
	private HttpApiClient client

	public ContentLibrarySync(Cloud cloud, MorpheusContext morpheusContext, HttpApiClient client) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
		this.client = client
	}

	def execute() {
		log.debug "execute: ${cloud}"
		try {

			// TODO : Is dedupe logic needed?
//			//dedupe
//			def groupedLocations = queryResults.existingLocations.groupBy({ row -> row[1] })
//			def dupedLocations = groupedLocations.findAll{ key, value -> value.size() > 1 }
//			def dupeCleanup = []
//			if(dupedLocations?.size() > 0)
//				log.warn("removing duplicate image locations: {}", dupedLocations.collect{ it.key })
//			dupedLocations?.each { key, value ->
//				value.eachWithIndex { row, index ->
//					if(index > 0)
//						dupeCleanup << row
//				}
//			}
//			VirtualImageLocation.withNewSession { session ->
//				dupeCleanup?.each { row ->
//					def dupeResults = virtualImageService.removeVirtualImageLocation(row[3])
//					if (dupeResults.success == true)
//						queryResults.existingLocations.remove(row)
//				}
//			}
//			return queryResults
			def listResults = listContentLibraryItems()
			if(listResults.success) {
				Observable<VirtualImageLocationIdentityProjection> domainRecords = morpheusContext.virtualImage.location.listSyncProjections(cloud.id).filter { it ->
					it.virtualImage.linkedClone != true &&
							it.sharedStorage == true &&
							it.virtualImage.imageType in [ImageType.ovf,ImageType.vmdk,ImageType.iso]
				}
				SyncTask<VirtualImageLocationIdentityProjection, Map, VirtualImageLocation> syncTask = new SyncTask<>(domainRecords, listResults?.libraryItems ?: [])
				syncTask.addMatchFunction { VirtualImageLocationIdentityProjection morpheusItem, Map cloudItem ->
					morpheusItem.externalId == cloudItem.externalId &&
							(
									(cloudItem.type == 'ovf' && morpheusItem.virtualImage.imageType != ImageType.iso) ||
											(cloudItem.type == 'iso' && morpheusItem.virtualImage.imageType == ImageType.iso) ||
											(cloudItem.type == 'vm-template' && morpheusItem.virtualImage.imageType == ImageType.vmdk)
							)

				}.onDelete { removeItems ->
					removeMissingVirtualImages(removeItems)
				}.onUpdate { List<SyncTask.UpdateItem<VirtualImageLocation, Map>> updateItems ->
					log.debug "About to call updateMatchedContentLibraryItems from sync with"
					updateMatchedContentLibraryItems(updateItems)
				}.onAdd { itemsToAdd ->
					addMissingContentLibraryItemLocations(itemsToAdd)
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<VirtualImageLocationIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<VirtualImageLocationIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheusContext.virtualImage.location.listById(updateItems.collect { it.existingItem.id } as List<Long>).map {VirtualImageLocation virtualImageLocation ->
						SyncTask.UpdateItemDto<VirtualImageLocation, Map> matchItem = updateItemMap[virtualImageLocation.id]
						return new SyncTask.UpdateItem<VirtualImageLocation,Map>(existingItem:virtualImageLocation, masterItem:matchItem.masterItem)
					}
				}.start()
			}
		} catch(e) {
			log.error "Error in execute : ${e}", e
		}
	}

	def listContentLibraryItems() {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listContentLibraryItems(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, client)
		return rtn
	}

	private addMissingContentLibraryItemLocations(objList) {
		log.debug "addMissingContentLibraryItemLocations: ${objList?.size()}"

		def names = objList.collect{it.name}?.unique()
		log.debug "names are ${names}"

		def existingIds = []
		morpheusContext.virtualImage.listSyncProjections(cloud.id).filter { it ->
			it.imageType in [ImageType.ovf, ImageType.vmdk, ImageType.iso] &&
					it.name in [names] &&
					(it.systemImage == true || (!it.ownerId || it.ownerId == cloud.owner.id))
		}.blockingSubscribe { existingIds << it}
		existingIds.unique{"${it.imageType.toString()}:${it.name}"}
		def secondaryMatchFunction = { Map morpheusItem, Map cloudItem ->
			cloudItem.name == morpheusItem.name  && ((cloudItem.type == 'ovf' && morpheusItem.imageType != ImageType.iso) || (cloudItem.type == ImageType.iso && morpheusItem.imageType == ImageType.iso))
		}
		def syncLists = buildSyncLists(existingIds, objList, secondaryMatchFunction)
		//add missing
		while(syncLists.addList?.size() > 0) {
			List chunkedAddList = syncLists.addList.take(50)
			syncLists.addList = syncLists.addList.drop(50)
			addMissingContentLibraryItems(chunkedAddList)
		}
		//update list
		while(syncLists.updateList?.size() > 0) {
			List chunkedUpdateList = syncLists.updateList.take(50)
			syncLists.updateList = syncLists.updateList.drop(50)
			updateMatchedContentLibraryItems(chunkedUpdateList)
		}
		//removes?
		syncLists.removeList?.each { removeItem ->
			// println("need to remove: ${removeItem}")
		}
	}

	private addMissingContentLibraryItems(List addList) {
		log.debug "addMissingContentLibraryItems: ${addList?.size()}"
		def dsIds = addList.collect { it.storage_backings?.collect{it.datastore_id}}?.flatten()?.findAll{it != null}.unique()
		List<DatastoreIdentityProjection> datastores = []
		if(dsIds) {
			morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter {
				it.externalId in dsIds
			}.blockingSubscribe { datastores << it}
		}

		def regionCode = VmwareCloudProvider.getRegionCode(cloud)
		addList?.each { it ->
			log.debug "add new VirtualImage for ${it.name} ${it.externalId}"
			def imageConfig = [
					account    : cloud.owner,
					category   : "vmware.vsphere.image.${cloud.id}",
					name       : it.name,
					code       : "vmware.vsphere.image.${cloud.id}.${it.ref}",
					imageType  : it.type == 'iso' ? ImageType.iso : ImageType.vmdk,
					minRam     : 0L,
					status     : 'Active',
					remotePath : it.library_id,
					externalId : it.externalId,
					imageRegion: regionCode,
					internalId : it.id
			]

			def osType = new OsType(code: 'other')

			imageConfig.osType = osType
			imageConfig.platform = osType?.platform
			if(imageConfig.platform == 'windows') {
				imageConfig.isForceCustomization = true
				imageConfig.isCloudInit = false
			}
			imageConfig.virtioSupported = false

			def add = new VirtualImage(imageConfig)
			// TODO : Need to add to accounts?
			//		Account account = cloud.account
//			if(account) {
//				add.addToAccounts(account)
//			}

			def createdImage = morpheusContext.virtualImage.create(add, cloud).blockingGet()

			def locationConfig = [
					code         : "vmware.vsphere.image.${cloud.id}.${it.ref}",
					virtualImage : createdImage,
					externalId   : it.externalId,
					imageName    : it.name,
					imageRegion  : regionCode,
					sharedStorage: true
			]
			def addLocation = new VirtualImageLocation(locationConfig)
			it.storage_backings?.each { sbacking ->
				if(sbacking.datastore_id) {
					DatastoreIdentityProjection matchingDatastore = datastores?.find{it.externalId == sbacking.datastore_id}
					if(matchingDatastore) {
						addLocation.datastore = new Datastore(id: matchingDatastore.id)
					}
				}
			}

			morpheusContext.virtualImage.location.create([addLocation], cloud).blockingGet()
		}
	}

	private updateMatchedContentLibraryItems(updateList) {
		log.debug "updateMatchedContentLibraryItems ${updateList?.size()}"

		def regionCode = VmwareCloudProvider.getRegionCode(cloud)
		List<VirtualImageLocation> existingLocations = []
		def imageIds = []
		def externalIds = updateList?.findAll{ it.existingItem?.externalId }?.collect{ it.existingItem?.externalId }
		if(updateList?.find { it.existingItem instanceof VirtualImageLocationIdentityProjection}){
			imageIds = updateList?.findAll{ it.existingItem.virtualImage?.id }?.collect{ it.existingItem.virtualImage?.id }

			List<Long> locationIds = updateList?.findAll { it.existingItem.id }?.collect { it.existingItem.id } as List<Long>
			morpheusContext.virtualImage.location.listById(locationIds).blockingSubscribe { existingLocations << it }
		} else {
			imageIds = updateList?.findAll{ it.existingItem.id }?.collect{ it.existingItem.id }
		}

		List<VirtualImage> existingItems = []
		def dsIds = updateList.collect { it.masterItem.storage_backings?.collect{it.datastore_id}}?.flatten()?.findAll{it != null}.unique()
		List<DatastoreIdentityProjection> datastores = []
		if(dsIds) {
			morpheusContext.cloud.datastore.listSyncProjections(cloud.id).filter {
				it.externalId in dsIds
			}.blockingSubscribe { datastores << it}
		}
		if(imageIds && externalIds) {
			def tmpProjs = []
			morpheusContext.virtualImage.listSyncProjections(cloud.id).filter { it ->
				it.id in imageIds || (it.systemImage == false && it.externalId != null && it.externalId in externalIds)
			}.blockingSubscribe { tmpProjs << it}

			morpheusContext.virtualImage.listById(tmpProjs.collect { it.id }).blockingSubscribe { it ->
				if(!(it.id in imageIds)) {
					// must have been hitting the 2nd || condition above in listSyncProjections
					if(it.imageLocations?.size() > 0) {
						existingItems << it
					} else {
						existingItems << it
					}
				}
			}
		} else if(imageIds) {
			morpheusContext.virtualImage.listByIds(imageIds).blockingSubscribe { existingItems << it }
		}

		// TODO : Dedupe logic needed?
//		//dedupe
//		def groupedImages = existingItems.groupBy({ row -> row.externalId })
//		def dupedImages = groupedImages.findAll{ key, value -> key != null && value.size() > 1 }
//		if(dupedImages?.size() > 0)
//			log.warn("removing duplicate images: {}", dupedImages.collect{ it.key })
//		dupedImages?.each { key, value ->
//			//each pass is set of all the images with the same external id
//			def dupeCleanup = []
//			value.eachWithIndex { row, index ->
//				def locationMatch = existingLocations.find{ it.virtualImage.id == row.id }
//				if(locationMatch == null) {
//					dupeCleanup << row
//					existingItems.remove(row)
//				}
//			}
//			//cleanup
//			log.info("duplicate key: ${key} total: ${value.size()} remove count: ${dupeCleanup.size()}")
//			//remove the dupes
//			deleteSyncedVirtualImages([key], dupeCleanup, [], zone, [hardDelete:true])
//		}

		//updates
		def imagesToSave = []
		def imageLocationsToSave = []

		updateList?.each { update ->
			log.debug "updating ${update.existingItem}"
			def matchedTemplate = update.masterItem

			VirtualImageLocation imageLocation = existingLocations?.find { update.existingItem instanceof VirtualImageLocationIdentityProjection && it.id == update.existingItem.id }

			if(imageLocation) {
				imageLocation = imageLocationsToSave.find { it.id == imageLocation.id } ?: imageLocation // See if it's already scheduled for a save

				def saveLocation = false
				def saveImage = false
				VirtualImage tmpImage
				Datastore datastore

				matchedTemplate.storage_backings?.each { sbacking ->
					if(sbacking.datastore_id) {
						DatastoreIdentityProjection matchingDatastore = datastores?.find{it.externalId == sbacking.datastore_id}

						if(matchingDatastore) {
							datastore = new Datastore(id: matchingDatastore.id)
						}
					}
				}
				if(imageLocation.imageName != matchedTemplate.name) {
					imageLocation.imageName = matchedTemplate.name
					tmpImage = loadImage(imageLocation, imagesToSave)
					if(tmpImage && tmpImage.refId == imageLocation.refId.toString()) {
						tmpImage.name = matchedTemplate.name
						saveImage = true
					}
					saveLocation = true
				}
				if(imageLocation.datastore?.id != datastore?.id) {
					imageLocation.datastore = datastore
					saveLocation = true
				}
				if(imageLocation.code == null) {
					imageLocation.code = "vmware.vsphere.image.${cloud.id}.${matchedTemplate.externalId}"
					saveLocation = true
				}
				if(imageLocation.externalId != matchedTemplate.externalId) {
					imageLocation.externalId = matchedTemplate.externalId
					saveLocation = true
				}

				if(imageLocation.virtualImage.deleted) {
					tmpImage = tmpImage ?: loadImage(imageLocation, imagesToSave)
					if(tmpImage) {
						tmpImage.deleted = false
						saveImage = true
					}
				} else {
					//this would mean we need to fix the display order. if there is more than one volume AND they all have the same display order
					// TODO : Volumes
//					if(imageLocation?.volumes?.size() > 1 && imageLocation?.volumes?.every({vol -> vol.displayOrder == 0})) {
//						imageLocation.volumes.sort {a, b ->
//							if (a.rootVolume) {
//								return -1
//							}
//							return a.controllerMountPoint <=> b.controllerMountPoint
//						}.eachWithIndex { vol, index ->
//							vol.displayOrder = index;
//							vol.save()
//						}
//					}
				}
				if(saveLocation) {
					imageLocationsToSave << imageLocation
				}
				if(saveImage) {
					imagesToSave << tmpImage
				}
			} else {
				VirtualImage image = existingItems?.find { it.externalId == matchedTemplate.externalId || it.name == matchedTemplate.name }
				log.debug "couldn't find VirtualImageLocation for ${matchedTemplate.externalId} so adding with image ${image}"
				if(image) {
					image = imagesToSave.find { it.id == image.id } ?: image // See if it's already scheduled for a save

					//if we matched by virtual image and not a location record we need to create that location record
					def locationConfig = [
							code:"vmware.vsphere.image.${cloud.id}.${matchedTemplate.externalId}",
							externalId:matchedTemplate.externalId,
	                        internalId:matchedTemplate.externalId,
							virtualImage: image,
							imageName:matchedTemplate.name,
							imageRegion:regionCode,
							sharedStorage:true
					]
					def addLocation = new VirtualImageLocation(locationConfig)
					matchedTemplate.storage_backings?.each { sbacking ->
						if(sbacking.datastore_id) {
							DatastoreIdentityProjection matchingDatastore = datastores?.find{it.externalId == sbacking.datastore_id}
							if(matchingDatastore) {
								addLocation.datastore = new Datastore(id: matchingDatastore.id)
							}
						}
					}
					imageLocationsToSave << addLocation

					//tmp fix
					if(!image.account && !image.systemImage)
						image.account = cloud.owner
					image.deleted = false
					imagesToSave << image
				}
			}
		}

		log.debug "VirtualImage to save ${imagesToSave?.size()}"
		if(imagesToSave) {
			morpheusContext.virtualImage.save(imagesToSave).blockingGet()
		}
		log.debug "VirtualImageLocations to save ${imageLocationsToSave?.size()}"
		if(imageLocationsToSave) {
			morpheusContext.virtualImage.location.save(imageLocationsToSave, cloud).blockingGet()
		}
	}

	private removeMissingVirtualImages(removeList) {
		log.debug "removeMissingVirtualImages: ${removeList?.size()}"
		morpheusContext.virtualImage.location.remove(removeList).blockingGet()
	}

	private loadImage(VirtualImageLocation location, imagesToSave) {
		def tmpImage
		if(imagesToSave) {  // See if it is already scheduled for a save
			imagesToSave.find { it.id == location.virtualImage.id}
		}
		if(!tmpImage) {
			morpheusContext.virtualImage.listByIds([location.virtualImage.id]).blockingSubscribe { tmpImage == it }
			return tmpImage
		}
	}

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
}
