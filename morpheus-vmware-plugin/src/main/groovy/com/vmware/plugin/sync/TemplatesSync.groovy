package com.morpheusdata.vmware.plugin.sync

import groovy.util.logging.Slf4j
import com.morpheusdata.vmware.plugin.*
import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Datastore
import com.morpheusdata.model.VirtualImageLocation
import com.morpheusdata.model.projection.VirtualImageLocationIdentityProjection
import com.morpheusdata.core.*
import com.morpheusdata.core.util.SyncTask
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
				return projection.virtualImage.linkedClone != true && !projection.sharedStorage && projection.virtualImage.imageType in ['ovf', 'vmware', 'vmdk']
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
//				addMissingVirtualImageLocations(cloud, itemsToAdd)
			}.onUpdate { List<SyncTask.UpdateItem<Datastore, Map>> updateItems ->
//				updateMatchedVirtualImages(cloud, updateItems)
			}.onDelete { removeItems ->
//				removeMissingVirtualImages(cloud, removeItems)
			}.start()
		}

//				def queryResults = [listResults: listResults, existingLocations:[]]
//				queryResults.existingLocations = VirtualImageLocation.withCriteria(cache:false) {
//					createAlias('virtualImage', 'virtualImage')
//					createAlias('virtualImage.owner', 'virtualImageOwner', CriteriaSpecification.LEFT_JOIN)
//					inList('virtualImage.imageType',['ovf','vmware','vmdk'])
//					ne('virtualImage.linkedClone',true)
//					eq('sharedStorage',false)
//					projections {
//						property('imageName')
//						property('externalId')
//						property('virtualImage.imageType')
//						property('id')
//						property('virtualImage.id')
//					}
//				}

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

