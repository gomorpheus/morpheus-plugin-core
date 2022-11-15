package com.morpheusdata.cloud

import com.morpheusdata.core.backup.BackupExecutionProvider
import com.morpheusdata.core.backup.BackupJobProvider
import com.morpheusdata.core.backup.BackupProvider;
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.BackupRestoreProvider
import com.morpheusdata.core.backup.BackupTypeProvider
import com.morpheusdata.model.BackupIntegration
import com.morpheusdata.model.BackupJob
import com.morpheusdata.model.BackupProviderType
import com.morpheusdata.model.BackupType
import com.morpheusdata.model.Icon
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.ReplicationType
import com.morpheusdata.response.ServiceResponse

class DigitalOceanBackupProvider implements BackupProvider {
	MorpheusContext context
	Plugin plugin
	DigitalOceanApiService apiService


	DigitalOceanBackupProvider(Plugin plugin, MorpheusContext context) {
		this.context = context
		this.plugin = plugin
		this.apiService = new DigitalOceanApiService()
	}

	@Override
	MorpheusContext getMorpheus() {
		return this.context
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getCode() {
		return 'do-plugin-backup'
	}

	@Override
	String getName() {
		return 'DigitalOcean Plugin Backup Provider'
	}

	@Override
	Icon getIcon() {
		return null
	}

	@Override
	String getViewSet() {
		return null
	}

	@Override
	Boolean getEnabled() {
		return null
	}

	@Override
	Boolean getCreatable() {
		return null
	}

	@Override
	Boolean getDownloadEnabled() {
		return null
	}

	@Override
	Boolean getHasAddToJob() {
		return null
	}

	@Override
	Boolean getHasBackups() {
		return null
	}

	@Override
	Boolean hasCancelBackup() {
		return null
	}

	@Override
	Boolean getHasCloneJob() {
		return null
	}

	@Override
	Boolean getHasCopyToStore() {
		return null
	}

	@Override
	Boolean getHasCreateJob() {
		return null
	}

	@Override
	Boolean getHasJobs() {
		return null
	}

	@Override
	Boolean getHasOptionalJob() {
		return null
	}

	@Override
	Boolean getHasReplication() {
		return null
	}

	@Override
	Boolean getHasReplicationGroups() {
		return null
	}

	@Override
	Boolean getHasRepositories() {
		return null
	}

	@Override
	Boolean getHasRetentionCount() {
		return null
	}

	@Override
	Boolean getHasSchedule() {
		return null
	}

	@Override
	Boolean getHasServers() {
		return null
	}

	@Override
	Boolean getHasSites() {
		return null
	}

	@Override
	Boolean hasStorageProvider() {
		return null
	}

	@Override
	Boolean getHasStreamToStore() {
		return null
	}

	@Override
	Boolean getRestoreExistingEnabled() {
		return null
	}

	@Override
	Boolean getRestoreNewEnabled() {
		return null
	}

	@Override
	Collection<OptionType> getOptionTypes() {
		return null
	}

	@Override
	Collection<OptionType> getReplicationGroupOptionTypes() {
		return null
	}

	@Override
	Collection<OptionType> getReplicationOptionTypes() {
		return null
	}

	@Override
	Collection<OptionType> getBackupJobOptionTypes() {
		return null
	}

	@Override
	Collection<OptionType> getBackupOptionTypes() {
		return null
	}

	@Override
	Collection<OptionType> getInstanceReplicationGroupOptionTypes() {
		return null
	}

	@Override
	void addScopedProvider(BackupIntegration backupIntegration) {

	}

	@Override
	void addScopedProvider(BackupTypeProvider backupTypeProvider, String provisionTypeCode, String containerTypeCode) {

	}

	@Override
	Collection<BackupIntegration> getScopedProviders() {
		return null
	}

	@Override
	ServiceResponse configureBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map config, Map opts) {
		return null
	}

	@Override
	ServiceResponse validateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse initializeBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse updateBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse deleteBackupProvider(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse refresh(com.morpheusdata.model.BackupProvider backupProvider) {
		return null
	}

	@Override
	ServiceResponse configureBackupJob(BackupJob backupJobModel, Map config, Map opts) {
		return null
	}

	@Override
	ServiceResponse validateBackupJob(BackupJob backupJobModel, Map config, Map opts) {
		return null
	}

	@Override
	ServiceResponse createBackupJob(BackupJob backupJobModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse cloneBackupJob(BackupJob sourceBackupJobModel, BackupJob backupJobModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse addToBackupJob(BackupJob backupJobModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse deleteBackupJob(BackupJob backupJobModel, Map opts) {
		return null
	}

	@Override
	ServiceResponse executeBackupJob(BackupJob backupJobModel, Map opts) {
		return null
	}
}
