package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusDatastoreService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Datastore} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusDatastoreService
 * @author Bob Whiton
 * @deprecated replaced by {@link DatastoreIdentity} since 0.15.3 for naming improvements
 */
@Deprecated(since="0.15.3",forRemoval = false)
public class DatastoreIdentityProjection extends DatastoreIdentity {

	public DatastoreIdentityProjection() {
		super();
	}
	public DatastoreIdentityProjection(Long cloudId, String externalId) {
		super(cloudId,externalId);
	}
}
