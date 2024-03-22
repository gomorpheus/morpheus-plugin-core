package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupJob;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupProviderType;
import com.morpheusdata.model.projection.BackupJobIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

/**
 * Context methods for interacting with {@link BackupJob} in Morpheus. Backup Jobs contain one or more backups and optional
 * have a schedule to automatically run the job at a recurring interval.
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupJobService extends MorpheusDataService<BackupJob, BackupJobIdentityProjection>, MorpheusIdentityService<BackupJobIdentityProjection> {

	/**
	 * Returns the BackupContext used for performing updates or queries on {@link Backup} related assets within Morpheus.
	 * Typically this would be called by a {@link BackupProvider}
	 * @return An instance of the Backup Context to be used for calls by various backup providers
	 */
	MorpheusBackupService getBackup();

	/**
	 * Lists all backup job projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link Backup} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<BackupJobIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all {@link BackupJob} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupJob} ids to fetch.
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	Observable<BackupJob> listById(Collection<Long> ids);

	/**
	 * Removes missing Backup Jobs on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is an Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getBackupJob().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of backup job projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<BackupJobIdentityProjection> removeList);

	/**
	 * Creates new Backup Job Domains from cache / sync implementations
	 * @param addList List of new {@link BackupJob} objects to be inserted into the database
	 * @return notification of completion
	 */
	Single<Boolean> create(List<BackupJob> addList);

	/**
	 * Saves a list of {@link BackupJob} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup Job objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<BackupJob> saveList);

	/**
	 * Saves a {@link BackupJob} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param backupJob a Backup Object to be updated in the database.
	 * @return the Single Observable containing the resulting Backup Job Object
	 */
	Single<BackupJob> save(BackupJob backupJob);
}
