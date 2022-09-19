package com.morpheusdata.infoblox

import com.morpheusdata.core.Plugin

class InfobloxPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-infoblox-plugin'
	}

	@Override
	void initialize() {
		InfobloxProvider infobloxProvider = new InfobloxProvider(this, morpheus)
		this.pluginProviders.put("infoblox", infobloxProvider)
		this.setName("Infoblox")
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
