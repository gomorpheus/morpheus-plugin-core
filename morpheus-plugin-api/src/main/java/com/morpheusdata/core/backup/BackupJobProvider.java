package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides a standard set of methods for interacting with backup providers.
 * @since 0.12.2
 * @author Dustin DeYoung
 */
public interface BackupJobProvider extends PluginProvider {

	ServiceResponse configureBackupJob();
	ServiceResponse validateBackupJob();
	ServiceResponse createBackupJob();
	ServiceResponse cloneBackupJob();
	ServiceResponse addToBackupJob();
	ServiceResponse deleteBackupJob();
	ServiceResponse prepareExecuteBackupJob();
	ServiceResponse executeBackupJob(BackupJob backupJob, Map opts);
	ServiceResponse cancelBackupJob();


}
