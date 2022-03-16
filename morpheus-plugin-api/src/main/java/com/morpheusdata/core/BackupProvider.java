package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupResult;
import java.util.Map;

/**
 * Provides a standard set of methods for interacting with backup providers.
 * @since 0.12.2
 * @author Dustin DeYoung
 */
public interface BackupProvider extends PluginProvider {
	
	// Backup Jobs
	ServiceResponse configureBackupJob();
	ServiceResponse validateBackupJob();
	ServiceResponse createBackupJob();
	ServiceResponse cloneBackupJob();
	ServiceResponse addToBackupJob();
	ServiceResponse deleteBackupJob();

	// Backups
	ServiceResponse configureBackup();
	ServiceResponse validateBackup();
	ServiceResponse createBackup();
	ServiceResponse deleteBackup();

	// Backup Job Operations
	ServiceResponse prepareExecuteBackupJob();
	ServiceResponse executeBackupJob(BackupJob backupJob, Map opts);
	ServiceResponse cancelBackupJob();

	// Backup Results
	ServiceResponse deleteBackupResult();

	// Backup Operations
	ServiceResponse prepareExecuteBackup();
	ServiceResponse prepareBackupResult();
	ServiceResponse executeBackup(Backup backup, Map opts);
	ServiceResponse cancelBackup();

	// Restore Operations
	ServiceResponse prepareRestoreBackup();
	ServiceResponse restoreBackup();
	ServiceResponse finalizeRestore();

}
