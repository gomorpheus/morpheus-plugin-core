package com.morpheusdata.model;

import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;

/**
 * SecurityGroupRuleApplications are used to model the firewall rule Applications. For example,
 * the application services in NSX-T firewalls are modeled using SecurityGroupRuleApplication.
 */
public class SecurityGroupRuleApplication extends MorpheusModel {

	protected SecurityGroupRuleIdentityProjection securityGroupRule;

	protected String name;
	protected String applicationType = "port"; //port or service or protocol group
	protected String application;
	protected String applicationRefType;
	protected String applicationRefId;
	protected String portRange;
	protected String sourcePortRange;
	protected String protocol;
	protected String icmpType;
	//detailed ports
	protected String sourceFromPort;
	protected String sourceToPort;
	protected String destinationFromPort;
	protected String destinationToPort;
	protected Boolean enabled = true;
	//linking
	protected String internalId;
	protected String externalId;
	protected String uniqueId;
	protected String providerId;
	protected String externalType;
	protected String iacId; //id for infrastructure as code integrations
	protected String refType;
	protected Long refId;
	protected String rawData;

	public SecurityGroupRuleIdentityProjection getSecurityGroupRule() {
		return securityGroupRule;
	}

	public void setSecurityGroupRule(SecurityGroupRuleIdentityProjection securityGroupRule) {
		this.securityGroupRule = securityGroupRule;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getApplicationRefType() {
		return applicationRefType;
	}

	public void setApplicationRefType(String applicationRefType) {
		this.applicationRefType = applicationRefType;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}

	public String getPortRange() {
		return portRange;
	}

	public void setPortRange(String portRange) {
		this.portRange = portRange;
	}

	public String getSourcePortRange() {
		return sourcePortRange;
	}

	public void setSourcePortRange(String sourcePortRange) {
		this.sourcePortRange = sourcePortRange;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getIcmpType() {
		return icmpType;
	}

	public void setIcmpType(String icmpType) {
		this.icmpType = icmpType;
	}

	public String getSourceFromPort() {
		return sourceFromPort;
	}

	public void setSourceFromPort(String sourceFromPort) {
		this.sourceFromPort = sourceFromPort;
	}

	public String getSourceToPort() {
		return sourceToPort;
	}

	public void setSourceToPort(String sourceToPort) {
		this.sourceToPort = sourceToPort;
	}

	public String getDestinationFromPort() {
		return destinationFromPort;
	}

	public void setDestinationFromPort(String destinationFromPort) {
		this.destinationFromPort = destinationFromPort;
	}

	public String getDestinationToPort() {
		return destinationToPort;
	}

	public void setDestinationToPort(String destinationToPort) {
		this.destinationToPort = destinationToPort;
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

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
