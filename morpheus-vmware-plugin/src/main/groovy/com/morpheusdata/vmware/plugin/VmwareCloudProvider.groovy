package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeCapacityInfo
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.OsType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.projection.*
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.vmware.plugin.utils.*
import com.morpheusdata.vmware.plugin.sync.DatastoresSync

import com.morpheusdata.vmware.plugin.sync.*
import groovy.util.logging.Slf4j
import com.vmware.vim25.*
import com.morpheusdata.core.util.ConnectionUtils
import java.security.MessageDigest
import io.reactivex.*
import io.reactivex.annotations.NonNull
import com.morpheusdata.core.util.SyncTask

@Slf4j
class VmwareCloudProvider implements CloudProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	VmwareCloudProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		OptionType apiUrl = new OptionType(
				name: 'Api Url',
				code: 'vmware-plugin-api-url',
				fieldName: 'serviceUrl',
				displayOrder: 0,
				fieldLabel: 'Api Url',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType username = new OptionType(
				name: 'Username',
				code: 'vmware-plugin-username',
				fieldName: 'serviceUsername',
				displayOrder: 1,
				fieldLabel: 'Username',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType password = new OptionType(
				name: 'Password',
				code: 'vmware-plugin-password',
				fieldName: 'servicePassword',
				displayOrder: 2,
				fieldLabel: 'Password',
				required: true,
				inputType: OptionType.InputType.PASSWORD,
				fieldContext: 'domain'
		)
		OptionType version = new OptionType(
				name: 'Version',
				code: 'vmware-plugin-version',
				fieldName: 'apiVersion',
				displayOrder: 3,
				fieldLabel: 'Version',
				required: true,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				optionSource: 'vmwarePluginVersions'
		)
		OptionType vdc = new OptionType(
				name: 'VDC',
				code: 'vmware-plugin-vdc',
				fieldName: 'datacenter',
				displayOrder: 4,
				fieldLabel: 'VDC',
				required: true,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				dependsOn: 'vmware-plugin-api-url,vmware-plugin-username,vmware-plugin-password',
				optionSource: 'vmwarePluginVDC'
		)

		OptionType cluster = new OptionType(
				name: 'Cluster',
				code: 'vmware-plugin-cluster',
				fieldName: 'cluster',
				displayOrder: 5,
				fieldLabel: 'Cluster',
				required: true,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				dependsOn: 'vmware-plugin-vdc',
				optionSource: 'vmwarePluginCluster'
		)

		OptionType resourcePool = new OptionType(
				name: 'Resource Pool',
				code: 'vmware-plugin-resource-pool',
				fieldName: 'resourcePoolId',
				displayOrder: 6,
				fieldLabel: 'Resource Pool',
				required: false,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config',
				dependsOn: 'vmware-plugin-cluster',
				optionSource: 'vmwarePluginResourcePool'
		)

		[apiUrl, username, password, version, vdc, cluster, resourcePool]
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		ComputeServerType hypervisorType = new ComputeServerType()
		hypervisorType.name = 'Vmware Plugin Hypervisor'
		hypervisorType.code = 'vmware-plugin-hypervisor'
		hypervisorType.description = 'vmware plugin hypervisor'
		hypervisorType.vmHypervisor = true
		hypervisorType.controlPower = false
		hypervisorType.reconfigureSupported = false
		hypervisorType.externalDelete = false
		hypervisorType.hasAutomation = false
		hypervisorType.agentType = ComputeServerType.AgentType.none
		hypervisorType.platform = PlatformType.esxi
		hypervisorType.managed = false

		ComputeServerType serverType = new ComputeServerType()
		serverType.name = 'Vmware Plugin Server'
		serverType.code = 'vmware-plugin-server'
		serverType.description = 'vmware plugin server'
		serverType.reconfigureSupported = false
		serverType.hasAutomation = false
		serverType.supportsConsoleKeymap = true
		serverType.platform = PlatformType.none
		serverType.managed = false

		return [hypervisorType, serverType]
	}

	@Override
	Collection<ProvisioningProvider> getAvailableProvisioningProviders() {
		return plugin.getProvidersByType(ProvisioningProvider) as Collection<ProvisioningProvider>
	}

	@Override
	Collection<BackupProvider> getAvailableBackupProviders() {
		return null
	}

	@Override
	ProvisioningProvider getProvisioningProvider(String providerCode) {
		return getAvailableProvisioningProviders().find { it.code == providerCode }
	}

	@Override
	Collection<NetworkType> getNetworkTypes() {
		return null
	}

	@Override
	ServiceResponse validate(Cloud cloudInfo) {
		log.info("validate: {}", cloudInfo)
		try {
			if(cloudInfo) {
				def configMap = cloudInfo.getConfigMap()
				if(configMap.datacenter?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Choose a datacenter')
				} else if(cloudInfo.serviceUsername?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Enter a username')
				} else if(cloudInfo.servicePassword?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Enter a password')
				} else if(cloudInfo.serviceUrl?.length() < 1) {
					return new ServiceResponse(success: false, msg: 'Enter an api url')
				} else {
					//test api call
					def apiUrl = VmwareProvisionProvider.getVmwareApiUrl(cloudInfo.serviceUrl)
					//get creds
					def dcList = VmwareComputeUtility.listDatacenters(apiUrl, cloudInfo.serviceUsername, cloudInfo.servicePassword)
					if(dcList.success == true) {
						return ServiceResponse.success()
					} else {
						return new ServiceResponse(success: false, msg: 'Invalid vmware credentials')
					}
				}
			} else {
				return new ServiceResponse(success: false, msg: 'No cloud found')
			}
		} catch(e) {
			log.error('Error validating cloud', e)
			return new ServiceResponse(success: false, msg: 'Error validating cloud')
		}
	}

	@Override
	void refresh(Cloud cloudInfo) {
		initializeCloud(cloudInfo)
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {
		// TODO : implement
	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		// TODO : implement
		return new ServiceResponse(success: true)
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'vmware-plugin-cloud'
	}

	@Override
	String getName() {
		return 'Vmware'
	}

	@Override
	String getDescription() {
		return 'Vmware VCenter plugin'
	}

	@Override
	Boolean getHasComputeZonePools() {
		return true
	}

	@Override
	ServiceResponse startServer(ComputeServer computeServer) {
		log.debug("startServer: ${computeServer}")
		def rtn = [success:false]
		try {
			return vmwareProvisionProvider().startServer(computeServer)
		} catch(e) {
			rtn.msg = "Error starting server: ${e.message}"
			log.error("startServer error: ${e}", e)
		}
		return ServiceResponse.create(rtn)
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		log.debug("stopServer: ${computeServer}")
		def rtn = [success:false]
		try {
			return vmwareProvisionProvider().stopServer(computeServer)
		} catch(e) {
			rtn.msg = "Error stoping server: ${e.message}"
			log.error("stopServer error: ${e}", e)
		}
		return ServiceResponse.create(rtn)
	}

	@Override
	ServiceResponse initializeCloud(Cloud cloud) {
		ServiceResponse rtn = new ServiceResponse(success: false)
		log.info "Initializing Cloud: ${cloud.code}"
		log.info "config: ${cloud.configMap}"
		
		try {
			def syncDate = new Date()
			def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
			def apiVersion = cloud.getConfigProperty('apiVersion') ?: '6.7'
			def apiUrlObj = new URL(authConfig.apiUrl)
			def apiHost = apiUrlObj.getHost()
			def apiPort = apiUrlObj.getPort() > 0 ? apiUrlObj.getPort() : (apiUrlObj?.getProtocol()?.toLowerCase() == 'https' ? 443 : 80)
			def hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, null)
			log.debug("vmware online: {} - {}", apiHost, hostOnline)
			if(hostOnline) {
				def testResults = VmwareComputeUtility.testConnection(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
				if(testResults.success == true) {
					morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.syncing, null, syncDate)

					checkZoneConfig(cloud)
					checkCluster(cloud)
					Date now = new Date()

					cacheResourcePools(cloud)

//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
					log.debug("resource pools completed in ${new Date().time - now.time} ms")

					//folders
//					lockService.renewLock(lockId.toString(),[timeout:lockTimeout, ttl:lockTtl])
					(new FoldersSync(cloud, morpheusContext)).execute()

//					lockService.renewLock(lockId.toString(), [timeout:lockTimeout, ttl:lockTtl])
					//interrupted thread
//					if(Thread.interrupted())
//						throw new InterruptedException();
//					log.debug("folders completed in ${new Date().time - now.time} ms")
//					now = new Date()
//					//datastores
					(new DatastoresSync(cloud, morpheusContext)).execute()

//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
					//
//					if(Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("datastores completed in ${new Date().time - now.time} ms")
//					now = new Date()
//					//fix region codes?
//					fixMissingRegionCodes(zone, getRegionCode(zone))

//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("region codes completed in ${new Date().time - now.time} ms")
//					now = new Date()
//					//templates
					(new TemplatesSync(cloud, morpheusContext)).execute()
//					cacheContentLibraryItems([zone:zone,proxySettings:proxySettings]) //dont pause for this right now
//
//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("templates completed in ${new Date().time - now.time} ms")
//					now = new Date()
					//networks
//					cacheNetworks([zone:zone]).get()
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("networks completed in ${new Date().time - now.time} ms")
					now = new Date()
					//hosts
					cacheHosts(cloud)
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("hosts completed in ${new Date().time - now.time} ms")
//					now = new Date()
//					//storage pods
					(new StoragePodsSync(cloud, morpheusContext)).execute()
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("storage pods completed in ${new Date().time - now.time} ms")
//					now = new Date()
//					//ip pools
//					cacheIpPools([zone:zone])
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					now = new Date()
					//custom specs
//					cacheCustomSpecs([zone:zone])
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("cacheCustomSpecs completed in ${new Date().time - now.time} ms")
//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
//					now = new Date()
//					//alarms
//					cacheAlarms([zone:zone]).get()
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("alarms completed in ${new Date().time - now.time} ms")
//					now = new Date()
//					//events
//					cacheEvents([zone:zone]).get()
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("events completed in ${new Date().time - now.time} ms")
					//datacenters
//					now = new Date()
//					cacheDatacenters([zone:zone])
//					sessionFactory.currentSession.clear()
//					zone.attach()
//					zone.account.attach()
//					zone.owner.attach()
//					if (Thread.interrupted()) {
//						throw new InterruptedException();
//					}
//					log.debug("datacenters completed in ${new Date().time - now.time} ms")
//					lockService.renewLock(lockId.toString(),[timeout: lockTimeout, ttl: lockTtl])
//					//vms
//					if(apiVersion && apiVersion != '6.0') {
//						now = new Date()
//						cacheCategories([zone:zone,proxySettings:proxySettings])
//						if (Thread.interrupted()) {
//							throw new InterruptedException();
//						}
//						log.debug("categories completed in ${new Date().time - now.time} ms")
//						now = new Date()
//						cacheTags([zone:zone,proxySettings:proxySettings])
//						if (Thread.interrupted()) {
//							throw new InterruptedException();
//						}
//						log.debug("tags completed in ${new Date().time - now.time} ms")
//					}

//					now = new Date()
//					def doInventory = zone.getConfigProperty('importExisting')
					Boolean createNew = true
//					if(doInventory == 'on' || doInventory == 'true' || doInventory == true) {
//						createNew = true
//					}
//
					//Returning Promise Chain now
					def proxySettings
					(new VirtualMachineSync(cloud, createNew, proxySettings, apiVersion, morpheusContext)).execute()
//					cacheVirtualMachines(cloud, createNew, proxySettings, apiVersion)//.then {
//						refreshZoneVms(zone, [:], syncDate)
//						return true
//					}.then {
						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.ok, null, syncDate)
//						newSessionClearZoneAlarm(zone.id)
//						log.debug("Cached Virtual Machines in ${new Date().time - now.time}ms")
//						return true
//					}.onError { Exception ex ->
//						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.ok, null, syncDate)
//						newSessionClearZoneAlarm(zone.id)
//						log.debug("Cached Virtual Machines in ${new Date().time - now.time}ms")
//					}


				}
				else {
					if (testResults.invalidLogin == true) {
						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.error, 'invalid credentials', syncDate)
					} else {
						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.error, 'error connecting', syncDate)
					}
				}

			} else {
				morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.offline, 'vmware is not reachable', syncDate)
			}
			rtn.success = true
		} catch (e) {
			log.error("refresh cloud error: ${e}", e)
		}
		return rtn
	}

	def checkZoneConfig(Cloud cloud) {
		log.debug "checkZoneConfig"
		def save = false
		//check the datacenter
		def datacenter = cloud.getConfigProperty('datacenter')
		def datacenterId = cloud.getConfigProperty('datacenterId')
		def datacenterResults = listDatacenters(cloud)
		def currentDatacenter = datacenterResults?.datacenters?.find{ it.name == cloud.getConfigProperty('datacenter') }
		//set the id of the datacenter if not set
		if(!datacenterId && datacenter) {
			if(currentDatacenter) {
				cloud.setConfigProperty('datacenterId', currentDatacenter.ref)
				save = true
			}
		}
		//check for datacenter name changes
		if(!currentDatacenter && datacenterId) {
			currentDatacenter = datacenterResults?.datacenters?.find{ it.ref == datacenterId }
			if(currentDatacenter) {
				cloud.setConfigProperty('datacenter', currentDatacenter.name)
				save = true
			}
		}
		//check the resource pool
		if(!cloud.getConfigProperty('resourcePoolId') && cloud.getConfigProperty('resourcePool')) {
			//we need to migrate the data model
			def results = listResourcePools(cloud)
			def currentPool = results?.resourcePools?.find{ it.name == cloud.getConfigProperty('resourcePool') }
			cloud.setConfigProperty('resourcePoolId', currentPool.ref)
			cloud.setConfigProperty('resourcePool', '')
			save = true
		}
		//check the region code
		def regionCode = getRegionCode(cloud)
		if(cloud.regionCode != regionCode) {
			cloud.regionCode = regionCode
			save = true
		}
		if(save) {
			morpheusContext.cloud.save(cloud).blockingGet()
		}
	}

	def checkCluster(Cloud cloud) {
		log.debug "checkCluster: ${cloud}"
		def serviceInstance
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		try {
			def cluster = cloud.getConfigProperty('cluster')
			if(!cluster) {
				serviceInstance = VmwareComputeUtility.getServiceInstance(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
				def resourcePool = cloud.getConfigProperty('resourcePoolId')
				def resourcePoolEntity = resourcePool ? VmwareComputeUtility.getManagedObject(serviceInstance, 'ResourcePool', resourcePool) : null
				def clusterEntity = resourcePoolEntity ? resourcePoolEntity.getOwner() : null
				if(clusterEntity) {
					cloud.setConfigProperty('cluster', clusterEntity.getName())
					morpheusContext.cloud.save(cloud).blockingGet()
				}
			}
		} catch(e) {
			log.warn("error checking for cluster on cloud: ${cloud?.id}: ${e}", e)
		} finally {
			if(serviceInstance)
				VmwareComputeUtility.releaseServiceInstance(serviceInstance, authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
		}
	}

	def cacheResourcePools(Cloud cloud) {
		log.debug "cacheResourcePools: ${cloud}"

		try {
			// Load the cloud based data
			String clusterScope = cloud.getConfigProperty('cluster') as String
			String clusterRef = cloud.getConfigProperty('clusterRef') as String
			List clusters = []
			def clsResults = listComputeResources(cloud)
			Boolean success = clsResults.success
			def tmpClusterResults = clsResults.computeResorces
			success = tmpClusterResults.success
			if (clusterScope) {
				clusters = tmpClusterResults.findAll { it.name == clusterScope }
				if (!clusters && clusterRef) {
					clusters = tmpClusterResults.findAll { it.ref == clusterRef }
				}
				if (clusters && (clusterRef != clusters?.first()?.ref || clusterScope != clusters?.first()?.name)) {
					//fix zone config
//					Promises.task {
//						ComputeZone.withNewTransaction {
//							ComputeZone tmpZone = ComputeZone.get(opts.zone.id)
//							tmpZone.setConfigProperty('cluster', clusters.first().name)
//							tmpZone.setConfigProperty('clusterRef', clusters.first().ref)
//							tmpZone.save(flush:true)
//						}
//					}
				}
			} else {
				clusters = tmpClusterResults
			}
			Map tmpPoolResults = [:]
			if (success) {
				for (cluster in clusters) {
					tmpPoolResults[cluster.name] = listResourcePools(cloud, cluster.name)
				}
			}

			def clusterResults = [clusterScope: clusterScope, poolResults: tmpPoolResults, clusters: clusters.collectEntries { [(it.name): it.ref] }]

			Map<String, Object> poolResults = clusterResults.poolResults as Map<String, Object>
			def tmpExistingItems = []
			def oldClusterNamesByRef = []
			if (!poolResults.isEmpty()) {
				morpheus.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
					if (projection.type != 'Datacenter' && (projection.internalId == null || (projection.internalId in poolResults.keySet()))) {
						return true
					}
					false
				}.blockingSubscribe {
					tmpExistingItems << it
					oldClusterNamesByRef << [(it.uniqueId): it.name] as Map<String, String>
				}
			}

			def queryResults = [existingItems: tmpExistingItems, poolResults: poolResults, clusters: clusterResults.clusters, existingClusterNames: oldClusterNamesByRef]

			def existingItems = queryResults.existingItems

			for (clusterName in poolResults.keySet()) {
				log.debug "Working on cluster ${clusterName}"
				def listResults = poolResults[clusterName]
				if (listResults.success) {
					String originalClusterName = queryResults.existingClusterNames[queryResults.clusters[clusterName]]
					Observable<ComputeServerIdentityProjection> domainRecords = Observable.create(new ObservableOnSubscribe<ComputeZonePoolIdentityProjection>() {
						@Override
						void subscribe(@NonNull ObservableEmitter<ComputeZonePoolIdentityProjection> emitter) throws Exception {
							def clusterExistingItems = existingItems.findAll { it.internalId == clusterName || it.internalId == null || it.internalId == originalClusterName }
							clusterExistingItems.each { it ->
								emitter.onNext(it)
							}
							emitter.onComplete()
						}
					})
					SyncTask<ComputeZonePoolIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, listResults.resourcePools)
					syncTask.addMatchFunction { ComputeZonePoolIdentityProjection domainObject, Map cloudItem ->
						domainObject.externalId == cloudItem.ref
					}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItems ->
						Map<Long, SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
						morpheus.cloud.pool.listById(updateItems?.collect { it.existingItem.id }).map { ComputeZonePool pool ->
							SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map> matchItem = updateItemMap[pool.id]
							return new SyncTask.UpdateItem<ComputeZonePool, Map>(existingItem: pool, masterItem: matchItem.masterItem)
						}
					}.onAdd { itemsToAdd ->
						addMissingResourcePools(cloud, clusterName, queryResults.clusters[clusterName] as String, itemsToAdd)
					}.onUpdate { List<SyncTask.UpdateItem<ComputeZonePool, Map>> updateItems ->
						def nameChanges = updateMatchedResourcePools(cloud, clusterName, queryResults.clusters[clusterName] as String, updateItems)
//						if(nameChanges) {
//							propagateResourcePoolTreeNameChanges(nameChanges)
//						}
					}.onDelete { removeItems ->
						removeMissingResourcePools(cloud, clusterName, queryResults.clusters[clusterName] as String, removeItems)
					}.start()


				}
//				if(cloud.owner.masterAccount == false) {
//					zonePoolService.chooseOwnerPoolDefaults(opts.zone.owner, opts.zone)
//				}
			}
			def clusteredSyncLists = [clusters: queryResults.clusters]

//			ComputeZonePool.withNewSession{ session ->
//				def clusterNames = clusteredSyncLists.clusters.keySet()
//				// purge old clusters
//				if(clusterNames) {
//					def oldPools = ComputeZonePool.withCriteria {
//						not {
//							inList('internalId', clusterNames)
//						}
//						eq('refType', 'ComputeZone')
//						eq('refId', opts.zone.id)
//					}
//
//					for(tmpPool in oldPools) {
//						ComputeZonePool.withNewTransaction { tx ->
//							tmpPool.attach()
//							zonePoolService.internalDeleteComputeZonePool(tmpPool)
//						}
//
//					}
//				}
//			}
		} catch(e) {
			log.error "Error in cacheResourcePools: ${e}", e
		}

	}

	def addMissingResourcePools(Cloud cloud, String clusterName, String clusterRef, List addList) {
		log.debug "addMissingResourcePools ${cloud} ${clusterName} ${clusterRef} ${addList.size()}"
		def adds = []

		// Gather up all the parent pools
		def parentIds = []
		for(cloudItem in addList) {
			if (cloudItem.parentType != 'ClusterComputeResource') {
				parentIds << cloudItem.parentId
			}
		}

		def parentPools = [:]
		if(parentIds) {
			morpheus.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
				return (projection.externalId in parentIds)
			}.blockingSubscribe {
				parentPools[it.externalId] = it
			}
		}

		for(cloudItem in addList) {
			def poolConfig = [owner:cloud.owner, name:cloudItem.name, externalId:cloudItem.ref, internalId: clusterName, refType:'ComputeZone', refId:cloud.id,
			                  cloud:cloud, category:"vmware.vsphere.resourcepool.${cloud.id}", code:"vmware.vsphere.resourcepool.${cloud.id}.${cloudItem.ref}",
			                  readOnly:cloudItem.readOnly]
			def add = new ComputeZonePool(poolConfig)
			if(cloudItem.parentType != 'ClusterComputeResource') {
				def parentPool = parentPools[cloudItem.parentId]
				add.type = 'default'
				add.parent = new ComputeZonePool(id: parentPool.id)
				add.treeName = nameForPool(add)
			} else {
				add.type = 'Cluster'
				add.name = clusterName
				add.treeName = clusterName
				add.uniqueId = clusterRef
			}
			adds << add
//			def resourcePerm = new ResourcePermission(morpheusResourceType:'ComputeZonePool', morpheusResourceId:add.id, account:zone.account)
//			resourcePerm.save(flush:true)
		}
		if(adds) {
			morpheusContext.cloud.pool.create(adds).blockingGet()
		}
	}

	def updateMatchedResourcePools(Cloud cloud, String clusterName, String clusterRef, List updateList) {
		log.debug "updateMatchedResourcePools: ${cloud} ${clusterName} ${clusterRef} ${updateList.size()}"
//		def matchedResourcePools = ComputeZonePool.where{zone.id == currentZone.id && (internalId == clusterName || internalId == null) && externalId in updateList.collect{ul -> ul.existingItem[1]}}.list()?.collectEntries{[(it.externalId):it]}
		List<Long> propagateTreeNameChanges = []
		def updates = []
		for(update in updateList) {
			def existingStore = update.existingItem
			if (existingStore) {
				Boolean save = false
				def matchItem = update.masterItem
				def name = matchItem.name

				if(existingStore && existingStore.readOnly != matchItem.readOnly) {
					existingStore.readOnly = matchItem.readOnly
					save = true
				}
				if(existingStore.internalId != clusterName) {
					existingStore.internalId = clusterName
					save = true
					if(existingStore.parent == null && existingStore.type != 'Cluster') {
						existingStore.type = 'Cluster'
					}
				}

				if(matchItem.parentType == 'ClusterComputeResource' && existingStore.uniqueId == null) {
					existingStore.uniqueId = clusterRef
					save = true
				}

				if(existingStore && existingStore.parent?.externalId != matchItem.parentId) {
					if(matchItem.parentType == 'ResourcePool') {
//						def parentPool = ComputeZonePool.findByZoneAndExternalId(currentZone,matchItem.parentId)
//						existingStore.parent = parentPool
					} else {
						existingStore.parent = null
						existingStore.type = 'Cluster'
					}
					existingStore.treeName = nameForPool(existingStore)
					propagateTreeNameChanges << existingStore.id
					save = true
				}
				if(existingStore.parent == null && existingStore.name != clusterName) {
					existingStore.name = clusterName
					existingStore.treeName = clusterName
					propagateTreeNameChanges << existingStore.id
					save = true
				} else if (existingStore.parent != null && existingStore.name != name) {
					existingStore.name = name
					existingStore.treeName = nameForPool(existingStore)
					propagateTreeNameChanges << existingStore.id
					save = true
				}
				if(save) {
					updates << existingStore
				}
			}
		}
		if(updates) {
			morpheusContext.cloud.pool.save(updates).blockingGet()
		}
		return propagateTreeNameChanges?.unique()
	}

	def removeMissingResourcePools(Cloud cloud, String clusterName, String clusterRef, List removeList) {
//		def removeItems = ComputeZonePool.where{zone.id == currentZone.id && (internalId == clusterName || internalId == null) && externalId in removeList.collect{it[1]}}.list()
//		removeItems?.toArray()?.each { removeItem ->
//		removeList?.each { removeItem ->
//			log.info("Removing Pool ${removeItem.name}")
//			removeItems.findAll{ existing -> existing.parent?.id == removeItem.id}.each { prnt ->
//				prnt.parent = null
//				prnt.save(flush:true)
//			}
//			zonePoolService.internalDeleteComputeZonePool(removeItem)
//		}
		morpheusContext.cloud.pool.remove(removeList).blockingGet()
	}

	protected nameForPool(pool) {
		def nameElements = [pool.name]
		def currentPool = pool
		while(currentPool.parent) {
			nameElements.add(0, currentPool.parent.name)
			currentPool = currentPool.parent
		}
		return nameElements.join(' / ')
	}

	def cacheHosts(Cloud cloud) {
		log.debug "cacheHosts: ${cloud}"

		try {
			def statsData = []

			def queryResults = [:]

			queryResults.serverType = new ComputeServerType(code: 'vmware-plugin-hypervisor')
			queryResults.serverOs = new OsType(code: 'esxi.6')

			def poolListProjections = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { poolProjection ->
				return poolProjection.internalId != null && poolProjection.type == 'Cluster'
			}.blockingSubscribe { poolListProjections << it }
			queryResults.clusters = []
			morpheusContext.cloud.pool.listById(poolListProjections.collect { it.id }).blockingSubscribe { queryResults.clusters << it }

			def cloudItems = []
			def listResultSuccess = false
			queryResults.clusters?.each { cluster ->
				def listResults = listHosts(cloud, cluster.internalId)
				if (listResults.success) {
					listResultSuccess = true
					cloudItems += listResults?.hosts
				}
			}

			if (listResultSuccess) {
				Observable domainRecords = morpheus.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection projection ->
					if (projection.category == "vmware.vsphere.host.${cloud.id}") {
						return true
					}
					false
				}
				SyncTask<ComputeServerIdentityProjection, Map, ComputeServer> syncTask = new SyncTask<>(domainRecords, cloudItems)
				syncTask.addMatchFunction { ComputeServerIdentityProjection domainObject, Map cloudItem ->
					domainObject.externalId == cloudItem?.ref.toString()
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItems ->
					Map<Long, SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
					morpheus.computeServer.listById(updateItems?.collect { it.existingItem.id }).map { ComputeServer server ->
						SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map> matchItem = updateItemMap[server.id]
						return new SyncTask.UpdateItem<ComputeServer, Map>(existingItem: server, masterItem: matchItem.masterItem)
					}
				}.onAdd { itemsToAdd ->
					addMissingHosts(cloud, queryResults.clusters, itemsToAdd)
				}.onUpdate { List<SyncTask.UpdateItem<ComputeServer, Map>> updateItems ->
					updateMatchedHosts(cloud, queryResults.clusters, updateItems)
				}.onDelete { removeItems ->
					removeMissingHosts(cloud, removeItems)
				}.start()
			}
		} catch(e) {
			log.error "Error in cacheHosts: ${e}", e
		}
	}

	def removeMissingHosts(Cloud cloud, List removeList) {
		log.debug "removeMissingHosts: ${cloud} ${removeList.size()}"
		morpheus.computeServer.remove(removeList).blockingGet()
	}

	def updateMatchedHosts(Cloud cloud, List clusters, List updateList) {
		log.debug "updateMatchedHosts: ${cloud} ${updateList.size()}"
//		def volumeType = StorageVolumeType.findByCode('vmware-datastore')

//		List<ComputeZonePool> zoneClusters = ComputeZonePool.where{zone.id == currentZone.id &&  type == 'Cluster' && internalId in updateList.collect{it.masterItem.cluster}.unique()}.list()
		def statsData = []
//		def matchedServersByExternalId = matchedServers?.collectEntries { [(it.externalId): it] }
		for(update in updateList) {
			ComputeServer currentServer = update.existingItem
			def matchedServer = update.masterItem
			if(currentServer) {
				def save = false
//				def clusterObj = zoneClusters?.find { pool -> pool.internalId == update.masterItem.cluster }
//				if(currentServer.resourcePool?.id != clusterObj.id) {
//					currentServer.resourcePool = clusterObj
//					save = true
//				}
//				def hostUuid = matchedServer.uuid
//				if(hostUuid && currentServer.uniqueId != hostUuid) {
//					currentServer.uniqueId = hostUuid
//					save = true
//				}
//				if(save) {
//					currentServer.save(flush: true)
//				}
//				syncHostDatastoreVolumes(currentServer,matchedServer,volumeType)
//				statsData += updateHostStats(currentServer, matchedServer)
			}
		}
//		if(statsData) {
//			def msg = [refId:1, jobType:'statsUpdate', data:statsData]
//			sendRabbitMessage('main', '', ApplianceJobService.applianceMonitorQueue, msg)
//		}
	}

	def addMissingHosts(Cloud cloud, List clusters, List addList) {
		log.debug "addMissingHosts: ${cloud} ${addList.size()}"
//		def volumeType = StorageVolumeType.findByCode('vmware-datastore')

		def serverType = new ComputeServerType(code: 'vmware-plugin-hypervisor')
		def serverOs = new OsType(code: 'esxi.6')
		for(cloudItem in addList) {
			def clusterObj = clusters?.find{ pool -> pool.internalId == cloudItem.cluster }

			def serverConfig = [account:cloud.owner, category:"vmware.vsphere.host.${cloud.id}", cloud: cloud,
			                    name:cloudItem.name, resourcePool: clusterObj, externalId:cloudItem.ref, uniqueId:cloudItem.uuid, sshUsername:'root', status:'provisioned',
			                    provision:false, serverType:'hypervisor', computeServerType:serverType, serverOs:serverOs,
			                    osType:'esxi', hostname:cloudItem.hostname]
			def newServer = new ComputeServer(serverConfig)
			newServer.maxMemory = cloudItem.memorySize
			newServer.maxStorage = 0
			newServer.capacityInfo = new ComputeCapacityInfo(maxMemory:cloudItem.memorySize)
			if(!morpheusContext.computeServer.create([newServer]).blockingGet()){
				log.error "Error in creating host server ${newServer}"
			}
//			syncHostDatastoreVolumes(newServer,cloudItem,volumeType)
		}
	}

	static listHosts(Cloud cloud, String clusterScope) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterScope ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listHosts(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listComputeResources(Cloud cloud) {
		log.debug "listComputeResources: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		rtn = VmwareComputeUtility.listComputeResources(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter])
		return rtn
	}

	static listDatacenters(Cloud cloud) {
		log.debug "listDatacenters: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listDatacenters(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [:])
		return rtn
	}

	static listDatastores(Cloud cloud, String clusterInternalId=null) {
		log.debug "listDatastores: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterInternalId ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listDatastores(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listStoragePods(Cloud cloud, String clusterInternalId=null) {
		log.debug "listStoragePods: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterInternalId ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listStoragePods(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listResourcePools(Cloud cloud, String clusterName=null) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = clusterName ?: cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listResourcePools(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster: cluster])
		return rtn
	}

	static listVirtualMachines(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		def resourcePool = cloud?.getConfigProperty('resourcePoolId')
		rtn = VmwareComputeUtility.listVirtualMachines(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, resourcePool:resourcePool, cluster:cluster])
		return rtn
	}

	static listTemplates(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		def resourcePool = cloud?.getConfigProperty('resourcePoolId')
		rtn = VmwareComputeUtility.listTemplates(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, resourcePool:resourcePool, cluster:cluster])
		return rtn
	}

	static getRegionCode(Cloud cloud) {
		def datacenter = cloud?.getConfigProperty('datacenter')
		def apiUrl = VmwareProvisionProvider.getVmwareApiUrl(cloud.serviceUrl)
		def regionString = "${apiUrl}.${datacenter}"
		MessageDigest md = MessageDigest.getInstance("MD5")
		md.update(regionString.bytes)
		byte[] checksum = md.digest()
		return checksum.encodeHex().toString()
	}

	VmwareProvisionProvider vmwareProvisionProvider() {
		this.plugin.getProviderByCode('vmware-provision-provider-plugin')
	}
}
