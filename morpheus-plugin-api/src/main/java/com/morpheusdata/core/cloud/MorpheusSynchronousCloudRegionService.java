package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.CloudRegion;
import com.morpheusdata.model.projection.CloudRegionIdentity;

public interface MorpheusSynchronousCloudRegionService extends MorpheusSynchronousDataService<CloudRegion,CloudRegionIdentity>, MorpheusSynchronousIdentityService<CloudRegionIdentity> {
}
