package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.AccountCredential
import com.morpheusdata.model.Cloud
import groovy.util.logging.Slf4j

@Slf4j
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

	MorpheusContext getMorpheusContext() {
		return morpheus
	}

	Map getAuthConfig(Cloud cloud) {
		log.debug "getAuthConfig: ${cloud}"
		def rtn = [:]

		if(!cloud.accountCredentialLoaded) {
			AccountCredential accountCredential
			try {
				accountCredential = this.morpheus.cloud.loadCredentials(cloud.id).blockingGet()
			} catch(e) {
				// If there is no credential on the cloud, then this will error
			}
			cloud.accountCredentialLoaded = true
			cloud.accountCredentialData = accountCredential?.data
		}

		def doUsername
		if(cloud.accountCredentialData && cloud.accountCredentialData.containsKey('username')) {
			doUsername = cloud.accountCredentialData['username']
		} else {
			doUsername = cloud.configMap.doUsername
		}
		def doApiKey
		if(cloud.accountCredentialData && cloud.accountCredentialData.containsKey('password')) {
			doApiKey = cloud.accountCredentialData['password']
		} else {
			doApiKey = cloud.configMap.doApiKey
		}
		rtn.doUsername = doUsername
		rtn.doApiKey = doApiKey
		return rtn
	}
}
