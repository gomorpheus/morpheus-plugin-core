package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.Snapshot;
import com.morpheusdata.model.projection.SnapshotIdentityProjection;

public interface MorpheusSynchronousSnapshotService extends MorpheusSynchronousDataService<Snapshot, SnapshotIdentityProjection>, MorpheusSynchronousIdentityService<SnapshotIdentityProjection> {
}
