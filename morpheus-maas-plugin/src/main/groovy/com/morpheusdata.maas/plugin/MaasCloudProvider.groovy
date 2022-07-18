package com.morpheusdata.maas.plugin

import com.morpheusdata.core.backup.BackupProvider;
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.AccountCredentialType
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Icon
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.ReferenceData
import com.morpheusdata.model.StorageControllerType
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.StorageVolumeType
import com.morpheusdata.model.projection.ComputeServerIdentityProjection
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.projection.NetworkIdentityProjection
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import com.morpheusdata.request.ValidateCloudRequest
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j
import io.reactivex.Observable

@Slf4j
class MaasCloudProvider implements CloudProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	MaasCloudProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		//MaaS
		OptionType serviceUrl = new OptionType(
				name: 'MaaS Api Url',
				code: 'maas-service-url',
				fieldName: 'serviceUrl',
				displayOrder: 0,
				fieldLabel: 'MaaS Api Url',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType serviceToken = new OptionType(
				name: 'API Key',
				code: 'maas-service-token',
				fieldName: 'serviceToken',
				displayOrder: 1,
				fieldLabel: 'API Key',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType availablePool = new OptionType(
				name: 'Available Pool',
				code: 'maas-resource-pool',
				fieldName: 'resourcePoolId',
				optionSource: 'maasPluginResourcePools',
				displayOrder: 2,
				fieldLabel: 'Available Pool',
				required: false,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'maas-service-url,maas-service-token',
				fieldContext: 'config'
		)

		OptionType releaseMode = new OptionType(
				name: 'Release Mode',
				code: 'maas-release-mode',
				fieldName: 'releaseMode',
				optionSource: 'maasPluginReleaseModes',
				displayOrder: 3,
				fieldLabel: 'Release Mode',
				required: true,
				inputType: OptionType.InputType.SELECT,
				fieldContext: 'config'
		)

		OptionType releasePoolName = new OptionType(
				name: 'Release Pool',
				code: 'maas-release-pool',
				fieldName: 'releasePoolName',
				optionSource: 'maasPluginResourcePools',
				displayOrder: 4,
				fieldLabel: 'Release Pool',
				required: false,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'maas-service-url,maas-service-token',
				fieldContext: 'config'
		)
		[serviceUrl, serviceToken, availablePool, releasePoolName, releaseMode]
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		ComputeServerType maasType = new ComputeServerType()
		maasType.name = 'Plugin Maas Server'
		maasType.code = 'maas-metal'
		maasType.description = 'maas server'
		maasType.platform = PlatformType.none
		maasType.bareMetalHost = true
		maasType.managed = false
		return [maasType]
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
	Collection<StorageVolumeType> getStorageVolumeTypes() {
		return null
	}

	@Override
	Collection<StorageControllerType> getStorageControllerTypes() {
		return null
	}

	@Override
	ServiceResponse validate(Cloud cloudInfo, ValidateCloudRequest validateCloudRequest) {
		log.info("MaaS validate")
		return new ServiceResponse(success: true)
	}

	@Override
	ServiceResponse refresh(Cloud cloudInfo) {
		initializeCloud(cloudInfo)
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {

	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
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
	Icon getIcon() {
		return null
	}

	@Override
	String getCode() {
		return 'maas-cloud'
	}

	@Override
	String getName() {
		return 'Metal as a Service'
	}

	@Override
	String getDescription() {
		return 'Metal as a Service Description'
	}

	@Override
	Boolean hasComputeZonePools() {
		return true
	}

	@Override
	Boolean hasNetworks() {
		return false
	}

	@Override
	Boolean hasFolders() {
		return false
	}

	@Override
	Boolean hasDatastores() {
		return false
	}

	@Override
	Boolean hasCloudInit() {
		false
	}

	@Override
	Boolean supportsDistributedWorker() {
		false
	}

	@Override
	ServiceResponse startServer(ComputeServer computeServer) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse deleteServer(ComputeServer computeServer) {
		log.debug "deleteServer: ${computeServer}.. looking for 'maas-provision-provider-plugin' provisionProvider"
		return getProvisioningProvider('maas-provision-provider-plugin').removeServer(computeServer)
	}

	@Override
	ServiceResponse initializeCloud(Cloud cloud) {
		ServiceResponse rtn = new ServiceResponse(success: false)
		log.info "Initializing Cloud: ${cloud.code}"
		log.info "config: ${cloud.configMap}"
		try {
			def syncDate = new Date()
			String apiUrl = cloud.serviceUrl
			def apiUrlObj = new URL(apiUrl)
			def apiHost = apiUrlObj.getHost()
			def apiPort = apiUrlObj.getPort() > 0 ? apiUrlObj.getPort() : (apiUrlObj?.getProtocol()?.toLowerCase() == 'https' ? 443 : 80)
			log.info("apiHost: ${apiHost}, port: ${apiPort}")
//			def hostOnline = ConnectionUtils.testHostConnection(apiHost, apiPort, true, true, cloud.apiProxy)
			def hostOnline = true
			log.info("hostOnline: $hostOnline")
			if (hostOnline) {
//				cloud.serviceUrl = apiUrl
//				cloud.serviceToken = cloud.configMap.serviceToken
				def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
				def testResults = MaasComputeUtility.testConnection(authConfig, [:])
				if (testResults.success) {
					//cache stuff
					def cacheOpts = [:]
					cacheRegionControllers(cloud, cacheOpts)
					cacheRackControllers(cloud, cacheOpts)
					cacheResourcePools(cloud, cacheOpts)
					cacheMachines(cloud, cacheOpts)
					cacheImages(cloud, cacheOpts)
//					cacheFabrics(cloud, cacheOpts)
					cacheSubnets(cloud, cacheOpts)
					rtn = ServiceResponse.success()
				} else {
					rtn = ServiceResponse.error(testResults.invalidLogin == true ? 'invalid credentials' : 'error connecting')
				}
			} else {
				rtn = ServiceResponse.error('maas is not reachable', null, [status: Cloud.Status.offline])
			}
		} catch (e) {
			log.error("refresh zone error: ${e}", e)
		}
		return rtn
	}

	protected def cacheReferenceData(Cloud cloud, String category, List<Map> apiItems) {
		log.debug "cacheReferenceData: ${cloud} ${category} ${apiItems}"
		try {
			Observable<ReferenceDataSyncProjection> domainRecords = morpheusContext.cloud.listReferenceDataByCategory(cloud, category)
			SyncTask<ReferenceDataSyncProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, apiItems)
			syncTask.addMatchFunction { ReferenceDataSyncProjection domainObject, Map apiItem ->
				domainObject.externalId == apiItem.system_id && domainObject.name == apiItem.hostname
			}.onDelete { removeItems ->
				log.debug "onDelete ${removeItems}"
				morpheus.cloud.remove(removeItems).blockingGet()
			}.onAdd { itemsToAdd ->
				log.debug "onAdd ${itemsToAdd}"
				while (itemsToAdd?.size() > 0) {
					List chunkedAddList = itemsToAdd.take(50)
					itemsToAdd = itemsToAdd.drop(50)
					addMissingReferenceData(category, cloud, chunkedAddList)
				}
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
				morpheus.cloud.listReferenceDataById(updateItems.collect { it.existingItem.id } as List<Long>).map {ReferenceDataSyncProjection referenceData ->
					SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map> matchItem = updateItemMap[referenceData.id]
					return new SyncTask.UpdateItem<ReferenceDataSyncProjection,Map>(existingItem:referenceData, masterItem:matchItem.masterItem)
				}
			}.onUpdate { List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems ->
				updateMatchedReferenceData(category, cloud, updateItems)
			}.start()

		} catch(e) {
			log.error("cacheRegionControllers error: ${e}", e)
		}
	}

	def cacheRegionControllers(Cloud cloud, Map opts) {
		log.debug "cacheRegionControllers: ${cloud}"
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		String category = "maas.regioncontroller.${cloud.id}"
		def apiResponse = MaasComputeUtility.listRegionControllers(authConfig, opts)
		if (apiResponse.success) {
			List apiItems = apiResponse.data as List<Map>
			log.info("region controllers to cache: $apiItems")
			cacheReferenceData(cloud, category, apiItems)
		}
	}

	void updateMatchedReferenceData(String category, Cloud cloud, List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems) {
		log.debug "updateMatchedReferenceData: ${updateItems} ${cloud} ${category}"
		List<ReferenceData> itemsToSave = []
		for(item in updateItems) {
			def existingItem = item.existingItem
			def name = item.masterItem.hostname
			def code = "$category.${item.masterItem.system_id}"
			def keyValue = item.masterItem.system_id
			def value = item.masterItem.fqdn
			def externalId = item.masterItem.system_id
			if(existingItem.name != name || existingItem.code != code || existingItem.keyValue != keyValue
					|| existingItem.value != value|| existingItem.externalId != externalId) {
				item.existingItem.name = item.masterItem.name
				item.existingItem.code = item.masterItem.code
				item.existingItem.keyValue = item.masterItem.keyValue
				item.existingItem.value = item.masterItem.value
				item.existingItem.externalId = item.masterItem.externalId
				itemsToSave.add(item.existingItem)
			}
		}
		morpheusContext.cloud.save(itemsToSave, cloud, category).blockingGet()

	}

	void addMissingReferenceData(String category, Cloud cloud, List<Map> list) {
		List<ReferenceData> records = []
		for(item in list) {
			ReferenceData referenceData = new ReferenceData(
					name: item.hostname,
					code: "$category.${item.system_id}",
					keyValue: item.system_id,
					value: item.fqdn,
					externalId: item.system_id,
					type: 'string'
			)
			records.add(referenceData)
		}
		morpheusContext.cloud.create(records, cloud, category).blockingGet()
	}

	def cacheRackControllers(Cloud cloud, Map opts) {
		log.info('cacheRackControllers')
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		String category = "maas.rackcontroller.${cloud.id}"
		def apiResponse = MaasComputeUtility.listRackControllers(authConfig, opts)
		if (apiResponse.success) {
			List apiItems = apiResponse.data as List<Map>
			cacheReferenceData(cloud, category, apiItems)
		}
	}

	def cacheImages(Cloud cloud, Map opts) {
		log.info('cacheImages')
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)

		def osTypes = []
		morpheusContext.osType.listAll().blockingSubscribe { osTypes << it }

		def apiResponse = MaasComputeUtility.listImages(authConfig, opts)
		if(apiResponse.success) {
			try {
				List apiItems = apiResponse?.data as List<Map>
				Observable<VirtualImageIdentityProjection> domainRecords = morpheus.virtualImage.listSyncProjections(cloud.id)
				SyncTask<VirtualImageIdentityProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, apiItems)
				syncTask.addMatchFunction { VirtualImageIdentityProjection domainObject, Map apiItem ->
					domainObject.externalId?.toString() == "${apiItem.name}/${apiItem.id}"
				}.onDelete { removeItems ->
					morpheus.virtualImage.remove(removeItems).blockingGet()
				}.onAdd { itemsToAdd ->
					while (itemsToAdd?.size() > 0) {
						List<Map> chunkedAddList = itemsToAdd.take(50)
						itemsToAdd = itemsToAdd.drop(50)
						List<VirtualImage> itemsToSave = []
						for(cloudImage in chunkedAddList) {
							itemsToSave.add(MaasComputeUtility.bootImageToVirtualImage(cloud, cloudImage, osTypes))
						}
						morpheus.virtualImage.create(itemsToSave, cloud).blockingGet()
					}
				}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map>> updateItems ->

					Map<Long, SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheus.virtualImage.listById(updateItems?.collect{ it.existingItem.id}).map {VirtualImage virtualImage ->
						SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map> matchItem = updateItemMap[virtualImage.id]
						return new SyncTask.UpdateItem<VirtualImage,Map>(existingItem:virtualImage, masterItem:matchItem.masterItem)
					}

				}.onUpdate { List<SyncTask.UpdateItem<VirtualImage, Map>> updateItems ->
					List<VirtualImage> toSave = []
					try {
						for (item in updateItems) {
							VirtualImage virtualImage = MaasComputeUtility.bootImageToVirtualImage(cloud, item.masterItem, osTypes)
							virtualImage.id = item.existingItem.id
							def existing = item.existingItem
							if (virtualImage.category != existing.category || virtualImage.code != existing.code
									|| virtualImage.name != existing.name || virtualImage.imageType != existing.imageType
									|| virtualImage.externalId != existing.externalId || virtualImage.osType?.id != existing.osType?.id
									|| virtualImage.platform != existing.platform) {
								toSave.add(virtualImage)
							}
						}
						morpheus.virtualImage.save(toSave, cloud).blockingGet()
					} catch(e) {
						log.error "Error on update virtualImage: ${e}", e
					}
				}.start()
			} catch(e) {
				log.error("cacheImages error: ${e}", e)
			}
		}

	}

	def cacheResourcePools(Cloud cloud, Map opts) {
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		String category = "maas.resourcepool.${cloud.id}"
		def apiResponse = MaasComputeUtility.listResourcePools(authConfig, opts)
		if (apiResponse.success) {
			List apiItems = apiResponse.data as List<Map>
			log.info("resource pools to cache: $apiItems")

			def configMap = cloud.getConfigMap()
			def poolId = configMap.resourcePoolId != null ? configMap.resourcePoolId as Integer : configMap.resourcePoolId
			def releaseName = configMap.releasePoolName
			def releaseMatchId = (releaseName != null && releaseName != '') ? "${releaseName}" : null
			def poolMatchId = (poolId != null && poolId != '') ? "${poolId}" : null //string incase

			Observable<ComputeZonePoolIdentityProjection> domainRecords = morpheus.cloud.pool.listSyncProjections(cloud.id, category)
			SyncTask<ComputeZonePoolIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, apiItems)
			syncTask.addMatchFunction { ComputeZonePoolIdentityProjection domainObject, Map apiItem ->
				domainObject.externalId?.toString() == apiItem.id?.toString()
			}.onDelete { removeItems ->
				morpheus.cloud.pool.remove(removeItems).blockingGet()
			}.onAdd { itemsToAdd ->
				while (itemsToAdd?.size() > 0) {
					List<Map> chunkedAddList = itemsToAdd.take(50)
					itemsToAdd = itemsToAdd.drop(50)
					List<ComputeZonePool> itemsToSave = []
					for(resourcePool in chunkedAddList) {
						itemsToSave.add(MaasComputeUtility.resourcePoolToComputeZonePool(resourcePool, cloud, category, poolMatchId, releaseMatchId))
					}
					morpheus.cloud.pool.create(itemsToSave).blockingGet()
				}
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItems ->

				Map<Long, SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
				morpheus.cloud.pool.listById(updateItems?.collect{ it.existingItem.id}).map {ComputeZonePool pool ->
					SyncTask.UpdateItemDto<ComputeZonePoolIdentityProjection, Map> matchItem = updateItemMap[pool.id]
					return new SyncTask.UpdateItem<ComputeZonePool,Map>(existingItem:pool, masterItem:matchItem.masterItem)
				}

			}.onUpdate { List<SyncTask.UpdateItem<ComputeZonePool, Map>> updateItems ->
				List<ComputeZonePool> toSave = []
				for(item in updateItems) {
					ComputeZonePool computeZonePool = MaasComputeUtility.resourcePoolToComputeZonePool(item.masterItem, cloud, category, poolMatchId, releaseMatchId)
					def existing = item.existingItem
					computeZonePool.id = item.existingItem.id
					if(computeZonePool.name != existing.name) {
						toSave.add(computeZonePool)
					} else if(computeZonePool.readOnly != existing.readOnly) {
						toSave.add(computeZonePool)
					}
				}
				if(toSave) {
					morpheus.cloud.pool.save(toSave).blockingGet()
				}
			}.start()
		}
	}

	def cacheMachines(Cloud cloud, Map opts) {
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		def apiResponse = MaasComputeUtility.listMachines(authConfig, opts)
		if (apiResponse.success) {
			List tmpApiItems = apiResponse.data as List<Map>

			def configMap = cloud.getConfigMap()
			def releaseName = configMap.releasePoolName
			def releaseMatchId = (releaseName != null && releaseName != '') ? "${releaseName}" : null

			// Fetch the pools
			def poolListProjections = []
			morpheusContext.cloud.pool.listSyncProjections(cloud.id, '').blockingSubscribe { poolListProjections << it }
			def poolList = []
			morpheusContext.cloud.pool.listById(poolListProjections.collect { it.id }).blockingSubscribe { poolList << it }

			// Fetch the plans
			def servicePlanProjections = []
			morpheusContext.servicePlan.listSyncProjections(cloud.id).blockingSubscribe { servicePlanProjections << it }
			def typePlans = []
			morpheusContext.servicePlan.listById(servicePlanProjections.collect { it.id }).blockingSubscribe {
				if(it.tagMatch != null) {
					typePlans << it
				}
			}

			def apiItems = []
			for(Map apiItem in tmpApiItems) {
				// Filter machines based on the pool configured in the cloud settings
				def poolMatch = apiItem?.pool?.id != null ? poolList?.find {
					it.externalId == "${apiItem.pool.id}"
				} : null
				def syncMachine = false
				if (poolMatch && poolMatch.readOnly == false)
					syncMachine = true
				else if (releaseMatchId && poolMatch && poolMatch.externalId == releaseMatchId)
					syncMachine = true
				if (syncMachine) {
					apiItems << apiItem
				}
			}

			log.info("machines to cache: ${apiItems.size()}")
			Observable<ComputeServerIdentityProjection> domainRecords = morpheus.computeServer.listSyncProjections(cloud.id)
			SyncTask<ComputeServerIdentityProjection, Map, ComputeServer> syncTask = new SyncTask<>(domainRecords, apiItems)
			syncTask.addMatchFunction { ComputeServerIdentityProjection domainObject, Map apiItem ->
				domainObject.externalId == apiItem.system_id
			}.onDelete { removeItems ->
				morpheus.computeServer.remove(removeItems).blockingGet()
			}.onAdd { itemsToAdd ->
				while (itemsToAdd?.size() > 0) {
					List<Map> chunkedAddList = itemsToAdd.take(50)
					itemsToAdd = itemsToAdd.drop(50)
					List<ComputeServer> itemsToSave = []
					for(maasMachine in chunkedAddList) {
						def poolMatch = maasMachine?.pool?.id != null ? poolList?.find{ it.externalId == "${maasMachine.pool.id}" } : null
						itemsToSave.add(MaasComputeUtility.configureComputeServer(maasMachine, null, cloud, poolMatch, typePlans))
					}
					morpheus.computeServer.create(itemsToSave).blockingGet()
				}
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItems ->
				Map<Long, SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
				morpheus.computeServer.listById(updateItems?.collect{ it.existingItem.id}).map {ComputeServer server ->
					SyncTask.UpdateItemDto<ComputeServerIdentityProjection, Map> matchItem = updateItemMap[server.id]
					return new SyncTask.UpdateItem<ComputeServer,Map>(existingItem:server, masterItem:matchItem.masterItem)
				}
			}.onUpdate { List<SyncTask.UpdateItem<ComputeServer, Map>> updateItems ->
				List<ComputeServer> toSave = []
				for(item in updateItems) {
					def poolMatch = item.masterItem.pool?.id != null ? poolList?.find{ it.externalId == "${item.masterItem.pool.id}" } : null
					ComputeServer machine = MaasComputeUtility.configureComputeServer(item.masterItem, item.existingItem, cloud, poolMatch, typePlans)
					machine.id = item.existingItem.id
					if(machine.getDirtyProperties()) {
						toSave.add(machine)
					}
				}
				morpheus.computeServer.save(toSave).blockingGet()
			}.start()
		}
	}

	def cacheSubnets(Cloud cloud, Map opts) {
		log.debug "cacheSubnets ${cloud}"
		try {
			def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
			def listResults = MaasComputeUtility.listSubnets(authConfig, opts)
			if(listResults.success == true) {
				List<Map> apiItems = listResults?.data
				Observable<NetworkIdentityProjection> networkObservable = morpheusContext.network.listIdentityProjections(cloud)

				SyncTask<NetworkIdentityProjection,Map, Network> syncTask = new SyncTask<>(networkObservable,apiItems)

				syncTask.addMatchFunction { NetworkIdentityProjection networkIdentity, Map cloudItem ->
					networkIdentity?.externalId == "${cloudItem.id}"
				}.withLoadObjectDetails { updateItems ->
					Map<Long, SyncTask.UpdateItemDto<NetworkIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
					morpheus.network.listById(updateItems?.collect{ it.existingItem.id}).map {Network network ->
						SyncTask.UpdateItemDto<NetworkIdentityProjection, Map> matchItem = updateItemMap[network.id]
						return new SyncTask.UpdateItem<Network,Map>(existingItem:network, masterItem:matchItem.masterItem)
					}
				}.onAdd { addItems ->
					addMissingNetworks(cloud, addItems)
				}.onUpdate{ updateItems ->
					updateMatchedNetworks(cloud,updateItems)
				}.onDelete { removeItems ->
					morpheus.network.remove(removeItems).blockingGet()
				}.start()
			} else {
				log.error("Error Listing Subnets from MaaS Api ${listResults.msg}")
			}
		} catch(e) {
			log.error("cacheSubnets error: ${e}", e)
		}

	}

	protected void addMissingNetworks(Cloud cloud, Collection<Map> addItems) {
		String objCategory = "maas.subnet.${cloud.id}"
		NetworkType networkType = new NetworkType(code: "maas-plugin-network")
		Collection<Network> networksToAdd = addItems.collect {Map cloudItem ->
			Network add = new Network(owner:cloud.owner, code:objCategory + ".${cloudItem.id}", category:objCategory,
					externalId:"${cloudItem.id}", name:cloudItem.name, dhcpServer:(cloudItem.vlan?.dhcp_on),
					type:networkType, cidr:cloudItem.cidr, gateway:cloudItem.gateway_ip, fabricId:cloudItem.vlan?.fabric,
					refType:'ComputeZone', refId:cloud.id)
			add.dnsPrimary = cloudItem.dns_servers?.size() > 0 ? cloudItem.dns_servers[0] : null
			add.dnsSecondary = cloudItem.dns_servers?.size() > 1 ? cloudItem.dns_servers[1] : null
			return add
		}
		if(networksToAdd.size() > 0) {
			morpheusContext.network.create(networksToAdd).blockingGet()
		}
	}

	protected void updateMatchedNetworks(Cloud cloud, List<SyncTask.UpdateItem<Network,Map>> updateItems) {
		List<Network> itemsToUpdate = []
		for(updateMap in updateItems) {
			def doUpdate = false
			//set raw data
//			def rawData = updateMap.masterItem.encodeAsJson().toString()
//			if(updateMap.existingItem.rawData != rawData) {
//				updateMap.existingItem.rawData = rawData
//				doUpdate = true
//			}
			//save it
			if(doUpdate) {
				itemsToUpdate << updateMap.existingItem
			}
		}
		if(itemsToUpdate.size() > 0) {
			morpheusContext.network.save(itemsToUpdate).blockingGet()
		}
	}
}
