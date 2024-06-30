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

package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.Datastore;
import com.morpheusdata.model.projection.*;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link Datastore} in Morpheus
 *
 * @author Bob Whiton
 */
public interface MorpheusDatastoreService extends MorpheusDataService<Datastore,DatastoreIdentity>, MorpheusIdentityService<DatastoreIdentity> {

	/**
	 * Get a list of {@link Datastore} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<DatastoreIdentity> listSyncProjections(Long cloudId);

	/**
	 * Get the default image Datastore
	 * @param cloudId The id of the cloud
	 * @param accountId The id of the account
	 * @return The default image Datastore
	 */
	Single<Datastore> getDefaultImageDatastoreForAccount(Long cloudId, Long accountId);

	/**
	 * Get a list of Datastore objects from a list of projection ids
	 *
	 * @param ids Datastore ids
	 * @return Observable stream of Datastores
	 */
	Observable<Datastore> listById(Collection<Long> ids);

	/**
	 * Remove persisted Datastore from Morpheus
	 *
	 * @param datastores Datastores to delete
	 * @param zonePool CloudPoolIdentity representing the associated zonePool for the datastore
	 * @return success
	 */
	Single<Boolean> remove(List<DatastoreIdentity> datastores, CloudPoolIdentity zonePool);

}
