package com.morpheusdata.cloud

import com.morpheusdata.core.Plugin

/**
 * An example plugin to demonstrate an implementation of the NetworkProvider
 */
class GooglePlugin extends Plugin {

	@Override
	void initialize() {
		this.name = 'Google Plugin'
		GoogleCloudProvider cloudProvider = new GoogleCloudProvider(this, morpheus)
		GoogleOptionSourceProvider optionSourceProvider = new GoogleOptionSourceProvider(this, morpheus)
		GoogleNetworkProvider networkProvider = new GoogleNetworkProvider(this, morpheus)

		pluginProviders.put(cloudProvider.code, cloudProvider)
		pluginProviders.put(optionSourceProvider.code, optionSourceProvider)
		pluginProviders.put(networkProvider.code, networkProvider)
	}

	@Override
	void onDestroy() {

	}
}
