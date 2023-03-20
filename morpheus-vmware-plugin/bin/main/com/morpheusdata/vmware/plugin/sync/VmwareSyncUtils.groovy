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
		log.debug "buildSyncLists: ${existingItems}, ${masterItems}"
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
		def rtn = [changed: false, maxStorage: 0l]
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
				rtn.maxStorage += volume.maxStorage
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
				def matchController = matchStorageVolumeController(locationOrServer, volume)
				if(matchController?.id != existingVolume.controller?.id) {
					existingVolume.controller = matchController
					save = true
				}
				if(save) {
					saveList << existingVolume
				}
				// Remove any duplicates
				def matchedVolumes = serverVolumes?.findAll{ it.externalId == volume.key.toString()}
				if(matchedVolumes?.size() > 1) {
					def deleteVolumes = matchedVolumes.findAll { it.id != existingVolume.id }
					if(locationOrServer instanceof ComputeServer) {
						morpheusContext.storageVolume.remove(deleteVolumes, locationOrServer, false).blockingGet()
					} else {
						morpheusContext.storageVolume.remove(deleteVolumes, locationOrServer).blockingGet()
					}
				}
			}

			if(saveList) {
				rtn.changed = true
				log.debug "Found ${saveList?.size()} volumes to update"
				morpheusContext.storageVolume.save(saveList).blockingGet()
			}

			// The removes
			if(syncLists.removeList) {
				rtn.changed = true
				if(locationOrServer instanceof ComputeServer) {
					morpheusContext.storageVolume.remove(syncLists.removeList, locationOrServer, false).blockingGet()
				} else {
					morpheusContext.storageVolume.remove(syncLists.removeList, locationOrServer).blockingGet()
				}
			}

			// The adds
			def newVolumes = buildNewStorageVolumes(syncLists.addList, cloud, locationOrServer, null, opts)
			if(newVolumes) {
				rtn.changed = true
				newVolumes?.each { rtn.maxStorage += it.maxStorage }
				morpheusContext.storageVolume.create(newVolumes, locationOrServer).blockingGet()
			}
		} catch(e) {
			log.error "Error in syncVolumes: ${e}", e
		}
		rtn
	}

	static buildNewStorageVolumes(volumes, cloud, locationOrServer, account, opts = [:]) {
		log.debug "buildNewStorageVolumes: ${volumes?.size()} ${cloud} ${locationOrServer} ${account} ${opts}"
		def rtn = []
		def existingVolumes = locationOrServer?.volumes
		def newIndex = existingVolumes?.size() ?: 0

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
					datastore : datastore,
					displayOrder: (index + newIndex),
			]

			def newVolume = buildStorageVolume(account ?: cloud.account, locationOrServer, volumeConfig, (index + newIndex))

			def matchController = matchStorageVolumeController(locationOrServer, volume)
			if (matchController) {
				newVolume.controller = matchController
			}

			rtn << newVolume
		}
		return rtn
	}


	static StorageVolume buildStorageVolume(Account account, locationOrServer, volume, index, size = null) {
		log.debug "buildStorageVolume: ${account} ${locationOrServer} ${volume} ${index}"
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
		if(volume.datastoreId) {
			storageVolume.datastore = new DatastoreIdentityProjection(id: volume.datastoreId.toLong())
		}
		if(volume.controller) {
			if(volume.controller instanceof StorageController)
				storageVolume.controller = volume.controller
		}
		storageVolume.rootVolume = volume.rootVolume == true
		storageVolume.removable = storageVolume.rootVolume != true
		storageVolume.displayOrder = volume.displayOrder ?: locationOrServer?.volumes?.size() ?: 0
		storageVolume.diskIndex = index
		return storageVolume
	}

	def buildComputeServerInterface(Account account, Instance instance, ComputeServer server, networkInterface, index, opts = [:]) {
		log.debug "networkInterface: ${networkInterface}, ${index}, ${opts}"
		def computeServerInterface = new ComputeServerInterface()
		def platform = server.serverOs?.platform ?: (server.platform ?: 'linux')
		//computeServerInterface.name = networkInterface.name
		def nicName
		if(index == 0 && server.sourceImage?.interfaceName) {
			nicName = server.sourceImage?.interfaceName
		}
		if(networkInterface instanceof Map && networkInterface.nicName) {
			nicName = networkInterface.nicName
		}
		if(!nicName) {
			if(platform == 'windows') {
				nicName = (index == 0) ? 'Ethernet' : 'Ethernet ' + (index + 1)
			} else if(platform == 'linux') {
				nicName = "eth${index}"
			} else {
				nicName = "eth${index}"
			}
		}
		computeServerInterface.name =  nicName
		computeServerInterface.displayOrder = index
		computeServerInterface.macAddress = networkInterface.macAddress
		computeServerInterface.ipMode = networkInterface.ipMode

		def v = networkInterface.replaceHostRecord
		v == true || v == 'true' || v == 'on' || v == 'yes'
		computeServerInterface.replaceHostRecord = (v == true || v == 'true' || v == 'on' || v == 'yes')
		computeServerInterface.primaryInterface = networkInterface.isPrimary != null ? networkInterface.isPrimary : index == 0
		if(networkInterface instanceof ComputeServerInterface) {
			computeServerInterface.type = networkInterface.type
		} else if(networkInterface.networkInterfaceTypeId) {
			computeServerInterface.type = ComputeServerInterfaceType.get(networkInterface.networkInterfaceTypeId?.toLong())
		}
		if(networkInterface.externalId) {
			computeServerInterface.externalId = networkInterface.externalId
		}
		if(networkInterface.network?.id) {
			computeServerInterface.network = new Network(id: networkInterface.network.id.toLong())
			if(!(networkInterface.network instanceof Network) && networkInterface.network?.subnet) {
				computeServerInterface.subnet = new NetworkSubnet(id: networkInterface.network.subnet.toLong())
			}
			// TODO : Handle NetworkGroup
//			if(networkInterface.network instanceof Map && networkInterface.network?.group) {
//				computeServerInterface.networkGroup = new NetworkGroup(id: networkInterface.network.group.toLong())
//			}
		} else if(networkInterface.network?.group) {
			// TODO : Handle NetworkGroup
//			computeServerInterface.networkGroup = NetworkGroup.get(networkInterface.network.group.toLong())
//			def siteId = instance ? instance.site.id : server?.provisionSiteId
//			def usedNetworks = []
//			if(instance) {
//				usedNetworks = instance.containers.findAll{container -> container.server.id != server.id}?.collect{ container -> container.server.interfaces?.collect{ net -> net.network }}.flatten()?.findAll{it != null} ?: []
//				def serverUsedNetworks = server.interfaces?.collect{ net -> net.network}?.findAll{it != null} ?: []
//				if(serverUsedNetworks) {
//					usedNetworks += serverUsedNetworks
//				}
//				usedNetworks.flatten()
//			}
//			def usedSubnets = []
//			if(instance) {
//				usedSubnets = instance.containers.findAll{container -> container.server.id != server.id}?.collect{ container -> container.server.interfaces?.collect{ net -> net.subnet }}.flatten()?.findAll{it != null} ?: []
//				def serverUsedSubnets = server.interfaces?.collect{ net -> net.subnet}?.findAll{it != null} ?: []
//				if(serverUsedSubnets) {
//					usedSubnets += serverUsedSubnets
//				}
//				usedSubnets.flatten()
//			}
//			//computeServerInterface.network = networkService.pickNetworkGroupNetworkOrSubnet(computeServerInterface.networkGroup, account.id,
//			def pickedItem = networkService.pickNetworkGroupNetworkOrSubnet(computeServerInterface.networkGroup, account.id,
//					siteId, server?.zone?.id, usedNetworks, usedSubnets)
//			if(pickedItem instanceof NetworkSubnet) {
//				computeServerInterface.subnet = pickedItem
//				computeServerInterface.network = computeServerInterface.subnet?.network
//			} else {
//				computeServerInterface.network = pickedItem
//			}
		} else if(!(networkInterface.network instanceof Network) && networkInterface.network?.subnet) {
			computeServerInterface.subnet = new NetworkSubnet(id: networkInterface.network.subnet.toLong())
			computeServerInterface.network = new Network(id: computeServerInterface.subnet?.networkId)
		}
		if(computeServerInterface.network && !instance?.networkDomain) {
			computeServerInterface.networkDomain = computeServerInterface.network.networkDomain
		}
		if((computeServerInterface.subnet?.pool?.id ?: computeServerInterface.network?.pool?.id) && (computeServerInterface.ipMode != 'dhcp' || !computeServerInterface.ipMode)) {
			def poolId = computeServerInterface.subnet?.pool?.id ?: computeServerInterface.network?.pool?.id
			computeServerInterface.networkPool = new NetworkPool(id: poolId.toLong())
			computeServerInterface.dhcp = computeServerInterface.networkPool?.dhcpServer
			if(computeServerInterface.ipMode != 'pool') {
				computeServerInterface.ipAddress = networkInterface.ipAddress
				computeServerInterface.publicIpAddress = computeServerInterface.ipAddress
				computeServerInterface.dhcp = false
			}
		}
		def networkOrSubnet = computeServerInterface.subnet ?: computeServerInterface.network
		if(networkOrSubnet?.dhcpServer && (computeServerInterface.ipMode == 'dhcp' || !computeServerInterface.ipMode)) {
			computeServerInterface.dhcp = true
			if(!(networkInterface instanceof ComputeServerInterface) && networkInterface.forceIpSet ) {
				computeServerInterface.ipAddress = networkInterface.forceIpSet.ipAddress
				computeServerInterface.publicIpAddress = networkInterface.forceIpSet.publicIpAddress
			}
		} else if(!(networkInterface instanceof ComputeServerInterface) && networkInterface.ipAddress ) {
			computeServerInterface.ipAddress = networkInterface.ipAddress
			computeServerInterface.publicIpAddress = computeServerInterface.ipAddress
			computeServerInterface.dhcp = false
		}
		return computeServerInterface
	}

	static Boolean syncInterfaces(ComputeServer server, List networks, List ipList, Map systemNetworks, Collection<ComputeServerInterfaceType> netTypes, MorpheusContext morpheusContext) {
		def rtn = false
		try {
			def serverInterfaces = server.interfaces
			def missingInterfaces = []
			def newInterfaces = []
			Boolean primaryInterfaceExists = serverInterfaces?.any{it.primaryInterface}
			networks.eachWithIndex { netEntry, index ->
				def ipAddress = ipList.find{ it.mode == 'ipv4' && it.macAddress == netEntry.macAddress}?.ipAddress
				def ipv6Address = ipList.find{ it.mode == 'ipv6' && it.macAddress == netEntry.macAddress}?.ipAddress
				def netType = netTypes.find{it.externalId == netEntry.type}
				def net = systemNetworks["${netEntry.switchUuid ?:''}:${netEntry.networkId}".toString()]
				def matchedInterface = serverInterfaces?.find{ it.externalId == netEntry.key.toString()}
				if(!matchedInterface) {
					matchedInterface = serverInterfaces?.find{!it.externalId}
				}
				if(!matchedInterface) {
					def newInterface = new ComputeServerInterface(externalId: netEntry.key, type: netType, macAddress: netEntry.macAddress, name: netEntry.name, ipAddress: ipAddress, ipv6Address: ipv6Address, network:net, displayOrder:netEntry.row)
					if(!serverInterfaces) {
						newInterface.primaryInterface = true
					} else {
						newInterface.primaryInterface = false
					}
					newInterfaces << newInterface
				} else {
					def primaryInterface = netEntry.row?.toInteger() == 0
					def save = false
					if(!primaryInterfaceExists) {
						if(matchedInterface.primaryInterface != primaryInterface) {
							matchedInterface.primaryInterface = primaryInterface
							save = true
						}
					}
					if(net && matchedInterface.network?.code != net.code) {
						matchedInterface.network = net
						save = true
					}
					if(matchedInterface.ipAddress != ipAddress) {
						matchedInterface.ipAddress = ipAddress
						save = true
					}
					if(matchedInterface.ipv6Address != ipv6Address) {
						matchedInterface.ipv6Address = ipv6Address
						save = true
					}
					if(matchedInterface.macAddress != netEntry.macAddress) {
						matchedInterface.macAddress = netEntry.macAddress
						save = true
					}
					if(matchedInterface.externalId != netEntry.key.toString()) {
						matchedInterface.externalId = netEntry.key.toString()
						save = true
					}
					if(matchedInterface.type?.code != netType.code) {
						matchedInterface.type = netType
						save = true
					}
					if(matchedInterface.displayOrder != netEntry.row) {
						matchedInterface.displayOrder = netEntry.row
						save = true
					}
					if(save) {
						morpheusContext.computeServer.computeServerInterface.save([matchedInterface]).blockingGet()
						rtn = true
					}
					// Remove any duplicates
					def matchedInterfaces = serverInterfaces?.findAll{ it.externalId == netEntry.key.toString()}
					if(matchedInterfaces?.size() > 1) {
						def deleteInterfaces = matchedInterfaces.findAll { it.id != matchedInterface.id }
						morpheusContext.computeServer.computeServerInterface.remove(deleteInterfaces, server).blockingGet()
					}
				}
			}

			server.interfaces?.each { iface ->
				def found = networks.find{it.key.toString() == iface.externalId}
				if(!found) {
					missingInterfaces << iface
				}
			}
			if(missingInterfaces) {
				morpheusContext.computeServer.computeServerInterface.remove(missingInterfaces, server).blockingGet()
				rtn = true
			}
			if(newInterfaces?.size() > 0) {
				morpheusContext.computeServer.computeServerInterface.create(newInterfaces, server).blockingGet()
				rtn = true
			}
		} catch(e) {
			log.error("syncInterfaces error: ${e}", e)
		}
		return rtn
	}

	static Boolean syncControllers(Cloud cloud, serverOrLocation, List externalControllers, Boolean checkContainer = true, Account account = null, MorpheusContext morpheusContext) {
		def rtn = false //returns if there are changes to be saved
		log.debug("controllers: {}", externalControllers)
		try {
			def serverControllers = serverOrLocation.controllers?.sort{it.id}
			def matchContainer = null //checkContainer ? (server.id ? Container.findByServer(server) : null) : null
			def matchFunction = { existingController, cloudController ->
				existingController.busNumber == "${cloudController.busNumber}" && existingController.type?.code == cloudController.type
			}
			def syncLists = buildSyncLists(serverControllers, externalControllers, matchFunction)

			def saveList = []
			syncLists.updateList?.each { updateMap ->
				log.debug "Updating ${updateMap}"
				StorageController existingController = updateMap.existingItem
				def controller = updateMap.masterItem

				def save = false
				if (existingController.controllerKey == null || existingController.controllerKey != controller.controllerKey) {
					existingController.controllerKey = controller.controllerKey
					existingController.externalId = controller.externalId
					save = true
				}
				if(save) {
					saveList << existingController
				}
				// Remove any duplicates
				def matchedControllers = serverControllers?.findAll{ it.externalId == controller.externalId}
				if(matchedControllers?.size() > 1) {
					def deleteControllers = matchedControllers.findAll { it.id != existingController.id }
					if(serverOrLocation instanceof ComputeServer) {
						morpheusContext.storageController.remove(deleteControllers, serverOrLocation, false).blockingGet()
					} else {
						morpheusContext.storageController.remove(deleteControllers, serverOrLocation).blockingGet()
					}


				}
			}

			if(saveList) {
				rtn = true
				log.debug "Found ${saveList?.size()} controllers to update"
				morpheusContext.storageController.save(saveList).blockingGet()
			}

			// The removes
			if(syncLists.removeList) {
				rtn = true
				if(serverOrLocation instanceof ComputeServer) {
					morpheusContext.storageController.remove(syncLists.removeList, serverOrLocation, false).blockingGet()
				} else {
					morpheusContext.storageController.remove(syncLists.removeList, serverOrLocation).blockingGet()
				}
			}

			// The adds
			if(syncLists.addList) {
				rtn = true
				def newControllers = []
				syncLists.addList.eachWithIndex { controller, index ->
					def newIndex = syncLists.updateList.size() + index
					def newController = buildStorageController(controller, newIndex)
					if(matchContainer)
						newController.uniqueId = "morpheus-controller-${matchContainer.instance?.id}-${matchContainer.id}-${newIndex}"
					else
						newController.uniqueId = java.util.UUID.randomUUID()
					newControllers << newController
				}
				morpheusContext.storageController.create(newControllers, serverOrLocation).blockingGet()
			}

		} catch(e) {
			log.error("syncControllers error: ${e}", e)
		}
		return rtn
	}

	static StorageController buildStorageController(controller, index) {
		def storageController = new StorageController()
		storageController.name = controller.name
		storageController.description = controller.description
		storageController.controllerKey = controller.controllerKey
		storageController.unitNumber = controller.unitNumber
		storageController.busNumber = controller.busNumber
		storageController.displayOrder = index
		if(controller.type) {
			if(controller.type instanceof StorageControllerType)
				storageController.type = new StorageControllerType(id: controller.type?.id?.toLong())
			else
				storageController.type = new StorageControllerType(code: controller.type)
		} else if(controller.typeId && controller.typeId.toString().isLong()) {
			storageController.type = new StorageControllerType(id: controller.typeId?.toLong())
		} else if(controller.typeCode) {
			storageController.type = new StorageControllerType(code: controller.typeCode)
		} else {
			storageController.type = new StorageControllerType(code: 'vmware-plugin-standard')
		}
		if(controller.externalId)
			storageController.externalId = controller.externalId
		if(controller.internalId)
			storageController.internalId = controller.internalId
		return storageController
	}

	static StorageController matchStorageVolumeController(locationOrServer, volume) {
		return locationOrServer.controllers.sort{it.id}?.find{it.controllerKey == volume.controllerKey}
	}

	static String getControllerMountPoint(StorageVolume volume, StorageController controller) {
		if(volume && controller) {
			return "${controller.id}:${controller.busNumber}:${controller.type.id}:${volume.unitNumber ?: 0}"
		} else {
			return null
		}
	}

	static getVolumeDisplayOrderUpdates(MorpheusContext morpheusContext, VirtualImageLocation imageLocation, List <VirtualImageLocation> locationList) {
		log.debug "getVolumeDisplayOrderUpdates: ${imageLocation?.id} ${locationList?.size()}"
		def volumeIds = imageLocation?.volumes.collect { it.id}
		def allVolumesAndControllers = getVolumesAndControllersForLocations(morpheusContext, locationList)
		def currentVolumes = allVolumesAndControllers.volumes.findAll {it.id in volumeIds}
		def volumesToUpdate = []
		if(currentVolumes?.every { vol -> vol.displayOrder == 0}) {
			//this would mean we need to fix the display order. if there is more than one volume AND they all have the same display order
			currentVolumes.sort {a, b ->
				if (a.rootVolume) {
					return -1
				}
				def aController = allVolumesAndControllers.controllers.find{ it.id == a.controller?.id}
				def bController = allVolumesAndControllers.controllers.find{ it.id == b.controller?.id}
				return VmwareSyncUtils.getControllerMountPoint(a, aController) <=> VmwareSyncUtils.getControllerMountPoint(b, bController)
			}.eachWithIndex { vol, index ->
				vol.displayOrder = index
				vol.diskIndex = index
				volumesToUpdate << vol
			}
		}
		volumesToUpdate
	}

	private static getVolumesAndControllersForLocations(MorpheusContext morpheusContext, List <VirtualImageLocation> locationList) {
		log.debug "getVolumesAndControllersForLocations: ${locationList.size()}"
		def volumeIds = locationList.collect {it.volumes.id }.flatten()
		def volumes = []
		morpheusContext.storageVolume.listById(volumeIds).blockingSubscribe {
			volumes << it
		}
		def controllers = []
		def controllerIds = volumes.findAll {it.controller }.collect { it.controller.id }
		morpheusContext.storageController.listById(controllerIds).blockingSubscribe {
			controllers << it
		}
		["volumes": volumes, "controllers": controllers]
	}
}
