package com.morpheusdata.cloud

import com.morpheusdata.core.backup.BackupProvider;
import com.morpheusdata.core.CloudProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.ProvisioningProvider
import com.morpheusdata.core.util.SyncTask
import com.morpheusdata.model.*
import com.morpheusdata.request.ValidateCloudRequest
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

	GoogleCloudProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.morpheusContext
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
	Icon getIcon() {
		return null
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
	Boolean hasComputeZonePools() {
		return false
	}

	@Override
	Boolean hasNetworks() {
		return false
	}

	@Override
	Boolean hasFolders() {
		return false
	}

	@Override
	Boolean hasCloudInit() {
		false
	}

	@Override
	Boolean hasDatastores() {
		return false
	}

	@Override
	Boolean supportsDistributedWorker() {
		false
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
	ServiceResponse startServer(ComputeServer computeServer) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse stopServer(ComputeServer computeServer) {
		return ServiceResponse.success()
	}

	@Override
	ServiceResponse deleteServer(ComputeServer computeServer) {
		return ServiceResponse.success()
	}

	@Override
	ProvisioningProvider getProvisioningProvider(String providerCode) {
		return getAvailableProvisioningProviders()?.find { it.code == providerCode }
	}

	@Override
	Collection<NetworkType> getNetworkTypes(){
		return GoogleCommon.getNetworkTypes()
	}

	@Override
	Collection<StorageVolumeType> getStorageVolumeTypes() {
		return null
	}

	@Override
	Collection<StorageControllerType> getStorageControllerTypes() {
		return null
	}

	@Override
	ServiceResponse validate(Cloud zoneInfo, ValidateCloudRequest validateCloudRequest) {
		log.debug "validating Cloud: ${zoneInfo.code}"
		def jsonSettings = this.morpheusContext.getSettings(this.plugin).blockingGet()
		def settings = new groovy.json.JsonSlurper().parseText(jsonSettings ?: '{}')
		println "value of setting textField ${settings['textField']}"
		return new ServiceResponse(success: true)
	}

	@Override
	ServiceResponse initializeCloud(Cloud cloud) {
		log.debug "Initializing Cloud: ${cloud.code}"
		log.debug "config: ${cloud.configMap}"
		ServiceResponse.success()
	}

	@Override
	ServiceResponse refresh(Cloud cloudInfo) {
		log.debug "cloud refresh has run for ${cloudInfo.code}"
		ServiceResponse.success()
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
