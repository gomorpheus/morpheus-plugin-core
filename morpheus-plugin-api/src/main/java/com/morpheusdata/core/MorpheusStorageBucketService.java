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

import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.model.projection.StorageBucketIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Maybe;
import java.util.List;

/**
 * Context methods for dealing with {@link StorageBucket} in Morpheus
 */
public interface MorpheusStorageBucketService extends MorpheusDataService<StorageBucket,StorageBucketIdentityProjection>, MorpheusIdentityService<StorageBucketIdentityProjection> {

	/**
	 * Get a list of StorageBucket projections scoped to a {@link StorageServer}
	 * @param storageServer {@link StorageServer}
	 * @return Observable stream of sync projection
	 */
	Observable<StorageBucketIdentityProjection> listIdentityProjections(StorageServer storageServer);

	/**
	 * Get a list of StorageBucket projections scoped to a {@link StorageServer} and region code
	 * @param storageServer {@link StorageServer}
	 * @param region the region code
	 * @return Observable stream of sync projection
	 */
	Observable<StorageBucketIdentityProjection> listIdentityProjections(StorageServer storageServer, String region);

	/**
	 * Remove persisted StorageBuckets from Morpheus
	 * Removal is skipped if the buckets are still in use by a Backup,DeploymentVersion, or VirtualImage.
	 * @param storageBuckets List of {@link StorageBucketIdentityProjection} to delete
	 * @return success
	 */
	Single<Boolean> removeForSync(List<StorageBucketIdentityProjection> storageBuckets);
}
