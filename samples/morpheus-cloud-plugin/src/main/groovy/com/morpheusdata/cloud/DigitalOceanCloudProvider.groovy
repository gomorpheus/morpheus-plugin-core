package com.morpheusdata.cloud

import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity

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
	MorpheusContext getMorpheusContext() {
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getProviderCode() {
		return 'digital-ocean-plugin'
	}

	@Override
	String getProviderName() {
		return 'Digital Ocean2'
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
				inputType: OptionType.InputType.TEXT
		)
		OptionType ot2 = new OptionType(
				name: 'API Key',
				code: 'do-api-key',
				fieldName: 'doApiKey',
				displayOrder: 1,
				fieldLabel: 'API Key',
				required: true,
				inputType: OptionType.InputType.PASSWORD
		)
		OptionType ot3 = new OptionType(
				name: 'Datacenter',
				code: 'do-datacenter',
				fieldName: 'datacenter',
				optionSource: 'loadDatacenters',
				displayOrder: 2,
				fieldLabel: 'Datacenter',
				required: true,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'do-api-key'
		)
		return [ot1, ot2, ot3]
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		def type1 = new ComputeServerType(name: 'Example Cloud Type', code: 'example-cloud-type', platform: PlatformType.mac)
		return [type1]
	}

	@Override
	Collection<ProvisioningProvider> getAvailableProvisioningProviders() {
		return plugin.getProvidersByType(ProvisioningProvider) as Collection<ProvisioningProvider>
	}

	@Override
	ProvisioningProvider getProvisioningProvider(String providerCode) {
		return getAvailableProvisioningProviders().find { it.providerCode == providerCode }
	}

	@Override
	ServiceResponse validate(Cloud zoneInfo) {
		println "validating Cloud: ${zoneInfo.code}"
		if (!zoneInfo.configMap.datacenter) {
			return new ServiceResponse(success: false, msg: 'Choose a datacenter')
		}
		if (!zoneInfo.configMap.doUsername) {
			return new ServiceResponse(success: false, msg: 'Enter a username')
		}
		if (!zoneInfo.configMap.doApiKey) {
			return new ServiceResponse(success: false, msg: 'Enter your api key')
		}
		return new ServiceResponse(success: true)
	}

	@Override
	ServiceResponse initializeCloud(Cloud zoneInfo) {
		ServiceResponse serviceResponse
		println "Initializing Cloud: ${zoneInfo.code}"
		println "config: ${zoneInfo.configMap}"
		String apiKey = zoneInfo.configMap.doApiKey
		HttpGet accountGet = new HttpGet("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/account")

		// check account
		def respMap = apiService.makeApiCall(accountGet, apiKey)
		if (respMap.resp.statusLine.statusCode == 200 && respMap.json.account.status == 'active') {
			serviceResponse = new ServiceResponse(success: true, content: respMap.json)

			loadDatacenters(zoneInfo)
			cacheSizes(apiKey)
			morpheusContext.compute.cacheImages(listImages(zoneInfo, false), zoneInfo)
			morpheusContext.compute.cacheImages(listImages(zoneInfo, true), zoneInfo)
			KeyPair keyPair = morpheusContext.compute.findOrGenerateKeyPair(zoneInfo.account).blockingGet()
			if (keyPair) {
				findOrUploadKeypair(apiKey, keyPair.publicKey, keyPair.name)
			} else {
				println "no morpheus keys found"
			}
		} else {
			serviceResponse = new ServiceResponse(success: false, msg: respMap.resp?.statusLine?.statusCode, content: respMap.json)
		}

		serviceResponse
	}

	@Override
	void refresh(Cloud cloudInfo) {
		println "cloud refresh has run for ${cloudInfo.code}"
		cacheSizes(cloudInfo.configMap.doApiKey)
		loadDatacenters(cloudInfo)
		morpheusContext.compute.cacheImages(listImages(cloudInfo, false), cloudInfo) // public OS
		morpheusContext.compute.cacheImages(listImages(cloudInfo, true), cloudInfo) // User's private
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {
		println "daily refresh run for ${cloudInfo.code}"
	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		return new ServiceResponse(success: true)
	}

	List<Map> loadDatacenters(def cloudInfo) {
		List datacenters = []
		println "load datacenters for ${cloudInfo.code}"
		HttpGet http = new HttpGet("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/regions")
		def respMap = apiService.apiService.makeApiCall(http, cloudInfo.configMap.doApiKey)
		respMap?.json?.regions?.each {
			datacenters << [value: it.slug, name: it.name, available: it.available]
		}
		// TODO cache these?
		datacenters
	}

	List<VirtualImage> listImages(Cloud cloudInfo, Boolean userImages) {
		println "list ${userImages ? 'User' : 'OS'} Images"
		List<VirtualImage> virtualImages = []

		Map queryParams = [:]
		if (userImages) {
			queryParams.private = 'true'
		} else {
			queryParams.type = 'distribution'
		}
		List images = apiService.makePaginatedApiCall(cloudInfo.configMap.doApiKey, '/v2/images', 'images', queryParams)

		String imageCodeBase = "doplugin.image.${userImages ? 'user' : 'os'}"

		images.each {
			Map props = [
					name      : it.name,
					externalId: it.id,
					code      : "${imageCodeBase}.${cloudInfo.code}.${it.id}",
					category  : "${imageCodeBase}.${cloudInfo.code}",
					imageType : ImageType.qcow2,
					platform  : it.distribution,
					isPublic  : it.public,
					minDisk   : it.min_disk_size,
					locations : it.regions
			]
			virtualImages << new VirtualImage(props)
		}
		virtualImages
	}

	def cacheSizes(String apiKey) {
		HttpGet sizesGet = new HttpGet("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/sizes")
		Map respMap = apiService.makeApiCall(sizesGet, apiKey)
		List<ServicePlan> servicePlans = []
		respMap.json?.sizes?.each {
			def name = getNameForSize(it)
			def servicePlan = new ServicePlan(
					code: "doplugin.size.${it.slug}",
					provisionTypeCode: getProviderCode(),
					description: name,
					name: name,
					editable: false,
					externalId: it.slug,
					maxCores: it.vcpus,
					maxMemory: it.memory.toLong() * 1024l * 1024l, // MB
					maxStorage: it.disk.toLong() * 1024l * 1024l * 1024l, //GB
					sortOrder: it.disk.toLong(),
					price_monthly: it.price_monthly,
					price_hourly: it.price_hourly
			)
			servicePlans << servicePlan
		}

		morpheusContext.compute.cachePlans(servicePlans)
	}

	KeyPair findOrUploadKeypair(String apiKey, String publicKey, String keyName) {
		keyName = keyName ?: 'morpheus_do_plugin_key'
		println "find or update keypair for key $keyName"
		List keyList = apiService.makePaginatedApiCall(apiKey, '/v2/account/keys', 'ssh_keys', [:])
		println "keylist: $keyList"
		def match = keyList.find { publicKey.startsWith(it.public_key) }
		println("match: ${match} - list: ${keyList}")
		if (!match) {
			println 'key not found in DO'
			HttpPost httpPost = new HttpPost("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/account/keys")
			httpPost.entity = new StringEntity(JsonOutput.toJson([public_key: publicKey, name: keyName]))
			def respMap = apiService.makeApiCall(httpPost, apiKey)
			if (respMap.resp.statusLine.statusCode == 200) {
				match = new KeyPair(name: respMap.json.name, externalId: respMap.json.id, publicKey: respMap.json.public_key, publicFingerprint: respMap.json.fingerprint)
			} else {
				println 'failed to add DO ssh key'
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
