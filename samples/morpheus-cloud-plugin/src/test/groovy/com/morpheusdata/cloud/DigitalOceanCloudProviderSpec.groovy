package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import org.apache.http.HttpEntity
import org.apache.http.StatusLine
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.impl.client.CloseableHttpClient
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class DigitalOceanCloudProviderSpec extends Specification {

	@Subject
	DigitalOceanCloudProvider provider
	@Shared
	CloseableHttpClient client

	def setup() {
		Plugin plugin = Mock(Plugin)
		MorpheusContext context = Mock(MorpheusContext)
		provider = new DigitalOceanCloudProvider(plugin, context)
		client = Mock(CloseableHttpClient)
		provider.client = client
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
		CloseableHttpResponse response = Mock(CloseableHttpResponse)
		HttpEntity entity = Mock(HttpEntity)
		StatusLine statusLine = Mock(StatusLine)

		when:
		def resp = provider.initializeCloud(cloud)

		then:
		1 * client.execute(_) >> response
		1 * response.entity >> entity
		2 * response.statusLine >> statusLine
		2 * statusLine.statusCode >> 400
		!resp.success
		resp.msg = 400
	}
}
