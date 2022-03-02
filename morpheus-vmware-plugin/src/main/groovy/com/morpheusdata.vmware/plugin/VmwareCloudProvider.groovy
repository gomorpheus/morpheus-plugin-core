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
import groovy.util.logging.Slf4j
import com.vmware.vim25.*
import com.morpheusdata.core.util.ConnectionUtils
import java.security.MessageDigest
import io.reactivex.*
import io.reactivex.annotations.NonNull
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.core.util.ComputeUtility

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
		// TODO
		return new ServiceResponse(success: true)
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
		// TODO : implement
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		// TODO : implement
		return ServiceResponse.success()
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

					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
					log.debug("resource pools completed in ${new Date().time - now.time} ms")
					//folders
//					lockService.renewLock(lockId.toString(),[timeout:lockTimeout, ttl:lockTtl])
//					cacheFolders(cloud).get()

//					lockService.renewLock(lockId.toString(), [timeout:lockTimeout, ttl:lockTtl])
					//interrupted thread
//					if(Thread.interrupted())
//						throw new InterruptedException();
//					log.debug("folders completed in ${new Date().time - now.time} ms")
//					now = new Date()
//					//datastores
//					cacheDatastores([zone:zone])

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
//					cacheTemplates([zone:zone]).get()
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
//					cacheStoragePods([zone:zone])
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
					cacheVirtualMachines(cloud, createNew, proxySettings, apiVersion)//.then {
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

	def cacheVirtualMachines(Cloud cloud, Boolean createNew, proxySettings, apiVersion) {

		try {
			def queryResults = [:]
			def startTime = new Date().time

//			queryResults.clusters = ComputeZonePool.where { refType == 'ComputeZone' && refId == opts.zone.id && type == 'Cluster' && internalId != null && inventory != false }.property('internalId').list()
//			queryResults.serverType = ComputeServerType.findByCode('vmwareUnmanaged')
			// TODO : Handle blackListedNames!
//			queryResults.blackListedNames = queryResults.existingItems.findAll { it[3] == 'provisioning' }.collect { it[2] }

			def cloudItems = []
			def listResultSuccess = true
			// TODO : Handle cluster by cluster
//			queryResults.clusters?.each { cluster ->
//				def listResults = listVirtualMachines(opts + [cluster:cluster])
//				if(!listResults.success) {
//					listResultSuccess = false
//				}else {
//					cloudItems += listResults?.virtualMachines
//				}
//			}
			cloudItems = listVirtualMachines(cloud)?.virtualMachines

			if (listResultSuccess) {
				cloudItems = cloudItems?.unique { it.config.uuid }
			}
			log.debug("Build Sync List in ${new Date().time - startTime}")
			def defaultServerType = new ComputeServerType(code: 'vmware-plugin-server')
			def syncData = [cloudItems: cloudItems, defaultServerType: defaultServerType]


			log.info("virtualMachines to cache: ${syncData.cloudItems.size()}")

			Observable domainRecords = morpheus.computeServer.listSyncProjections(cloud.id).filter { ComputeServerIdentityProjection projection ->
				if (projection.computeServerTypeCode != 'vmware-plugin-hypervisor' || !projection.computeServerTypeCode) {
					return true
				}
				false
			}
			SyncTask<ComputeServerIdentityProjection, Map, ComputeServer> syncTask = new SyncTask<>(domainRecords, syncData.cloudItems)
			syncTask.addMatchFunction { ComputeServerIdentityProjection domainObject, Map cloudItem ->
				domainObject.uniqueId && (domainObject.uniqueId == cloudItem?.config.uuid)
			}.addMatchFunction { ComputeServerIdentityProjection domainObject, Map cloudItem ->
				domainObject.externalId && (domainObject.externalId == cloudItem?.ref.toString())
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it] }
				morpheus.computeServer.listById(updateItems?.collect { it.existingItem.id }).map { ComputeServer server ->
					SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map> matchItem = updateItemMap[server.id]
					return new SyncTask.UpdateItem<ComputeServer, Map>(existingItem: server, masterItem: matchItem.masterItem)
				}
			}.onAdd { itemsToAdd ->
				if (createNew) {
					addMissingVirtualMachines(cloud, itemsToAdd, defaultServerType)
				}
			}.onUpdate { List<SyncTask.UpdateItem<ComputeServer, Map>> updateItems ->
				updateMatchedVirtualMachines(cloud, updateItems)
//				updateServerUsages(usageLists.startUsageIds, usageLists.stopUsageIds, usageLists.restartUsageIds)
			}.onDelete { removeItems ->
				removeMissingVirtualMachines(cloud, removeItems)
//				morpheus.computeServer.remove(removeItems).blockingGet()
			}.start()
		} catch(e) {
			log.error "Error in cacheVirtualMachines: ${e}", e
		}
	}

	protected removeMissingVirtualMachines(Cloud cloud, List removeList) {
		log.debug "removeMissingVirtualMachines: ${cloud} ${removeList.size}"
		for(ComputeServer removeItem in removeList) {
			try {
				ComputeServer tmpServer = ComputeServer.where{ id == removeItem.id}.join('computeServerType').join('volumes').join('controllers').join('interfaces').join('metadata').list()?.first()

				def doDelete = true
//				if(blackListedNames?.contains(tmpServer.name))
//					doDelete = false
				if(doDelete) {
					log.info("remove vm: ${removeItem}")
					morpheus.computeServer.remove([removeItem]).blockingGet()
//					if(!tmpServer.computeServerType.managed) {
//						deleteUnmanagedServer(tmpServer)
//					} else if(tmpServer.status != 'provisioning' ) {
//						disableRemovedManagedServer(tmpServer)
//					}
				}
			} catch(de) {
				log.warn("Unable to remove Server from inventory, Perhaps it is associated with an instance currently... ${removeItem.name} - ID: ${removeItem.id}")
			}
		}
	}

	protected updateMatchedVirtualMachines(Cloud cloud, List updateList) {
		log.debug "updateMatchedVirtualMachines: ${cloud} ${updateList?.size()}"
		def rtn = [restartUsageIds: [], stopUsageIds: [], startUsageIds: []]

		def hosts = getHostsForVirtualMachines(cloud, updateList.collect { it.masterItem } )
		def resourcePools = getResourcePoolsForVirtualMachines(cloud, updateList.collect { it.masterItem})

//		Map<String,ComputeServerInterfaceType> netTypes = ComputeServerInterfaceType.list(readOnly:true)?.collectEntries{[(it.externalId):it]}
//		List<ComputeZonePool> zoneResourcePools = ComputeZonePool.where{ zone == cloud && externalId in updateList.collect{it.masterItem.resourcePool?.getVal()}.unique()}.list(readOnly:true)
//		List<ComputeZoneFolder> zoneFolders = ComputeZoneFolder.where{ zone == cloud && externalId in updateList.collect{it.masterItem.parent?.getVal()}.unique()}.list(readOnly:true)
//		Map<String,Network> systemNetworks
//		def networkIds = updateList.collect{it.masterItem.networks.collect{it.networkId}}.flatten().unique()
//		if(networkIds.size() > 0) {
//			systemNetworks = Network.where{refType == 'ComputeZone' && refId == cloud.id && type.code != 'childNetwork' && externalId in networkIds}.list(readOnly:true)?.collectEntries{[("${it.internalId ?: ''}:${it.externalId}".toString()):it]}
//		}
//		ServicePlan fallbackPlan = ServicePlan.findByCode('internal-custom-vmware')
//		Collection<ServicePlan> availablePlans = ServicePlan.where{active == true && deleted != true && provisionType.code == 'vmware'}.list(readOnly:true)
//		Collection<ResourcePermission> availablePlanPermissions = []
//		if(availablePlans) {
//			availablePlanPermissions = ResourcePermission.where{ morpheusResourceType == 'ServicePlan' && morpheusResourceId in availablePlans.collect{pl -> pl.id}}.list(readOnly:true)
//		}
//		Collection<MetadataTag> existingTags = MetadataTag.where{refType == 'ComputeZone' && refId == cloud.id}.list(readOnly:true)
//		List<ComputeServer> matchedServers = ComputeServer.where {
//			zone == cloud && (uniqueId in updateList.collect { rm -> rm.existingItem[0] } || externalId in updateList.collect { rm -> rm.existingItem[1] })
//		}.join('account').join('zone').join('zone.zoneType').join('computeServerType').join('plan').join('chassis').join('serverOs').join('sourceImage').join('folder').join('createdBy')
//				.join('userGroup').join('networkDomain').join('interfaces').join('serverOs').join('controllers').join('snapshots').join('metadata').join('volumes').join('volumes.datastore')
//				.join('resourcePool').join('parentServer').join('capacityInfo').join('metadata').list()

//		def managedServerIds = matchedServers?.findAll{it.computeServerType?.managed}?.collect{it.id}
//		def tmpContainers = [:]
//		if(managedServerIds) {
//			tmpContainers = Container.where{server.id in managedServerIds}.join('containerType').list(readOnly:true).groupBy{it.serverId}
//		}
//		//lets look for duplicate discovered vms in the result set and delete if unmanaged
//		def serversGroupedByUniqueId = matchedServers.groupBy{it.uniqueId}
//		serversGroupedByUniqueId?.each { tmpUniqueId, svList ->
//			if(svList.size() > 1) {
//				def discoveredServer = svList.find{ !it.computeServerType.managed}
//				if(discoveredServer) {
//					matchedServers.remove(discoveredServer)
//					deleteUnmanagedServer(discoveredServer)
//				}
//			}
//		}
		// println "Matched Servers: ${matchedServers?.collect{"${it.id}:${it.name}"}}"
//		def matchedServersByExternalId = matchedServers?.collectEntries { [(it.externalId): it] }
//		def matchedServersByUniqueId = matchedServers?.collectEntries { [(it.uniqueId): it] }
//		Map<Long, WikiPage> serverNotes = WikiPage.where{refType == 'ComputeServer' && refId in matchedServers.collect{it.id}}.list()?.collectEntries { [(it.refId): it] }
//		def vmIds = matchedServers.collect{it.externalId}
//		def apiVersion = cloud.getConfigProperty('apiVersion') ?: '6.7'
//		def tagAssociations
//		if(apiVersion && apiVersion != '6.0') {
//			tagAssociations = listTagAssociations([zone:cloud,vmIds: vmIds, proxySettings:opts.proxySettings])
//		}
//		def statsData = []
//		def updateServers = []

		for(update in updateList) {
			// TODO : Handle matching via uniqueId
//			ComputeServer currentServer = update.existingItem[0] ? matchedServersByUniqueId[update.existingItem[0]] : null
			ComputeServer currentServer = update.existingItem
			def matchedServer = update.masterItem
//			if(!currentServer) {
//				currentServer = matchedServersByExternalId[update.existingItem[1]]
//				// println "Found ${currentServer?.id}: ${currentServer?.name} by external id ${currentServer.externalId}"
//			}
			if(currentServer) {
//				updateServers << currentServer
				//update view ?
				if(currentServer && currentServer.status != 'provisioning') {
					try {
						// println "Server : ${currentServer.id}: ${currentServer.name}"
						def serverIps = VmwareComputeUtility.getServerIps(matchedServer.guest.net)
						if(!serverIps.ipAddress && matchedServer.guest.ipAddress) {
							serverIps.ipAddress = matchedServer.guest.ipAddress
						}
						def osTypeCode = VmwareComputeUtility.getMapVmwareOsType(matchedServer.config.guestId)
						def osType = new OsType(code: osTypeCode ?: 'other')
						def vmwareHost = matchedServer.guest?.hostName
						def resourcePoolId = matchedServer.resourcePool?.getVal()
						def resourcePool = resourcePools?.find{ pool -> pool.externalId == resourcePoolId }
//						def folderId = matchedServer.parent?.getVal()
//						def folder = zoneFolders?.find { folder -> folder.externalId == folderId }
						def powerState = matchedServer.runtime?.powerState == VirtualMachinePowerState.poweredOn ? ComputeServer.PowerState.on : (matchedServer.runtime?.powerState == VirtualMachinePowerState.suspended ? ComputeServer.PowerState.paused : ComputeServer.PowerState.off)
						def save = false
						def extraConfig = matchedServer.config?.extraConfig ?: []
						def consoleEnabled = extraConfig.find { option -> option.key.toLowerCase() == 'remotedisplay.vnc.enabled' }?.value?.toLowerCase() == 'true' ? true : false
						def consolePort = extraConfig.find { option -> option.key.toLowerCase() == 'remotedisplay.vnc.port' }?.value?.toInteger()
						def consolePassword = extraConfig.find { option -> option.key.toLowerCase() == 'remotedisplay.vnc.password' }?.value
						def hotResize = (matchedServer.config?.memoryHotAddEnabled)
						def cpuHotResize = matchedServer.config?.cpuHotAddEnabled == true
						def toolsInstalled = matchedServer.guest?.toolsStatus != 'toolsNotInstalled'
						def vmUuid = matchedServer.config?.uuid
						if(vmUuid && currentServer.uniqueId != vmUuid) {
							currentServer.uniqueId = vmUuid
							save = true
						}
						def instanceUuid = matchedServer.config?.instanceUuid
						if(instanceUuid && currentServer.internalId != instanceUuid) {
							currentServer.internalId = instanceUuid
							save = true
						}
						// TODO : Fix up computeServerType for windows
//						if(osType && osTypeCode != 'other.64' && ['windows','linux'].contains(osType.platform) && currentServer.computeServerType.managed &&  currentServer.computeServerType.platform != osType.platform ) {
//							if(osType.platform == 'windows') {
//								currentServer.computeServerType = ComputeServerType.findByCode('vmwareWindows')
//							} else {
//								currentServer.computeServerType = ComputeServerType.findByCode('vmwareVm')
//							}
//							save = true
//						}
						if(currentServer.status != 'provisioning' && osType && osType?.code != currentServer.serverOs?.code && osTypeCode && osTypeCode != 'other.64') {
							currentServer.serverOs = osType
							currentServer.osType = osType.platform
							save = true
						}
						if(currentServer.externalId != matchedServer.ref) {
							currentServer.externalId = matchedServer.ref
							save = true
						}
						if(currentServer.name != matchedServer.name) {
							currentServer.name = matchedServer.name
							save = true
						}
						if(osType && osType?.code != currentServer.serverOs?.code && osTypeCode && osTypeCode != 'other.64') {
							currentServer.serverOs = osType
							currentServer.osType = osType.platform
							save = true
						}
						if(currentServer.toolsInstalled != toolsInstalled) {
							currentServer.toolsInstalled = toolsInstalled
							save = true
						}
						if(currentServer.resourcePool?.id != resourcePool?.id) {
							currentServer.resourcePool = resourcePool
							save = true
						}
//						if(currentServer.folder?.id != folder?.id) {
//							currentServer.folder = folder
//							save = true
//						}
//						if(currentServer.computeServerType == null) {
//							currentServer.computeServerType = serverType
//							save = true
//						}
						if(vmwareHost && currentServer.hostname != vmwareHost) {
							currentServer.hostname = vmwareHost
							//managed guest virtual machine that is not a container host
//							if(currentServer.computeServerType?.guestVm && !currentServer.computeServerType?.containerHypervisor && currentServer.computeServerType?.managed) {
//								Container.where{server == currentServer}.updateAll(hostname:  vmwareHost)
//							}
							save = true
						}
						if(matchedServer.runtime?.host?.getVal()?.toString() != currentServer.parentServer?.externalId) {
							def host = hosts.find{hst -> hst.externalId == matchedServer.runtime.host.getVal()?.toString()}
							currentServer.parentServer = host
							save = true
						}
						//check capacity
						def maxMemory = (matchedServer.summary?.config?.memorySizeMB ?: 0) * ComputeUtility.ONE_MEGABYTE //vmware is not storing in Mibibytes, need old school megabytes solution soon
						def maxCores = matchedServer.summary?.config?.numCpu
						def coresPerSocket = matchedServer.config?.hardware?.numCoresPerSocket ?: 1
						Boolean planInfoChanged = false
						def capacityInfo = currentServer.capacityInfo
						//create one if it doesn't exist
						if(!capacityInfo) {
							currentServer.capacityInfo = new ComputeCapacityInfo(maxCores:maxCores, coresPerSocket:coresPerSocket, maxMemory:maxMemory)
							capacityInfo = currentServer.capacityInfo
							save = true
						}
						//adjust stats
						if(currentServer.maxCores != maxCores) {
							currentServer.maxCores = maxCores
							// log.info("Core Count Change Detected to ${maxCores} for ${currentServer.name}")
							capacityInfo.maxCores = currentServer.maxCores
							planInfoChanged = true
							save = true
						}
						if(currentServer.coresPerSocket != coresPerSocket) {
							currentServer.coresPerSocket = coresPerSocket
							planInfoChanged = true
							save = true
						}
						if(currentServer.maxMemory != maxMemory) {
							// log.info("Memory Changed Detected to ${maxMemory} from ${currentServer.maxMemory} for ${currentServer.name}")
							currentServer.maxMemory = maxMemory
							capacityInfo.maxMemory = currentServer.maxMemory
							planInfoChanged = true
							save = true
						}
						//plan
//						ServicePlan plan = findServicePlanBySizing(availablePlans, currentServer.maxMemory, currentServer.maxCores,
//								currentServer.coresPerSocket, fallbackPlan,currentServer.plan, currentServer.account, availablePlanPermissions)
//						if(currentServer.plan?.id != plan?.id) {
//							currentServer.plan = plan
//							// log.info("Changing Server Plan to ${plan?.name} -- ${currentServer.name}")
//							planInfoChanged = true
//							save = true
//						}
						//check storage
//						if(matchedServer.controllers) {
//							if(currentServer.status != 'resizing') {
//								def changed = vmwareProvisionService.syncControllers(cloud, currentServer, matchedServer.controllers, currentServer.account)
//								if(changed == true)
//									save = true
//							}
//						}
						def volumeInfoChanged = false
//						if(matchedServer.volumes) {
//							if(currentServer.status != 'resizing') {
//								def changed = vmwareProvisionService.syncVolumes(cloud, currentServer, matchedServer.volumes, currentServer.account)
//								if(changed == true) {
//									currentServer.maxStorage = currentServer.volumes ? (currentServer.volumes?.sum{it.maxStorage} as Long) : 0L
//									planInfoChanged = true
//									volumeInfoChanged = true
//									save = true
//								}
//							}
//						}
//						//check networks
//						if(matchedServer.networks) {
//							if(currentServer.status != 'resizing') {
//								def changed = vmwareProvisionService.syncInterfaces(cloud, currentServer, matchedServer.networks, serverIps.ipList, currentServer.account, systemNetworks, netTypes)
//								if(changed == true)
//									save = true
//							}
//						}
//						if(planInfoChanged && currentServer.computeServerType?.guestVm) {
//							Container.where{server == currentServer}.updateAll(plan:  plan, maxCores: currentServer.maxCores, maxMemory: currentServer.maxMemory, coresPerSocket: currentServer.coresPerSocket, maxStorage: currentServer.maxStorage)
//							Instance.where{ containers.server.id == currentServer.id}.join('containers').list(cache:false)?.each { instance ->
//								//Only update if instance contains containers with the same plan or if instance is 1:1 mapping to server
//								if(instance.containers.every{cnt -> (cnt.plan.id == currentServer.plan.id && cnt.maxMemory == currentServer.maxMemory && cnt.maxCores == currentServer.maxCores && cnt.coresPerSocket == currentServer.coresPerSocket) || cnt.server.id == currentServer.id}) {
//									//log.info("Changing Instance Plan To : ${plan.name} - memory: ${currentServer.maxMemory} for ${instance.name} - ${instance.id}")
//									instance.plan = plan
//									instance.maxCores = currentServer.maxCores
//									instance.maxMemory = currentServer.maxMemory
//									instance.maxStorage = currentServer.maxStorage
//									instance.coresPerSocket = currentServer.coresPerSocket
//									instance.save(flush:true)
//								}
//							}
//						}
//						def notesPage = serverNotes[currentServer.id]
//						// println "Annotation is ${matchedServer.summary.config.annotation} ${matchedServer.summary.config.annotation == null}"
//						if((notesPage != null && matchedServer.summary.config.annotation != notesPage?.content) || (notesPage == null && matchedServer.summary.config.annotation)) {
//							computeService.saveNotes(currentServer, matchedServer.summary.config.annotation, false, false)
//							if(!(currentServer.computeServerType?.containerHypervisor) && !(currentServer.computeServerType?.vmHypervisor)) {
//								Instance.where { containers.server == currentServer}.list()?.each { instance ->
//									def page = wikiPageService.findOrCreateReferencePage(instance.account, 'Instance', instance.id)
//									wikiPageService.updatePage(page, [name:instance.displayName ?: instance.name, category:'instances', content:matchedServer.summary.config.annotation], null,false)
//								}
//							}
//							save = true
//						}
						if(powerState != currentServer.powerState) {
							def previousState = currentServer.powerState
							currentServer.powerState = powerState
							if(currentServer.computeServerType?.guestVm) {
								if(currentServer.powerState == ComputeServer.PowerState.on) {
//									Container.where {
//										server == currentServer && status != Container.Status.deploying && status != Container.Status.failed
//									}.updateAll(userStatus: Container.Status.running, status: Container.Status.running)
//									rtn.startUsageIds << currentServer.id
//									def instanceIds = Container.withCriteria {
//										createAlias('instance', 'instance')
//										createAlias('server','server')
//										ne('status', Container.Status.failed)
//										ne('status', Container.Status.deploying)
//										not {
//											inList('instance.status', [Instance.Status.pendingReconfigureApproval, Instance.Status.pendingDeleteApproval, Instance.Status.removing,Instance.Status.restarting,Instance.Status.finishing,Instance.Status.resizing,Instance.Status.failed])
//										}
//										eq('server.id', currentServer.id)
//										projections {
//											property('instance.id')
//										}
//									}?.flatten()?.unique()
//									if(instanceIds) {
//										com.morpheus.Instance.where { id in instanceIds }.updateAll(status: com.morpheus.Instance.Status.running)
//									}
								} else if(currentServer.powerState == ComputeServer.PowerState.paused) {
//									Container.where {
//										server == currentServer && status != Container.Status.deploying && status != Container.Status.failed
//									}.updateAll(userStatus: Container.Status.suspended, status: Container.Status.suspended)
//									if(previousState != 'on') {
//										rtn.startUsageIds << currentServer.id
//									}
//
//									def instanceIds = Container.withCriteria {
//										createAlias('instance', 'instance')
//										createAlias('server','server')
//										ne('status', Container.Status.failed)
//										ne('status', Container.Status.deploying)
//										not {
//											inList('instance.status', [Instance.Status.pendingReconfigureApproval, Instance.Status.pendingDeleteApproval, Instance.Status.removing,Instance.Status.restarting,Instance.Status.finishing,Instance.Status.resizing,Instance.Status.failed])
//										}
//										eq('server.id', currentServer.id)
//										projections {
//											property('instance.id')
//										}
//									}?.flatten()?.unique()
//									if(instanceIds) {
//										com.morpheus.Instance.where { id in instanceIds }.updateAll(status: com.morpheus.Instance.Status.suspended)
//									}
								} else {
//									def containerStatus = currentServer.powerState == ComputeServer.PowerState.paused ? Container.Status.suspended : Container.Status.stopped
//									def instanceStatus = currentServer.powerState == ComputeServer.PowerState.paused ? com.morpheus.Instance.Status.suspended : com.morpheus.Instance.Status.stopped
//									Container.where {
//										server == currentServer && status != Container.Status.deploying
//									}.updateAll(status:containerStatus)
//									rtn.stopUsageIds << currentServer.id
//									def instanceIds = Container.withCriteria {
//										createAlias('instance', 'instance')
//										createAlias('server', 'server')
//										ne('status', Container.Status.failed)
//										ne('status', Container.Status.deploying)
//										not {
//											inList('instance.status', [Instance.Status.removing, Instance.Status.restarting, Instance.Status.finishing, Instance.Status.resizing, Instance.Status.stopping, Instance.Status.starting])
//										}
//										eq('server.id', currentServer.id)
//										projections {
//											property('instance.id')
//										}
//									}?.flatten()?.unique()
//									if(instanceIds) {
//										com.morpheus.Instance.where {
//											id in instanceIds
//										}.updateAll(status: instanceStatus)
//									}
								}
							}
							save = true
						}

//						syncSnapshotsForServer(currentServer,matchedServer.snapshots,matchedServer.currentSnapshot)

						Boolean tagChanges = false
//						if(tagAssociations && tagAssociations.success) {
//							def associatedTags = tagAssociations ? tagAssociations.associations[currentServer.externalId] : null
//							def tags = []
//							if(associatedTags) {
//								associatedTags.each { tagA ->
//									def tagMatch = existingTags.find{it.externalId == tagA}
//									if(tagMatch) {
//										tags << tagMatch
//									}
//								}
//							}
//							def tagMatchFunction = { MetadataTag morpheusItem, MetadataTag matchedMetadata ->
//								morpheusItem?.id == matchedMetadata?.id
//							}
//							def tagSyncLists = ComputeUtility.buildSyncLists(currentServer.metadata, tags, tagMatchFunction)
//							tagSyncLists.addList?.each { tag ->
//								currentServer.addToMetadata(tag)
//								save = true
//								tagChanges = true
//							}
//							tagSyncLists.removeList?.each { tagRemove ->
//								currentServer.removeFromMetadata(tagRemove)
//								save = true
//								tagChanges = true
//							}
//							if(tagChanges && currentServer.computeServerType?.containerHypervisor == false && currentServer.computeServerType?.vmHypervisor == false) {
//								def instances = Container.where{server == currentServer && instance != null}.join('instance').list()?.collect{ct -> ct.instance}
//								instances?.each { instance ->
//									tagSyncLists = ComputeUtility.buildSyncLists(instance.metadata, tags, tagMatchFunction)
//									tagSyncLists.addList?.each { tag ->
//										instance.addToMetadata(tag)
//									}
//									tagSyncLists.removeList?.each { tagRemove ->
//										instance.removeFromMetadata(tagRemove)
//									}
//									instance.save(flush:true)
//								}
//							}
//						}
						//check for restart usage records
						if(planInfoChanged || volumeInfoChanged || tagChanges) {
//							if(!rtn.stopUsageIds.contains(currentServer.id) && !rtn.startUsageIds.contains(currentServer.id))
//								rtn.restartUsageIds << currentServer.id
						}
//						def privateIp = currentServer.interfaces.find { it.primaryInterface }?.ipAddress ?: serverIps.ipAddress
//						def publicIp = currentServer.interfaces.find { it.primaryInterface }?.ipAddress ?: serverIps.ipAddress
//						if(publicIp != currentServer.externalIp) {
//							if(currentServer.externalIp == currentServer.sshHost) {
//								currentServer.sshHost = publicIp
//							}
//							currentServer.externalIp = publicIp
//							save = true
//						}
//						if(privateIp != currentServer.internalIp) {
//							if(currentServer.internalIp == currentServer.sshHost) {
//								currentServer.sshHost = privateIp
//							}
//							currentServer.internalIp = privateIp
//							save = true
//						}
						if(hotResize != currentServer.hotResize) {
							currentServer.hotResize = hotResize
							save = true
						}
						if(cpuHotResize != currentServer.cpuHotResize) {
							currentServer.cpuHotResize = cpuHotResize
							save = true
						}
						if(consoleEnabled != (currentServer.consoleHost != null) || (consoleEnabled && consolePort != currentServer.consolePort)) {
							if(consoleEnabled) {
								// println "Correcting Hypervisor Console Information"
								currentServer.consoleHost = currentServer.parentServer?.name
								currentServer.consolePort = consolePort
								currentServer.consoleType = 'vnc'
								currentServer.consolePassword = consolePassword
//								ComputePort.where {
//									refType == 'ComputeServer' && refId == currentServer.id && portType == 'vnc'
//								}.deleteAll()
//								def computePort = new ComputePort(parentType: 'ComputeZone', parentId: cloud.id, port: currentServer.consolePort, portCount: 1,
//										portType: 'vnc', refType: 'ComputeServer', regionCode: cloud.regionCode, refId: currentServer.id)
//								computePort.save(flush: true)
							} else {
								currentServer.consoleHost = null
								currentServer.consolePort = null
								currentServer.consoleType = null
								currentServer.consolePassword = null
//								ComputePort.where {
//									refType == 'ComputeServer' && refId == currentServer.id
//								}.deleteAll()
							}
						}
						if(currentServer.consolePassword != consolePassword) {
							currentServer.consolePassword = consolePassword
							save = true
						}
						if(currentServer.consoleHost && currentServer.consoleHost != currentServer.parentServer?.name) {
							currentServer.consoleHost = currentServer.parentServer?.name
							save = true
						}

						if(save) {
							morpheusContext.computeServer.save([currentServer]).blockingGet()
						}
//						if((currentServer.agentInstalled == false || currentServer.powerState == 'off' || currentServer.powerState == 'suspended') && currentServer.status != 'provisioning') {
//							// def agentlessContainers = Container.where { currentServer.id in }
//							statsData += updateVirtualMachineStats(currentServer, matchedServer,tmpContainers)
//						}
					} catch(ex) {
						log.warn("Error Updating Virtual Machine ${currentServer?.name} - ${currentServer.externalId} - ${ex}", ex)
					}
				}
			}
		}
//		if(statsData) {
//			containerService.updateContainerStats(statsData)
//			def msg = [refId:1, jobType:'statsUpdate', data:statsData]
//			sendRabbitMessage('main', '', ApplianceJobService.applianceMonitorQueue, msg)
//		}
//		if(updateServers) {
//			tagCompliancePolicyService.checkTagComplianceForServers(cloud, updateServers)
//		}
		return rtn
	}

	def addMissingVirtualMachines(Cloud cloud, List addList, ComputeServerType defaultServerType, blackListedNames=[]) {
		log.debug "addMissingVirtualMachines ${cloud} ${addList?.size()} ${defaultServerType} ${blackListedNames}"
//		Map<String,ComputeServerInterfaceType> netTypes = ComputeServerInterfaceType.list()?.collectEntries{[(it.externalId):it]}

		// Gather up all the hosts needed
		// TODO: Maybe pass these in rather than refetch?
		def hosts = getHostsForVirtualMachines(cloud, addList)
		def resourcePools = getResourcePoolsForVirtualMachines(cloud, addList)
		// TODO : Set the plan
//		def plans = getServicePlans(cloud)
		// TODO : Handle Folders and Networks
//		List<ComputeZoneFolder> zoneFolders = ComputeZoneFolder.where{zone == cloud && externalId in addList.collect{it.parent?.getVal()}.unique()}.list()
//		Map<String,Network> systemNetworks
//		def networkIds = addList.collect{it.networks.collect{it.networkId}}.flatten().unique()
//		if(networkIds) {
//			systemNetworks = Network.where{refType == 'ComputeZone' && refId == cloud.id && type.code != 'childNetwork' && externalId in networkIds}.list()?.collectEntries{[("${it.internalId ?: ''}:${it.externalId}".toString()):it]}
//		}

//		ServicePlan fallbackPlan = ServicePlan.findByCode('internal-custom-vmware')
//		Collection<ServicePlan> availablePlans = ServicePlan.where{active == true && deleted != true && provisionType.code == 'vmware'}.list()
//		Collection<ResourcePermission> availablePlanPermissions = []
//		if(availablePlans) {
//			availablePlanPermissions = ResourcePermission.where{ morpheusResourceType == 'ServicePlan' && morpheusResourceId in availablePlans.collect{pl -> pl.id}}.list()
//		}
//		def addedServers = []
		for(cloudItem in addList) {
			//if we have extra zones - try to find the vm in that zone first
			def vmUuid = cloudItem.config.uuid
			def resourcePoolId = cloudItem.resourcePool?.getVal()
			def resourcePool = resourcePools?.find{ pool -> pool.externalId == resourcePoolId }
			def doCreate = true
//			if(resourcePool.inventory == false) {
//				doCreate = false //granular inventory control
//			}
			if(blackListedNames?.contains(cloudItem.name)) {
				doCreate = false
			}
			// TODO : Handle workload limit
//			if(doCreate == true && !applianceLicenseService.getWorkloadUsage().limitReached) {
			if(doCreate == true) {
				def serverIps = VmwareComputeUtility.getServerIps(cloudItem.guest.net)
				if(!serverIps.ipAddress && cloudItem.guest.ipAddress) {
					serverIps.ipAddress = cloudItem.guest.ipAddress
				}
				def ipAddress = serverIps.ipAddress
				def hostname = cloudItem.guest.hostName

				// TODO : Handle folder
//				def folderId = cloudItem.parent?.getVal()
//				def folder = zoneFolders?.find{ folder -> folder.externalId == folderId }
				def vmConfig = [account:cloud.account, externalId:cloudItem.ref, name:cloudItem.name, externalIp:ipAddress,
				                internalIp:ipAddress, sshHost:ipAddress, sshUsername:'root', hostname:hostname, provision:false, computeServerType:defaultServerType,
				                cloud:cloud, lvmEnabled:false, managed:false, serverType:'vm', status:'provisioned',
				                resourcePool:resourcePool, uniqueId:vmUuid, internalId: cloudItem.config.instanceUuid] // folder: folder
				vmConfig.powerState = cloudItem.runtime.powerState == VirtualMachinePowerState.poweredOn ? ComputeServer.PowerState.on : (cloudItem.runtime.powerState == VirtualMachinePowerState.suspended ? ComputeServer.PowerState.paused : ComputeServer.PowerState.off)
				vmConfig.hotResize = (cloudItem.config.memoryHotAddEnabled)
				vmConfig.cpuHotResize = (cloudItem.config.cpuHotAddEnabled == true)
//				vmConfig.plan = fallbackPlan
				ComputeServer add = new ComputeServer(vmConfig)
				def maxStorage = 0
				def maxMemory = 0
				def usedStorage = 0
				def maxCores = 0
				def coresPerSocket
				def extraConfig = []
				if(cloudItem.summary) {
					usedStorage = cloudItem.summary?.storage?.committed ?: 0
					maxStorage = (usedStorage ?: 0) + (cloudItem.summary?.storage?.uncommitted ?: 0)
					maxCores = cloudItem.summary?.config?.numCpu ?: 0
					coresPerSocket = cloudItem.config.hardware.numCoresPerSocket ?: 1
					log.debug("coresPerSocket: {}", coresPerSocket)
					maxMemory = (cloudItem.summary?.config?.memorySizeMB ?: 0) * ComputeUtility.ONE_MEGABYTE  //vmware is not storing in Mibibytes, need old school megabytes
					extraConfig = cloudItem.config?.extraConfig
					add.maxMemory = maxMemory
					add.maxCores = maxCores
					add.coresPerSocket = coresPerSocket
					// TODO : Set the plan
//					add.plan = findServicePlanBySizing(availablePlans, add.maxMemory, add.maxCores, coresPerSocket, fallbackPlan,null,add.account,availablePlanPermissions)
					def osTypeCode = VmwareComputeUtility.getMapVmwareOsType(cloudItem.config.guestId)
					def osType = new OsType(code: osTypeCode ?: 'other')
					add.serverOs = osType
					add.osType = osType?.platform?.toLowerCase()
					if(add.osType == 'windows')
						add.sshUsername = 'Administrator'
					if(cloudItem.runtime?.host.getVal()) {
						def host = hosts.find{hst -> hst.externalId == cloudItem.runtime.host.getVal()}
						add.parentServer = host
					}
					add.toolsInstalled = cloudItem.guest?.toolsStatus != 'toolsNotInstalled'
					if(extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.enabled'}) {
						if(extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.enabled'}.value?.toLowerCase() == 'true') {
							add.consoleType = 'vnc'
							add.consoleHost = add.parentServer?.name
							add.consolePassword = extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.password'}?.value
							add.consolePort = extraConfig.find{option -> option.key.toLowerCase() == 'remotedisplay.vnc.port'}?.value?.toInteger()
						}
					}
					add.capacityInfo = new ComputeCapacityInfo(maxCores:maxCores, maxMemory:maxMemory, maxStorage:maxStorage, usedStorage:usedStorage)
					if(!morpheusContext.computeServer.create([add]).blockingGet()){
						log.error "Error in creating server ${add}"
					}


//					if(cloudItem.summary.config.annotation) {
//						computeService.saveNotes(add,cloudItem.summary.config.annotation,false,false)
//					}
//					//sync controllers
//					vmwareProvisionService.syncControllers(cloud, add, cloudItem.controllers, add.account)
//					//sync volumes
//					vmwareProvisionService.syncVolumes(cloud, add, cloudItem.volumes, add.account)
//					vmwareProvisionService.syncInterfaces(cloud, add, cloudItem.networks, serverIps.ipList, add.account,systemNetworks,netTypes)
				} else {
					add.capacityInfo = new ComputeCapacityInfo(maxCores: maxCores, maxMemory: maxMemory, maxStorage: maxStorage, usedStorage: usedStorage)
					if (!morpheusContext.computeServer.create([add]).blockingGet()) {
						log.error "Error in creating server ${add}"
					}
				}
//				syncSnapshotsForServer(add,cloudItem.snapshots,cloudItem.currentSnapshot)
//				if(add.consolePort) {
//					def computePort = new ComputePort(parentType:'ComputeZone', parentId:cloud.id, regionCode: cloud.regionCode, port:add.consolePort, portCount:1,
//							portType:'vnc', refType:'ComputeServer', refId:add.id)
//					computePort.save(flush:true)
//				}
//				addedServers << add
//				initializeServerUsage(add)
			}
		}
//		tagCompliancePolicyService.checkTagComplianceForServers(cloud,addedServers)

	}

	private getHostsForVirtualMachines(Cloud cloud, List cloudVMs) {
		log.debug "getHostsForVirtualMachines: ${cloud} ${cloudVMs?.size()}"
		def hostExternalIds = cloudVMs.collect{it.runtime?.host?.getVal()}.unique()
		def hostIdentities = []
		morpheusContext.computeServer.listSyncProjections(cloud.id).blockingSubscribe { hostIdentities << it.id }
		def hosts = []
		morpheusContext.computeServer.listById(hostIdentities).blockingSubscribe { ComputeServer it ->
			if(it.computeServerType.code == 'vmware-plugin-hypervisor' && it.externalId in hostExternalIds) {
				hosts << it
			}
		}
		hosts
	}

	private getResourcePoolsForVirtualMachines(Cloud cloud, List cloudVMs) {
		log.debug "getResourcePoolsForVirtualMachines: ${cloud} ${cloudVMs?.size()}"
		def resourcePoolExternalIds = cloudVMs.collect { it.resourcePool?.getVal() }.unique()
		def resourcePoolProjections = []
		morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').blockingSubscribe { resourcePoolProjections << it }
		def resourcePools = []
		morpheusContext.cloud.pool.listById(resourcePoolProjections.collect { it.id }).blockingSubscribe { ComputeZonePool it ->
			if (it.externalId in resourcePoolExternalIds) {
				resourcePools << it
			}
		}
		resourcePools
	}

	private getServicePlans(Cloud cloud) {
		log.debug "getServicePlans: ${cloud}"
		def servicePlanProjections = []
		morpheusContext.servicePlan.listSyncProjections(cloud.id).blockingSubscribe { servicePlanProjections << it }
		def plans = []
		morpheusContext.servicePlan.listById(servicePlanProjections.collect { it.id }).blockingSubscribe {
			// TODO : Use custom seeded service plans
			if(it.active == true && it.deleted != true && it.provisionTypeCode == 'vmware') {
				plans << it
			}
		}
		plans
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

	static listDatastores(Cloud cloud) {
		log.debug "listDatastores: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listDatastores(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
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

	String getRegionCode(Cloud cloud) {
		def datacenter = cloud?.getConfigProperty('datacenter')
		def apiUrl = VmwareProvisionProvider.getVmwareApiUrl(cloud.serviceUrl)
		def regionString = "${apiUrl}.${datacenter}"
		MessageDigest md = MessageDigest.getInstance("MD5")
		md.update(regionString.bytes)
		byte[] checksum = md.digest()
		return checksum.encodeHex().toString()
	}
}