//	@Transactional(readOnly=true)
//	protected addMissingVirtualImageLocations(ComputeZone zone, List objList) {
//
//		def names = objList.collect{it.name}?.unique()
//		def existingItems = VirtualImage.createCriteria().list() {
//			createAlias('owner','owner', CriteriaSpecification.LEFT_JOIN)
//			inList('imageType',['ovf','vmware','vmdk'])
//			inList('name', names)
//			or {
//				eq('systemImage', true)
//				or {
//					isNull('owner')
//					eq('owner.id', zone.owner.id)
//				}
//			}
//			projections {
//				property('name')
//				property('externalId')
//				property('imageType')
//				property('id')
//			}
//		}
//		def existingIds = existingItems.collect{existing -> [name:existing[0], externalId:existing[1], imageType:existing[2], id:existing[3]]} ?: []
//		existingIds.unique{"${it.imageType}:${it.name}"}
//		def secondaryMatchFunction = { Map morpheusItem, Map cloudItem ->
//			cloudItem.name == morpheusItem.name
//		}
//		def syncLists = ComputeUtility.buildSyncLists(existingIds, objList, secondaryMatchFunction)
//		//add missing
//		while(syncLists.addList?.size() > 0) {
//			List chunkedAddList = syncLists.addList.take(50)
//			syncLists.addList = syncLists.addList.drop(50)
//			addMissingVirtualImages(zone, chunkedAddList)
//		}
//		//update list
//		while(syncLists.updateList?.size() > 0) {
//			List chunkedUpdateList = syncLists.updateList.take(50)
//			syncLists.updateList = syncLists.updateList.drop(50)
//			updateMatchedVirtualImages(zone, chunkedUpdateList)
//		}
//		//removes?
//		syncLists.removeList?.each { removeItem ->
//			// println("need to remove: ${removeItem}")
//		}
//	}
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected addMissingVirtualImages(ComputeZone zone, List addList) {
//		Account owner = Account.read(zone.owner.id)
//		Account account = zone.account ? Account.read(zone.account.id) : null
//		def regionCode = getRegionCode(zone)
//		addList?.each {
//			def imageConfig = [owner:owner, visibility:'private', category:"vmware.vsphere.image.${zone.id}", name:it.name,
//			                   code:"vmware.vsphere.image.${zone.id}.${it.ref}", imageType:'vmdk', minRam:it.summary?.config?.memorySizeMB * ComputeUtility.ONE_MEGABYTE, status: 'Active',
//			                   remotePath:it.summary?.config?.vmPathName, externalId:it.ref, imageRegion:regionCode, internalId:it.config?.uuid,
//			                   refType:'ComputeZone', refId:"${zone.id}"]
//			def osTypeCode = VmwareComputeUtility.getMapVmwareOsType(it.config.guestId)
//			log.debug "cacheTemplates osTypeCode: ${osTypeCode}"
//			def osType = OsType.findByCode(osTypeCode ?: 'other')
//			log.debug "osType: ${osType}"
//			imageConfig.osType = osType
//			imageConfig.platform = osType?.platform
//			if(imageConfig.platform == 'windows') {
//				imageConfig.isForceCustomization = true
//				imageConfig.isCloudInit = false
//			}
//			imageConfig.virtioSupported = false
//
//			def add = new VirtualImage(imageConfig)
//			if(account) {
//				add.addToAccounts(account)
//			}
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
//			def locationConfig = [code:"vmware.vsphere.image.${zone.id}.${it.ref}", externalId:it.ref,
//			                      refType:'ComputeZone', refId:zone.id, imageName:it.name, imageRegion:regionCode]
//			def addLocation = new VirtualImageLocation(locationConfig)
//			add.addToLocations(addLocation)
//			add.save(flush:true)
//
//			def msg = [refId:addLocation.id, jobType:'virtualImageUpdatePricePlan']
//			sendRabbitMessage('main','', ApplianceJobService.applianceJobHighQueue, msg)
//		}
//	}
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected updateMatchedVirtualImages(ComputeZone zone, List updateList) {
//		def regionCode = getRegionCode(zone)
//		def locationIds = updateList?.findAll{ it.existingItem.locationId }?.collect{ it.existingItem.locationId }
//		def isPublicImage = false
//		List<VirtualImageLocation> existingLocations = locationIds ? VirtualImageLocation.where { id in locationIds && refType == 'ComputeZone' && refId == zone.id }.list(readOnly:true) : []
//		def imageIds = updateList?.findAll{ it.existingItem.id }?.collect{ it.existingItem.id }
//		def externalIds = updateList?.findAll{ it.existingItem.externalId }?.collect{ it.existingItem.externalId }
//		List<VirtualImage> existingItems = []
//		if(imageIds && externalIds) {
//			existingItems	= VirtualImage.where{ id in imageIds ||
//					(systemImage == false && externalId != null && refType == 'ComputeZone' && refId == zone.id.toString() &&
//							externalId in externalIds && locations.size() == 0) }.join('locations').list(cache:false,readOnly:true)
//		} else if(imageIds) {
//			existingItems	= VirtualImage.where{ id in imageIds }.list(cache:false,readOnly:true)
//		}
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
//		//updates
//		updateList?.each { update ->
//			def matchedTemplate = update.masterItem
//			def imageLocation = existingLocations?.find { it.id == update.existingItem.locationId }
//			if(imageLocation) {
//				def save = false
//				def saveImage = false
//				if(imageLocation.imageName != matchedTemplate.name) {
//					imageLocation.imageName = matchedTemplate.name
//					if(imageLocation.virtualImage.refId == imageLocation.refId.toString()) {
//						imageLocation.virtualImage.name = matchedTemplate.name
//						imageLocation.virtualImage.save()
//					}
//					save = true
//				}
//				if(imageLocation.code == null) {
//					imageLocation.code = "vmware.vsphere.image.${zone.id}.${matchedTemplate.ref}"
//					save = true
//				}
//				if(imageLocation.externalId != matchedTemplate.ref) {
//					imageLocation.externalId = matchedTemplate.ref
//					save = true
//				}
//				if(matchedTemplate.config?.uuid && imageLocation.internalId != matchedTemplate.config?.uuid) {
//					imageLocation.internalId = matchedTemplate.config.uuid
//					save = true
//				}
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
//				if(save) {
//					imageLocation.attach()
//					imageLocation.save()
//				}
//				if(saveImage) {
//					imageLocation.virtualImage?.save(flush:true)
//				}
//			} else {
//				def image = existingItems?.find { it.externalId == matchedTemplate.ref || it.name == matchedTemplate.name }
//				if(image) {
//					image.attach()
//					//if we matched by virtual image and not a location record we need to create that location record
//					def locationConfig = [code:"vmware.vsphere.image.${zone.id}.${matchedTemplate.ref}", externalId:matchedTemplate.ref,
//					                      internalId:matchedTemplate.config?.uuid, refType:'ComputeZone', refId:zone.id, imageName:matchedTemplate.name, imageRegion:regionCode]
//					def addLocation = new VirtualImageLocation(locationConfig)
//					addLocation.save()
//					//tmp fix
//					if(!image.owner && !image.systemImage)
//						image.owner = zone.owner
//					image.deleted = false
//					image.addToLocations(addLocation)
//					image.isPublic = isPublicImage
//					image.save()
//
//					def msg = [refId:addLocation.id, jobType:'virtualImageUpdatePricePlan']
//					sendRabbitMessage('main','', ApplianceJobService.applianceJobHighQueue, msg)
//				}
//
//			}
//		}
//	}
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected removeMissingVirtualImages(ComputeZone zone, List removeList) {
//		def locationIds = removeList.findAll{ it.locationId }?.collect{ it.locationId }
//		List<VirtualImageLocation> existingLocations = locationIds ? VirtualImageLocation.where { id in locationIds && refType == 'ComputeZone' &&
//				refId == zone.id }.join('virtualImage').join('virtualImage.locations').list() : []
//		try {
//			for(vlocation in existingLocations) {
//				if(vlocation.virtualImage.locations?.size() == 1) {
//					if(vlocation.virtualImage.systemImage != true && !vlocation.virtualImage.userUploaded && !vlocation.virtualImage.userDefined) {
//						vlocation.virtualImage.deleted = true
//						vlocation.virtualImage.save(flush:true)
//					}
//					vlocation.virtualImage.removeFromLocations(vlocation)
//					vlocation.delete(flush:true)
//				} else if(vlocation.virtualImage.locations.size() > 1 && vlocation.virtualImage.locations?.every{it.refId == zone.id}) {
//					vlocation.virtualImage.locations.findAll{it.refId == zone.id && it.id != vlocation.id}?.each { vloc ->
//						vlocation.virtualImage.removeFromLocations(vloc)
//						vloc.delete(flush:true)
//					}
//					if(vlocation.virtualImage.systemImage != true && !vlocation.virtualImage.userUploaded && !vlocation.virtualImage.userDefined) {
//						vlocation.virtualImage.deleted = true
//						vlocation.virtualImage.save(flush:true)
//					}
//					vlocation.virtualImage.removeFromLocations(vlocation)
//					vlocation.delete(flush:true)
//				} else {
//					vlocation.virtualImage.removeFromLocations(vlocation)
//					vlocation.delete(flush:true)
//				}
//
//				accountUsageService.queueStopAllUsage(null, AccountUsage.VIRTUAL_IMAGE_LOCATION_REF_TYPE, vlocation.id)
//			}
//		} catch(e) {
//			log.error("error deleting synced virtual image: ${e}", e)
//		}
//	}

}
