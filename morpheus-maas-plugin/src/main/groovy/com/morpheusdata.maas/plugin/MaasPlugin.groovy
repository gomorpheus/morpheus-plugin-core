package com.morpheusdata.maas.plugin

import com.morpheusdata.core.Plugin

/**
 * Metal as a Service Plugin
 */
class MaasPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-maas-plugin'
	}

	@Override
	void initialize() {
		this.setName('MaaS Plugin')
		def maasProvision = new MaasProvisionProvider(this, this.morpheus)
		def maasCloud = new MaasCloudProvider(this, this.morpheus)
		def maasOptionSourceProvider = new MaasOptionSourceProvider(this, morpheus)
		def maasNetworkProvider = new MaasNetworkProvider(this, morpheus)

		this.pluginProviders.put(maasProvision.code, maasProvision)
		this.pluginProviders.put(maasCloud.code, maasCloud)
		this.pluginProviders.put(maasOptionSourceProvider.code, maasOptionSourceProvider)
		this.pluginProviders.put(maasNetworkProvider.code, maasNetworkProvider)
	}

	@Override
	void onDestroy() {

	}
}
