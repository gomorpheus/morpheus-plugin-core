package com.morpheusdata.cloud

import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.*
import com.morpheusdata.model.projection.ServicePlanIdentityProjection
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import io.reactivex.Observable

@Slf4j
class DigitalOceanCloudProvider implements CloudProvider {
	Plugin plugin
	MorpheusContext morpheusContext
	DigitalOceanApiService apiService

	DigitalOceanCloudProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
		apiService = new DigitalOceanApiService()
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
		return 'digital-ocean-plugin'
	}

	@Override
	String getName() {
		return 'Digital Ocean Plugin'
	}

	@Override
	String getDescription() {
		return 'Digital Ocean Plugin Description'
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		OptionType ot1 = new OptionType(
				name: 'Username',
				code: 'do-username',
				fieldName: 'doUsername',
				displayOrder: 0,
				fieldLabel: 'Username',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'config'
		)
		OptionType ot2 = new OptionType(
				name: 'API Key',
				code: 'do-api-key',
				fieldName: 'doApiKey',
				displayOrder: 1,
				fieldLabel: 'API Key',
				required: true,
				inputType: OptionType.InputType.PASSWORD,
				fieldContext: 'config'
		)
		OptionType ot3 = new OptionType(
				name: 'Datacenter',
				code: 'do-datacenter',
				fieldGroup: 'SomeFieldGroup',
				fieldName: 'datacenter',
				optionSource: 'datacenters',
				displayOrder: 2,
				fieldLabel: 'Datacenter',
				required: true,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'do-api-key',
				fieldContext: 'config'
		)
		return [ot1, ot2, ot3]
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		//digital ocean
		def serverTypes = [
				new ComputeServerType(code: 'digitalOceanWindows2', name: 'DigitalOcean Windows Node', description: '', platform: PlatformType.windows, agentType: ComputeServerType.AgentType.host,
						enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: false, creatable: false, computeService: 'digitalOceanComputeService',
						displayOrder: 17, hasAutomation: true, reconfigureSupported: true,
						containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, guestVm: true,
				),

				new ComputeServerType(code: 'digitalOceanVm2', name: 'DigitalOcean VM Instance', description: '', platform: PlatformType.linux,
						enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: false, creatable: false, computeService: 'digitalOceanComputeService',
						displayOrder: 0, hasAutomation: true, reconfigureSupported: true,
						containerHypervisor: false, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.guest, guestVm: true,
				),

				//docker
				new ComputeServerType(code: 'digitalOceanLinux2', name: 'DigitalOcean Docker Host', description: '', platform: PlatformType.linux,
						enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: false, creatable: true, computeService: 'digitalOceanComputeService',
						displayOrder: 16, hasAutomation: true, reconfigureSupported: true,
						containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.host, clusterType: ComputeServerType.ClusterType.docker,
						computeTypeCode: 'docker-host',
				),

				//kubernetes
				new ComputeServerType(code: 'digitalOceanKubeMaster2', name: 'Digital Ocean Kubernetes Master', description: '', platform: PlatformType.linux,
						reconfigureSupported: true, enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: true, creatable: true,
						supportsConsoleKeymap: true, computeService: 'digitalOceanComputeService', displayOrder: 10,
						hasAutomation: true, containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.host, clusterType: ComputeServerType.ClusterType.kubernetes,
						computeTypeCode: 'kube-master',
						optionTypes: [

						]
				),
				new ComputeServerType(code: 'digitalOceanKubeWorker2', name: 'Digital Ocean Kubernetes Worker', description: '', platform: PlatformType.linux,
						reconfigureSupported: true, enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: true, creatable: true,
						supportsConsoleKeymap: true, computeService: 'digitalOceanComputeService', displayOrder: 10,
						hasAutomation: true, containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.host, clusterType: ComputeServerType.ClusterType.kubernetes,
						computeTypeCode: 'kube-worker',
						optionTypes: [

						]
				),
				//unmanaged discovered type
				new ComputeServerType(code: 'digitalOceanUnmanaged', name: 'Digital Ocean VM', description: 'Digital Ocean VM', platform: PlatformType.none, agentType: ComputeServerType.AgentType.guest,
						enabled: true, selectable: false, externalDelete: true, managed: false, controlPower: true, controlSuspend: false, creatable: false, computeService: 'digitalOceanComputeService',
						displayOrder: 99, hasAutomation: false,
						containerHypervisor: false, bareMetalHost: false, vmHypervisor: false, managedServerType: 'digitalOceanVm2', guestVm: true, supportsConsoleKeymap: true
				)
		]

		return serverTypes
	}

	@Override
	Collection<ProvisioningProvider> getAvailableProvisioningProviders() {
		return plugin.getProvidersByType(ProvisioningProvider) as Collection<ProvisioningProvider>
	}

	@Override
	Collection<BackupProvider> getAvailableBackupProviders() {
		return plugin.getProvidersByType(BackupProvider) as Collection<BackupProvider>
	}

	@Override
	ProvisioningProvider getProvisioningProvider(String providerCode) {
		return getAvailableProvisioningProviders().find { it.code == providerCode }
	}

	@Override
	ServiceResponse validate(Cloud zoneInfo) {
		log.debug "validating Cloud: ${zoneInfo.code}"
		if (!zoneInfo.configMap.datacenter) {
			return new ServiceResponse(success: false, msg: 'Choose a datacenter')
		}
		if (!zoneInfo.configMap.doUsername) {
			return new ServiceResponse(success: false, msg: 'Enter a username')
		}
		if (!zoneInfo.configMap.doApiKey) {
			return new ServiceResponse(success: false, msg: 'Enter your api key')
		}

		HttpGet http = new HttpGet("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/regions")
		def respMap = apiService.makeApiCall(http, zoneInfo.configMap.doApiKey)
		if(respMap.resp.statusLine.statusCode != 200) {
			return new ServiceResponse(success: false, msg: 'Invalid credentials')
		}

		return new ServiceResponse(success: true)
	}

	@Override
	ServiceResponse initializeCloud(Cloud cloud) {
		ServiceResponse serviceResponse
		log.debug "Initializing Cloud: ${cloud.code}"
		log.debug "config: ${cloud.configMap}"
		String apiKey = cloud.configMap.doApiKey
		HttpGet accountGet = new HttpGet("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/account")

		// check account
		def respMap = apiService.makeApiCall(accountGet, apiKey)
		if (respMap.resp.statusLine.statusCode == 200 && respMap.json.account.status == 'active') {
			serviceResponse = new ServiceResponse(success: true, content: respMap.json)

			cacheSizes(cloud, apiKey)
			cacheImages(cloud)

			KeyPair keyPair = morpheusContext.cloud.findOrGenerateKeyPair(cloud.account).blockingGet()
			if (keyPair) {
				KeyPair updatedKeyPair = findOrUploadKeypair(apiKey, keyPair.publicKey, keyPair.name)
				morpheusContext.cloud.updateKeyPair(updatedKeyPair, cloud)
			} else {
				log.debug "no morpheus keys found"
			}
		} else {
			serviceResponse = new ServiceResponse(success: false, msg: respMap.resp?.statusLine?.statusCode, content: respMap.json)
		}

		serviceResponse
	}

	@Override
	void refresh(Cloud cloudInfo) {
		log.debug "cloud refresh has run for ${cloudInfo.code}"
		cacheSizes(cloudInfo, cloudInfo.configMap.doApiKey)
		cacheImages(cloudInfo)
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {
		log.debug "daily refresh run for ${cloudInfo.code}"
	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		return new ServiceResponse(success: true)
	}

	List<VirtualImage> listImages(Cloud cloudInfo, Boolean userImages) {
		log.debug "list ${userImages ? 'User' : 'OS'} Images"
		List<VirtualImage> virtualImages = []

		Map queryParams = [:]
		if (userImages) {
			queryParams.private = 'true'
		} else {
			queryParams.type = 'distribution'
		}
		List images = apiService.makePaginatedApiCall(cloudInfo.configMap.doApiKey, '/v2/images', 'images', queryParams)

		String imageCodeBase = "doplugin.image.${userImages ? 'user' : 'os'}"

		log.info("images: $images")
		images.each {
			Map props = [
					name      : "${it.distribution} ${it.name}",
					externalId: it.id,
					code      : "${imageCodeBase}.${cloudInfo.code}.${it.id}",
					category  : "${imageCodeBase}.${cloudInfo.code}",
					imageType : ImageType.qcow2,
					platform  : it.distribution,
					isPublic  : it.public,
					minDisk   : it.min_disk_size,
					locations : it.regions,
					account   : cloudInfo.account,
					refId     : cloudInfo.id,
					refType   : 'ComputeZone'
			]
			virtualImages << new VirtualImage(props)
		}
		log.info("api images: $virtualImages")
		virtualImages
	}

	def cacheImages(Cloud cloud) {
		List<VirtualImage> apiImages = listImages(cloud, false)
		apiImages += listImages(cloud, true)

		Observable<VirtualImageIdentityProjection> domainImages = morpheusContext.virtualImage.listSyncProjections(cloud.id)
		SyncTask<VirtualImageIdentityProjection, VirtualImage, VirtualImage> syncTask = new SyncTask(domainImages, apiImages)
		syncTask.addMatchFunction { VirtualImageIdentityProjection projection, VirtualImage apiImage ->
			projection.externalId == apiImage.externalId
		}.onDelete { List<VirtualImageIdentityProjection> deleteList ->
			morpheus.virtualImage.remove(deleteList)
		}.onAdd { createList ->
			log.info("Creating ${createList?.size()} new images")
			while (createList.size() > 0) {
				List chunkedList = createList.take(50)
				createList = createList.drop(50)
				morpheus.virtualImage.create(chunkedList, cloud).blockingGet()
			}
		}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<VirtualImageIdentityProjection, VirtualImage>> updateItems ->

			Map<Long, SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
			morpheus.virtualImage.listById(updateItems.collect { it.existingItem.id } as Collection<Long>).map {VirtualImage virtualImage ->
				SyncTask.UpdateItemDto<VirtualImageIdentityProjection, Map> matchItem = updateItemMap[virtualImage.id]
				return new SyncTask.UpdateItem<VirtualImage,Map>(existingItem:virtualImage, masterItem:matchItem.masterItem)
			}

		}.onUpdate { updateList ->
			updateMatchedImages(updateList, cloud)
		}.start()
	}

	void updateMatchedImages(List<SyncTask.UpdateItem<VirtualImage,Map>> updateItems, Cloud cloud) {
		List<VirtualImage> imagesToUpdate = updateItems.collect { it.existingItem }
		morpheusContext.virtualImage.save(imagesToUpdate, cloud).blockingGet()
	}

	def cacheSizes(Cloud cloud, String apiKey) {
		log.info("cacheSizes")
		HttpGet sizesGet = new HttpGet("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/sizes")
		Map respMap = apiService.makeApiCall(sizesGet, apiKey)
		List<ServicePlan> servicePlans = []
		respMap.json?.sizes?.each {
			def name = getNameForSize(it)
			def servicePlan = new ServicePlan(
					code: "doplugin.size.${it.slug}",
					provisionTypeCode: 'do-provider',
					description: name,
					name: name,
					editable: false,
					externalId: it.slug,
					maxCores: it.vcpus,
					maxMemory: it.memory.toLong() * 1024l * 1024l, // MB
					maxStorage: it.disk.toLong() * 1024l * 1024l * 1024l, //GB
					sortOrder: it.disk.toLong(),
					price_monthly: it.price_monthly,
					price_hourly: it.price_hourly,
					refType: 'ComputeZone',
					refId: cloud.id
			)
			servicePlans << servicePlan
		}
		log.info("api service plans: $servicePlans")
		if (servicePlans) {
			Observable<ServicePlanIdentityProjection> domainPlans = morpheusContext.servicePlan.listSyncProjections(cloud.id)
			SyncTask<ServicePlanIdentityProjection, ServicePlan, ServicePlan> syncTask = new SyncTask(domainPlans, servicePlans)
			syncTask.addMatchFunction { ServicePlanIdentityProjection projection, ServicePlan apiPlan ->
				projection.externalId == apiPlan.externalId
			}.onDelete { List<ServicePlanIdentityProjection> deleteList ->
				morpheus.servicePlan.remove(deleteList)
			}.onAdd { createList ->
				while (createList.size() > 0) {
					List chunkedList = createList.take(50)
					createList = createList.drop(50)
					morpheus.servicePlan.create(chunkedList).blockingGet()
				}
			}.withLoadObjectDetails { List<SyncTask.UpdateItemDto<ServicePlanIdentityProjection, ServicePlan>> updateItems ->

				Map<Long, SyncTask.UpdateItemDto<ServicePlanIdentityProjection, Map>> updateItemMap = updateItems.collectEntries { [(it.existingItem.id): it]}
				morpheus.servicePlan.listById(updateItems.collect { it.existingItem.id } as Collection<Long>).map {ServicePlan servicePlan ->
					SyncTask.UpdateItemDto<ServicePlanIdentityProjection, Map> matchItem = updateItemMap[servicePlan.id]
					return new SyncTask.UpdateItem<ServicePlan,Map>(existingItem:servicePlan, masterItem:matchItem.masterItem)
				}


			}.onUpdate { updateList ->
				updateMatchedPlans(updateList)
			}.start()
		}
	}

	def updateMatchedPlans(List<SyncTask.UpdateItem<ServicePlan,Map>> updateItems) {
		List<ServicePlan> plansToUpdate = updateItems.collect { it.existingItem }
		morpheusContext.servicePlan.save(plansToUpdate).blockingGet()
	}

	KeyPair findOrUploadKeypair(String apiKey, String publicKey, String keyName) {
		keyName = keyName ?: 'morpheus_do_plugin_key'
		log.debug "find or update keypair for key $keyName"
		List keyList = apiService.makePaginatedApiCall(apiKey, '/v2/account/keys', 'ssh_keys', [:])
		log.debug "keylist: $keyList"
		def match = keyList.find { publicKey.startsWith(it.public_key) }
		log.debug("match: ${match} - list: ${keyList}")
		if (!match) {
			log.debug 'key not found in DO'
			HttpPost httpPost = new HttpPost("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/account/keys")
			httpPost.entity = new StringEntity(JsonOutput.toJson([public_key: publicKey, name: keyName]))
			def respMap = apiService.makeApiCall(httpPost, apiKey)
			if (respMap.resp.statusLine.statusCode == 200) {
				match = new KeyPair(name: respMap.json.name, externalId: respMap.json.id, publicKey: respMap.json.public_key, publicFingerprint: respMap.json.fingerprint)
			} else {
				log.debug 'failed to add DO ssh key'
			}
			match = respMap.json
		}
		new KeyPair(name: match.name, externalId: match.id, publicKey: match.public_key, publicFingerprint: match.fingerprint)
	}

	private getNameForSize(sizeData) {
		def memoryName = sizeData.memory < 1000 ? "${sizeData.memory} MB" : "${sizeData.memory.div(1024l)} GB"
		"Plugin Droplet ${sizeData.vcpus} CPU, ${memoryName} Memory, ${sizeData.disk} GB Storage"
	}
}
