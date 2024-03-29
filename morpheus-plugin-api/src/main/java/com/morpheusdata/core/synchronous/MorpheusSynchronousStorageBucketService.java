package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.StorageBucket;
import com.morpheusdata.model.projection.StorageBucketIdentityProjection;

public interface MorpheusSynchronousStorageBucketService extends MorpheusSynchronousDataService<StorageBucket, StorageBucketIdentityProjection>, MorpheusSynchronousIdentityService<StorageBucketIdentityProjection> {
}
