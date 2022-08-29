package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.SecurityGroupIdentityProjection;
import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.List;

public class SecurityGroupRule extends SecurityGroupRuleIdentityProjection {

    protected SecurityGroupIdentityProjection securityGroup;
    protected String groupName; //for things that also group rules into a set
    protected String groupType = "instance"; //firewall,instance,router
    protected String description;
    protected String ruleType = "custom";
    protected String direction = "ingress"; //ingress, egress // used by morpheus for comparison
    protected String etherType; // used by morpheus for comparison (if not set, defaults to IPv4)
    protected String policy = "accept"; //accept, drop
    protected Integer priority;
    //source
    protected String source; // used by morpheus for comparison
    protected String sourceType = "cidr"; //cidr, group, tier, all
    @JsonSerialize(using= ModelAsIdOnlySerializer.class)
    protected SecurityGroup sourceGroup;
    //destination
    protected String destination; // used by morpheus for comparison
    protected String destinationType = "instance"; //cidr, group, tier, instance
    @JsonSerialize(using= ModelAsIdOnlySerializer.class)
    protected SecurityGroup destinationGroup;
    //applied targets
    protected String appliedTarget;
    protected String appliedTargetType;
    //application
    protected String applicationType = "port"; //port or service or protocol group
    protected String application;
    protected String portRange; // used by morpheus for comparison
    protected String sourcePortRange;
    protected String destinationPortRange; // used by morpheus for comparison
    protected String protocol; // used by morpheus for comparison (if not set, defaults to 'any')
    protected String icmpType;
    //detailed ports
    protected String sourceFromPort;
    protected String sourceToPort;
    protected String destinationFromPort;
    protected String destinationToPort;
    //linking
    protected String internalId;
    protected String uniqueId;
    protected String providerId;
    protected String externalType;
    protected String iacId; //id for infrastructure as code integrations
    //config
    protected String rawData;
    protected Boolean enabled;
    protected Boolean visible;
    protected String scope;
    protected String profile;
    protected String syncSource = "external";

	protected List<SecurityGroupRuleLocation> locations = new ArrayList<>();

    public SecurityGroupIdentityProjection getSecurityGroup() {
        return securityGroup;
    }

    public void setSecurityGroup(SecurityGroupIdentityProjection securityGroup) {
        this.securityGroup = securityGroup;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public SecurityGroup getSourceGroup() {
        return sourceGroup;
    }

    public void setSourceGroup(SecurityGroup sourceGroup) {
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

    public SecurityGroup getDestinationGroup() {
        return destinationGroup;
    }

    public void setDestinationGroup(SecurityGroup destinationGroup) {
        this.destinationGroup = destinationGroup;
    }

    public String getAppliedTarget() {
        return appliedTarget;
    }

    public void setAppliedTarget(String appliedTarget) {
        this.appliedTarget = appliedTarget;
    }

    public String getAppliedTargetType() {
        return appliedTargetType;
    }

    public void setAppliedTargetType(String appliedTargetType) {
        this.appliedTargetType = appliedTargetType;
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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

	public List<SecurityGroupRuleLocation> getLocations() {
		return locations;
	}

	public void setLocations(List<SecurityGroupRuleLocation> locations) {
		this.locations = locations;
	}
}
