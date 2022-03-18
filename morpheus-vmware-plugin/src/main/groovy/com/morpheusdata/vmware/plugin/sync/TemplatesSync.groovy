package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Datastore
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.VirtualImageLocation
import com.morpheusdata.model.ImageType
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
		log.debug "cacheTemplates: ${cloud}"
		def lock
		def configMap = cloud.getConfigMap()
		def datacenter = configMap.datacenter
//		def lockKey = "zone.refresh.images.vmware.${datacenter}"
//		def regionCode = getRegionCode(opts.zone)
//		lock = lockService.acquireLock(lockKey.toString(), [timeout:(300l * 1000l), ttl:(60l * 60l * 1000l)])
		def listResults = VmwareCloudProvider.listTemplates(cloud)
		if(listResults.success) {
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

	// TODO: Do we still need dedupe logic
//				//dedupe
//				def groupedLocations = queryResults.existingLocations.groupBy({ row -> row[1] })
//				def dupedLocations = groupedLocations.findAll{ key, value -> value.size() > 1 }
//				def dupeCleanup = []
//				if(dupedLocations?.size() > 0)
//					log.warn("removing duplicate image locations: {}", dupedLocations.collect{ it.key })
//				dupedLocations?.each { key, value ->
//					value.eachWithIndex { row, index ->
//						if(index > 0)
//							dupeCleanup << row
//					}
//				}
//				VirtualImageLocation.withNewSession { session ->
//					dupeCleanup?.each { row ->
//						def dupeResults = virtualImageService.removeVirtualImageLocation(row[3])
//						if (dupeResults.success == true)
//							queryResults.existingLocations.remove(row)
//					}
//				}
//				return queryResults
		try {

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
//			it.volumes?.eachWithIndex { volume, index ->
//				def volumeConfig = [account:owner, name:volume.name, maxStorage:(volume.size * ComputeUtility.ONE_KILOBYTE),
//				                    displayOrder:index, deviceName:(volume.deviceName ?: vmwareProvisionService.getDiskName(index)), type:StorageVolumeType.findByCode('vmware-disk'),
//				                    unitNumber:"${volume.unitNumber}", externalId:volume.key, internalId:volume.fileName, rootVolume:(index == 0),
//				                    zoneId:zone?.id, uniqueId:"vmware.vsphere.volume.${zone.id}.${it.ref}.${volume.key}"]
//				if(volume.controllerKey)
//					volumeConfig.controller = add.controllers?.find{ controller -> controller.controllerKey == "${volume.controllerKey}"}
//				def newVolume = new StorageVolume(volumeConfig)
//				newVolume.save()
//				add.addToVolumes(newVolume)
//			}
			def add = new VirtualImage(imageConfig)
			adds << add
		}

		// Create em all!
		log.debug "About to create ${adds.size()} virtualImages"
		morpheusContext.virtualImage.create(adds, cloud).blockingGet()

		// Now add the locations
		def locationAdds = []
		// Fetch the images that we just created
		def imageMap = [:]
		morpheusContext.virtualImage.listSyncProjections(cloud.id).filter { VirtualImageIdentityProjection proj ->
			addNames.contains(proj.name)
		}.blockingSubscribe { imageMap[it.externalId] = it }

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
//				if(matchedTemplate.volumes) {
//					def start = new Date()
//					def changed = vmwareProvisionService.syncVolumes(zone, imageLocation, matchedTemplate.volumes)
//					if(changed == true)
//						save = true
//				}
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
//							return a.controllerMountPoint <=> b.controllerMountPoint
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
	}

	private removeMissingVirtualImages(List removeList) {
		log.debug "removeMissingVirtualImages: ${removeList?.size()}"

		morpheusContext.virtualImage.location.remove(removeList).blockingGet()
	}

}
