package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.backup.BackupJobProvider;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.Account;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DefaultBackupJobProvider implements BackupJobProvider {

	static Logger log = LoggerFactory.getLogger(DefaultBackupJobProvider.class);

	Plugin plugin;
	MorpheusContext morpheus;

	DefaultBackupJobProvider(Plugin plugin, MorpheusContext morpheus) {
		this.plugin = plugin;
		this.morpheus = morpheus;
	}

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
		try {
			log.debug("Executing backup job {}", backupJob.getId());
			Long backupJobId = backupJob.getId();
			Account account = backupJob.getAccount();

			Observable<Backup> backupObservable;
			if(account != null) {
				Long accountId = backupJob.getAccount().getId();
				backupObservable = morpheus.getBackup().listByAccountIdAndBackupJobIdAndActive(accountId, backupJobId, true);
			} else {
				backupObservable = morpheus.getBackup().listByBackupJobIdAndActive(backupJobId, true);
			}
			log.debug("backupResults: {}", backupObservable);

			backupObservable
				.subscribeOn(Schedulers.io())
				.subscribe(
					(Backup backup) -> {
						try {
							log.debug("Executing backup {}", backup.getId());
							morpheus.getBackup().executeBackup(backup.getId()).blockingGet();
						} catch(Exception ex) {
							log.error("Failed to execute backup {}", backup.getId());
						}
					},
					(Throwable ex) -> {
						log.error("Failed to execute backup job {} with error: {}", backupJobId, ex, ex);
					}
        		);


			return ServiceResponse.success();
		} catch(Exception e) {
			log.error("Failed to execute backup job {}, {}", backupJob.getId(), e);
			return ServiceResponse.error("Failed to execute backup job " + backupJob.getId().toString());
		}
	}

}

