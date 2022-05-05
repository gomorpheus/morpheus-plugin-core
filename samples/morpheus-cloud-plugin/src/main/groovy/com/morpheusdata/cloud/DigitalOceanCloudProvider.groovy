package com.morpheusdata.cloud

import com.morpheusdata.cloud.sync.DatacentersSync
import com.morpheusdata.cloud.sync.ImagesSync
import com.morpheusdata.cloud.sync.SizesSync
import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import org.apache.http.client.methods.HttpDelete
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity

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
	Icon getIcon() {
		return new Icon(path:"digital-ocean-plugin.svg", darkPath: "digital-ocean-plugin.svg")
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
	Boolean hasComputeZonePools() {
		return false
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
						enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: false, creatable: false, computeService: null,
						displayOrder: 17, hasAutomation: true, reconfigureSupported: true, provisionTypeCode: 'do-provider',
						containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, guestVm: true,
				),

				new ComputeServerType(code: 'digitalOceanVm2', name: 'DigitalOcean VM Instance', description: '', platform: PlatformType.linux,
						enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: false, creatable: false, computeService: null,
						displayOrder: 0, hasAutomation: true, reconfigureSupported: true, provisionTypeCode: 'do-provider',
						containerHypervisor: false, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.guest, guestVm: true,
				),

				//docker
				new ComputeServerType(code: 'digitalOceanLinux2', name: 'DigitalOcean Docker Host', description: '', platform: PlatformType.linux,
						enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: false, creatable: true, computeService: null,
						displayOrder: 16, hasAutomation: true, reconfigureSupported: true,
						containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.host, clusterType: ComputeServerType.ClusterType.docker,
						computeTypeCode: 'docker-host',
				),

				//kubernetes
				new ComputeServerType(code: 'digitalOceanKubeMaster2', name: 'Digital Ocean Kubernetes Master', description: '', platform: PlatformType.linux,
						reconfigureSupported: true, enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: true, creatable: true,
						supportsConsoleKeymap: true, computeService: null, displayOrder: 10,
						hasAutomation: true, containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.host, clusterType: ComputeServerType.ClusterType.kubernetes,
						computeTypeCode: 'kube-master',
						optionTypes: [

						]
				),
				new ComputeServerType(code: 'digitalOceanKubeWorker2', name: 'Digital Ocean Kubernetes Worker', description: '', platform: PlatformType.linux,
						reconfigureSupported: true, enabled: true, selectable: false, externalDelete: true, managed: true, controlPower: true, controlSuspend: true, creatable: true,
						supportsConsoleKeymap: true, computeService: null, displayOrder: 10,
						hasAutomation: true, containerHypervisor: true, bareMetalHost: false, vmHypervisor: false, agentType: ComputeServerType.AgentType.host, clusterType: ComputeServerType.ClusterType.kubernetes,
						computeTypeCode: 'kube-worker',
						optionTypes: [

						]
				),
				//unmanaged discovered type
				new ComputeServerType(code: 'digitalOceanUnmanaged', name: 'Digital Ocean VM', description: 'Digital Ocean VM', platform: PlatformType.none, agentType: ComputeServerType.AgentType.guest,
						enabled: true, selectable: false, externalDelete: true, managed: false, controlPower: true, controlSuspend: false, creatable: false, computeService: null,
						displayOrder: 99, hasAutomation: false, provisionTypeCode: 'do-provider',
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

			(new DatacentersSync(cloud, morpheusContext, apiService)).execute()
			(new SizesSync(cloud, morpheusContext, apiService)).execute()
			(new ImagesSync(cloud, morpheusContext, apiService)).execute()

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
	ServiceResponse refresh(Cloud cloud) {
		log.debug "cloud refresh has run for ${cloud.code}"
		(new SizesSync(cloud, morpheusContext, apiService)).execute()
		(new ImagesSync(cloud, morpheusContext, apiService)).execute()
		return ServiceResponse.success()
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {
		log.debug "daily refresh run for ${cloudInfo.code}"
	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		return new ServiceResponse(success: true)
	}

	@Override
	ServiceResponse startServer(ComputeServer computeServer) {
		String dropletId = computeServer.externalId
		String apiKey = computeServer.cloud.configMap.doApiKey
		log.debug "startServer: ${dropletId}"
		if (!dropletId) {
			log.debug "no Droplet ID provided"
			return new ServiceResponse(success: false, msg: 'No Droplet ID provided')
		}
		def body = ['type': 'power_on']
		apiService.performDropletAction(dropletId, body, apiKey)
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		String dropletId = computeServer.externalId
		String apiKey = computeServer.cloud.configMap.doApiKey
		log.debug "stopServer: ${dropletId}"
		if (!dropletId) {
			log.debug "no Droplet ID provided"
			return new ServiceResponse(success: false, msg: 'No Droplet ID provided')
		}
		def body = ['type': 'shutdown']
		apiService.performDropletAction(dropletId, body, apiKey)
	}

	@Override
	ServiceResponse deleteServer(ComputeServer computeServer) {
		String dropletId = computeServer.externalId
		log.debug "deleteServer for server: ${dropletId}"
		if (!dropletId) {
			log.debug "no Droplet ID provided"
			return new ServiceResponse(success: false, msg: 'No Droplet ID provided')
		}
		HttpDelete httpDelete = new HttpDelete("${DigitalOceanApiService.DIGITAL_OCEAN_ENDPOINT}/v2/droplets/${dropletId}")
		Map respMap = apiService.makeApiCall(httpDelete, computeServer.cloud.configMap.doApiKey)
		if (respMap?.resp?.statusLine?.statusCode == 204) {
			return new ServiceResponse(success: true)
		} else {
			return new ServiceResponse(success: false, content: respMap?.json, msg: respMap?.resp?.statusLine?.statusCode, error: respMap?.json)
		}
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
}
