package com.morpheusdata.cloud

import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
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
	ServiceResponse executeBackup(ComputeServer server, Map opts) {
		Map body = [type: 'snapshot', name: opts.snapshotName]
		String apiKey = server.cloud.configMap.doApiKey
		apiService.performDropletAction(server.externalId, body, apiKey)
	}

	@Override
	ServiceResponse restoreBackup() {
		return null
	}

	@Override
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
