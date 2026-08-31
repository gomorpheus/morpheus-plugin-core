/*
 *  Copyright 2026 HPE Development Company, L.P.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.morpheusdata.core;

import com.morpheusdata.model.StorageReplicationPartner;
import com.morpheusdata.model.projection.StorageReplicationPartnerIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

/**
 * Context methods for dealing with {@link StorageReplicationPartner} in Morpheus.
 * A replication partner represents a remote storage system that a local array
 * replicates data to.
 *
 * @since 1.5.0
 * @author HPE Storage Plugin Team
 */
public interface MorpheusStorageReplicationPartnerService extends
		MorpheusDataService<StorageReplicationPartner, StorageReplicationPartnerIdentityProjection>,
		MorpheusIdentityService<StorageReplicationPartnerIdentityProjection> {

	/**
	 * List identity projections for all replication partners belonging to a storage server.
	 *
	 * @param storageServerId ID of the local {@link com.morpheusdata.model.StorageServer}
	 * @return Observable stream of identity projections
	 */
	Observable<StorageReplicationPartnerIdentityProjection> listIdentityProjections(Long storageServerId);
}
