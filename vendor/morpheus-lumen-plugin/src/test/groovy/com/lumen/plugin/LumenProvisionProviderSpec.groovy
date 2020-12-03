package com.lumen.plugin

import com.morpheusdata.MorpheusContextImpl
import com.morpheusdata.core.MorpheusContext
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class LumenProvisionProviderSpec extends Specification {

	@Shared MorpheusContext context
	@Shared LumenPlugin plugin
	@Subject @Shared LumenProvisionProvider provider

	void setup() {
		context = Mock(MorpheusContextImpl)
		plugin = Mock(LumenPlugin)
		provider = new LumenProvisionProvider(plugin, context)
	}

	void "Provider valid"() {
		expect:
		provider
		provider.providerName == "Lumen"
		provider.providerCode == "lumen-provision"
		and:
		provider.provisionComplete(null, null).success
		provider.deProvisionStarted(null, null).success
	}
}
