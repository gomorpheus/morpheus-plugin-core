package com.morpheusdata.core.backup.response;

import com.morpheusdata.model.BackupResult;

public class BackupExecutionResponse {

	public BackupExecutionResponse(BackupResult backupResult) {
		this.backupResult = backupResult;
	}

	protected boolean updates = false;
	protected BackupResult backupResult;

	public boolean isUpdates() {
		return updates;
	}

	public void setUpdates(boolean updates) {
		this.updates = updates;
	}

	public BackupResult getBackupResult() {
		return backupResult;
	}

	public void setBackupResult(BackupResult backupResult) {
		this.backupResult = backupResult;
	}
}
