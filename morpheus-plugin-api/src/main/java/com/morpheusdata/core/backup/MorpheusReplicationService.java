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
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.Replication;
import com.morpheusdata.model.ReplicationGroup;
import com.morpheusdata.model.ReplicationSite;
import com.morpheusdata.model.ReplicationType;

import com.morpheusdata.model.projection.ReplicationIdentityProjection;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.Collection;
import java.util.List;

public interface MorpheusReplicationService extends MorpheusDataService<Replication, ReplicationIdentityProjection>, MorpheusIdentityService<ReplicationIdentityProjection> {

	/**
	 * Returns the MorpheusReplicationGroupContext used for performing updates/queries on {@link ReplicationGroup} related assets
	 * within Morpheus.
	 * @return An instance of the MorpheusReplicationGroupContext to be used for calls by various backup providers
	 */
	MorpheusReplicationGroupService getReplicationGroup();

	/**
	 * Returns the MorpheusReplicationTypeContext used for performing updates/queries on {@link ReplicationSite} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusReplicationSiteService getReplicationSite();

	/**
	 * Returns the MorpheusReplicationTypeContext used for performing updates/queries on {@link ReplicationType} related assets
	 * within Morpheus.
	 * @return An instance of the BackupTypeContext to be used for calls by various backup providers
	 */
	MorpheusReplicationTypeService getType();

	//ORM Object Methods
	/**
	 * Lists all backup projection objects for a specified replication id.
	 * The projection is a subset of the properties on a full {@link Replication} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the replications to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<ReplicationIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all backup projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Replication} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<ReplicationIdentityProjection> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all {@link Replication} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link Replication} ids to fetch.
	 * @return an RxJava Observable stream of {@link Replication} objects for subscription.
	 */
	@Deprecated(since="0.15.4")
	Observable<ReplicationIdentityProjection> listById(Collection<Long> ids);

	/**
	 * Removes Missing Backup on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getBackup().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of backup projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<ReplicationIdentityProjection> removeList);

	/**
	 * Creates new Backup Domains from cache / sync implementations
	 * @param addList List of new {@link Replication} objects to be inserted into the database
	 * @return notification of completion
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<Replication> addList);

	/**
	 * Saves a list of {@link Replication} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<Replication> saveList);
}
