package com.morpheusdata.maas.plugin

import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.MorpheusComputeServerService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.cloud.MorpheusComputeZonePoolService
import com.morpheusdata.core.network.MorpheusNetworkService
import com.morpheusdata.core.provisioning.MorpheusProvisionService
import com.morpheusdata.model.Account
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.Instance
import com.morpheusdata.model.Network
import com.morpheusdata.model.UsersConfiguration
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import spock.lang.Specification
import spock.lang.Subject

class MaasProvisionProviderSpec extends Specification {

	@Subject
	MaasProvisionProvider service

	MorpheusContext context
	MorpheusNetworkService networkContext
	MorpheusCloudService cloudContext
	MorpheusComputeServerService computeServerContext
	MorpheusComputeZonePoolService poolContext
	MorpheusProvisionService provisionService
	MaasPlugin plugin

	void setup() {
		context = Mock(MorpheusContext)
		networkContext = Mock(MorpheusNetworkService)
		cloudContext = Mock(MorpheusCloudService)
		poolContext = Mock(MorpheusComputeZonePoolService)
		provisionService = Mock(MorpheusProvisionService)

		computeServerContext = Mock(MorpheusComputeServerService)
		context.getNetwork() >> networkContext
		context.getCloud() >> cloudContext
		context.getComputeServer() >> computeServerContext
		context.getProvision() >> provisionService
		cloudContext.getPool() >> poolContext
		plugin = Mock(MaasPlugin)

		service = new MaasProvisionProvider(plugin, context)
	}

	void "Validate defaults are set correctly"() {
		expect:
		service.getCode() == 'maas-provision-provider-plugin'
		service.getName() == 'MaaS Plugin'
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

	void "runWorkload"() {
		given:
		GroovySpy(MaasComputeUtility, global: true)
		Account cloudAccount = new Account(id: 222)
		Account containerAccount = new Account(id: 333)
		Cloud cloud = new Cloud(id: 1, account: cloudAccount, configMap: [serviceUrl: 'maas.io', serviceToken: 'consumerKey:apiKey:secretKey'])
		ComputeServer server = new ComputeServer(cloud: cloud, account: cloudAccount)
		Instance instance = new Instance(id: 777)
		Workload workload = new Workload(server: server, account: containerAccount, instance: instance)
		Map runConfig = [:]

		when:
		def resp = service.runWorkload(workload, runConfig)

		then:
		resp.success
		3 * computeServerContext.save({List<ComputeServer> servers -> servers[0].account == containerAccount}) >> Single.just(true)
		0 * cloudContext.buildContainerUserGroups(*_) >> Single.just([:])

		and: "other method mocks"
		1 * computeServerContext.get(_) >> Single.just(server)
		1 * provisionService.getUserConfig(_,_,_) >> Single.just(new UsersConfiguration())
		1 * provisionService.buildCloudConfigOptions(*_) >> Single.just(new HashMap<String,Object>())
		1 * provisionService.buildCloudUserData(*_) >> Single.just(new ArrayList<String>())
		1 * MaasComputeUtility.deployMachine(_, _, _, _) >> [success: true]
		1 * MaasComputeUtility.waitForMachine(*_) >> [success: true, data: [ip_addresses: ['a.b.c.d', 'e.f.g.h']]]
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
