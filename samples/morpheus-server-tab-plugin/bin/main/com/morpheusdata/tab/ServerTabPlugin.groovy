package com.morpheusdata.tab

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

/**
 * Example Custom Tab Plugin
 */
class ServerTabPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-server-tab-plugin'
	}

	@Override
	void initialize() {
		CustomServerTabProvider customTabProvider = new CustomServerTabProvider(this, morpheus)
		this.pluginProviders.put(customTabProvider.code, customTabProvider)
		this.setName("Custom Server Tabs")
		this.setPermissions([Permission.build('Custom Server Tab','custom-server-tab', [Permission.AccessType.none, Permission.AccessType.full])])
	}

	@Override
	void onDestroy() {
	}
}
