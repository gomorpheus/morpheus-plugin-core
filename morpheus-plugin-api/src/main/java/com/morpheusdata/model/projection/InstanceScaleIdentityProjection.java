package com.morpheusdata.model.projection;

public class InstanceScaleIdentityProjection extends MorpheusIdentityModel {
	protected String name;
	protected String externalId;

	public InstanceScaleIdentityProjection(){}
	public InstanceScaleIdentityProjection(Long id, String name, String externalId) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
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
