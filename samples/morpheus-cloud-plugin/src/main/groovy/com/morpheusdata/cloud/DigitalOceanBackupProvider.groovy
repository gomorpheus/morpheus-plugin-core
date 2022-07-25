package com.morpheusdata.cloud

import com.morpheusdata.core.backup.BackupExecutionProvider
import com.morpheusdata.core.backup.BackupJobProvider
import com.morpheusdata.core.backup.BackupProvider;
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.BackupRestoreProvider
import com.morpheusdata.model.BackupType
import com.morpheusdata.model.Icon
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.ReplicationType

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
	Collection<BackupJobProvider> getAvailableBackupJobProviders() {
		return null
	}

	@Override
	BackupJobProvider getAvailableBackupJobProvider(String providerCode) {
		return null
	}

	@Override
	Collection<BackupExecutionProvider> getAvailableBackupExecutionProviders() {
		return null
	}

	@Override
	BackupExecutionProvider getAvailableBackupExecutionProvider(String providerCode) {
		return null
	}

	@Override
	Collection<BackupRestoreProvider> getAvailableBackupRestoreProviders() {
		return null
	}

	@Override
	BackupRestoreProvider getAvailableBackupRestoreProvider(String providerCode) {
		return null
	}

	@Override
	String providerService() {
		return null
	}

	@Override
	String jobService() {
		return null
	}

	@Override
	Boolean hasCopyToStore() {
		return null
	}

	@Override
	Boolean hasStreamToStore() {
		return null
	}

	@Override
	Boolean downloadEnabled() {
		return null
	}

	@Override
	Boolean restoreExistingEnabled() {
		return null
	}

	@Override
	Boolean restoreNewEnabled() {
		return null
	}

	@Override
	Boolean hasBackups() {
		return null
	}

	@Override
	Boolean hasReplication() {
		return null
	}

	@Override
	Boolean hasServers() {
		return null
	}

	@Override
	Boolean hasRepositories() {
		return null
	}

	@Override
	Boolean hasJobs() {
		return null
	}

	@Override
	Boolean hasSites() {
		return null
	}

	@Override
	Boolean hasReplicationGroups() {
		return null
	}

	@Override
	Boolean hasCreateJob() {
		return null
	}

	@Override
	Boolean hasCloneJob() {
		return null
	}

	@Override
	Boolean hasAddToJob() {
		return null
	}

	@Override
	Boolean hasOptionalJob() {
		return null
	}

	@Override
	Boolean hasSchedule() {
		return null
	}

	@Override
	Boolean hasStorageProvider() {
		return null
	}

	@Override
	Boolean hasRetentionCount() {
		return null
	}

	@Override
	Boolean hasCancelBackup() {
		return null
	}

	@Override
	Collection<BackupType> backupTypes() {
		return null
	}

	@Override
	Collection<ReplicationType> replicationTypes() {
		return null
	}

	@Override
	Collection<OptionType> OptionTypes() {
		return null
	}

	@Override
	Collection<OptionType> replicationGroupOptions() {
		return null
	}

	@Override
	Collection<OptionType> replicationOptions() {
		return null
	}

	@Override
	Collection<OptionType> backupJobOptions() {
		return null
	}

	@Override
	Collection<OptionType> backupOptions() {
		return null
	}

	@Override
	Collection<OptionType> instanceReplicationGroupOptions() {
		return null
	}

	@Override
	Icon getIcon() {
		return null
	}
}
