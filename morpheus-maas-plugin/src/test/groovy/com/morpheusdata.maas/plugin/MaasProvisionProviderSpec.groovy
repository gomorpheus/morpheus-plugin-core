package com.morpheusdata.maas.plugin

import com.morpheusdata.MorpheusContextImpl
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusNetworkContext
import spock.lang.Specification
import spock.lang.Subject

class MaasProvisionProviderSpec extends Specification {

	@Subject
	MaasProvisionProvider service

	MorpheusContext context
	MorpheusNetworkContext networkContext
	MaasPlugin plugin

	void setup() {
		context = Mock(MorpheusContextImpl)
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
}
