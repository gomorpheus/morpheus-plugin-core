package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.model.*;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

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

	public abstract BackupExecutionProvider getExecutionProvider();

	public void setExecutionProvider(BackupExecutionProvider executionProvider) {
		this.executionProvider = executionProvider;
	}

	public abstract BackupRestoreProvider getRestoreProvider();

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
	public ServiceResponse executeBackup(Backup backupModel, BackupResult backupResult, Map executionConfig, Cloud cloud, ComputeServer computeServer, Map opts) {
		return getExecutionProvider().executeBackup(backupModel, backupResult, executionConfig, cloud, computeServer, opts);
	}

	@Override
	public 	ServiceResponse refreshBackupResult(BackupResult backupResult) {
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
	public ServiceResponse restoreBackup(BackupRestore backupRestore, BackupResult backupResultModel, Backup backupModel, Map opts) {
		return getRestoreProvider().restoreBackup(backupRestore, backupResultModel, backupModel, opts);
	}

	@Override
	public ServiceResponse refreshBackupRestoreResult(BackupRestore backupRestoreModel, BackupResult backupResultModel) {
		return getRestoreProvider().refreshBackupRestoreResult(backupRestoreModel, backupResultModel);
	}


}
