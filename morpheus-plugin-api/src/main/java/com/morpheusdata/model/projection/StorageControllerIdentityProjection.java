package com.morpheusdata.model.projection;

import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.StorageController} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Alex Clement
 * @since 0.13.0
 */
public class StorageControllerIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String name;
	protected String controllerKey;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getControllerKey() {
		return controllerKey;
	}

	public void setControllerKey(String controllerKey) {
		this.controllerKey = controllerKey;
		markDirty("controllerKey", controllerKey);
	}
}
