package com.morpheusdata.reports

import com.morpheusdata.core.Plugin

/**
 * Example Custom Reports Plugin
 */
class UserProvisioningReportPlugin extends Plugin {

	@Override
	void initialize() {
		CustomReportProvider customReportProvider = new CustomReportProvider(this, morpheus)
		this.pluginProviders.put(customReportProvider.code, customReportProvider)
		this.setName("JW Custom Report")
		
	}

	@Override
	void onDestroy() {
	}
}
