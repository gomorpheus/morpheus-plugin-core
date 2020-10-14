package com.morpheusdata.cloud

import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.ComputeServerType
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.PlatformType
import com.morpheusdata.model.Cloud
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
		return this.morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getProviderCode() {
		return 'example-cloud'
	}

	@Override
	String getProviderName() {
		return 'Example Cloud'
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		OptionType ot1 = new OptionType(
				name: 'Cloud Access Key',
				code: 'sample-cloud-access-key',
				fieldName: 'cloudAccessKey',
				optionSource: true,
				displayOrder: 0,
				fieldLabel: 'API Access Key',
				required: true,
				inputType: OptionType.InputType.TEXT
		)
		OptionType ot2 = new OptionType(
				name: 'Cloud Secret Key',
				code: 'sample-cloud-secret-key',
				fieldName: 'cloudSecretsKey',
				optionSource: true,
				displayOrder: 1,
				fieldLabel: 'API Secret Key',
				required: true,
				inputType: OptionType.InputType.PASSWORD
		)
		return [ot1, ot2]
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		def type1 = new ComputeServerType(name: 'Example Cloud Type', code: 'example-cloud-type', platform: PlatformType.mac)
		return [type1]
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
	ServiceResponse validate(Cloud zoneInfo) {
		return new ServiceResponse(success: true)
	}

	@Override
	void initializeZone(Cloud zoneInfo) {
		println "Initializing Cloud: ${zoneInfo.code}"
	}

	@Override
	void refresh(Cloud zoneInfo) {
		println 'refresh has run'
	}

	@Override
	void refreshDaily(Cloud zoneInfo) {
		println 'daily refresh run'
	}
}
