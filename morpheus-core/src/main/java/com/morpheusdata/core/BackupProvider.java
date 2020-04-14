package com.morpheusdata.core;

import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.Restore;

import java.util.Map;

/**
 * BackupProvider
 *
 */
public interface BackupProvider extends PluginProvider {
	Object getBackupRestoreInstanceConfig(BackupResult backupResult, Instance backupInstance, Map opts);
	Object executeBackup(Backup backup, Map backupConfig, Map opts);
	Object extractBackup(BackupResult backupResult, Map opts);
	Object restoreBackup(Restore backupRestore, BackupResult backupResult, Map opts);
	Object cleanBackupResult(BackupResult backupResult);
}
