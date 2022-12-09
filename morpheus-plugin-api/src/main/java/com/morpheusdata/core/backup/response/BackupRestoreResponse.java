package com.morpheusdata.core.backup.response;

import com.morpheusdata.model.BackupRestore;
import com.morpheusdata.model.BackupResult;

import java.util.Map;

/**
 * Response from the execution of the backup restore containing relevant information on the results of the restore process.
 * @since 0.13.4
 */
public class BackupRestoreResponse {

	public BackupRestoreResponse(BackupRestore backupRestore) {
		this.backupRestore = backupRestore;
	}

	/**
	 * Notify receiver of updates available on the backupRestore
	 */
	protected boolean updates = false;

	/**
	 * {@link BackupRestore} containing data on the results of the restore execution
	 */
	protected BackupRestore backupRestore;

	/**
	 * Any configurations that are needed later in the restore process. The primary intention is to pass information
	 * to the restore refresh process which executes periodically until the restore process has completed.
 	 */
	protected Map restoreConfig;

	/**
	 * are there updates available on the associated {@link BackupRestore}
	 * @return Boolean updates available on the backupRestore object
	 */
	public boolean isUpdates() {
		return updates;
	}

	/**
	 * Notify receiver of updates available on the associated {@link BackupRestore}
	 * @param updates are there updates available in the associated {@link BackupRestore}
	 */
	public void setUpdates(boolean updates) {
		this.updates = updates;
	}

	/**
	 * get the associated {@link BackupRestore}
	 * @return {@link BackupRestore} with associated data from the backup restore process
	 */
	public BackupRestore getBackupRestore() {
		return backupRestore;
	}

	/**
	 * set the associated {@link BackupRestore}
	 * @param backupRestore the {@link BackupRestore} to set on the response
	 */
	public void setBackupRestore(BackupRestore backupRestore) {
		this.backupRestore = backupRestore;
	}

	/**
	 * get the restoreConfig
	 * @return Map the restoreConfig
	 */
	public Map getRestoreConfig() { return restoreConfig; }

	/**
	 * set the restoreConfig
	 * @param restoreConfig restoreConfig to set on the response
	 */
	public void setRestoreConfig(Map restoreConfig) { this.restoreConfig = restoreConfig; }
}
