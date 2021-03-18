package com.morpheusdata.reports

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

/**
 * Example Custom Reports Plugin
 */
class ReportsPlugin extends Plugin {

	@Override
	void initialize() {
		CustomReportProvider customReportProvider = new CustomReportProvider(this, morpheusContext)
		this.pluginProviders.put(customReportProvider.providerCode, customReportProvider)
		this.setName("Custom Reports")
		
	}

	@Override
	void onDestroy() {
	}
}
