package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.projection.BackupRestoreIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

/**
 * Context methods for interacting with {@link BackupRestore BackupRestores} in Morpheus
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupRestoreService extends MorpheusDataService<BackupRestore, BackupRestoreIdentityProjection>, MorpheusIdentityService<BackupRestoreIdentityProjection> {

	//ORM Object Methods
	/**
	 * Lists all backup projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link com.morpheusdata.model.Backup} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupRestoreIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all backup projection objects for a specified backup id.
	 * The projection is a subset of the properties on a full {@link BackupRestore} object for sync matching.
	 * @param backup the {@link Backup} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupRestoreIdentityProjection> listIdentityProjections(Backup backup);

	/**
	 * Lists all backup projection objects for a specified backup result id.
	 * The projection is a subset of the properties on a full {@link com.morpheusdata.model.BackupRestore} object for sync matching.
	 * @param backupResult the {@link BackupResult} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupRestoreIdentityProjection> listIdentityProjections(BackupResult backupResult);

	/**
	 * Lists all {@link com.morpheusdata.model.BackupRestore} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link com.morpheusdata.model.BackupRestore} ids to fetch.
	 * @return an RxJava Observable stream of {@link com.morpheusdata.model.Backup} objects for subscription.
	 */
	@Deprecated(since="0.15.4")
	Observable<BackupRestoreIdentityProjection> listById(Collection<Long> ids);

	/**
	 * Removes Missing Backup Restore on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getBackup().getRestore().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of backup restore projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<BackupRestoreIdentityProjection> removeList);

	/**
	 * Creates new Backup Restore Domains from cache / sync implementations
	 * @param addList List of new {@link BackupRestore} objects to be inserted into the database
	 * @return notification of completion
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<BackupRestore> addList);

	/**
	 * Saves a list of {@link BackupRestore} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup Restore objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<BackupRestore> saveList);

	/**
	 * Trigger the provision finalize process on a restored workload.
	 * @param workload the workload to be finalized
	 * @return if the finalize process was triggered successfully
	 */
	Single<Boolean> finalizeRestore(com.morpheusdata.model.Workload workload);

}
