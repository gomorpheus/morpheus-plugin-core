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
import com.morpheusdata.model.NetworkSubnetType
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.OsType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.projection.*
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.vmware.plugin.utils.*
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
		def vmwareNetworkConfig = [
				code: 'vmware-plugin-network',
				externalType: 'Network',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Network'
		]
		NetworkType vmwareNetwork = new NetworkType(vmwareNetworkConfig)

		def subnetConfig = [
				code: 'esxi-plugin-subnet',
				dhcpServerEditable: true,
				canAssignPool: true,
				cidrRequired: true,
				cidrEditable: true,
				name: 'Esxi Plugin Subnet'
		]
		NetworkSubnetType esxiSubnet = new NetworkSubnetType(subnetConfig)
		vmwareNetwork.setNetworkSubnetTypes([esxiSubnet])

		def vmwareDistributedSwitchConfig = [
				code: 'vmware-plugin-distributed-switch',
				externalType: 'DistributedSwitch',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Distributed Switch'
		]
		NetworkType vmwareDistributedSwitch = new NetworkType(vmwareDistributedSwitchConfig)

		def vmwareDistributedConfig = [
				code: 'vmware-plugin-distributed',
				externalType: 'DistributedVirtualPortgroup',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Distributed Switch Group'
		]
		NetworkType vmwareDistributed = new NetworkType(vmwareDistributedConfig)

		def vmwareOpaqueConfig = [
				code: 'vmware-plugin-opaque',
				externalType: 'OpaqueNetwork',
				cidrEditable:true,
				dhcpServerEditable:true,
				dnsEditable:true,
				gatewayEditable:true,
				vlanIdEditable:true,
				canAssignPool:true,
				name: 'VMware Plugin Opaque Network'
		]
		NetworkType vmwareOpaque = new NetworkType(vmwareOpaqueConfig)

		return [vmwareNetwork, vmwareDistributedSwitch, vmwareDistributed, vmwareOpaque]
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
	Boolean hasComputeZonePools() {
		return true
	}

	@Override
	Boolean hasNetworks() {
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
					log.debug("resource pools completed in ${new Date().time - now.time} ms")
					(new FoldersSync(cloud, morpheusContext)).execute()
					(new DatastoresSync(cloud, morpheusContext)).execute()
//					//fix region codes?
//					fixMissingRegionCodes(zone, getRegionCode(zone))
					(new TemplatesSync(cloud, morpheusContext)).execute()
//					cacheContentLibraryItems([zone:zone,proxySettings:proxySettings]) //dont pause for this right now
					(new NetworksSync(cloud, morpheusContext, getNetworkTypes())).execute()
					(new HostsSync(cloud, morpheusContext)).execute()
					(new StoragePodsSync(cloud, morpheusContext)).execute()
					(new IPPoolsSync(cloud, morpheusContext)).execute()
					(new CustomSpecSync(cloud, morpheusContext)).execute()
					(new AlarmsSync(cloud, morpheusContext)).execute()
//					cacheEvents([zone:zone]).get() // TODO : OperationEvents don't seem to be used.. skipping
					(new DatacentersSync(cloud, morpheusContext)).execute()
//					//vms
//					if(apiVersion && apiVersion != '6.0') {
//						now = new Date()
//						cacheCategories([zone:zone,proxySettings:proxySettings])
//						cacheTags([zone:zone,proxySettings:proxySettings])
//					}
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
				morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
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
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').filter { ComputeZonePoolIdentityProjection projection ->
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
				if(parentPool) {
					add.parent = new ComputeZonePool(id: parentPool.id)
				}
				add.type = 'default'
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
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listDatacenters(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, getApiOptions(cloud))
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

	static listIpPools(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listIpPools(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listNetworks(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listNetworks(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listCustomizationSpecs(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		def cluster = cloud?.getConfigProperty('cluster')
		rtn = VmwareComputeUtility.listCustomizationSpecs(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter, cluster:cluster])
		return rtn
	}

	static listAlarms(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		rtn = VmwareComputeUtility.listAlarms(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, getApiOptions(cloud))
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

	static getApiOptions(Cloud cloud, Map opts = [:]) {
		def rtn = [:]
		rtn.datacenter = cloud?.getConfigProperty('datacenter')
		rtn.cluster = opts.cluster ?: cloud?.getConfigProperty('cluster')
		rtn.resourcePool = cloud?.getConfigProperty('resourcePoolId')
		rtn.proxySettings = opts.proxySettings
		return rtn
	}

	static findManagedObject(morpheusContext, Cloud cloud, String type, String id) {
		//find the matching item in our db
		def rtn = [refType:null, refId:null]
		if(type && id) {
			def assignToZone = false
			switch(type) {
				case 'StoragePod':
					def match
					morpheusContext.cloud.datastore.listSyncProjections(cloud.id)
						.filter { it.externalId == id && it.type == 'cluster' }
						.blockingSubscribe { match == it }
					if(match) {
						rtn.refType = 'datastore'
						rtn.refId = match.id
					} else {
						assignToZone = true
					}
					break
				case 'Datastore':
					def match
					morpheusContext.cloud.datastore.listSyncProjections(cloud.id)
							.filter { it.externalId == id && it.type != 'cluster' }
							.blockingSubscribe { match == it }
					if(match) {
						rtn.refType = 'datastore'
						rtn.refId = match.id
					} else {
						assignToZone = true
					}
					break
				case 'VirtualMachine':
				case 'HostSystem':
					def match
					morpheusContext.computeServer.listSyncProjections(cloud.id)
							.filter { it.externalId == id }
							.blockingSubscribe { match == it }
					if(match) {
						rtn.refType = 'computeServer'
						rtn.refId = match.id
					} else {
						assignToZone = true
					}
					break
			}
			//fallback to zone
			if(assignToZone) {
				rtn.refType = 'computeZone'
				rtn.refId = cloud.id
			}
		}
		return rtn
	}

	VmwareProvisionProvider vmwareProvisionProvider() {
		this.plugin.getProviderByCode('vmware-provision-provider-plugin')
	}
}
