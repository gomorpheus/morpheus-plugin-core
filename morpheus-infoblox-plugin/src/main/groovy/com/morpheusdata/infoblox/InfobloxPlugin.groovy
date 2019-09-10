package com.morpheusdata.infoblox

import com.morpheusdata.core.Plugin

class InfobloxPlugin extends Plugin {

	@Override
	void initialize() {
		InfobloxProvider infobloxProvider = new InfobloxProvider(this, morpheusContext)
		this.pluginProviders.put("infoblox", infobloxProvider)

		println this.providers.add(infobloxProvider.providedPoolTypes)
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		//nothing to do for now
	}
}


//AccountIntegrationType
// -> has many - optiontype // for UI
