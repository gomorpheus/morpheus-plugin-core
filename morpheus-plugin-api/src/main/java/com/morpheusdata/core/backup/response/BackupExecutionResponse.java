package com.morpheusdata.core.backup.response;

import com.morpheusdata.model.BackupResult;

/**
 * Response from the execution of the backup containing relevant information on the results of the backup output.
 * @since 0.13.4
 */
public class BackupExecutionResponse {

	public BackupExecutionResponse(BackupResult backupResult) {
		this.backupResult = backupResult;
	}

	/**
	 * Notify receiver of updates available on the backupResult
	 */
	protected boolean updates = false;

	/**
	 * {@link BackupResult} containing data on the results of the backup execution
	 */
	protected BackupResult backupResult;

	/**
	 * are there updates available on the associated {@link BackupResult}
	 * @return Boolean updates available on the backupResults object
	 */
	public boolean isUpdates() {
		return updates;
	}

	/**
	 * Notify receiver of updates available on the associated {@link BackupResult}
	 * @param updates are there updates available in the associated {@link BackupResult}
	 */
	public void setUpdates(boolean updates) {
		this.updates = updates;
	}

	/**
	 * get the associated {@link BackupResult}
	 * @return {@link BackupResult} with associated backup execution data
	 */
	public BackupResult getBackupResult() {
		return backupResult;
	}

	/**
	 * set the associated {@link BackupResult}
	 * @param backupResult the {@link BackupResult} to set on the response
	 */
	public void setBackupResult(BackupResult backupResult) {
		this.backupResult = backupResult;
	}
}
