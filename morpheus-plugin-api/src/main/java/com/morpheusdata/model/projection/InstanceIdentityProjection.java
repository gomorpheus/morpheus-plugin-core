package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Instance} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusCloudService
 * @author Mike Truso
 * @since 0.8.0
 */
public class InstanceIdentityProjection extends MorpheusIdentityModel {
	protected String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
