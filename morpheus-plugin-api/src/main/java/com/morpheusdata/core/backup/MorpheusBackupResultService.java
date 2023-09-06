package com.morpheusdata.core.backup;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.Backup;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.BackupRestore;
import com.morpheusdata.model.BackupResult;
import com.morpheusdata.model.projection.BackupResultIdentityProjection;

import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

/**
 * Context methods for interacting with {@link BackupResult BackupResults} in Morpheus
 * @since 0.13.4
 * @author Dustin DeYoung
 */
public interface MorpheusBackupResultService extends MorpheusDataService<BackupResult, BackupResultIdentityProjection>, MorpheusIdentityService<BackupResultIdentityProjection> {

	/**
	 * Lists all backup result projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link BackupResult} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the backups to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<BackupResultIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all backup result projection objects for a specified backup.
	 * The projection is a subset of the properties on a full {@link BackupResult} object for sync matching.
	 * @param backup the {@link Backup} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<BackupResultIdentityProjection> listIdentityProjections(Backup backup);

	/**
	 * Lists all backup result projection objects for a specified account ID and backup.
	 * The projection is a subset of the properties on a full {@link BackupResult} object for sync matching.
	 * @param accountId the {@link com.morpheusdata.model.Account} identifier associated to the domains to be listed.
	 * @param backup the {@link Backup} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<BackupResultIdentityProjection> listIdentityProjectionsByAccount(Long accountId, Backup backup);

	/**
	 * Lists all {@link BackupResult} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupResult} ids to fetch.
	 * @return an RxJava Observable stream of {@link BackupResult} objects for subscription.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<BackupResult> listById(Collection<Long> ids);

	/**
	 * Lists all backup result projection objects for a specified account ID and backup.
	 * The projection is a subset of the properties on a full {@link BackupResult} object for sync matching.
	 * @param backupSetId the {@link com.morpheusdata.model.BackupResult} backupSetId
	 * @param containerId the {@link com.morpheusdata.model.Container} identifier of the container associated to the backup result.
	 * @return an RxJava Observable stream of {@link BackupResult} objects for subscription.
	 */
	@Deprecated(since="0.15.3", forRemoval=true)
	Observable<BackupResult> listByBackupSetIdAndContainerId(String backupSetId, Long containerId);
}
