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
	 * Lists all Backup Repository projection objects for a specified backup provider id.
	 * The projection is a subset of the properties on a full {@link BackupRepository} object for sync matching.
	 * @param backupProvider the {@link AbstractBackupProvider} identifier associated to the backup repositories to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated(since="1.0.5", forRemoval=true)
	Observable<BackupRepositoryIdentityProjection> listIdentityProjections(BackupProvider backupProvider);

	/**
	 * Lists all {@link BackupRepository} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of {@link BackupRepository} ids to fetch.
	 * @return an RxJava Observable stream of {@link Backup} objects for subscription.
	 */
	@Deprecated(since="1.0.5", forRemoval=true)
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
	@Deprecated(since="1.0.5", forRemoval=true)
	Single<Boolean> remove(List<BackupRepositoryIdentityProjection> removeList);

	/**
	 * Creates new Backup Repository Domains from cache / sync implementations
	 * @param addList List of new {@link BackupRepository} objects to be inserted into the database
	 * @return notification of completion
	 */
	@Deprecated(since="1.0.5", forRemoval=true)
	Single<Boolean> create(List<BackupRepository> addList);

	/**
	 * Saves a list of {@link BackupRepository} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param saveList a List of Backup Repository objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	@Deprecated(since="1.0.5", forRemoval=true)
	Single<Boolean> save(List<BackupRepository> saveList);
}
