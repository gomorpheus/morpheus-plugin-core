package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Instance;
import com.morpheusdata.model.projection.InstanceIdentityProjection;

public interface MorpheusSynchronousInstanceService extends MorpheusSynchronousDataService<Instance, InstanceIdentityProjection>, MorpheusSynchronousIdentityService<InstanceIdentityProjection> {
}
