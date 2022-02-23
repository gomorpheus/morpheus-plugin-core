package com.morpheusdata.vmware.plugin

import com.morpheusdata.core.Plugin

class VmwarePlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-vmware-plugin'
	}

	@Override
	void initialize() {
		this.setName('Vmware Plugin')
		def vmwareProvision = new VmwareProvisionProvider(this, this.morpheus)
		def vmwareCloud = new VmwareCloudProvider(this, this.morpheus)
		def vmwareOptionSourceProvider = new VmwareOptionSourceProvider(this, morpheus)

		this.pluginProviders.put(vmwareProvision.code, vmwareProvision)
		this.pluginProviders.put(vmwareCloud.code, vmwareCloud)
		this.pluginProviders.put(vmwareOptionSourceProvider.code, vmwareOptionSourceProvider)
	}

	@Override
	void onDestroy() {

	}
}
