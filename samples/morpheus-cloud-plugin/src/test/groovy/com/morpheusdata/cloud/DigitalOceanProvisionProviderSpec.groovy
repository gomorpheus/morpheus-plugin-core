package com.morpheusdata.cloud

import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.MorpheusComputeServerService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusVirtualImageService
import com.morpheusdata.core.provisioning.MorpheusProvisionService
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Instance
import com.morpheusdata.model.KeyPair
import com.morpheusdata.model.NetworkConfiguration
import com.morpheusdata.model.ServicePlan
import com.morpheusdata.model.UsersConfiguration
import com.morpheusdata.model.Workload
import com.morpheusdata.model.projection.VirtualImageIdentityProjection
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonSlurper
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.annotations.NonNull
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
	@Shared
	MorpheusContext context
	@Shared
	MorpheusCloudService cloudContext
	@Shared
	MorpheusVirtualImageService virtualImageContext
	MorpheusProvisionService provisionService
	MorpheusComputeServerService computeServerContext

	def setup() {
		Plugin plugin = Mock(Plugin)
		context = Mock(MorpheusContext)
		cloudContext = Mock(MorpheusCloudService)
		virtualImageContext = Mock(MorpheusVirtualImageService)
		provisionService = Mock(MorpheusProvisionService)
		computeServerContext = Mock(MorpheusComputeServerService)
		context.getComputeServer() >> computeServerContext
		context.getCloud() >> cloudContext
		context.getVirtualImage() >> virtualImageContext
		context.getProvision() >> provisionService
		provider = new DigitalOceanProvisionProvider(plugin, context)
		apiService = Mock(DigitalOceanApiService)
		provider.apiService = apiService
	}

	void "startWorkload"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.startWorkload(workload)

		then:
		1 * apiService.performDropletAction('drop1111', ['type': 'power_on'], 'abc123') >> new ServiceResponse(success: true, data: actionSuccessJson('power_on').action)
		resp.success == true
		resp.data.id == 1092647540
	}

	void "startWorkload - no droplet id"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: null, cloud: cloud)

		when:
		def resp = provider.startWorkload(workload)

		then:
		0 * apiService.performDropletAction(_, _, _)
		resp.success == false
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
		1 * apiService.performDropletAction(_, _, _) >> new ServiceResponse(success: false)
		resp.success == false
	}

	void "stopWorkload"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.stopWorkload(workload)

		then:
		1 * apiService.makeApiCall(_ as HttpPost, _) >> [resp: [statusLine: [statusCode: 201]], json: actionInProgressJson('shutdown')]
		1 * apiService.checkActionComplete(_, _) >> new ServiceResponse(success: true, data: actionSuccessJson('shutdown').action)
		resp.success == true
		resp.data.id == 1092647540
	}

	void "stopWorkload - no droplet id"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: null, cloud: cloud)

		when:
		def resp = provider.stopWorkload(workload)

		then:
		0 * apiService.makeApiCall(_, _)
		resp.success == false
		resp.msg == 'No Droplet ID provided'
	}

	void "stopWorkload and power off"() {
		given:
		String shutdownResponse = """
{"action":{"id":1092647540,"status":"in-progress","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		def shutdownJson = slurper.parseText(shutdownResponse)
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)

		when:
		def resp = provider.stopWorkload(workload)

		then:
		1 * apiService.makeApiCall({ HttpPost post ->
			['{"type":"shutdown"}'] == new BufferedReader(new InputStreamReader(post.entity.content, StandardCharsets.UTF_8)).collect()
		}, _) >> [resp: [statusLine: [statusCode: 400]], json: shutdownJson]
		1 * apiService.performDropletAction(_, [type: 'power_off'], _) >> new ServiceResponse(success: true, data: actionSuccessJson('power_off').action)
		resp.success == true
		resp.data.id == 1092647540
	}

	void "stopWorkload and power off - fail"() {
		given:
		String shutdownResponse = """
{"action":{"id":1092647540,"status":"in-progress","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""

		String powerOffResponse = """
{"action":{"id":1092647540,"status":"failed","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
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
		1 * apiService.performDropletAction(_, [type: 'power_off'], _) >> new ServiceResponse(success: false)
		resp.success == false
	}

	void "runWorkload"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		ServicePlan plan = new ServicePlan(externalId: 'plan1')
		Workload workload = new Workload(plan: plan)
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)
		Map serverOpts = [
				'name'             : 'droplet1',
				'datacenterName'   : 'nyc1',
				'sizeRef'          : 's-1vcpu-1gb',
				'imageRef'         : 'ubuntu-16-04-x64',
				'sshKeys'          : [12345],
				'doBackups'        : false,
				'ipv6'             : "true",
				'userData'         : null,
				'privateNetworking': "true"
		]
		String createServerResponse = """{
  "droplet": {
    "id": 3164494,
    "name": "example.com",
    "memory": 1024,
    "vcpus": 1,
    "disk": 25,
    "locked": true,
    "status": "new"
    }
}"""
		JsonSlurper slurper = new JsonSlurper()
		def createServerJson = slurper.parseText(createServerResponse)

		when:
		def resp = provider.runWorkload(workload, serverOpts)

		then:
		2 * computeServerContext.save(*_) >> Single.just(true)
		1 * apiService.makeApiCall(_, _) >> [resp: [statusLine: [statusCode: 202]], json: createServerJson]
		1 * cloudContext.findOrGenerateKeyPair(_) >> Single.just(new KeyPair(id: 789, externalId: 'key1'))
		1 * provisionService.getUserConfig(_,_,_) >> Single.just(new UsersConfiguration())
		1 * provisionService.getNetworkConfig(*_) >> Single.just(new NetworkConfiguration())

		resp.success == true
		resp.data.externalId == "3164494"
	}

	void "runWorkload - fail"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		ServicePlan plan = new ServicePlan(externalId: 'plan1')
		Workload workload = new Workload(plan: plan)
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)
		Map serverOpts = [:]
		String createServerResponse = """
{"action":{"id":1092647540,"status":"fail","type":"shutdown","started_at":"2020-12-16T18:06:52Z","region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		def createServerJson = slurper.parseText(createServerResponse)

		when:
		def resp = provider.runWorkload(workload, serverOpts)

		then:
		1 * computeServerContext.save(*_) >> Single.just(true)
		1 * provisionService.getNetworkConfig(*_) >> Single.just(new NetworkConfiguration())
		1 * provisionService.getUserConfig(_,_,_) >> Single.just(new UsersConfiguration())
		1 * apiService.makeApiCall(_, _) >> [resp: [statusLine: [statusCode: 400]], json: createServerJson]
		1 * cloudContext.findOrGenerateKeyPair(_) >> Single.just(new KeyPair(id: 789, externalId: 'key1'))
		resp.success == false
		resp.msg == '400'
	}

	void "runWorkload - missing apiKey"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [:])
		Workload workload = new Workload()
		workload.server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)
		Map serverOpts = [:]

		when:
		def resp = provider.runWorkload(workload, serverOpts)

		then:
		0 * apiService.makeApiCall(_, _)
		0 * cloudContext.findOrGenerateKeyPair(_)
		resp.success == false
		resp.msg == 'No API Key provided'
	}

	void "resizeServer"() {
		given:
		Cloud cloud = new Cloud(name: 'Digital Ocean', configMap: [doApiKey: 'abc123'])
		Workload workload = new Workload()
		ComputeServer server = new ComputeServer(name: 'serv1', externalId: 'drop1111', cloud: cloud)
		workload.server = server
		ServicePlan plan = new ServicePlan(externalId: 'plan_123')
		Instance instance = new Instance(id: 42)
		Map opts = [:]

		when:
		def resp = provider.resizeWorkload(instance, workload, plan, opts)

		then:
		resp.success
		1 * apiService.performDropletAction('drop1111', ['type': 'resize', disk: true, size: 'plan_123'], 'abc123') >> new ServiceResponse(success: true, data: actionSuccessJson('resize').action)
	}

	def actionSuccessJson(String type) {
		String responseContent = """
{"action":{"id":1092647540,"status":"completed","type":"$type","started_at":"2020-12-16T18:06:52Z","completed_at":"2020-12-16T18:07:52Z","resource_id":221818430,"resource_type":"droplet","region":{"name":"New York 1","slug":"nyc1","features":["backups","ipv6","metadata","install_agent","storage","image_transfer"],"available":true,"sizes":["s-1vcpu-1gb","512mb","s-1vcpu-2gb","1gb","s-3vcpu-1gb","s-2vcpu-2gb","s-1vcpu-3gb","s-2vcpu-4gb","2gb","s-4vcpu-8gb","m-1vcpu-8gb","c-2","4gb","c2-2vcpu-4gb","g-2vcpu-8gb","gd-2vcpu-8gb","m-16gb","s-8vcpu-16gb","m-2vcpu-16gb","s-6vcpu-16gb","c-4","8gb","c2-4vpcu-8gb","m3-2vcpu-16gb","g-4vcpu-16gb","so-2vcpu-16gb","m6-2vcpu-16gb","gd-4vcpu-16gb","m-32gb","so1_5-2vcpu-16gb","m-4vcpu-32gb","s-8vcpu-32gb","c-8","c2-8vpcu-16gb","m3-4vcpu-32gb","g-8vcpu-32gb","s-12vcpu-48gb","so-4vcpu-32gb","m6-4vcpu-32gb","gd-8vcpu-32gb","m-64gb","so1_5-4vcpu-32gb","m-8vcpu-64gb","s-16vcpu-64gb","c-16","32gb","c2-16vcpu-32gb","m3-8vcpu-64gb","g-16vcpu-64gb","s-20vcpu-96gb","48gb","so-8vcpu-64gb","m6-8vcpu-64gb","gd-16vcpu-64gb","m-128gb","so1_5-8vcpu-64gb","m-16vcpu-128gb","s-24vcpu-128gb","c-32","64gb","c2-32vpcu-64gb","m3-16vcpu-128gb","m-24vcpu-192gb","g-32vcpu-128gb","s-32vcpu-192gb","so-16vcpu-128gb","m6-16vcpu-128gb","gd-32vcpu-128gb","m3-24vcpu-192gb","g-40vcpu-160gb","so1_5-16vcpu-128gb","gd-40vcpu-160gb","so-24vcpu-192gb","m6-24vcpu-192gb","m3-32vcpu-256gb","so1_5-24vcpu-192gb","so-32vcpu-256gb","m6-32vcpu-256gb","so1_5-32vcpu-256gb"]},"region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		slurper.parseText(responseContent)
	}

	def actionInProgressJson(String type) {
		String responseContent = """
{"action":{"id":1092647540,"status":"in-progress","type":"$type","started_at":"2020-12-16T18:06:52Z","completed_at":null,"resource_id":221818430,"resource_type":"droplet","region":{"name":"New York 1","slug":"nyc1","features":["backups","ipv6","metadata","install_agent","storage","image_transfer"],"available":true,"sizes":["s-1vcpu-1gb","512mb","s-1vcpu-2gb","1gb","s-3vcpu-1gb","s-2vcpu-2gb","s-1vcpu-3gb","s-2vcpu-4gb","2gb","s-4vcpu-8gb","m-1vcpu-8gb","c-2","4gb","c2-2vcpu-4gb","g-2vcpu-8gb","gd-2vcpu-8gb","m-16gb","s-8vcpu-16gb","m-2vcpu-16gb","s-6vcpu-16gb","c-4","8gb","c2-4vpcu-8gb","m3-2vcpu-16gb","g-4vcpu-16gb","so-2vcpu-16gb","m6-2vcpu-16gb","gd-4vcpu-16gb","m-32gb","so1_5-2vcpu-16gb","m-4vcpu-32gb","s-8vcpu-32gb","c-8","c2-8vpcu-16gb","m3-4vcpu-32gb","g-8vcpu-32gb","s-12vcpu-48gb","so-4vcpu-32gb","m6-4vcpu-32gb","gd-8vcpu-32gb","m-64gb","so1_5-4vcpu-32gb","m-8vcpu-64gb","s-16vcpu-64gb","c-16","32gb","c2-16vcpu-32gb","m3-8vcpu-64gb","g-16vcpu-64gb","s-20vcpu-96gb","48gb","so-8vcpu-64gb","m6-8vcpu-64gb","gd-16vcpu-64gb","m-128gb","so1_5-8vcpu-64gb","m-16vcpu-128gb","s-24vcpu-128gb","c-32","64gb","c2-32vpcu-64gb","m3-16vcpu-128gb","m-24vcpu-192gb","g-32vcpu-128gb","s-32vcpu-192gb","so-16vcpu-128gb","m6-16vcpu-128gb","gd-32vcpu-128gb","m3-24vcpu-192gb","g-40vcpu-160gb","so1_5-16vcpu-128gb","gd-40vcpu-160gb","so-24vcpu-192gb","m6-24vcpu-192gb","m3-32vcpu-256gb","so1_5-24vcpu-192gb","so-32vcpu-256gb","m6-32vcpu-256gb","so1_5-32vcpu-256gb"]},"region_slug":"nyc1"}}
"""
		JsonSlurper slurper = new JsonSlurper()
		slurper.parseText(responseContent)
	}

	void "optionTypes"() {
		when:
		def options = provider.optionTypes

		then:
		options.size() == 1
		options.first().optionSource == 'pluginImage'
	}

}
