package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ServicePlan} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see com.morpheusdata.core.MorpheusServicePlanContext
 * @author Mike Truso
 * @since 0.8.0
 */
public class ServicePlanIdentityProjection extends MorpheusModel {
	protected String code;
	protected String name;
	protected String externalId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

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
