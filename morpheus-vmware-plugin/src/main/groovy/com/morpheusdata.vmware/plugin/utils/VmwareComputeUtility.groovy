package com.morpheusdata.vmware.plugin.utils

import com.vmware.vim25.*
import com.vmware.vim25.mo.*
import com.vmware.vim25.VirtualEthernetCard
import groovy.util.logging.Slf4j

@Slf4j
class VmwareComputeUtility {

	static VmwareConnectionPool connectionPool = new VmwareConnectionPool()

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
}
