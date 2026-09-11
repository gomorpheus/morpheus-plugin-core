/*
 *  Copyright 2026 HPE Development Company, L.P.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.morpheusdata.core;

import com.morpheusdata.model.StorageReplicationGroup;
import com.morpheusdata.model.projection.StorageReplicationGroupIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

/**
 * Context methods for dealing with {@link StorageReplicationGroup} in Morpheus.
 * A replication group tracks the RPO, lag and failover state for a set of volumes
 * being replicated to a {@link com.morpheusdata.model.StorageReplicationPartner}.
 *
 * @since 1.5.0
 * @author HPE Storage Plugin Team
 */
public interface MorpheusStorageReplicationGroupService extends
		MorpheusDataService<StorageReplicationGroup, StorageReplicationGroupIdentityProjection>,
		MorpheusIdentityService<StorageReplicationGroupIdentityProjection> {

	/**
	 * List identity projections for all replication groups belonging to a storage server.
	 *
	 * @param storageServerId ID of the {@link com.morpheusdata.model.StorageServer}
	 * @return Observable stream of identity projections
	 */
	Observable<StorageReplicationGroupIdentityProjection> listIdentityProjections(Long storageServerId);

	/**
	 * List replication groups whose {@code lagSeconds} exceeds {@code targetRpoSeconds}.
	 * Used by the RPO breach detection scheduler.
	 *
	 * @param storageServerId ID of the {@link com.morpheusdata.model.StorageServer}
	 * @return Observable stream of breaching groups
	 */
	Observable<StorageReplicationGroup> listRpoBreaches(Long storageServerId);
}
