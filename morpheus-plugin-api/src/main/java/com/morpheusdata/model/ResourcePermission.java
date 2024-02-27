package com.morpheusdata.model;

public class ResourcePermission extends MorpheusModel {
	protected Long morpheusResourceId;
	protected String morpheusResourceType;

	public Long getMorpheusResourceId() {
		return morpheusResourceId;
	}

	public void setMorpheusResourceId(Long morpheusResourceId) {
		this.morpheusResourceId = morpheusResourceId;
	}

	public String getMorpheusResourceType() {
		return morpheusResourceType;
	}

	public void setMorpheusResourceType(String morpheusResourceType) {
		this.morpheusResourceType = morpheusResourceType;
	}
}
