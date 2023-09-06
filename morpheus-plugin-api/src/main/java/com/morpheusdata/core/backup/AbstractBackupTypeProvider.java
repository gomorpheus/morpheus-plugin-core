package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.backup.response.BackupExecutionResponse;
import com.morpheusdata.core.backup.response.BackupRestoreResponse;
import com.morpheusdata.model.*;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides a standard set of methods for a {@link com.morpheusdata.core.backup.BackupProvider}. A backup provider is the primary connection to the
 * external provider services. The backup provider supplies providers for provision types and/or container types via
 * the {@link BackupTypeProvider BackupTypeProviders} implemented within the provider.
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public abstract class AbstractBackupTypeProvider implements BackupTypeProvider {

	Plugin plugin;
	MorpheusContext morpheusContext;
	BackupExecutionProvider executionProvider;
	BackupRestoreProvider restoreProvider;

	public AbstractBackupTypeProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin;
		this.morpheusContext = context;
	}

	@Override
	public MorpheusContext getMorpheus() {
		return morpheusContext;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}

	/**
	 * Get the backup provider which will be responsible for all the operations related to backup executions.
	 * @return a {@link BackupExecutionProvider} providing methods for backup execution.
	 */
	public abstract BackupExecutionProvider getExecutionProvider();

	/**
	 * Set the backup provider which will be responsible for all the operations related to backup executions.
	 * @param executionProvider a {@link BackupExecutionProvider} providing methods for backup execution.
	 */
	public void setExecutionProvider(BackupExecutionProvider executionProvider) {
		this.executionProvider = executionProvider;
	}

	/**
	 * Get the backup provider which will be responsible for all the operations related to backup restore.
	 * @return a {@link BackupRestoreProvider} providing methods for backup restore operations.
	 */
	public abstract BackupRestoreProvider getRestoreProvider();

	/**
	 * Get the backup provider which will be responsible for all the operations related to backup restore.
	 * @param restoreProvider a {@link BackupRestoreProvider} providing methods for backup restore operations.
	 */
	public void setRestoreProvider(BackupRestoreProvider restoreProvider) {
		this.restoreProvider = restoreProvider;
	}

	// provider
	@Override
	public ServiceResponse refresh(Map authConfig, BackupProvider backupProvider) {
		return ServiceResponse.success();
	}

	@Override
	public 	ServiceResponse clean(com.morpheusdata.model.BackupProvider backupProviderModel, Map opts) {
		return ServiceResponse.success();
	}

	// Backup Execution
	@Override
	public ServiceResponse configureBackup(Backup backupModel, Map config, Map opts) {
		return getExecutionProvider().configureBackup(backupModel, config, opts);
	}

	@Override
	public ServiceResponse validateBackup(Backup backupModel, Map config, Map opts) {
		return getExecutionProvider().validateBackup(backupModel, config, opts);
	}

	@Override
	public ServiceResponse createBackup(Backup backupModel, Map opts) {
		return getExecutionProvider().createBackup(backupModel, opts);
	}

	@Override
	public ServiceResponse deleteBackup(Backup backupModel, Map opts) {
		return getExecutionProvider().deleteBackup(backupModel, opts);
	}

	@Override
	public ServiceResponse deleteBackupResult(BackupResult backupResultModel, Map opts) {
		return getExecutionProvider().deleteBackupResult(backupResultModel, opts);
	}

	@Override
	public ServiceResponse prepareExecuteBackup(Backup backupModel, Map opts) {
		return getExecutionProvider().prepareExecuteBackup(backupModel, opts);
	}

	@Override
	public ServiceResponse prepareBackupResult(BackupResult backupResultModel, Map opts) {
		return getExecutionProvider().prepareBackupResult(backupResultModel, opts);
	}

	@Override
	public ServiceResponse<BackupExecutionResponse> executeBackup(Backup backupModel, BackupResult backupResult, Map executionConfig, Cloud cloud, ComputeServer computeServer, Map opts) {
		return getExecutionProvider().executeBackup(backupModel, backupResult, executionConfig, cloud, computeServer, opts);
	}

	@Override
	public 	ServiceResponse<BackupExecutionResponse> refreshBackupResult(BackupResult backupResult) {
		return getExecutionProvider().refreshBackupResult(backupResult);
	}

	@Override
	public ServiceResponse cancelBackup(BackupResult backupResultModel, Map opts) {
		return getExecutionProvider().cancelBackup(backupResultModel, opts);
	}

	@Override
	public ServiceResponse extractBackup(BackupResult backupResultModel, Map opts) {
		return getExecutionProvider().extractBackup(backupResultModel, opts);
	}

	// Backup Restore
	@Override
	public ServiceResponse configureRestoreBackup(BackupResult backupResultModel, Map config, Map opts) {
		return getRestoreProvider().configureRestoreBackup(backupResultModel, config, opts);
	}

	@Override
	public ServiceResponse getBackupRestoreInstanceConfig(BackupResult backupResultModel, Instance instanceModel, Map restoreConfig, Map opts) {
		return getRestoreProvider().getBackupRestoreInstanceConfig(backupResultModel, instanceModel, restoreConfig, opts);
	}

	@Override
	public ServiceResponse validateRestoreBackup(BackupResult backupResultModel, Map opts) {
		return getRestoreProvider().validateRestoreBackup(backupResultModel, opts);
	}

	@Override
	public ServiceResponse getRestoreOptions( Backup backupModel, Map opts) {
		return getRestoreProvider().getRestoreOptions(backupModel, opts);
	}

	@Override
	public ServiceResponse<BackupRestoreResponse> restoreBackup(BackupRestore backupRestore, BackupResult backupResultModel, Backup backupModel, Map opts) {
		return getRestoreProvider().restoreBackup(backupRestore, backupResultModel, backupModel, opts);
	}

	@Override
	public ServiceResponse<BackupRestoreResponse> refreshBackupRestoreResult(BackupRestore backupRestoreModel, BackupResult backupResultModel) {
		return getRestoreProvider().refreshBackupRestoreResult(backupRestoreModel, backupResultModel);
	}


}
