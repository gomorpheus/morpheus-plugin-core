package com.morpheusdata.maas.plugin

import com.morpheusdata.core.Plugin

/**
 * Metal as a Service Plugin
 */
class MaasPlugin extends Plugin {

	@Override
	void initialize() {
		this.setName('MaaS Plugin')
		def maasProvision = new MaasProvisionProvider(this, this.morpheusContext)
		def maasCloud = new MaasCloudProvider(this, this.morpheusContext)
		this.pluginProviders.put(maasProvision.providerCode, maasProvision)
		this.pluginProviders.put(maasCloud.providerCode, maasCloud)
	}

	@Override
	void onDestroy() {

	}
}
