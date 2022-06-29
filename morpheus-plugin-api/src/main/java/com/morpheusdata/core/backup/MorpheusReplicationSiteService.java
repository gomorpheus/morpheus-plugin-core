package com.morpheusdata.core.backup;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.ReplicationSite;

import com.morpheusdata.model.projection.ReplicationSiteIdentityProjection;

import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

public interface MorpheusReplicationSiteService {

	//ORM Object Methods
	/**
	 * Lists all backup projection objects for a specified replication site id.
	 * The projection is a subset of the properties on a full {@link ReplicationSite} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the replication sites to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<ReplicationSiteIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all backup projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link ReplicationSite} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<ReplicationSiteIdentityProjection> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all {@link ReplicationSite} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link ReplicationSite} ids to fetch.
	 * @return an RxJava Observable stream of {@link ReplicationSite} objects for subscription.
	 */
	Observable<ReplicationSiteIdentityProjection> listById(Collection<Long> ids);

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
	Single<Boolean> remove(List<ReplicationSiteIdentityProjection> removeList);

	/**
	 * Creates new Backup Domains from cache / sync implementations
	 * @param addList List of new {@link ReplicationSite} objects to be inserted into the database
	 * @return notification of completion
	 */
	Single<Boolean> create(List<ReplicationSite> addList);

	/**
	 * Saves a list of {@link ReplicationSite} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<ReplicationSite> saveList);

	/**
	 * Saves a {@link ReplicationSite} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param replicationSite a Replication Site Object to be updated in the database.
	 * @return the Single Observable containing the resulting Backup Object
	 */
	Single<ReplicationSite> save(ReplicationSite replicationSite);

}
