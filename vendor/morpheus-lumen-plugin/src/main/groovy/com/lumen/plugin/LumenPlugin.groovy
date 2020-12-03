package com.lumen.plugin

import com.morpheusdata.core.Plugin

/**
 * Lumen Plugin
 */
class LumenPlugin extends Plugin {

	@Override
	void initialize() {
		this.setName('Lumen Plugin')
		def lumenProvision = new LumenProvisionProvider(this, this.morpheusContext)
		this.pluginProviders.put(lumenProvision.providerName, lumenProvision)
	}

	@Override
	void onDestroy() {

	}
}
