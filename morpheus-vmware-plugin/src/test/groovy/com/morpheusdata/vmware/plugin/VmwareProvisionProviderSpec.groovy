package com.morpheusdata.vmware.plugin

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
import com.morpheusdata.vmware.plugin.utils.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.annotations.NonNull
import spock.lang.Specification
import spock.lang.Subject

class VmwareProvisionProviderSpec extends Specification {

	@Subject
	VmwareProvisionProvider service

	MorpheusContext context
	MorpheusNetworkService networkContext
	MorpheusCloudService cloudContext
	MorpheusComputeServerService computeServerContext
	MorpheusComputeZonePoolService poolContext
	MorpheusProvisionService provisionService
	VmwarePlugin plugin

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
		plugin = Mock(VmwarePlugin)

		service = new VmwareProvisionProvider(plugin, context)
	}

	void "Validate defaults are set correctly"() {
		expect:
		service.getCode() == 'vmware-provision-provider-plugin'
		service.getName() == 'Vmware Plugin'
	}

	void "getAuthConfig - opts"() {
		given:
		def options = [serviceUrl: '1.1.1.1', serviceUsername: 'u1', servicePassword: 'password']

		when:
		def config = VmwareProvisionProvider.getAuthConfig(options)

		then:
		config.apiUrl == 'https://1.1.1.1/sdk'
		config.apiUsername == 'u1'
		config.apiPassword == 'password'
	}

	void "getAuthConfig - Cloud"() {
		given:
		Cloud cloud = new Cloud(serviceUrl: '1.1.1.1', serviceUsername: 'u1', servicePassword: 'password')

		when:
		def config = plugin.getAuthConfig(cloud)

		then:
		config.apiUrl == 'https://1.1.1.1/sdk'
		config.apiUsername == 'u1'
		config.apiPassword == 'password'
	}

	void "getApiUrl"() {
		expect:
		apiUrl == plugin.getVmwareApiUrl(url)

		where:
		url | apiUrl
		'vmware.io'         | 'https://vmware.io/sdk'
		'http://vmware.io'  | 'http://vmware.io/sdk'
		'https://vmware.io' | 'https://vmware.io/sdk'
	}
}
