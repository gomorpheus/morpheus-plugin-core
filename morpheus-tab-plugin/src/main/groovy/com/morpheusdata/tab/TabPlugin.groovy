package com.morpheusdata.tab

import com.morpheusdata.core.Plugin

class TabPlugin extends Plugin {

	@Override
	void initialize() {
		CustomTabProvider customTabProvider = new CustomTabProvider(this, morpheusContext)
		this.pluginProviders.put(customTabProvider.providerCode, customTabProvider)
		this.setName("Custom Tabs")
	}

	@Override
	void onDestroy() {

	}
}
