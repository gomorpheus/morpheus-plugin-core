package com.morpheusdata.bigip

import com.morpheusdata.core.Plugin

class BigIpPlugin extends Plugin {
	@Override
	String getCode() {
		return 'morpheus-bigip-plugin'
	}

	@Override
	void initialize() {
		BigIpProvider bigipProvider = new BigIpProvider(this, morpheus)
		BigIpOptionSourceProvider bigIpOptionSourceProvider = new BigIpOptionSourceProvider(this, morpheus)
		this.pluginProviders.put(bigipProvider.code, bigipProvider)
		this.pluginProviders.put(bigIpOptionSourceProvider.code, bigIpOptionSourceProvider)
		this.setName("BigIp")
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		//nothing to do for now
	}

	BigIpProvider getProvider() {
		return getProviderByCode(BigIpProvider.PROVIDER_CODE)
	}
}
