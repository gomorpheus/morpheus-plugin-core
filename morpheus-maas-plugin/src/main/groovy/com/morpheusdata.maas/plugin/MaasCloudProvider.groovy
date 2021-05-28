package com.morpheusdata.maas.plugin

import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.ComputeZonePool
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.ReferenceData
import com.morpheusdata.model.VirtualImage
import com.morpheusdata.model.projection.ComputeServerIdentityProjection
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection
import com.morpheusdata.model.projection.NetworkIdentityProjection
import com.morpheusdata.model.projection.ReferenceDataSyncProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
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
				optionSource: 'maasReleaseModes',
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
	ServiceResponse validate(Cloud cloudInfo) {
		log.info("MaaS validate")
		return new ServiceResponse(success: true)
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
					morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.syncing, null, syncDate)
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
					cacheSubnets(cloud, cacheOpts)
					morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.ok, null, syncDate)
				} else {
					if (testResults.invalidLogin == true) {
						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.error, 'invalid credentials', syncDate)
					} else {
						morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.error, 'error connecting', syncDate)
					}
				}
			} else {
				morpheusContext.cloud.updateZoneStatus(cloud, Cloud.Status.offline, 'maas is not reachable', syncDate)
			}
			rtn.success = true
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
		String category = "maas.rackcontroller.${cloud.id}"
		List<ReferenceDataSyncProjection> rackControllers = []
		morpheusContext.cloud.listReferenceDataByCategory(cloud, category).subscribe{
			rackControllers << it
		}
		log.info("cached rackControllers category=$category: $rackControllers")
		rackControllers.each { rack ->
			def apiResponse = MaasComputeUtility.listImages(authConfig, rack.externalId, opts)
			if(apiResponse.success) {
				try {
					List apiItems = apiResponse?.data?.images as List<Map>
					Observable<VirtualImageIdentityProjection> domainRecords = morpheus.virtualImage.listSyncProjections(cloud.id)
					SyncTask<VirtualImageIdentityProjection, Map, ReferenceData> syncTask = new SyncTask<>(domainRecords, apiItems)
					syncTask.addMatchFunction { VirtualImageIdentityProjection domainObject, Map apiItem ->
						domainObject.externalId == apiItem.externalId
					}.onDelete { removeItems ->
						morpheus.virtualImage.remove(removeItems).blockingGet()
					}.onAdd { itemsToAdd ->
						while (itemsToAdd?.size() > 0) {
							List<Map> chunkedAddList = itemsToAdd.take(50)
							itemsToAdd = itemsToAdd.drop(50)
							List<VirtualImage> itemsToSave = []
							for(cloudImage in chunkedAddList) {
								itemsToSave.add(MaasComputeUtility.bootImageToVirtualImage(cloud, cloudImage))
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
						for(item in updateItems) {
							VirtualImage virtualImage = MaasComputeUtility.bootImageToVirtualImage(cloud, item.masterItem)
							def existing = item.existingItem
							if(virtualImage.category != existing.category || virtualImage.code != existing.code
								|| virtualImage.name != existing.name || virtualImage.imageType != existing.imageType
								|| virtualImage.externalId != existing.externalId) {
								toSave.add(virtualImage)
							}
						}
						morpheus.virtualImage.save(toSave, cloud).blockingGet()
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
			log.info("resource pools to cache: $apiItems")
			Observable<ComputeZonePoolIdentityProjection> domainRecords = morpheus.cloud.pool.listSyncProjections(cloud.id, category)
			SyncTask<ComputeZonePoolIdentityProjection, Map, ComputeZonePool> syncTask = new SyncTask<>(domainRecords, apiItems)
			syncTask.addMatchFunction { ComputeZonePoolIdentityProjection domainObject, Map apiItem ->
				domainObject.externalId == apiItem.externalId
			}.onDelete { removeItems ->
				morpheus.cloud.pool.remove(removeItems).blockingGet()
			}.onAdd { itemsToAdd ->
				while (itemsToAdd?.size() > 0) {
					List<Map> chunkedAddList = itemsToAdd.take(50)
					itemsToAdd = itemsToAdd.drop(50)
					List<ComputeZonePool> itemsToSave = []
					for(resourcePool in chunkedAddList) {
						itemsToSave.add(MaasComputeUtility.resourcePoolToComputeZonePool(resourcePool, cloud, category))
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
					ComputeZonePool computeZonePool = MaasComputeUtility.resourcePoolToComputeZonePool(item.masterItem, cloud)
					def existing = item.existingItem
					if(computeZonePool.name != existing.name) {
						toSave.add(computeZonePool)
					}
				}
				morpheus.cloud.pool.save(toSave).blockingGet()
			}.start()
		}
	}

	def cacheMachines(Cloud cloud, Map opts) {
		def authConfig = MaasProvisionProvider.getAuthConfig(cloud)
		def apiResponse = MaasComputeUtility.listMachines(authConfig, opts)
		if (apiResponse.success) {
			List apiItems = apiResponse.data as List<Map>
			log.info("machines to cache: $apiItems")
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
						itemsToSave.add(MaasComputeUtility.machineToComputeServer(maasMachine, cloud))
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
					ComputeServer machine = MaasComputeUtility.machineToComputeServer(item.masterItem, cloud)
					def existing = item.existingItem
					if(machine.name != existing.name) {
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
