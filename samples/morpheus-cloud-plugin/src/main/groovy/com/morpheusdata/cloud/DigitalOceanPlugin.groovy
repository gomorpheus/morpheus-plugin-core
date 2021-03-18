package com.morpheusdata.cloud

import com.morpheusdata.core.Plugin

class DigitalOceanPlugin extends Plugin {

	@Override
	void initialize() {
		this.name = 'Digital Ocean Plugin'
		DigitalOceanCloudProvider cloudProvider = new DigitalOceanCloudProvider(this, morpheus)
		DigitalOceanProvisionProvider provisionProvider = new DigitalOceanProvisionProvider(this, morpheus)
		DigitalOceanBackupProvider backupProvider = new DigitalOceanBackupProvider(this, morpheus)
		pluginProviders.put(provisionProvider.code, provisionProvider)
		pluginProviders.put(cloudProvider.code, cloudProvider)
		pluginProviders.put(backupProvider.code, backupProvider)
	}

	@Override
	void onDestroy() {

	}
}
