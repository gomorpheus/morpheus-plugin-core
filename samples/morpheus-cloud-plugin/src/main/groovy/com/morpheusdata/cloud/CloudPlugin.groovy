package com.morpheusdata.cloud

import com.morpheusdata.core.Plugin

class CloudPlugin extends Plugin {

	@Override
	void initialize() {
		this.name = 'Digital Ocean Plugin'
		ExampleCloudProvider cloudProvider = new ExampleCloudProvider(this, morpheusContext)
		ExampleProvisionProvider provisionProvider = new ExampleProvisionProvider(this, morpheusContext)
		pluginProviders.put(provisionProvider.providerCode, provisionProvider)
		pluginProviders.put(cloudProvider.providerCode, cloudProvider)
	}

	@Override
	void onDestroy() {

	}
}
