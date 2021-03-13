package com.morpheusdata.rubrik

import com.morpheusdata.core.BackupProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.apiutil.RestApiUtil
import groovy.util.logging.Slf4j

@Slf4j
class RubrikBackupProvider implements BackupProvider {
	MorpheusContext context
	Plugin plugin
	RubrikApiService apiService


	RubrikBackupProvider(Plugin plugin, MorpheusContext context) {
		this.context = context
		this.plugin = plugin
		this.apiService = new RubrikApiService()
	}

	RubrikBackupProvider(Plugin plugin, MorpheusContext context, RubrikApiService api) {
		this.context = morpheusContext
		this.plugin = plugin
		this.apiService = api
	}

	@Override
	ServiceResponse executeBackup(ComputeServer server, Map opts) {
		def results = ServiceResponse.prepare()

		return results
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
	MorpheusContext getMorpheusContext() {
		return this.context
	}

	@Override
	Plugin getPlugin() {
		return this.plugin
	}

	@Override
	String getProviderCode() {
		return 'rubrik-backup-plugin'
	}

	@Override
	String getProviderName() {
		return 'Rubrik Backup Provider Plugin'
	}
}
