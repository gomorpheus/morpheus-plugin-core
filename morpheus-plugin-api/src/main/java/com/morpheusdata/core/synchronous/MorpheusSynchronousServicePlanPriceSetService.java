package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.ServicePlanPriceSet;
import com.morpheusdata.model.projection.ServicePlanPriceSetIdentityProjection;

public interface MorpheusSynchronousServicePlanPriceSetService extends MorpheusSynchronousDataService<ServicePlanPriceSet, ServicePlanPriceSetIdentityProjection>, MorpheusSynchronousIdentityService<ServicePlanPriceSetIdentityProjection> {
}
