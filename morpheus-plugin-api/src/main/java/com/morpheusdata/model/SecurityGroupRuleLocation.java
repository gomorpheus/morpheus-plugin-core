package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.SecurityGroupIdentityProjection;
import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class SecurityGroupRuleLocation extends MorpheusModel {

    protected SecurityGroupRuleIdentityProjection securityGroupRule;
    protected String code;
    protected String category;
    protected String name;
    protected String groupName; //for things that also group rules into a set
    protected String ruleType;
    protected String direction = "ingress"; //ingress, egress
    protected String etherType;
    protected String policy = "accept"; //accept, drop
    protected Integer priority;
    //source
    protected String source;
    protected String sourceType = "cidr"; //cidr, group, tier, all
    @JsonSerialize(using= ModelAsIdOnlySerializer.class)
    protected SecurityGroupIdentityProjection sourceGroup;
    //destination
    protected String destination;
    protected String destinationType = "instance"; //cidr, group, tier, instance
    @JsonSerialize(using= ModelAsIdOnlySerializer.class)
    protected SecurityGroupIdentityProjection destinationGroup;
    //application
    protected String applicationType = "port"; //port or service or protocol group
    protected String application;
    protected String portRange;
    protected String sourcePortRange;
	protected String destinationPortRange;
    protected String protocol;
    protected String icmpType;
    //detailed ports
    protected String sourceFromPort;
    protected String sourceToPort;
    protected String destinationFromPort;
    protected String destinationToPort;
    //linking
    protected String internalId;
    protected String externalId;
    protected String refType; //usually the zone its in
    protected Long refId;
    protected String subRefType; //the instance or serveer its tied to in an ACI like env
    protected Long subRefId;
    protected String uniqueId;
    protected String providerId;
    protected String externalType;
    protected String iacId; //id for infrastructure as code integrations
    protected String rawData;
    protected Boolean enabled;
    protected Boolean visible;
    protected String profile;
    protected String syncSource = "external";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getEtherType() {
        return etherType;
    }

    public void setEtherType(String etherType) {
        this.etherType = etherType;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public SecurityGroupIdentityProjection getSourceGroup() {
        return sourceGroup;
    }

    public void setSourceGroup(SecurityGroupIdentityProjection sourceGroup) {
        this.sourceGroup = sourceGroup;
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

    public SecurityGroupIdentityProjection getDestinationGroup() {
        return destinationGroup;
    }

    public void setDestinationGroup(SecurityGroupIdentityProjection destinationGroup) {
        this.destinationGroup = destinationGroup;
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

	public String getDestinationPortRange() {
		return destinationPortRange;
	}

	public void setDestinationPortRange(String destinationPortRange) {
		this.destinationPortRange = destinationPortRange;
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

    public String getSubRefType() {
        return subRefType;
    }

    public void setSubRefType(String subRefType) {
        this.subRefType = subRefType;
    }

    public Long getSubRefId() {
        return subRefId;
    }

    public void setSubRefId(Long subRefId) {
        this.subRefId = subRefId;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getSyncSource() {
        return syncSource;
    }

    public void setSyncSource(String syncSource) {
        this.syncSource = syncSource;
    }

    public SecurityGroupRuleIdentityProjection getSecurityGroupRule() {
        return securityGroupRule;
    }

    public void setSecurityGroupRule(SecurityGroupRuleIdentityProjection securityGroupRule) {
        this.securityGroupRule = securityGroupRule;
    }
}
