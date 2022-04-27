package com.morpheusdata.vmware.plugin.sync

import com.morpheusdata.model.projection.StorageControllerIdentityProjection
import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import com.morpheusdata.core.*
import com.morpheusdata.core.util.*
import io.reactivex.*

@Slf4j
class TemplatesSync {

	private Cloud cloud
	private MorpheusContext morpheusContext

	public TemplatesSync(Cloud cloud, MorpheusContext morpheusContext) {
		this.cloud = cloud
		this.morpheusContext = morpheusContext
	}

	def execute() {
		log.debug "execute TemplatesSync: ${cloud}"

		try {
	//		lock = lockService.acquireLock(lockKey.toString(), [timeout:(300l * 1000l), ttl:(60l * 60l * 1000l)])
			def listResults = VmwareCloudProvider.listTemplates(cloud)
			if(listResults.success) {
				// First pass through.. dedupe logic
				removeDuplicates()
				// Now.. get on with the sync
				Observable domainRecords = morpheusContext.virtualImage.location.listSyncProjections(cloud.id).filter { VirtualImageLocationIdentityProjection projection ->
					return projection.virtualImage.linkedClone != true && !projection.sharedStorage && projection.virtualImage.imageType in [ImageType.ovf, ImageType.vmdk]
				}
				SyncTask<VirtualImageLocationIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, listResults?.templates)
				syncTask.addMatchFunction { VirtualImageLocationIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<VirtualImageLocationIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<VirtualImageLocationIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheusContext.virtualImage.location.listById(updateItems?.collect { it.existingItem.id }).map { VirtualImageLocation virtualImageLocation ->
						SyncTask.UpdateItemDto<VirtualImageLocationIdentityProjection, Map> matchItem = updateItemMap[virtualImageLocation.id]
						return new SyncTask.UpdateItem<Datastore, Map>(existingItem: virtualImageLocation, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
					addMissingVirtualImageLocations(itemsToAdd)
				}.onUpdate { List<SyncTask.UpdateItem<VirtualImageLocation, Map>> updateItems ->
					updateMatchedVirtualImages(updateItems)
				}.onDelete { removeItems ->
					removeMissingVirtualImages(removeItems)
				}.start()
			}
		} catch(e) {
			log.error "Error in execute of TemplatesSync: ${e}", e
		}
	}

	def addMissingVirtualImageLocations(List objList) {
		log.debug "addMissingVirtualImageLocations: ${objList?.size()}"

		def names = objList.collect{it.name}?.unique()
		List<VirtualImageIdentityProjection> existingItems = []
		def allowedImageTypes = [ImageType.ovf, ImageType.vmdk]

		def osTypes = []
		morpheusContext.osType.listAll().blockingSubscribe { osTypes << it }

		def uniqueIds = [] as Set
		Observable domainRecords = morpheusContext.virtualImage.listSyncProjections(cloud.id).filter { VirtualImageIdentityProjection proj ->
			def include = proj.imageType in allowedImageTypes && proj.name in names && (proj.systemImage || (!proj.ownerId || proj.ownerId == cloud.owner.id))
			if(include) {
				def uniqueKey = "${proj.imageType.toString()}:${proj.name}".toString()
				if(!uniqueIds.contains(uniqueKey)) {
					uniqueIds << uniqueKey
					return true
				}
			}
			return false
		}
		SyncTask<VirtualImageIdentityProjection, Map, VirtualImage> syncTask = new SyncTask<>(domainRecords, objList)
		syncTask.addMatchFunction { VirtualImageIdentityProjection domainObject, Map cloudItem ->
			cloudItem.name == domainObject.name
		}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map>> updateItems ->
			Map<Long, SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
			morpheusContext.virtualImage.listById(updateItems?.collect { it.existingItem.id }).map { VirtualImage virtualImage ->
				SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map> matchItem = updateItemMap[virtualImage.id]
				return new SyncTask.UpdateItem<VirtualImage, Map>(existingItem: virtualImage, masterItem: matchItem.masterItem)
			}
		}.onAdd { itemsToAdd ->
			addMissingVirtualImages(itemsToAdd, osTypes)
		}.onUpdate { List<SyncTask.UpdateItem<VirtualImage, Map>> updateItems ->
			// Found the VirtualImage for this location.. just need to create the location
			addMissingVirtualImageLocationsForImages(updateItems)
		}.start()
	}

