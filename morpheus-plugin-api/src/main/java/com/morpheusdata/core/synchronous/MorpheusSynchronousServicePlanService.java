package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.ServicePlan;
import com.morpheusdata.model.projection.ServicePlanIdentityProjection;

public interface MorpheusSynchronousServicePlanService extends MorpheusSynchronousDataService<ServicePlan, ServicePlanIdentityProjection>, MorpheusSynchronousIdentityService<ServicePlanIdentityProjection> {
}
