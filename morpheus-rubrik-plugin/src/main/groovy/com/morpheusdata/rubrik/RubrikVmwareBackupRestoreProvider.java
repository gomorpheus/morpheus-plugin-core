package com.morpheusdata.rubrik;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.backup.AbstractBackupRestoreProvider;
import com.morpheusdata.response.ServiceResponse;

public class RubrikVmwareBackupRestoreProvider extends AbstractBackupRestoreProvider {

	static String LOCK_NAME = "backups.rubrik.restore";

	RubrikVmwareBackupRestoreProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext);
	}

	@Override
	public String getCode() {
		return "rubrik2BackupRestore";
	}

	@Override
	public String getName() {
		return "Rubrik 2.0 Backup Restore Provider";
	}

	@Override
	public ServiceResponse prepareRestoreBackup() {
		return null;
	}

	@Override
	public ServiceResponse restoreBackup() {
		return null;
	}

	@Override
	public ServiceResponse finalizeRestore() {
		return null;
	}
}
