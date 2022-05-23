package com.morpheusdata.vmware.plugin.utils

import com.morpheusdata.core.util.ComputeUtility
import com.morpheusdata.core.util.SSLUtility
import com.vmware.vim25.*
import com.vmware.vim25.mo.*
import com.vmware.vim25.VirtualEthernetCard
import groovy.util.logging.Slf4j
import com.morpheusdata.core.util.HttpApiClient
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.NTCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.SSLContextBuilder
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.conn.ssl.X509HostnameVerifier
import org.apache.http.entity.InputStreamEntity
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.ProxyAuthenticationStrategy
import com.bertramlabs.plugins.karman.CloudFile

import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocket
import java.security.cert.X509Certificate

@Slf4j
class VmwareComputeUtility {

	static VmwareConnectionPool connectionPool = new VmwareConnectionPool()
	static ignoreSsl = true
	static int CHUCK_LEN = 64 * 1024
	static long SHUTDOWN_TIMEOUT = 5l*60000l
	static VmwareLeaseProgressUpdater leaseUpdater
	static morpheusExtensionId = 'com.morpheusdata.plugin.vmware.extension'
	static final java.util.concurrent.ExecutorService monitorServiceExecutor = java.util.concurrent.Executors.newCachedThreadPool()

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
						newController.type = 'vmware-plugin-ide'
					else if(virtualDevice instanceof VirtualBusLogicController)
						newController.type = 'vmware-plugin-busLogic'
					else if(virtualDevice instanceof VirtualLsiLogicController)
						newController.type = 'vmware-plugin-lsiLogic'
					else if(virtualDevice instanceof VirtualLsiLogicSASController)
						newController.type = 'vmware-plugin-lsiLogicSas'
					else if(virtualDevice instanceof ParaVirtualSCSIController)
						newController.type = 'vmware-plugin-paravirtual'
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

