package com.morpheusdata.core;

import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.model.projection.StorageBucketIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.Maybe;
import java.util.List;

/**
 * Context methods for dealing with {@link StorageBucket} in Morpheus
 */
public interface MorpheusStorageBucketService extends MorpheusDataService<StorageBucket,StorageBucketIdentityProjection> {

	/**
	 * Get a {@link StorageBucket} by id.
	 * @param id StorageBucket id
	 * @return Observable stream of sync projection
	 */
	Maybe<StorageBucket> get(Long id);

	/**
	 * Get a list of StorageBucket objects from a list of ids
	 * @param ids StorageBucket ids
	 * @return Observable stream of StorageBucket
	 */
	Observable<StorageBucket> listById(List<Long> ids);

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