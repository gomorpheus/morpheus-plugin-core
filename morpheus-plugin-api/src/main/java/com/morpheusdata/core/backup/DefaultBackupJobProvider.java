package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.backup.BackupJobProvider;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

class DefaultBackupJobProvider implements BackupJobProvider {

	@Override
	public ServiceResponse configureBackupJob(BackupJob backupJobModel, Map config, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse validateBackupJob(BackupJob backupJobModel, Map config, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse createBackupJob(BackupJob backupJobModel, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse cloneBackupJob(BackupJob sourceBackupJobModel, BackupJob backupJobModel, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse addToBackupJob(BackupJob backupJobModel, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse deleteBackupJob(BackupJob backupJobModel, Map opts) {
		return ServiceResponse.success();
	}

	@Override
	public ServiceResponse executeBackupJob(BackupJob backupJob, Map opts) {
		return ServiceResponse.success();
	}

}

