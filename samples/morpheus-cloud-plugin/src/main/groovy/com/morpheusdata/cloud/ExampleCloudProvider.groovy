package com.morpheusdata.cloud

import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.Cloud
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonParser
import groovy.json.JsonSlurper
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

class ExampleCloudProvider implements CloudProvider {
	Plugin plugin
	MorpheusContext morpheusContext
	private static final String DIGITAL_OCEAN_ENDPOINT = 'https://api.digitalocean.com'

	ExampleCloudProvider(Plugin plugin, MorpheusContext context) {
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
				optionSource: true,
				displayOrder: 0,
				fieldLabel: 'Username',
				required: true,
				inputType: OptionType.InputType.TEXT
		)
		OptionType ot2 = new OptionType(
				name: 'API Key',
				code: 'do-api-key',
				fieldName: 'doApiKey',
				optionSource: true,
				displayOrder: 1,
				fieldLabel: 'API Key',
				required: true,
				inputType: OptionType.InputType.PASSWORD
		)
		return [ot1, ot2]
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
		return getAvailableProvisioningProviders().find{it.providerCode == providerCode}
	}

	@Override
	ServiceResponse validate(Cloud zoneInfo) {
		return new ServiceResponse(success: true)
	}

	@Override
	ServiceResponse initializeCloud(Cloud zoneInfo) {
		println "Initializing Cloud: ${zoneInfo.code}"
		println "config: ${zoneInfo.configMap}"
		HttpGet http = new HttpGet("${DIGITAL_OCEAN_ENDPOINT}/v2/account")
		http.addHeader("Authorization", "Bearer ${zoneInfo.configMap.doApiKey}")

		CloseableHttpClient client = HttpClients.createDefault()
		def resp = client.execute(http)
		println "resp: ${resp}"
		String responseContent = EntityUtils.toString(resp.entity)
		println responseContent
		JsonSlurper slurper = new JsonSlurper()
		def json = slurper.parseText(responseContent)

		if(resp.statusLine.statusCode == 200 && json.account.status == 'active') {
			return new ServiceResponse(success: true, content: responseContent)
		} else {
			return new ServiceResponse(success: false, msg: resp?.statusLine?.statusCode, content: responseContent)
		}
	}

	@Override
	void refresh(Cloud zoneInfo) {
		println "cloud refresh has run for ${zoneInfo.code}"
	}

	@Override
	void refreshDaily(Cloud zoneInfo) {
		println "daily refresh run for ${zoneInfo.code}"
	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		return new ServiceResponse(success: true)
	}
}