	private addMissingVirtualImages(List addList, List osTypes) {
		log.debug "addMissingVirtualImages ${addList?.size()}"
		Account account = cloud.account
		def regionCode = VmwareCloudProvider.getRegionCode(cloud)
		def adds = []
		def addNames = []
		Map<String, ArrayList> imageRefVolumesMap = [:]
		Map<String, ArrayList> imageRefControllersMap = [:]
		addList?.each {
			addNames << it.name
			def imageConfig = [
					account    : account,
					category   : "vmware.vsphere.image.${cloud.id}",
					name       : it.name,
					code       : "vmware.vsphere.image.${cloud.id}.${it.ref}",
					imageType  : ImageType.vmdk,
					minRam     : it.summary?.config?.memorySizeMB * ComputeUtility.ONE_MEGABYTE,
					status     : 'Active',
					remotePath : it.summary?.config?.vmPathName,
					externalId : it.ref,
					imageRegion: regionCode,
					internalId : it.config?.uuid
			]
			def osTypeCode = VmwareComputeUtility.getMapVmwareOsType(it.config.guestId)
			log.debug "cacheTemplates osTypeCode: ${osTypeCode}"
			osTypeCode = osTypeCode ?: 'other'
			def osType = osTypes.find { it.code == osTypeCode }
			log.debug "osType: ${osType}"
			imageConfig.osType = osType
			imageConfig.platform = osType?.platform
			if(imageConfig.platform == 'windows') {
				imageConfig.isForceCustomization = true
				imageConfig.isCloudInit = false
			}
			imageConfig.virtioSupported = false

//			it.controllers?.each { controller ->
//				def controllerConfig = [name:controller.name, description:controller.description, controllerKey:"${controller.key}",
//				                        type:StorageControllerType.findByCode(controller.type), unitNumber:"${controller.unitNumber}", busNumber:"${controller.busNumber}",
//				                        uniqueId:"vmware.vsphere.controller.${zone.id}.${it.ref}.${controller.key}"]
//				def newController = new StorageController(controllerConfig)
//				newController.save()
//				add.addToControllers(newController)
//			}
			
			def add = new VirtualImage(imageConfig)
			imageRefVolumesMap[add.externalId] = it.volumes
			imageRefControllersMap[add.externalId] = it.controllers
			adds << add
		}

		// Create em all!
		log.debug "About to create ${adds.size()} virtualImages"
		morpheusContext.virtualImage.create(adds, cloud).blockingGet()

		// Fetch the images that we just created
		def imageMap = [:]
		morpheusContext.virtualImage.listSyncProjections(cloud.id).filter { VirtualImageIdentityProjection proj ->
			addNames.contains(proj.name)
		}.blockingSubscribe { imageMap[it.externalId] = it }

		def addedControllers = []
		//add the controllers
		imageRefControllersMap?.each { externalId, controllers ->
			VirtualImageIdentityProjection proj = imageMap[externalId]
			if (proj) {
				def controllersToAdd = []
				controllers?.eachWithIndex { controller, index ->
					def controllerConfig = [
							name:controller.name,
							description:controller.description,
							controllerKey:"${controller.key}",
							type: new StorageControllerType(code: controller.type),
							unitNumber:"${controller.unitNumber}",
							busNumber:"${controller.busNumber}",
							uniqueId:"vmware.vsphere.controller.${cloud.id}.${externalId}.${controller.key}"
					]


					def newController = new StorageController(controllerConfig)
					controllersToAdd << newController
					addedControllers << newController
				}
				if (controllersToAdd) {
					log.debug "Adding ${controllersToAdd?.size()} volumes to ${proj.externalId} in cloud ${cloud.id}"
					morpheusContext.storageController.create(controllersToAdd, proj).blockingGet()
				}
			} else {
				log.warn "Could not find VirtualImage that was just created with externalId ${externalId} in cloud ${cloud.id}"
			}
		}

		// Now add the volumes
		imageRefVolumesMap?.each { externalId, volumes ->
			VirtualImageIdentityProjection proj = imageMap[externalId]
			if (proj) {
				def volumesToAdd = []
				volumes?.eachWithIndex { volume, index ->
					def volumeConfig = [
							account     : account,
							name        : volume.name,
							maxStorage  : (volume.size * ComputeUtility.ONE_KILOBYTE),
							displayOrder: index,
							diskIndex   : index,
							type        : new StorageVolumeType(code: 'vmware-plugin-disk'),
							unitNumber  : "${volume.unitNumber}",
							externalId  : volume.key,
							internalId  : volume.fileName,
							rootVolume  : (index == 0),
							uniqueId    : "vmware.vsphere.volume.${cloud.id}.${externalId}.${volume.key}"
					]
					if(volume.controllerKey && addedControllers.find{ controller -> controller.controllerKey == "${volume.controllerKey}"})
						volumeConfig.controller = new StorageControllerIdentityProjection([controllerKey:  volume.controllerKey])

					def newVolume = new StorageVolume(volumeConfig)
					volumesToAdd << newVolume
				}
				if (volumesToAdd) {
					log.debug "Adding ${volumesToAdd?.size()} volumes to ${proj.externalId} in cloud ${cloud.id}"
					morpheusContext.storageVolume.create(volumesToAdd, proj).blockingGet()
				}
			} else {
				log.warn "Could not find VirtualImage that was just created with externalId ${externalId} in cloud ${cloud.id}"
			}
		}

		// Now add the locations
		def locationAdds = []
		adds?.each { add ->
			log.debug "Adding location for ${add.externalId}"
			def virtualImage = imageMap[add.externalId]
			if(virtualImage) {
				def locationConfig = [
						virtualImage: virtualImage,
						code        : "vmware.vsphere.image.${cloud.id}.${add.externalId}",
						externalId  : add.externalId,
						imageName   : add.name,
						imageRegion : regionCode,
				]
				VirtualImageLocation location = new VirtualImageLocation(locationConfig)
				locationAdds << location
			} else {
				log.warn "Unable to find virtualImage for ${add.externalId}"
			}
		}

		if(locationAdds) {
			log.debug "About to create ${locationAdds.size()} locations"
			morpheusContext.virtualImage.location.create(locationAdds, cloud).blockingGet()
		}
	}

