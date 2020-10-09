package com.morpheusdata.cloud

import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.Zone
import com.morpheusdata.response.ServiceResponse

class ExampleCloudProvider implements CloudProvider {
	Plugin plugin
	MorpheusContext morpheusContext

	ExampleCloudProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return null
	}

	@Override
	Plugin getPlugin() {
		return null
	}

	@Override
	String getProviderCode() {
		return 'example-cloud'
	}

	@Override
	String getProviderName() {
		return 'ExampleCloud'
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		return null
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		return [new ComputeServerType(name: 'Example Cloud Type', code: 'example-cloud-type', platform: PlatformType.mac)]
	}

	@Override
	Collection<ProvisioningProvider> getAvailableProvisioningProviders() {
		return plugin.getProvidersByType(ProvisioningProvider) as Collection<ProvisioningProvider>
	}

	@Override
	ProvisioningProvider getProvisioningProvider(String providerCode) {
		return getAvailableProvisioningProviders().find{it.providerCode == providerCode}
	}

	@Override
	ServiceResponse validate(Zone zoneInfo) {
		return null
	}

	@Override
	void initializeZone(Zone zoneInfo) {

	}

	@Override
	void refresh(Zone zoneInfo) {

	}

	@Override
	void refreshDaily(Zone zoneInfo) {

	}
}
