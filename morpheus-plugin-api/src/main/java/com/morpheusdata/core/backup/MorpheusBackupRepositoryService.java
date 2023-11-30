package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupRepository;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupProviderType;
import com.morpheusdata.model.projection.BackupRepositoryIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

/**
 * Context methods for interacting with {@link BackupRepository} in Morpheus.
 * @since 1.0.3
 * @author Bob Whiton
 */
public interface MorpheusBackupRepositoryService extends MorpheusDataService<BackupRepository, BackupRepositoryIdentityProjection>, MorpheusIdentityService<BackupRepositoryIdentityProjection> {

	/**
	 * Returns the BackupContext used for performing updates or queries on {@link Backup} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}
	 * @return An instance of the Backup Context to be used for calls by various backup providers
	 */
	MorpheusBackupService getBackup();

	/**
	 * Lists all Backup Repository projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link BackupRepository} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the backup repositories to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupRepositoryIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all {@link BackupRepository} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupRepository} ids to fetch.
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	Observable<BackupRepositoryIdentityProjection> listById(Collection<Long> ids);

	/**
	 * Removes missing Backup Repositories on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is an Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.async.getBackupRepository().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of Backup Repository projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<BackupRepositoryIdentityProjection> removeList);

	/**
	 * Creates new Backup Repository Domains from cache / sync implementations
	 * @param addList List of new {@link BackupRepository} objects to be inserted into the database
	 * @return notification of completion
	 */
	Single<Boolean> create(List<BackupRepository> addList);

	/**
	 * Saves a list of {@link BackupRepository} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup Repository objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<BackupRepository> saveList);

	/**
	 * Saves a {@link BackupRepository} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param backupRepository a Backup Object to be updated in the database.
	 * @return the Single Observable containing the resulting Backup Repository Object
	 */
	Single<BackupRepository> save(BackupRepository backupRepository);
}
