package com.morpheusdata.vmware.util

import com.bertramlabs.plugins.karman.CloudFile
import com.morpheusdata.model.ComputeServer
import com.vmware.vim25.*
import com.vmware.vim25.mo.*
import groovy.transform.CompileStatic
import groovy.util.logging.Commons
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpPut
import org.apache.http.client.utils.URIBuilder
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.conn.ssl.SSLContextBuilder
import org.apache.http.conn.ssl.TrustStrategy
import org.apache.http.conn.ssl.X509HostnameVerifier
import org.apache.http.entity.InputStreamEntity
import org.apache.http.impl.client.HttpClients

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocket
import java.security.cert.X509Certificate
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Commons
class VmwareComputeUtility {

	static ignoreSsl = true
	static int CHUCK_LEN = 64 * 1024
	static long SHUTDOWN_TIMEOUT = 5l*60000l
	static VmwareLeaseProgressUpdater leaseUpdater
	static VmwareConnectionPool connectionPool = new VmwareConnectionPool()
	static final vmwareMutex = new Object()
	static morpheusExtensionId = 'com.morpheus.vmware.extension'
	static final ExecutorService monitorServiceExecutor = Executors.newCachedThreadPool()

	static testConnection(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, invalidLogin:false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			rtn.success = true
		} catch(com.vmware.vim25.InvalidLogin e) {
			rtn.invalidLogin = true
		} catch(e) {
			log.error("testConnection to ${apiUrl}: ${e}")
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
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
		return rtn
	}

	static getServiceInstance(apiUrl, username, password) {
		return connectionPool.getConnection(apiUrl, username, password)
	}

	static releaseServiceInstance(serviceInstance, apiUrl, username, password) {
		if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
	}

