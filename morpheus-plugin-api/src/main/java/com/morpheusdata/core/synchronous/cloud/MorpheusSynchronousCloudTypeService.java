package com.morpheusdata.core.synchronous.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.CloudType;
import com.morpheusdata.model.projection.CloudTypeIdentityProjection;

public interface MorpheusSynchronousCloudTypeService extends MorpheusSynchronousDataService<CloudType, CloudTypeIdentityProjection>, MorpheusSynchronousIdentityService<CloudTypeIdentityProjection> {
}
