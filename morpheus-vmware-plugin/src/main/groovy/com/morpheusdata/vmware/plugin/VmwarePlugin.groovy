package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.AccountCredential
import com.morpheusdata.model.Cloud
import groovy.util.logging.Slf4j

@Slf4j
class VmwarePlugin extends Plugin {

	private String cloudProviderCode

	@Override
	String getCode() {
		return 'morpheus-vmware-plugin'
	}

	@Override
	void initialize() {
		this.setName('Vmware Plugin')
		def vmwareProvision = new VmwareProvisionProvider(this, this.morpheus)
		def vmwareCloud = new VmwareCloudProvider(this, this.morpheus)
		cloudProviderCode = vmwareCloud.code
		def vmwareOptionSourceProvider = new VmwareOptionSourceProvider(this, morpheus)
		def vmwareIPAMProvider = new VmwareIPAMProvider(this, morpheus)

		this.pluginProviders.put(vmwareProvision.code, vmwareProvision)
		this.pluginProviders.put(vmwareCloud.code, vmwareCloud)
		this.pluginProviders.put(vmwareOptionSourceProvider.code, vmwareOptionSourceProvider)
		this.pluginProviders.put(vmwareIPAMProvider.code, vmwareIPAMProvider)
	}

	@Override
	void onDestroy() {

	}

	def MorpheusContext getMorpheusContext() {
		this.morpheus
	}

	def VmwareCloudProvider getCloudProvider() {
		this.getProviderByCode(cloudProviderCode)
	}

	def getAuthConfig(Cloud cloud) {
		log.debug "getAuthConfig: ${cloud}"
		def rtn = [:]

		if(!cloud.accountCredentialLoaded) {
			AccountCredential accountCredential
			try {
				accountCredential = this.morpheus.cloud.loadCredentials(cloud.id).blockingGet()
			} catch(e) {
				// If there is no credential on the cloud, then this will error
				// TODO: Change to using 'maybe' rather than 'blockingGet'?
			}
			cloud.accountCredentialLoaded = true
			cloud.accountCredentialData = accountCredential?.data
		}

		def username
		def password
		if(cloud.accountCredentialData && cloud.accountCredentialData.containsKey('username')) {
			username = cloud.accountCredentialData['username']
		} else {
			username = cloud.serviceUsername
		}
		if(cloud.accountCredentialData && cloud.accountCredentialData.containsKey('password')) {
			password = cloud.accountCredentialData['password']
		} else {
			password = cloud.servicePassword
		}

		rtn.apiUrl = VmwareProvisionProvider.getVmwareApiUrl(cloud.serviceUrl)
		rtn.apiUsername = username
		rtn.apiPassword = password
		rtn.apiVersion = cloud.getConfigProperty('apiVersion') ?: '6.7'
		return rtn
	}
}
