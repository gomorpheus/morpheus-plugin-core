package com.morpheusdata.rubrik

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*;
import com.morpheusdata.core.BackupProvider
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.core.util.RestApiUtil
import groovy.util.logging.Slf4j

@Slf4j
class RubrikBackupProvider implements BackupProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	RestApiUtil apiService

	static String LOCK_NAME = 'backups.rubrik'

	RubrikBackupProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
		this.apiService = new RestApiUtil()
	}

	RubrikBackupProvider(Plugin plugin, MorpheusContext morpheusContext, RestApiUtil api) {
		this.morpheusContext = morpheusContext
		this.plugin = plugin
		this.apiService = api
	}

	/**
	 * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
	 *
	 * @return an implementation of the MorpheusContext for running Future based rxJava queries
	 */
	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	/**
	 * Returns the instance of the Plugin class that this provider is loaded from
	 * @return Plugin class contains references to other providers
	 */
	@Override
	Plugin getPlugin() {
		return plugin
	}

	/**
	 * A unique shortcode used for referencing the provided provider. Make sure this is going to be unique as any data
	 * that is seeded or generated related to this provider will reference it by this code.
	 * @return short code string that should be unique across all other plugin implementations.
	 */
	@Override
	String getCode() {
		return 'rubrik2'
	}

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	@Override
	String getName() {
		return 'Rubrik 2.0'
	}

	@Override
	ServiceResponse configureBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse validateBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse createBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse cloneBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse addToBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse deleteBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}


	@Override
	ServiceResponse configureBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse validateBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse createBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}
	ServiceResponse deleteBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	// Backup Job Operations

	@Override
	ServiceResponse prepareExecuteBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse executeBackupJob(BackupJob backupJob, Map opts) {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse cancelBackupJob() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	// Backup Results

	@Override
	ServiceResponse deleteBackupResult() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	// Backup Operations

	@Override
	ServiceResponse prepareExecuteBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse prepareBackupResult() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse executeBackup(Backup backup, Map opts) {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse cancelBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	// Restore Operations

	@Override
	ServiceResponse prepareRestoreBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse restoreBackup() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}

	@Override
	ServiceResponse finalizeRestore() {
		return ServiceResponse.error("IMPLEMENT ME!")
	}
}
