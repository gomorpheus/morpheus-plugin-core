package com.morpheusdata.tab

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

/**
 * Example Custom Tab Plugin
 */
class TabPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-tab-plugin'
	}

	@Override
	void initialize() {
		CustomTabProvider customTabProvider = new CustomTabProvider(this, morpheus)
		this.pluginProviders.put(customTabProvider.code, customTabProvider)
		this.setName("Custom Tabs")
		this.setPermissions([Permission.build('Custom Instance Tab','custom-instance-tab', [Permission.AccessType.none, Permission.AccessType.full])])
	}

	@Override
	void onDestroy() {
	}
}
