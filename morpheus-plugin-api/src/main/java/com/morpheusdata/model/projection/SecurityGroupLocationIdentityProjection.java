package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;


public class SecurityGroupLocationIdentityProjection extends MorpheusModel {

	public SecurityGroupLocationIdentityProjection(){}

	public SecurityGroupLocationIdentityProjection(Long id, String name, String externalId, String category, Long refId, String refType, SecurityGroupIdentityProjection securityGroup) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
		this.category = category;
		this.refId = refId;
		this.refType = refType;
		this.securityGroup = securityGroup;
	}

	protected SecurityGroupIdentityProjection securityGroup;
	protected String name;
	protected String externalId;
	protected String category;
	protected Long refId;
	protected String refType;

	public SecurityGroupIdentityProjection getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(SecurityGroupIdentityProjection securityGroup) {
		this.securityGroup = securityGroup;
	}

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

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}
}
