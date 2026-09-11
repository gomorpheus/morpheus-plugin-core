/*
 *  Copyright 2026 HPE Development Company, L.P.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.StorageReplicationGroup;
import com.morpheusdata.model.projection.StorageReplicationGroupIdentityProjection;

import java.util.List;

/**
 * Synchronous context methods for {@link StorageReplicationGroup} in Morpheus.
 * A replication group tracks the RPO, lag and failover state for a set of volumes
 * being replicated to a {@link com.morpheusdata.model.StorageReplicationPartner}.
 *
 * @since 1.5.0
 * @author HPE Storage Plugin Team
 */
public interface MorpheusSynchronousStorageReplicationGroupService extends
		MorpheusSynchronousDataService<StorageReplicationGroup, StorageReplicationGroupIdentityProjection>,
		MorpheusSynchronousIdentityService<StorageReplicationGroupIdentityProjection> {

	/**
	 * List identity projections for all replication groups belonging to a storage server.
	 *
	 * @param storageServerId ID of the {@link com.morpheusdata.model.StorageServer}
	 * @return a List of identity projections
	 */
	List<StorageReplicationGroupIdentityProjection> listIdentityProjections(Long storageServerId);

	/**
	 * List replication groups whose {@code lagSeconds} exceeds {@code targetRpoSeconds}.
	 * Used by the RPO breach detection scheduler.
	 *
	 * @param storageServerId ID of the {@link com.morpheusdata.model.StorageServer}
	 * @return a List of breaching groups
	 */
	List<StorageReplicationGroup> listRpoBreaches(Long storageServerId);
}
