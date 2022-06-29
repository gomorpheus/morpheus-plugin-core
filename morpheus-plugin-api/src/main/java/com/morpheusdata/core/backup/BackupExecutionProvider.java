package com.morpheusdata.core.backup;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.Backup;

import java.util.Collection;
import java.util.Map;

/**
 * Provides a standard set of methods for interacting with backup providers.
 * @since 0.12.2
 * @author Dustin DeYoung
 */
public interface BackupExecutionProvider extends PluginProvider {

	ServiceResponse configureBackup();
	ServiceResponse validateBackup();
	ServiceResponse createBackup();
	ServiceResponse deleteBackup();
	ServiceResponse deleteBackupResult();
	ServiceResponse prepareExecuteBackup();
	ServiceResponse prepareBackupResult();
	ServiceResponse executeBackup(Backup backup, Map opts);
	ServiceResponse cancelBackup();

}
