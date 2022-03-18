package com.morpheusdata.tab

import com.morpheusdata.core.Plugin
import com.morpheusdata.views.HandlebarsRenderer
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
		this.setRenderer(new HandlebarsRenderer(this.classLoader))
		this.controllers.add(new CustomTabController(this, morpheus))
	}

	@Override
	void onDestroy() {
	}

	@Override
	public List<Permission> getPermissions() {
		Permission permission = new Permission('Custom Tab Plugin', 'customTabPlugin', [Permission.AccessType.full])
		return [permission];
	}
}
