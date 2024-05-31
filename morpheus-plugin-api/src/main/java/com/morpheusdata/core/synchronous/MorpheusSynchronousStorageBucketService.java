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
