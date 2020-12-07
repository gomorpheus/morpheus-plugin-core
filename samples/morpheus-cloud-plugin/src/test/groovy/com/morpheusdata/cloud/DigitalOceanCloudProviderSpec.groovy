package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class DigitalOceanCloudProviderSpec extends Specification {

	@Subject
	DigitalOceanCloudProvider provider
	@Shared
	DigitalOceanApiService apiService

	def setup() {
		Plugin plugin = Mock(Plugin)
		MorpheusContext context = Mock(MorpheusContext)
		provider = new DigitalOceanCloudProvider(plugin, context)
		apiService = Mock(DigitalOceanApiService)
		provider.apiService = apiService
	}

	void "validate - fail"() {
		given:
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user'])

		when:
		def res = provider.validate(cloud)

		then:
		!res.success
		res.msg == 'Choose a datacenter'
	}

	void "validate"() {
		given:
		Cloud cloud = new Cloud(configMap: [doApiKey: 'abc123', doUsername: 'user', datacenter: 'nyc1'])

		when:
		def res = provider.validate(cloud)

		then:
		res.success
	}

	void "getNameForSize"() {
		expect:
		expected == provider.getNameForSize(sizeData)

		where:
		sizeData                                | expected
		[vcpus: 1, memory: 25, disk: 25]        | 'Plugin Droplet 1 CPU, 25 MB Memory, 25 GB Storage'
		[vcpus: 3, memory: 25 * 1024, disk: 500] | 'Plugin Droplet 3 CPU, 25 GB Memory, 500 GB Storage'
	}

	void "initializeCloud - fail"() {
		given:
		Cloud cloud = new Cloud(code: 'doCloud', configMap: [doApiKey: 'abc123'])

		when:
		def resp = provider.initializeCloud(cloud)

		then:
		1 * apiService.makeApiCall(_, _) >> [resp: [success: false, statusLine: [statusCode: 400]]]
		!resp.success
		resp.msg == '400'
	}
}
