package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.MorpheusComputeServerService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.cloud.MorpheusCloudService
import com.morpheusdata.core.cloud.MorpheusComputeZonePoolService
import com.morpheusdata.core.network.MorpheusNetworkService
import com.morpheusdata.core.provisioning.MorpheusProvisionService
import com.morpheusdata.model.Cloud
import spock.lang.Specification
import spock.lang.Subject

class VmwarePluginSpec extends Specification {
	@Subject
	VmwarePlugin plugin

	MorpheusContext context
	MorpheusNetworkService networkContext
	MorpheusCloudService cloudContext
	MorpheusComputeServerService computeServerContext
	MorpheusComputeZonePoolService poolContext
	MorpheusProvisionService provisionService

	void setup() {
		context = Mock(MorpheusContext)
		networkContext = Mock(MorpheusNetworkService)
		cloudContext = Mock(MorpheusCloudService)
		poolContext = Mock(MorpheusComputeZonePoolService)
		provisionService = Mock(MorpheusProvisionService)

		computeServerContext = Mock(MorpheusComputeServerService)


		plugin = new VmwarePlugin()
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
}
