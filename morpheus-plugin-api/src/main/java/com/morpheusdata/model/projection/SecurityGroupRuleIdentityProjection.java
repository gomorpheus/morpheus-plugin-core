package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;


public class SecurityGroupRuleIdentityProjection extends MorpheusModel {

	public SecurityGroupRuleIdentityProjection(){}

	public SecurityGroupRuleIdentityProjection(Long id, String name, String externalId) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
	}

	protected String name;
	protected String externalId;

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
}
