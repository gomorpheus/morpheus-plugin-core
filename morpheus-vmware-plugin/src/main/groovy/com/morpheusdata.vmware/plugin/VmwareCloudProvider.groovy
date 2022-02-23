package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

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
				fieldName: 'cluster',
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
			def apiUrlObj = new URL(authConfig.apiUrl)
			def apiHost = apiUrlObj.getHost()
			def apiPort = apiUrlObj.getPort() > 0 ? apiUrlObj.getPort() : (apiUrlObj?.getProtocol()?.toLowerCase() == 'https' ? 443 : 80)
			def hostOnline = ConnectionUtils.testHostConnectivity(apiHost, apiPort, true, true, null)
			log.debug("vmware online: {} - {}", apiHost, hostOnline)
			if(hostOnline) {
				def testResults = VmwareComputeUtility.testConnection(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword)
				if(testResults.success == true) {
					morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.syncing, null, syncDate)

//					cacheResourcePools(cloud).get()
//					cacheHosts(cloud).get()
//					cacheVirtualMachines([zone:zone, createNew:createNew,apiVersion: apiVersion])

					morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.ok, null, syncDate)
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

//	def cacheResourcePools(Cloud cloud) {
//		return Promises.task {
//			String clusterScope = opts.zone.getConfigProperty('cluster') as String
//			String clusterRef = opts.zone.getConfigProperty('clusterRef') as String
//			List clusters = []
//			def clsResults = listComputeResources(opts)
//			Boolean success = clsResults.success
//			def clusterResults = clsResults.computeResorces
//			success = clusterResults.success
//			if(clusterScope) {
//				clusters = clusterResults.findAll{it.name == clusterScope}
//				if(!clusters && clusterRef) {
//					clusters = clusterResults.findAll{it.ref == clusterRef}
//				}
//				if(clusters && (clusterRef != clusters?.first()?.ref || clusterScope != clusters?.first()?.name)) {
//					//fix zone config
//					Promises.task {
//						ComputeZone.withNewTransaction {
//							ComputeZone tmpZone = ComputeZone.get(opts.zone.id)
//							tmpZone.setConfigProperty('cluster', clusters.first().name)
//							tmpZone.setConfigProperty('clusterRef', clusters.first().ref)
//							tmpZone.save(flush:true)
//						}
//					}
//				}
//			} else {
//				clusters = clusterResults
//			}
//			Map poolResults = [:]
//			if(success) {
//				for (cluster in clusters) {
//					poolResults[cluster.name] = listResourcePools(opts + [cluster:cluster.name])
//				}
//			}
//
//			return [success: success,clusterScope: clusterScope,poolResults: poolResults, clusters: clusters.collectEntries{ [(it.name): it.ref]}]
//		}.then { clusterResults ->
//			Map<String,Object> poolResults = clusterResults.poolResults as Map<String,Object>
//			List existingItems = []
//			Map<String,String> oldClusterNamesByRef = [:]
//			if(!poolResults.isEmpty()) {
//
//				ComputeZonePool.withNewSession { session ->
//					existingItems = ComputeZonePool.withCriteria {
//						ne('type','Datacenter')
//						eq('refType', 'ComputeZone')
//						eq('refId', opts.zone.id)
//						or {
//							isNull('internalId')
//							inList('internalId', poolResults.keySet())
//						}
//						projections {
//							property('internalId')
//							property('externalId')
//						}
//					}
//
//					oldClusterNamesByRef = ComputeZonePool.withCriteria {
//						eq('type','Cluster')
//						eq('refType', 'ComputeZone')
//						eq('refId', opts.zone.id)
//
//						projections {
//							property('name')
//							property('uniqueId')
//						}
//					}?.collectEntries { [(it[1]):it[0]] } as Map<String,String>
//
//				}
//
//			}
//
//			return [existingItems:existingItems,poolResults:poolResults, clusters: clusterResults.clusters, existingClusterNames: oldClusterNamesByRef]
//		}.then { queryResults ->
//			def existingItems = queryResults.existingItems
//			def syncLists =[:]
//			Map poolResults = queryResults.poolResults as Map
//			Closure matchFunction = { morpheusItem, cloudItem ->
//				morpheusItem[1] == cloudItem.ref
//			}
//			for(clusterName in poolResults.keySet()) {
//				def listResults = poolResults[clusterName]
//				if(listResults.success) {
//					String originalClusterName = queryResults.existingClusterNames[queryResults.clusters[clusterName]]
//					def clusterExistingItems = existingItems.findAll{it[0] == clusterName || it[0] == null || it[0] == originalClusterName}
//					if(listResults.success == true) {
//						syncLists[clusterName] = ComputeUtility.buildSyncLists(clusterExistingItems, listResults.resourcePools, matchFunction)
//					}
//				}
//
//			}
//
//			return [syncLists: syncLists, clusters:queryResults.clusters]
//		}.then { clusteredSyncLists ->
//			ComputeZonePool.withNewSession { session ->
//				for (String clusterName in clusteredSyncLists.syncLists.keySet()) {
//					def syncLists = clusteredSyncLists.syncLists[clusterName]
//					while (syncLists.addList?.size() > 0) {
//						List chunkedAddList = syncLists.addList.take(50)
//						syncLists.addList = syncLists.addList.drop(50)
//						addMissingResourcePools(opts.zone as ComputeZone, clusterName,clusteredSyncLists.clusters[clusterName] as String, chunkedAddList)
//					}
//				}
//			}
//			return clusteredSyncLists
//		}.then { clusteredSyncLists ->
//			//update list
//			ComputeZonePool.withNewSession { session ->
//				for (String clusterName in clusteredSyncLists.syncLists.keySet()) {
//					def syncLists = clusteredSyncLists.syncLists[clusterName]
//					while (syncLists.updateList?.size() > 0) {
//						List chunkedUpdateList = syncLists.updateList.take(50)
//						syncLists.updateList = syncLists.updateList.drop(50)
//						def nameChanges = updateMatchedResourcePools(opts.zone as ComputeZone,clusterName,clusteredSyncLists.clusters[clusterName] as String, chunkedUpdateList)
//						if(nameChanges) {
//							propagateResourcePoolTreeNameChanges(nameChanges)
//						}
//					}
//				}
//			}
//			return clusteredSyncLists
//		}.then { clusteredSyncLists ->
//			ComputeZonePool.withNewSession { session ->
//				for (String clusterName in clusteredSyncLists.syncLists.keySet()) {
//					def syncLists = clusteredSyncLists.syncLists[clusterName]
//					while(syncLists.removeList?.size() > 0) {
//						List chunkedRemoveList = syncLists.removeList.take(50)
//						syncLists.removeList = syncLists.removeList.drop(50)
//						removeMissingResourcePools(opts.zone as ComputeZone,clusterName,clusteredSyncLists.clusters[clusterName] as String, chunkedRemoveList)
//					}
//				}
//				if(opts.zone.owner.masterAccount == false) {
//					zonePoolService.chooseOwnerPoolDefaults(opts.zone.owner, opts.zone)
//				}
//			}
//			return clusteredSyncLists
//		}.then { clusteredSyncLists ->
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
//			return ServiceResponse.success()
//		}.onError { Exception ex ->
//			log.error("Error Caching Resource Pools in Vmware Cloud {} - {}",opts.zone.id,ex.message,ex)
//			return ServiceResponse.error(ex.message)
//		}
//
//	}
//
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected addMissingResourcePools(ComputeZone zone, String clusterName, String clusterRef, List addList) {
//
//		for(cloudItem in addList) {
//			def poolConfig = [owner:zone.owner, name:cloudItem.name, externalId:cloudItem.ref, internalId: clusterName, refType:'ComputeZone', refId:zone.id,
//			                  zone:zone, category:"vmware.vsphere.resourcepool.${zone.id}", code:"vmware.vsphere.resourcepool.${zone.id}.${cloudItem.ref}",
//			                  readOnly:cloudItem.readOnly]
//			def add = new ComputeZonePool(poolConfig)
//			if(cloudItem.parentType != 'ClusterComputeResource') {
//				def parentPool = ComputeZonePool.findByZoneAndExternalId(zone,cloudItem.parentId)
//				add.parent = parentPool
//				add.treeName = nameForPool(add)
//			} else {
//				add.type = 'Cluster'
//				add.name = clusterName
//				add.treeName = clusterName
//				add.uniqueId = clusterRef
//			}
//			add.save(flush:true)
//			def resourcePerm = new ResourcePermission(morpheusResourceType:'ComputeZonePool', morpheusResourceId:add.id, account:zone.account)
//			resourcePerm.save(flush:true)
//		}
//	}
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected updateMatchedResourcePools(ComputeZone currentZone, String clusterName, String clusterRef, List updateList) {
//		def matchedResourcePools = ComputeZonePool.where{zone.id == currentZone.id && (internalId == clusterName || internalId == null) && externalId in updateList.collect{ul -> ul.existingItem[1]}}.list()?.collectEntries{[(it.externalId):it]}
//		List<Long> propagateTreeNameChanges = []
//		for(update in updateList) {
//			def existingStore = matchedResourcePools[update.existingItem[1]]
//			if (existingStore) {
//				Boolean save = false
//				def matchItem = update.masterItem
//				def name = matchItem.name
//
//				if(existingStore && existingStore.readOnly != matchItem.readOnly) {
//					existingStore.readOnly = matchItem.readOnly
//					save = true
//				}
//				if(existingStore.internalId != clusterName) {
//					existingStore.internalId = clusterName
//					save = true
//					if(existingStore.parent == null && existingStore.type != 'Cluster') {
//						existingStore.type = 'Cluster'
//					}
//				}
//
//				if(matchItem.parentType == 'ClusterComputeResource' && existingStore.uniqueId == null) {
//					existingStore.uniqueId = clusterRef
//					save = true
//				}
//
//				if(existingStore && existingStore.parent?.externalId != matchItem.parentId) {
//					if(matchItem.parentType == 'ResourcePool') {
//						def parentPool = ComputeZonePool.findByZoneAndExternalId(currentZone,matchItem.parentId)
//						existingStore.parent = parentPool
//					} else {
//						existingStore.parent = null
//						existingStore.type = 'Cluster'
//					}
//					existingStore.treeName = nameForPool(existingStore)
//					propagateTreeNameChanges << existingStore.id
//					save = true
//				}
//				if(existingStore.parent == null && existingStore.name != clusterName) {
//					existingStore.name = clusterName
//					existingStore.treeName = clusterName
//					propagateTreeNameChanges << existingStore.id
//					save = true
//				} else if (existingStore.parent != null && existingStore.name != name) {
//					existingStore.name = name
//					existingStore.treeName = nameForPool(existingStore)
//					propagateTreeNameChanges << existingStore.id
//					save = true
//				}
//				if(save) {
//					existingStore.save()
//
//				}
//			}
//		}
//		return propagateTreeNameChanges?.unique()
//	}
//
//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	protected removeMissingResourcePools(ComputeZone currentZone, String clusterName, String clusterRef, List removeList) {
//		def removeItems = ComputeZonePool.where{zone.id == currentZone.id && (internalId == clusterName || internalId == null) && externalId in removeList.collect{it[1]}}.list()
//		removeItems?.toArray()?.each { removeItem ->
//			log.info("Removing Pool ${removeItem.name}")
//			removeItems.findAll{ existing -> existing.parent?.id == removeItem.id}.each { prnt ->
//				prnt.parent = null
//				prnt.save(flush:true)
//			}
//			zonePoolService.internalDeleteComputeZonePool(removeItem)
//		}
//	}

	static listComputeResources(Cloud cloud) {
		log.debug "listComputeResources: ${cloud}"
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		rtn = VmwareComputeUtility.listComputeResources(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter])
		return rtn
	}

	def listDatacenters(Cloud cloud) {
		log.debug "listDatacenters: ${cloud}"
		def rtn = [success:false]
		def authConfig = vmwareProvisionService.getAuthConfig(cloud)
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

	static listResourcePools(Cloud cloud) {
		def rtn = [success:false]
		def authConfig = VmwareProvisionProvider.getAuthConfig(cloud)
		def datacenter = cloud?.getConfigProperty('datacenter')
		rtn = VmwareComputeUtility.listResourcePools(authConfig.apiUrl, authConfig.apiUsername, authConfig.apiPassword, [datacenter:datacenter])
		return rtn
	}
}