	private addMissingVirtualImageLocationsForImages(List<SyncTask.UpdateItem<VirtualImage, Map>> addItems) {
		log.debug "addMissingVirtualImageLocationsForImages ${addItems?.size()}"

		def regionCode = VmwareCloudProvider.getRegionCode(cloud)
		def locationAdds = []
		addItems?.each { add ->
			VirtualImage virtualImage = add.existingItem
			def locationConfig = [
					virtualImage: virtualImage,
					code        : "vmware.vsphere.image.${cloud.id}.${virtualImage.externalId}",
					externalId  : virtualImage.externalId,
					imageName   : virtualImage.name,
					imageRegion : regionCode,
			]
			VirtualImageLocation location = new VirtualImageLocation(locationConfig)
			locationAdds << location
		}

		if(locationAdds) {
			log.debug "About to create ${locationAdds.size()} locations"
			morpheusContext.virtualImage.location.create(locationAdds, cloud).blockingGet()
		}
	}

	private updateMatchedVirtualImages(List<SyncTask.UpdateItem<VirtualImageLocation, Map>> updateList) {
		log.debug "updateMatchedVirtualImages: ${updateList?.size()}"

		List<VirtualImageLocation> existingLocations = updateList?.collect { it.existingItem }
		def regionCode = VmwareCloudProvider.getRegionCode(cloud)
		def isPublicImage = false

		def imageIds = updateList?.findAll{ it.existingItem.virtualImage?.id }?.collect{ it.existingItem.virtualImage.id }
		def externalIds = updateList?.findAll{ it.existingItem.externalId }?.collect{ it.existingItem.externalId }
		List<VirtualImage> existingItems = []
		if(imageIds && externalIds) {
			def tmpImgProjs = []
			morpheusContext.virtualImage.listSyncProjections(cloud.id).filter { img ->
				!img.systemImage && img.externalId != null && img.externalId in externalIds
			}.blockingSubscribe { tmpImgProjs << it }
			if(tmpImgProjs) {
				morpheusContext.virtualImage.listById(tmpImgProjs.collect { it.id }).filter { img ->
					img.imageLocations.size() == 0
				}.blockingSubscribe { existingItems << it }
			}
		} else if(imageIds) {
			morpheusContext.virtualImage.listById(imageIds).blockingSubscribe { existingItems << it }
		}
		//dedupe
		VmwareSyncUtils.deDupeVirtualImages(existingItems, existingLocations, cloud, morpheusContext)

		Map<VirtualImageLocation, ArrayList> locationVolumesMap = [:]
		//updates
		updateList?.each { update ->
			def matchedTemplate = update.masterItem
			VirtualImageLocation imageLocation = existingLocations?.find { it.id == update.existingItem.id }
			if(imageLocation) {
				def save = false
				def saveImage = false
				if(imageLocation.imageName != matchedTemplate.name) {
					imageLocation.imageName = matchedTemplate.name
//					if(imageLocation.virtualImage.refId == imageLocation.refId.toString()) {
//						imageLocation.virtualImage.name = matchedTemplate.name
//						imageLocation.virtualImage.save()
//					}
					save = true
				}
				if(imageLocation.imageRegion != regionCode) {
					imageLocation.imageRegion = regionCode
					save = true
				}
				if(imageLocation.code == null) {
					imageLocation.code = "vmware.vsphere.image.${cloud.id}.${matchedTemplate.ref}"
					save = true
				}
				if(imageLocation.externalId != matchedTemplate.ref) {
					imageLocation.externalId = matchedTemplate.ref
					save = true
				}
				if(matchedTemplate.config?.uuid && imageLocation.internalId != matchedTemplate.config?.uuid) {
					imageLocation.internalId = matchedTemplate.config.uuid
					save = true
				}
//				if(matchedTemplate.controllers) {
//					def start = new Date()
//					def changed = vmwareProvisionService.syncControllers(zone, imageLocation, matchedTemplate.controllers, false)
//					if(changed == true)
//						save = true
//				}
				if(matchedTemplate.volumes) {
					locationVolumesMap[imageLocation] = matchedTemplate.volumes
				}
//				if(imageLocation.virtualImage.deleted) {
//					imageLocation.virtualImage.deleted = false
//					saveImage = true
//				} else {
//					//this would mean we need to fix the display order. if there is more than one volume AND they all have the same display order
//					if(imageLocation?.volumes?.size() > 1 && imageLocation?.volumes?.every({vol -> vol.displayOrder == 0})) {
//						imageLocation.volumes.sort {a, b ->
//							if (a.rootVolume) {
//								return -1
//							}
//							return a.controllerMountPoint <=> b.controllerMountPoint TODO: Need controller support for this
//						}.eachWithIndex { vol, index ->
//							vol.displayOrder = index;
//							vol.save()
//						}
//					}
//				}
//				if(imageLocation.virtualImage?.isPublic != isPublicImage) {
//					imageLocation.virtualImage.isPublic = isPublicImage
//					imageLocation.isPublic = isPublicImage
//					saveImage = true
//					save = true
//				}
				if(save) {
					morpheusContext.virtualImage.location.save([imageLocation], cloud).blockingGet() // TODO: Save these in a batch
				}
				if(saveImage) {
//					imageLocation.virtualImage?.save(flush:true)
				}
			} else {
				VirtualImage image = existingItems?.find { it.externalId == matchedTemplate.ref || it.name == matchedTemplate.name }
				if(image) {
					//if we matched by virtual image and not a location record we need to create that location record
					def locationConfig = [
							code        : "vmware.vsphere.image.${cloud.id}.${matchedTemplate.ref}",
							externalId  : matchedTemplate.ref,
							internalId  : matchedTemplate.config?.uuid,
							imageName   : matchedTemplate.name,
							imageRegion : regionCode,
							virtualImage: new VirtualImageIdentityProjection(id: image.id)
					]
					def addLocation = new VirtualImageLocation(locationConfig)
					morpheusContext.virtualImage.location.save([addLocation], cloud).blockingGet()

					//tmp fix
					if(!image.owner && !image.systemImage)
						image.ownerId = cloud.owner.id
//					image.deleted = false
					image.isPublic = isPublicImage
					morpheusContext.virtualImage.save([image], cloud).blockingGet()  // TODO: Save these in a batch
				}

			}
		}

		// Sync the volumes for each
		locationVolumesMap?.each { VirtualImageLocation location, volumes ->
			VmwareSyncUtils.syncVolumes(location, volumes, cloud, morpheusContext)
		}

	}

	private removeDuplicates() {
		log.debug "removeDuplicates for ${cloud}"
		List<VirtualImageLocationIdentityProjection> allLocations = []
		morpheusContext.virtualImage.location.listSyncProjections(cloud.id).filter { VirtualImageLocationIdentityProjection projection ->
			return projection.virtualImage.linkedClone != true && !projection.sharedStorage && projection.virtualImage.imageType in [ImageType.ovf, ImageType.vmdk]
		}.blockingSubscribe { allLocations << it }
		VmwareSyncUtils.deDupeVirtualImageLocations(allLocations, cloud, morpheusContext)
	}

	private removeMissingVirtualImages(List removeList) {
		log.debug "removeMissingVirtualImages: ${removeList?.size()}"

		morpheusContext.virtualImage.location.remove(removeList).blockingGet()
	}

}
