package com.morpheusdata.core.backup;

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

import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

public interface MorpheusBackupService {

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

	//ORM Object Methods
	/**
	 * Lists all backup projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link Backup} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all backup projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Backup} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupIdentityProjection> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all {@link Backup} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link Backup} ids to fetch.
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	Observable<Backup> listById(Collection<Long> ids);

	/**
	 * Lists all {@link Backup} objects by a specified {@link BackupJob } and active status.
	 * @param backupJobId ID of a {@link BackupJob}
	 * @param active filter the active or inactive state of the backup results
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */

	Observable<Backup> listByBackupJobIdAndActive(Long backupJobId, Boolean active);

	/**
	 * Lists all {@link Backup} objects by a specified {@link Account} and {@link BackupJob } and active status.
	 * @param accountId ID of an {@link Account}
	 * @param backupJobId ID of a {@link BackupJob}
	 * @param active filter the active or inactive state of the backup results
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	Observable<Backup> listByAccountIdAndBackupJobIdAndActive(Long accountId, Long backupJobId, Boolean active);

	/**
	 * Removes Missing Backup on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getBackup().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of backup projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<BackupIdentityProjection> removeList);

	/**
	 * Creates new Backup Domains from cache / sync implementations
	 * @param addList List of new {@link Backup} objects to be inserted into the database
	 * @return notification of completion
	 */
	Single<Boolean> create(List<Backup> addList);

	/**
	 * Saves a list of {@link Backup} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<Backup> saveList);

	/**
	 * Saves a {@link Backup} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param backup a Backup Object to be updated in the database.
	 * @return the Single Observable containing the resulting Backup Object
	 */
	Single<Backup> save(Backup backup);

	/**
	 * Initiates the execution of a backup {@link Backup}. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param backupId the ID of the {@link Backup} to begin execution.
	 * @return the Single Observable containing the success or failure of the backup execution call
	 */
	Single<Boolean> executeBackup(Long backupId);

}
