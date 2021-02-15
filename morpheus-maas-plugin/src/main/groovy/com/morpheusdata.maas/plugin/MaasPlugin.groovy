package com.morpheusdata.maas.plugin

import com.morpheusdata.core.Plugin

/**
 * Metal as a Service Plugin
 */
class MaasPlugin extends Plugin {

	@Override
	void initialize() {
		this.setName('MaaS Plugin')
		def lumenProvision = new MaasProvisionProvider(this, this.morpheusContext)
		this.pluginProviders.put(lumenProvision.providerName, lumenProvision)
	}

	@Override
	void onDestroy() {

	}
}
