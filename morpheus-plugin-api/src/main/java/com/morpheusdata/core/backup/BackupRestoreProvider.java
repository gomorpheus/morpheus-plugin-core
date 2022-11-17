package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupRestore;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.Instance;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 *
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface BackupRestoreProvider {

	ServiceResponse configureRestoreBackup(BackupResult backupResultModel, Map config, Map opts);

	ServiceResponse getBackupRestoreInstanceConfig(BackupResult backupResultModel, Instance instanceModel, Map restoreConfig, Map opts);

	ServiceResponse validateRestoreBackup(BackupResult backupResultModel, Map opts);

	ServiceResponse getRestoreOptions(Backup backupModel, Map opts);

	ServiceResponse restoreBackup(BackupRestore backupRestoreModel, BackupResult backupResultModel, Backup backupModel, Map opts);

	ServiceResponse refreshBackupRestoreResult(BackupRestore backupRestore, BackupResult backupResult);

}
