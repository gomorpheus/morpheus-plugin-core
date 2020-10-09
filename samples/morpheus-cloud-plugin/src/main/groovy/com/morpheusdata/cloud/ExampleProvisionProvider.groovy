package com.morpheusdata.cloud

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.Workload
import com.morpheusdata.response.ServiceResponse

class ExampleProvisionProvider implements ProvisioningProvider {
	Plugin plugin
	MorpheusContext context

	ExampleProvisionProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.context = context
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		return null
	}

	@Override
	String getProvisionTypeCode() {
		return 'example-provision-type'
	}

	@Override
	String getName() {
		return null
	}

	@Override
	Boolean hasDatastores() {
		return null
	}

	@Override
	Boolean hasNetworks() {
		return null
	}

	@Override
	Integer getMaxNetworks() {
		return null
	}

	@Override
	ServiceResponse validateWorkload(Map opts) {
		return null
	}

	@Override
	ServiceResponse runWorkload(Workload workload, Map opts) {
		return null
	}

	@Override
	ServiceResponse stopWorkload(Workload workload) {
		return null
	}

	@Override
	ServiceResponse startWorkload(Workload workload) {
		return null
	}

	@Override
	ServiceResponse restartWorkload(Workload workload) {
		return null
	}

	@Override
	ServiceResponse removeWorkload(Workload workload, Map opts) {
		return null
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return this.context
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getProviderCode() {
		return 'example-cloud-provision'
	}

	@Override
	String getProviderName() {
		return 'Example Cloud Provision Provider'
	}
}
