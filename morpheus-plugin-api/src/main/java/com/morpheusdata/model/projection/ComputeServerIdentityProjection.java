package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class ComputeServerIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String name;

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
}
