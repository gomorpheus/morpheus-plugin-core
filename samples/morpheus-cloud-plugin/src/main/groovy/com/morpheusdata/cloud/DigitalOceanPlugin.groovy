package com.morpheusdata.cloud

import com.morpheusdata.core.Plugin

class DigitalOceanPlugin extends Plugin {

	@Override
	void initialize() {
		this.name = 'Digital Ocean Plugin'
		DigitalOceanCloudProvider cloudProvider = new DigitalOceanCloudProvider(this, morpheusContext)
		DigitalOceanProvisionProvider provisionProvider = new DigitalOceanProvisionProvider(this, morpheusContext)
		DigitalOceanBackupProvider backupProvider = new DigitalOceanBackupProvider(this, morpheusContext)
		pluginProviders.put(provisionProvider.providerCode, provisionProvider)
		pluginProviders.put(cloudProvider.providerCode, cloudProvider)
		pluginProviders.put(backupProvider.providerCode, backupProvider)
	}

	@Override
	void onDestroy() {

	}
}