	static getTargetDatastores(apiUrl, username, password, datacenterId, clusterId, hostId) {
		def rtn
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def dsRoot
			if(hostId)
				dsRoot = getManagedObject(serviceInstance, 'HostSystem', hostId)
			if(dsRoot == null) {
				def rootFolder = serviceInstance.getRootFolder()
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', datacenterId)
				dsRoot = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', clusterId)
			}
			if(dsRoot != null) {
				def datastores = dsRoot.getDatastores()
				rtn = datastores?.collect {store -> [datastore:store, accessible:store.getSummary().isAccessible(), freeSpace:store.getSummary().getFreeSpace(),
				                                     capacity:store.getSummary().getCapacity(), type:store.getSummary().getType(), ref:store.getMOR().getVal(),
				                                     availablePercent: (store.getSummary().getFreeSpace() ?: 0) / (store.getSummary().getCapacity() ?: 1)]
				}
				rtn?.sort{-it.availablePercent}
			}
		} catch(e) {
			log.error("getTargetDatastores error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static getUniqueVmName(apiUrl, username, password, name) {
		def rtn
		def newName = name
		def attempts = 0
		def keepGoing = true
		while(attempts < 100 && keepGoing == true) {
			def testName = newName
			if(attempts > 0)
				testName = testName + "-${attempts}"
			def match = findVirtualMachine(apiUrl, username, password, testName)
			if(match?.virtualMachine != null) {
				attempts++
			} else {
				rtn = testName
				keepGoing = false
			}
		}
		return rtn
	}

	static findVirtualMachine(apiUrl, username, password, vmName, opts = [:]) {
		def rtn = [success:false, virtualMachine:null]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def entityList = new InventoryNavigator(datacenter).searchManagedEntities('VirtualMachine')
				/*def entityList
				if(opts.cluster) {
					def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
					if(opts.resourcePool) {
						def resourcePool = new InventoryNavigator(cluster).searchManagedEntity('ResourcePool', opts.resourcePool)
						entityList = new InventoryNavigator(resourcePool).searchManagedEntities('VirtualMachine')
					} else {
						entityList = new InventoryNavigator(cluster).searchManagedEntities('VirtualMachine')
					}
				} else if(opts.resourcePool) {
					def resourcePool = new InventoryNavigator(datacenter).searchManagedEntity('ResourcePool', opts.resourcePool)
					log.debug("find res pool: ${resourcePool} for: ${opts.resourcePool} dc: ${datacenter}")
					entityList = new InventoryNavigator(resourcePool).searchManagedEntities('VirtualMachine')
				} else {
					entityList = new InventoryNavigator(datacenter).searchManagedEntities('VirtualMachine')
				}*/
				rtn.virtualMachine = entityList?.find { it.getName() == vmName }
				rtn.success = true
			} else {
				rtn.msg = 'no vm found'
			}
		} catch(e) {
			log.error("listVirtualMachines: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static checkImageId(apiUrl, username, password, imageId) {
		def rtn
		if(imageId) {
			def serviceInstance
			try {
				serviceInstance = connectionPool.getConnection(apiUrl, username, password)
				def vm = getManagedObject(serviceInstance, 'VirtualMachine', imageId)
				def vmConfig = vm.getConfig()
				if(vmConfig) {
					def vmHardware = vmConfig.getHardware()
					if(vmHardware) {
						rtn = imageId
					}
				}
			} catch(e) {
				log.warn("checkImageId - image not found - ${imageId}")
			} finally {
				if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
			}

		}
		return rtn
	}

	static findOvfFile(Collection fileList) {
		def rtn
		if(fileList) {
			def matchList = []
			for(file in fileList) {
				def filePath = file.name.toLowerCase();
				def fileName = getLastPath(filePath)
				println("find ovf file: ${filePath} - ${fileName}")\
				//looking for non tmp file ovf files
				if(fileName.endsWith('.ovf') == true && fileName.startsWith('.') != true)
					matchList << file
			}
			if(matchList.size() > 0)
				rtn = matchList[0]
		}
		return rtn
	}

	static insertContainerImage(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		log.info("insertContainerImage: ${opts.image?.name}")
		def currentList = listTemplates(apiUrl, username, password, opts)?.templates
		log.debug("got: ${currentList}")
		def image = opts.image
		def match = currentList.find { it.name == image.name }
		if(!match) {
			def insertOpts = [vmName:image.name, ovfFile:image.imageSrc, cloudFiles:image.cloudFiles, minDisk:image.minDisk,
			                  minRam:image.minRam, imageType:image.imageType, containerType:image.containerType, hostId:opts.hostId, hostIp:opts.hostIp,
			                  networkName:opts.networkName ?: '', cachePath:opts.cachePath, datastoreId:opts.datastoreId, cluster:opts.cluster,
			                  networkId:opts.networkId, networkBackingType:opts.networkBackingType, resourcePool:opts.resourcePool,
			                  datacenter:opts.datacenter, folder:opts.folder, proxySettings: opts.proxySettings]
			def createResults = importOvfImage(apiUrl, username, password, insertOpts)
			if(createResults.success == true) {
				def vmResults = findVirtualMachine(apiUrl, username, password, image.name, opts)
				if(vmResults.success == true) {
					def vmImage = vmResults?.virtualMachine
					if(vmImage) {
						if(opts.noTemplate != true && vmImage.getConfig().template != true)
							vmImage.markAsTemplate()
						rtn.imageId = vmImage.getMOR().getVal()
						rtn.success = true
					}
				} else {
					rtn.msg = 'could not find template'
				}
			}
		} else {
			rtn.imageId = match.ref
			rtn.success = true
		}
		log.info("insertContainerImage results: ${rtn}")
		return rtn
	}

	static importOvfImage(apiUrl, username, password, opts = [:]) {
		def rtn = [:]
		def httpNfcLease
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password, true, true)
			def rootFolder = serviceInstance.getRootFolder()
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def cluster = opts.cluster ? new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster) : null
			def host = opts.hostId ? getManagedObject(serviceInstance, 'HostSystem', opts.hostId) : null
			def network = opts.networkId ? getManagedObject(serviceInstance, opts.networkBackingType ?: 'Network', opts.networkId) : null
			def datastore = opts.datastoreId ? getDatastoreOrStoragePodDatastore(serviceInstance,opts.datastoreId,null, ComputeUtility.ONE_GIGABYTE) : getFreeDatastore(host ?: cluster, ComputeUtility.ONE_GIGABYTE)
			def resourcePool = findResourcePool(serviceInstance, opts.resourcePool, datacenter, cluster, host)
			def imageFolder = opts.folder ? getManagedObject(serviceInstance, 'Folder', opts.folder) : null
			if(resourcePool) {
				log.info("importOvf resourcePool: ${resourcePool} datastore: ${datastore} file: ${opts.ovfFile}")
				//ovf url - download first
				def ovfFile = opts.ovfFile //url to ovf file
				def ovfResults = [success:false]
				def ovfRoot
				def ovfName
				CloudFile ovfCloudFile = findOvfFile(opts.cloudFiles) //opts.cloudFiles?.find { CloudFile cloudFile -> cloudFile.name.toLowerCase().endsWith(".ovf") }
				println("found ovs file: ${ovfCloudFile}")
				if(ovfCloudFile) {
					log.debug("ovfCloudFile: ${ovfCloudFile}")
					ovfResults = [success:true, ovf:ovfCloudFile?.getText()]
					ovfRoot = ovfCloudFile?.name?.substring(0, ovfCloudFile.name.lastIndexOf('/') + 1)
					ovfName = ovfCloudFile?.name?.substring(ovfCloudFile.name.lastIndexOf('/') + 1)?.replaceAll('.ovf', '')
				} else if(ovfFile) {
					log.debug("ovfFile: ${ovfFile}")
					ovfResults = downloadOvf(ovfFile)
					ovfRoot = ovfFile.substring(0, ovfFile.lastIndexOf('/') + 1)
					ovfName = ovfFile.substring(ovfFile.lastIndexOf('/') + 1)?.replaceAll('.ovf', '')
				}
				println("ovf text: ${ovfResults}")
				if(ovfResults.success == true && ovfResults.ovf) {
					def vmName = opts.vmName
					def networkName = findOvfNetworkName(ovfResults.ovf) //opts.networkName ?: 'Network 1'
					def vmList = host?.getVms()
					def vmFolder = imageFolder ?: datacenter.getVmFolder()
					def importSpecParams = new OvfCreateImportSpecParams()
					if(host)
						importSpecParams.setHostSystem(host.getMOR())
					if(network == null)
						network = findNetwork(serviceInstance, datacenter, cluster, host)
					importSpecParams.setLocale('US')
					importSpecParams.setDiskProvisioning('sparse')
					importSpecParams.setEntityName(vmName)
					importSpecParams.setDeploymentOption('')
					if(network.getName() && networkName) {
						log.info("translating network: ${networkName} to ${network.getName()}")
						def networkMapping = new OvfNetworkMapping()
						networkMapping.setName(networkName)
						networkMapping.setNetwork(network.getMOR())
						importSpecParams.setNetworkMapping([networkMapping] as OvfNetworkMapping[])
					}
					importSpecParams.setPropertyMapping(null)
					def ovfDescriptor = ovfResults.ovf
					log.info("import ovf - datastore: ${datastore} - ovfDesc:" + ovfDescriptor)
					def ovfImportResult = serviceInstance.getOvfManager().createImportSpec(ovfDescriptor, resourcePool, datastore, importSpecParams)
					if(ovfImportResult != null) {
						OvfFileItem[] fileItems = ovfImportResult.getFileItem()
						log.info("got: ${ovfImportResult} - files: ${fileItems} errors: ${ovfImportResult?.getError()?.collect { it.getLocalizedMessage() + ' ' }} warning: ${ovfImportResult.getWarning()?.collect { it.getLocalizedMessage() + ' ' }}")
						def totalBytes = 0
						ovfImportResult.getFileItem()?.each { fileItem ->
							printOvfFileItem(fileItem)
							totalBytes += fileItem.getSize()
						}
						log.info("Total bytes: " + totalBytes)
						httpNfcLease = resourcePool.importVApp(ovfImportResult.getImportSpec(), vmFolder, host)
						def hls
						def ready = false
						while(ready == false) {
							hls = httpNfcLease.getState()
							sleep(100)
							if(hls == HttpNfcLeaseState.ready || hls == HttpNfcLeaseState.error)
								ready = true
						}
						if(hls == HttpNfcLeaseState.ready) {
							log.info("HttpNfcLeaseState: ready ")
							def httpNfcLeaseInfo = httpNfcLease.getInfo()
							printHttpNfcLeaseInfo(httpNfcLeaseInfo)
							leaseUpdater = new VmwareLeaseProgressUpdater(httpNfcLease, 5000)
							leaseUpdater.start()
							def deviceUrls = httpNfcLeaseInfo.getDeviceUrl()
							long bytesAlreadyWritten = 0
							deviceUrls.eachWithIndex { deviceUrl, deviceIndex ->
								log.info("deviceUrl: ${deviceUrl} key: ${deviceUrl.getImportKey()} url: ${deviceUrl.getUrl()}")
								def deviceKey = deviceUrl.getImportKey()
								def fileItemMatch = ovfImportResult.fileItem?.find {
									(it.getDeviceId() == deviceKey)
								}
								if(fileItemMatch) {
									log.info("Import key==OvfFileItem device id: " + deviceKey)
									def absoluteFile = fileItemMatch.getPath()
									def srcUrl = ovfRoot + absoluteFile
									def tgtUrl = deviceUrl.getUrl()
									if(tgtUrl?.indexOf('*') > -1) {
										def hostName = host.getName()
										if(opts.hostIp)
											hostName = opts.hostIp
										tgtUrl = tgtUrl.replace("*", hostName)
									}
									log.debug "importOvfImage ovfCloudFile: ${ovfCloudFile}"
									if(ovfCloudFile) {
										log.info("importOvfImage src URL : ${srcUrl} - ${opts.cloudFiles}")
										def vmdkCloudFile = opts.cloudFiles.find { CloudFile cloudFile -> cloudFile.name == srcUrl }
										log.info "importOvfImage vmdkCloudFile: ${vmdkCloudFile}"
										uploadImage(vmdkCloudFile, tgtUrl, leaseUpdater, opts.cachePath)
									} else {
										log.info "importOvfImage srcUrl: ${srcUrl}"
										uploadImage(srcUrl, tgtUrl, leaseUpdater, opts.cachePath)
									}
									//uploadVmdkFile(ovfFileItem.isCreate(), absoluteFile, urlToPost, bytesAlreadyWritten, totalBytes)
									//bytesAlreadyWritten += ovfFileItem.getSize()
									log.info("Completed uploading the VMDK file:" + absoluteFile)
								} else {
									log.info("no file item returned from vmware - guessing")
									def srcUrl = "${ovfRoot}${ovfName}-disk${(deviceIndex + 1)}.vmdk"
									def tgtUrl = deviceUrl.getUrl()
									if(tgtUrl?.indexOf('*') > -1) {
										def hostName = host.getName()
										if(opts.hostIp)
											hostName = opts.hostIp
										tgtUrl = tgtUrl.replace("*", hostName)
									}
									if(ovfCloudFile) {
										def vmdkCloudFile = opts.cloudFiles.find { CloudFile cloudFile -> cloudFile.name == srcUrl }
										if(vmdkCloudFile)
											uploadImage(vmdkCloudFile, tgtUrl, leaseUpdater, opts.cachePath)
									} else {
										uploadImage(srcUrl, tgtUrl, leaseUpdater, opts.cachePath)
									}
								}
							}
							try {
								httpNfcLease.httpNfcLeaseProgress(100)
								leaseUpdater.interrupt()
								sleep(3000)
								httpNfcLease.httpNfcLeaseComplete()
							} catch(e2) {
								log.warn("error completing lease: ${e2}", e2)
							}
							rtn.success = true
						} else {
							rtn.msg = 'failed to get a lease to import vm image'
						}
					} else {
						rtn.msg = 'vm image import failed'
					}
				} else {
					rtn.msg = 'could not read ovf file'
				}
			} else {
				//no host
				rtn.msg = 'vmware host not specified'
			}
		} catch(InvalidType e) {
			log.error("registerExtension fault: ${e.argument} ${e}", e)
			try {
				leaseUpdater.interrupt()
				httpNfcLease.httpNfcLeaseAbort()
			} catch(e2) {
				log.error("error ending upload lease")
			}
		} catch(MethodFault e) {
			log.error("registerExtension fault: ${e.faultCause} ${e.faultMessage} ${e}", e)
			try {
				leaseUpdater.interrupt()
				httpNfcLease.httpNfcLeaseAbort()
			} catch(e2) {
				log.error("error ending upload lease")
			}
		} catch(e) {
			log.error("error: ${e}", e)
			try {
				leaseUpdater.interrupt()
				httpNfcLease.httpNfcLeaseAbort()
			} catch(e2) {
				log.error("error ending upload lease")
			}
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		log.debug("importOvfImage: ${rtn}")
		return rtn
	}

	static uploadImage(CloudFile cloudFile, String tgtUrl, leaseUpdator, cachePath = null, proxySettings = null) {
		log.info("uploadImage cloudFile: ${cloudFile.name} tgt: ${tgtUrl}")
		def rtn = [success: false]
		def usingCache = false
		def sourceStream
		Long totalCount
		try {
			if(cachePath) {
				def cacheFile = new File(cachePath, cloudFile.name)
				if(cacheFile.exists()) {
					totalCount = cacheFile.length()
					//if(totalCount == cloudFile.getContentLength()) {
					sourceStream = cacheFile.newInputStream()
					log.info("uploadImage from cache")
					//}
				}
				if(!sourceStream) {
					sourceStream = new CacheableInputStream(cloudFile.inputStream, cacheFile)
					totalCount = cloudFile.getContentLength()
				}
			} else {
				sourceStream = cloudFile.inputStream
				totalCount = cloudFile.getContentLength()
			}
			rtn = uploadImage(sourceStream, totalCount, tgtUrl, leaseUpdator,proxySettings)
		} catch(ex) {
			log.error("uploadImage cloudFile error: ${ex}", ex)
		} finally {
			try {
				sourceStream?.close()
			} catch(e) {
			}
		}
	}

	static uploadImage(String srcUrl, String tgtUrl, leaseUpdater, cachePath = null, proxySettings = null) {
		log.info("uploadImage src: ${srcUrl} tgt: ${tgtUrl}")
		def rtn = [success: false]
		def inboundClient
		def sourceStream
		def urlPath = new URL(srcUrl).path
		try {
			Long cacheCount
			Long totalCount
			if(cachePath) {
				def cacheFile = new File(cachePath, urlPath)
				if(cacheFile.exists()) {
					sourceStream = cacheFile.newInputStream()
					cacheCount = cacheFile.length()
				}
			}
			inboundClient = HttpClients.createDefault()
			def inboundGet = new HttpGet(srcUrl)
			def inboundResponse = inboundClient.execute(inboundGet)
			totalCount = inboundResponse.getEntity().getContentLength()
			if(sourceStream == null || totalCount != cacheCount) {
				sourceStream = inboundResponse.getEntity().getContent()
			} else {
				log.info("uploadImage from cache")
			}
			rtn = uploadImage(sourceStream, totalCount, tgtUrl, leaseUpdater,proxySettings)
		} catch(e) {
			log.error("uploadImage error: ${e}", e)
		} finally {
			inboundClient?.close()
			try {
				sourceStream?.close()
			} catch(ex) {
			}
		}
		return rtn
	}

	static uploadImage(InputStream sourceStream, Long contentLength, String tgtUrl, leaseUpdater,proxySettings=null) {
		log.info("uploadImage stream size: ${contentLength} tgt: ${tgtUrl}")
		def outboundClient
		def rtn = [success: false]
		try {
			def outboundSslBuilder = new SSLContextBuilder()
			outboundSslBuilder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					return true
				}
			})
			def outboundSocketFactory = new SSLConnectionSocketFactory(outboundSslBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
			def clientBuilder = HttpClients.custom().setSSLSocketFactory(outboundSocketFactory)
			clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
				boolean verify(String host, SSLSession sess) { return true }

				void verify(String host, SSLSocket ssl) {}

				void verify(String host, String[] cns, String[] subjectAlts) {}

				void verify(String host, X509Certificate cert) {}
			})
			if(proxySettings) {
				def proxyHost = proxySettings.proxyHost
				def proxyPort = proxySettings.proxyPort
				if(proxyHost && proxyPort) {
					log.info "proxy detected for image upload ${proxyHost}:${proxyPort}"
					def proxyUser = proxySettings.proxyUser
					def proxyPassword = proxySettings.proxyPassword
					def proxyWorkstation = proxySettings.proxyWorkstation ?: null
					def proxyDomain = proxySettings.proxyDomain ?: null
					clientBuilder.setProxy(new HttpHost(proxyHost, proxyPort))
					if(proxyUser) {
						CredentialsProvider credsProvider = new BasicCredentialsProvider()
						NTCredentials ntCreds = new NTCredentials(proxyUser, proxyPassword, proxyWorkstation, proxyDomain)
						credsProvider.setCredentials(new AuthScope(proxyHost, proxyPort), ntCreds)
						clientBuilder.setDefaultCredentialsProvider(credsProvider)
						clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy())
					}
				}
			}
			outboundClient = clientBuilder.build()
			def outboundPut = new HttpPost(tgtUrl)
			def vmInputStream = new VmwareLeaseProgressInputStream(new BufferedInputStream(sourceStream, 1200), leaseUpdater, contentLength)
			def inputEntity = new InputStreamEntity(vmInputStream, contentLength)
			outboundPut.addHeader('Connection', 'Keep-Alive')
			if(tgtUrl.endsWith('.iso')) {
				outboundPut.addHeader('Content-Type', 'application/octet-stream')
			} else {
				outboundPut.addHeader('Content-Type', 'application/x-vnd.vmware-streamVmdk')
			}

			outboundPut.setEntity(inputEntity)
			def responseBody = outboundClient.execute(outboundPut)
			if(responseBody.statusLine.statusCode < 400) {
				rtn.success = true
			} else {
				rtn.success = false
				rtn.msg = "Upload Image Error HTTP:${responseBody.statusLine.statusCode}"
			}
		} catch(e) {
			log.error("uploadImage From Stream error: ${e}", e)
		} finally {
			outboundClient.close()
		}
		return rtn
	}

	static getDatastoreOrStoragePodDatastore(serviceInstance, datastoreId, vm, diskSize) {
		def rtn
		//check if we have a datastore
		try {
			rtn = getManagedObject(serviceInstance, 'Datastore', datastoreId)
			//test name to see if its found
			def testName = rtn.getName()
		} catch(e) {
			//datastore not found
			rtn = null
		}
		if(rtn == null) {
			//check if we have a storage pod
			try {
				rtn = getManagedObject(serviceInstance, 'StoragePod', datastoreId)
				//test name to see if its found
				def testName = rtn.getName()
				//get a free datastore the vm is already using
				def vmDatastore = vm ? getFreeDatastore(vm, diskSize) : null
				log.debug("vm datastore: ${vmDatastore}")
				rtn = getFreeStoragePodDatastore(rtn, diskSize, vmDatastore?.getMOR()?.getVal())
				log.debug("got datastore from storage pod: ${rtn}")
			} catch(e) {
				//storage pod not found
				rtn = null
			}
		}
		return rtn
	}

	static getFreeStoragePodDatastore(storagePod, size, datastoreId = null, forced = false) {
		def rtn
		def datastores = storagePod.getChildEntity()?.findAll{it instanceof com.vmware.vim25.mo.Datastore}
		def datastoreList = datastores.collect {store -> [datastore:store, accessible:store.getSummary().isAccessible(), freeSpace:store.getSummary().getFreeSpace(),
		                                                  capacity:store.getSummary().getCapacity(), type:store.getSummary().getType(), ref:store.getMOR().getVal(),
		                                                  availablePercent: (store.getSummary().getFreeSpace() ?: 0) / (store.getSummary().getCapacity() ?: 1)]}
		def availableList = datastoreList?.findAll{it.accessible == true && it.freeSpace > size}
		availableList?.sort{-it.availablePercent}
		if(datastoreId) {
			def dsMatch = availableList.find{it.ref == datastoreId}
			if(dsMatch) {
				rtn = dsMatch.datastore
				log.debug("picked requested datastore for storage pod - ${rtn}")
			}
		}
		if(rtn == null) {
			def dsMap = availableList?.first()
			rtn = dsMap?.datastore
			log.debug("picked datastore for storage pod - ${dsMap}")
		}
		return rtn
	}

	static getFreeDatastore(vm, size, datastoreId = null, forced = false) {
		def rtn
		def datastores = vm.getDatastores()
		def datastoreList = datastores.collect {store -> [datastore:store, accessible:store.getSummary().isAccessible(), freeSpace:store.getSummary().getFreeSpace(), name: store.name,
		                                                  capacity:store.getSummary().getCapacity(), type:store.getSummary().getType(), ref:store.getMOR().getVal(),
		                                                  availablePercent: (store.getSummary().getFreeSpace() ?: 0) / (store.getSummary().getCapacity() ?: 1)]}
		def availableList = datastoreList?.findAll{it.accessible == true && it.freeSpace > size}
		availableList = availableList?.findAll{!it.name.startsWith('rubrik_')}?.sort{-it.availablePercent}
		if(datastoreId) {
			def dsMatch = availableList.find{it.ref == datastoreId}
			if(dsMatch) {
				rtn = dsMatch.datastore
				log.debug("picked requested datastore for vm - ${rtn}")
			}
		}
		if(rtn == null) {
			def dsMap = availableList?.first()
			rtn = dsMap?.datastore
			log.debug("picked datastore for vm - ${dsMap}")
		}
		return rtn
	}

	static getLastPath(String path) {
		def rtn = path
		if(path) {
			def tokens = path.tokenize('/')
			if(tokens.size() > 0)
				rtn = tokens[tokens.size() - 1]
		}
		return rtn
	}

	static findResourcePool(serviceInstance, resourcePoolId, datacenter, cluster, host) {
		def rtn
		if(resourcePoolId) {
			rtn = getManagedObject(serviceInstance, 'ResourcePool', resourcePoolId)
		} else {
			if(host)
				rtn = host.getParent().getResourcePool()
			else if(cluster)
				rtn = cluster.getResourcePool()
		}
		return rtn
	}

	static findNetwork(serviceInstance, datacenter, cluster, host) {
		def rtn
		if(host)
			rtn = pickNetwork(host.getNetworks())
		if(rtn == null && cluster)
			rtn = pickNetwork(cluster.getNetworks())
		if(rtn == null && datacenter)
			rtn = pickNetwork(datacenter.getNetworks())
		return rtn
	}

	static pickNetwork(networkList) {
		def rtn
		if(networkList?.size() > 0) {
			def distributedList = networkList.findAll{it instanceof DistributedVirtualPortgroup}
			if(distributedList?.size() > 0)
				rtn = distributedList.first()
			else
				rtn = networkList.first()
		}
		return rtn
	}


	static downloadOvf(ovfSrc) {
		def rtn = [success: false]
		def inboundClient
		try {
			inboundClient = HttpClients.createDefault()
			def inboundGet = new HttpGet(ovfSrc)
			def inboundResponse = inboundClient.execute(inboundGet)
			def inputEntity = new InputStreamEntity(new BufferedInputStream(inboundResponse.getEntity().getContent(), 1200), inboundResponse.getEntity().getContentLength())
			def inputStream = inputEntity.getContent()
			def output = new ByteArrayOutputStream()
			def buffer = new byte[1024]
			int len
			while((len = inputStream.read(buffer)) != -1) {
				output.write(buffer, 0, len)
			}
			rtn.ovf = output.toString()
			rtn.success = true
		} catch(e) {
			log.error("downloadOvf error: ${e}", e)
		} finally {
			inboundClient.close()
		}
		return rtn
	}

	static findOvfNetworkName(ovfContent) {
		def rtn = 'nat'
		try {
			def connectStart = ovfContent.indexOf('<rasd:Connection>')
			def connectEnd = ovfContent.indexOf('</rasd:Connection>')
			if(connectStart > -1 && connectEnd > -1)
				rtn = ovfContent.substring(connectStart + 17, connectEnd)
		} catch(e) {
			log.error("findOvfNetworkName error: ${e}", e)
		}
		return rtn
	}

	static printHttpNfcLeaseInfo(HttpNfcLeaseInfo info) {
		log.debug("================ HttpNfcLeaseInfo ================")
		HttpNfcLeaseDeviceUrl[] deviceUrlArr = info.getDeviceUrl()
		for(HttpNfcLeaseDeviceUrl durl : deviceUrlArr) {
			log.debug("Device URL Import Key: " + durl.getImportKey())
			log.debug("Device URL Key: " + durl.getKey())
			log.debug("Device URL : " + durl.getUrl())
			log.debug("Updated device URL: " + durl.getUrl())
		}
		log.debug("Lease Timeout: " + info.getLeaseTimeout())
		log.debug("Total Disk capacity: " + info.getTotalDiskCapacityInKB())
		log.debug("==================================================")
	}

	static createVm(apiUrl, username, password, opts=[:]) {
		def rtn = [success:false, modified:false]
		def serviceInstance
		try {
			def vmName = opts.name
			def maxMemory = opts.maxMemory.toLong() //MB convert
			def cpuCount = opts.cpuCount?.toInteger() ?: 1
			def coresPerSocket = opts.coresPerSocket?.toInteger() ?: 1
			//setup
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def datastore = opts.datastoreId ? getManagedObject(serviceInstance, 'Datastore', opts.datastoreId) : null
			def datastoreName = datastore?.name ?: 'storage1'
			def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
			def resourcePool = opts.resourcePool ? getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool) : null
			def folder = opts.folder ? getManagedObject(serviceInstance, 'Folder', opts.folder) : null
			def vmFolder = folder ?: datacenter.getVmFolder()
			def vmSpec = new VirtualMachineConfigSpec()
			def thin = (opts.storageType != 'thickEager' && opts.storageType != 'thick')
			vmSpec.setName(vmName)
			vmSpec.setMemoryMB(maxMemory)
			vmSpec.setNumCPUs(cpuCount)
			vmSpec.setNumCoresPerSocket(coresPerSocket)
			vmSpec.setGuestId('otherGuest64')
			//extra config
			if(opts.extraConfig) {
				vmSpec.setExtraConfig(opts.extraConfig as OptionValue[])
				log.debug("set extra config: ${opts.extraConfig}")
			}

			if(opts.nestedVirtualization) {
				vmSpec.setNestedHVEnabled(opts.nestedVirtualization)
			}
			def vmFileInfo = new VirtualMachineFileInfo()
			vmFileInfo.setVmPathName("[" + datastoreName + "]" + vmName)
			vmSpec.setFiles(vmFileInfo)
			def deviceChangeArray = []
			def virtualDevices = [] //fresh vm so dont have any existing
			//network config
			if(opts.networkConfig?.primaryInterface?.network?.externalId) { //new style multi network
				def primaryInterface = opts.networkConfig.primaryInterface
				def nicSpec
				def networkBackingType = primaryInterface.network.externalType != 'string' ? (primaryInterface.network.externalType ?: primaryInterface.network.parentNetwork?.externalType) : 'Network'
				def vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', primaryInterface.network.externalId ?: primaryInterface.network.parentNetwork?.externalId)
				if(vmNetwork)
					nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, primaryInterface, 0, false,
							[switchUuid:primaryInterface.network.internalId ?: primaryInterface.network.parentNetwork?.internalId])
				if(nicSpec)
					deviceChangeArray << nicSpec
				//additional nics
				def extraIndex = 1
				opts.networkConfig.extraInterfaces?.each { extraInterface ->
					log.debug("Provisioning extra interface ${extraInterface}")
					if(extraInterface.network?.externalId) {
						nicSpec = null
						networkBackingType = extraInterface.network.externalType != 'string' ? (extraInterface.network.externalType ?: extraInterface.network.parentNetwork?.externalType) : 'Network'
						vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', extraInterface.network.externalId) ?: extraInterface.network.parentNetwork?.externalId
						if(vmNetwork)
							nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, extraInterface, extraIndex, false,
									[switchUuid:extraInterface.network?.internalId ?: extraInterface.network.parentNetwork?.internalId])
						if(nicSpec)
							deviceChangeArray << nicSpec
						extraIndex++
					}
				}
			}
			def newControllerSpec = new VirtualDeviceConfigSpec()
			def newController = createController('vmware-lsiLogic')
			def busNumber = findFreeBusNumber(virtualDevices, 0)
			newController.setKey(-1)
			newController.setBusNumber(busNumber)
			newController.setSharedBus(VirtualSCSISharing.noSharing)
			newControllerSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
			newControllerSpec.setDevice(newController)
			deviceChangeArray << newControllerSpec
			def dsName = datastore ? datastore.getName() : null
			def fileName = vmName + "/" + (opts.diskName ?: 'morpheus_data') + ".vmdk"
			if(dsName) {
				fileName = "[" + dsName + "] " + vmName + "/" + (opts.diskName ?: 'morpheus_data') + ".vmdk"
			}
			def virtualDisk = new VirtualDisk()
			def diskfileBacking = getDiskfileBacking(virtualDevices, thin)
			def diskSpec = new VirtualDeviceConfigSpec()
			diskfileBacking.setDiskMode(opts.diskMode ?: 'persistent')
			diskfileBacking.setFileName(fileName)
			virtualDisk.setControllerKey(-1)
			virtualDisk.setUnitNumber(0)
			virtualDisk.setBacking(diskfileBacking)
			virtualDisk.setCapacityInKB(1024 * 1024)
			virtualDisk.setKey(-1)
			diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
			diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create)
			diskSpec.setDevice(virtualDisk)
			deviceChangeArray << diskSpec
			if(deviceChangeArray?.size() > 0) {
				deviceChangeArray = deviceChangeArray.flatten()
				vmSpec.setDeviceChange(deviceChangeArray as VirtualDeviceConfigSpec[])
			}
			def vmResult
			def host = opts.hostId ? getManagedObject(serviceInstance, 'HostSystem', opts.hostId) : null
			def vmTask
			if(opts.storagePodId) {
				def storagePod = getManagedObject(serviceInstance, 'StoragePod', opts.storagePodId)
				log.info("pod: ${storagePod.getMOR()} - resource pool: ${resourcePool.getMOR()} respool - ${resourcePool.getMOR()} ${vmFolder.getMOR()}")
				def podSpec = new StorageDrsPodSelectionSpec()
				podSpec.setStoragePod(storagePod.getMOR())
				def storagePlacementSpec = new StoragePlacementSpec()
				storagePlacementSpec.setCloneName(vmName)
				storagePlacementSpec.setConfigSpec(vmSpec)
				storagePlacementSpec.setType('create')
				storagePlacementSpec.setPodSelectionSpec(podSpec)
				storagePlacementSpec.setFolder(vmFolder.getMOR())
				storagePlacementSpec.setResourcePool(resourcePool.getMOR())
				if(host)
					storagePlacementSpec.setHost(host.getMOR())
				def serviceContent = serviceInstance.retrieveServiceContent()
				def storageManager = getManagedObject(serviceInstance, 'StorageResourceManager', serviceContent.getStorageResourceManager().getVal())
				def storagePlacementResult = storageManager.recommendDatastores(storagePlacementSpec)
				log.debug("storagePlacementResult: ${storagePlacementResult}")
				def cloneRecs = storagePlacementResult.getRecommendations()
				def rerunRecommendation = false
				cloneRecs.each { cloneRec ->
					log.info("Clone Recommendation: ${cloneRec.getKey()} - ${cloneRec.target}")
					cloneRec.action?.each { action ->
						if(action instanceof  StoragePlacementAction) {
							log.info("- Found Storage Placement Action - ${action.destination?.val}")
							datastore = action.destination?.val ? getManagedObject(serviceInstance, 'Datastore', action.destination?.val) : null
							dsName = datastore?.getName() ?: null
							if(dsName) {
								fileName = "[" + dsName + "] " + vmName + "/" + (opts.diskName ?: 'morpheus_data') + ".vmdk"
								log.info("Changing diskFileBacking ${fileName}")
								diskfileBacking.setFileName(fileName)
								rerunRecommendation = true
							}
						}
					}
				}
				if(rerunRecommendation) {
					storagePlacementResult = storageManager.recommendDatastores(storagePlacementSpec)
					cloneRecs = storagePlacementResult.getRecommendations()
				}
				def cloneRec = cloneRecs?.length > 0 ? cloneRecs[0] : null
				def cloneKey = cloneRec?.getKey()
				if(cloneKey) {
					def keySet = [cloneKey] as String[]
					vmTask = storageManager.applyStorageDrsRecommendation_Task(keySet)
					vmResult = retryWaitForTask(vmTask)
				} else {
					rtn.msg = 'Error creating vm clone'
				}
			} else {
				vmTask = vmFolder.createVM_Task(vmSpec,resourcePool,host?.getMOR())
				vmResult = retryWaitForTask(vmTask)
			}
			//finished - proceed
			if(vmResult == Task.SUCCESS) {
				rtn.success = true
				def newVm
				def newVmId = getVirtualMachineIdFromTask(vmTask)

				if (newVmId) {
					newVm = getManagedObject(serviceInstance, 'VirtualMachine', newVmId)
				} else {
					newVm = new InventoryNavigator(rootFolder).searchManagedEntity('VirtualMachine', vmName)
				}

				rtn.results = [server:[id:newVm.getMOR().getVal(), name:vmName, instanceUuid: newVm.getConfig().getInstanceUuid()], entity:newVm, volumes:getVmVolumes(newVm.getConfig().getHardware().getDevice()),
				               controllers:getVmControllers(newVm.getConfig().getHardware().getDevice()), networks:getVmNetworks(newVm.getConfig().getHardware().getDevice())]
			} else {
				rtn.msg = 'Error creating vm clone - ' + vmResult
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			rtn.msg = 'error creating vm'
			log.error("createVm error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static cloneVmFromContentLibrary(apiUrl, username, password, HttpApiClient client, opts = [:]) {
		log.info("Launching VM From OVF Library!")
		def rtn = [success:false, modified:false]
		def serviceInstance

		def sessionId
		def apiResults
		def url = privateGetSchemeHostAndPort(apiUrl)

		try {
			def vmName = opts.name
			def vmTemplate = opts.template
			def snapshotId = opts.snapshot ?: opts.snapshotId
			def maxMemory = opts.maxMemory.toLong() //MB convert
			def maxStorage = (opts.maxStorage.toLong().div(1024)) //KB
			def cpuCount = opts.cpuCount?.toInteger() ?: 1
			def coresPerSocket = opts.coresPerSocket?.toInteger() ?: 1
			//setup
			serviceInstance = connectionPool.getConnection(apiUrl, username, password,true,true)
			def sessionResults = getRestSessionId(apiUrl,username,password,opts)
			sessionId = sessionResults.sessionId

			//do deploy from rest
			Boolean thin = true
			if(opts.storageType) {
				thin = opts.storageType == 'thin'
			}

			def body = [
					deployment_spec: [
							accept_all_EULA: true,
							name: vmName,

							storage_provisioning: thin ? 'thin' : 'thick'
					],
					target: [
							resource_pool_id: opts.resourcePool,
					]


			]
			if(opts.datastoreId) {
				body.deployment_spec.default_datastore_id = opts.datastoreId
			} else if(opts.storagePodId) {
				body.deployment_spec.default_datastore_id = opts.storagePodId
			}
			log.info("Body submission: ${body}")
			if(opts.folder) {
				body.target.folder_id = opts.folder
			}
			if(opts.hostId) {
				body.target.host_id = opts.hostId
			}
			apiResults = client.callJsonApi(url,"/rest/com/vmware/vcenter/ovf/library-item/id:${opts.imageId}",new HttpApiClient.RequestOptions([headers: ["vmware-api-session-id": sessionId],query: ['~action':'deploy'],body:body,reuse:true, readTimeout: (60*60000).toInteger(), proxySettings: opts.proxySettings]),'POST')
			def cloneResults = apiResults.data.value
			log.info("Clone Results: ${apiResults}")
			if(cloneResults.succeeded) {
				def vmId = cloneResults.resource_id.id
				VirtualMachine vm = getManagedObject(serviceInstance, 'VirtualMachine', vmId)
				def vmConfig = vm.getConfig()
				def virtualDevices = vmConfig.getHardware().getDevice()
				def vmSpec = new VirtualMachineConfigSpec()
				vmSpec.setMemoryMB(maxMemory)
				vmSpec.setNumCPUs(cpuCount)
				vmSpec.setNumCoresPerSocket(coresPerSocket)
				//annotations
				if(opts.annotation) {
					vmSpec.setAnnotation(opts.annotation)
				}
				//extra config
				if(opts.extraConfig) {
					vmSpec.setExtraConfig(opts.extraConfig as OptionValue[])
					log.debug("set extra config: ${opts.extraConfig}")
				}
				if(opts.nestedVirtualization) {
					vmSpec.setNestedHVEnabled(opts.nestedVirtualization)
				} else {
					vmSpec.setNestedHVEnabled(false)
				}

				def deviceChangeArray = []
				if(opts.networkConfig?.primaryInterface?.network?.externalId) { //new style multi network
					def primaryInterface = opts.networkConfig.primaryInterface
					def nicSpec
					def networkBackingType = primaryInterface.network.externalType != 'string' ? primaryInterface.network.externalType : 'Network'
					def vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', primaryInterface.network.externalId ?: primaryInterface.network.parentNetwork?.externalId)
					if(vmNetwork)
						nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, primaryInterface, 0, false,
								[switchUuid:primaryInterface.network.internalId ?: primaryInterface.network.parentNetwork?.internalId])
					if(nicSpec)
						deviceChangeArray << nicSpec
					//additional nics
					def extraIndex = 1
					opts.networkConfig.extraInterfaces?.each { extraInterface ->
						log.debug("Provisioning extra interface ${extraInterface}")
						if(extraInterface.network?.externalId) {
							nicSpec = null
							networkBackingType = extraInterface.network.externalType != 'string' ? extraInterface.network.externalType : 'Network'
							vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', extraInterface.network.externalId ?: extraInterface.network.parentNetwork.externalId)
							if(vmNetwork)
								nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, extraInterface, extraIndex, false,
										[switchUuid:extraInterface.network?.internalId ?: extraInterface.network.parentNetwork?.internalId])
							if(nicSpec)
								deviceChangeArray << nicSpec
							extraIndex++
						}
					}
				} else {
					def nicSpec
					if(opts.networkId) {
						def vmNetwork = getManagedObject(serviceInstance, opts.networkBackingType ?: 'Network', opts.networkId)
						if(vmNetwork)
							nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, null)
					}
					if(nicSpec)
						deviceChangeArray << nicSpec
				}
				deviceChangeArray = deviceChangeArray.flatten()
				if(deviceChangeArray?.size() > 0)
					vmSpec.setDeviceChange(deviceChangeArray as VirtualDeviceConfigSpec[])
				def vmTask = vm.reconfigVM_Task(vmSpec)
				def reconfigResult = vmTask.waitForTask()
				if(reconfigResult == Task.SUCCESS) {
					rtn.success = true
					rtn.results = [server:[id:vm.getMOR().getVal(), name:vmName, instanceUuid: vm.getConfig().getInstanceUuid()],
					               entity:vm, volumes:getVmVolumes(vm.getConfig().getHardware().getDevice()),
					               controllers:getVmControllers(vm.getConfig().getHardware().getDevice()),
					               networks:getVmNetworks(vm.getConfig().getHardware().getDevice())
					]
					if(opts.guestCustUnattend && rtn.results.networks) {
						String macAddress = rtn.results.networks.first().macAddress
						opts.guestCustUnattend = opts.guestCustUnattend.replace('Ethernet0',macAddress.replace(':','-'))
					}
				}
				def customResults = addVmCustomizations(serviceInstance, apiUrl, username, password, opts)

				rtn.customized = customResults.modified

				if(customResults?.success == true) {
					if(customResults.modified) {
						vm.customizeVM_Task(customResults.customSpec)
					}

					rtn.modified = customResults.modified
					rtn.results.server.ipAddressList = customResults.ipAddressList
					rtn.results.server.ipAddress = customResults.ipAddress
					rtn.results.server.networkPoolId = customResults.networkPoolId
				}

			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			rtn.msg = 'error creating vm'
			log.error("cloneVm error: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(client, url, sessionId)
			}
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl, username, password, serviceInstance)}
		}
		return rtn
	}

	static cloneVm(apiUrl, username, password, opts = [:]) {
		log.debug("launch vm from template: ${opts}")
		def rtn = [success:false, modified:false]
		def serviceInstance
		try {
			def vmName = opts.name
			def vmTemplate = opts.template
			def snapshotId = opts.snapshot ?: opts.snapshotId
			def maxMemory = opts.maxMemory.toLong() //MB convert
			def maxStorage = (opts.maxStorage.toLong().div(1024)) //KB
			def cpuCount = opts.cpuCount?.toInteger() ?: 1
			def coresPerSocket = opts.coresPerSocket?.toInteger() ?: 1
			//setup
			serviceInstance = connectionPool.getConnection(apiUrl, username, password,true,true)
			def rootFolder = serviceInstance.getRootFolder()
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
			def resourcePool = opts.resourcePool ? VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool) : null
			def folder = opts.folder ? getManagedObject(serviceInstance, 'Folder', opts.folder) : null
			def vmFolder = folder ?: datacenter.getVmFolder()
			def snapshot = snapshotId ? getManagedObject(serviceInstance, 'VirtualMachineSnapshot', snapshotId) : null
			VirtualMachine vm
			if(opts.cloneVmId) {
				log.info("cloning vmware vm ${opts.cloneVmId}")
				vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.cloneVmId)
			} else {
				log.info("cloning vmware template ${vmTemplate}")
				vm = getManagedObject(serviceInstance, 'VirtualMachine', vmTemplate)
			}
			def cloneSpec = new VirtualMachineCloneSpec()
			cloneSpec.setPowerOn(false)
			cloneSpec.setTemplate(false)
			def vmSpec = new VirtualMachineConfigSpec()
			vmSpec.setMemoryMB(maxMemory)
			vmSpec.setNumCPUs(cpuCount)
			vmSpec.setNumCoresPerSocket(coresPerSocket)
			//annotations
			if(opts.annotation) {
				vmSpec.setAnnotation(opts.annotation)
			}
			//extra config
			if(opts.extraConfig) {
				vmSpec.setExtraConfig(opts.extraConfig as OptionValue[])
				log.debug("set extra config: ${opts.extraConfig}")
			}
			if(opts.nestedVirtualization) {
				vmSpec.setNestedHVEnabled(opts.nestedVirtualization)
			} else {
				vmSpec.setNestedHVEnabled(false)
			}
			//snapshot
			if(snapshot) {
				log.info("Snapshot Set")
				cloneSpec.setSnapshot(snapshot?.getMOR())
			}

			VirtualMachineRelocateSpec relocateSpec = new VirtualMachineRelocateSpec()

			//set disk transforms
			if(opts.linkedClone) {
				log.info("Initiating Linked Clone")
				relocateSpec.setDiskMoveType("createNewChildDiskBacking")
			} else if(opts.storageType) {
				def thin = opts.storageType == 'thin'
				def vmVols = getVmVolumes(vm.config.hardware.device)
				if(thin && !vmVols.every{it.thin}) {
					log.info("Applying Thin Provision Spec")
					relocateSpec.setTransform(VirtualMachineRelocateTransformation.sparse)
				} else if(!thin && !vmVols.every{!it.thin}) {
					log.info("Applying Thick Provision Spec")
					relocateSpec.setTransform(VirtualMachineRelocateTransformation.flat)
				}
			}
			//set resource pool
			if(resourcePool)
				relocateSpec.setPool(resourcePool.getMOR())
			//apply location
			cloneSpec.setLocation(relocateSpec)
			def host = opts.hostId ? getManagedObject(serviceInstance, 'HostSystem', opts.hostId) : null
			def datastore = opts.datastoreId ? getManagedObject(serviceInstance, 'Datastore', opts.datastoreId) : null

			def vmResult
			def vmTask

			if(opts.storagePodId && !opts.linkedClone) {
				def storagePod = getManagedObject(serviceInstance, 'StoragePod', opts.storagePodId)
				log.info("pod: ${storagePod.getMOR()} - resource pool: ${resourcePool?.getMOR()} respool - ${resourcePool?.getMOR()} ${vmFolder.getMOR()}")
				def podSpec = new StorageDrsPodSelectionSpec()
				podSpec.setStoragePod(storagePod.getMOR())
				def storagePlacementSpec = new StoragePlacementSpec()
				storagePlacementSpec.setCloneName(vmName)
				storagePlacementSpec.setCloneSpec(cloneSpec)
				storagePlacementSpec.setType('clone')
				storagePlacementSpec.setPodSelectionSpec(podSpec)
				storagePlacementSpec.setFolder(vmFolder.getMOR())
				storagePlacementSpec.setVm(vm.getMOR())
				if(resourcePool)
					storagePlacementSpec.setResourcePool(resourcePool.getMOR())
				if(host)
					storagePlacementSpec.setHost(host.getMOR())
				def serviceContent = serviceInstance.retrieveServiceContent()
				def storageManager = getManagedObject(serviceInstance, 'StorageResourceManager', serviceContent.getStorageResourceManager().getVal())
				def storagePlacementResult = storageManager.recommendDatastores(storagePlacementSpec)
				def cloneRecs = storagePlacementResult.getRecommendations()
				def cloneRec = cloneRecs?.length > 0 ? cloneRecs[0] : null
				def cloneKey = cloneRec?.getKey()
				if(cloneKey) {
					def keySet = [cloneKey] as String[]
					vmTask = storageManager.applyStorageDrsRecommendation_Task(keySet)
					vmResult = retryWaitForTask(vmTask)
				} else {
					rtn.msg = 'Error creating vm clone'
				}
			} else {
				if(host)
					relocateSpec.setHost(host.getMOR())
				def targetDatastore

				if(!opts.linkedClone) {
					if(datastore) {
						targetDatastore = datastore.getMOR()
					} else {
						if(host)
							targetDatastore = getFreeDatastore(host, maxStorage * 1024l).getMOR()
						else if(cluster)
							targetDatastore = getFreeDatastore(cluster, maxStorage * 1024l).getMOR()
					}
					relocateSpec.setDatastore(targetDatastore)
				}

				//clone it
				vmTask = vm.cloneVM_Task(vmFolder, vmName, cloneSpec)
				vmResult = retryWaitForTask(vmTask)
			}
			if(vmResult == Task.SUCCESS) {
				rtn.success = true

				def newVm
				def newVmId = getVirtualMachineIdFromTask(vmTask)

				if (newVmId) {
					newVm = getManagedObject(serviceInstance, 'VirtualMachine', newVmId)
				} else {
					newVm = new InventoryNavigator(rootFolder).searchManagedEntity('VirtualMachine', vmName)
				}

				def vmConfig = newVm.getConfig()
				def virtualDevices = vmConfig.getHardware().getDevice()
				def deviceChangeArray = []
				if(opts.networkConfig?.primaryInterface?.network?.externalId) { //new style multi network
					def primaryInterface = opts.networkConfig.primaryInterface
					def nicSpec
					def networkBackingType = primaryInterface.network.externalType != 'string' ? primaryInterface.network.externalType : 'Network'
					def vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', primaryInterface.network.externalId ?: primaryInterface.network.parentNetwork?.externalId)
					if(vmNetwork)
						nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, primaryInterface, 0, false,
								[switchUuid:primaryInterface.network.internalId ?: primaryInterface.network.parentNetwork?.internalId])
					if(nicSpec)
						deviceChangeArray << nicSpec
					//additional nics
					def extraIndex = 1
					opts.networkConfig.extraInterfaces?.each { extraInterface ->
						log.debug("Provisioning extra interface ${extraInterface}")
						if(extraInterface.network?.externalId) {
							nicSpec = null
							networkBackingType = extraInterface.network.externalType != 'string' ? extraInterface.network.externalType : 'Network'
							vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', extraInterface.network.externalId ?: extraInterface.network.parentNetwork.externalId)
							if(vmNetwork)
								nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, extraInterface, extraIndex, false,
										[switchUuid:extraInterface.network?.internalId ?: extraInterface.network.parentNetwork?.internalId])
							if(nicSpec)
								deviceChangeArray << nicSpec
							extraIndex++
						}
					}
				} else {
					def nicSpec
					if(opts.networkId) {
						def vmNetwork = getManagedObject(serviceInstance, opts.networkBackingType ?: 'Network', opts.networkId)
						if(vmNetwork)
							nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, null)
					}
					if(nicSpec)
						deviceChangeArray << nicSpec
				}
				deviceChangeArray = deviceChangeArray.flatten()
				if(deviceChangeArray?.size() > 0)
					vmSpec.setDeviceChange(deviceChangeArray as VirtualDeviceConfigSpec[])

				vmTask = newVm.reconfigVM_Task(vmSpec)
				def reconfigResult = vmTask.waitForTask()
				if(reconfigResult == Task.SUCCESS) {
					rtn.success = true
					rtn.results = [server:[id:newVm.getMOR().getVal(), name:vmName, instanceUuid: newVm.getConfig().getInstanceUuid()],
					               entity:newVm, volumes:getVmVolumes(newVm.getConfig().getHardware().getDevice()),
					               controllers:getVmControllers(newVm.getConfig().getHardware().getDevice()),
					               networks:getVmNetworks(newVm.getConfig().getHardware().getDevice())
					]
					if(opts.guestCustUnattend && rtn.results.networks) {
						String macAddress = rtn.results.networks.first().macAddress
						opts.guestCustUnattend = opts.guestCustUnattend.replace('Ethernet0',macAddress.replace(':','-'))
					}
				}
				def customResults = addVmCustomizations(serviceInstance, apiUrl, username, password, opts)

				rtn.customized = customResults.modified

				if(customResults?.success == true) {
					if(customResults.modified) {
						newVm.customizeVM_Task(customResults.customSpec)
					}

					rtn.modified = customResults.modified
					rtn.results.server.ipAddressList = customResults.ipAddressList
					rtn.results.server.ipAddress = customResults.ipAddress
					rtn.results.server.networkPoolId = customResults.networkPoolId
				}
			} else {

				def taskFault = vmTask?.getTaskInfo()?.getError()?.getFault()
				//don't map a instance to an existing vm - deleting the instance will remove the wrong vm
				if (!(taskFault instanceof DuplicateName)) {
					def newVm = new InventoryNavigator(rootFolder).searchManagedEntity('VirtualMachine', vmName)
					if(newVm) {
						rtn.results = [server:[id:newVm.getMOR().getVal(), name:vmName, instanceUuid: newVm.getConfig().getInstanceUuid()],
						               entity:newVm, volumes:getVmVolumes(newVm.getConfig().getHardware().getDevice()),
						               controllers:getVmControllers(newVm.getConfig().getHardware().getDevice()),
						               networks:getVmNetworks(newVm.getConfig().getHardware().getDevice())
						]
					}
				}
				rtn.msg = 'Error creating vm clone - ' + vmResult
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			rtn.msg = 'error creating vm'
			log.error("cloneVm error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl, username, password, serviceInstance)}
		}
		return rtn
	}

	static resizeVmDisk(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			int diskSize = opts.diskSize?.toInteger()
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			def diskSpec
			def diskIndex = opts.diskIndex ?: 0
			def doResize = false
			def diskKeyMatch = opts.diskKey ? opts.diskKey.toInteger() : null
			def found = false
			def currentDisk = 0
			def diskDevices = virtualDevices?.findAll{it instanceof VirtualDisk}?.sort{a,b -> a.getKey().toInteger() <=> b.getKey().toInteger()}

			diskDevices?.eachWithIndex { virtualDevice, i ->
				log.debug("virtual device: ${virtualDevice?.getClass()?.getName()} - key: ${virtualDevice.getKey()} - label: ${virtualDevice.getDeviceInfo().getLabel()}")
				if(!found && doResize == false) {
					log.debug("diskKey: ${virtualDevice.getKey()} - disk capacity in KB: ${virtualDevice.getCapacityInKB()} - new disk size (1024*diskSize): ${1024 * diskSize}")
					if((diskKeyMatch == null && (diskIndex == currentDisk)) || diskKeyMatch == virtualDevice.getKey()) {
						def currentSize = virtualDevice.getCapacityInKB()
						def newSize = 1024l * diskSize
						log.info("found disk: ${opts.diskKey} - checking resize: ${currentSize} to ${newSize}")
						if(newSize > currentSize) {
							doResize = true
							diskSpec = new VirtualDeviceConfigSpec()
							diskSpec.setOperation(VirtualDeviceConfigSpecOperation.edit)
							virtualDevice.setCapacityInKB(newSize)
							diskSpec.setDevice(virtualDevice)
						}
						found = true
					}

				}
				currentDisk++
			}
			if(doResize == true && diskSpec) {
				def deviceChange = [diskSpec] as VirtualDeviceConfigSpec[]
				vmConfigSpec.setDeviceChange(deviceChange)
				def vmTask = vm.reconfigVM_Task(vmConfigSpec)
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
					vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
				} else {
					log.error("Error Resizing Disk for VM")
					TaskInfo info = vmTask.getTaskInfo()
					rtn.msg = "Error Resizing Disk for VM: ${info.getError().getLocalizedMessage()}"
				}
			}
			rtn.volumes = getVmVolumes(vm.getConfig().getHardware().getDevice())
			rtn.controllers = getVmControllers(vm.getConfig().getHardware().getDevice())
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error resizing vm disk', e)
		} catch(e) {
			log.error("resizeVmDisk error: ${e} - ${e}", e)
			rtn.msg = 'error resizing vm disk'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static deleteVmDisk(apiUrl, username, password, opts = [:]) {
		log.debug "deleteVmDisk: ${apiUrl}, ${username}, ${password}, ${opts}"
		def rtn = [success: true]
		ServiceInstance serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			VirtualMachine vmManager = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def virtualDevices = vmManager.getConfig().getHardware().getDevice()
			def diskKeyMatch = opts.diskKey ? opts.diskKey.toInteger() : null
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualDisk) {
					VirtualDisk vDisk = virtualDevice
					log.debug("diskKey: ${vDisk.getKey()} - disk capacity in KB: ${vDisk.getCapacityInKB()}")
					if(diskKeyMatch == vDisk.getKey()) {
						log.info("found disk: ${opts.diskKey}")
						// First.. remove the virtualdisk from the VM
						VirtualDeviceConfigSpec diskSpec = new VirtualDeviceConfigSpec()
						diskSpec.setOperation(VirtualDeviceConfigSpecOperation.remove)
						diskSpec.setDevice(virtualDevice)
						def vmConfigSpec = new VirtualMachineConfigSpec()
						def deviceChange = [diskSpec] as VirtualDeviceConfigSpec[]
						vmConfigSpec.setDeviceChange(deviceChange)
						def vmTask = vmManager.reconfigVM_Task(vmConfigSpec)
						def result = vmTask.waitForTask()
						if(result == Task.SUCCESS) {
							rtn.success = true
							// Second.. delete the underlying virtualdisk
							def diskPath = vDisk.backing.getFileName()
							def rootFolder = serviceInstance.getRootFolder()
							def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
							VirtualDiskManager vdManager = serviceInstance.virtualDiskManager
							vdManager.deleteVirtualDisk_Task(diskPath, datacenter)
						} else {
							rtn.success = false
							log.error "error in reconfiguring VM to remove disk: ${result}"
						}
					}
				}
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error removing vm disk', e)
		} catch(e) {
			rtn.success = false
			rtn.msg = 'error removing vm disk'
			log.error("deleteVmDisk error: ${e} - ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static deleteVmController(apiUrl, username, password, opts = [:]) {
		log.debug "deleteVmController: ${apiUrl}, ${username}, ${password}, ${opts}"
		def rtn = [success:true]
		ServiceInstance serviceInstance
		try {
			if(ignoreSsl) {
				SSLUtility.trustAllHostnames()
				SSLUtility.trustAllHttpsCertificates()
			}
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			VirtualMachine vmManager = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def virtualDevices = vmManager.getConfig().getHardware().getDevice()
			def controllerKeyMatch = opts.controllerKey ? opts.controllerKey.toInteger() : null
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualController) {
					log.debug("delete controllerKey: ${virtualDevice.getKey()}")
					if(controllerKeyMatch == virtualDevice.getKey()) {
						log.info("found delete controller: ${opts.controllerKey}")
						// First.. remove the virtualdisk from the VM
						VirtualDeviceConfigSpec controllerSpec = new VirtualDeviceConfigSpec()
						controllerSpec.setOperation(VirtualDeviceConfigSpecOperation.remove)
						controllerSpec.setDevice(virtualDevice)
						def vmConfigSpec = new VirtualMachineConfigSpec()
						def deviceChange = [controllerSpec] as VirtualDeviceConfigSpec[]
						vmConfigSpec.setDeviceChange(deviceChange)
						def vmTask = vmManager.reconfigVM_Task(vmConfigSpec)
						def result = vmTask.waitForTask()
						if(result == Task.SUCCESS) {
							rtn.success = true
						} else {
							rtn.success = false
							log.error "error in reconfiguring VM to remove controller: ${result}"
						}
					}
				}
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error removing vm controller', e)
		} catch(e) {
			rtn.success = false
			rtn.msg = 'error removing vm controller'
			log.error("deleteVmController error: ${e} - ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static deleteVmNetwork(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false]
		ServiceInstance serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			VirtualMachine vmManager = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def virtualDevices = vmManager.getConfig().getHardware().getDevice()
			def removeDevice
			virtualDevices?.each { virtualDevice ->
				if(removeDevice == null && virtualDevice instanceof VirtualEthernetCard) {
					if(virtualDevice.getKey() == opts.networkKey || virtualDevice.getUnitNumber() == opts.unitNumber) {
						removeDevice = virtualDevice
					}
				}
			}
			if(removeDevice == null && opts.index) {
				def counter = 0
				virtualDevices?.each { virtualDevice ->
					if(removeDevice == null && virtualDevice instanceof VirtualEthernetCard) {
						if(counter == opts.index)
							removeDevice = virtualDevice
						counter ++
					}
				}
			}
			if(removeDevice) {
				VirtualDeviceConfigSpec vmNicSpec = new VirtualDeviceConfigSpec()
				vmNicSpec.setOperation(VirtualDeviceConfigSpecOperation.remove)
				vmNicSpec.setDevice(removeDevice)
				def vmConfigSpec = new VirtualMachineConfigSpec()
				def deviceChange = [vmNicSpec] as VirtualDeviceConfigSpec[]
				vmConfigSpec.setDeviceChange(deviceChange)
				def vmTask = vmManager.reconfigVM_Task(vmConfigSpec)
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
				} else {
					rtn.success = false
					log.error "error in reconfiguring VM to remove nic: ${result}"
				}
			} else {
				//doesn't exists - allow it to go
				rtn.success = true
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error removing network adapter', e)
		} catch(e) {
			rtn.success = false
			rtn.msg = 'error removing network adapter'
			log.error("deleteVmNetwork error: ${e} - ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static getDiskfileBacking(deviceList, thin = true, Boolean eager=false) {
		log.debug("Detecting thin mode: ${thin}")
		def rtn
		deviceList?.each { device ->
			if(rtn == null && (device instanceof VirtualDisk && !(device instanceof VirtualCdrom))) {
				def backing = device.getBacking()
				if(backing instanceof VirtualDiskFlatVer1BackingInfo) {
					rtn = new VirtualDiskFlatVer1BackingInfo()
				} else if(backing instanceof VirtualDiskFlatVer2BackingInfo) {
					rtn = new VirtualDiskFlatVer2BackingInfo()
					rtn.setThinProvisioned(thin)
					if(eager && !thin) {
						rtn.setEagerlyScrub(true)
					}
				} else if(backing instanceof VirtualDiskRawDiskMappingVer1BackingInfo) {
					rtn = new VirtualDiskRawDiskMappingVer1BackingInfo()
				} else if(backing instanceof VirtualDiskSparseVer1BackingInfo) {
					rtn = new VirtualDiskSparseVer1BackingInfo()
				} else if(backing instanceof VirtualDiskSparseVer2BackingInfo) {
					rtn = new VirtualDiskSparseVer2BackingInfo()
				}
			}
		}
		if(rtn == null) {
			rtn = new VirtualDiskFlatVer2BackingInfo()
			rtn.setThinProvisioned(thin)
			if(eager && !thin) {
				rtn.setEagerlyScrub(true)
			}
		}
		return rtn
	}

	static getServerDetail(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, found:true]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			if(vm) {
				def server = [:]
				log.debug("net: ${vm.getGuest()?.getIpAddress()}")
				def serverIps = getServerIps(vm.getGuest()?.getNet())
				if(!serverIps.ipAddress) {
					def ip = vm.getGuest()?.getIpAddress()
					if(ip) {
						serverIps.ipAddress = ip
						serverIps.ipList = []
					}
				}
				server.uuid = vm.config?.uuid
				server.instanceUuid = vm.config?.instanceUuid
				server.mor = vm.MOR.val
				server.ipAddress = serverIps.ipAddress
				server.ipv6Ip = serverIps.ipv6Ip
				server.ipList = serverIps.ipList
				server.hostname = vm.getGuest().getHostName()
				server.poweredOn = vm.getRuntime().getPowerState() == VirtualMachinePowerState.poweredOn
				server.host = vm.getRuntime().host.getVal()
				rtn.results = [server:server, vm:vm]
				rtn.success = true
			} else {
				log.info("checkServerDetail: server not found - ${opts.externalId}")
			}
		} catch(ManagedObjectNotFound e) {
			rtn.found = false
			rtn.msg = 'vm not found'
			log.error("getServerDetail error: ${e}")
		} catch(RuntimeException e) {
			if(e.cause instanceof ManagedObjectNotFound) {
				rtn.found = false
				rtn.msg = 'vm not found'
			}
			log.error("getServerDetail error: ${e}")
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			log.error("getServerDetail error: ${e}")
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}


	static getServerWmksTicket(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, found:true]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			VirtualMachine vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId) as VirtualMachine
			if(vm) {

				VirtualMachineTicket ticket = vm.acquireTicket("webmks")
				rtn.url = "wss://${ticket.host}:${ticket.port}/ticket/${ticket.ticket}"
				def vcenterHost = new URL(apiUrl)
				def cfgFile = java.net.URLEncoder.encode(ticket.cfgFile, "UTF-8")
				def vmName = java.net.URLEncoder.encode(vm.name, "UTF-8")
				//rtn.headers = [Cookie: 'token']
				//rtn.url = "wss://${vcenterHost.host}/vsphere-client/webconsole/authd?vmid=${opts.externalId}&vmName=${vmName}&host=${ticket.host}&port=${ticket.port}&cfgFile=${cfgFile}&thumbprint=${ticket.sslThumbprint}&ticket=${ticket.ticket}&encoding=UTF-8"
				log.debug("Config File: ${ticket.cfgFile}")
				log.debug("Returning URL: ${rtn.url}")
				rtn.success = true
			} else {
				log.info("checkServerDetail: server not found - ${opts.externalId}")
			}
		} catch(ManagedObjectNotFound e) {
			rtn.found = false
			rtn.msg = 'vm not found'
			log.error("getServerDetail error: ${e}")
		} catch(RuntimeException e) {
			if(e.cause instanceof ManagedObjectNotFound) {
				rtn.found = false
				rtn.msg = 'vm not found'
			}
			log.error("getServerDetail error: ${e}")
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			log.error("getServerDetail error: ${e}")
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static createCloneDiskSpec(deviceList, long diskSize) {
		def diskSpec
		deviceList.each { device ->
			if(device instanceof VirtualDisk) {
				diskSpec = new VirtualDeviceConfigSpec()
				diskSpec.setOperation(VirtualDeviceConfigSpecOperation.edit)
				diskSpec.setDevice(device)
				device.setCapacityInKB(diskSize)
			}
		}
		return diskSpec
	}

	static createScsiSpec(int cKey) {
		def scsiSpec = new VirtualDeviceConfigSpec()
		scsiSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
		def scsiCtrl = new VirtualLsiLogicController()
		scsiCtrl.setKey(cKey)
		scsiCtrl.setBusNumber(0)
		scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing)
		scsiSpec.setDevice(scsiCtrl)
		return scsiSpec
	}

	static createDiskSpec(String datasourceName, int cKey, long diskSize, String diskMode) {
		def diskSpec = new VirtualDeviceConfigSpec()
		diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
		diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create)
		def virtualDisk = new VirtualDisk()
		virtualDisk.setCapacityInKB(diskSize)
		diskSpec.setDevice(virtualDisk)
		virtualDisk.setKey(-1)
		virtualDisk.setUnitNumber(0)
		virtualDisk.setControllerKey(cKey)
		def diskfileBacking = new VirtualDiskFlatVer2BackingInfo()
		def fileName = "[" + datasourceName + "]"
		diskfileBacking.setFileName(fileName)
		diskfileBacking.setDiskMode(diskMode)
		diskfileBacking.setThinProvisioned(true)
		virtualDisk.setBacking(diskfileBacking)
		return diskSpec
	}




	static createNicSpec(String networkName, String nicName, String type = 'vmxNet3') {
		def nicSpec = new VirtualDeviceConfigSpec()
		nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
		def nic
		if(type == 'vmxNet2')
			nic = new VirtualVmxnet2()
		else if(type == 'vmxNet3')
			nic = new VirtualVmxnet3()
		else
			nic = new VirtualE1000()
		//nic = new VirtualPCNet32()

		def nicBacking = new VirtualEthernetCardNetworkBackingInfo()
		nicBacking.setDeviceName(networkName)
		def info = new Description()
		info.setLabel(nicName)
		info.setSummary(networkName)
		nic.setDeviceInfo(info)
		// type: "generated", "manual", "assigned" by VC
		nic.setAddressType('generated')
		nic.setBacking(nicBacking)
		nic.setKey(-1)
		nicSpec.setDevice(nic)
		return nicSpec
	}

	static createSetNetworkOnNicSpec(deviceList, network, serviceInstance, networkInterface, index = 0, forceAdd = false, opts = [:]) {
		def nicSpec
		def counter = 0
		def nicDevice
		def nicBacking
		def nicSpecs = []
		if(forceAdd != true) {
			deviceList?.each { device ->
				if(device instanceof VirtualEthernetCard) {
					if(index == counter) {
						if(networkInterface?.type?.code == 'vmxNet3' && !(device instanceof VirtualVmxnet3)) {
							VirtualDeviceConfigSpec vmNicSpec = new VirtualDeviceConfigSpec()
							vmNicSpec.setOperation(VirtualDeviceConfigSpecOperation.remove)
							vmNicSpec.setDevice(device)
							nicSpecs << vmNicSpec
						} else if(networkInterface?.type?.code == 'vmxNet2' && !(device instanceof VirtualVmxnet2)) {
							VirtualDeviceConfigSpec vmNicSpec = new VirtualDeviceConfigSpec()
							vmNicSpec.setOperation(VirtualDeviceConfigSpecOperation.remove)
							vmNicSpec.setDevice(device)
							nicSpecs << vmNicSpec
						} else if(networkInterface?.type?.code == 'e1000' && !(device instanceof VirtualE1000)) {
							VirtualDeviceConfigSpec vmNicSpec = new VirtualDeviceConfigSpec()
							vmNicSpec.setOperation(VirtualDeviceConfigSpecOperation.remove)
							vmNicSpec.setDevice(device)
							nicSpecs << vmNicSpec
						} else {
							nicSpec = new VirtualDeviceConfigSpec()
							nicSpec.setOperation(VirtualDeviceConfigSpecOperation.edit)
							nicSpec.setDevice(device)
							nicDevice = device
							nicBacking = device.getBacking()
						}

					}
					counter++
				}
			}
		}
		if(!nicSpec) {
			//new device
			nicSpec = new VirtualDeviceConfigSpec()
			nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
			//make a device
			log.debug("creating nic: ${networkInterface?.type?.code}")
			if(networkInterface?.type?.code == 'vmxNet2')
				nicDevice = new VirtualVmxnet2()
			else if(networkInterface?.type?.code == 'e1000')
				nicDevice = new VirtualE1000()
			else
				nicDevice = new VirtualVmxnet3()
			nicDevice.setAddressType('generated')
			nicDevice.setKey(-1 - index)
			nicSpec.setDevice(nicDevice)

		}
		def newBacking
		if(network.getMOR().getType() == 'DistributedVirtualPortgroup') {
			def vswitchPortConnection = new DistributedVirtualSwitchPortConnection()
			def switchUuid
			if(opts.switchUuid) {
				switchUuid = opts.switchUuid
			} else {
				def distributedVirtualSwitchRef = network.config.distributedVirtualSwitch
				def distributedVirtualSwitch = getManagedObject(serviceInstance, 'DistributedVirtualSwitch', distributedVirtualSwitchRef)
				switchUuid = distributedVirtualSwitch.uuid
			}
			vswitchPortConnection.setSwitchUuid(switchUuid)
			vswitchPortConnection.setPortgroupKey(network.key)
			newBacking = (nicBacking && nicBacking instanceof VirtualEthernetCardDistributedVirtualPortBackingInfo) ? nicBacking : new VirtualEthernetCardDistributedVirtualPortBackingInfo()
			if(nicBacking == null || newBacking.getPort()?.getPortgroupKey() != network.key || newBacking.getPort()?.getSwitchUuid() != switchUuid) {
				log.info("Changing network adapter to distributed network: ${network.getName()}")
				newBacking.setPort(vswitchPortConnection)
				nicDevice.setBacking(newBacking)
				if(!nicDevice.connectable) {
					def connectable = new VirtualDeviceConnectInfo()
					nicDevice.setConnectable(connectable)
				}
				nicDevice.connectable.setStartConnected(true)
				nicDevice.connectable.setConnected(true)
				nicDevice.connectable.setAllowGuestControl(true)
				nicSpecs << nicSpec
			}
		} else if(network.getMOR().getType() == 'OpaqueNetwork') {
			newBacking = (nicBacking && nicBacking instanceof VirtualEthernetCardOpaqueNetworkBackingInfo) ? nicBacking : new VirtualEthernetCardOpaqueNetworkBackingInfo()
			if(nicBacking == null || newBacking?.getOpaqueNetworkId() != network.getSummary()?.getOpaqueNetworkId()) {
				log.info("Changing network adapter to opaque network: ${network.getName()}")
				newBacking.setOpaqueNetworkId(network.getSummary().getOpaqueNetworkId())
				newBacking.setOpaqueNetworkType(network.getSummary().getOpaqueNetworkType())
				nicDevice.setBacking(newBacking)
				if(!nicDevice.connectable) {
					def connectable = new VirtualDeviceConnectInfo()
					nicDevice.setConnectable(connectable)
				}
				nicDevice.connectable.setStartConnected(true)
				nicDevice.connectable.setConnected(true)
				nicDevice.connectable.setAllowGuestControl(true)
				nicSpecs << nicSpec
			}
		} else {
			newBacking = (nicBacking && nicBacking instanceof VirtualEthernetCardNetworkBackingInfo) ? nicBacking : new VirtualEthernetCardNetworkBackingInfo()
			if(nicBacking == null || newBacking.getDeviceName() != network.getName()) {
				log.info("Changing network adapter from network: ${newBacking.getDeviceName()} to: ${network.getName()}")
				newBacking.setDeviceName(network.getName())
				//newBacking.setNetwork(network.getMOR())	 //not necessary - read only
				nicDevice.setBacking(newBacking)
				if(!nicDevice.connectable) {
					def connectable = new VirtualDeviceConnectInfo()
					nicDevice.setConnectable(connectable)
				}
				nicDevice.connectable.setStartConnected(true)
				nicDevice.connectable.setConnected(true)
				nicDevice.connectable.setAllowGuestControl(true)
				nicSpecs << nicSpec
			}
		}
		return nicSpecs ? nicSpecs : null
	}

	static addCloudInitIso(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		try {
			def cloudConfigUser = opts.cloudConfigUser
			def cloudConfigMeta = opts.cloudConfigMeta
			def cloudConfigNetwork = opts.cloudConfigNetwork
			def cloudIsoOutputStream
			if(opts.isSysprep) {
				// TODO : Call back to Morpheus to build the ISO
//				cloudIsoOutputStream = IsoUtility.buildAutoUnattendIso(cloudConfigUser)
			} else {
				// TODO : Call back to Morpheus to build the ISO
//				cloudIsoOutputStream = IsoUtility.buildCloudIso(opts.platform, cloudConfigMeta, cloudConfigUser, cloudConfigNetwork)
			}

			def isoOutput = cloudIsoOutputStream.toByteArray()
			def cloudConfigInputStream = new ByteArrayInputStream(isoOutput) //cloudConfigData.createInputStream()
			def cloudConfigInputLength = isoOutput.length //cloudConfigData.getLength()
			rtn = addIso(apiUrl,username,password,opts + [inputStream: cloudConfigInputStream, contentLength: cloudConfigInputLength, isoName: 'config.iso', overwrite:true])
		} catch(e) {
			log.error("addCloudInitIso error: ${e}", e)
		}
		return rtn
	}

	static addIso(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def inputStream
		def contentLength
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def isoName = opts.isoName
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def datastore = getFreeDatastore(vm, 10000, opts.datastoreId)
			def dcPath = getDatacenterPath(serviceInstance as ServiceInstance, datacenter as Datacenter)
			if(opts.inputStream && opts.contentLength) {
				inputStream = opts.inputStream
				contentLength = opts.contentLength
			} else if(opts.cloudFiles) {
				CloudFile isoCloudFile = opts.cloudFiles?.find { CloudFile cloudFile -> cloudFile.name.toLowerCase().endsWith(".iso") }
				if(isoCloudFile) {
					contentLength = isoCloudFile.getContentLength()
					inputStream = isoCloudFile.getInputStream()
					isoName = isoCloudFile?.name?.substring(isoCloudFile.name.lastIndexOf('/') + 1)
				}
			}

			def uploadUrl = apiUrl.replaceAll('sdk', 'folder') + '/' + encodeUrl(opts.path ?: vm.getName()) + "/${isoName ?: 'config.iso'}?dcPath=" + encodeUrl(dcPath) + '&dsName=' + encodeUrl(datastore.getName())
			log.info("url: ${uploadUrl}")
			def sessionManager = serviceInstance.getSessionManager()
			def sessionId = sessionManager.getServerConnection().getVimService().getWsc().getCookie()
			if(!fileExists(username,password,sessionId,uploadUrl, opts.proxySettings) || opts.overwrite) {
				def uploadResults = uploadFile(username, password, sessionId, inputStream, contentLength, uploadUrl, opts.proxySettings)
				rtn.success = uploadResults.success
			}

			rtn.isoName = isoName ?: 'config.iso'
		} catch(e) {
			log.error("addCloudInitIso error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static addVmDisk(apiUrl, username, password, opts = [:]) {
		log.info("addVmDisk: ${apiUrl}, ${username}, ${opts}")
		def rtn = [success: false]
		def serviceInstance
		try {
			def thin = (opts.type != 'thickEager' && opts.type != 'thick')
			Boolean eager = (opts.type == 'thickEager')
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			int diskSize = opts.diskSize?.toInteger()
			def diskSpec = new VirtualDeviceConfigSpec()
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			def virtualDisk = new VirtualDisk()
			def diskfileBacking = getDiskfileBacking(virtualDevices, thin,eager)
			def key
			def busNumber = opts.busNumber
			def unitNumber = opts.unitNumber
			def controllerType = opts.controllerType
			def diskIndex = opts.diskIndex ?: 2
			def diskController = findController(virtualDevices, busNumber, controllerType)
			if(diskController) {
				key = diskController.getKey()
			} else if(controllerType) {
				//make one
				def newVmControllerSpec = new VirtualMachineConfigSpec()
				def newControllerSpec = new VirtualDeviceConfigSpec()
				def newController = createController(controllerType)
				busNumber = findFreeBusNumber(virtualDevices, busNumber.toInteger())
				newController.setBusNumber(busNumber)
				newController.setSharedBus(VirtualSCSISharing.noSharing)
				newControllerSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
				newControllerSpec.setDevice(newController)
				def controllerChange = [newControllerSpec] as VirtualDeviceConfigSpec[]
				newVmControllerSpec.setDeviceChange(controllerChange)
				def vmControllerTask = vm.reconfigVM_Task(newVmControllerSpec)
				def result = vmControllerTask.waitForTask()
				if(result == Task.SUCCESS) {
					log.info("created controller: ${busNumber} type: ${controllerType}")
					vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
					vmConfig = vm.getConfig()
					virtualDevices = vmConfig.getHardware().getDevice()
					diskController = findController(virtualDevices, busNumber, controllerType)
					if(diskController) {
						key = diskController.getKey()
						log.debug("found created controller: ${key}")
						rtn.controller = [busNumber:busNumber, unitNumber:unitNumber, key:key, controllerKey:key, type:controllerType]
						rtn.entity = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
						rtn.controllers = getVmControllers(rtn.entity.getConfig().getHardware().getDevice())
					}
				}
			}
			if(key != null) {
				unitNumber = findFreeUnitNumber(virtualDevices, key, unitNumber)
				log.debug("datastoreId: ${opts.datastoreId}")
				def datastore = getAssigedOrFreeDatastore(serviceInstance, opts.datastoreId, vm, diskSize)
				def dsName = datastore ? datastore.getName() : getFreeDatastoreName(vm, diskSize, opts.datastoreId)
				log.debug("backing: ${diskfileBacking.getClass().getName()} dnName: ${dsName} key: ${key} unit: ${unitNumber} diskSize: ${diskSize}")
				def fileName = "[" + dsName + "] " + vm.getName() + "/" + (opts.diskName ?: 'morpheus_data') + ".vmdk"
				diskfileBacking.setFileName(fileName)
				diskfileBacking.setDiskMode(opts.diskMode ?: 'persistent')
				virtualDisk.setControllerKey(key)
				virtualDisk.setUnitNumber(unitNumber)
				virtualDisk.setBacking(diskfileBacking)
				Long desiredDiskSizeKb = 1024L * diskSize?.toLong()
				virtualDisk.setCapacityInKB(desiredDiskSizeKb)
				virtualDisk.setKey(-1 - diskIndex)
				diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
				diskSpec.setFileOperation(VirtualDeviceConfigSpecFileOperation.create)
				diskSpec.setDevice(virtualDisk)
				def deviceChange = [diskSpec] as VirtualDeviceConfigSpec[]
				vmConfigSpec.setDeviceChange(deviceChange)
				def vmTask = vm.reconfigVM_Task(vmConfigSpec)
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
					rtn.dsName = dsName
					rtn.datastore = datastore
					rtn.entity = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
					rtn.controllerKey = key
					rtn.volumes = getVmVolumes(rtn.entity.getConfig().getHardware().getDevice())
				} else {
					log.error("Error Adding Disk to vm")
					TaskInfo info = vmTask.getTaskInfo()
					rtn.msg = "Error Adding Disk to VM: ${info.getError().getLocalizedMessage()}"
				}
			} else {
				rtn.msg = 'No controller available for requested types'
				log.error(rtn.msg)
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating disk', e)
			log.error("addVmDisk method fault error: ${e}", e)

		} catch(e) {
			rtn.success = false
			log.error("addVmDisk error: ${e}", e)
			rtn.msg = 'error creating disk'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static adjustVmConfig(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			def vmConfig = vm.getConfig()
			if(opts.containsKey('extraConfig')) {
				vmConfigSpec.setExtraConfig(opts.extraConfig as OptionValue[])
			}
			def vmTask = vm.reconfigVM_Task(vmConfigSpec)
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS) {
				rtn.success = true
			}
		} catch(e) {
			log.error("adjustVmConfig error: ${e.message}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static listVmEvents(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def eventManager = serviceInstance.getEventManager()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def eventFilter = new EventFilterSpec()
			def entitySpec = new EventFilterSpecByEntity()
			entitySpec.setEntity(vm.getMOR())
			entitySpec.setRecursion(EventFilterSpecRecursionOption.all)
			eventFilter.setEntity(entitySpec)
			def timeSpec = new EventFilterSpecByTime()
			def now = new Date()
			def startTime = opts.startTime
			if(startTime == null) {
				startTime = Calendar.getInstance()
				startTime.setTime(new Date(now.time - (1000l * 60l * 120l)))
			}
			timeSpec.setBeginTime(startTime)
			eventFilter.setTime(timeSpec)
			if(opts.eventTypeIds)
				eventFilter.setEventTypeId(opts.eventTypeIds as String[])
			rtn.eventList = eventManager.queryEvents(eventFilter)
			rtn.success = true
		} catch(e) {
			log.error("listVmEvents error: ${e}", e)
			rtn.msg = 'error loading vm events'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static addVmCdRom(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		removeVmCdRom(apiUrl,username,password,opts) //first remove the old cdrom object in case
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def datastore = getFreeDatastore(vm, 10000, opts.datastoreId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			def cdSpec = new VirtualDeviceConfigSpec()
			cdSpec.setOperation(VirtualDeviceConfigSpecOperation.add)
			def cdrom = new VirtualCdrom()
			def cdDeviceBacking = new VirtualCdromIsoBackingInfo()
			cdDeviceBacking.setDatastore(datastore.getMOR())
			cdDeviceBacking.setFileName("[" + datastore.getName() + "] " + (opts.path ?: vm.getName()) + "/" + opts.isoName)
			def vd = getIDEController(vm)
			cdrom.setBacking(cdDeviceBacking)
			cdrom.setControllerKey(vd.getKey())
			cdrom.setUnitNumber(vd.getUnitNumber())
			cdrom.setKey(-1)
			cdSpec.setDevice(cdrom)
			def deviceChange = [cdSpec] as VirtualDeviceConfigSpec[]
			vmConfigSpec.setDeviceChange(deviceChange)
			def vmTask = vm.reconfigVM_Task(vmConfigSpec)
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS) {
				rtn.success = true
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error adding cdrom', e)
		} catch(e) {
			log.error("addVmCdRom error: ${e}", e)
			rtn.msg = 'error adding cdrom'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static removeVmCdRom(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			def cdSpec = new VirtualDeviceConfigSpec()
			cdSpec.setOperation(VirtualDeviceConfigSpecOperation.remove)
			def cdRemove
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualCdrom)
					cdRemove = virtualDevice
			}
			if(cdRemove) {
				cdSpec.setDevice(cdRemove)
				def deviceChange = [cdSpec] as VirtualDeviceConfigSpec[]
				vmConfigSpec.setDeviceChange(deviceChange)
				def vmTask = vm.reconfigVM_Task(vmConfigSpec)
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
				}
			} else {
				rtn.msg = 'no cd rom found'
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error removing cdrom', e)
		} catch(e) {
			log.error("removeVmCdRom error: ${e}", e)
			rtn.msg = 'error removing cdrom'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static ejectVmCdRom(apiUrl, username, password, opts = [:]) {

		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			def cdSpec = new VirtualDeviceConfigSpec()
			cdSpec.setOperation(VirtualDeviceConfigSpecOperation.edit)
			def cdDeviceBacking = new VirtualCdromRemotePassthroughBackingInfo()
			def connectable = new VirtualDeviceConnectInfo()
			connectable.setStartConnected(false)
			connectable.setConnected(false)
			connectable.setAllowGuestControl(true)
			cdDeviceBacking.setDeviceName('')
			def cdRemove
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualCdrom) {
					//log.debug("cd: ${virtualDevice.getBacking()?.getClass()}")
					if(virtualDevice.getBacking() instanceof VirtualCdromIsoBackingInfo && (!opts.fileName || virtualDevice.getBacking().getFileName()?.endsWith(opts.fileName)) ) {
						cdRemove = virtualDevice
					}
				}
			}
			if(cdRemove) {
				cdRemove.setConnectable(connectable)
				cdRemove.setBacking(cdDeviceBacking)
				cdSpec.setDevice(cdRemove)
				def deviceChange = [cdSpec] as VirtualDeviceConfigSpec[]
				vmConfigSpec.setDeviceChange(deviceChange)
				def vmTask = vm.reconfigVM_Task(vmConfigSpec)
				rtn.success = doTaskAnswerQuestions(vm, vmTask)
			} else {
				rtn.msg = 'no cd rom found'
			}
		} catch(e) {
			log.error("ejectVmCdRom error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
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
