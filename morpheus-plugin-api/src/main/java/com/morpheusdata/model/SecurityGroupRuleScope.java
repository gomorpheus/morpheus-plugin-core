package com.morpheusdata.model;

import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;

/**
 * SecurityGroupRuleScopes are used to model the firewall rule Scopes. For example,
 * the scopes in NSX-T firewalls are modeled using SecurityGroupRuleScope.
 */
public class SecurityGroupRuleScope extends MorpheusModel {

	protected SecurityGroupRuleIdentityProjection securityGroupRule;
	protected String name;
	protected String externalId;
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

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
}
