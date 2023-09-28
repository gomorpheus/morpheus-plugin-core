package com.morpheusdata.core.synchronous.backup;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.ReplicationSite;
import com.morpheusdata.model.projection.ReplicationSiteIdentityProjection;

public interface MorpheusSynchronousReplicationSiteService extends MorpheusSynchronousDataService<ReplicationSite, ReplicationSiteIdentityProjection>, MorpheusSynchronousIdentityService<ReplicationSiteIdentityProjection> {
}