package com.morpheusdata.approvals

import com.morpheusdata.core.Plugin

/**
 * Example plugin integrating with an ITSM solution. In this case, a simple file system check
 */
class ApprovalsPlugin extends Plugin {

	@Override
	void initialize() {
		this.setName('Approvals Plugin')
		FileWatcherProvider provider = new FileWatcherProvider(this, morpheusContext)
		this.pluginProviders.put(provider.providerCode, provider)
	}

	@Override
	void onDestroy() {

	}
}
