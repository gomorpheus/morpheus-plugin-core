package com.morpheusdata.maas.plugin

import com.morpheusdata.apiutil.NetworkUtility
import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.ReferenceData
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.projection.NetworkIdentityProjection
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
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
				inputType: OptionType.InputType.TEXT
		)
		OptionType serviceToken = new OptionType(
				name: 'API Key',
				code: 'maas-service-token',
				fieldName: 'serviceToken',
				displayOrder: 1,
				fieldLabel: 'API Key',
				required: true,
				inputType: OptionType.InputType.TEXT
		)
		OptionType availablePool = new OptionType(
				name: 'Available Pool',
				code: 'maas-resource-pool',
				fieldName: 'resourcePoolId',
				optionSource: 'maasResourcePools',
				displayOrder: 2,
				fieldLabel: 'Available Pool',
				required: false,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'maas-service-url'
		)

		OptionType releaseMode = new OptionType(
				name: 'Release Mode',
				code: 'maas-release-mode',
				fieldName: 'releaseMode',
				optionSource: 'maasReleaseModes',
				displayOrder: 3,
				fieldLabel: 'Release Mode',
				required: true,
				inputType: OptionType.InputType.SELECT,
		)

		OptionType releasePoolName = new OptionType(
				name: 'Release Pool',
				code: 'maas-release-pool',
				fieldName: 'releasePoolName',
				optionSource: 'maasResourcePools',
				displayOrder: 4,
				fieldLabel: 'Release Pool',
				required: false,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'maas-service-url'
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
		return getAvailableProvisioningProviders().find { it.providerCode == providerCode }
	}

	@Override
	ServiceResponse validate(Cloud cloudInfo) {
		return null
	}

	@Override
	void refresh(Cloud cloudInfo) {
		initializeCloud(cloudInfo)
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {

	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		return null
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getProviderCode() {
		return 'maas-cloud'
	}

	@Override
	String getProviderName() {
		return 'Metal as a Service'
	}

	List<Map<String, Object>> maasResourcePools(def cloud) {
		log.info("maasResourcePools")
		String serviceUrl = cloud?.serviceUrl ?:cloud?.configMap?.serviceUrl
		if (serviceUrl) {
			def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
			String category = "maas.resourcepool.${cloud.id}"
			List<ComputeZonePool> cachedPools = morpheusContext.cloud.readResourcePools(cloud, category).blockingGet()
			if (cachedPools) {
				return cachedPools.collect { [name: it.name, value: it.externalId] }
			} else {
				log.info("no cached pools found")
				def apiResponse = MaasComputeUtility.listResourcePools(authConfig, [:])
				if (apiResponse.success) {
					return apiResponse.data.collect { [name: it.name, value: it.id] }
				}
			}
		}
		[]
	}

	List maasReleaseModes(Cloud cloud) {
		[
				[name: 'Release', value: 'release'],
				[name: 'Quick Delete', value: 'quick-delete'],
				[name: 'Delete', value: 'delete']
		]
	}

	@Override
	ServiceResponse initializeCloud(Cloud cloud) {
		ServiceResponse rtn = new ServiceResponse(success: false)
		log.debug "Initializing Cloud: ${cloud.code}"
		log.debug "config: ${cloud.configMap}"
		try {
			def syncDate = new Date()
			String apiUrl = cloud.configMap.serviceUrl
			def apiUrlObj = new URL(apiUrl)
			def apiHost = apiUrlObj.getHost()
			def apiPort = apiUrlObj.getPort() > 0 ? apiUrlObj.getPort() : (apiUrlObj?.getProtocol()?.toLowerCase() == 'https' ? 443 : 80)
			log.info("apiHost: ${apiHost}, port: ${apiPort}")
			def hostOnline = NetworkUtility.testHostConnection(apiHost, apiPort, true, true, cloud.apiProxy)
			log.info("hostOnline: $hostOnline")
			if (hostOnline) {
				cloud.serviceUrl = apiUrl
				cloud.serviceToken = cloud.configMap.serviceToken
				def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
				def testResults = MaasComputeUtility.testConnection(authConfig, [:])
				if (testResults.success) {
					morpheusContext.cloud.updateZoneStatus(cloud, 'syncing', null, syncDate)
					//cache stuff
					def cacheOpts = [:]
//					//region controllers
					cacheRegionControllers(cloud, cacheOpts)
//					//rack controllers
					cacheRackControllers(cloud, cacheOpts)
//					//resource pools
					cacheResourcePools(cloud, cacheOpts)
//					//machines
					cacheMachines(cloud, cacheOpts)
//					//images
					cacheImages(cloud, cacheOpts)
//					//fabrocs
//					cacheFabrics(zone, cacheOpts)
//					//subnets
					cacheSubnets(zone, cacheOpts)
					morpheusContext.cloud.updateZoneStatus(cloud, 'ok', null, syncDate)
				} else {
					if (testResults.invalidLogin == true) {
						morpheusContext.cloud.updateZoneStatus(cloud, 'error', 'invalid credentials', syncDate)
					} else {
						morpheusContext.cloud.updateZoneStatus(cloud, 'error', 'error connecting', syncDate)
					}
				}
			} else {
				morpheusContext.cloud.updateZoneStatus(cloud, 'offline', 'maas is not reachable', syncDate)
			}
			rtn.success = true
		} catch (e) {
			log.error("refresh zone error: ${e}", e)
		}
		return rtn
	}

	protected def cacheReferenceData(Cloud cloud, String category, List<Map> apiItems) {
		try {
			Observable<ReferenceDataSyncProjection> domainRecords = morpheusContext.cloud.listReferenceDataByCategory(cloud, category)
			SyncTask<ReferenceDataSyncProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, apiItems)
			syncTask.addMatchFunction { ReferenceDataSyncProjection domainObject, Map apiItem ->
				domainObject.externalId == apiItem.system_id
			}.addMatchFunction { ReferenceDataSyncProjection domainObject, Map apiItem ->
				domainObject.name == apiItem.hostname
			}.onDelete { removeItems ->
				morpheusContext.cloud.removeMissingReferenceDataByIds(removeItems.collect {it.id}).blockingGet()
			}.onAdd { itemsToAdd ->
				while (itemsToAdd?.size() > 0) {
					List chunkedAddList = itemsToAdd.take(50)
					itemsToAdd = itemsToAdd.drop(50)
					addMissingReferenceData(category, cloud, chunkedAddList)
				}
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ReferenceDataSyncProjection, Map>> updateItems ->
				return morpheusContext.cloud.listReferenceDataByExternalIds(updateItems.collect { it.existingItem.id } as List<Long>)
			}.onUpdate { List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems ->
				updateMatchedReferenceData(updateItems)
			}.start()
		} catch(e) {
			log.error("cacheRegionControllers error: ${e}", e)
		}
	}

	def cacheRegionControllers(Cloud cloud, Map opts) {
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		String category = "maas.regioncontroller.${cloud.id}"
		def apiResponse = MaasComputeUtility.listRegionControllers(authConfig, opts)
		if (apiResponse.success) {
			List apiItems = apiResponse.data as List<Map>
			cacheReferenceData(cloud, category, apiItems)
		}
	}

	void updateMatchedReferenceData(List<SyncTask.UpdateItem<ReferenceData, Map>> updateItems) {
		List<ReferenceData> itemsToSave = []
		for(item in updateItems) {
			def existingItem = item.existingItem
			def name = item.masterItem.name
			def code = item.masterItem.code
			def keyValue = item.masterItem.keyValue
			def value = item.masterItem.value
			def externalId = item.masterItem.externalId
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
		morpheusContext.cloud.saveAll(itemsToSave).blockingGet()

	}

	void addMissingReferenceData(String category, Cloud cloud, List<Map> list) {
		List<ReferenceData> records = []
		for(regionController in list) {
			ReferenceData referenceData = new ReferenceData(
					name: regionController.hostname,
					code: "$category.${regionController.system_id}",
					keyValue: regionController.system_id,
					value: regionController.fqdn,
					externalId: regionController.system_id,
					type: 'string'
			)
			records.add(referenceData)
		}
		morpheusContext.cloud.saveAll(records, cloud, category).blockingGet()
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
		String category = "maas.rackcontroller.${cloud.id}"
		def rackControllers = morpheusContext.cloud.findReferenceDataByCategory(cloud, category).blockingGet()
		log.info("cached rackControllers category=$category: $rackControllers")
		rackControllers.each { rack ->
			def apiResponse = MaasComputeUtility.listImages(authConfig, rack.externalId, opts)
			if(apiResponse.success) {
				try {
					List apiItems = apiResponse?.data?.images as List<Map>
					Observable<VirtualImageIdentityProjection> domainRecords = morpheusContext.cloud.listReferenceDataByCategory(cloud, category)
					SyncTask<VirtualImageIdentityProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, apiItems)
					syncTask.addMatchFunction { VirtualImageIdentityProjection domainObject, Map apiItem ->
						domainObject.externalId == apiItem.externalId
					}.onDelete { removeItems ->
						morpheusContext.virtualImage.remove(removeItems).blockingGet()
					}.onAdd { itemsToAdd ->
						while (itemsToAdd?.size() > 0) {
							List<Map> chunkedAddList = itemsToAdd.take(50)
							itemsToAdd = itemsToAdd.drop(50)
							List<VirtualImage> itemsToSave = []
							for(cloudImage in chunkedAddList) {
								itemsToSave.add(MaasComputeUtility.bootImageToVirtualImage(cloud, cloudImage))
							}
							morpheusContext.virtualImage.save(itemsToSave).blockingGet()
						}
					}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map>> updateItems ->
						return morpheusContext.cloud.listReferenceDataByExternalIds(updateItems.collect { it.existingItem.externalId } as List<Long>)
					}.onUpdate { List<SyncTask.UpdateItem<VirtualImage, Map>> updateItems ->
						List<VirtualImage> toSave = []
						for(item in updateItems) {
							VirtualImage virtualImage = MaasComputeUtility.bootImageToVirtualImage(cloud, item.masterItem)
							def existing = item.existingItem
							if(virtualImage.category != existing.category || virtualImage.code != existing.code
								|| virtualImage.name != existing.name || virtualImage.imageType != existing.imageType
								|| virtualImage.externalId != existing.externalId) {
								toSave.add(virtualImage)
							}
						}
						morpheusContext.virtualImage.save(toSave).blockingGet()
					}.start()
				} catch(e) {
					log.error("cacheImages error: ${e}", e)
				}
			}
		}
	}

	def cacheResourcePools(Cloud cloud, Map opts) {
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		String category = "maas.resourcepool.${cloud.id}"
		def apiResponse = MaasComputeUtility.listResourcePools(authConfig, opts)
		if (apiResponse.success) {
			List apiItems = apiResponse.data as List<Map>
			cacheReferenceData(cloud, category, apiItems)
		}
	}

	def cacheMachines(Cloud cloud, Map opts) {
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		String category = "maas.server.${cloud.id}"
		def apiResponse = MaasComputeUtility.listMachines(authConfig, opts)
		if (apiResponse.success) {
			List apiItems = apiResponse.data as List<Map>
			cacheReferenceData(cloud, category, apiItems)
		}
	}

	def cacheSubnets(Cloud cloud, Map opts) {
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
					morpheusContext.network.listById(updateItems?.collect{ it.existingItem.id})
				}.onAdd { Collection<Map> addItems ->
					addMissingNetworks(cloud, addItems)
				}.onUpdate{updateItems ->
					updateMatchedNetworks(cloud,updateItems)
				}.onDelete {NetworkIdentityProjection removeItems ->
					morpheusContext.network.remove(removeItems).blockingGet()
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
		NetworkType networkType = new NetworkType(code: "maasSubnet")
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