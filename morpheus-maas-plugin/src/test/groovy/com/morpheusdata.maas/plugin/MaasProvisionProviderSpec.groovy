package com.morpheusdata.maas.plugin

import com.morpheusdata.core.cloud.MorpheusCloudContext
import com.morpheusdata.core.MorpheusComputeServerContext
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.cloud.MorpheusComputeZonePoolContext
import com.morpheusdata.core.network.MorpheusNetworkContext
import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Network
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import io.reactivex.Single
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Subject

class MaasProvisionProviderSpec extends Specification {

	@Subject
	MaasProvisionProvider service

	MorpheusContext context
	MorpheusNetworkContext networkContext
	MorpheusCloudContext cloudContext
	MorpheusComputeServerContext computeServerContext
	MorpheusComputeZonePoolContext poolContext
	MaasPlugin plugin

	void setup() {
		context = Mock(MorpheusContext)
		networkContext = Mock(MorpheusNetworkContext)
		cloudContext = Mock(MorpheusCloudContext)
		poolContext = Mock(MorpheusComputeZonePoolContext)
		computeServerContext = Mock(MorpheusComputeServerContext)
		context.getNetwork() >> networkContext
		context.getCloud() >> cloudContext
		context.getComputeServer() >> computeServerContext
		cloudContext.getPool() >> poolContext
		plugin = Mock(MaasPlugin)

		service = new MaasProvisionProvider(plugin, context)
	}

	void "Validate defaults are set correctly"() {
		expect:
		service.getCode() == 'maas'
		service.getName() == 'MaaS'
	}

	void "getAuthConfig"() {
		given:
		Cloud cloud = new Cloud(configMap: [serviceUrl: 'maas.io', serviceToken: 'consumerKey:apiKey:secretKey'])

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
		url | apiUrl
		'maas.io'         | 'https://maas.io'
		'http://maas.io'  | 'http://maas.io'
		'https://maas.io' | 'https://maas.io'
	}


	void "validateWorkload"() {
		expect:
		ServiceResponse response = service.validateWorkload(opts)
		out == response.success

		where:
		opts         | out
		[:]          | true
		[foo: 'bar'] | true
	}

	void "releaseMachine"() {
		given:
		Account cloudAccount = new Account(id: 1)
		Account serverAccount = new Account(id: 2)
		Cloud cloud = new Cloud(account: cloudAccount)
		ComputeServer server = new ComputeServer(cloud: cloud, account: serverAccount)
		Map authConfig = [:]
		Map opts = [:]
		GroovySpy(MaasComputeUtility, global: true)

		when:
		def resp = service.releaseMachine(server, authConfig, opts)

		then:
		resp.success
		1 * MaasComputeUtility.releaseMachine(_, _, [erase: true, quick_erase: true]) >> [success: true]
		1 * MaasComputeUtility.waitForMachineRelease(_, _, _) >> [success: 'SUCCESS']

		and: "account is reset"
		1 * computeServerContext.save({ List<ComputeServer> serverList -> serverList[0].account == cloudAccount }) >> Single.just(true)
	}

	void "releaseMachine - release"() {
		given:
		Cloud cloud = new Cloud(configMap: [releaseMode: 'release'])
		ComputeServer server = new ComputeServer(cloud: cloud)
		Map authConfig = [:]
		Map opts = [:]
		GroovySpy(MaasComputeUtility, global: true)

		when:
		def resp = service.releaseMachine(server, authConfig, opts)

		then:
		resp.success
		1 * MaasComputeUtility.releaseMachine(_, _, [erase: false, quick_erase: false]) >> [success: true]
		1 * MaasComputeUtility.waitForMachineRelease(_, _, _) >> [success: 'SUCCESS']
		1 * computeServerContext.save(_) >> Single.just(true)
	}

	void "releaseMachine - release fail"() {
		given:
		Cloud cloud = new Cloud(configMap: [releaseMode: 'release'])
		ComputeServer server = new ComputeServer(cloud: cloud)
		Map authConfig = [:]
		Map opts = [:]
		GroovySpy(MaasComputeUtility, global: true)

		when:
		def resp = service.releaseMachine(server, authConfig, opts)

		then:
		!resp.success
		resp.msg == 'Failed to release server'
		1 * MaasComputeUtility.releaseMachine(_, _, [erase: false, quick_erase: false]) >> [success: false]
		0 * MaasComputeUtility.waitForMachineRelease(_, _, _)
		0 * computeServerContext.save(_)
	}

