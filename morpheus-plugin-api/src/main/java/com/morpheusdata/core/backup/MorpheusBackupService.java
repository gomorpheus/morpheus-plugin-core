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

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.BackupRestore;
import com.morpheusdata.model.BackupType;
import com.morpheusdata.model.Replication;
import com.morpheusdata.model.Account;
import com.morpheusdata.model.projection.BackupIdentityProjection;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

/**
 * Context methods for interacting with {@link Backup Backups} in Morpheus
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupService extends MorpheusDataService<Backup, BackupIdentityProjection>, MorpheusIdentityService<BackupIdentityProjection> {

	/**
	 * Returns the MorpheusBackupTypeContext used for performing updates/queries on {@link BackupType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusBackupTypeService getType();

	/**
	 * Returns the BackupJobContext used for performing updates or queries on {@link BackupJob} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}
	 * @return An instance of the Backup Job Context to be used for calls by various backup providers
	 */
	MorpheusBackupJobService getBackupJob();

	/**
	 * Returns the BackupResultContext used for performing updates or queries on {@link BackupResult} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}.
	 * @return An instance of the Backup Result Context to be used for calls by various backup providers
	 */
	MorpheusBackupResultService getBackupResult();

	/**
	 * Returns the BackupRestoreContext used for performing updates or queries on {@link BackupRestore} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}.
	 * @return An instance of the Backup Restore Context to be used for calls by various backup providers
	 */
	MorpheusBackupRestoreService getBackupRestore();


	/**
	 * Returns the MorpheusReplicationContext used for performing updates/queries on {@link Replication} related assets
	 * within Morpheus.
	 * @return An instance of the MorpheusReplicationContext to be used for calls by various backup providers
	 */
	MorpheusReplicationService getReplication();

	/**
	 * Lists all backup projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link Backup} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<BackupIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all backup projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Backup} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<BackupIdentityProjection> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all {@link Backup} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link Backup} ids to fetch.
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<Backup> listById(Collection<Long> ids);

	/**
	 * Lists all {@link Backup} objects by a specified {@link BackupJob } and active status.
	 * @param backupJobId ID of a {@link BackupJob}
	 * @param active filter the active or inactive state of the backup results
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<Backup> listByBackupJobIdAndActive(Long backupJobId, Boolean active);

	/**
	 * Lists all {@link Backup} objects by a specified {@link Account} and {@link BackupJob } and active status.
	 * @param accountId ID of an {@link Account}
	 * @param backupJobId ID of a {@link BackupJob}
	 * @param active filter the active or inactive state of the backup results
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<Backup> listByAccountIdAndBackupJobIdAndActive(Long accountId, Long backupJobId, Boolean active);

	/**
	 * Initiates the execution of a backup {@link Backup}. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param backupId the ID of the {@link Backup} to begin execution.
	 * @return the Single Observable containing the success or failure of the backup execution call
	 */
	Single<Boolean> executeBackup(Long backupId);

}
