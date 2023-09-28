package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.PricePlan;
import com.morpheusdata.model.projection.PricePlanIdentityProjection;

public interface MorpheusSynchronousPricePlanService extends MorpheusSynchronousDataService<PricePlan, PricePlanIdentityProjection>, MorpheusSynchronousIdentityService<PricePlanIdentityProjection> {
}
