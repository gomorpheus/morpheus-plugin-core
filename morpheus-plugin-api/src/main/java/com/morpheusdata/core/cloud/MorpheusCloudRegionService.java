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
import com.morpheusdata.model.CloudRegion;
import com.morpheusdata.model.projection.CloudRegionIdentity;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.Optional;

/**
 * Provides service/context methods for querying {@link com.morpheusdata.model.CloudRegion} objects for use with syncing a clouds regions
 * These regions are useful for all region based clouds as well when needing to iterate over regions to sync items.
 * NOTE: Morpheus used to internally call a Cloud a Zone. This is why some of these models contain the Zone terminology.
 * @author David Estes
 * @since 0.14.0
 */
public interface MorpheusCloudRegionService extends MorpheusDataService<CloudRegion,CloudRegionIdentity>, MorpheusIdentityService<CloudRegionIdentity> {
	/**
	 * Get a list of {@link CloudRegion} projections based on Cloud id
	 *
	 * @param cloudId  Cloud id
	 * @return Observable stream of sync projection
	 */
	Observable<CloudRegionIdentity> listIdentityProjections(Long cloudId);


	Observable<CloudRegionIdentity> listIdentityProjectionsForRegionsWithVolumes(Long cloudId);
	Observable<CloudRegionIdentity> listIdentityProjectionsForRegionsWithCloudPools(Long cloudId);


	Single<Optional<CloudRegion>> findByCloudAndRegionCode(Long cloudId, String regionCode);
}
