package com.morpheusdata.model;

import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;

/**
 * SecurityGroupRuleDestinations are used to model the firewall rule Destinations. For example,
 * the destination groups in NSX-T firewalls are modeled using SecurityGroupRuleDestination.
 */
public class SecurityGroupRuleDestination extends MorpheusModel {

	protected SecurityGroupRuleIdentityProjection securityGroupRule;

	protected String name;
	protected String destination;
	protected String destinationType = "instance"; //cidr, group, tier, instance
	protected String destinationRefType; //external type and id
	protected String destinationRefId;
	protected Boolean enabled = true;
	//linking
	protected String internalId;
	protected String externalId;
	protected String uniqueId;
	protected String providerId;
	protected String externalType;
	protected String iacId; //id for infrastructure as code integrations
	protected String rawData;

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

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(String destinationType) {
		this.destinationType = destinationType;
	}

	public String getDestinationRefType() {
		return destinationRefType;
	}

	public void setDestinationRefType(String destinationRefType) {
		this.destinationRefType = destinationRefType;
	}

	public String getDestinationRefId() {
		return destinationRefId;
	}

	public void setDestinationRefId(String destinationRefId) {
		this.destinationRefId = destinationRefId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
}
