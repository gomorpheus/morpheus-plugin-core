package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface BackupTypeProvider extends PluginProvider {

	String getContainerType();

	Boolean getCopyToStore();

	Boolean getDownloadEnabled();

	Boolean getRestoreExistingEnabled();

	Boolean getRestoreNewEnabled();

	String getRestoreType();

	String getRestoreNewMode();

	Boolean getHasCopyToStore();

	Collection<OptionType> getOptionTypes();

	// Collection<StorageServerType> getStorageServerTypes();

	public ServiceResponse refresh(Map authConfig, com.morpheusdata.model.BackupProvider backupProvider);

	ServiceResponse clean(com.morpheusdata.model.BackupProvider backupProvider, Map opts);

		// Backup Execution
	public ServiceResponse configureBackup(Backup backup, Map config, Map opts);

	ServiceResponse validateBackup(Backup backup, Map config, Map opts);

	ServiceResponse createBackup(Backup backup, Map opts);

	ServiceResponse deleteBackup(Backup backup, Map opts);

	ServiceResponse deleteBackupResult(BackupResult backupResult, Map opts);

	ServiceResponse prepareExecuteBackup(Backup backup, Map opts);

	ServiceResponse prepareBackupResult(BackupResult backupResult, Map opts);

	ServiceResponse executeBackup(Backup backup, BackupResult backupResult, Map executionConfig, Cloud cloud, ComputeServer computeServer, Map opts);

	ServiceResponse refreshBackupResult(BackupResult backupResult);
	
	ServiceResponse cancelBackup(BackupResult backupResult, Map opts);

	ServiceResponse extractBackup(BackupResult backupResult, Map opts);


	// Backup Restore
	ServiceResponse configureRestoreBackup(BackupResult backupResult, Map config, Map opts);

	ServiceResponse getBackupRestoreInstanceConfig(BackupResult backupResult, Instance instance, Map restoreConfig, Map opts);

	ServiceResponse validateRestoreBackup(BackupResult backupResult, Map opts);

	ServiceResponse getRestoreOptions(Backup backup, Map opts);

	ServiceResponse restoreBackup(BackupRestore backupRestore, BackupResult backupResult, Backup backup, Map opts);

	ServiceResponse refreshBackupRestoreResult(BackupRestore backupRestore, BackupResult backupResult);


	// Replication Groups
	// Replication Sites
	// Replication Execution

}
