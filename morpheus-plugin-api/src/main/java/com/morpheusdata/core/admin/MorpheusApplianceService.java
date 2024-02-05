package com.morpheusdata.core.admin;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.ApplianceInstance;
import com.morpheusdata.model.projection.ApplianceInstanceIdentityProjection;

public interface MorpheusApplianceService extends MorpheusDataService<ApplianceInstance, ApplianceInstanceIdentityProjection>, MorpheusIdentityService<ApplianceInstanceIdentityProjection> {
}
