package com.morpheusdata.tab

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

class TabPlugin extends Plugin {

	@Override
	void initialize() {
		CustomTabProvider customTabProvider = new CustomTabProvider(this, morpheusContext)
		this.pluginProviders.put(customTabProvider.providerCode, customTabProvider)
		this.setName("Custom Tabs")
		Permission pluginPermission = new Permission('Custom Instance Tab','custom-instance-tab', [Permission.AccessType.none, Permission.AccessType.read, Permission.AccessType.full])
		morpheusContext.createPermission(pluginPermission).blockingGet()
	}

	@Override
	void onDestroy() {
		morpheusContext.deletePermission('custom-instance-tab').blockingGet()
	}
}
