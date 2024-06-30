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

package com.morpheusdata.core;

import com.morpheusdata.model.WikiPage;
import com.morpheusdata.model.projection.WikiPageIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing Wiki Pages in Morpheus
 */
public interface MorpheusWikiPageService extends MorpheusDataService<WikiPage, WikiPageIdentityProjection>, MorpheusIdentityService<WikiPageIdentityProjection> {

	/**
	 * Get a list of WikiPage projections based on the refIds and refType
	 * @param refType the refType to match on. Typically, 'ComputeServer'
	 * @param refIds the refIds to match on. Typically, the ids of the Compute Servers
	 * @return Observable stream of sync projection
	 */
	@Deprecated(since="0.15.4")
	Observable<WikiPageIdentityProjection> listSyncProjections(String refType, List<Long> refIds);

	/**
	 * Get a list of WikiPage objects from a list of projection ids
	 * @param ids WikiPage ids
	 * @return Observable stream of WikiPage
	 */
	@Deprecated(since="0.15.4")
	Observable<WikiPage> listById(Collection<Long> ids);

	/**
	 * Save updates to existing WikiPages
	 * @param wikiPages updated WikiPages
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<WikiPage> wikiPages);

	/**
	 * Create new WikiPages in Morpheus
	 * @param wikiPages new WikiPages to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<WikiPage> wikiPages);

	/**
	 * Remove persisted WikiPages from Morpheus
	 * @param wikiPages WikiPages to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<WikiPageIdentityProjection> wikiPages);
}
