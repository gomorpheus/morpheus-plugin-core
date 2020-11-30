package com.morpheusdata.cloud

import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonSlurper
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class DigitalOceanCloudProvider implements CloudProvider {
	Plugin plugin
	MorpheusContext morpheusContext
	private static final String DIGITAL_OCEAN_ENDPOINT = 'https://api.digitalocean.com'

	DigitalOceanCloudProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
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
		if (!zoneInfo.configMap.doApiKey) {
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
		HttpGet accountGet = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/account")

		// check account
		def respMap = makeApiCall(accountGet, apiKey)
		if (respMap.resp.statusLine.statusCode == 200 && respMap.json.account.status == 'active') {
			serviceResponse = new ServiceResponse(success: true, content: respMap.json)

			// TODO
//			cacheDatacenters(opts)
			cacheSizes(apiKey)
//			cacheOsImages(opts)
//			cacheUserImages(opts)

//			morpheusContext.compute.createLayout()
//			morpheusContext.compute.createInstanceType()
		} else {
			serviceResponse = new ServiceResponse(success: false, msg: respMap.resp?.statusLine?.statusCode, content: respMap.json)
		}

		serviceResponse
	}

	@Override
	void refresh(Cloud cloudInfo) {
		println "cloud refresh has run for ${cloudInfo.code}"
		cacheSizes(cloudInfo.configMap.doApiKey)
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
		println "load datacenters for ${cloudInfo.code}"
		// TODO fetch dynamically given api key
//		HttpGet http = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/regions")
//		http.addHeader("Authorization", "Bearer ${cloudInfo.configMap.doApApiKeyiKey}")
		return [
				[value: 'nyc1', name: 'New York 1', available: true],
				[value: 'sfo1', name: 'San Francisco 1', available: true],
				[value: 'nyc2', name: 'New York 2', available: true],
				[value: 'ams2', name: 'Amsterdam 2', available: true],
				[value: 'sgp1', name: 'Singapore 1', available: true],
				[value: 'lon1', name: 'London 1', available: true],
				[value: 'nyc3', name: 'New York 3', available: true],
				[value: 'ams3', name: 'Amsterdam 3', available: true],
				[value: 'fra1', name: 'Frankfurt 1', available: true]
		]
	}

	Map makeApiCall(HttpRequestBase http, String apiKey) {
		CloseableHttpClient client = HttpClients.createDefault()
		try {
			http.addHeader("Authorization", "Bearer ${apiKey}")
			def resp = client.execute(http)
			try {
				println "resp: ${resp}"
				String responseContent = EntityUtils.toString(resp.entity)
				println responseContent
				JsonSlurper slurper = new JsonSlurper()
				def json = slurper.parseText(responseContent)
				[resp: resp, json: json]
			} catch (Exception e) {
				println e.message
			} finally {
				resp.close()
			}
		} catch (Exception e) {
			println e.message
		} finally {
			client.close()
		}
	}

	def cacheSizes(String apiKey) {
		HttpGet sizesGet = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/sizes")
		Map respMap = makeApiCall(sizesGet, apiKey)
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

	private getNameForSize(sizeData) {
		def memoryName = sizeData.memory < 1000 ? "${sizeData.memory} MB" : "${sizeData.memory.div(1024l)} GB"
		"Plugin Droplet ${sizeData.vcpus} CPU, ${memoryName} Memory, ${sizeData.disk}GB Storage"
	}
}
