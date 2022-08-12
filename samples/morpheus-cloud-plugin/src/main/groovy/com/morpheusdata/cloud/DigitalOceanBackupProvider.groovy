package com.morpheusdata.cloud

import com.morpheusdata.core.backup.BackupExecutionProvider
import com.morpheusdata.core.backup.BackupJobProvider
import com.morpheusdata.core.backup.BackupProvider;
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.BackupRestoreProvider
import com.morpheusdata.model.BackupIntegration
import com.morpheusdata.model.BackupProviderType
import com.morpheusdata.model.BackupType
import com.morpheusdata.model.Icon
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
	Icon getIcon() {
		return null
	}

	@Override
	BackupProviderType getBackupProviderType() {
		return null
	}

	@Override
	Collection<BackupType> getBackupTypes() {
		return null
	}

	@Override
	Collection<ReplicationType> getReplicationTypes() {
		return null
	}

	@Override
	Collection<BackupIntegration> getBackupIntegrations() {
		return null
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
}
