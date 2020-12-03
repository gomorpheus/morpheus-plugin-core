package com.morpheusdata.maas.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.network.MorpheusNetworkContext
import com.morpheusdata.model.Cloud
import spock.lang.Specification
import spock.lang.Subject

class MaasProvisionProviderSpec extends Specification {

	@Subject
	MaasProvisionProvider service

	MorpheusContext context
	MorpheusNetworkContext networkContext
	MaasPlugin plugin

	void setup() {
		context = Mock(MorpheusContext)
		networkContext = Mock(MorpheusNetworkContext)
		context.getNetwork() >> networkContext
		plugin = Mock(MaasPlugin)

		service = new MaasProvisionProvider(plugin, context)
	}

	void "Validate defaults are set correctly"() {
		expect:
		service.getProviderCode() == 'maas-provision'
		service.getProviderName() == 'MaaS'
	}

	void "getAuthConfig"() {
		given:
		Cloud cloud = new Cloud(configMap: [serviceUrl: 'lumen.com', serviceToken: 'consumerKey:apiKey:secretKey'])

		when:
		def config = service.getAuthConfig(cloud)

		then:
		config.apiUrl == 'https://' + cloud.configMap.serviceUrl
		config.apiVersion == '2.0'
		config.oauth.apiKey == 'apiKey'
		config.oauth.apiSecret == 'secretKey'
		config.oauth.consumerKey == 'consumerKey'
		config.basePath == '/MAAS/api/2.0'
	}

	void "getApiUrl"() {
		expect:
		apiUrl == service.getApiUrl(url)

		where:
		url                 | apiUrl
		'lumen.com'         | 'https://lumen.com'
		'http://lumen.com'  | 'http://lumen.com'
		'https://lumen.com' | 'https://lumen.com'
	}
}
