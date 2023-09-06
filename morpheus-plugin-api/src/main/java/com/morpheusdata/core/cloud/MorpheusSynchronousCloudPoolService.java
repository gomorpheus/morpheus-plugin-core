package com.morpheusdata.core.cloud;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.projection.CloudPoolIdentity;

public interface MorpheusSynchronousCloudPoolService extends MorpheusSynchronousDataService<CloudPool,CloudPoolIdentity>, MorpheusSynchronousIdentityService<CloudPoolIdentity> {
}
