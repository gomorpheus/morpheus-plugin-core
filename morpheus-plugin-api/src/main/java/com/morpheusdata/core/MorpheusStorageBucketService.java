package com.morpheusdata.core;

import com.bertramlabs.plugins.karman.StorageProvider;
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

	/**
	 * Get a StorageProvider for a given StorageBucket
	 * @param storageBucketId the id of the StorageBucket
	 * @return Single stream of the StorageProvider for the storage bucket
	 */
	Single<StorageProvider> getBucketStorageProvider(Long storageBucketId);
}