	static registerExtension(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false, datacenters:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def serviceContent = serviceInstance.retrieveServiceContent()
			def extensionManager = getManagedObject(serviceInstance, 'ExtensionManager', serviceContent.getExtensionManager().getVal())
			if(extensionManager.findExtension(morpheusExtensionId) != null) {
				//extensionManager.unregisterExtension(morpheusExtensionId)
				rtn.msg = 'extension already installed'
			} else {
				def morpheusExtension = new Extension()
				def description = new Description()
				description.setLabel('morpheus')
				description.setSummary('morpheus')
				morpheusExtension.setDescription(description)
				morpheusExtension.setKey(morpheusExtensionId)
				morpheusExtension.setVersion('1.0.0')
				morpheusExtension.setCompany('Morpheus')
				def morpheusServer = new ExtensionServerInfo()
				morpheusServer.setDescription(description)
				morpheusServer.setUrl(opts.extensionUrl)
				morpheusServer.setCompany('Morpheus')
				morpheusServer.setType(morpheusExtensionId)
				morpheusServer.setAdminEmail(['morpheus@gomorpheus.com'] as String[])
				def morpheusClient = new ExtensionClientInfo()
				morpheusClient.setCompany('Morpheus')
				morpheusClient.setUrl(opts.extensionUrl)
				morpheusClient.setType(morpheusExtensionId)
				morpheusClient.setVersion('1.0.0')
				morpheusClient.setDescription(description)
				morpheusExtension.setClient([morpheusClient] as ExtensionClientInfo[])
				morpheusExtension.setServer([morpheusServer] as ExtensionServerInfo[])
				morpheusExtension.setLastHeartbeatTime(Calendar.getInstance())
				extensionManager.registerExtension(morpheusExtension)
				def certPem = '-----BEGIN CERTIFICATE-----\n' + SSLUtility.extensionCert + '-----END CERTIFICATE-----'
				log.debug("pem: ${certPem}")
				extensionManager.setExtensionCertificate(morpheusExtensionId, certPem)
				rtn.success = true
			}
		} catch(MethodFault e) {
			log.error("registerExtension fault: ${e.faultCause} ${e.faultMessage} ${e}", e)
		} catch(e) {
			log.error("registerExtension error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}

	static formatVmwareCert(cert) {
		def rtn = ''
		def counter = 0
		def keepGoing = true
		def spaceIndex = cert.indexOf(' ')
		if(spaceIndex > -1)
			cert = cert.substring(spaceIndex + 1)
		spaceIndex = cert.indexOf(' ')
		if(spaceIndex > -1)
			cert = cert.substring(0, spaceIndex)
		while(keepGoing == true) {
			if(counter + 64 > cert.length()) {
				rtn = rtn + cert.substring(counter, counter + 64) + '\r\n'
			} else {
				rtn = rtn + cert.substring(counter) + '\r\n'
				keepGoing = false
			}
			counter += 64
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

	static listComputeResources(apiUrl, username, password, opts = [:]) {
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

	static listResourcePools(apiUrl, username, password, opts = [:]) {
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
					entityList = new InventoryNavigator(cluster).searchManagedEntities('ResourcePool')
				} else {
					log.debug("no cluster")
					entityList = new InventoryNavigator(datacenter).searchManagedEntities('ResourcePool')
				}
				entityList?.each { resourcePool ->
					def objRoles = resourcePool.getEffectiveRole()
					def readOnly = isEntityReadOnly(objRoles)
					log.debug("account: ${username} pool: ${resourcePool.getName()} role: ${objRoles}")
					rtn.resourcePools << [name:resourcePool.getName(), type:resourcePool.getMOR().getType(), ref:resourcePool.getMOR().getVal(),
										  resourcePool:resourcePool, parentType: resourcePool.parent?.getMOR().getType(), parentId: resourcePool.parent?.getMOR().getVal(), readOnly:readOnly]
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

	static listFolders(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, folders: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				if(datacenter) {
					def entityList = new InventoryNavigator(datacenter).searchManagedEntities('Folder')
					entityList?.each { folder ->
						def objRoles = folder.getEffectiveRole()
						def readOnly = isEntityReadOnly(objRoles)
						log.debug("account: ${username} folder: ${folder.getName()} role: ${objRoles}")
						def addFolder = [name:folder.getName(), childTypes:folder.getChildType(), type:folder.getMOR().getType(), ref:folder.getMOR().getVal(),
										 folder:folder, parentType:folder.getParent()?.getMOR()?.getType(), parentRef:folder.getParent()?.getMOR()?.getVal(),
										 folderRole:objRoles, readOnly:readOnly]
						rtn.folders << addFolder
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

	static listAlarms(Map authConfig, Map opts = [:]) {
		def rtn = [success: false, alarms:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
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
			if(serviceInstance) {connectionPool.releaseConnection(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, serviceInstance)}
		}
		return rtn
	}

	static listEvents(Map authConfig, Map opts = [:]) {
		def rtn = [success:false, events:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
			def eventManager = serviceInstance.getEventManager()
			def categoryList = opts.categoryList ?: (['error', 'warning'] as String[])
			def eventFilterSpec = new EventFilterSpec()
			eventFilterSpec.setCategory(categoryList)
			//eventFilterSpec.setType()
			def dateFilter = new EventFilterSpecByTime()
			def startTime = serviceInstance.currentTime()
			def minuteCount = opts.minuteCount ?: 10
			startTime.roll(Calendar.MINUTE, -(minuteCount))
			dateFilter.setBeginTime(startTime)
			eventFilterSpec.setTime(dateFilter)
			def entityList = eventManager.queryEvents(eventFilterSpec)
			entityList?.each { event ->
				def addEvent = [time:event.getCreatedTime()?.getTime(), key:event.getKey(), host:event.getHost()?.getName(),
								datacenter:event.getDatacenter()?.getName(), datastore:event.getDs()?.getName(), dvs:event.getDvs()?.getName(),
								message:event.getFullFormattedMessage(), network:event.getNet()?.getName(), username:event.getUserName(),
								vm:event.getVm()?.getName(), chainId:event.getChainId(), changeTag:event.getChangeTag(),
								resource:event.getComputeResource()?.getName(), resourceType:event.getComputeResource()?.getComputeResource()?.getType(),
								resourceId:event.getComputeResource()?.getComputeResource()?.getVal(),
								externalId:String.valueOf(event.getKey() ?: '')
				]
				rtn.events << addEvent
			}
			rtn.success = true
		} catch(e) {
			log.error("listEvents: ${opts.datacenter} ${opts.cluster} ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, serviceInstance)}
		}
		return rtn
	}

	static listOldDatastores(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, datastores: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def entityList
				if(opts.cluster) {
					def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
					entityList = cluster.getDatastores()
				} else {
					entityList = new InventoryNavigator(datacenter).searchManagedEntities('Datastore')
				}
				entityList?.each { datastore ->
					rtn.datastores << [name:datastore.getName(), type:datastore.getMOR().getType(), ref:datastore.getMOR().getVal(),
									   freeSpace:datastore.getInfo().getFreeSpace(), totalSpace:datastore.getInfo().getMaxFileSize(),
									   accessible:datastore.getSummary().isAccessible()]
				}
				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listDatastores: ${e}", e)
		}
		return rtn
	}

	//this doesnt work cuz the search by cluster fails due to it not being a parent child relationship
	static listDatastores(apiUrl, username, password, opts = [:]) {
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

	static listOldHosts(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, hosts: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def entityList
				if(opts.cluster) {
					def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
					entityList = new InventoryNavigator(cluster).searchManagedEntities('HostSystem')
				} else {
					entityList = new InventoryNavigator(datacenter).searchManagedEntities('HostSystem')
				}
				entityList?.each { host ->
					rtn.hosts << [name:host.getName(), type:host.getMOR().getType(), ref:host.getMOR().getVal(), memorySize:host.getHardware()?.getMemorySize(),
								  cpuCoreCount:host.getHardware()?.getCpuInfo()?.getNumCpuCores(), entity:host,
								  hostname:host.getConfig()?.getNetwork()?.getDnsConfig()?.getHostName() ?: host.getName()]
				}
				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listDatastores: ${e}", e)
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
								def addHost = [name:obj.name, type:objRef.type, ref:objRef.val, parent:obj['parent'], memorySize:obj['hardware.memorySize'],
											   cpuCoreCount:obj['hardware.cpuInfo.numCpuCores'], hostname:obj['config.network.dnsConfig.hostName'] ?: obj.name,
											   datastoreList:datastoreList,
											   datastores:[],
											   summary:[quickStats:obj['summary.quickStats'],
														hardware:[cpuMhz:obj['summary.hardware.cpuMhz'], cpuModel:obj['summary.hardware.cpuModel'],
																  cpuCount:obj['summary.hardware.numCpuPkgs'], threadCount:obj['summary.hardware.numCpuThreads'],
																  nicCount:obj['summary.hardware.numNics'], vendor:obj['summary.hardware.vendor']]
											   ],
											   hardware:[memorySize:obj['hardware.memorySize'], cpuInfo:[numCpuCores:obj['hardware.cpuInfo.numCpuCores']]],
											   runtime:[powerState:obj['runtime.powerState']]
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

	static loadHost(apiUrl, username, password, hostId) {
		def rtn = [success:false, host:null]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			rtn.host = getManagedObject(serviceInstance, 'HostSystem', hostId)
			rtn.success = rtn.host != null
		} catch(e) {
			log.error("loadHost: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static listOldTemplates(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, templates: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def entityList
				//if(opts.cluster) {
				//def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
				//if(opts.resourcePool) {
				//	def resourcePool = new InventoryNavigator(cluster).searchManagedEntity('ResourcePool', opts.resourcePool)
				//	entityList = new InventoryNavigator(resourcePool).searchManagedEntities('VirtualMachine')
				//} else {
				//entityList = new InventoryNavigator(cluster).searchManagedEntities('VirtualMachine')
				//}
				//} else if(opts.resourcePool) {
				//	def resourcePool = new InventoryNavigator(datacenter).searchManagedEntity('ResourcePool', opts.resourcePool)
				//	entityList = new InventoryNavigator(resourcePool).searchManagedEntities('VirtualMachine')
				//} else {
				entityList = new InventoryNavigator(datacenter).searchManagedEntities('VirtualMachine')
				//}
				entityList?.each { vm ->
					try {
						if(opts.noTemplate == true || vm.getConfig()?.template == true) {
							def vmResPool = vm.getResourcePool()
							def vmResPoolId = vmResPool == null ? null : (vmResPool instanceof ResourcePool ? vmResPool.getMOR().getVal() : vmResPool.getVal())
							rtn.templates << [name:vm.getName(), os:vm.getConfig().getGuestFullName(), type:vm.getMOR().getType(), ref:vm.getMOR().getVal(), vm:vm,
											  resourcePool:vmResPoolId, volumes:getVmVolumes(vm.getConfig().getHardware().getDevice()),
											  controllers:getVmControllers(vm.getConfig().getHardware().getDevice()), networks:getVmNetworks(vm.getConfig().getHardware().getDevice())]
						}
					} catch(e2) {
						//couldn't find template error
						log.debug("inventory list returned an invalid vm: ${e2}")
					}
				}
				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listTemplates - url: ${apiUrl} error:${e}", e)
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

	static listOldVirtualMachines(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, virtualMachines: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def entityList
				if(opts.cluster) {
					def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
					if(opts.resourcePool) {
						def resourcePool = VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
						entityList = new InventoryNavigator(resourcePool).searchManagedEntities('VirtualMachine')
					} else {
						entityList = new InventoryNavigator(cluster).searchManagedEntities('VirtualMachine')
					}
				} else if(opts.resourcePool) {
					def resourcePool = VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
					entityList = new InventoryNavigator(resourcePool).searchManagedEntities('VirtualMachine')
				} else {
					entityList = new InventoryNavigator(datacenter).searchManagedEntities('VirtualMachine')
				}
				entityList?.each { vm ->
					if(vm.getConfig()?.template == false) {
						rtn.virtualMachines << [name:vm.getName(), os:vm.getConfig().getGuestFullName(), type:vm.getMOR().getType(), ref:vm.getMOR().getVal(),
												vm:vm, volumes:getVmVolumes(vm.getConfig().getHardware().getDevice()), controllers:getVmControllers(vm.getConfig().getHardware().getDevice()),
												networks:getVmNetworks(vm.getConfig().getHardware().getDevice())]
					}
				}
				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listOldVirtualMachines: ${e}", e)
		} finally {
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
								def resourcePool = VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
								parentEntity = resourcePool
							} else {
								parentEntity = cluster
							}
						}
					} else {
						rtn.msg = "no datacenter found"
					}
				} else if(opts.resourcePool) {
					def resourcePool = VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
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

	static brianTest(apiUrl, username, password, opts) {
		def rtn = [success:false, fields:[]]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def serviceContent = serviceInstance.retrieveServiceContent()
			def fieldsManagerRef = serviceContent.getCustomFieldsManager()
			println("fieldsManagerRef: ${fieldsManagerRef}")
			def fieldsManager = getManagedObject(serviceInstance, 'CustomFieldsManager', fieldsManagerRef.getVal())
			println("fieldsManager: ${fieldsManager}")
			rtn.fields = fieldsManager.getField()
		} catch(e) {
			log.error("brianTest: ${e}", e)
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

	static listIpPoolAllocations(apiUrl, username, password, poolId, opts = [:]) {
		def rtn = [success: false, ipAllocations: []]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			if(opts.datacenter) {
				def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
				def serviceContent = serviceInstance.retrieveServiceContent()
				def ipPoolManagerRef = serviceContent.getIpPoolManager()
				def ipPoolManager = ipPoolManagerRef ? getManagedObject(serviceInstance, 'IpPoolManager', ipPoolManagerRef.getVal()) : null
				def entityList = ipPoolManager ? ipPoolManager.queryIPAllocations(datacenter, poolId, morpheusExtensionId) : []
				log.debug("ip pools allocations: ${entityList}")
				entityList?.each { ip ->
					rtn.ipAllocations << [allocationId: ip.getAllocationId(), ipAddress: ip.getIpAddress()]
				}
				log.info("ipAllocations: ${rtn.ipAllocations}")
				rtn.success = true
			} else {
				rtn.msg = 'no datacenter found'
			}
		} catch(e) {
			log.error("listIpPoolAllocations: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
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

	static listCategories(apiUrl,username,password,opts=[:]) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		try {
			def sessionResults = getRestSessionId(apiUrl,username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			URIBuilder uriBuilder = new URIBuilder(apiUrl)
			uriBuilder.path = "/rest/com/vmware/cis/tagging/category"
			def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/category",null,null,[headers: ["vmware-api-session-id": sessionId]],'GET')
			rtn.categoryIds = apiResults.data?.value
			rtn.categories = []
			rtn.categoryIds?.each { categoryId ->
				apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/category/id:${categoryId}",null,null,[headers: ["vmware-api-session-id": sessionId]],'GET')
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
				logoutRestSessionId(apiUrl,sessionId)
			}
		}

		return rtn
	}

	static listTags(apiUrl,username,password,opts=[:]) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		try {
			def sessionResults = getRestSessionId(apiUrl,username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			URIBuilder uriBuilder = new URIBuilder(apiUrl)
			uriBuilder.path = "/rest/com/vmware/cis/tagging/tag"
			def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/tag",null,null,[headers: ["vmware-api-session-id": sessionId]],'GET')
			rtn.tagIds = apiResults.data?.value
			rtn.tags = []
			rtn.tagIds?.each { tagId ->
				apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/tag/id:${tagId}",null,null,[headers: ["vmware-api-session-id": sessionId]],'GET')
				if(apiResults.success) {
					def tagObj = [externalId: tagId, name: apiResults.data.value.name, categoryId: apiResults.data.value.category_id, description: apiResults.data.value.description]
					rtn.tags << tagObj
				}
			}
			rtn.success = apiResults.success
		} catch(e) {
			log.error("listCustomizationSpecs: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(apiUrl,sessionId)
			}
		}

		return rtn
	}

	static attachTagToVirtualMachine(apiUrl,username,password,tagId,vmId) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		try {
			rtn.associations = [:]
			def sessionResults = getRestSessionId(apiUrl,username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			URIBuilder uriBuilder = new URIBuilder(apiUrl)
			def query = ['~action':'attach']
			uriBuilder.path = "/rest/com/vmware/cis/tagging/tag-association/id:${tagId}"
			def body = [object_id: [type: 'VirtualMachine', id: vmId]]
			def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/tag-association/id:${tagId}",null,null,[headers: ["vmware-api-session-id": sessionId], body: body, query:query],'POST')
			rtn.success = apiResults.success
		} catch(e) {
			log.error("listTagAssociations: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(apiUrl,sessionId)
			}
		}

		return rtn
	}


	static detachTagToVirtualMachine(apiUrl,username,password,tagId,vmId) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		try {
			rtn.associations = [:]
			def sessionResults = getRestSessionId(apiUrl,username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			URIBuilder uriBuilder = new URIBuilder(apiUrl)
			def query = ['~action':'detach']
			uriBuilder.path = "/rest/com/vmware/cis/tagging/tag-association/id:${tagId}"
			def body = [object_id: [type: 'VirtualMachine', id: vmId]]
			def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/tag-association/id:${tagId}",null,null,[headers: ["vmware-api-session-id": sessionId], body: body, query:query],'POST')
			rtn.success = apiResults.success
		} catch(e) {
			log.error("detachTag: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(apiUrl,sessionId)
			}
		}

		return rtn
	}

	static createTagCategory(apiUrl,username,password,opts) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		try {
			rtn.associations = [:]
			def sessionResults = getRestSessionId(apiUrl,username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			URIBuilder uriBuilder = new URIBuilder(apiUrl)
			uriBuilder.path = "/rest/com/vmware/cis/tagging/category"
			def body = [create_spec: [
					associable_types: ['VirtualMachine'],
					cardinality: 'SINGLE',
					name:opts.name,
					description: opts.name
			]
			]
			def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/category",null,null,[headers: ["vmware-api-session-id": sessionId], body: body],'POST')
			rtn.success = apiResults.success
			rtn.categoryId = apiResults.data.value
		} catch(e) {
			log.error("createTagCategory: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(apiUrl,sessionId)
			}
		}

		return rtn
	}

	static createTag(apiUrl,username,password,opts) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		try {
			rtn.associations = [:]
			def sessionResults = getRestSessionId(apiUrl,username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			URIBuilder uriBuilder = new URIBuilder(apiUrl)
			uriBuilder.path = "/rest/com/vmware/cis/tagging/tag"
			def body = [create_spec: [
					category_id: opts.categoryId,
					name:opts.name,
					description: opts.name
			]
			]
			def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/tag",null,null,[headers: ["vmware-api-session-id": sessionId], body: body],'POST')
			rtn.success = apiResults.success
			rtn.tagId = apiResults.data.value
		} catch(e) {
			log.error("createTagCategory: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(apiUrl,sessionId)
			}
		}

		return rtn
	}

	static listTagAssociationsForVirtualMachines(apiUrl,username,password,opts=[:]) {
		def rtn = [success: false, customSpecs: []]
		def sessionId
		try {
			rtn.associations = [:]
			def sessionResults = getRestSessionId(apiUrl,username,password)
			sessionId = sessionResults.sessionId//serviceInstance.sessionManager.currentSession.key
			log.debug("sessionId: ${sessionId}")
			URIBuilder uriBuilder = new URIBuilder(apiUrl)
			uriBuilder.path = "/rest/com/vmware/cis/tagging/tag-association"
			def body = [object_ids: []]
			def params = ['~action':'list-attached-tags-on-objects']
			body.object_ids = opts.vmIds.collect{[type: 'VirtualMachine', id: it]}
			def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/tagging/tag-association",null,null,[headers: ["vmware-api-session-id": sessionId], body: body, query: params],'POST')
			apiResults.data?.value.collect { association ->
				rtn.associations[association.object_id.id] = association.tag_ids
			}
			rtn.success = apiResults.success
		} catch(e) {
			log.error("listTagAssociations: ${e}", e)
		} finally {
			if(sessionId) {
				logoutRestSessionId(apiUrl,sessionId)
			}
		}

		return rtn
	}

	static getRestSessionId(apiUrl,username,password,opts=[:]) {
		URIBuilder uriBuilder = new URIBuilder(apiUrl)
		uriBuilder.path = "/rest/com/vmware/cis/session"
		def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/session",username,password,[:],'POST')
		return [success:apiResults.success,sessionId:apiResults.data?.value]
	}

	static logoutRestSessionId(apiUrl,sessionId) {
		URIBuilder uriBuilder = new URIBuilder(apiUrl)
		uriBuilder.path = "/rest/com/vmware/cis/session"
		def apiResults = ApiUtility.callJsonApi(uriBuilder.build().toString(),"/rest/com/vmware/cis/session",null,null,[headers: ["vmware-api-session-id": sessionId]],'DELETE')
		return [success:apiResults.success]
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

	static startVm(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmRuntime = vm.getRuntime()
			if(vmRuntime.getPowerState() == VirtualMachinePowerState.poweredOff || vmRuntime.getPowerState() == VirtualMachinePowerState.suspended) {
				def vmTask = vm.powerOnVM_Task()
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
				}
			} else {
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

	static setScreenResolution(apiUrl, username,password, opts=[:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			log.debug("Setting Screen Resolution: ${opts}")
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmRuntime = vm.getRuntime()

			if(vm && vm.capability.isSettingScreenResolutionSupported()) {
				vm.setScreenResolution(opts.width,opts.height)
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error powering off vm', e)
		} catch(e) {
			log.error("setScreenResolution error: ${e}", e)
			rtn.msg = 'error setting screen resolution on vm'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static stopVm(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
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
							vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
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
				vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
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

	static suspendVm(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmRuntime = vm.getRuntime()
			if(vmRuntime.getPowerState() == VirtualMachinePowerState.poweredOn) {
				vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
				vmRuntime = vm.getRuntime()
				if(vmRuntime.getPowerState() == VirtualMachinePowerState.poweredOn) {
					def vmTask = vm.suspendVM_Task()
					def result = vmTask.waitForTask()
					if(result == Task.SUCCESS) {
						rtn.success = true
					}
				} else {
					rtn.success = true
				}
			} else {
				rtn.msg = 'VM is already powered off'
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error powering off vm', e)
		} catch(e) {
			log.error("pauseVm error: ${e}", e)
			rtn.msg = 'error suspending vm'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static destroyVm(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmRuntime = vm.getRuntime()
			if(vmRuntime.getPowerState() == VirtualMachinePowerState.poweredOff) {
				def vmTask = vm.destroy_Task()
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
				}
			} else {
				rtn.msg = 'VM is already powered off'
			}
		} catch(e) {
			if(e.cause instanceof ManagedObjectNotFound) {
				log.debug("destroyVM: Managed object already removed, allow local delete to continue.")
				rtn.success = true
			} else {
				log.error("destroyVm error: ${e}", e)
				rtn.msg = 'error powering off vm'
			}
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
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
					def vmTask = storageManager.applyStorageDrsRecommendation_Task(keySet)
					vmResult = retryWaitForTask(vmTask)
				} else {
					rtn.msg = 'Error creating vm clone'
				}
			} else {
				def vmTask = vmFolder.createVM_Task(vmSpec,resourcePool,host?.getMOR())
				vmResult = retryWaitForTask(vmTask)
			}
			//finished - proceed
			if(vmResult == Task.SUCCESS) {
				rtn.success = true
				def newVm = new InventoryNavigator(rootFolder).searchManagedEntity('VirtualMachine', vmName)
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
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			def cloneSpec = new VirtualMachineCloneSpec()
			cloneSpec.setPowerOn(false)
			cloneSpec.setTemplate(false)
			def vmSpec = new VirtualMachineConfigSpec()
			vmSpec.setName(vmName)
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
				cloneSpec.setSnapshot(snapshot?.getMOR())
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
			cloneSpec.setConfig(vmSpec)
			def relocateSpec = new VirtualMachineRelocateSpec()
			//set disk transforms
			if(opts.storageType) {
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
			def customResults = addVmCustomizations(serviceInstance, apiUrl, username, password, cloneSpec, opts)
			rtn.customized = customResults.modified
			def vmResult
			if(opts.storagePodId) {
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
					def vmTask = storageManager.applyStorageDrsRecommendation_Task(keySet)
					vmResult = retryWaitForTask(vmTask)
				} else {
					rtn.msg = 'Error creating vm clone'
				}
			} else {
				if(host)
					relocateSpec.setHost(host.getMOR())
				def targetDatastore
				if(datastore) {
					targetDatastore = datastore.getMOR()
				} else {
					if(host)
						targetDatastore = getFreeDatastore(host, maxStorage * 1024l).getMOR()
					else if(cluster)
						targetDatastore = getFreeDatastore(cluster, maxStorage * 1024l).getMOR()
				}
				relocateSpec.setDatastore(targetDatastore)
				//clone it
				def vmTask = vm.cloneVM_Task(vmFolder, vmName, cloneSpec)
				vmResult = retryWaitForTask(vmTask)
			}
			if(vmResult == Task.SUCCESS) {
				rtn.success = true
				def newVm = new InventoryNavigator(rootFolder).searchManagedEntity('VirtualMachine', vmName)
				rtn.results = [server:[id:newVm.getMOR().getVal(), name:vmName, instanceUuid: newVm.getConfig().getInstanceUuid()],
							   entity:newVm, volumes:getVmVolumes(newVm.getConfig().getHardware().getDevice()),
							   controllers:getVmControllers(newVm.getConfig().getHardware().getDevice()),
							   networks:getVmNetworks(newVm.getConfig().getHardware().getDevice())
				]
				if(customResults?.success == true) {
					rtn.modified = customResults.modified
					rtn.results.server.ipAddressList = customResults.ipAddressList
					rtn.results.server.ipAddress = customResults.ipAddress
					rtn.results.server.networkPoolId = customResults.networkPoolId
				}
			} else {
				rtn.msg = 'Error creating vm clone - ' + vmResult
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			rtn.msg = 'error creating vm'
			log.error("cloneVm error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}


	static cloneVmToTemplate(apiUrl, username, password, opts = [:]) {
		log.debug("launch vm from template: ${opts}")
		def rtn = [success:false, modified:false]
		def serviceInstance
		try {
			def vmName = opts.name
			def maxStorage = (opts.maxStorage.toLong().div(1024)) //KB
			//setup
			serviceInstance = connectionPool.getConnection(apiUrl, username, password,true)
			def rootFolder = serviceInstance.getRootFolder()
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def cluster = new InventoryNavigator(datacenter).searchManagedEntity('ComputeResource', opts.cluster)
			def resourcePool = opts.resourcePool ? VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool) : null

			def folder = opts.folder ? getManagedObject(serviceInstance, 'Folder', opts.folder) : null
			def vmFolder = folder ?: datacenter.getVmFolder()
			log.info("cloning vmware vm ${opts.cloneVmId}")
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.cloneVmId)

			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			def cloneSpec = new VirtualMachineCloneSpec()
			cloneSpec.setPowerOn(false)
			cloneSpec.setTemplate(true)
			def vmSpec = new VirtualMachineConfigSpec()
			vmSpec.setName(vmName)
			// vmSpec.setMemoryMB(maxMemory)
			// vmSpec.setNumCPUs(cpuCount)
			// vmSpec.setNumCoresPerSocket(coresPerSocket)
			//annotations

			//extra config
			if(opts.extraConfig) {
				vmSpec.setExtraConfig(opts.extraConfig as OptionValue[])
				log.debug("set extra config: ${opts.extraConfig}")
			}


			def deviceChangeArray = []
			// if(opts.networkConfig?.primaryInterface?.network?.externalId) { //new style multi network
			// 	def primaryInterface = opts.networkConfig.primaryInterface
			// 	def nicSpec
			// 	def networkBackingType = primaryInterface.network.externalType != 'string' ? primaryInterface.network.externalType : 'Network'
			// 	def vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', primaryInterface.network.externalId)
			// 	if(vmNetwork)
			// 		nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, primaryInterface, 0, false,
			// 			[switchUuid:primaryInterface.network.internalId])
			// 	if(nicSpec)
			// 		deviceChangeArray << nicSpec
			// 	//additional nics
			// 	def extraIndex = 1
			// 	opts.networkConfig.extraInterfaces?.each { extraInterface ->
			// 		log.debug("Provisioning extra interface ${extraInterface}")
			// 		if(extraInterface.network?.externalId) {
			// 			nicSpec = null
			// 			networkBackingType = extraInterface.network.externalType != 'string' ? extraInterface.network.externalType : 'Network'
			// 			vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', extraInterface.network.externalId)
			// 			if(vmNetwork)
			// 				nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, extraInterface, extraIndex, false,
			// 					[switchUuid:extraInterface.network?.internalId])
			// 			if(nicSpec)
			// 				deviceChangeArray << nicSpec
			// 			extraIndex++
			// 		}
			// 	}
			// } else {
			// 	def nicSpec
			// 	if(opts.networkId) {
			// 		def vmNetwork = getManagedObject(serviceInstance, opts.networkBackingType ?: 'Network', opts.networkId)
			// 		if(vmNetwork)
			// 			nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, null)
			// 	}
			// 	if(nicSpec)
			// 		deviceChangeArray << nicSpec
			// }
			deviceChangeArray = deviceChangeArray.flatten()
			if(deviceChangeArray?.size() > 0)
				vmSpec.setDeviceChange(deviceChangeArray as VirtualDeviceConfigSpec[])
			cloneSpec.setConfig(vmSpec)
			def relocateSpec = new VirtualMachineRelocateSpec()
			if(resourcePool) {
				relocateSpec.setPool(resourcePool.getMOR())
			}
			cloneSpec.setLocation(relocateSpec)
			def host = opts.hostId ? getManagedObject(serviceInstance, 'HostSystem', opts.hostId) : null
			def datastore = opts.datastoreId ? getManagedObject(serviceInstance, 'Datastore', opts.datastoreId) : null
			def vmResult
			if(opts.storagePodId) {
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
				if(resourcePool) {
					storagePlacementSpec.setResourcePool(resourcePool.getMOR())
				}
				if(host)
					storagePlacementSpec.setHost(host.getMOR())
				def serviceContent = serviceInstance.retrieveServiceContent()
				def storageManager = getManagedObject(serviceInstance, 'StorageResourceManager', serviceContent.getStorageResourceManager().getVal())
				def storagePlacementResult = storageManager.recommendDatastores(storagePlacementSpec)
				log.debug("storagePlacementResult: ${storagePlacementResult}")
				def cloneRecs = storagePlacementResult.getRecommendations()
				def cloneRec = cloneRecs?.length > 0 ? cloneRecs[0] : null
				def cloneKey = cloneRec?.getKey()
				if(cloneKey) {
					def keySet = [cloneKey] as String[]
					def vmTask = storageManager.applyStorageDrsRecommendation_Task(keySet)
					vmResult = retryWaitForTask(vmTask)
				} else {
					rtn.msg = 'Error creating vm clone'
				}
			} else {
				if(host)
					relocateSpec.setHost(host.getMOR())
				if(datastore) {
					relocateSpec.setDatastore(datastore.getMOR())
				} else {
					if(host)
						relocateSpec.setDatastore(getFreeDatastore(host, maxStorage * 1024l).getMOR())
					else if(cluster)
						relocateSpec.setDatastore(getFreeDatastore(cluster, maxStorage * 1024l).getMOR())
				}
				def vmTask = vm.cloneVM_Task(vmFolder, vmName, cloneSpec)
				vmResult = retryWaitForTask(vmTask)
			}
			if(vmResult == Task.SUCCESS) {
				rtn.success = true
				def newVm = new InventoryNavigator(rootFolder).searchManagedEntity('VirtualMachine', vmName)
				rtn.results = [template:[id:newVm.getMOR().getVal(), name:vmName], entity:newVm, volumes:getVmVolumes(newVm.getConfig().getHardware().getDevice()),
							   controllers:getVmControllers(newVm.getConfig().getHardware().getDevice()), networks:getVmNetworks(newVm.getConfig().getHardware().getDevice())]
			} else {
				rtn.msg = 'Error creating vm clone - ' + vmResult
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			rtn.msg = 'error creating vm'
			log.error("cloneVm error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}


	static launchVm(apiUrl, username, password, opts = [:]) {
		log.info("launchVm: ${opts}")
		def rtn = [success: false]
		def serviceInstance
		try {
			def vmName = opts.name
			def vmTemplate = opts.template
			def maxMemory = opts.maxMemory.toLong() //MB convert
			def maxStorage = (opts.maxStorage.toLong().div(1024)).toInteger() //KB
			def cpuCount = opts.cpuCount?.toInteger() ?: 1
			def coresPerSocket = opts.coresPerSocket?.toInteger() ?: 1
			//setup
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def resourcePool = VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
			if(!resourcePool)
				resourcePool = getManagedObject(serviceInstance, 'ResourcePool', opts.resourcePool)
			def folder = opts.folder ? getManagedObject(serviceInstance, 'Folder', opts.folder) : null
			def vmFolder = folder ?: datacenter.getVmFolder()
			log.debug("FOund Folder: ${vmFolder}")
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', vmTemplate)
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			//rename it - attach network - resize disk
			def vmRenameTask = vm.rename_Task(vmName)
			def vmRenameTaskResult = vmRenameTask.waitForTask()
			if(vmRenameTaskResult == Task.SUCCESS) {
				//reconfigure it
				def vmConfigSpec = new VirtualMachineConfigSpec()
				vmConfigSpec.setName(vmName)
				vmConfigSpec.setMemoryMB(maxMemory)
				vmConfigSpec.setNumCPUs(cpuCount)
				vmConfigSpec.setNumCoresPerSocket(coresPerSocket)
				def deviceChangeList = []
				if(maxStorage > 0) {
					def diskSpec
					virtualDevices?.each { virtualDevice ->
						if(virtualDevice instanceof VirtualDisk) {
							diskSpec = new VirtualDeviceConfigSpec()
							diskSpec.setOperation(VirtualDeviceConfigSpecOperation.edit)
							virtualDevice.setCapacityInKB(maxStorage)
							diskSpec.setDevice(virtualDevice)
						}
					}
					if(diskSpec)
						deviceChangeList << diskSpec
				}
				if(opts.annotation)
					vmConfigSpec.setAnnotation(opts.annotation)
				def nicSpec
				if(opts.networkId) {
					def vmNetwork = getManagedObject(serviceInstance, opts.networkBackingType ?: 'Network', opts.networkId)
					if(vmNetwork)
						nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance)
				}
				if(nicSpec)
					deviceChangeList << nicSpec
				if(deviceChangeList?.size() > 0) {
					deviceChangeList = deviceChangeList.flatten()
					def deviceChangeArray = deviceChangeList.toArray() as VirtualDeviceConfigSpec[]
					vmConfigSpec.setDeviceChange(deviceChangeArray)
				}
				def vmTask = vm.reconfigVM_Task(vmConfigSpec)
				def result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
					def newVm = new InventoryNavigator(rootFolder).searchManagedEntity('VirtualMachine', vmName)
					rtn.results = [server:[id:newVm.getMOR().getVal(), name:vmName], entity:newVm, volumes:getVmVolumes(newVm.getConfig().getHardware().getDevice()),
								   controllers:getVmControllers(newVm.getConfig().getHardware().getDevice()), networks:getVmNetworks(newVm.getConfig().getHardware().getDevice())]
				} else {
					rtn.msg = 'Error configuring vm'
				}
			} else {
				rtn.msg = 'Error rename vm'
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm', e)
		} catch(e) {
			rtn.msg = e.message
			log.error("launchVm error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}




	static toList(value) {
		[value].flatten()
	}

	static createDistributedSwitch(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {

			def switchName = opts.networkName
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def networkFolder = datacenter.getNetworkFolder()
			def dsSpec = new DVSCreateSpec()
			def dsConfigSpec = new VMwareDVSConfigSpec()
			dsConfigSpec.setName(switchName)
			//switchIpAddress
			//host
			def dsTask = networkFolder.createDVS_Task()
		} catch(e) {
			rtn.msg = 'error creating distributed switch'
			log.error("createDistributedSwitch error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static validateServerConfig(apiUrl, username, password, Map opts = [:]) {
		log.debug("validateServerConfig: ${opts}")
		def rtn = [success:false, errors:[]]
		try {
			//template
			if(opts.validateTemplate && !opts.template)
				rtn.errors << [field:'template', msg:'Template is required']
			//network
			if(opts.networkId) {
				// great
			} else if (opts?.networkInterfaces) {
				// JSON (or Map from parseNetworks)
				log.debug("validateServerConfig networkInterfaces: ${opts?.networkInterfaces}")
				opts?.networkInterfaces?.eachWithIndex { nic, index ->
					def networkId = nic.network?.id ?: nic.network.group
					log.debug("network.id: ${networkId}")
					if(!networkId) {
						rtn.errors << [field:'networkInterface', msg:'Network is required']
					}
				}
			} else if (opts?.networkInterface) {
				// UI params
				log.debug("validateServerConfig networkInterface: ${opts.networkInterface}")
				toList(opts?.networkInterface?.network?.id)?.eachWithIndex { networkId, index ->
					log.debug("network.id: ${networkId}")
					if(networkId?.length() < 1) {
						rtn.errors << [field:'networkInterface', msg:'Network is required']
					}
				}

			} else {
				rtn.errors << [field:'networkId', msg:'Network is required']
			}
			//if no api url - esxi needs a host selected?
			if(!apiUrl)
				rtn.errors << [field:'hostId', msg:'ESXi host is required']

			def resourcePoolId = opts?.resourcePoolId ?: opts?.config?.resourcePoolId ?: opts.server?.config?.resourcePoolId ?: opts?.resourcePool ?: opts?.config?.vmwareResourcePoolId
			if(!resourcePoolId) {
				rtn.errors += [field: 'resourcePoolId', msg: 'Resource Pool is required']
			}
			// If host is selected, must validate that the cluster or datastore is reachable by the host
			if(apiUrl && opts.hostId) {
				// Gather up the datastore ids to validate (ignore 'auto')
				def dataStoreIds = []
				opts.volumes?.each { volume ->
					if(volume?.datastoreId) {
						dataStoreIds << volume.datastoreId
					}
					if (volume?.controllerMountPoint) {
						def mountConfig = volume?.controllerMountPoint.tokenize(':')
						// log.debug("mountConfig: ${mountConfig}")
						// id:busNumber:typeId:unitId
						// def controllerId = mountConfig[0]
						// def busNumber = mountConfig[1]
						def typeId = mountConfig[2]?.toLong()
						def unitNumber = mountConfig[3]
						def storageType = StorageControllerType.read(typeId)
						if (storageType.reservedUnitNumber == unitNumber) {
							rtn.errors += [field:'volumes', msg:"the storage controller unit selected is reserverd: ${volume?.controllerMountPoint}"]
						}
					}
				}
				if(dataStoreIds) {
					if(ignoreSsl) {
						SSLUtility.trustAllHostnames()
						SSLUtility.trustAllHttpsCertificates()
					}
					def serviceInstance = connectionPool.getConnection(apiUrl, username, password)
					ComputeServer computeServer = ComputeServer.read(opts.hostId.toLong())
					def host = getManagedObject(serviceInstance, 'HostSystem', computeServer.externalId)
					log.debug "validateServerConfig host: ${host}"
					def hostDataStores = host.getDatastores()
					log.debug "validateServerConfig hostDataStores: ${hostDataStores}"
					dataStoreIds.each { dataStoreId ->
						if(opts.datastoreId &&  dataStoreId != 'autoCluster' && dataStoreId == 'auto' ) {
							def datastore = com.morpheus.Datastore.read(opts.datastoreId.toLong())
							if(datastore.type == 'generic') {
								def datastoreAvailable = hostDataStores.find { it.getMOR().getVal() == datastore.externalId } != null
								if(!datastoreAvailable)
									rtn.errors << [field:'volume', msg :"Datastore ${datastore.name} is not accessible to host ${computeServer.name}"]
							} else if(datastore.type == 'cluster') {
								def storagePod = getManagedObject(serviceInstance, 'StoragePod', datastore.externalId)
								storagePod.getChildEntity().each { vmds ->
									def currentDatastoreExtId = vmds.getMOR().getVal()
									def datastoreAvailable = hostDataStores.find { it.getMOR().getVal() == currentDatastoreExtId } != null
									if(!datastoreAvailable) {
										rtn.errors << [field:'volume', msg :"Datastore ${vmds.getName()} within cluster ${datastore.name} is not accessible to host ${computeServer.name}"]
									}
								}
							}
						}
					}
				}
			}
			rtn.success = rtn.errors.size() == 0
		} catch(e) {
			log.error "validateServerConfig error: ${e}", e
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

	static snapshotVm(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false, externalId: opts.externalId]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def snapshotName = opts.snapshotName ?: vm.getName() + ' ' + System.currentTimeMillis()
			def snapshotDescription = opts.snapshotDescription ?: ''
			def vmTask
			def result = ""
			try{
				vmTask = vm.createSnapshot_Task(snapshotName, snapshotDescription, false, true)
				result = vmTask.waitForTask()
			} catch (ex){
				log.warn("Quiescent snapshot failed ${ex.getMessage()}")
			}
			if(result == Task.SUCCESS) {
				rtn.success = true
				//def taskObject = getManagedObject(serviceInstance, 'Task', vmTask.getVal())
				rtn.snapshotId = vmTask.getTaskInfo().getResult().getVal()
				sleep(10000)
				log.info("Received Snapshot ID ${rtn.snapshotId}")
			} else {
				log.warn("Quiscent Snapshot Failed, Attempting Standard Snapshot")
				vmTask = vm.createSnapshot_Task(snapshotName, snapshotDescription, false, false)
				result = vmTask.waitForTask()
				if(result == Task.SUCCESS) {
					rtn.success = true
					//def taskObject = getManagedObject(serviceInstance, 'Task', vmTask.getVal())
					rtn.snapshotId = vmTask.getTaskInfo().getResult().getVal()
					sleep(5000)
					log.info("Received Snapshot ID ${rtn.snapshotId}")
				}
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating vm snapshot', e)
		} catch(e) {
			log.error("snapshotVm error: ${e}", e)
			rtn.msg = 'error creating vm snapshot'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static exportVm() {

	}

	static exportVmSnapshot(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def exportLease
		def exportUpdater
		def serviceInstance
		ZipOutputStream targetZipStream
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password, true, true)
			def rootFolder = serviceInstance.getRootFolder()
			def vmSnapshot = getManagedObject(serviceInstance, 'VirtualMachineSnapshot', opts.snapshotId)
			exportLease = vmSnapshot.exportSnapshot()
			def ready = false
			def hls
			while(ready == false) {
				hls = exportLease.getState()
				sleep(100)
				if(hls == HttpNfcLeaseState.ready || hls == HttpNfcLeaseState.error)
					ready = true
			}
			if(hls == HttpNfcLeaseState.error) {
				log.error("Error Acquiring Export OVF Template Lease: ${exportLease.getError()}")
				return [success: false, msg: 'error acquiring ovf template export']
			}

			HttpNfcLeaseInfo exportLeaseInfo = (HttpNfcLeaseInfo)(exportLease.getInfo())
			exportLeaseInfo.setLeaseTimeout(300000000)
			def sessionId = serviceInstance.getSessionManager().getServerConnection().getVimService().getWsc().getCookie()
			exportUpdater = new VmwareLeaseProgressUpdater(exportLease, 5000)
			exportUpdater.start()
			def targetDir = opts.targetDir
			targetZipStream = (ZipOutputStream)opts.targetZipStream
			def downloadResults
			if(targetZipStream) {
				downloadResults = downloadVm(exportLeaseInfo, targetZipStream, sessionId, exportUpdater, opts)
			} else {
				downloadResults = downloadVm(exportLeaseInfo, targetDir, sessionId, exportUpdater, opts)
			}

			if(downloadResults.success == true) {
				def ovfWriter
				if(targetZipStream) {
					ZipEntry ovfEntry = new ZipEntry(opts.vmName + '.ovf')
					targetZipStream.putNextEntry(ovfEntry)
					ovfWriter = new OutputStreamWriter(targetZipStream)
				} else {
					def ovfOutput = new File(targetDir, opts.vmName + '.ovf')
					ovfOutput.createNewFile()
					ovfWriter = new FileWriter(ovfOutput)
				}
				//def ovfDescriptor = serviceInstance.getOvfManager().createDescriptor(vmSnapshot, downloadResults.ovfConfig)
				def ovfDescriptor = serviceInstance.getOvfManager().createDescriptor(vmSnapshot.getVm(), downloadResults.ovfConfig)

				ovfWriter.write(ovfDescriptor.getOvfDescriptor())
				ovfWriter.flush()
				// ovfWriter.close()
				log.debug("got: ${downloadResults}")
				rtn.success = true
				exportLease.httpNfcLeaseProgress(100)
				exportUpdater.interrupt()
				sleep(3000)
				exportLease.httpNfcLeaseComplete()
			} else {
				exportUpdater.interrupt()
				exportLease.httpNfcLeaseAbort()
			}
		} catch(e) {
			log.error("exportVmSnapshot error: ${e}", e)
			rtn.msg = 'error powering off vm'
			exportUpdater.interrupt()
			exportLease.httpNfcLeaseAbort()
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
			if(targetZipStream) { targetZipStream.flush() ; targetZipStream.close() }
		}

		return rtn
	}

	static downloadVm(exportLeaseInfo, ZipOutputStream targetZipStream, token, leaseUpdater, opts) {
		def rtn = [success: false, ovfFiles: []]
		try {
			if(!exportLeaseInfo) {
				rtn.msg = "An error occurred attempting to acquire a Vmware Export OVF Template Lease... There could be a problem with your vmware services. See KB: https://kb.vmware.com/selfservice/microsites/search.do?language=en_US&cmd=displayKC&externalId=1027707"
				log.error(rtn.msg)
				return rtn
			}
			printHttpNfcLeaseInfo(exportLeaseInfo)
			def deviceUrls = exportLeaseInfo.getDeviceUrl()
			long bytesAlreadyWritten = 0
			deviceUrls.eachWithIndex { deviceUrl, deviceIndex ->
				def ovfFile = new OvfFile()
				ovfFile.setDeviceId(deviceUrl.getKey())
				def ovfPath = deviceUrl.getUrl()
				def ovfName = ovfPath.substring(ovfPath.lastIndexOf('/') + 1)
				ovfFile.setPath(ovfName)
				log.info("Zipping: ${ovfName}")
				ZipEntry zipEntry = new ZipEntry(ovfName)
				targetZipStream.putNextEntry(zipEntry)
				if(ovfPath?.indexOf('*') > -1) {
					def hostname = opts.hostIp ?: opts.hostName
					ovfPath = ovfPath.replace("*", hostName)
				}
				// def bufferOutputStream = new BufferedOutputStream()
				def downloadResults = downloadImage(ovfPath, targetZipStream, token, leaseUpdater,deviceUrls.size(), deviceIndex)
				ovfFile.setSize(downloadResults.contentLength)
				rtn.ovfFiles << ovfFile
			}
			rtn.ovfConfig = new OvfCreateDescriptorParams()
			rtn.ovfConfig.setOvfFiles(rtn.ovfFiles as OvfFile[])
			rtn.success = true
		} catch(e) {
			log.error("downloadVm error: ${e}", e)
		}
		return rtn
	}


	static downloadVm(exportLeaseInfo, String targetDir, token, leaseUpdater, opts) {
		def rtn = [success: false, ovfFiles: []]
		try {
			if(!exportLeaseInfo) {
				rtn.msg = "An error occurred attempting to acquire a Vmware Export OVF Template Lease... There could be a problem with your vmware services. See KB: https://kb.vmware.com/selfservice/microsites/search.do?language=en_US&cmd=displayKC&externalId=1027707"
				log.error(rtn.msg)
				return rtn
			}
			printHttpNfcLeaseInfo(exportLeaseInfo)
			def deviceUrls = exportLeaseInfo.getDeviceUrl()
			long bytesAlreadyWritten = 0
			deviceUrls.eachWithIndex { deviceUrl, deviceIndex ->
				def ovfFile = new OvfFile()
				ovfFile.setDeviceId(deviceUrl.getKey())
				def ovfPath = deviceUrl.getUrl()
				def ovfName = ovfPath.substring(ovfPath.lastIndexOf('/') + 1)
				ovfFile.setPath(ovfName)
				def targetFile = new File(targetDir, ovfName)
				if(!targetFile.exists())
					targetFile.createNewFile()
				if(ovfPath?.indexOf('*') > -1) {
					def hostname = opts.hostIp ?: opts.hostName
					ovfPath = ovfPath.replace("*", hostName)
				}
				// def bufferOutputStream = new BufferedOutputStream()
				def outStream = targetFile.newOutputStream()
				def downloadResults = downloadImage(ovfPath, outStream, token, leaseUpdater,deviceUrls.size(), deviceIndex)
				outStream.close()
				ovfFile.setSize(downloadResults.contentLength)
				rtn.ovfFiles << ovfFile
			}
			rtn.ovfConfig = new OvfCreateDescriptorParams()
			rtn.ovfConfig.setOvfFiles(rtn.ovfFiles as OvfFile[])
			rtn.success = true
		} catch(e) {
			log.error("downloadVm error: ${e}", e)
		}
		return rtn
	}

	static archiveVmSnapshot(apiUrl, username, password, cloudBucket, archiveFolder, opts = [:], Closure progressCallback = null) {
		def rtn = [success: false]
		def exportLease
		def exportUpdater
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password,true,true)
			def rootFolder = serviceInstance.getRootFolder()
			VirtualMachineSnapshot vmSnapshot = getManagedObject(serviceInstance, 'VirtualMachineSnapshot', opts.snapshotId)
			//String dspath = vmSnapshot.config.files.snapshotDirectory
			VirtualMachine vm = vmSnapshot.getVm()
			//VirtualMachineFileLayout vmLayout = vm.getLayout()
			//VirtualMachineFileLayoutSnapshotLayout[] snapshotLayouts = vmLayout.getSnapshot()
			//def hostBrowser = vm.environmentBrowser.datastoreBrowser
			// snapshotLayouts?.each { snapshotLayout ->
			// 	log.debug("snapshot layout: ${snapshotLayout}")
			// 	def snapshotFiles = snapshotLayout.getSnapshotFile()
			// 	snapshotFiles?.each { snapshotFile ->
			// 		def searchSpec = new HostDatastoreBrowserSearchSpec()
			// 		def fileIndex = snapshotFile.lastIndexOf('/')
			// 		def fileName = snapshotFile.substring(fileIndex + 1)
			// 		def filePath = snapshotFile.substring(0, fileIndex)
			// 		searchSpec.setMatchPattern([fileName] as String[])
			// 		def queryFlags = new FileQueryFlags()
			// 		queryFlags.setFileOwner(false)
			// 		queryFlags.setFileSize(true)
			// 		searchSpec.setDetails(queryFlags)
			// 		def task = hostBrowser.searchDatastore_Task(filePath, searchSpec)
			// 		task.waitForTask()
			// 		def searchResults = task.getTaskInfo().getResult()
			// 		if(searchResults) {
			// 			FileInfo fileInfo = searchResults.getFile()[0]
			// 			log.debug("fileInfo: ${fileInfo.path} - ${fileInfo.fileSize}")
			// 		} else {
			// 			log.warn("not found")
			// 		}
			// 	}
			// }
			exportLease = vmSnapshot.exportSnapshot()
			def ready = false
			def hls
			while(ready == false) {
				hls = exportLease.getState()
				sleep(100)
				if(hls == HttpNfcLeaseState.ready || hls == HttpNfcLeaseState.error)
					ready = true
			}
			if(hls == HttpNfcLeaseState.error) {
				log.error("Error Acquiring Export OVF Template Lease: ${exportLease.getError()}")
				return [success: false, msg: 'error acquiring ovf template export']
			}
			def exportLeaseInfo = exportLease.getInfo()
			def sessionId = serviceInstance.getSessionManager().getServerConnection().getVimService().getWsc().getCookie()
			exportUpdater = new VmwareLeaseProgressUpdater(exportLease, 5000)
			if(opts.progressCallback)
				exportUpdater.setProgressCallback(opts.progressCallback)
			exportUpdater.start()
			def downloadResults = archiveVm(exportLeaseInfo, cloudBucket, archiveFolder, sessionId, exportUpdater, opts, progressCallback)
			if(downloadResults.success == true) {
				def ovfDescriptor = serviceInstance.getOvfManager().createDescriptor(vm, downloadResults.ovfConfig)
				def ovfOutput = cloudBucket["${archiveFolder}/${opts.vmName}.ovf"]
				ovfOutput.text = ovfDescriptor.getOvfDescriptor()
				ovfOutput.save()
				log.debug("got: ${downloadResults}")
				rtn.success = true
				exportLease.httpNfcLeaseProgress(100)
				exportUpdater.interrupt()
				sleep(3000)
				exportLease.httpNfcLeaseComplete()
			} else {
				exportUpdater.interrupt()
				exportLease.httpNfcLeaseAbort()
			}
		} catch(e) {
			log.error("archiveVmSnapshot error: ${e}", e)
			rtn.msg = 'error powering off vm'
			exportUpdater?.interrupt()
			exportLease?.httpNfcLeaseAbort()
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static archiveVm(exportLeaseInfo, cloudBucket, archiveFolder, token, leaseUpdater, opts, Closure progressCallback = null) {
		def rtn = [success: false, ovfFiles: []]
		try {
			log.info("Lease Info Acquired ${exportLeaseInfo}")
			opts.diskCapacity = (exportLeaseInfo.getTotalDiskCapacityInKB()) * 1024
			printHttpNfcLeaseInfo(exportLeaseInfo)
			def deviceUrls = exportLeaseInfo.getDeviceUrl()
			long bytesAlreadyWritten = 0
			deviceUrls.eachWithIndex { deviceUrl, deviceIndex ->
				def ovfFile = new OvfFile()
				ovfFile.setDeviceId(deviceUrl.getKey())
				def ovfPath = deviceUrl.getUrl()
				def ovfName = ovfPath.substring(ovfPath.lastIndexOf('/') + 1)
				ovfFile.setPath(ovfName)
				def targetFile = cloudBucket["${archiveFolder}/${ovfName}"]
				if(ovfPath?.indexOf('*') > -1) {
					def hostname = opts.hostIp ?: opts.hostName
					ovfPath = ovfPath.replace("*", hostName)
				}
				Closure nestedCallback = { percent ->
					def totalPercent = (percent + (100*deviceIndex)) / deviceUrls.size()
					if(progressCallback) {
						progressCallback.call(totalPercent)
					}
				}
				def downloadResults = archiveImage(ovfPath, targetFile, token, leaseUpdater, opts.diskCapacity ?: deviceUrl.getFileSize(), nestedCallback, deviceUrls.size(), deviceIndex)
				ovfFile.setSize(downloadResults.contentLength)
				rtn.ovfFiles << ovfFile
			}
			rtn.ovfConfig = new OvfCreateDescriptorParams()
			rtn.ovfConfig.setOvfFiles(rtn.ovfFiles as OvfFile[])
			rtn.success = true
		} catch(e) {
			log.error("downloadVm error: ${e}", e)
		}
		return rtn
	}

	static moveVm(apiUrl, username, password, opts = [:]) {

	}

	static restoreVmSnapshot(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def vmSnapshot = getManagedObject(serviceInstance, 'VirtualMachineSnapshot', opts.snapshotId)
			def vmTask = vmSnapshot.revertToSnapshot_Task(null)
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS) {
				rtn.success = true
				//rtn.snapshotId = vmTask.getTaskInfo().getResult().getVal()
			}
		} catch(e) {
			log.error("restoreVmSnapshot error: ${e}", e)
			rtn.msg = 'error restoring snapshot'
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
		//Task task = vmsnap.revertToSnapshot_Task(null)

	}

	static removeVmSnapshot(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			log.info("removing vmware snapshot ${opts.snapshotId}")
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def vmSnapshot = getManagedObject(serviceInstance, 'VirtualMachineSnapshot', opts.snapshotId)
			def vmTask = vmSnapshot.removeSnapshot_Task(false)
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS) {
				rtn.success = true
			}
		} catch(e) {
			//log.error("removeVmSnapshot error: ${e}")
			rtn.msg = "error removing snapshot ${opts.snapshotId}"
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static removeAllVmSnapshots(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {

			log.info("removing vmware snapshots for vm ${opts.externalId}")
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmTask = vm.removeAllSnapshots_Task()
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS) {
				rtn.success = true
			}
		} catch(e) {
			//log.error("removeAllVmSnapshots error: ${e}")
			rtn.msg = "error removing snapshots for vm ${opts.externalId}"
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
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

	static filterVmVolumes(virtualDevices) {
		def rtn = []
		try {
			virtualDevices?.each { virtualDevice ->
				if(virtualDevice instanceof VirtualDisk && !(virtualDevice instanceof VirtualCdrom)) {
					rtn << virtualDevice
				}
			}
		} catch(e) {
			log.error("filterVmVolumes error: ${e}")
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

	static findNic(networks, index, type) {
		def rtn = networks.find{it.type == type && it.key == index}
		if(!rtn)
			rtn = networks?.size() > index ? networks[index] : null
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

	static getAssigedOrFreeDatastore(serviceInstance, datastoreId, vm, diskSize) {
		def rtn
		try {
			if(datastoreId)
				rtn = getDatastoreOrStoragePodDatastore(serviceInstance, datastoreId, vm, diskSize)
			if(rtn == null)
				rtn = getFreeDatastore(vm, diskSize)
		} catch(e) {
			log.error("getAssigedOrFreeDatastore error: ${e}", e)
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

	static getFreeDatastoreName(vm, size, datastoreId = null) {
		def datastore = getFreeDatastore(vm, size, datastoreId)
		return datastore ? datastore.getName() : null
	}

	static getFreeStoragePod(zoneId) {
		def rtn
		def existingItems = ReferenceData.withCriteria {
			eq('category', "vmware.vsphere.storagePod.${zoneId}")
		}
		if(existingItems?.size() > 0) {
			rtn = existingItems.max{it.numberValue}?.keyValue
		}
		return rtn
	}

	static addCloudInitIso(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		try {
			def cloudConfigUser = opts.cloudConfigUser
			def cloudConfigMeta = opts.cloudConfigMeta
			def cloudConfigNetwork = opts.cloudConfigNetwork
			def cloudIsoOutputStream = IsoUtility.buildCloudIso(opts.platform, cloudConfigMeta, cloudConfigUser, cloudConfigNetwork)
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
			if(!fileExists(username,password,sessionId,uploadUrl) || opts.overwrite) {
				def uploadResults = uploadFile(username, password, sessionId, inputStream, contentLength, uploadUrl)
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

	static getDatacenterPath(ServiceInstance serviceInstance, Datacenter datacenter) {
		def folderPath = ''

		def parent = datacenter.getParent()
		while(parent != null) {
			if(parent.name == 'Datacenters') {
				break
			} else {
				folderPath = "${parent.name}/" + folderPath
				parent = parent.getParent()
			}
		}
		return folderPath + datacenter.getName()
	}

	static encodeUrl(str) {
		return java.net.URLEncoder.encode(str, "UTF-8").replaceAll('\\+', '%20') //.replaceAll('%', '%25')
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

	static doTaskAnswerQuestions(vm, vmTask) {
		def rtn = false
		def future
		def keepGoing = true
		def counter = 0
		future = monitorServiceExecutor.submit(new Runnable() {
			@Override
			void run() {
				while(keepGoing == true) {
					try {
						log.debug("ANSWERING QUESTION FOR CD EJECT!!!")
						def runtimeInfo = vm.getRuntime()
						def question = runtimeInfo.getQuestion()
						log.debug("checking question: ${question}")
						if(question != null) {
							log.debug("Checking question  ${question.getMessage()} - ${question.getText()}")
							if(question.getMessage() != null) {
								for(VirtualMachineMessage msg : question.getMessage()) {
									log.debug("Message Id: ${msg.getId()}")
									if("msg.cdromdisconnect.locked".equalsIgnoreCase(msg.getId())) {
										log.info('answering msg.cdromdisconnect.locked')
										def answer = '0'
										def choice = question.getChoice()
										if(choice != null) {
											choice.getChoiceInfo()?.each { info ->
												if(info.getLabel() == 'button.yes')
													answer = info.getKey()
											}
										}
										vm.answerVM(question.getId(), answer)
										break
									}
								}
							} else if(question.getText() != null) {
								def text = question.getText()
								def msgId
								def msgText
								def tokens = text.split(':')
								msgId = tokens[0]
								msgText = tokens[1]
								if("msg.cdromdisconnect.locked".equalsIgnoreCase(msgId))
									log.info('answering text msg.cdromdisconnect.locked')
								def answer = '0'
								def choice = question.getChoice()
								if(choice != null) {
									choice.getChoiceInfo()?.each { info ->
										if(info.getLabel() == 'button.yes')
											answer = info.getKey()
									}
								}
								vm.answerVM(question.getId(), answer)
								break
							}
						}
					} catch(e) {
						log.error("error answering questions: ${e}", e)
					}
					sleep(5000)
					counter++
					if(counter > 100)
						keepGoing = false
				}
			}
		})
		try {
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS)
				rtn = true
		} catch(e) {
			log.error("doTaskAnswerQuestions error: ${e}", e)
		} finally {
			keepGoing = false
			future.cancel(true)
		}
		return rtn
	}

	static removeVmFile(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def datacenter = new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter)
			def datastore = getFreeDatastore(vm, 1, opts.datastoreId)
			def filePath = "[" + datastore.getName() + "] " + vm.getName() + "/" + opts.fileName
			log.info("remove file path: ${filePath}")
			def fileManager = serviceInstance.getFileManager()
			def vmTask = fileManager.deleteDatastoreFile_Task(filePath, datacenter)
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS) {
				rtn.success = true
			}
		} catch(e) {
			log.error("removeVmFile error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static addVmNetwork(apiUrl, username, password, opts = [:]) {
		log.info("addVmNetwork: ${apiUrl}, ${username}, ${opts}")
		def rtn = [success:false]
		def serviceInstance
		try {

			def networkConfig = opts.networkConfig
			if(networkConfig?.network?.externalId) {
				serviceInstance = connectionPool.getConnection(apiUrl, username, password)
				def rootFolder = serviceInstance.getRootFolder()
				def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
				def vmConfig = vm.getConfig()
				def virtualDevices = vmConfig.getHardware().getDevice()
				def networkBackingType = networkConfig.network.externalType != 'string' ? networkConfig.network.externalType : 'Network'
				def vmNetwork = getManagedObject(serviceInstance, networkBackingType ?: 'Network', networkConfig.network.externalId ?: networkConfig.network.parentNetwork?.externalId)
				if(vmNetwork) {
					def nicIndex = networkConfig.displayOrder ?: opts.index ?: 0
					def nicType = networkConfig?.type?.code ?: 'vmxNet3'
					def nicSpec = createSetNetworkOnNicSpec(virtualDevices, vmNetwork, serviceInstance, networkConfig, nicIndex, true,
							[switchUuid:networkConfig.network.internalId])
					def vmConfigSpec = new VirtualMachineConfigSpec()
					def deviceChange = nicSpec as VirtualDeviceConfigSpec[]
					vmConfigSpec.setDeviceChange(deviceChange)
					def vmConfigTask = vm.reconfigVM_Task(vmConfigSpec)
					def result = vmConfigTask.waitForTask()
					if(result == Task.SUCCESS) {
						log.info("created network")
						rtn.success = true
						vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
						vmConfig = vm.getConfig()
						virtualDevices = vmConfig.getHardware().getDevice()
						rtn.networks = getVmNetworks(vm.getConfig().getHardware().getDevice())
						def newNic = findNic(rtn.networks, nicIndex, nicType)
						if(newNic) {
							log.debug("found created network adapter: ${nicIndex}")
							rtn.network = newNic
							rtn.entity = vm
						}
					} else {
						rtn.msg = 'error adding network adapter'
					}
				} else {
					rtn.msg = 'requested network not found'
				}
			} else {
				rtn.msg = 'requested network not found'
			}
		} catch(MethodFault e) {
			rtn.msg = logFaultError('error creating network adapter', e)
		} catch(e) {
			log.error("addVmNetwork error: ${e}", e)
			rtn.msg = 'error creating network adapter'
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
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			int diskSize = opts.diskSize?.toInteger()
			def diskSpec = new VirtualDeviceConfigSpec()
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			def virtualDisk = new VirtualDisk()
			def diskfileBacking = getDiskfileBacking(virtualDevices, thin)
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

	static findController(virtualDevices, busNumber = null, typeCode = null) {
		def rtn
		virtualDevices?.each { virtualDevice ->
			if(rtn == null) {
				if(busNumber != null && typeCode != null) {
					if(virtualDevice instanceof com.vmware.vim25.VirtualController && virtualDevice.getBusNumber() == busNumber) {
						if(typeCode == 'vmware-ide' && virtualDevice instanceof com.vmware.vim25.VirtualIDEController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-busLogic' && virtualDevice instanceof com.vmware.vim25.VirtualBusLogicController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-lsiLogic' && virtualDevice instanceof com.vmware.vim25.VirtualLsiLogicController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-lsiLogicSas' && virtualDevice instanceof com.vmware.vim25.VirtualLsiLogicSASController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-paravirtual' && virtualDevice instanceof com.vmware.vim25.ParaVirtualSCSIController) {
							rtn = virtualDevice
						}
					}
				} else {
					if(virtualDevice instanceof com.vmware.vim25.VirtualSCSIController)
						rtn = virtualDevice
				}
			}
		}
		return rtn
	}

	static findVmNic(virtualDevices, index) {
		def rtn
		virtualDevices?.each { virtualDevice ->
			if(rtn == null) {
				if(busNumber != null && typeCode != null) {
					if(virtualDevice instanceof com.vmware.vim25.VirtualController && virtualDevice.getBusNumber() == busNumber) {
						if(typeCode == 'vmware-ide' && virtualDevice instanceof com.vmware.vim25.VirtualIDEController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-busLogic' && virtualDevice instanceof com.vmware.vim25.VirtualBusLogicController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-lsiLogic' && virtualDevice instanceof com.vmware.vim25.VirtualLsiLogicController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-lsiLogicSas' && virtualDevice instanceof com.vmware.vim25.VirtualLsiLogicSASController) {
							rtn = virtualDevice
						} else if(typeCode == 'vmware-paravirtual' && virtualDevice instanceof com.vmware.vim25.ParaVirtualSCSIController) {
							rtn = virtualDevice
						}
					}
				} else {
					if(virtualDevice instanceof com.vmware.vim25.VirtualSCSIController)
						rtn = virtualDevice
				}
			}
		}
		return rtn
	}

	static createController(type) {
		if(type == 'vmware-ide')
			return new VirtualIDEController()
		if(type == 'vmware-busLogic')
			return new VirtualBusLogicController()
		if(type == 'vmware-lsiLogic')
			return new VirtualLsiLogicController()
		if(type == 'vmware-lsiLogicSas')
			return new VirtualLsiLogicSASController()
		if(type == 'vmware-paravirtual')
			return new ParaVirtualSCSIController()
		return null
	}

	static findFreeUnitNumber(virtualDevices, key, suggested = null) {
		def rtn
		def controllerDevices = virtualDevices.findAll { it.getControllerKey() == key }
		def freeUnitFound = false
		if(suggested != null) {
			def match = controllerDevices.find{ it.getUnitNumber() == suggested }
			if(!match) {
				rtn = suggested
				freeUnitFound = true
			}
		}
		// We should always have something set so at some point this should throw an error
		// But right now the host wizard and api doesnt show/set controller mount points
		// so this is still needed
		(0..14).each { index ->
			if(freeUnitFound == false) {
				def match = controllerDevices.find { it.getUnitNumber() == index }
				// Vmware reserves unit number 7 so dont set it
				if(match == null && index != 7) {
					rtn = index
					freeUnitFound = true
				}
			}
		}
		return rtn
	}

	static findFreeBusNumber(virtualDevices, suggested = null, controllerType = null) {
		def rtn
		def controllerDevices
		if(controllerType == 'vmware-ide')
			controllerDevices = virtualDevices.findAll { it instanceof com.vmware.vim25.VirtualIDEController }
		else
			controllerDevices = virtualDevices.findAll { it instanceof com.vmware.vim25.VirtualSCSIController }
		def freeBusFound = false
		if(suggested != null) {
			def match = controllerDevices.find { it.getBusNumber() == suggested }
			if(!match) {
				rtn = suggested
				freeBusFound = true
			}
		}
		(0..14).each { index ->
			if(freeBusFound == false) {
				def match = controllerDevices.find { it.getBusNumber() == index }
				if(match == null) {
					rtn = index
					freeBusFound = true
				}
			}
		}
		return rtn
	}

	static adjustVmResources(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def vmConfigSpec = new VirtualMachineConfigSpec()
			def vmConfig = vm.getConfig()
			if(opts.maxMemory)
				vmConfigSpec.setMemoryMB(opts.maxMemory?.toLong())
			if(opts.maxCpu){
				vmConfigSpec.setNumCPUs(opts.maxCpu?.toInteger())
				vmConfigSpec.setNumCoresPerSocket(opts.coresPerSocket?.toInteger() ?: 1)
			}
			if(opts.containsKey('notes')) {
				vmConfigSpec.setAnnotation(opts.notes)
			}
			def vmTask = vm.reconfigVM_Task(vmConfigSpec)
			def result = vmTask.waitForTask()
			if(result == Task.SUCCESS)
				rtn.success = true
		} catch(e) {
			log.error("adjustVmResources error: ${e.message}", e)
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
				com.morpheus.util.SSLUtility.trustAllHostnames()
				com.morpheus.util.SSLUtility.trustAllHttpsCertificates()
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

	static getDiskfileBacking(deviceList, thin = true) {
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
		}
		return rtn
	}

	static getNewDiskfileBacking(device, storageType) {
		def rtn = [changed:false, backing:null]
		def backing = device.getBacking()
		log.debug("checking backing: ${backing} - new type: ${storageType}")
		//check by current type
		if(backing instanceof VirtualDiskRawDiskMappingVer1BackingInfo) {
			//cant change raw to thin - maybe could check datastore for other thin types - but this is an older format
			rtn.changed = false
		} else if(backing instanceof VirtualDiskFlatVer1BackingInfo) {
			if(storageType == 'thin') {
				//gotta change it out to v2 - might error
				rtn.changed = true
				rtn.backing = new VirtualDiskFlatVer2BackingInfo()
				rtn.backing.setThinProvisioned(true)
			}
		} else if(backing instanceof VirtualDiskFlatVer2BackingInfo) {
			if(storageType != 'thin' && backing.getThinProvisioned()) {
				//gotta change it out to v2 thick - might error
				rtn.changed = true
				rtn.backing = new VirtualDiskFlatVer2BackingInfo()
				rtn.backing.setThinProvisioned(false)
			} else if(storageType == 'thin' && !backing.getThinProvisioned()) {
				rtn.changed = true
				rtn.backing = new VirtualDiskFlatVer2BackingInfo()
				rtn.backing.setThinProvisioned(true)
			}
		} else if(backing instanceof VirtualDiskSparseVer1BackingInfo || backing instanceof VirtualDiskSparseVer2BackingInfo) {
			if(storageType != 'thin') {
				//gotta change it out to v2 thick - might error
				rtn.changed = true
				rtn.backing = new VirtualDiskFlatVer2BackingInfo()
				rtn.backing.setThinProvisioned(false)
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

	static insertContainerImage(apiUrl, username, password, opts = [:]) {
		def rtn = [success: false]
		log.info("insertContainerImage: ${opts.image?.name}")
		def currentList = listTemplates(apiUrl, username, password, opts)?.templates
		log.debug("got: ${currentList}")
		def image = opts.image
		def match = currentList.find { it.name == image.name }
		if(!match) {
			def insertOpts = [zone:opts.zone, vmName:image.name, ovfFile:image.imageSrc, cloudFiles:image.cloudFiles, minDisk:image.minDisk,
							  minRam:image.minRam, imageType:image.imageType, containerType:image.containerType, hostId:opts.hostId, hostIp:opts.hostIp,
							  networkName:opts.networkName ?: '', cachePath:opts.cachePath, datastoreId:opts.datastoreId, cluster:opts.cluster,
							  networkId:opts.networkId, networkBackingType:opts.networkBackingType, resourcePool:opts.resourcePool,
							  datacenter:opts.datacenter, folder:opts.folder]
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

	static queryHostPerformance(apiUrl, username, password, opts, hostId, startTime = null, endTime = null) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def host = getManagedObject(serviceInstance, 'HostSystem', hostId)
			if(host) {
				def now = new Date()
				if(endTime == null) {
					endTime = Calendar.getInstance()
					endTime.setTime(now)
				}
				if(startTime == null) {
					startTime = Calendar.getInstance()
					startTime.setTime(new Date(now.time - (1000l * 60l * 5l)))
				}
				def performanceManager = serviceInstance.getPerformanceManager()
				def providerSummary = performanceManager.queryPerfProviderSummary(host)
				int perfInterval = providerSummary.getRefreshRate() //300 //5 minutes?
				def counterList = performanceManager.getPerfCounter()
				def queryList = counterList?.collect { it.getAssociatedCounterId() }
				def results = performanceManager.queryPerformanceCou
				/*def metricsList = performanceManager.queryAvailablePerfMetric(host, null, null, perfInterval)
				metricsList?.each {
					log.debug("metric: ${it.getInstance()} - ${it.getCounterId()}")
				}
				def currentTime = serviceInstance.currentTime()
				def querySpec = new PerfQuerySpec()
				querySpec.setEntity(host.getMOR())
				querySpec.setMetricId(metricsList)
				querySpec.setFormat('normal')
				querySpec.setMaxSample(1)
				querySpec.setIntervalId(perfInterval)
				querySpec.setStartTime(startTime)
				querySpec.setEndTime(endTime)
				def performanceMetrics = performanceManager.queryPerfComposite(querySpec)
				log.info("got: ${performanceMetrics.getEntity()}")
				def metricsEntity = performanceMetrics.getEntity()
				def metricValueList = metricsEntity.getValue()
				def metricSampleInfo = metricsEntity.getSampleInfo()
				log.info("metricSampleInfo: ${metricSampleInfo?.collect{it.getTimestamp()?.getTime().toString() + ' - ' + it.getInterval()}}")
				log.info("metricValueList: ${metricValueList?.collect{it.getId().getCounterId() + ' - ' + it.getId().getInstance() + ' - ' + it.getValue()}}")*/
				/*performanceMetrics?.getChildEntity()?.each { metric ->
					def metricValueList = metric.getValue()
					def metricSampleInfo = metric.getSampleInfo()
					log.info("metricSampleInfo: ${metricSampleInfo?.collect{it.getTimestamp()?.getTime() + ' - ' + it.getInterval()}}")
					log.info("metricValueList: ${metricValueList?.collect{it.getId().getCounterId() + ' - ' + it.getId().getInstance() + ' - ' + it.getValue()}}")
				}*/
			}
		} catch(e) {
			log.error("queryHostPerformance error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static uploadTemplate(apiUrl, username, password, opts = [:]) {

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
			def datastore = opts.datastoreId ? getDatastoreOrStoragePodDatastore(serviceInstance,opts.datastoreId,null,ComputeUtility.ONE_GIGABYTE) : getFreeDatastore(host ?: cluster, ComputeUtility.ONE_GIGABYTE)
			def resourcePool = findResourcePool(serviceInstance, opts.resourcePool, datacenter, cluster, host)
			def imageFolder = opts.folder ? getManagedObject(serviceInstance, 'Folder', opts.folder) : null
			if(resourcePool) {
				log.info("importOvf resourcePool: ${resourcePool} datastore: ${datastore}")
				//ovf url - download first
				def ovfFile = opts.ovfFile //url to ovf file
				def ovfResults = [success:false]
				def ovfRoot
				def ovfName
				CloudFile ovfCloudFile = opts.cloudFiles?.find { CloudFile cloudFile -> cloudFile.name.toLowerCase().endsWith(".ovf") }
				if(ovfCloudFile) {
					log.debug "ovfCloudFile: ${ovfCloudFile}"
					ovfResults = [success:true, ovf:ovfCloudFile?.getText()]
					ovfRoot = ovfCloudFile?.name?.substring(0, ovfCloudFile.name.lastIndexOf('/') + 1)
					ovfName = ovfCloudFile?.name?.substring(ovfCloudFile.name.lastIndexOf('/') + 1)?.replaceAll('.ovf', '')
				} else if(ovfFile) {
					log.debug "ovfFile: ${ovfFile}"
					ovfResults = downloadOvf(ovfFile)
					ovfRoot = ovfFile.substring(0, ovfFile.lastIndexOf('/') + 1)
					ovfName = ovfFile.substring(ovfFile.lastIndexOf('/') + 1)?.replaceAll('.ovf', '')
				}
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
										log.debug("importOvfImage src URL : ${srcUrl} - ${opts.cloudFiles}")
										def vmdkCloudFile = opts.cloudFiles.find { CloudFile cloudFile -> cloudFile.name == srcUrl }
										log.debug "importOvfImage vmdkCloudFile: ${vmdkCloudFile}"
										uploadImage(vmdkCloudFile, tgtUrl, leaseUpdater, opts.cachePath)
									} else {
										log.debug "importOvfImage srcUrl: ${srcUrl}"
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

	static uploadVmdkFile(boolean put, String diskFilePath, String urlStr, long bytesAlreadyWritten, long totalBytes) {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			boolean verify(String urlHostName, SSLSession session) {
				return true
			}
		})
		def conn = new URL(urlStr).openConnection()
		conn.setDoOutput(true)
		conn.setUseCaches(false)
		conn.setChunkedStreamingMode(CHUCK_LEN)
		conn.setRequestMethod(put ? "PUT" : "POST")
		conn.setRequestProperty("Connection", "Keep-Alive")
		conn.setRequestProperty("Content-Type", "application/x-vnd.vmware-streamVmdk")
		conn.setRequestProperty("Content-Length", Long.toString(new File(diskFilePath).length()))
		def bos = new BufferedOutputStream(conn.getOutputStream())
		def diskis = new BufferedInputStream(new FileInputStream(diskFilePath))
		int bytesAvailable = diskis.available()
		int bufferSize = Math.min(bytesAvailable, CHUCK_LEN)
		def buffer = new byte[bufferSize]
		long totalBytesWritten = 0
		while(true) {
			int bytesRead = diskis.read(buffer, 0, bufferSize)
			if(bytesRead == -1) {
				log.debug("Total bytes written: " + totalBytesWritten)
				break
			}
			totalBytesWritten += bytesRead
			bos.write(buffer, 0, bufferSize)
			bos.flush()
			log.debug("Total bytes written: " + totalBytesWritten)
			int progressPercent = (int) (((bytesAlreadyWritten + totalBytesWritten) * 100) / totalBytes)
			leaseUpdater.setPercent(progressPercent)
		}
		diskis.close()
		bos.flush()
		bos.close()
		conn.disconnect()
	}

	static readOvfContent(ovfFilePath) {
		def strContent = new StringBuffer()
		def inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(ovfFilePath)))
		def lineStr
		while(lineStr = inputStream.readLine() != null) {
			strContent.append(lineStr)
		}
		inputStream.close()
		return strContent.toString()
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

	static guestProcessReady(apiUrl,username,password,opts = [:]) {
		def rtn = [success:false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			//log.debug("Vmware Tools Running Status : ${vm.getGuest().getToolsRunningStatus()}")
			if(vm.getGuest().getToolsRunningStatus() == 'guestToolsRunning') {
				rtn.success = true
			}
		} catch(e) {
			log.error("guestProcessReady error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn

	}

	static executeProcess(apiUrl, username, password, opts = [:]) {
		def rtn = [success:false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def guestOpsManager = serviceInstance.getGuestOperationsManager()
			def guestProcessManager = guestOpsManager.getProcessManager(vm)
			def guestAuth = new NamePasswordAuthentication()
			guestAuth.setUsername(opts.sshUsername)
			guestAuth.setPassword(opts.sshPassword)
			def guestExecSpec = new GuestProgramSpec()
			guestExecSpec.setProgramPath(opts.program)
			guestExecSpec.setArguments(opts.arguments)
			if(opts.workingDirectory)
				guestExecSpec.setWorkingDirectory(opts.workingDirectory)
			rtn.processId = guestProcessManager.startProgramInGuest(guestAuth, guestExecSpec)
			rtn.success = rtn.processId > 0
		} catch(e) {
			log.error("executeProcess error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static getProcessStatus(apiUrl, username, password, opts) {
		def rtn = [success: false]
		def serviceInstance
		try {
			serviceInstance = connectionPool.getConnection(apiUrl, username, password)
			def rootFolder = serviceInstance.getRootFolder()
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', opts.externalId)
			def guestOpsManager = serviceInstance.getGuestOperationsManager()
			def guestProcessManager = guestOpsManager.getProcessManager(vm)
			def guestAuth = new NamePasswordAuthentication()
			guestAuth.setUsername(opts.sshUsername)
			guestAuth.setPassword(opts.sshPassword)
			def processList = [opts.processId] as long[]
			log.debug("getProcessStatus - vm: ${opts.externalId} process: ${processList}")
			rtn.processStatus = guestProcessManager.listProcessesInGuest(guestAuth, processList)
			rtn.success = true
		} catch(com.vmware.vim25.InvalidGuestLogin e) {
			log.error("getProcessStatus invalid login")
			rtn.invalidLogin = true
		} catch(e) {
			log.error("getProcessStatus error: ${e}", e)
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static addVmCustomizations(serviceInstance, apiUrl, username, password, cloneSpec, opts) {
		def rtn = [success:false, modified:false, ipAddressList:[]]
		try {
			def customSpec
			//options
			if(opts.vmToolsInstalled && (opts.customSpec || opts.networkConfig?.doCustomizations == true || opts.forceCustomizations == true)) {
				log.debug("doing customizations")
				//load the requested custom spec or create a new one
				if(opts.customSpec) {
					def customManager = serviceInstance.getCustomizationSpecManager()
					def customSpecItem = customManager.getCustomizationSpec(opts.customSpec)
					customSpec = customSpecItem.getSpec()
				} else {
					customSpec = new CustomizationSpec()
				}
				//naming and identity config
				if(opts.platform == 'linux') {
					def identitySettings = customSpec?.getIdentity() ?: new CustomizationLinuxPrep()
					def customName = new CustomizationFixedName()
					customName.setName(opts.hostname)
					identitySettings.setHostName(customName)
					identitySettings.setDomain(opts.domainName ?: 'localdomain')
					customSpec.setIdentity(identitySettings)
				} else {
					CustomizationSysprep identitySettings = customSpec.getIdentity() ?: new CustomizationSysprep()
					def guiUnattended = identitySettings.getGuiUnattended()
					if(!guiUnattended) {
						guiUnattended = new CustomizationGuiUnattended()
						if(opts.runOnce) {
							guiUnattended.setAutoLogon(true)
							guiUnattended.setAutoLogonCount(1)
						} else {
							guiUnattended.setAutoLogon(false)
							guiUnattended.setAutoLogonCount(0)
						}

						def vmTimezone = findVmwareTimezone(opts.timezone)
						log.debug("Finding Timezone: ${vmTimezone.id ?: 20}")
						guiUnattended.setTimeZone(vmTimezone.id ?: 20)
					}
					def guiPassword = guiUnattended.getPassword()
					def adminUser = opts.userConfig?.userList?.find{it.username.toLowerCase() == 'administrator'}
					if(!guiPassword && opts.userConfig.sshPassword) {
						guiPassword = new CustomizationPassword()
						guiPassword.setValue(opts.userConfig.sshPassword)
						guiPassword.setPlainText(true)
						guiUnattended.setPassword(guiPassword)
					}
					identitySettings.setGuiUnattended(guiUnattended)
					def identification = identitySettings.getIdentification() ?: new CustomizationIdentification()
					if(opts.networkConfig?.networkDomain?.domainController && !opts.networkConfig?.networkDomain?.dcServer && !opts.networkConfig?.networkDomain?.ouPath) {
						identification.setDomainAdmin(opts.networkConfig.networkDomain.domainUsername)
						identification.setJoinDomain(opts.networkConfig.networkDomain.name)
						CustomizationPassword domainPassword = new CustomizationPassword()
						domainPassword.setPlainText(true)
						domainPassword.setValue(opts.networkConfig.networkDomain.domainPassword)
						identification.setDomainAdminPassword(domainPassword)
					}
					identitySettings.setIdentification(identification)
					def userData = identitySettings.getUserData() ?: new CustomizationUserData()
					//def customName = userData.getComputerName() ?: new CustomizationFixedName()
					def customName = new CustomizationFixedName()
					customName.setName(opts.hostname?.replace('_','-'))
					userData.setComputerName(customName)
					def windowsLicense = opts.licenses?.find{lic -> lic.licenseType?.code == 'win'}
					if(windowsLicense) {
						userData.setProductId(windowsLicense.licenseKey)
						userData.setFullName(windowsLicense.fullName ?: username)
						userData.setOrgName(windowsLicense.orgName ?: username)
					} else {
						userData.setProductId('')
						userData.setFullName(username)
						userData.setOrgName(username)
					}

					identitySettings.setUserData(userData)
					customSpec.setIdentity(identitySettings)
					if(opts.runOnce) {
						// Time to split the runOnce data up as its powershell
						log.debug("Adding Agent Install Run Once Commands")
						def windowsCommand = opts.runOnce.replace('\n','\r\n') + "\r\nlogoff\r\n"
						def runOnceCommands = []
						while(windowsCommand) {
							def chunk = windowsCommand.take(20)
							def encodedLine = chunk.getBytes("UTF-16LE").encodeBase64().toString()

							windowsCommand = windowsCommand.drop(20)
							def command = "[System.IO.File]::AppendAllText('C:\\installAgent.ps1',[System.Text.Encoding]::Unicode.GetString([System.Convert]::FromBase64String('${encodedLine}')))"
							runOnceCommands <<  "c:\\windows\\system32\\windowspowershell\\v1.0\\powershell.exe \"${MorpheusUtils.escapeWindowsCommand(command)}\""

						}
						runOnceCommands << "c:\\windows\\system32\\windowspowershell\\v1.0\\powershell.exe -File C:\\installAgent.ps1"
						//runOnceCommands << "logoff"
						CustomizationGuiRunOnce guiRunOnce = new CustomizationGuiRunOnce()
						guiRunOnce.setCommandList(runOnceCommands as String[])
						identitySettings.setGuiRunOnce(guiRunOnce)
						// powershellCommands << [command: "mkdir C:\\Windows\\Setup\\Scripts -Force"]
						// powershellCommands << [command: "Add-Content C:\\Windows\\Setup\\Scripts\\SetupComplete.cmd 'powershell -File C:\\installAgent.ps1'"]
					}
				}
				//network config
				if(opts.networkConfig?.doCustomizations == true || opts.forceCustomizations == true) {
					log.debug("setting network config: ${opts.networkConfig}")
					//primary interface
					def globalIpSettings = customSpec.getGlobalIPSettings() ?: new CustomizationGlobalIPSettings()
					def dnsServers = opts.networkConfig.primaryInterface.dnsServers?.tokenize(', ') ?: []
					opts.networkConfig.extraInterfaces?.each { networkInterface ->
						if(networkInterface.dnsServers) {
							dnsServers += networkInterface.dnsServers.tokenize(', ')
						}
					}
					dnsServers = dnsServers?.flatten()?.findAll {dnsServer -> dnsServer}
					if(dnsServers) {
						globalIpSettings.setDnsServerList(dnsServers as String[])
					}
					customSpec.setGlobalIPSettings(globalIpSettings)
					def currentAdapterMappings = customSpec.getNicSettingMap()
					def adapterMappings = []
					if(opts.networkConfig.primaryInterface) {
						def networkResults = addVmNetworkCustomizations(apiUrl, username, password, currentAdapterMappings, opts.networkConfig.primaryInterface, 0, opts)
						if(networkResults.success == true) {
							adapterMappings << networkResults.adapterMapping
							if(networkResults.ipAddress)
								rtn.ipAddressList << [index:0, ipAddress:networkResults.ipAddress]
						}
					}
					opts.networkConfig.extraInterfaces?.eachWithIndex { networkInterface, index ->
						def networkResults = addVmNetworkCustomizations(apiUrl, username, password, currentAdapterMappings, networkInterface, index + 1, opts)
						if(networkResults.success == true) {
							adapterMappings << networkResults.adapterMapping
							if(networkResults.ipAddress)
								rtn.ipAddressList << [index:index + 1, ipAddress:networkResults.ipAddress]
						}
					}
					customSpec.setNicSettingMap(adapterMappings as CustomizationAdapterMapping[])
					rtn.modified = true
					rtn.success = true
				}
			}
			if(customSpec) {
				rtn.modified = true
				cloneSpec.setCustomization(customSpec)
			}
		} catch(e) {
			log.error("addVmCustomizations error ${e}", e)
			throw e
		}
		return rtn
	}

	static addVmNetworkCustomizations(apiUrl, username, password, adapterMappings, networkConfig, index, opts) {
		def rtn = [success:false]
		def adapterMapping = adapterMappings?.size() > index ? adapterMappings[index] : new CustomizationAdapterMapping()
		def customIPSettings = adapterMapping.getAdapter() ?: new CustomizationIPSettings()
		if(networkConfig.doDhcp == true) {
			def dhcpIp = new CustomizationDhcpIpGenerator()
			customIPSettings.setIp(dhcpIp)
			adapterMapping.setAdapter(customIPSettings)
		} else if(networkConfig.doStatic == true && networkConfig.ipAddress) {
			if(networkConfig.gateway)
				customIPSettings.setGateway([networkConfig.gateway] as String[])
			if(networkConfig.netmask)
				customIPSettings.setSubnetMask(networkConfig.netmask)
			if(networkConfig.dnsServers) {
				def dnsServerList = networkConfig.dnsServers.tokenize(' ,')
				dnsServerList = dnsServerList?.flatten()?.findAll {dnsServer -> dnsServer}
				if(dnsServerList?.size() > 0)
					customIPSettings.setDnsServerList(dnsServerList as String[])
			}
			def fixedIp = new CustomizationFixedIp()
			fixedIp.setIpAddress(networkConfig.ipAddress)
			customIPSettings.setIp(fixedIp)
			adapterMapping.setAdapter(customIPSettings)
		} else if(networkConfig.doPool && networkConfig.networkPool) { //todo - trigger for customizations
			if(networkConfig.dnsServers?.size() > 0) {
				def dnsServerList = networkConfig.networkPool.dnsServers.tokenize(' ,')
				if(dnsServerList?.size() > 0)
					customIPSettings.setDnsServerList(dnsServerList as String[])
			}
			if(networkConfig.gateway)
				customIPSettings.setGateway([networkConfig.gateway] as String[])
			if(networkConfig.dnsDomain)
				customIPSettings.setDnsDomain(networkConfig.dnsDomain)
			if(networkConfig.networkPool.netmask)
				customIPSettings.setSubnetMask(networkConfig.netmask)
			if(networkConfig.dnsServers) {
				def dnsServerList = networkConfig.dnsServers.tokenize(' ,')
				dnsServerList = dnsServerList?.flatten()?.findAll {dnsServer -> dnsServer}
				if(dnsServerList?.size() > 0)
					customIPSettings.setDnsServerList(dnsServerList as String[])
			}
			def ipResults = allocateIpv4Address(apiUrl, username, password, networkConfig.networkPool.externalId?.toInteger(), "morpheus${opts.server.id}", opts)
			if(ipResults.success == true) {
				rtn.ipAddress = ipResults.ipAddress
				rtn.networkPoolId = networkConfig.networkPool.id
				def fixedIp = new CustomizationFixedIp()
				fixedIp.setIpAddress(ipResults.ipAddress)
				customIPSettings.setIp(fixedIp)
			} else {
				throw new Exception('failed to allocate ip address from pool')
			}
		}
		adapterMapping.setAdapter(customIPSettings)
		rtn.adapterMapping = adapterMapping
		rtn.success = true
		return rtn
	}

	static allocateIpv4Address(apiUrl, username, password, poolId, allocationId, opts) {
		def rtn = [success: false]
		def serviceInstance
		try {
			synchronized(vmwareMutex) {
				try {
					def proxyUrl = 'https://sdkTunnel:8089/sdk'
					def newUrl = apiUrl.replaceAll('https://', '')
					newUrl = newUrl.replaceAll('/sdk', '')
					log.debug("proxyTo: ${newUrl}")
					System.setProperty("proxySet", 'true')
					System.setProperty("proxyHost", newUrl)
					System.setProperty("proxyPort", '80')
					serviceInstance = connectionPool.getConnection(proxyUrl, username, password)
					def serviceContent = serviceInstance.retrieveServiceContent()
					def sessionManager = serviceInstance.getSessionManager()
					def rootFolder = serviceInstance.getRootFolder()
					def datacenter = opts.datacenter ? new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter) : null
					sessionManager.logout()
					serviceInstance.getServerConnection().getVimService().getWsc().cookie = null
					log.debug("logging in cert")
					sessionManager.loginExtensionByCertificate(morpheusExtensionId, 'en')
					if(datacenter) {
						def ipPoolManagerRef = serviceContent.getIpPoolManager()
						def ipPoolManager = ipPoolManagerRef ? getManagedObject(serviceInstance, 'IpPoolManager', ipPoolManagerRef.getVal()) : null
						def ipAddress = ipPoolManager.allocateIpv4Address(datacenter, poolId, allocationId)
						log.info("allocate ipAddress: ${ipAddress}")
						if(ipAddress) {
							rtn.success = true
							rtn.ipAddress = ipAddress
						}
					} else {
						rtn.msg = 'no datacenter specified'
					}
				} catch(e2) {
					log.error("proxy error: ${e2}", e2)
				} finally {
					System.setProperty("proxySet", "false")
					System.clearProperty("proxyHost")
					System.clearProperty("proxyPort")
				}
			}
		} catch(e) {
			log.error("allocateIpv4Address error ${e}", e)
			throw e
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}

		return rtn
	}

	static releaseIpv4Address(apiUrl, username, password, poolId, allocationId, opts) {
		def rtn = [success: false]
		def serviceInstance
		try {
			synchronized(vmwareMutex) {
				try {
					def proxyUrl = 'https://sdkTunnel:8089/sdk'
					def newUrl = apiUrl.replaceAll('https://', '')
					newUrl = newUrl.replaceAll('/sdk', '')
					log.debug("proxyTo: ${newUrl}")
					System.setProperty("proxySet", 'true')
					System.setProperty("proxyHost", newUrl)
					System.setProperty("proxyPort", '80')
					serviceInstance = connectionPool.getConnection(proxyUrl, username, password)
					def serviceContent = serviceInstance.retrieveServiceContent()
					def sessionManager = serviceInstance.getSessionManager()
					def rootFolder = serviceInstance.getRootFolder()
					def datacenter = opts.datacenter ? new InventoryNavigator(rootFolder).searchManagedEntity('Datacenter', opts.datacenter) : null
					sessionManager.logout()
					serviceInstance.getServerConnection().getVimService().getWsc().cookie = null
					log.debug("logging in cert")
					sessionManager.loginExtensionByCertificate(morpheusExtensionId, 'en')
					if(datacenter) {
						def ipPoolManagerRef = serviceContent.getIpPoolManager()
						def ipPoolManager = ipPoolManagerRef ? getManagedObject(serviceInstance, 'IpPoolManager', ipPoolManagerRef.getVal()) : null
						ipPoolManager.releaseIpAllocation(datacenter, poolId, allocationId)
						rtn.success = true
					} else {
						rtn.msg = 'no datacenter specified'
					}
				} catch(e2) {
					log.error("proxy error: ${e2}", e2)
				} finally {
					System.setProperty("proxySet", "false")
					System.clearProperty("proxyHost")
					System.clearProperty("proxyPort")
				}
			}
		} catch(e) {
			log.error("releaseIpv4Address error ${e}", e)
			throw e
		} finally {
			if(serviceInstance) {connectionPool.releaseConnection(apiUrl,username,password, serviceInstance)}
		}
		return rtn
	}


	static uploadImage(CloudFile cloudFile, String tgtUrl, leaseUpdator, cachePath = null) {
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
			rtn = uploadImage(sourceStream, totalCount, tgtUrl, leaseUpdator)
		} catch(ex) {
			log.error("uploadImage cloudFile error: ${ex}", ex)
		} finally {
			try {
				sourceStream?.close()
			} catch(e) {
			}
		}
	}

	static uploadImage(String srcUrl, String tgtUrl, leaseUpdater, cachePath = null) {
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
			rtn = uploadImage(sourceStream, totalCount, tgtUrl, leaseUpdater)
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

	static uploadImage(InputStream sourceStream, Long contentLength, String tgtUrl, leaseUpdater) {
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
			outboundClient = clientBuilder.build()
			def outboundPut = new HttpPost(tgtUrl)
			def vmInputStream = new VmwareLeaseProgressInputStream(new BufferedInputStream(sourceStream, 1200), leaseUpdater, contentLength)
			def inputEntity = new InputStreamEntity(vmInputStream, contentLength)
			outboundPut.addHeader('Connection', 'Keep-Alive')
			outboundPut.addHeader('Content-Type', 'application/x-vnd.vmware-streamVmdk')
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

	static fileExists(username,password,token,tgtUrl) {
		log.info("fileExists tgt: ${tgtUrl}")
		def outboundClient
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
			outboundClient = clientBuilder.build()
			def outboundPut = new HttpGet(tgtUrl)
			outboundPut.addHeader('Connection', 'Keep-Alive')
			outboundPut.addHeader('Content-Type', 'application/octet-stream')
			outboundPut.addHeader('Authorization', 'Basic ' + (username + ':' + password).bytes.encodeBase64().toString())
			outboundPut.addHeader('Cookie', 'vmware_cgi_ticket=' + token)

			def responseBody = outboundClient.execute(outboundPut)
			log.debug("Checking file Existence: ${responseBody.statusLine.statusCode}")
			return responseBody.statusLine.statusCode < 400
		} catch(e) {
			log.error("fileExists error: ${e}", e)
			return false
		} finally {
			outboundClient.close()
		}
	}

	static uploadFile(username, password, token, srcStream, contentLength, tgtUrl) {
		log.info("uploadFile tgt: ${tgtUrl}")
		def rtn = [success: false]
		def outboundClient
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
			outboundClient = clientBuilder.build()
			def outboundPut = new HttpPut(tgtUrl)
			def inputEntity = new InputStreamEntity(srcStream, contentLength)
			outboundPut.addHeader('Connection', 'Keep-Alive')
			outboundPut.addHeader('Content-Type', 'application/octet-stream')
			outboundPut.addHeader('Authorization', 'Basic ' + (username + ':' + password).bytes.encodeBase64().toString())
			outboundPut.addHeader('Cookie', 'vmware_cgi_ticket=' + token)
			outboundPut.setEntity(inputEntity)
			def responseBody = outboundClient.execute(outboundPut)
			log.debug("got: ${responseBody}")
			rtn.success = true
		} catch(e) {
			log.error("uploadFile error: ${e}", e)
		} finally {
			outboundClient.close()
		}
		return rtn
	}

	static downloadImage(String srcUrl, OutputStream targetStream, String token, leaseUpdater, int totalFiles=1, int currentFile=0) {
		log.info("downloadImage: src: ${srcUrl}")
		def inboundClient
		def rtn = [success: false]
		try {
			def inboundSslBuilder = new SSLContextBuilder()
			inboundSslBuilder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					return true
				}
			})
			def inboundSocketFactory = new SSLConnectionSocketFactory(inboundSslBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
			def clientBuilder = HttpClients.custom().setSSLSocketFactory(inboundSocketFactory)
			clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
				boolean verify(String host, SSLSession sess) { return true }

				void verify(String host, SSLSocket ssl) {}

				void verify(String host, String[] cns, String[] subjectAlts) {}

				void verify(String host, X509Certificate cert) {}
			})
			inboundClient = clientBuilder.build()
			def inboundGet = new HttpGet(srcUrl)
			inboundGet.addHeader('Cookie', 'vmware_cgi_ticket=' + token)
			def responseBody = inboundClient.execute(inboundGet)
			rtn.contentLength = responseBody.getEntity().getContentLength()
			log.info("download image contentLength: ${rtn.contentLength}")
			def vmInputStream = new VmwareLeaseProgressInputStream(new BufferedInputStream(responseBody.getEntity().getContent(), 16*1024), leaseUpdater, 0,totalFiles,currentFile)
			BufferedOutputStream outStream = new BufferedOutputStream(targetStream,16*1024)
			writeStreamToOut(vmInputStream,outStream)
			// targetStream << vmInputStream
			log.info("Image File ${srcUrl} Download Complete...Flushing")
			outStream.flush()
			// targetStream.flush()
			// outStream.close()

			rtn.success = true
		} catch(e) {
			log.error("downloadImage From Stream error: ${e}", e)
		} finally {
			inboundClient.close()
		}
		return rtn
	}


	@CompileStatic
	static void writeStreamToOut(InputStream inputStream, OutputStream out) {
		byte[] buffer = new byte[102400]
		int len
		while((len = inputStream.read(buffer)) != -1) {
			out.write(buffer, 0, len)
		}
	}


	static archiveImage(String srcUrl, CloudFile targetFile, String token, leaseUpdater, fileSize = 0, Closure progressCallback = null, Integer totalFiles=1, Integer currentFile=0) {
		log.info("downloadImage: src: ${srcUrl}")
		def inboundClient
		def rtn = [success: false]
		try {
			def inboundSslBuilder = new SSLContextBuilder()
			inboundSslBuilder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				boolean isTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
					return true
				}
			})
			def inboundSocketFactory = new SSLConnectionSocketFactory(inboundSslBuilder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
			def clientBuilder = HttpClients.custom().setSSLSocketFactory(inboundSocketFactory)
			clientBuilder.setHostnameVerifier(new X509HostnameVerifier() {
				boolean verify(String host, SSLSession sess) { return true }

				void verify(String host, SSLSocket ssl) {}

				void verify(String host, String[] cns, String[] subjectAlts) {}

				void verify(String host, X509Certificate cert) {}
			})
			inboundClient = clientBuilder.build()
			def inboundGet = new HttpGet(srcUrl)
			inboundGet.addHeader('Cookie', 'vmware_cgi_ticket=' + token)
			def responseBody = inboundClient.execute(inboundGet)
			rtn.contentLength = responseBody.getEntity().getContentLength()
			if(rtn.contentLength < 0 && fileSize > 0)
				rtn.contentLength = fileSize
			log.debug("download image contentLength: ${rtn.contentLength}")
			def vmInputStream = new ProgressInputStream(new VmwareLeaseProgressInputStream(new BufferedInputStream(responseBody.getEntity().getContent(), 1200), leaseUpdater, rtn.contentLength, totalFiles, currentFile), rtn.contentLength)
			vmInputStream.progressCallback = progressCallback
			// targetFile.setContentLength(rtn.contentLength)
			targetFile.setInputStream(vmInputStream)
			targetFile.save()
			rtn.success = true
		} catch(e) {
			log.error("downloadImage From Stream error: ${e}", e)
		} finally {
			inboundClient.close()
		}
		return rtn
	}

	static getIDEController(VirtualMachine vm) throws Exception {
		def rtn
		def vmConfig = vm.getConfig()
		def virtualDevices = vmConfig.getHardware().getDevice()
		virtualDevices.each { device ->
			if(rtn == null) {
				if(device instanceof VirtualIDEController)
					rtn = device
			}
		}
		return rtn
	}

	static getDefaultDevices(VirtualMachine vm) throws Exception {
		def rtn
		def vmRuntimeInfo = vm.getRuntime()
		def envBrowser = vm.getEnvironmentBrowser()
		def hmor = vmRuntimeInfo.getHost()
		def cfgOpt = envBrowser.queryConfigOption(null, new HostSystem(vm.getServerConnection(), hmor))
		if(cfgOpt != null) {
			rtn = cfgOpt.getDefaultDevice()
			if(rtn == null)
				throw new Exception("No Datastore found in ComputeResource")
		} else {
			throw new Exception("No VirtualHardwareInfo found in ComputeResource")
		}
		return rtn
	}

	static logFaultError(message, fault) {
		def rtn = buildFaultMessage(message, fault)
		log.error("${rtn}: ${fault}", fault)
		return rtn
	}

	static buildFaultMessage(message, fault) {
		def rtn = message
		try {
			def messages = fault.getFaultMessage()
			messages?.each { msg ->
				def localMsg = msg.getMessage()
				if(localMsg)
					rtn = rtn + ' - ' + localMsg
			}
		} catch(e) {
			log.error("buildFaultMessage error: ${e}", e)
		}
		return rtn
	}

	static hasVmwareDatastoreCluster(zoneId) {
		def rtn = false
		def tmpZone = zoneId ? ComputeZone.read(zoneId) : null
		if(tmpZone?.zoneType?.code == 'vmware' || tmpZone?.zoneType?.code == 'macstadium') {
			def tmpList = Datastore.withCriteria {
				eq('zone',tmpZone)
				eq('type','cluster')
				or {
					eq('visibility','public')
					owner {
						eq('id', tmpZone.account.id)
					}
					account {
						eq('id', tmpZone.account.id)
					}
				}
				maxResults(1)
			}
			rtn = tmpList?.size() > 0
		}
		return rtn
	}

	static setNetworkCreateOpts(virtualImage, createOpts, config) {
		def networkType = config.networkType
		createOpts.networkType = 'default'
		if(networkType && networkType != 'default') {
			if(networkType == 'dhcp') {
				if(virtualImage.isCloudInit != true || createOpts.platform == 'windows') {
					createOpts.networkType = networkType
				}
				//else?
			} else if(networkType == 'static') {
				if(virtualImage.isCloudInit != true || createOpts.platform == 'windows') {
					createOpts.networkType = networkType
					createOpts.ipAddress = config.ipAddress
					createOpts.gateway = config.gateway
					createOpts.netmask = config.netmask
					createOpts.dnsServers = config.dnsServers
				}
			} else {
				def networkPool = NetworkPool.read(networkType.toLong())
				createOpts.networkType = networkType
				createOpts.networkPool = networkPool
			}
		}
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

	static connectVmNetworks(Map authConfig, String vmId) {
		def rtn = [success: false]
		try {
			def serviceInstance = getServiceInstance(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
			def vm = getManagedObject(serviceInstance, 'VirtualMachine', vmId)
			def deviceChangeList = []
			def vmConfigSpec = new VirtualMachineConfigSpec()
			def vmConfig = vm.getConfig()
			def virtualDevices = vmConfig.getHardware().getDevice()
			def nics = virtualDevices.findAll { it instanceof VirtualEthernetCard }
			nics.each { nic ->
				if(!nic.connectable.connected) {
					def connectable = new VirtualDeviceConnectInfo()
					connectable.setStartConnected(true)
					connectable.setConnected(true)
					nic.setConnectable(connectable)
					def nicSpec = new VirtualDeviceConfigSpec()
					nicSpec.setOperation(VirtualDeviceConfigSpecOperation.edit)
					nicSpec.setDevice(nic)
					deviceChangeList << nicSpec
				}
			}

			if(deviceChangeList.size() > 0) {
				def deviceChanges = deviceChangeList as VirtualDeviceConfigSpec[]
				vmConfigSpec.setDeviceChange(deviceChanges)
				def vmTask = vm.reconfigVM_Task(vmConfigSpec)
				def vmResult = retryWaitForTask(vmTask)
				if(vmResult == Task.SUCCESS) {
					rtn.success = true
				}
			} else {
				rtn.success = true
			}
		} catch(e) {
			log.error("connectVmNetworks error: ${e}", e)
		}

		return rtn
	}


	static relocateStorage(Map authConfig, String vmId, String targetDatastoreId) {
		def rtn = [success: false]
		try {
			def serviceInstance = getServiceInstance(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)

			def vm = getManagedObject(serviceInstance, 'VirtualMachine', vmId)
			def datastore = getManagedObject(serviceInstance, 'Datastore', targetDatastoreId)

			def relocateSpec = new VirtualMachineRelocateSpec()
			relocateSpec.setDatastore(datastore.getMOR())
			def task = vm.relocateVM_Task(relocateSpec, VirtualMachineMovePriority.highPriority)

			// rtn = retryWaitForTask(task)
		} catch (e) {
			log.error("relocateStorage error: ${e}", e)
			rtn.success = false
		}

		return rtn
	}

	static retryWaitForTask(vmTask) {
		def attempts = 0
		def results = null
		while(results == null && attempts < 20) {
			try {
				results = vmTask.waitForTask(1000, 5000)
			} catch(MethodFault ex) {
				log.error("Error detected waiting for task! ${ex}",ex)
				results = logFaultError('error executing vmtask', ex)
				break
			} catch(e) {
				log.warn("error waiting for vmtask: ${e}", e)
				sleep(3000)
			}
			attempts++
		}
		return results
	}

	static isEntityReadOnly(roleList) {
		def rtn = true
		roleList?.each { roleId ->
			if(roleId == -1 || roleId > 2 || roleId < -5)
				rtn = false
		}
		return rtn
	}

	static getVmwareHostUrl(node) {
		def sshHost = node.sshHost
		if(sshHost) {
			return 'https://' + sshHost + '/sdk'
		}
		throw new Exception('no esxi apiUrl specified')
	}

	static getVmwareApiUrl(zone) {
		def config = zone.getConfigMap()
		if(config?.apiUrl)
			return config.apiUrl
		throw new Exception('no vmware apiUrl specified')
	}

	static getVmwareUsername(zone) {
		def config = zone.getConfigMap()
		if(config?.username)
			return config.username
		throw new Exception('no vmware username specified')
	}

	static getVmwarePassword(zone) {
		def config = zone.getConfigMap()
		if(config?.password)
			return config.password
		throw new Exception('no vmware password specified')
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

	static printOvfFileItem(OvfFileItem fi) {
		log.debug("================ OvfFileItem ================")
		log.debug("chunkSize: " + fi.getChunkSize())
		log.debug("create: " + fi.isCreate())
		log.debug("deviceId: " + fi.getDeviceId())
		log.debug("path: " + fi.getPath())
		log.debug("size: " + fi.getSize())
		log.debug("==============================================")
	}

	static extensionCert = '''-----BEGIN CERTIFICATE-----
MIIGDzCCA/egAwIBAgIJANC+8rlLkPsrMA0GCSqGSIb3DQEBBQUAMGIxCzAJBgNV
BAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRIwEAYDVQQHEwlTYW4gTWF0ZW8x
FTATBgNVBAoTDFRlc3QgQWNjb3VudDETMBEGA1UEAxMKdGVzdC5sb2NhbDAeFw0x
NTA5MjAxNzI0NTZaFw0xODA5MTkxNzI0NTZaMGIxCzAJBgNVBAYTAlVTMRMwEQYD
VQQIEwpDYWxpZm9ybmlhMRIwEAYDVQQHEwlTYW4gTWF0ZW8xFTATBgNVBAoTDFRl
c3QgQWNjb3VudDETMBEGA1UEAxMKdGVzdC5sb2NhbDCCAiIwDQYJKoZIhvcNAQEB
BQADggIPADCCAgoCggIBALXXj0DSDU3gWJR9SjKgmhc2XBaDi1frqflhkHPNGakG
UrkxJTaFeY6DCky/69ao1USCVLv8uJEqFU+DEeKaBpa2MTaNRbt6Y1ZYEitojxZv
v+7sEqGdcNMb/N0ymz/Pk49G70/yJ+nAMyulpQsY0TaBG1Mrhk0ZJIM6hrLCAHZN
DRJ1LNR3jyzIgp70QF/oMsx6jDVq7SHgW+ZRVnPmt58Vl5b/OgubisCw45GEOIAv
3SejiPg6HOi2/LNTdWbejrXwR6U6jkI/CocFTfkwLucErJGpvJ+fnLdbZlq3zAn1
5WeqaH0IRxPlT5mu9lNpAgO2TfjpkSifv/q12D/Tu+1h7RURghsmQOb0JUNKWmuW
ZfEjm7JZrPhbuBI5lA941Y3YZz2npgfgxMadU+vyfparT3LRszT+749kbgZyLdNC
WdOHbNsKzenA2sJBND9mUH/0lfLw9SU2KPbPmAu5x7ks/goar45KEBOMBGhzmgku
qsCT9lDTE4jF9hTqjiZ/DVHt2A+6hQKG0chIoyJNp+fB7lDlGug2spj6WKy3vxKe
KRvc4BycIm71ICgquRTH0MnDW2XjDt6XzZsusxxEOU1eCZpD6KttgVxUtIbCJAVz
5GyR6idIRusk7MJmLsUQuuaIKGUf6CiE3/YBFQ5yFUudh1FVh5b8ErfgLOg2l/Pb
AgMBAAGjgccwgcQwHQYDVR0OBBYEFJ4Tl0pUb28fvn09rk5H9Vebbl85MIGUBgNV
HSMEgYwwgYmAFJ4Tl0pUb28fvn09rk5H9Vebbl85oWakZDBiMQswCQYDVQQGEwJV
UzETMBEGA1UECBMKQ2FsaWZvcm5pYTESMBAGA1UEBxMJU2FuIE1hdGVvMRUwEwYD
VQQKEwxUZXN0IEFjY291bnQxEzARBgNVBAMTCnRlc3QubG9jYWyCCQDQvvK5S5D7
KzAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4ICAQBQz8CRPNWjErAhUeoa
q2CqzSQkUYOyJck4mpiFXUx3sIcUk/wqN1L3pLOK4BW3dnfFqhCaA+K0AVC11yAF
YYWPeoIvHKPx/y3RLTu49F0vXNukuCTFOBBcUBWddRvYD+kwFM7iX5WlMrc7n3oN
wU76F1PddxjBzFcGj6DJ9yXC6LC2ZH4b2rQBOT/q2wBdXFzqA4cQN980t+JLL8d2
tyTcd7FLzJVTl4ZKRTBhehEi+u+NrhwtVzAMEO2kE8onDyJlE0/V0g1r4/nf+GZJ
YyIsOyukbmdmxtrQ8MkT5laTJeBmEA/yRslSja0bS2DUtFxjdc4W/v7Xkn36tIsS
A3dzTAsyvtnM2PrTSpBjdrbJTlPuAa9DK+m1Zwel4fm4ieS1PettJ5UVrcVs/5cL
1MNr7+etkHM/wPgDTLaO5TY6HLf0OkCenZM7jpvh2P/Zx3xt4mHGkgCOe2ApDu32
JEO1BvcTEHRfK/ej1cmkdqRo5zSYNadvHh8Bg+j7RtLtuxkiWKS93dkFtFpFB0aH
/X3JLJbLz20AyI5DvncKrcbwoMn/cTZC1O8hmkk9iuGZxKibirQvWz9WUlZfnu8G
x5LvuTIPOwCr6J5LS2T9qngLZ4GuA1Pg+ZT+UlvyFdgXP4Mi2uqYNQBYAFSJJztG
Isv0PXT8ssXRW4Ln6+7yxh3IJw==
-----END CERTIFICATE-----
'''

	static extensionKey = '''-----BEGIN RSA PRIVATE KEY-----
MIIJKAIBAAKCAgEAtdePQNINTeBYlH1KMqCaFzZcFoOLV+up+WGQc80ZqQZSuTEl
NoV5joMKTL/r1qjVRIJUu/y4kSoVT4MR4poGlrYxNo1Fu3pjVlgSK2iPFm+/7uwS
oZ1w0xv83TKbP8+Tj0bvT/In6cAzK6WlCxjRNoEbUyuGTRkkgzqGssIAdk0NEnUs
1HePLMiCnvRAX+gyzHqMNWrtIeBb5lFWc+a3nxWXlv86C5uKwLDjkYQ4gC/dJ6OI
+Doc6Lb8s1N1Zt6OtfBHpTqOQj8KhwVN+TAu5wSskam8n5+ct1tmWrfMCfXlZ6po
fQhHE+VPma72U2kCA7ZN+OmRKJ+/+rXYP9O77WHtFRGCGyZA5vQlQ0paa5Zl8SOb
slms+Fu4EjmUD3jVjdhnPaemB+DExp1T6/J+lqtPctGzNP7vj2RuBnIt00JZ04ds
2wrN6cDawkE0P2ZQf/SV8vD1JTYo9s+YC7nHuSz+ChqvjkoQE4wEaHOaCS6qwJP2
UNMTiMX2FOqOJn8NUe3YD7qFAobRyEijIk2n58HuUOUa6DaymPpYrLe/Ep4pG9zg
HJwibvUgKCq5FMfQycNbZeMO3pfNmy6zHEQ5TV4JmkPoq22BXFS0hsIkBXPkbJHq
J0hG6yTswmYuxRC65ogoZR/oKITf9gEVDnIVS52HUVWHlvwSt+As6DaX89sCAwEA
AQKCAgAb4easWMkDdPAALMnww2F9R6+NIDSAkbkpbQVaTEuv+DE9BkUffA3shTlS
FWeTLaYt8BIQFSszzxEfdQiMB3QVLfCT3cugeJ68wZpfn5O5K/9xIi+lKl//bt4y
CK13dlu473aIeqgWFouCGM8ayX5i7h8DfLwGHGHV4WaACHFLsnrbpq3uEjsY8dHd
j7RDQHZxliTjl/auaBmPUk+ATBNByGzLEW2UFBCNhVIPoQeM2En/5vp8nWSLZmuP
D7ADywJ08Vf7BOSFUedKVszi9blrYPMZDr7GdxHvp4slQ+N8b4DyiVWmPluvuEyh
UwugNIzDhvOjxCSm5v/n8HQtLUk2Vc/s5U3VNKVARyA/xP+gOPn7pCdGcKBM652s
z5g7TC/k6ilK2IpwN3wX+l3FrlAYuguXdfyuJ0TUSa4y/pGZlKkkFv7/1jdEIM1f
GA6p3gLYV8xO4p/kR1+dG3oTVv9526Fe5mZDPVkUvgzfpFv1db/Tdb7jmujsvS0B
pRsPTquiavIczaEFRKloS5TR9YRosE0MyfdtXwtLHoIOnlkc9myVgrKb3puCvsLz
904m9d3Re89r+7L6toPSrIKPM/355KbqWNoBcxtlnIojrwW2wAQtANLaenHNsoLn
aG8Meel4Cd4i2tVOFBkLUWOrjFZU9bKLXO3uNaAwguF7eTgG2QKCAQEA2CBwfQSS
vQ8hP4SZY414aTpcSOnw7xTzhaLimdBYQ4Gl2m+a5XjBM346fCSBLeVdLRJEp8Hd
+Dc9UNGEHBkyDb+5Yc6wsKutHdQIZIIhzx2UVl9zcY0VjkGzjS+uR2PoA2eJJrkz
xILFuIl18CthAUhIme+Yy2LD2DdXj5gVFj+3uOPe8Qa8vODZ3C9JQSXtaQazbjoG
GEJai5JudR4sRRWkoeJzP/vdPp7BmbYgxEhhjAfDxrIF6ZL0eu4StBihupWPvxQP
C5yhW6BDrxKGIDLxQWhPFMnth40gPc0oqE0Xv+8x1QIM2frWHV+L/WRqswLUQdPj
/FhexmIh0VcptQKCAQEA12Pe9ponai0jRCqw2Zgo42OPZixNLPna6ACKTw7ewrXH
e/zbphiSKWh+dpWYxiH5a2EWYXmaYkc43mGeanIRnuq8xuYckIVGi3/9kG3lHtYH
ZSHE8di9hGTKyPZ+5NQ+HfcknIB9OpIDva7srHUbVR5eQfWQrQ/TePKpono/hrzp
9G/uDHjS09Vx2a/nj6UAQ9FmASb09WGKYRb4hj8XuwokvBINf1J6ujUB7tVllOZZ
wXSadxSqLQFg0LqgFOzU12BmVlrU9brUtFp9ymctGZYYa30zSvGWQQLAxTHfbyFb
prgZaRu8TcExqP0yxPDW08FfaWsd7fYpBv3X+yLhTwKCAQBkt7FUQWZUnpFi9Vs6
Dnp3p0/MVuEFuaDGvSKWhYYsZ2WwBuSLXZvDcGa25ZghmEq0r1e5Sdo0SyHS4OCM
65zs1VUB5H1ox4hUZzRGCnLQ/zraiX7WAuZxteKDwB2afU9pDr3WguZCBKM5nS2N
79kz7MiQnBx7Y5paz2eJrljdVnI6wk5pyjb4WlyMhKjBqHgbwpipnHaspXdmpaW+
BIKi6zQekNYekjQHc5pLFJnufsCerhupI9snf2lZREsJDJWJyxsC88kTIzn65hPE
qYBBzxuo3IBF8tgznCvTQSFaVRrlQgXZ+yucBY30gEtPmZUzuPTLfIA5X8FC+AZE
dTclAoIBAC/tejSXcsPOjXVipn2UvSxz//m1S+sBIfPUAvK6lVjwSBZP2rRn7UYn
iVRrz1XvQ2fgb7F69J5/yKi7QhvfjAo3o5ACrkJ+VYc/ELYtrfkhmP2o+QDzwKAQ
KYt0RiI3FCdo0g29DaCV0x1HKkTpvg4/tquor+vChMT6AkM0PqSFru2EZo0B6a/X
ZAETgkxVY+yzUKZa+9EKRld6XAzLR0WIaUosHjLBsKt0lhyGAZeTdk902dY7k+51
ywxHKs3dMTNHsR3wEkknrKMBK9J5CPETlBV/A4XeohePPEPk6Bn7a0B64LShxlHq
9ljrFnv6N2NhORDlFADacKCspX2tckMCggEBAKL/NSjq5wSy+a7NxkVVlqBEth6D
kgjmrf7lBuBS+VyjWVe+O31Kbuzr1S77OVYSeQDK8ycm4s8HIzudtcwkWD3bCAAU
sMinnOgD6oJ2We9PTrsE/Yv68kbNlCyF4AZ9QqnPaLlKvGv3xjYrAwWuRsQWZCkG
7C+6WBAjIEIxMY5iYRAYmw6I5mBsBnj/On4vQ9jVMJIzqBQJa1yfkcZJu8XSGEHl
g3n4vcv75qPGCZA4l1YpqZMkUMyyG/nQnrOiK5+fkSP7W0Pf/xMzZ8SUTZC3OER2
72RmW7kBnF5y0KhPH6QazWGjmnSE3AJ/suHDnTmPcnmdzwxOeq45pb1VHuM=
-----END RSA PRIVATE KEY-----
'''

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
			case 'centosGuest':
				return 'cent'
			case 'darwin10_64Guest':
			case 'darwin10Guest':
			case 'darwin11_64Guest':
			case 'darwin11Guest':
			case 'darwin12_64Guest':
			case 'darwin13_64Guest':
			case 'darwin14_64Guest':
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
				return 'other'
		}
		return 'other'
	}

	static findVmwareTimezone(javaId) {
		def rtn
		if(!javaId) {
			javaId = TimeZone.getDefault().getID()
		}
		if(javaId) {
			def javaTimezone = TimeZone.getTimeZone(javaId)
			def name = javaId
			if(javaId?.indexOf('/') > -1)
				name = name.substring(javaId.indexOf('/') + 1)
			name = name.toLowerCase()
			def match = vmwareTimezoneList.find{ it.name.toLowerCase().indexOf(name) > -1 }
			if(!match)
				match = vmwareTimezoneList.find{ it.description.toLowerCase().indexOf(name) > -1 }
			if(!match) {
				def rawOffset = (double) (javaTimezone.getRawOffset() / (1000d * 60d * 60d))
				match = vmwareTimezoneList.find{ it.rawOffset == rawOffset }
			}
			if(match)
				rtn = match
		}
		if(!rtn)
			rtn = vmwareTimezoneList.find{it.id == 85}
		return rtn
	}


	static vmwareTimezoneList = [
			[id:0, stringId:'000', name:'Dateline Standard Time', description:'(GMT-12:00) International Date Line West', rawOffset:-12d],
			[id:1, stringId:'001', name:'Samoa Standard Time', description:'(GMT-11:00) Midway Island, Samoa', rawOffset:-11d],
			[id:2, stringId:'002', name:'Hawaiian Standard Time', description:'(GMT-10:00) Hawaii', rawOffset:-10d],
			[id:3, stringId:'003', name:'Alaskan Standard Time', description:'(GMT-09:00) Alaska', rawOffset:-9d],
			[id:4, stringId:'004', name:'Pacific Standard Time', description:'(GMT-08:00) Pacific Time (US and Canada); Tijuana', rawOffset:-8d],
			[id:10, stringId:'010', name:'Mountain Standard Time', description:'(GMT-07:00) Mountain Time (US and Canada)', rawOffset:-7d],
			[id:13, stringId:'013', name:'Mexico Standard Time 2', description:'(GMT-07:00) Chihuahua, La Paz, Mazatlan', rawOffset:-7d],
			[id:15, stringId:'015', name:'U.S. Mountain Standard Time', description:'(GMT-07:00) Arizona', rawOffset:-7d],
			[id:20, stringId:'020', name:'Central Standard Time', description:'(GMT-06:00) Central Time (US and Canada', rawOffset:-6d],
			[id:25, stringId:'025', name:'Canada Central Standard Time', description:'(GMT-06:00) Saskatchewan', rawOffset:-6d],
			[id:30, stringId:'030', name:'Mexico Standard Time', description:'(GMT-06:00) Guadalajara, Mexico City, Monterrey', rawOffset:-6d],
			[id:33, stringId:'033', name:'Central America Standard Time', description:'(GMT-06:00) Central America', rawOffset:-6d],
			[id:35, stringId:'035', name:'Eastern Standard Time', description:'(GMT-05:00) Eastern Time (US and Canada)', rawOffset:-5d],
			[id:40, stringId:'040', name:'U.S. Eastern Standard Time', description:'(GMT-05:00) Indiana (East)', rawOffset:-5d],
			[id:45, stringId:'045', name:'S.A. Pacific Standard Time', description:'(GMT-05:00) Bogota, Lima, Quito', rawOffset:-5d],
			[id:50, stringId:'050', name:'Atlantic Standard Time', description:'(GMT-04:00) Atlantic Time (Canada)', rawOffset:-4d],
			[id:55, stringId:'055', name:'S.A. Western Standard Time', description:'(GMT-04:00) Caracas, La Paz', rawOffset:-4d],
			[id:56, stringId:'056', name:'Pacific S.A. Standard Time', description:'(GMT-04:00) Santiago', rawOffset:-4d],
			[id:60, stringId:'060', name:'Newfoundland and Labrador Standard Time', description:'(GMT-03:30) Newfoundland and Labrador', rawOffset:-3.5d],
			[id:65, stringId:'065', name:'E. South America Standard Time', description:'(GMT-03:00) Brasilia', rawOffset:-3d],
			[id:70, stringId:'070', name:'S.A. Eastern Standard Time', description:'(GMT-03:00) Buenos Aires, Georgetown', rawOffset:-3d],
			[id:73, stringId:'073', name:'Greenland Standard Time', description:'(GMT-03:00) Greenland', rawOffset:-3d],
			[id:75, stringId:'075', name:'Mid-Atlantic Standard Time', description:'(GMT-02:00) Mid-Atlantic', rawOffset:-2d],
			[id:80, stringId:'080', name:'Azores Standard Time', description:'(GMT-01:00) Azores', rawOffset:-1d],
			[id:83, stringId:'083', name:'Cape Verde Standard Time', description:'(GMT-01:00) Cape Verde Islands', rawOffset:-1d],
			[id:85, stringId:'085', name:'GMT Standard Time', description:'(GMT) Greenwich Mean Time: Dublin, Edinburgh, Lisbon, London', rawOffset:0d],
			[id:90, stringId:'090', name:'Greenwich Standard Time', description:'(GMT) Casablanca, Monrovia', rawOffset:0d],
			[id:95, stringId:'095', name:'Central Europe Standard Time', description:'(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague', rawOffset:1d],
			[id:100, stringId:'100', name:'Central European Standard Time', description:'(GMT+01:00) Sarajevo, Skopje, Warsaw, Zagreb', rawOffset:1d],
			[id:105, stringId:'105', name:'Romance Standard Time', description:'(GMT+01:00) Brussels, Copenhagen, Madrid, Paris', rawOffset:1d],
			[id:110, stringId:'110', name:'W. Europe Standard Time', description:'(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna', rawOffset:1d],
			[id:113, stringId:'113', name:'W. Central Africa Standard Time', description:'(GMT+01:00) West Central Africa', rawOffset:1d],
			[id:115, stringId:'115', name:'E. Europe Standard Time', description:'(GMT+02:00) Bucharest', rawOffset:2d],
			[id:120, stringId:'120', name:'Egypt Standard Time', description:'(GMT+02:00) Cairo', rawOffset:2d],
			[id:125, stringId:'125', name:'FLE Standard Time', description:'(GMT+02:00) Helsinki, Kiev, Riga, Sofia, Tallinn, Vilnius', rawOffset:2d],
			[id:130, stringId:'130', name:'GTB Standard Time', description:'(GMT+02:00) Athens, Istanbul, Minsk', rawOffset:2d],
			[id:135, stringId:'135', name:'Israel Standard Time', description:'(GMT+02:00) Jerusalem', rawOffset:2d],
			[id:140, stringId:'140', name:'South Africa Standard Time', description:'(GMT+02:00) Harare, Pretoria', rawOffset:2d],
			[id:145, stringId:'145', name:'Russian Standard Time', description:'(GMT+03:00) Moscow, St. Petersburg, Volgograd', rawOffset:3d],
			[id:150, stringId:'150', name:'Arab Standard Time', description:'(GMT+03:00) Kuwait, Riyadh', rawOffset:3d],
			[id:155, stringId:'155', name:'E. Africa Standard Time', description:'(GMT+03:00) Nairobi', rawOffset:3d],
			[id:158, stringId:'158', name:'Arabic Standard Time', description:'(GMT+03:00) Baghdad', rawOffset:3d],
			[id:160, stringId:'160', name:'Iran Standard Time', description:'(GMT+03:30) Tehran', rawOffset:3.5d],
			[id:165, stringId:'165', name:'Arabian Standard Time', description:'(GMT+04:00) Abu Dhabi, Muscat', rawOffset:4d],
			[id:170, stringId:'170', name:'Caucasus Standard Time', description:'(GMT+04:00) Baku, Tbilisi, Yerevan', rawOffset:4d],
			[id:175, stringId:'175', name:'Transitional Islamic State of Afghanistan Standard Time', description:'(GMT+04:30) Kabul', rawOffset:4.5d],
			[id:180, stringId:'180', name:'Ekaterinburg Standard Time', description:'(GMT+05:00) Ekaterinburg', rawOffset:5d],
			[id:185, stringId:'185', name:'West Asia Standard Time', description:'(GMT+05:00) Islamabad, Karachi, Tashkent', rawOffset:5d],
			[id:190, stringId:'190', name:'India Standard Time', description:'(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi', rawOffset:5.5d],
			[id:193, stringId:'193', name:'Nepal Standard Time', description:'(GMT+05:45) Kathmandu', rawOffset:5.75d],
			[id:195, stringId:'195', name:'Central Asia Standard Time', description:'(GMT+06:00) Astana, Dhaka', rawOffset:6d],
			[id:200, stringId:'200', name:'Sri Lanka Standard Time', description:'(GMT+06:00) Sri Jayawardenepura', rawOffset:6d],
			[id:201, stringId:'201', name:'N. Central Asia Standard Time', description:'(GMT+06:00) Almaty, Novosibirsk', rawOffset:6d],
			[id:203, stringId:'203', name:'Myanmar Standard Time', description:'(GMT+06:30) Yangon Rangoon', rawOffset:6.5d],
			[id:205, stringId:'205', name:'S.E. Asia Standard Time', description:'(GMT+07:00) Bangkok, Hanoi, Jakarta', rawOffset:7d],
			[id:207, stringId:'207', name:'North Asia Standard Time', description:'(GMT+07:00) Krasnoyarsk', rawOffset:7d],
			[id:210, stringId:'210', name:'China Standard Time', description:'(GMT+08:00) Beijing, Chongqing, Hong Kong SAR, Urumqi', rawOffset:8d],
			[id:215, stringId:'215', name:'Singapore Standard Time', description:'(GMT+08:00) Kuala Lumpur, Singapore', rawOffset:8d],
			[id:220, stringId:'220', name:'Taipei Standard Time', description:'(GMT+08:00) Taipei', rawOffset:8d],
			[id:225, stringId:'225', name:'W. Australia Standard Time', description:'(GMT+08:00) Perth', rawOffset:8d],
			[id:227, stringId:'227', name:'North Asia East Standard Time', description:'(GMT+08:00) Irkutsk, Ulaanbaatar', rawOffset:8d],
			[id:230, stringId:'230', name:'Korea Standard Time', description:'(GMT+09:00) Seoul', rawOffset:9d],
			[id:235, stringId:'235', name:'Tokyo Standard Time', description:'(GMT+09:00) Osaka, Sapporo, Tokyo', rawOffset:9d],
			[id:240, stringId:'240', name:'Yakutsk Standard Time', description:'(GMT+09:00) Yakutsk', rawOffset:9d],
			[id:245, stringId:'245', name:'A.U.S. Central Standard Time', description:'(GMT+09:30) Darwin', rawOffset:9.5d],
			[id:250, stringId:'250', name:'Cen. Australia Standard Time', description:'(GMT+09:30) Adelaide', rawOffset:9.5d],
			[id:255, stringId:'255', name:'A.U.S. Eastern Standard Time', description:'(GMT+10:00) Canberra, Melbourne, Sydney', rawOffset:10d],
			[id:260, stringId:'260', name:'E. Australia Standard Time', description:'(GMT+10:00) Brisbane', rawOffset:10d],
			[id:265, stringId:'265', name:'Tasmania Standard Time', description:'(GMT+10:00) Hobart', rawOffset:10d],
			[id:270, stringId:'270', name:'Vladivostok Standard Time', description:'(GMT+10:00) Vladivostok', rawOffset:10d],
			[id:275, stringId:'275', name:'West Pacific Standard Time', description:'(GMT+10:00) Guam, Port Moresby', rawOffset:10d],
			[id:280, stringId:'280', name:'Central Pacific Standard Time', description:'(GMT+11:00) Magadan, Solomon Islands, New Caledonia', rawOffset:11d],
			[id:285, stringId:'285', name:'Fiji Islands Standard Time', description:'(GMT+12:00) Fiji Islands, Kamchatka, Marshall Islands', rawOffset:12d],
			[id:290, stringId:'290', name:'New Zealand Standard Time', description:'(GMT+12:00) Auckland, Wellington', rawOffset:12d],
			[id:300, stringId:'300', name:'Tonga Standard Time', description:'(GMT+13:00) Nuku\'alofa', rawOffset:13d]
	]



}
