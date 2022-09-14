package com.morpheusdata.model;

import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;

/**
 * SecurityGroupRuleProfiles are used to model the firewall rule Profiles. For example,
 * the profiles in NSX-T firewalls are modeled using SecurityGroupRuleProfile.
 */
public class SecurityGroupRuleProfile extends MorpheusModel {

	protected SecurityGroupRuleIdentityProjection securityGroupRule;
	protected String name;
	protected String externalId;
	protected String rawData;
	protected String providerId;

	public SecurityGroupRuleIdentityProjection getSecurityGroupRule() {
		return securityGroupRule;
	}

	public void setSecurityGroupRule(SecurityGroupRuleIdentityProjection securityGroupRule) {
		this.securityGroupRule = securityGroupRule;
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

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
}
