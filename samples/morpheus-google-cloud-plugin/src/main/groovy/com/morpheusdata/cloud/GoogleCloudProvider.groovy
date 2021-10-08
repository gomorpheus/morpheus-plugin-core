package com.morpheusdata.cloud

import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.*
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import io.reactivex.Observable

@Slf4j
class GoogleCloudProvider implements CloudProvider {
	Plugin plugin
	MorpheusContext morpheusContext
	GoogleApiService apiService

	GoogleCloudProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
		apiService = new GoogleApiService()
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
	}

	@Override
	Collection<NetworkType> getNetworkTypes() {
		def networkConfig = [
				code: 'google-plugin-network',
				creatable: true,
				deletable: true,
				dhcpServerEditable: true,
				name: 'Google Plugin Network'
		]
		NetworkType googleNetwork = new NetworkType(networkConfig)

		OptionType ot1 = new OptionType(
				name: 'MTU',
				code: 'google-plugin-network-mtu',
				fieldName: 'mtu',
				displayOrder: 0,
				fieldLabel: 'MTU',
				required: true,
				inputType: OptionType.InputType.SELECT,
				defaultValue: 1460,
				fieldContext: 'config',
				optionSource: 'googlePluginMtu'
		)
		OptionType ot2 = new OptionType(
				name: 'Auto Create',
				code: 'google-plugin-network-auto-create-subnets',
				fieldName: 'autoCreate',
				displayOrder: 1,
				fieldLabel: 'Auto Create Subnets',
				required: true,
				defaultValue: 'on',
				inputType: OptionType.InputType.CHECKBOX,
				dependsOn: 'network.zone.id',
				fieldContext: 'config'
		)
		googleNetwork.setOptionTypes([ot1, ot2])

		def subnetConfig = [
				code: 'google-plugin-subnet',
				creatable: true,
				deletable: true,
				dhcpServerEditable: true,
				canAssignPool: true,
				cidrRequired: true,
				cidrEditable: true,
				name: 'Google Plugin Subnet'
		]
		NetworkSubnetType googleSubnet = new NetworkSubnetType(subnetConfig)
		OptionType subot1 = new OptionType(
				name: 'Name',
				code: 'google-plugin-subnet-name',
				fieldName: 'name',
				displayOrder: 0,
				fieldLabel: 'Name',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		OptionType subot2 = new OptionType(
				name: 'Description',
				code: 'google-plugin-subnet-description',
				fieldName: 'description',
				displayOrder: 1,
				fieldLabel: 'Description',
				required: false,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'domain'
		)
		googleSubnet.setOptionTypes([subot1, subot2])

		googleNetwork.setNetworkSubnetTypes([googleSubnet])

		[googleNetwork]
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'google-plugin'
	}

	@Override
	String getName() {
		return 'Google Plugin'
	}

	@Override
	String getDescription() {
		return 'Google Plugin Description'
	}

	@Override
	Boolean getHasComputeZonePools() {
		return false
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		OptionType ot1 = new OptionType(
				name: 'Client Email',
				code: 'google-plugin-client-email',
				fieldName: 'clientEmail',
				displayOrder: 0,
				fieldLabel: 'Client Email',
				required: true,
				inputType: OptionType.InputType.TEXT,
				fieldContext: 'config'
		)
		OptionType ot2 = new OptionType(
				name: 'Private Key',
				code: 'google-plugin-private-key',
				fieldName: 'privateKey',
				displayOrder: 1,
				fieldLabel: 'Private Key',
				required: true,
				inputType: OptionType.InputType.PASSWORD,
				fieldContext: 'config'
		)
		OptionType ot3 = new OptionType(
				name: 'Project ID',
				code: 'google-plugin-project-id',
				fieldName: 'projectId',
				optionSource: 'googlePluginProjects',
				displayOrder: 2,
				fieldLabel: 'Project ID',
				required: true,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'google-plugin-client-email,google-plugin-private-key',
				fieldContext: 'config'
		)
		OptionType ot4 = new OptionType(
				name: 'Region',
				code: 'google-plugin-region',
				fieldName: 'googleRegionId',
				optionSource: 'googlePluginRegions',
				displayOrder: 3,
				fieldLabel: 'Region',
				required: true,
				inputType: OptionType.InputType.SELECT,
				dependsOn: 'google-plugin-project-id',
				fieldContext: 'config'
		)
		return [ot1, ot2, ot3, ot4]
	}

	@Override
	Collection<ComputeServerType> getComputeServerTypes() {
		return null
	}

	@Override
	Collection<ProvisioningProvider> getAvailableProvisioningProviders() {
		return null
	}

	@Override
	Collection<BackupProvider> getAvailableBackupProviders() {
		return null
	}

	@Override
	ProvisioningProvider getProvisioningProvider(String providerCode) {
		return getAvailableProvisioningProviders()?.find { it.code == providerCode }
	}

	@Override
	ServiceResponse validate(Cloud zoneInfo) {
		log.debug "validating Cloud: ${zoneInfo.code}"
		return new ServiceResponse(success: true)
	}

	@Override
	ServiceResponse initializeCloud(Cloud cloud) {
		log.debug "Initializing Cloud: ${cloud.code}"
		log.debug "config: ${cloud.configMap}"
		ServiceResponse.success()
	}

	@Override
	void refresh(Cloud cloudInfo) {
		log.debug "cloud refresh has run for ${cloudInfo.code}"
	}

	@Override
	void refreshDaily(Cloud cloudInfo) {
		log.debug "daily refresh run for ${cloudInfo.code}"
	}

	@Override
	ServiceResponse deleteCloud(Cloud cloudInfo) {
		return new ServiceResponse(success: true)
	}
}
