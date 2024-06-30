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

package com.morpheusdata.core.synchronous;

import com.bertramlabs.plugins.karman.StorageProvider;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.model.projection.StorageBucketIdentityProjection;
import io.reactivex.rxjava3.core.Single;

public interface MorpheusSynchronousStorageBucketService extends MorpheusSynchronousDataService<StorageBucket, StorageBucketIdentityProjection>, MorpheusSynchronousIdentityService<StorageBucketIdentityProjection> {

	/**
	 * Get a StorageProvider for a given StorageBucket
	 * @param storageBucketId the id of the StorageBucket
	 * @return Single stream of the StorageProvider for the storage bucket
	 */
	StorageProvider getBucketStorageProvider(Long storageBucketId);
}
