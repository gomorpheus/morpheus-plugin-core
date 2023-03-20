package com.morpheusdata.approvals

import com.morpheusdata.core.Plugin

/**
 * Example plugin integrating with an ITSM solution. In this case, a simple file system check
 */
class ApprovalsPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-approvals-plugin'
	}

	@Override
	void initialize() {
		this.setName('Approvals Plugin')
		FileWatcherProvider provider = new FileWatcherProvider(this, morpheus)
		this.pluginProviders.put(provider.code, provider)
	}

	@Override
	void onDestroy() {

	}
}
