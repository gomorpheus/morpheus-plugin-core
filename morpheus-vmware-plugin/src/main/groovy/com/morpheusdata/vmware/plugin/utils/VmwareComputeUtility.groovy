package com.morpheusdata.vmware.plugin.utils

import com.vmware.vim25.*
import com.vmware.vim25.mo.*
import com.vmware.vim25.VirtualEthernetCard
import groovy.util.logging.Slf4j
import com.morpheusdata.core.util.HttpApiClient
import org.apache.http.client.utils.URIBuilder

@Slf4j
class VmwareComputeUtility {

	static VmwareConnectionPool connectionPool = new VmwareConnectionPool()
	static long SHUTDOWN_TIMEOUT = 5l*60000l

	static testConnection(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, invalidLogin:false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			serviceInstance.getRootFolder()
			rtn.success = true
		} catch(com.vmware.vim25.InvalidLogin e) {
			rtn.invalidLogin = true
		} catch(e) {
			log.error("testConnection to ${apiUrl}: ${e}")
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl, username, password, serviceInstance)}
		}
		return rtn
	}

	static getServiceInstance(apiUrl, username, password) {
		return connectionPool.getConnection(apiUrl, username, password)
	}

	static releaseServiceInstance(serviceInstance, apiUrl, username, password) {
		if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
	}

	static getManagedObject(serviceInstance, type, id) throws ManagedObjectNotFound {
		def mor
		def rtn
		if(id instanceof ManagedObjectReference) {
			mor = id
			type = id.getType()
		} else {
			mor = new ManagedObjectReference()
			mor.setType(type)
			mor.setVal(id)
		}
		if(type == 'Datacenter')
			rtn = new Datacenter(serviceInstance.getServerConnection(), mor)
		else if(type == 'ResourcePool')
			rtn = new ResourcePool(serviceInstance.getServerConnection(), mor)
		else if(type == 'ComputeResource')
			rtn = new ComputeResource(serviceInstance.getServerConnection(), mor)
		else if(type == 'ClusterComputeResource')
			rtn = new ClusterComputeResource(serviceInstance.getServerConnection(), mor)
		else if(type == 'VirtualMachine')
			rtn = new VirtualMachine(serviceInstance.getServerConnection(), mor)
		else if(type == 'VirtualMachineSnapshot')
			rtn = new VirtualMachineSnapshot(serviceInstance.getServerConnection(), mor)
		else if(type == 'Network')
			rtn = new com.vmware.vim25.mo.Network(serviceInstance.getServerConnection(), mor)
		else if(type == 'DistributedVirtualPortgroup')
			rtn = new com.vmware.vim25.mo.DistributedVirtualPortgroup(serviceInstance.getServerConnection(), mor)
		else if(type == 'Task')
			rtn = new com.vmware.vim25.mo.Task(serviceInstance.getServerConnection(), mor)
		else if(type == 'HostSystem')
			rtn = new HostSystem(serviceInstance.getServerConnection(), mor)
		else if(type == 'Datastore')
			rtn = new com.vmware.vim25.mo.Datastore(serviceInstance.getServerConnection(), mor)
		else if(type == 'StoragePod')
			rtn = new StoragePod(serviceInstance.getServerConnection(), mor)
		else if(type == 'IpPoolManager')
			rtn = new IpPoolManager(serviceInstance.getServerConnection(), mor)
		else if(type == 'ExtensionManager')
			rtn = new ExtensionManager(serviceInstance.getServerConnection(), mor)
		else if(type == 'StorageResourceManager')
			rtn = new StorageResourceManager(serviceInstance.getServerConnection(), mor)
		else if(type == 'HostStorageSystem')
			rtn = new HostStorageSystem(serviceInstance.getServerConnection(), mor)
		else if(type == 'HostDatastoreSystem')
			rtn = new HostDatastoreSystem(serviceInstance.getServerConnection(), mor)
		else if(type == 'DistributedVirtualSwitch')
			rtn = new DistributedVirtualSwitch(serviceInstance.getServerConnection(), mor)
		else if(type == 'Folder')
			rtn = new Folder(serviceInstance.getServerConnection(), mor)
		else if(type == 'Alarm')
			rtn = new Alarm(serviceInstance.getServerConnection(), mor)
		else if(type == 'CustomFieldsManager')
			rtn = new CustomFieldsManager(serviceInstance.getServerConnection(), mor)
		else if(type == 'OpaqueNetwork')
			rtn = new OpaqueNetwork(serviceInstance.getServerConnection(), mor)
		return rtn
	}

	static listComputeResources(apiUrl, username, password, opts = [:]) {
		log.debug "listComputeResources: ${apiUrl} ${opts}"
		def rtn = [success: false, computeResorces: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def entityList = new InventoryNavigator(datacenter).searchManagedEntities('ComputeResource')
					entityList?.each { computeResorce ->
						def objRoles = computeResorce.getEffectiveRole()
						def readOnly = isEntityReadOnly(objRoles)
						def configEx = computeResorce.getConfigurationEx()
						def drsEnabled = false
						def clusterType = configEx instanceof ClusterConfigInfoEx ? 'cluster' : 'host'
						if(clusterType == 'cluster') {
							def drsConfig = configEx.getDrsConfig()
							drsEnabled = drsConfig.getEnabled()
						}
						log.debug("account: ${username} cluster: ${computeResorce.getName()} role: ${objRoles}")
						rtn.computeResorces << [name:computeResorce.getName(), type:computeResorce.getMOR().getType(), ref:computeResorce.getMOR().getVal(),
						                        clusterType:clusterType, drsEnabled:drsEnabled, readOnly:readOnly]
					}
					rtn.success = true
				}

			} else {
				rtn.msg = 'no datacenter found'
			}
			rtn.success = true
		} catch(e) {
			log.error("listComputeResources: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static listDatacenters(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, datacenters: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def entityList = new InventoryNavigator(rootFolder).searchManagedEntities('Datacenter')
			entityList?.each { datacenter ->
				rtn.datacenters << [name:datacenter.getName(), type:datacenter.getMOR().getType(), ref:datacenter.getMOR().getVal()]
			}
			rtn.success = true
		} catch(e) {
			log.error("listDatacenters: ${e}", e)
			rtn.msg = e.getMessage()
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static listDatastores(apiUrl, username, password, opts = [:]) {
		log.debug "apiUrl ${apiUrl} ${username}"
		def rtn = [success:false, datastores:[]]
		def serviceInstance = opts.serviceInstance
		try {
			serviceInstance = serviceInstance ?: connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def parentEntity
			def entityList = null
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(opts.cluster) {
					if(datacenter) {
						def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
						if(cluster) {
							parentEntity = datacenter
							entityList = cluster.getDatastores()
						}
					} else {
						rtn.msg = "no datacenter found"
					}
				} else {
					parentEntity = datacenter
				}
				if(parentEntity) {
					def propList = []
					propList << 'name'
					propList << 'summary.accessible'
					propList << 'summary.capacity'
					propList << 'summary.freeSpace'
					propList << 'summary'
					propList << 'info.freeSpace'
					propList << 'info.maxFileSize'
					propList << 'info.url'
					propList << 'info'
					// propList << 'info.remoteHost'
					// propList << 'info.remotePath'
					// propList << 'info.userName'
					//load hosts
					def results = listBulkObjects(serviceInstance, parentEntity, 'Datastore', propList, opts)
					log.debug("results: ${results}")
					log.debug("entity list: ${entityList}")
					// println "Datastores: ${results.objects}"
					if(results.success == true) {
						results.objects?.each { obj ->
							def objRef = obj.item.getObj()
							def entityMatch = entityList?.find{ it.getMOR()?.getVal() == objRef.val }
							def isRubrikMount = obj.name.startsWith("rubrik_")
							if((entityList == null || entityMatch) && !isRubrikMount) {
								rtn.datastores << [name:obj.name, type:objRef.type, ref:objRef.val, freeSpace:obj['info.freeSpace'],
								                   totalSpace:obj['info.maxFileSize'], accessible:obj['summary.accessible'],
								                   summary:[accessible:obj['summary.accessible'], capacity:obj['summary.capacity'], freeSpace:obj['summary.freeSpace'], type:obj.summary?.type?.toLowerCase()],
								                   info:[freeSpace:obj['info.freeSpace'], maxFileSize:obj['info.maxFileSize'], url: obj['info.url'], remoteHost: obj.info instanceof com.vmware.vim25.NasDatastoreInfo ? obj.info.nas.remoteHost : null , remotePath: obj.info instanceof com.vmware.vim25.NasDatastoreInfo ? obj.info.nas.remotePath : null, userName: obj.info instanceof com.vmware.vim25.NasDatastoreInfo ? obj.info.nas.userName : null]
								]
							}
						}
					}
					rtn.success = results.success
					log.debug("count: ${rtn?.datastores?.size()} results: ${rtn}")
				}
			} else {
				rtn.msg = 'no datacenter configured'
			}
		} catch(e) {
			log.error("listDatastores: ${e}", e)
		} finally {
			if(serviceInstance && !opts.serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static listStoragePods(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, storagePods: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def entityList
					//if(opts.cluster) {
					//	def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
					//	entityList = new InventoryNavigator(cluster).searchManagedEntities('StoragePod')
					//} else {
					entityList = new InventoryNavigator(datacenter).searchManagedEntities('StoragePod')
					//}

					entityList?.each { pod ->
						def summary = pod.getSummary()
						rtn.storagePods << [name:summary.getName(), capacity:summary.getCapacity(), freeSpace:summary.getFreeSpace(),
						                    ref:pod.getMOR().getVal(), drsEnabled:pod.getPodStorageDrsEntry()?.getStorageDrsConfig()?.getPodConfig()?.isEnabled(),
						                    datastores:pod.getChildEntity()?.collect{ ds -> [datastore: ds, summary: ds.getSummary()]}, datastoreRefs: pod.getChildEntity()?.collect{ds -> ds.getMOR().val}, parent:pod.getParent()]
						//println("pod parent name: ${pod.getParent()?.getName()}")
						//println("pod parent: ${pod.getParent()?.getParent()}")
					}
					log.debug("storage pods: ${rtn.storagePods}")
					rtn.success = true
				} else {
					rtn.msg = "no datacenter found"
				}
			} else {
				rtn.msg = 'no datacenter configured'
			}
		} catch(e) {
			log.error("listStoragePods: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static listResourcePools(apiUrl, username, password, opts = [:]) {
		log.debug "listResourcePools ${opts}"
		def rtn = [success: false, resourcePools: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def entityList
				if(opts.cluster) {
					def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)

					def propList = []
					propList << 'name'
					propList << 'summary.name'
					propList << 'parent'
					propList << 'effectiveRole'



					//load hosts
					def results = listBulkObjects(serviceInstance, cluster, 'ResourcePool', propList, opts)

					results.objects.each { obj ->

						def objRef = obj.item.getObj()
						// log.info("Checking array: ${obj['effectiveRole'].get_int()}")
						def readOnly = isEntityReadOnly(obj['effectiveRole'].get_int())
						def poolItem = [name: obj['summary.name'] ?: obj['name'], ref: objRef.val, type:objRef.type, parentId: obj['parent'].getVal(), parentType: obj['parent'].getType(), readOnly: readOnly]
						rtn.resourcePools << poolItem
					}


				} else {
					log.debug("no cluster")
					entityList = new InventoryNavigator(datacenter).searchManagedEntities('ResourcePool')
					entityList?.each { resourcePool ->
						def objRoles = resourcePool.getEffectiveRole()
						def readOnly = isEntityReadOnly(objRoles)
						def parentMOR = resourcePool.parent?.getMOR()
						log.debug("account: ${username} pool: ${resourcePool.getName()} role: ${objRoles}")
						rtn.resourcePools << [name:resourcePool.getName(), type:resourcePool.getMOR().getType(), ref:resourcePool.getMOR().getVal(),
						                      resourcePool:resourcePool, parentType: parentMOR.getType(), parentId: parentMOR.getVal(), readOnly:readOnly]
					}
				}

				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listResourcePools: ${opts.datacenter} ${opts.cluster} ${e}", e)
		}
		finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static listVirtualMachines(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, virtualMachines:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def parentEntity
				if(opts.cluster) {
					if(datacenter) {
						def cluster = new InventoryNavigator(datacenter)?.searchManagedEntity('ComputeResource', opts.cluster)
						if(cluster) {
							if(opts.resourcePool) {
								def resourcePool = getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
								parentEntity = resourcePool
							} else {
								parentEntity = cluster
							}
						}
					} else {
						rtn.msg = "no datacenter found"
					}
				} else if(opts.resourcePool) {
					def resourcePool = getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
					parentEntity = resourcePool
				} else {
					parentEntity = datacenter
				}
				if(parentEntity) {
					def propList = []
					propList << 'name'
					propList << 'resourcePool'
					propList << 'config.uuid'
					propList << 'config.instanceUuid'
					propList << 'config.guestFullName'
					propList << 'config.template'
					propList << 'config.cpuHotAddEnabled'
					propList << 'config.memoryHotAddEnabled'
					propList << 'config.hardware.numCoresPerSocket'
					propList << 'runtime.host'
					propList << 'summary.storage.committed'
					propList << 'summary.storage.uncommitted'
					propList << 'summary.config.numCpu'
					propList << 'summary.config.memorySizeMB'
					propList << 'summary.config.vmPathName'
					propList << 'summary.config.annotation'
					propList << 'summary.quickStats'
					propList << 'summary.customValue'
					propList << 'config.extraConfig'
					propList << 'config.guestId'
					propList << 'guest.toolsStatus'
					propList << 'guest.hostName'
					propList << 'runtime.powerState'
					propList << 'runtime.maxCpuUsage'
					propList << 'config.hardware.device'
					propList << 'guest.net'
					propList << 'guest.ipAddress'
					propList << 'parent'
					propList << 'tag'
					propList << 'snapshot.rootSnapshotList'
					propList << 'snapshot.currentSnapshot'
					def results = listBulkObjects(serviceInstance, parentEntity, 'VirtualMachine', propList, opts)
					if(results.success == true) {
						results.objects?.each { obj ->
							if(obj['config.template'] == false) {
								def objRef = obj.item.getObj()
								def deviceList = obj['config.hardware.device']?.getVirtualDevice()
								def netList = obj['guest.net']?.getGuestNicInfo()
								def extraConfigList = obj['config.extraConfig']?.getOptionValue()
								def resourcePool = obj.resourcePool
								rtn.virtualMachines << [name:obj.name, os:obj['config.guestFullName'], resourcePool:resourcePool, type:objRef.type,
								                        ref:objRef.val, parent:obj.parent,
								                        config:[template:obj['config.template'], uuid:obj['config.uuid'], instanceUuid:obj['config.instanceUuid'], cpuHotAddEnabled:obj['config.cpuHotAddEnabled'],
								                                memoryHotAddEnabled:obj['config.memoryHotAddEnabled'], extraConfig:extraConfigList, guestId:obj['config.guestId'],
								                                hardware:[device:deviceList, numCoresPerSocket: obj['config.hardware.numCoresPerSocket']]],
								                        runtime:[host:obj['runtime.host'], powerState:obj['runtime.powerState'], maxCpuUsage:obj['runtime.maxCpuUsage']],
								                        summary:[
										                        storage:[comitted:obj['summary.storage.committed'], uncommitted:obj['summary.storage.uncommitted']],
										                        config:[numCpu:obj['summary.config.numCpu'], annotation: obj['summary.config.annotation'], memorySizeMB:obj['summary.config.memorySizeMB'],
										                                vmPathName:obj['summary.config.vmPathName']],
										                        quickStats:obj['summary.quickStats'],
										                        customValues:obj['summary.customValue'].getCustomFieldValue()],
								                        guest:[toolsStatus:obj['guest.toolsStatus'], hostName:obj['guest.hostName'], ipAddress: obj['guest.ipAddress'], net:netList],
								                        volumes:getVmVolumes(deviceList),
								                        controllers:getVmControllers(deviceList),
								                        networks:getVmNetworks(deviceList),
								                        snapshots: obj['snapshot.rootSnapshotList'],
								                        currentSnapshot: obj['snapshot.currentSnapshot'],
								                        tags:obj['tag'].getTag()
								]
								// println "snapshot tree: obj['snapshot.rootSnapshotList']?."

							}
						}
					}
					rtn.success = results.success
					log.debug("count: ${rtn?.virtualMachines?.size()} results: ${rtn}")
				}
			} else {
				rtn.msg = 'no datacenter confiugred'
			}
		} catch(e) {
			log.error("listVirtualMachines: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static listFolders(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, folders: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def propList = []
					propList << 'name'
					propList << 'parent'
					propList << 'childType'
					propList << 'effectiveRole'



					//load hosts
					def results = listBulkObjects(serviceInstance, datacenter, 'Folder', propList, opts)

					results.objects.each { obj ->

						def objRef = obj.item.getObj()
						// log.info("Checking array: ${obj['effectiveRole'].get_int()}")
						def readOnly = isEntityReadOnly(obj['effectiveRole'].get_int())
						def folderItem = [name: obj['name'], childTypes: obj['childType'].getString(), ref: objRef.val, type:objRef.type, parentRef: obj['parent'].getVal(), parentType: obj['parent'].getType(), readOnly: readOnly, folderRole: obj['effectiveRole'].get_int()]
						rtn.folders << folderItem
					}

					rtn.success = true
				} else {
					rtn.msg = 'no datacenter found'
				}
			} else {
				rtn.msg = 'no datacenter configured'
			}
		} catch(e) {
			log.error("listFolders: ${opts.datacenter} ${opts.cluster} ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static getVmControllers(virtualDevices) {
		def rtn = []
		try {
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualController) {
					def newController = [name:virtualDevice.getDeviceInfo()?.getLabel(), description:virtualDevice.getDeviceInfo()?.getSummary(),
					                     controllerKey:"${virtualDevice.getKey()}", externalId:"${virtualDevice.getKey()}", key:virtualDevice.getKey(),
					                     unitNumber:virtualDevice.getUnitNumber(), busNumber:virtualDevice.getBusNumber()]
					if(virtualDevice instanceof VirtualIDEController)
						newController.type = 'vmware-ide'
					else if(virtualDevice instanceof VirtualBusLogicController)
						newController.type = 'vmware-busLogic'
					else if(virtualDevice instanceof VirtualLsiLogicController)
						newController.type = 'vmware-lsiLogic'
					else if(virtualDevice instanceof VirtualLsiLogicSASController)
						newController.type = 'vmware-lsiLogicSas'
					else if(virtualDevice instanceof ParaVirtualSCSIController)
						newController.type = 'vmware-paravirtual'
					if(newController.type)
						rtn << newController
				}
			}
		} catch(e) {
			log.error("getVmControllers error: ${e}")
		}
		return rtn
	}

	static getVmNetworks(virtualDevices) {
		def rtn = []
		try {
			def counter = 0
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualEthernetCard) {

					def newNic = [name:virtualDevice.getDeviceInfo()?.getLabel(), description:virtualDevice.getDeviceInfo()?.getSummary(),
					              key:virtualDevice.getKey(), unitNumber:virtualDevice.getUnitNumber(), macAddress:virtualDevice.getMacAddress(),
					              controllerKey:virtualDevice.getControllerKey(), type:'e1000', row:counter]

					if(virtualDevice.getBacking() instanceof VirtualEthernetCardDistributedVirtualPortBackingInfo) {
						newNic.portGroup = virtualDevice.getBacking().port.portgroupKey
						newNic.networkId = virtualDevice.getBacking().port.portgroupKey
						newNic.switchUuid = virtualDevice.getBacking().port.switchUuid
					} else if(virtualDevice.getBacking() instanceof VirtualEthernetCardNetworkBackingInfo) {
						newNic.networkId = virtualDevice.getBacking().network.getVal()
					}
					if(virtualDevice instanceof VirtualVmxnet2)
						newNic.type = 'vmxNet2'
					else if(virtualDevice instanceof VirtualVmxnet3)
						newNic.type = 'vmxNet3'
					rtn << newNic
					counter++
				}
			}
		} catch(e) {
			log.error("getVmNetworks error: ${e}")
		}
		return rtn
	}

	static getVmVolumes(virtualDevices) {
		def rtn = []
		try {
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualDisk && !(virtualDevice instanceof VirtualCdrom)) {
					def newDisk = [name:virtualDevice.getDeviceInfo()?.getLabel(), size:virtualDevice.getCapacityInKB(), key:virtualDevice.getKey(),
					               unitNumber:virtualDevice.getUnitNumber(), controllerKey:"${virtualDevice.getControllerKey()}", type:'']
					def backing = virtualDevice.getBacking()
					if(backing instanceof  VirtualDeviceFileBackingInfo) {
						newDisk.fileName = backing.getFileName()
						newDisk.datastore = backing.getDatastore()?.getVal()
						if(backing instanceof VirtualDiskFlatVer2BackingInfo) {
							VirtualDiskFlatVer2BackingInfo info = backing as VirtualDiskFlatVer2BackingInfo
							newDisk.thin = info.getThinProvisioned()
						}
					}
					rtn << newDisk
				}
			}
		} catch(e) {
			log.error("getVmVolumes error: ${e}")
		}
		return rtn
	}

	static isEntityReadOnly(roleList) {
		def rtn = true
		roleList?.each { roleId ->
			if(roleId == -1 || roleId > 2 || roleId < -5)
				rtn = false
		}
		return rtn
	}

	static listBulkObjects(serviceInstance, parentEntity, entityType, propList, opts = [:]) {
		def rtn = [success:false, objects:[]]
		try {
			def options = new RetrieveOptions()
			options.setMaxObjects(100)
			def propSpec = new PropertySpec()
			propSpec.setAll(false)
			propSpec.setType(entityType)
			propSpec.setPathSet(propList as String[])
			def objSpec = new ObjectSpec()
			objSpec.setObj(parentEntity.getMOR())
			objSpec.setSelectSet(com.vmware.vim25.mo.util.PropertyCollectorUtil.buildFullTraversalV4())
			objSpec.setSkip(true)
			def pfSpec = new PropertyFilterSpec[1]
			pfSpec[0] = new PropertyFilterSpec()
			def objSpecList = new ObjectSpec[1]
			objSpecList[0] = objSpec
			pfSpec[0].setObjectSet(objSpecList)
			def propFilterSpec = new PropertySpec[1]
			propFilterSpec[0] = propSpec
			pfSpec[0].setPropSet(propFilterSpec)
			def keepGoing = true
			def attempts = 0
			def token = null
			def propertyCollector = serviceInstance.getPropertyCollector()
			while(keepGoing == true && attempts < 1000) {
				attempts++
				def results
				if(token == null)
					results = propertyCollector.retrievePropertiesEx(pfSpec, options)
				else
					results = propertyCollector.continueRetrievePropertiesEx(token)
				log.debug("results: ${results?.dump()}")
				results?.getObjects()?.each { item ->
					def addObj = [item:item]
					item.getPropSet()?.each { prop ->
						addObj[prop.getName()] = prop.getVal()
					}
					rtn.objects << addObj
				}
				token = results?.getToken()
				if(!token) {
					if(attempts == 0 && !results) {
						log.info("No Information Found for : ${parentEntity} - ${entityType}")
					}
					keepGoing = false
				}
			}
			rtn.success = true
		} catch(e) {
			log.error("listBulkObjects: ${e}", e)
		}
		return rtn
	}

	static getServerIps(netList, networkMatch = null) throws ManagedObjectNotFound {
		def rtn = [ipAddress:null, ipv6Ip:null, ipList:[]]
		netList.each { net ->
			def netNetwork = net.getNetwork()
			net.getIpConfig()?.getIpAddress()?.each { ipAddr ->
				def ip = ipAddr.getIpAddress()
				if(ip.indexOf(':') > -1) {
					//ipv6
					if(netNetwork != null && (networkMatch == null || networkMatch == netNetwork) && rtn.ipv6Ip == null)
						rtn.ipv6Ip = ip
					rtn.ipList << [network:netNetwork, deviceConfigId: net.getDeviceConfigId(), ipAddress:ip, mode:'ipv6', macAddress:net.getMacAddress()]
				} else if(ip.indexOf('.') > -1) {
					//ipv4
					if(netNetwork != null && (networkMatch == null || networkMatch == netNetwork) && rtn.ipAddress == null)
						rtn.ipAddress = rtn.ipAddress ?: ip
					rtn.ipList << [network:netNetwork, deviceConfigId: net.getDeviceConfigId(), ipAddress:ip, mode:'ipv4', macAddress:net.getMacAddress()]
				}
			}
		}
		if(!rtn.ipAddress) {
			netList.each { net ->
				def netNetwork = net.getNetwork()
				net.getIpConfig()?.getIpAddress()?.each { ipAddr ->
					def ip = ipAddr.getIpAddress()
					if(ip.indexOf(':') > -1) {
						//ipv6
						if(net.getDeviceConfigId() != null && rtn.ipv6Ip == null)
							rtn.ipv6Ip = rtn.ipv6Ip ?: ip
						rtn.ipList << [network:netNetwork, deviceConfigId: net.getDeviceConfigId(), ipAddress:ip, mode:'ipv6', macAddress:net.getMacAddress()]
					} else if(ip.indexOf('.') > -1) {
						//ipv4
						if(net.getDeviceConfigId() != null && rtn.ipAddress == null)
							rtn.ipAddress = rtn.ipAddress ?: ip
						rtn.ipList << [network:netNetwork, deviceConfigId: net.getDeviceConfigId(), ipAddress:ip, mode:'ipv4', macAddress:net.getMacAddress()]
					}
				}
			}
		}
		return rtn
	}

	static listHosts(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, hosts:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def parentEntity
					if(opts.cluster) {
						def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
						if(cluster)
							parentEntity = cluster
					} else {
						parentEntity = datacenter
					}
					if(parentEntity) {
						def propList = []
						propList << 'name'
						propList << 'hardware.memorySize'
						propList << 'hardware.systemInfo.uuid'
						propList << 'hardware.cpuInfo.numCpuCores'
						propList << 'summary.hardware.cpuModel'
						propList << 'summary.hardware.numCpuPkgs'
						propList << 'summary.hardware.numCpuThreads'
						propList << 'summary.hardware.numNics'
						propList << 'summary.hardware.vendor'
						propList << 'config.network.dnsConfig.hostName'
						propList << 'datastore'
						propList << 'summary.hardware.cpuMhz'
						propList << 'summary.quickStats'
						propList << 'runtime.powerState'
						propList << 'parent'
						//load hosts
						def results = listBulkObjects(serviceInstance, parentEntity, 'HostSystem', propList, opts)
						log.debug("results: ${results}")
						if(results.success == true) {
							def dsList = listDatastores(apiUrl, username, password, opts + [serviceInstance: serviceInstance])
							results.objects?.each { obj ->
								def objRef = obj.item.getObj()
								def datastoreList = obj['datastore']?.getManagedObjectReference()
								def addHost = [name:obj.name, uuid:obj['hardware.systemInfo.uuid'], type:objRef.type, ref:objRef.val, parent:obj['parent'], memorySize:obj['hardware.memorySize'],
								               cpuCoreCount:obj['hardware.cpuInfo.numCpuCores'], hostname:obj['config.network.dnsConfig.hostName'] ?: obj.name,
								               datastoreList:datastoreList,
								               datastores:[],
								               summary:[quickStats:obj['summary.quickStats'],
								                        hardware:[cpuMhz:obj['summary.hardware.cpuMhz'], cpuModel:obj['summary.hardware.cpuModel'],
								                                  cpuCount:obj['summary.hardware.numCpuPkgs'], threadCount:obj['summary.hardware.numCpuThreads'],
								                                  nicCount:obj['summary.hardware.numNics'], vendor:obj['summary.hardware.vendor']]
								               ],
								               hardware:[memorySize:obj['hardware.memorySize'], cpuInfo:[numCpuCores:obj['hardware.cpuInfo.numCpuCores']]],
								               runtime:[powerState:obj['runtime.powerState']],
								               cluster: opts.cluster
								]
								//bulk load the datastores
								datastoreList?.each { ds ->
									def dsRef = ds.getVal()
									def dsMatch = dsList?.datastores?.find{ it.ref == dsRef }
									if(dsMatch)
										addHost.datastores << dsMatch
								}
								rtn.hosts << addHost
							}
						} else {
							log.warn("Error Listing Hosts...Could be a User Permissions Issue for datacenter : ${opts?.datacenter} - ${opts?.cluster}")
						}
						rtn.success = results.success
						log.debug("count: ${rtn?.hosts?.size()} results: ${rtn}")
					}
				} else {
					rtn.msg = "no datacenter found"
				}
			} else {
				rtn.msg = 'no datacenter configured'
			}
		} catch(e) {
			log.error("listHosts: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static listTemplates(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, templates:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def parentEntity = datacenter
					if(parentEntity) {
						def propList = []
						propList << 'name'
						propList << 'resourcePool'
						propList << 'config.uuid'
						propList << 'config.guestFullName'
						propList << 'config.template'
						propList << 'summary.config.numCpu'
						propList << 'summary.config.memorySizeMB'
						propList << 'summary.config.vmPathName'
						propList << 'config.guestId'
						propList << 'config.hardware.device'
						def results = listBulkObjects(serviceInstance, parentEntity, 'VirtualMachine', propList, opts)
						log.debug("results: ${results}")
						if(results.success == true) {
							results.objects?.each { obj ->
								if(obj['config.template'] == true) {
									def objRef = obj.item.getObj()
									def deviceList = obj['config.hardware.device']?.getVirtualDevice()
									def resourcePool = obj.resourcePool
									def resourcePoolId = resourcePool?.getVal()
									rtn.templates << [name:obj.name, os:obj['config.guestFullName'], resourcePool:resourcePoolId, type:objRef.type, ref:objRef.val,
									                  config:[template:obj['config.template'], uuid:obj['config.uuid'], guestId:obj['config.guestId'],
									                          hardware:[device:deviceList]],
									                  summary:[
											                  config:[numCpu:obj['summary.config.numCpu'], memorySizeMB:obj['summary.config.memorySizeMB'], vmPathName:obj['summary.config.vmPathName']]],
									                  volumes:getVmVolumes(deviceList),
									                  controllers:getVmControllers(deviceList),
									                  networks:getVmNetworks(deviceList)
									]
								}
							}
						}
						rtn.success = results.success
					}
				} else {
					rtn.msg = "no datacenter found"
				}
				log.debug("count: ${rtn?.templates?.size()} results: ${rtn}")
			} else {
				rtn.msg = 'no datacenter configured'
			}
		} catch(e) {
			log.error("listTemplates - url: ${apiUrl} error:${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static listNetworks(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, networks: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def entityList
					if(opts.cluster) {
						def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
						if(cluster) {
							log.debug("loading cluster networks")
							entityList = cluster.getNetworks()
						}
					} else {
						entityList = new InventoryNavigator(datacenter).searchManagedEntities('Network')
					}
					//need to pull up distributed port groups
					def portGroupResults = listPortGroups(apiUrl, username, password, opts + [serviceInstance: serviceInstance])
					entityList?.each { network ->
						log.debug("network: ${network.getName()} ${network.getMOR().getType()}")
						if(!opts.distributed || network.getMOR().getType() == 'DistributedVirtualPortgroup') {
							def addItem = [name:network.getName(), type:network.getMOR().getType(), ref:network.getMOR().getVal(), obj:network]
							if(network.getMOR().getType() == 'DistributedVirtualPortgroup') {
								addItem.key = network.getKey()
								def matchPortGroup = portGroupResults.portGroups?.find{ it.key == addItem.key }
								if(matchPortGroup) {
									addItem.switchName = matchPortGroup.switchName
									addItem.switchUuid = matchPortGroup.switchUuid
									addItem.uplink = matchPortGroup.uplinkPortgroup
								}
							}
							rtn.networks << addItem
						}
					}
					rtn.success = true
				} else {
					rtn.msg = "no datacenter found"
				}
			} else {
				rtn.msg = 'no datacenter configured'
			}
		} catch(e) {
			log.error("listNetworks: ${e}", e)
		} finally {
			if(serviceInstance) { connectionPool.releaseConnection(apiUrl,username,password, serviceInstance) }
		}
		return rtn
	}

	static listPortGroups(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, portGroups:[]]
		def serviceInstance = opts.serviceInstance
		try {
			serviceInstance = serviceInstance ?: connectionPool.getConnection(apiUrl, username, password)
			def hostList = listHosts(apiUrl, username, password, opts)
			hostList?.hosts?.each { host ->
				def hostParent = getManagedObject(serviceInstance, host.parent.getType(), host.parent.getVal())
				def envBrowser = hostParent.getEnvironmentBrowser()
				if(envBrowser) {
					def configTarget = envBrowser.queryConfigTarget(host.entity)
					def portGroupList = configTarget.getDistributedVirtualPortgroup()
					portGroupList?.each { portGroup ->
						def newItem = [name:portGroup.portgroupName, key:portGroup.portgroupKey, entity:portGroup,
									   type:portGroup.portgroupType, switchName:portGroup.switchName, switchUuid:portGroup.switchUuid,
									   uplink:portGroup.uplinkPortgroup]
						def match = rtn.portGroups.find{ it.key == newItem.key }
						if(!match)
							rtn.portGroups << newItem
					}
				} else {
					log.warn("It appears a host ${host.name} is down during sync... Skipping")
				}

			}
			rtn.success = true
		} catch(e) {
			log.error("listPortGroups: ${e}", e)
		} finally {
			if(serviceInstance && !opts.serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static getMapVmwareOsType(vmwareType) {
		switch(vmwareType) {
			case 'asianux3_64Guest':
			case 'asianux4_64Guest':
			case 'asianux5_64Guest':
			case 'coreos64Guest':
			case 'turboLinux64Guest':
			case 'mandriva64Guest':
			case 'sles64Guest':
			case 'sles10_64Guest':
			case 'sles11_64Guest':
			case 'sles12_64Guest':
			case 'sles15_64Guest':
			case 'freebsd64Guest':
			case 'other24xLinux64Guest':
			case 'other26xLinux64Guest':
			case 'other3xLinux64Guest':
			case 'otherLinux64Guest':
				return 'linux.64'
			case 'asianux3Guest':
			case 'asianux4Guest':
			case 'eComStation2Guest':
			case 'eComStationGuest':
			case 'turboLinuxGuest':
			case 'genericLinuxGuest':
			case 'mandrakeGuest':
			case 'mandrivaGuest':
			case 'sles10Guest':
			case 'sles11Guest':
			case 'sles12Guest':
			case 'slesGuest':
			case 'freebsdGuest':
			case 'other24xLinuxGuest':
			case 'other26xLinuxGuest':
			case 'other3xLinuxGuest':
			case 'otherLinuxGuest':
				return 'linux.32'
			case 'centos64Guest':
				return 'cent.64'
			case 'centos7_64Guest':
				return 'cent.7.64'
			case 'centos8_64Guest':
				return 'cent.8.64'
			case 'centosGuest':
				return 'cent'
			case 'darwin10_64Guest':
			case 'darwin10Guest':
			case 'darwin11_64Guest':
			case 'darwin11Guest':
			case 'darwin12_64Guest':
			case 'darwin13_64Guest':
			case 'darwin14_64Guest':
			case 'darwin15_64Guest':
			case 'darwin16_64Guest':
			case 'darwin17_64Guest':
			case 'darwin18_64Guest':
			case 'darwin19_64Guest':
			case 'darwin64Guest':
			case 'darwinGuest':
				return 'mac'
			case 'debian4_64Guest':
			case 'debian5_64Guest':
				return 'debian.64'
			case 'debian4Guest':
			case 'debian5Guest':
				return 'debian'
			case 'debian6_64Guest':
				return 'debian.6.64'
			case 'debian6Guest':
				return 'debian.6'
			case 'debian7_64Guest':
				return 'debian.7.64'
			case 'debian7Guest':
				return 'debian.7'
			case 'debian8_64Guest':
				return 'debian.8.64'
			case 'debian8Guest':
				return 'debian.8'
			case 'debian9_64Guest':
				return 'debian.9.64'
			case 'debian9Guest':
				return 'debian.9'
			case 'debian10_64Guest':
				return 'debian.10.64'
			case 'debian10Guest':
				return 'debian.10'
			case 'debian11_64Guest':
				return 'debian.11.64'
			case 'debian11Guest':
				return 'debian.11'
			case 'fedora64Guest':
				return 'fedora.64'
			case 'fedoraGuest':
				return 'fedora'
			case 'oracleLinux64Guest':
				return 'oracle.64'
			case 'oracleLinuxGuest':
				return 'oracle.32'
			case 'redhatGuest':
			case 'rhel2Guest':
			case 'rhel3Guest':
			case 'rhel4Guest':
			case 'rhel5Guest':
				return 'redhat'
			case 'rhel3_64Guest':
			case 'rhel4_64Guest':
			case 'rhel5_64Guest':
				return 'redhat.64'
			case 'rhel6_64Guest':
				return 'redhat.6.64'
			case 'rhel6Guest':
				return 'redhat.6'
			case 'rhel7_64Guest':
				return 'redhat.7.64'
			case 'rhel7Guest':
				return 'redhat.7'
			case 'solaris10_64Guest':
			case 'solaris11_64Guest':
				return 'solaris.64'
			case 'solaris10Guest':
			case 'solaris6Guest':
			case 'solaris7Guest':
			case 'solaris8Guest':
			case 'solaris9Guest':
				return 'solaris.32'
			case 'suse64Guest':
			case 'opensuse64Guest':
				return 'suse.64'
			case 'suseGuest':
			case 'opensuseGuest':
				return 'suse'
			case 'ubuntu64Guest':
				return 'ubuntu.64'
			case 'ubuntuGuest':
				return 'ubuntu'
			case 'win2000AdvServGuest':
			case 'win2000ProGuest':
			case 'win2000ServGuest':
			case 'win31Guest':
			case 'win95Guest':
			case 'win98Guest':
			case 'windowsHyperVGuest':
			case 'winLonghorn64Guest':
			case 'winLonghornGuest':
			case 'winMeGuest':
			case 'winNetBusinessGuest':
			case 'winNetDatacenter64Guest':
			case 'winNetDatacenterGuest':
			case 'winNetEnterprise64Guest':
			case 'winNetEnterpriseGuest':
			case 'winNetStandard64Guest':
			case 'winNetStandardGuest':
			case 'winNetWebGuest':
			case 'winNTGuest':
			case 'winVista64Guest':
			case 'winVistaGuest':
			case 'winXPHomeGuest':
			case 'winXPPro64Guest':
			case 'winXPProGuest':
				return 'windows'
			case 'windows7_64Guest':
				return 'windows.7.64'
			case 'windows7Guest':
				return 'windows.7'
			case 'windows8_64Guest':
				return 'windows.8.64'
			case 'windows9_64Guest':
				return 'windows.10.64'
			case 'windows8Guest':
				return 'windows.8'
			case 'windows9Guest':
				return 'windows.10'
			case 'windows7Server64Guest':
				return 'windows.server.2008.r2'
			case 'windows8Server64Guest':
				return 'windows.server.2012'
			case 'windows9Server64Guest':
				return 'windows.server.2016'
			case 'windows10Server64Guest':
			case 'windows2019srv_64Guest':
				return 'windows.server.2019'
			case 'otherGuest64':
			case 'dosGuest':
			case 'netware4Guest':
			case 'netware5Guest':
			case 'netware6Guest':
			case 'nld9Guest':
			case 'oesGuest':
			case 'sjdsGuest':
			case 'unixWare7Guest':
			case 'vmkernel5Guest':
			case 'vmkernel6Guest':
			case 'vmkernelGuest':
			case 'openServer5Guest':
			case 'openServer6Guest':
			case 'os2Guest':
			case 'otherGuest':
				return 'other.64'
		}
		return 'other.64'
	}

	static stopVm(apiUrl, username, password, String externalId) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', externalId)
			def vmRuntime = vm.getRuntime()
			if(vmRuntime.getPowerState() == VirtualMachinePowerState.poweredOn || vmRuntime.getPowerState() == VirtualMachinePowerState.suspended) {
				if(vmRuntime.getPowerState() != VirtualMachinePowerState.suspended) {
					try {
						vm.shutdownGuest()
						long counter = 0
						while(counter < SHUTDOWN_TIMEOUT) {
							counter += 5000
							sleep(5000)
							log.debug "checking powerstate"
							vm = getManagedObject(serviceInstance, 'VirtualMachine', externalId)
							vmRuntime = vm.getRuntime()
							if(vmRuntime.getPowerState() != VirtualMachinePowerState.poweredOn) {
								log.debug "guest shutdown successful"
								break
							}
						}
					} catch(ex) {
						log.error("Error shutting down guest operating system via vmware ${ex.getMessage()}...Attempting hard shutdown.")
					}
				}
				vm = getManagedObject(serviceInstance, 'VirtualMachine', externalId)
				vmRuntime = vm.getRuntime()
				if(vmRuntime.getPowerState() == VirtualMachinePowerState.poweredOn || vmRuntime.getPowerState() == VirtualMachinePowerState.suspended) {
					def vmTask = vm.powerOffVM_Task()
					def result = vmTask.waitForTask()
					if(result == Task.SUCCESS) {
						rtn.success = true
					}
				} else {
					rtn.success = true
				}
			} else {
				rtn.success = true
				rtn.msg = 'VM is already powered off'
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error powering off vm', e)
		} catch(e) {
			log.error("stopVm error: ${e}", e)
			rtn.msg = 'error powering off vm'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static startVm(apiUrl, username, password, externalId) {
		def rtn = [success:false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', externalId)
			def vmRuntime = vm.getRuntime()
			if(vmRuntime.getPowerState() == VirtualMachinePowerState.poweredOff || vmRuntime.getPowerState() == VirtualMachinePowerState.suspended) {
				def vmTask = vm.powerOnVM_Task()
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
				}
			} else {
				rtn.success = true
				rtn.msg = 'VM is already powered on'
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error powering on vm', e)
		} catch(e) {
			log.error("startVm error: ${e}", e)
			rtn.msg = 'error powering on vm'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static listIpPools(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, ipPools: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def serviceContent = serviceInstance.retrieveServiceContent()
					def ipPoolManagerRef = serviceContent.getIpPoolManager()
					def ipPoolManager = ipPoolManagerRef ? getManagedObject(serviceInstance, 'IpPoolManager', ipPoolManagerRef.getVal()) : null
					def entityList = ipPoolManager ? ipPoolManager.queryIpPools(datacenter) : []
					log.debug("ip pools: ${entityList}")
					entityList?.each { ipPool ->
						def ipv4Config = ipPool.getIpv4Config()
						def ipv6Config = ipPool.getIpv6Config()
						rtn.ipPools << [name:ipPool.getName(), id:ipPool.getId(), ref:"${ipPool.getId()}", ipv4Count:ipPool.getAllocatedIpv4Addresses(),
						                ipv6Count:ipPool.getAllocatedIpv6Addresses(), ipv4Available:ipPool.getAvailableIpv4Addresses(), ipv6Available:ipPool.getAvailableIpv6Addresses(),
						                dnsDomain:ipPool.getDnsDomain(), dnsSearchPath:ipPool.getDnsSearchPath(), hostPrefix:ipPool.getHostPrefix(), httpProxy:ipPool.getHttpProxy(),
						                dhcpServer:ipv4Config.getDhcpServerAvailable(), dnsServers:ipv4Config.getDns(), gateway:ipv4Config.getGateway(),
						                ipPoolEnabled:ipv4Config.getIpPoolEnabled(), netmask:ipv4Config.getNetmask(), ipRanges:ipv4Config.getRange(),
						                subnetAddress:ipv4Config.getSubnetAddress()
						]
					}
					rtn.success = true
				} else {
					rtn.msg = 'no datacenter found'
				}
			} else {
				rtn.msg = 'no datacenter configured'
			}
		} catch(e) {
			log.error("listIpPools: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static listCustomizationSpecs(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, customSpecs: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def customManager = serviceInstance.getCustomizationSpecManager()
			if(customManager) {
				def entityList = customManager.getInfo()
				entityList?.each { spec ->
					rtn.customSpecs << [name: spec.getName(), type: spec.getType(), description: spec.getDescription(), version: spec.getChangeVersion(), spec: spec]
				}
				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listCustomizationSpecs: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static listAlarms(apiUrl, username, password, Map opts = [:]) {
		def rtn = [success: false, alarms:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password,)
			def alarmManager = serviceInstance.getAlarmManager()
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def entityList =  datacenter?.getTriggeredAlarmState()  //alarmManager.getAlarmState(datacenter)
				entityList?.each { alarm ->
					def alarmObj = getManagedObject(serviceInstance, 'Alarm', alarm.getAlarm())
					def alarmInfo = alarmObj.getAlarmInfo()
					def alarmEntityId = alarm.getEntity()
					def alarmEntity = getManagedObject(serviceInstance, 'Unknown', alarmEntityId)
					def addAlarm = [acknowledged:alarm.getAcknowledged(), time:alarm.getTime()?.getTime(), key:alarm.getKey(),
					                acknowledgedByUser:alarm.getAcknowledgedByUser(), acknowledgedTime:alarm.getAcknowledgedTime()?.getTime(),
					                status:(alarm.getOverallStatus()?.toString() ?: 'unknown'), externalId:alarm.getKey(),
					                alarm:[action:alarmInfo.getAction(), description:alarmInfo.getDescription(), name:alarmInfo.getName(),
					                       setting:alarmInfo.getSetting(), systemName:alarmInfo.getSystemName()]
					]
					alarm?.getDynamicProperty()?.each { prop ->
						def propName = prop.getName()
						if(propName)
							addAlarm[propName] = prop.getVal()
					}
					if(alarmEntity) {
						addAlarm.entity = [type:alarmEntityId?.getType(), id:alarmEntityId?.getVal(), name:alarmEntity.getName(),
						                   status:(alarmEntity.getOverallStatus()?.toString() ?: 'unknown'),
						                   configStatus:(alarmEntity.getConfigStatus()?.toString() ?: 'unknown')]
					}
					rtn.alarms << addAlarm
				}
				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listAlarms: ${opts.datacenter} ${opts.cluster} ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl, username, password, serviceInstance)}
		}
		return rtn
	}

	static listCategories(apiUrl,username,password, HttpApiClient client,opts=[:]) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		def apiResults
		def url = privateGetSchemeHostAndPort(apiUrl)
		try {
			def sessionResults = getRestSessionId(client, url, username,password,opts)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			apiResults = client.callJsonApi(url, "/rest/com/vmware/cis/tagging/category",null,null, new HttpApiClient.RequestOptions(headers:["vmware-api-session-id": sessionId]), 'GET')
			rtn.categoryIds = apiResults.data?.value
			rtn.categories = []
			rtn.categoryIds?.each { categoryId ->
				apiResults = client.callJsonApi(url, "/rest/com/vmware/cis/tagging/category/id:${categoryId}",null,null,new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId]),'GET')
				sleep(50)
				if(apiResults.success) {
					def categoryObj = [externalId: categoryId, name: apiResults.data.value.name, description: apiResults.data.value.description]
					rtn.categories << categoryObj
				}
			}

			rtn.success = apiResults.success
		} catch(e) {
			log.error("listCustomizationSpecs: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(client, url, sessionId,opts)
			}
		}

		return rtn
	}

	static listTags(apiUrl,username,password,HttpApiClient client,opts=[:]) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		def apiResults
		def url = privateGetSchemeHostAndPort(apiUrl)
		try {
			def sessionResults = getRestSessionId(client, url, username,password,opts)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			// TODO: proxySettings support
			apiResults = client.callJsonApi(url,"/rest/com/vmware/cis/tagging/tag",null,null,new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId]),'GET')
			rtn.tagIds = apiResults.data?.value
			rtn.tags = []
			rtn.tagIds?.each { tagId ->
				// TODO : proxySettings support
				// apiResults = client.callJsonApi(url,"/rest/com/vmware/cis/tagging/tag/id:${tagId}",null,null,[headers: ["vmware-api-session-id": sessionId],reuse:true,proxySettings: opts.proxySettings, httpClient: apiResults.httpClient],'GET')
				apiResults = client.callJsonApi(url,"/rest/com/vmware/cis/tagging/tag/id:${tagId}",null,null,new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId]),'GET')
				sleep(50)
				if(apiResults.success) {
					def tagObj = [externalId: tagId, name: apiResults.data.value.name, categoryId: apiResults.data.value.category_id, description: apiResults.data.value.description]
					rtn.tags << tagObj
				}
			}
			rtn.success = apiResults.success
		} catch(e) {
			log.error("listTags: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(client, url,sessionId,opts)
			}
		}

		return rtn
	}

	static listContentLibraryItems(apiUrl,username,password,HttpApiClient client) {
		def rtn = [success: false,libraryItems:[]]
		def sessionId
		def apiResults
		def url = privateGetSchemeHostAndPort(apiUrl)
		try {
			def contentLibResults = listContentLibraries(apiUrl,username,password,client)
			if(contentLibResults.success ) {
				rtn.libraryItems = []
				rtn.libraryItemIds = []
				def sessionResults = getRestSessionId(client, url, username, password)
				sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
				contentLibResults.libraries?.each { library ->
					String libraryId = library.externalId
					log.debug("sessionId: ${sessionId}")
					def query = ['library_id': libraryId]
					apiResults = client.callJsonApi(url, "/rest/com/vmware/content/library/item", null, null, new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId], queryParams: query), 'GET')
					def libraryItemIds = apiResults.data?.value
					rtn.libraryItemIds += libraryItemIds
					libraryItemIds?.each { itemId ->
						apiResults = client.callJsonApi(url, "/rest/com/vmware/content/library/item/id:${itemId}", null, null, new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId]), 'GET')
						sleep(50)
						if (apiResults.success) {
							def libItemObj = [externalId: itemId] + apiResults.data.value + [storage_backings: library.storage_backings]
							rtn.libraryItems << libItemObj
						}
					}
				}

				rtn.success = true
			}
		} catch(e) {
			log.error("listContentLibraries: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(client, url, sessionId)
			}
		}

		return rtn
	}

	static listContentLibraries(apiUrl,username,password,HttpApiClient client) {
		def rtn = [success: false]
		def sessionId
		def apiResults
		def url = privateGetSchemeHostAndPort(apiUrl)
		try {
			def sessionResults = getRestSessionId(client, url, username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			apiResults = client.callJsonApi(url,"/rest/com/vmware/content/library",null,null,new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId]),'GET')
			rtn.libraryIds = apiResults.data?.value
			rtn.libraries = []
			rtn.libraryIds?.each { libId ->
				apiResults = client.callJsonApi(url,"/rest/com/vmware/content/library/id:${libId}",null,null,new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId]),'GET')
				sleep(50)
				if(apiResults.success) {
					def libObj = [externalId: libId] + apiResults.data.value
					rtn.libraries << libObj
				}
			}
			rtn.success = apiResults.success
		} catch(e) {
			log.error("listContentLibraries: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(client, url, sessionId)
			}
		}

		return rtn
	}

	static listTagAssociationsForVirtualMachines(apiUrl,username,password,HttpApiClient client,opts=[:]) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		def url = privateGetSchemeHostAndPort(apiUrl)
		try {
			rtn.associations = [:]
			def sessionResults = getRestSessionId(client, url, username, password)
			sessionId = sessionResults.sessionId
			log.debug("sessionId: ${sessionId}")
			def body = [object_ids: []]
			def params = ['~action':'list-attached-tags-on-objects']
			body.object_ids = opts.vmIds.collect{[type: 'VirtualMachine', id: it]}
			def apiResults = client.callJsonApi(url,"/rest/com/vmware/cis/tagging/tag-association",null,null,new HttpApiClient.RequestOptions([headers: ["vmware-api-session-id": sessionId], body: body, queryParams: params]),'POST')
			if(apiResults.success) {
				apiResults.data?.value.collect { association ->
					rtn.associations[association.object_id.id] = association.tag_ids
				}
			} else {
				log.warn("Error Calling Tag Associations API: ${apiResults.statusCode} for VMs: ${opts.vmIds}")
				rtn.success = false
			}
			rtn.success = apiResults.success
		} catch(e) {
			log.error("listTagAssociations: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(client, url, sessionId)
			}
		}

		return rtn
	}

	static getRestSessionId(HttpApiClient client, url, username, password,opts=[:]) {
		def apiResults = client.callJsonApi(url,"/rest/com/vmware/cis/session",username,password,new HttpApiClient.RequestOptions(),'POST')
		return [success:apiResults.success,sessionId:apiResults.data?.value]
	}

	static logoutRestSessionId(client, url, sessionId,opts=[:]) {
		def apiResults = client.callJsonApi(url, "/rest/com/vmware/cis/session",null,null,new HttpApiClient.RequestOptions(headers: ["vmware-api-session-id": sessionId]),'DELETE')
		return [success:apiResults.success]
	}

	static privateGetSchemeHostAndPort(apiUrl) {
		URIBuilder uriBuilder = new URIBuilder(apiUrl)
		"${uriBuilder.scheme}://${uriBuilder.host}${uriBuilder.port != -1 ? ':' + uriBuilder.port : ''}"
	}
}
