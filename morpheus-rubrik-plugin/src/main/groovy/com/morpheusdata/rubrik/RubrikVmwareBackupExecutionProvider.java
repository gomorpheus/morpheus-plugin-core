package com.morpheusdata.rubrik;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.backup.AbstractBackupExecutionProvider;
import com.morpheusdata.model.Backup;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

public class RubrikVmwareBackupExecutionProvider extends AbstractBackupExecutionProvider {

	static String LOCK_NAME = "backups.rubrik.execution";

	RubrikVmwareBackupExecutionProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext);
	}

	@Override
	public String getCode() {
		return "rubrik2BackupExecution";
	}

	@Override
	public String getName() {
		return "Rubrik 2.0 Backup Execution Provider";
	}


	@Override
	public ServiceResponse configureBackup() {
		return null;
	}

	@Override
	public ServiceResponse validateBackup() {
		return null;
	}

	@Override
	public ServiceResponse createBackup() {
		return null;
	}

	@Override
	public ServiceResponse deleteBackup() {
		return null;
	}

	@Override
	public ServiceResponse deleteBackupResult() {
		return null;
	}

	@Override
	public ServiceResponse prepareExecuteBackup() {
		return null;
	}

	@Override
	public ServiceResponse prepareBackupResult() {
		return null;
	}

	@Override
	public ServiceResponse executeBackup(Backup backup, Map opts) {
		return null;
	}

	@Override
	public ServiceResponse cancelBackup() {
		return null;
	}
}
