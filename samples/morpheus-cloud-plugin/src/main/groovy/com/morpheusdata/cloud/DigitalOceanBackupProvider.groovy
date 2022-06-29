package com.morpheusdata.cloud

import com.morpheusdata.core.backup.BackupProvider;
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Backup
import com.morpheusdata.model.BackupJob
import com.morpheusdata.model.ComputeServer
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
	ServiceResponse configureBackupJob() {
		return null
	}

	@Override
	ServiceResponse validateBackupJob() {
		return null
	}

	@Override
	ServiceResponse createBackupJob() {
		return null
	}

	@Override
	ServiceResponse cloneBackupJob() {
		return null
	}

	@Override
	ServiceResponse addToBackupJob() {
		return null
	}

	@Override
	ServiceResponse deleteBackupJob() {
		return null
	}

	@Override
	ServiceResponse configureBackup() {
		return null
	}

	@Override
	ServiceResponse validateBackup() {
		return null
	}

	@Override
	ServiceResponse createBackup() {
		return null
	}

	@Override
	ServiceResponse deleteBackup() {
		return null
	}

	@Override
	ServiceResponse prepareExecuteBackupJob() {
		return null
	}

	@Override
	ServiceResponse executeBackupJob(BackupJob backupJob, Map opts) {
		return null
	}

	@Override
	ServiceResponse cancelBackupJob() {
		return null
	}

	@Override
	ServiceResponse deleteBackupResult() {
		return null
	}

	@Override
	ServiceResponse prepareExecuteBackup() {
		return null
	}

	@Override
	ServiceResponse prepareBackupResult() {
		return null
	}

	@Override
	// TODO: existing method that needs converted to the stub below.
	// ServiceResponse executeBackup(ComputeServer server, Map opts) {
	// 	Map body = [type: 'snapshot', name: opts.snapshotName]
	// 	String apiKey = server.cloud.configMap.doApiKey
	// 	apiService.performDropletAction(server.externalId, body, apiKey)
	// }
	ServiceResponse executeBackup(Backup backup, Map opts) {
		return null
	}

	@Override
	ServiceResponse cancelBackup() {
		return null
	}

	@Override
	ServiceResponse prepareRestoreBackup() {
		return null
	}

	@Override
	ServiceResponse restoreBackup() {
		return null
	}

	@Override
	ServiceResponse finalizeRestore() {
		return null
	}

	ServiceResponse getSnapshot() {
		return null
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
}
