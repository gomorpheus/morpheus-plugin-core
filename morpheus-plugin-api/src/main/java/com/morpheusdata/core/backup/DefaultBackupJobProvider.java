/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;
import com.morpheusdata.core.backup.BackupJobProvider;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.model.Account;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Default backup job provider which delegates the backup operations back to morpheus. Use this as the backup
 * job provider in a backup provider when the provider does not have the concept of scheduled batch backup operations,
 * or it is desired for morpheus to handle the scheduling and running of batch backup operations.
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public class DefaultBackupJobProvider implements BackupJobProvider {

	static Logger log = LoggerFactory.getLogger(DefaultBackupJobProvider.class);

	Plugin plugin;
	MorpheusContext morpheus;

	public DefaultBackupJobProvider(Plugin plugin, MorpheusContext morpheus) {
		this.plugin = plugin;
		this.morpheus = morpheus;
	}

	/**
	 * There is no additional work to be done, allow morpheus to complete the internal process.
	 */
	@Override
	public ServiceResponse configureBackupJob(BackupJob backupJobModel, Map config, Map opts) { return ServiceResponse.success(); }

	/**
	 * There is no additional work to be done, allow morpheus to complete the internal process.
	 */
	@Override
	public ServiceResponse validateBackupJob(BackupJob backupJobModel, Map config, Map opts) { return ServiceResponse.success(); }

	/**
	 * There is no additional work to be done, allow morpheus to complete the internal process.
	 */
	@Override
	public ServiceResponse createBackupJob(BackupJob backupJobModel, Map opts) {
		return ServiceResponse.success();
	}

	/**
	 * There is no additional work to be done, allow morpheus to complete the internal process.
	 */
	@Override
	public ServiceResponse cloneBackupJob(BackupJob sourceBackupJobModel, BackupJob backupJobModel, Map opts) {	return ServiceResponse.success();}

	/**
	 * There is no additional work to be done, allow morpheus to complete the internal process.
	 */
	@Override
	public ServiceResponse addToBackupJob(BackupJob backupJobModel, Map opts) {
		return ServiceResponse.success();
	}

	/**
	 * There is no additional work to be done, allow morpheus to complete the internal process.
	 */
	@Override
	public ServiceResponse deleteBackupJob(BackupJob backupJobModel, Map opts) {
		return ServiceResponse.success();
	}

	/**
	 * Trigger the execution of all backups with the job through morpheus backup services.
	 * @param backupJob backup job to be executed
	 * @param opts optional parameters for the job execution process
	 * @return {@link ServiceResponse} containing the success or failure of the job execution process
	 */
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
							/**
							 * send the backup execution operation back to morpheus and route the operation
							 * to the appropriate provider.
							 */
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

