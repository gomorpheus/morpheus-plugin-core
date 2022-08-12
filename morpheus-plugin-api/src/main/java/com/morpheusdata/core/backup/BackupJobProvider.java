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
public interface BackupJobProvider {

	ServiceResponse configureBackupJob(BackupJob backupJobModel, Map config, Map opts);

	ServiceResponse validateBackupJob(BackupJob backupJobModel, Map config, Map opts);

	ServiceResponse createBackupJob(BackupJob backupJobModel, Map opts);

	ServiceResponse cloneBackupJob(BackupJob sourceBackupJobModel, BackupJob backupJobModel, Map opts);

	ServiceResponse addToBackupJob(BackupJob backupJobModel, Map opts);

	ServiceResponse deleteBackupJob(BackupJob backupJobModel, Map opts);

	ServiceResponse executeBackupJob(BackupJob backupJob, Map opts);


}
