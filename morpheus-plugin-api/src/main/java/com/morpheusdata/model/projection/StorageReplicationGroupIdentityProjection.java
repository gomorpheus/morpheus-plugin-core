/*
 *  Copyright 2026 HPE Development Company, L.P.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.morpheusdata.model.projection;

import com.morpheusdata.model.StorageReplicationGroup;

/**
 * Lightweight sync projection for {@link StorageReplicationGroup}.
 * @since 1.5.0
 */
public class StorageReplicationGroupIdentityProjection extends MorpheusIdentityModel {

	protected String name;
	protected String externalId;

	public StorageReplicationGroupIdentityProjection() {}
	public StorageReplicationGroupIdentityProjection(Long id, String externalId) {
		this.id = id;
		this.externalId = externalId;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; markDirty("name", name); }

	public String getExternalId() { return externalId; }
	public void setExternalId(String externalId) { this.externalId = externalId; markDirty("externalId", externalId); }
}
