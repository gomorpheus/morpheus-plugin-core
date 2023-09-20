package com.morpheusdata.core.providers;

import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 * This Provider interface is used in combination with {@link StorageProvider} to define a
 * {@link com.morpheusdata.model.StorageServerType} that can create and delete object storage
 * buckets.
 *
 * @since 0.15.1
 * @see StorageProvider
 */
public interface StorageProviderBuckets {
	ServiceResponse validateBucket(StorageBucket storageBucket, Map opts);
	
	ServiceResponse createBucket(StorageBucket storageBucket, Map opts);

	ServiceResponse updateBucket(StorageBucket storageBucket, Map opts);

	ServiceResponse deleteBucket(StorageBucket storageBucket, Map opts);

	Collection<String> getStorageBucketProviderTypes();
}
