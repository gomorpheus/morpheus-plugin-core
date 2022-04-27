package com.morpheusdata.core.backup;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.projection.BackupResultIdentityProjection;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

public interface MorpheusBackupResultService {

	/**
	 * Lists all backup result projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link BackupResult} object for sync matching.
	 * @param backupProvider the {@link com.morpheusdata.core.BackupProvider} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupResultIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all backup result projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link BackupResult} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupResultIdentityProjection> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all {@link BackupResult} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupResult} ids to fetch.
	 * @return an RxJava Observable stream of {@link BackupResult} objects for subscription.
	 */
	Observable<BackupResultIdentityProjection> listById(Collection<Long> ids);

	/**
	 * Removes Missing Backup Result on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getBackup().getBackupResult().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of backup result projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<BackupResultIdentityProjection> removeList);

	/**
	 * Creates new Backup Result Domains from cache / sync implementations
	 * @param addList List of new {@link BackupResult} objects to be inserted into the database
	 * @return notification of completion
	 */
	Single<Boolean> create(List<BackupResult> addList);

	/**
	 * Saves a list of {@link BackupResult} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup Result objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<BackupResult> saveList);

	/**
	 * Saves a {@link BackupResult} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param backupResult a Backup Result Object to be updated in the database.
	 * @return the Single Observable containing the resulting Backup Object
	 */
	Single<BackupResult> save(BackupResult backupResult);
}
