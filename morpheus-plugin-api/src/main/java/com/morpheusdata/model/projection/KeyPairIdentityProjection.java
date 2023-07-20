package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.KeyPair} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Dan DeVilbiss
 * @since 0.15.0
 */
public class KeyPairIdentityProjection extends MorpheusModel {
	protected String name;
	protected String externalId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}
}
