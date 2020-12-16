package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Workload
import groovy.json.JsonSlurper
import org.apache.http.client.methods.HttpPost
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import java.nio.charset.StandardCharsets

class DigitalOceanProvisionProviderSpec extends Specification {

	@Subject
	DigitalOceanProvisionProvider provider
	@Shared
	DigitalOceanApiService apiService

	def setup() {
		Plugin plugin = Mock(Plugin)
		MorpheusContext context = Mock(MorpheusContext)
		provider = new DigitalOceanProvisionProvider(plugin, context)
		apiService = Mock(DigitalOceanApiService)
		provider.apiService = apiService
	}

	void "startWorkload"() {
		given:
		String responseContent = """
{"action":{"id":1092647540,"status":"in-progress","type":"power_on","started_at":"2020-12-16T18:06:52Z","completed_at":null,"resource_id":221818430,"resource_type":"droplet","region":{"name":"New York 1","slug":"nyc1","features":["backups","ipv6","metadata","install_agent","storage","image_transfer"],"available":true,"sizes":["s-1vcpu-1gb","512mb","s-1vcpu-2gb","1gb","s-3vcpu-1gb","s-2vcpu-2gb","s-1vcpu-3gb","s-2vcpu-4gb","2gb","s-4vcpu-8gb","m-1vcpu-8gb","c-2","4gb","c2-2vcpu-4gb","g-2vcpu-8gb","gd-2vcpu-8gb","m-16gb","s-8vcpu-16gb","m-2vcpu-16gb","s-6vcpu-16gb","c-4","8gb","c2-4vpcu-8gb","m3-2vcpu-16gb","g-4vcpu-16gb","so-2vcpu-16gb","m6-2vcpu-16gb","gd-4vcpu-16gb","m-32gb","so1_5-2vcpu-16gb","m-4vcpu-32gb","s-8vcpu-32gb","c-8","c2-8vpcu-16gb","m3-4vcpu-32gb","g-8vcpu-32gb","s-12vcpu-48gb","so-4vcpu-32gb","m6-4vcpu-32gb","gd-8vcpu-32gb","m-64gb","so1_5-4vcpu-32gb","m-8vcpu-64gb","s-16vcpu-64gb","c-16","32gb","c2-16vcpu-32gb","m3-8vcpu-64gb","g-16vcpu-64gb","s-20vcpu-96gb","48gb","so-8vcpu-64gb","m6-8vcpu-64gb","gd-16vcpu-64gb","m-128gb","so1_5-8vcpu-64gb","m-16vcpu-128gb","s-24vcpu-128gb","c-32","64gb","c2-32vpcu-64gb","m3-16vcpu-128gb","m-24vcpu-192gb","g-32vcpu-128gb","s-32vcpu-192gb","so-16vcpu-128gb","m6-16vcpu-128gb","gd-32vcpu-128gb","m3-24vcpu-192gb","g-40vcpu-160gb","so1_5-16vcpu-128gb","gd-40vcpu-160gb","so-24vcpu-192gb","m6-24vcpu-192gb","m3-32vcpu-256gb","so1_5-24vcpu-192gb","so-32vcpu-256gb","m6-32vcpu-256gb","so1_5-32vcpu-256gb"]},"region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		def json = slurper.parseText(responseContent)
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.startWorkload(workload)

		then:
		1 * apiService.makeApiCall(_, _) >> [resp: [statusLine: [statusCode: 201]], json: json]
		resp.success == true
		resp.data.id == 1092647540
	}

	void "startWorkload - fail"() {
		given:
		String responseContent = """
{"action":{"id":1092647540,"status":"in-progress","type":"power_on","started_at":"2020-12-16T18:06:52Z","completed_at":null,"resource_id":221818430,"resource_type":"droplet","region":{"name":"New York 1","slug":"nyc1","features":["backups","ipv6","metadata","install_agent","storage","image_transfer"],"available":true,"sizes":["s-1vcpu-1gb","512mb","s-1vcpu-2gb","1gb","s-3vcpu-1gb","s-2vcpu-2gb","s-1vcpu-3gb","s-2vcpu-4gb","2gb","s-4vcpu-8gb","m-1vcpu-8gb","c-2","4gb","c2-2vcpu-4gb","g-2vcpu-8gb","gd-2vcpu-8gb","m-16gb","s-8vcpu-16gb","m-2vcpu-16gb","s-6vcpu-16gb","c-4","8gb","c2-4vpcu-8gb","m3-2vcpu-16gb","g-4vcpu-16gb","so-2vcpu-16gb","m6-2vcpu-16gb","gd-4vcpu-16gb","m-32gb","so1_5-2vcpu-16gb","m-4vcpu-32gb","s-8vcpu-32gb","c-8","c2-8vpcu-16gb","m3-4vcpu-32gb","g-8vcpu-32gb","s-12vcpu-48gb","so-4vcpu-32gb","m6-4vcpu-32gb","gd-8vcpu-32gb","m-64gb","so1_5-4vcpu-32gb","m-8vcpu-64gb","s-16vcpu-64gb","c-16","32gb","c2-16vcpu-32gb","m3-8vcpu-64gb","g-16vcpu-64gb","s-20vcpu-96gb","48gb","so-8vcpu-64gb","m6-8vcpu-64gb","gd-16vcpu-64gb","m-128gb","so1_5-8vcpu-64gb","m-16vcpu-128gb","s-24vcpu-128gb","c-32","64gb","c2-32vpcu-64gb","m3-16vcpu-128gb","m-24vcpu-192gb","g-32vcpu-128gb","s-32vcpu-192gb","so-16vcpu-128gb","m6-16vcpu-128gb","gd-32vcpu-128gb","m3-24vcpu-192gb","g-40vcpu-160gb","so1_5-16vcpu-128gb","gd-40vcpu-160gb","so-24vcpu-192gb","m6-24vcpu-192gb","m3-32vcpu-256gb","so1_5-24vcpu-192gb","so-32vcpu-256gb","m6-32vcpu-256gb","so1_5-32vcpu-256gb"]},"region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		def json = slurper.parseText(responseContent)
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.startWorkload(workload)

		then:
		1 * apiService.makeApiCall(_, _) >> [resp: [statusLine: [statusCode: 400]], json: json]
		resp.success == false
	}

	void "stopWorkload"() {
		given:
		String shutdownResponse = """
{"action":{"id":1092647540,"status":"in-progress","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		def json = slurper.parseText(shutdownResponse)
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.stopWorkload(workload)

		then:
		1 * apiService.makeApiCall({ HttpPost post ->
			['{"type":"shutdown"}'] == new BufferedReader(new InputStreamReader(post.entity.content, StandardCharsets.UTF_8)).collect()
		}, _) >> [resp: [statusLine: [statusCode: 201]], json: json]
		0 * apiService.makeApiCall({ HttpPost post ->
			['{"type":"power_off"}'] == new BufferedReader(new InputStreamReader(post.entity.content, StandardCharsets.UTF_8)).collect()
		}, _)
		resp.success == true
		resp.data.id == 1092647540
	}

	void "stopWorkload and power off"() {
		given:
		String shutdownResponse = """
{"action":{"id":1092647540,"status":"in-progress","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""

		String powerOffResponse = """
{"action":{"id":1092647540,"status":"power_off","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		def shutdownJson = slurper.parseText(shutdownResponse)
		def powerOffJson = slurper.parseText(powerOffResponse)
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.stopWorkload(workload)

		then:
		1 * apiService.makeApiCall({ HttpPost post ->
			['{"type":"shutdown"}'] == new BufferedReader(new InputStreamReader(post.entity.content, StandardCharsets.UTF_8)).collect()
		}, _) >> [resp: [statusLine: [statusCode: 400]], json: shutdownJson]
		1 * apiService.makeApiCall({ HttpPost post ->
			['{"type":"power_off"}'] == new BufferedReader(new InputStreamReader(post.entity.content, StandardCharsets.UTF_8)).collect()
		}, _) >> [resp: [statusLine: [statusCode: 201]], json: powerOffJson]
		resp.success == true
		resp.data.id == 1092647540
	}

	void "stopWorkload and power off - fail"() {
		given:
		String shutdownResponse = """
{"action":{"id":1092647540,"status":"in-progress","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""

		String powerOffResponse = """
{"action":{"id":1092647540,"status":"power_off","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		def shutdownJson = slurper.parseText(shutdownResponse)
		def powerOffJson = slurper.parseText(powerOffResponse)
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.stopWorkload(workload)

		then:
		1 * apiService.makeApiCall({ HttpPost post ->
			['{"type":"shutdown"}'] == new BufferedReader(new InputStreamReader(post.entity.content, StandardCharsets.UTF_8)).collect()
		}, _) >> [resp: [statusLine: [statusCode: 400]], json: shutdownJson]
		1 * apiService.makeApiCall({ HttpPost post ->
			['{"type":"power_off"}'] == new BufferedReader(new InputStreamReader(post.entity.content, StandardCharsets.UTF_8)).collect()
		}, _) >> [resp: [statusLine: [statusCode: 400]], json: powerOffJson]
		resp.success == false
	}

}
