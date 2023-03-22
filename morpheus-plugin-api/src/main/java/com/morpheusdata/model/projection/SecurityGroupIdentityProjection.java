package com.morpheusdata.model.projection;

import com.morpheusdata.model.projection.MorpheusIdentityModel;


public class SecurityGroupIdentityProjection extends MorpheusIdentityModel {

	public SecurityGroupIdentityProjection(){}

	public SecurityGroupIdentityProjection(Long id, String name, String externalId, String category) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
		this.category = category;
	}

	protected String name;
	protected String externalId;
	protected String category;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
