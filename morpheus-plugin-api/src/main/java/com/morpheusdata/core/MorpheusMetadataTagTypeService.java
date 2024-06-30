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

import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.MetadataTagType;
import com.morpheusdata.model.projection.MetadataTagTypeIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing MetadataTagTypes in Morpheus
 */
public interface MorpheusMetadataTagTypeService extends MorpheusDataService<MetadataTagType, MetadataTagTypeIdentityProjection>, MorpheusIdentityService<MetadataTagType> {

	/**
	 * Get a list of MetadataTagType projections based on the refId and refType associated with the MetadataTagType
	 * @param refType the refType to match on. Typically 'ComputeZone' for Cloud related tags
	 * @param refId the refId to match on. Typically the id of the Cloud for Cloud related tags
	 * @return Observable stream of sync projection
	 */
	Observable<MetadataTagTypeIdentityProjection> listSyncProjections(String refType, Long refId);
	
	/**
	 * Get a list of MetadataTagType objects from a list of projection ids
	 * @param ids MetadataTagType ids
	 * @return Observable stream of MetadataTagType
	 */
	@Deprecated(since="0.15.4")
	Observable<MetadataTagType> listById(Collection<Long> ids);

	/**
	 * Save updates to existing MetadataTagTypes
	 * @param metadataTagTypes updated MetadataTagTypes
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<MetadataTagType> metadataTagTypes);

	/**
	 * Create new MetadataTagTypes in Morpheus
	 * @param metadataTagTypes new MetadataTagType to persist
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<MetadataTagType> metadataTagTypes);
	
	/**
	 * Remove persisted MetadataTagTypes from Morpheus
	 * @param metadataTagTypes MetadataTagTypes to delete
	 * @return success
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<MetadataTagTypeIdentityProjection> metadataTagTypes);
}
