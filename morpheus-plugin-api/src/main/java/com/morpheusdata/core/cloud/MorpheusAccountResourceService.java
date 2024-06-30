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
import com.morpheusdata.core.data.DataQuery;
import com.morpheusdata.model.AccountResource;
import com.morpheusdata.model.AccountResourceType;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.projection.AccountResourceIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

/**
 * Provides service/context methods for querying {@link com.morpheusdata.model.AccountResource} objects for use with syncing generic cloud resources.
 * These resources are useful for syncing non compute / vm related objects. For example, cloud native services often can be categorized as a resource
 * with a given {@link com.morpheusdata.model.AccountResourceType}. These even can be linked to deployed terraform resource artefacts or cloudformation
 *
 * <p><strong>This service is accessible in the {@link MorpheusContext} via the following traversal path:</strong> <br>{@code morpheusContext.getAsync().getCloud().getAccountResource()}</p>
 *
 * @author David Estes
 * @since 0.14.0
 */
public interface MorpheusAccountResourceService  extends MorpheusDataService<AccountResource,AccountResourceIdentityProjection>, MorpheusIdentityService<AccountResourceIdentityProjection> {
	/**
	 * Get a list of {@link AccountResource} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {@link MorpheusAccountResourceService#listIdentityProjections(DataQuery)}
	 */
	@Deprecated
	Observable<AccountResourceIdentityProjection> listIdentityProjections(Long cloudId);

	/**
	 * Get a list of {@link AccountResource} projections based on Cloud id and resource type code
	 * @param cloudId  Cloud id
	 * @param typeCode the {@link AccountResourceType#getCode()} to query by
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {@link MorpheusAccountResourceService#listIdentityProjections(DataQuery)}
	 */
	@Deprecated
	Observable<AccountResourceIdentityProjection> listIdentityProjections(Long cloudId,String typeCode);

	/**
	 * Get a list of {@link AccountResource} projections based on Cloud id and resource type code
	 *
	 * @param cloudId  Cloud id
	 * @param typeCode the {@link AccountResourceType#getCode()} to query by
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {@link MorpheusAccountResourceService#listIdentityProjections(DataQuery)}
	 */
	@Deprecated
	Observable<AccountResourceIdentityProjection> listIdentityProjections(Long cloudId,String typeCode, String regionCode);

	/**
	 * Get a list of {@link AccountResource} projections based on Cloud id and resource type code
	 *
	 * @param cloudId  Cloud id
	 * @param typeCode the {@link AccountResourceType#getCode()} to query by
	 * @param ownerId the
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {@link MorpheusAccountResourceService#listIdentityProjections(DataQuery)}
	 */
	@Deprecated
	Observable<AccountResourceIdentityProjection> listIdentityProjections(Long cloudId, String typeCode, String regionCode, Long ownerId);

}
