package com.morpheusdata.core.backup.response;

import com.morpheusdata.model.BackupRestore;

import java.util.Map;

public class BackupRestoreResponse {

	public BackupRestoreResponse(BackupRestore backupRestore) {
		this.backupRestore = backupRestore;
	}

	protected boolean updates = false;
	protected BackupRestore backupRestore;

	// Any configurations that are needed later in the restore process
	protected Map restoreConfig;

	public boolean isUpdates() {
		return updates;
	}

	public void setUpdates(boolean updates) {
		this.updates = updates;
	}

	public BackupRestore getBackupRestore() {
		return backupRestore;
	}

	public void setBackupRestore(BackupRestore backupRestore) {
		this.backupRestore = backupRestore;
	}

	public Map getRestoreConfig() { return restoreConfig; }

	public void setRestoreConfig(Map restoreConfig) { this.restoreConfig = restoreConfig; }
}
