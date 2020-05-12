package com.morpheusdata.tab

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

class TabPlugin extends Plugin {

	@Override
	void initialize() {
		CustomTabProvider customTabProvider = new CustomTabProvider(this, morpheusContext)
		this.pluginProviders.put(customTabProvider.providerCode, customTabProvider)
		this.setName("Custom Tabs")
		Permission pluginPermission = new Permission(name: 'Custom Instance Tab', code: 'custom-instance-tab', availableAccessTypes: [Permission.AccessType.full, Permission.AccessType.read, Permission.AccessType.none])
		morpheusContext.createPermission(pluginPermission).blockingGet()
	}

	@Override
	void onDestroy() {
		morpheusContext.deletePermission('custom-instance-tab').blockingGet()
	}
}
