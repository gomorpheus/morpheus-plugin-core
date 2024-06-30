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

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.projection.CloudPoolIdentity;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link CloudPool} in Morpheus
 * <p><strong>This service is accessible in the {@link MorpheusContext} via the following traversal path:</strong> <br>
 * {@code morpheusContext.getAsync().getCloud().getPool()}</p>
 * @author Mike Truso
 */
public interface MorpheusCloudPoolService extends MorpheusDataService<CloudPool,CloudPoolIdentity>, MorpheusIdentityService<CloudPoolIdentity> {

	/**
	 * Get a list of {@link CloudPool} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @param category filter by category
	 * @return Observable stream of sync projection
	 */
	Observable<CloudPoolIdentity> listIdentityProjections(Long cloudId, String category, String regionCode);

	/**
	 * Get a list of {@link CloudPool} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @param category filter by category
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, String, String)}}
	 */
	@Deprecated
	Observable<CloudPoolIdentity> listSyncProjections(Long cloudId, String category);


	/**
	 * Returns a list of all pools filtered by a cloud as well as a list of externalIds. This is useful for chunked syncs where a cloud can contain
	 * a LARGE amount of pools
	 * @param cloudId the current id of the cloud we are filtering by
	 * @param externalIds a list of external ids to fetch by within the cloud scope
	 * @return Observable stream of CloudPools filtered by cloud and a collection of externalIds
	 */
	Observable<CloudPool> listByCloudAndExternalIdIn(Long cloudId, Collection<String> externalIds);


	/**
	 * Returns a pool from a pool ID string (usually starts with pool- or poolGroup-) obtained from user inputs. In the case of a pool group ID a
	 * pool will be selected based on the group's selection mode
	 * @param poolId
	 * @param accountId
	 * @param siteId
	 * @param zoneId
	 * @return a cloud pool or null
	 */
	Maybe<CloudPool> get(String poolId, Long accountId, Long siteId, Long zoneId);

}
