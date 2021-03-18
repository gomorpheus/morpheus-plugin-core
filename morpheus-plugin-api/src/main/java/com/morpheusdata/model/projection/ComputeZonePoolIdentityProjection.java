package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class ComputeZonePoolIdentityProjection extends MorpheusModel {
	protected String name;
	protected String externalId;
	protected String category;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}


}
