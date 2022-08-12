package com.morpheusdata.core.backup;

import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.Backup;

import java.util.Collection;
import java.util.Map;

/**
 * Provides a standard set of methods for interacting with backup providers.
 * @since 0.12.2
 * @author Dustin DeYoung
 */
public interface BackupExecutionProvider {

	ServiceResponse configureBackup(Backup backupModel, Map config, Map opts);

	ServiceResponse validateBackup(Backup backupModel, Map config, Map opts);

	ServiceResponse createBackup(Backup backupModel, Map opts);

	ServiceResponse deleteBackup(Backup backupModel, Map opts);

	ServiceResponse deleteBackupResult(BackupResult backupResultModel, Map opts);

	ServiceResponse prepareExecuteBackup(Backup backupModel, Map opts);

	ServiceResponse prepareBackupResult(BackupResult backupResultModel, Map opts);

	ServiceResponse executeBackup(Backup backup, BackupResult backupResult, Map executionConfig, Cloud cloud, ComputeServer computeServer, Map opts);

	ServiceResponse refreshBackupResult(BackupResult backupResult);

	ServiceResponse cancelBackup(BackupResult backupResultModel, Map opts);

	ServiceResponse extractBackup(BackupResult backupResultModel, Map opts);

}
