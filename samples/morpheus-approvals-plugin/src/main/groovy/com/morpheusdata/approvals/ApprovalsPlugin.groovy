package com.morpheusdata.approvals

import com.morpheusdata.core.Plugin

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
