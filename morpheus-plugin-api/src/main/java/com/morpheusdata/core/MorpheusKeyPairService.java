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

import com.morpheusdata.model.KeyPair;
import com.morpheusdata.model.projection.KeyPairIdentityProjection;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for InstanceScaleType in Morpheus
 */
public interface MorpheusKeyPairService extends MorpheusDataService<KeyPair,KeyPairIdentityProjection> {

	/**
	 * Get a list of {@link KeyPairIdentityProjection} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link com.morpheusdata.model.ComputeZoneRegion} to optionally filter by
	 * @return Observable stream of identity projection
	 */
	Observable<KeyPairIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	Maybe<KeyPair> findOrGenerateByAccount(Long accountId);

	Single<Boolean> addZoneKeyPairLocation(Long cloudId, String locationId, String keyId);

	Single<Boolean> addKeyPairLocation(Long keyPairId, String locationId, String keyId);

}