	void "releaseMachine - release wait failure"() {
		given:
		Cloud cloud = new Cloud(configMap: [releaseMode: 'release'])
		ComputeServer server = new ComputeServer(cloud: cloud)
		Map authConfig = [:]
		Map opts = [:]
		GroovySpy(MaasComputeUtility, global: true)

		when:
		def resp = service.releaseMachine(server, authConfig, opts)

		then:
		!resp.success
		resp.msg == 'Failed waiting for server release'
		1 * MaasComputeUtility.releaseMachine(_, _, [erase: false, quick_erase: false]) >> [success: true]
		1 * MaasComputeUtility.waitForMachineRelease(_, _, _) >> [success: 'FAIL']
		1 * computeServerContext.save(_) >> Single.just(true)
	}

	void "stopServer"() {
		given:
		Cloud cloud = new Cloud(configMap: [serviceUrl: 'maas.io', serviceToken: 'consumerKey:apiKey:secretKey'])
		ComputeServerType serverType = new ComputeServerType(controlPower: true)
		ComputeServer server = new ComputeServer(id: 333, cloud: cloud, managed: true, computeServerType: serverType)
		GroovySpy(MaasComputeUtility, global: true)

		when:
		def resp = service.stopServer(server)

		then:
		resp.success
		1 * MaasComputeUtility.powerOffMachine(_, _, _) >> [success: true]
		1 * MaasComputeUtility.waitForMachinePowerState(_, _, 'off', _) >> [success: true]
		1 * computeServerContext.updatePowerState(333, ComputeServer.PowerState.off)
	}

	void "stopServer - unmanaged"() {
		given:
		Cloud cloud = new Cloud(configMap: [serviceUrl: 'maas.io', serviceToken: 'consumerKey:apiKey:secretKey'])
		ComputeServerType serverType = new ComputeServerType(controlPower: false)
		ComputeServer server = new ComputeServer(id: 333, cloud: cloud, managed: false, computeServerType: serverType)
		GroovySpy(MaasComputeUtility, global: true)

		when:
		def resp = service.stopServer(server)

		then:
		!resp.success
		0 * MaasComputeUtility.powerOffMachine(_, _, _) >> [success: true]
		0 * MaasComputeUtility.waitForMachinePowerState(_, _, 'off', _) >> [success: true]
		0 * computeServerContext.updatePowerState(333, ComputeServer.PowerState.off)
	}

	void "stopServer - power off failure"() {
		given:
		Cloud cloud = new Cloud(configMap: [serviceUrl: 'maas.io', serviceToken: 'consumerKey:apiKey:secretKey'])
		ComputeServerType serverType = new ComputeServerType(controlPower: true)
		ComputeServer server = new ComputeServer(id: 333, cloud: cloud, managed: true, computeServerType: serverType)
		GroovySpy(MaasComputeUtility, global: true)

		when:
		def resp = service.stopServer(server)

		then:
		!resp.success
		1 * MaasComputeUtility.powerOffMachine(_, _, _) >> [success: false]
		0 * MaasComputeUtility.waitForMachinePowerState(_, _, 'off', _)
		0 * computeServerContext.updatePowerState(333, ComputeServer.PowerState.off)
	}

	void "runServer"() {
		expect: "always false"
		def resp = service.runServer(new ComputeServer(), [:])
		!resp.success
		resp.error == 'error'
	}

	@Ignore
	void "runWorkload"() {
		given:
		Account cloudAccount = new Account(id: 222)
		Account containerAccount = new Account(id: 333)
		Cloud cloud = new Cloud(id: 1, account: cloudAccount, configMap: [serviceUrl: 'maas.io', serviceToken: 'consumerKey:apiKey:secretKey'])
		ComputeServer server = new ComputeServer(cloud: cloud, account: cloudAccount)
		Instance instance = new Instance(id: 777)
		Workload workload = new Workload(server: server, account: containerAccount, instance: instance)

		when:
		def resp = service.runWorkload(workload, runConfig)

		then:
		resp.success
		1 * computeServerContext.save({List<ComputeServer> servers -> servers[0].account == containerAccount}) >> Single.just(true)
		1 * cloudContext.buildContainerUserGroups(*_) >> Single.just([:])
		// TODO runBareMetal needs implementation and/or convert to spy
//		1 * service.runBareMetal(_, _) >> Single.just(new ServiceResponse(success: true))
	}

	void "finalizeBareMetal"() {
		given:
		Map runConfig = [server: new ComputeServer(), container: new Workload()]
		String privateIp = '192.168.1.29'
		String publicIp = '7.7.7.7'
		ServiceResponse runResults = new ServiceResponse(success: true, data: [ip_addresses: [privateIp, publicIp]])
		Map opts = [:]

		when:
		def res = service.finalizeBareMetal(runConfig, runResults, opts)

		then:
		res.success
		1 * networkContext.setComputeServerNetwork(_, privateIp, publicIp, null, null) >> Single.just(new Network())
		1 * computeServerContext.save(_) >> Single.just(true)
	}
}
