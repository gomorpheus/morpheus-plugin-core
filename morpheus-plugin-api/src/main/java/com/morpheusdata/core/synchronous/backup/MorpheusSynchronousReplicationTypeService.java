package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.ReplicationType;
import com.morpheusdata.model.projection.ReplicationTypeIdentityProjection;

public interface MorpheusSynchronousReplicationTypeService extends MorpheusSynchronousDataService<ReplicationType, ReplicationTypeIdentityProjection>, MorpheusSynchronousIdentityService<ReplicationTypeIdentityProjection> {
}
