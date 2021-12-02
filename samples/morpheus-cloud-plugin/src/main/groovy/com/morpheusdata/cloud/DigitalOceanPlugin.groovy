package com.morpheusdata.cloud

import com.morpheusdata.core.Plugin

class DigitalOceanPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-digital-ocean-plugin'
	}

	@Override
	void initialize() {
		this.name = 'Digital Ocean Plugin'
		DigitalOceanCloudProvider cloudProvider = new DigitalOceanCloudProvider(this, morpheus)
		DigitalOceanProvisionProvider provisionProvider = new DigitalOceanProvisionProvider(this, morpheus)
		DigitalOceanBackupProvider backupProvider = new DigitalOceanBackupProvider(this, morpheus)
		DigitalOceanOptionSourceProvider optionSourceProvider = new DigitalOceanOptionSourceProvider(this, morpheus)
		pluginProviders.put(provisionProvider.code, provisionProvider)
		pluginProviders.put(cloudProvider.code, cloudProvider)
		pluginProviders.put(backupProvider.code, backupProvider)
		pluginProviders.put(optionSourceProvider.code, optionSourceProvider)
	}

	@Override
	void onDestroy() {

	}
}
