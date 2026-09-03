/*
 *  Copyright 2026 HPE Development Company, L.P.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.StorageReplicationPartner;
import com.morpheusdata.model.projection.StorageReplicationPartnerIdentityProjection;

import java.util.List;

/**
 * Synchronous context methods for {@link StorageReplicationPartner} in Morpheus.
 * A replication partner represents a remote storage system that a local storage
 * server replicates data to.
 *
 * @since 1.5.0
 * @author HPE Storage Plugin Team
 */
public interface MorpheusSynchronousStorageReplicationPartnerService extends
		MorpheusSynchronousDataService<StorageReplicationPartner, StorageReplicationPartnerIdentityProjection>,
		MorpheusSynchronousIdentityService<StorageReplicationPartnerIdentityProjection> {

	/**
	 * List identity projections for all replication partners belonging to a storage server.
	 *
	 * @param storageServerId ID of the local {@link com.morpheusdata.model.StorageServer}
	 * @return a List of identity projections
	 */
	List<StorageReplicationPartnerIdentityProjection> listIdentityProjections(Long storageServerId);
}
