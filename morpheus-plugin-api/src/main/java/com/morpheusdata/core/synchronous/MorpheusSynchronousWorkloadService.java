package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Workload;
import com.morpheusdata.model.projection.WorkloadIdentityProjection;

public interface MorpheusSynchronousWorkloadService extends MorpheusSynchronousDataService<Workload, WorkloadIdentityProjection>, MorpheusSynchronousIdentityService<WorkloadIdentityProjection> {
}
