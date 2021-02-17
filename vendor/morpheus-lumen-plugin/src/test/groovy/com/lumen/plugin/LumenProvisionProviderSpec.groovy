package com.lumen.plugin

import com.morpheusdata.MorpheusContextImpl
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.MorpheusNetworkContext
import com.morpheusdata.model.Account
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.ComputeZone
import com.morpheusdata.model.Container
import spock.lang.Specification
import spock.lang.Subject

class LumenProvisionProviderSpec extends Specification {

	@Subject
	LumenProvisionProvider service

	MorpheusContext context
	MorpheusNetworkContext networkContext
	LumenPlugin plugin

	void setup() {
		context = Mock(MorpheusContextImpl)
		networkContext = Mock(MorpheusNetworkContext)
		context.getNetwork() >> networkContext
		plugin = Mock(LumenPlugin)

		service = new LumenProvisionProvider(plugin, context)
	}

	void "Validate defaults are set correctly"() {
		expect:
		service.getProviderCode() == 'lumen-provision'
		service.getProviderName() == 'Lumen'
		service.DEFAULT_CIDR_MASK == 29
		service.getDefaultCustomer() == [accountNumber:'morpheus', accountName:'morpheus']
	}

	void "validateContainer"() {
		when:"called with no params"
		def resp = service.validateContainer().blockingGet()
		then:
		resp.success

		when:"called with map"
		resp = service.validateContainer([:]).blockingGet()
		then:
		resp.success
	}

	void "createContainerResources - bad container"() {
		given:
		def badContainer = new Container()
		when:
		def result = service.createContainerResources(badContainer).blockingGet()

		then:
		!result.success
		result.msg == "unknown error preparing network"
	}

	void "createContainerResources - container"() {
		given: "A valid container"
		def account = new Account()
		def zone = new ComputeZone()
		def server = new ComputeServer(zone: zone)
		def container = new Container(account: account, server: server)

		when:
		def result = service.createContainerResources(container).blockingGet()

		then:
		result.success
	}


}
