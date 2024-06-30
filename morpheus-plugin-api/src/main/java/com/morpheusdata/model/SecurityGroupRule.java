/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.SecurityGroupIdentityProjection;
import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * SecurityGroupRules are used to model the routing rules on Security Groups or firewalls. For example,
 * the Inbound and Outbound rules in Amazon are modeled using SecurityGroupRule. The protocol, port range,
 * type, source, destination, and policy (among other attributes) can be specified.
 */
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
    protected SecurityGroupIdentityProjection sourceGroup;
    //destination
    protected String destination; // used by morpheus for comparison
    protected String destinationType = "instance"; //cidr, group, tier, instance
    @JsonSerialize(using= ModelAsIdOnlySerializer.class)
    protected SecurityGroupIdentityProjection destinationGroup;
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
	protected List<SecurityGroupRuleApplication> applications = new ArrayList<>();
	protected List<SecurityGroupRuleDestination> destinations = new ArrayList<>();
	protected List<SecurityGroupRuleProfile> profiles = new ArrayList<>();
	protected List<SecurityGroupRuleScope> scopes = new ArrayList<>();
	protected List<SecurityGroupRuleSource> sources = new ArrayList<>();

	/**
	 * The SecurityGroup for which this SecurityGroupRule applies
	 * @return The SecurityGroup for which this SecurityGroupRule applies
	 */
	public SecurityGroupIdentityProjection getSecurityGroup() {
        return securityGroup;
    }

	/**
	 * The SecurityGroup for which this SecurityGroupRule applies
	 * @param securityGroup The SecurityGroup for which this SecurityGroupRule applies
	 */
    public void setSecurityGroup(SecurityGroupIdentityProjection securityGroup) {
        this.securityGroup = securityGroup;
    }

	/**
	 * Some clouds group SecurityGroupRules into a group. The name of that group may be specified
	 * @return Some clouds group SecurityGroupRules into a group. The name of that group may be specified
	 */
    public String getGroupName() {
        return groupName;
    }

	/**
	 * Some clouds group SecurityGroupRules into a group. The name of that group may be specified
	 * @param groupName Some clouds group SecurityGroupRules into a group. The name of that group may be specified
	 */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

	/**
	 * Some clouds group SecurityGroupRules into a group. The type of that group may be specified. Either instance, firewall, or router is typical
	 * @return Some clouds group SecurityGroupRules into a group. The type of that group may be specified. Either instance, firewall, or router is typical
	 */
    public String getGroupType() {
        return groupType;
    }

	/**
	 * Some clouds group SecurityGroupRules into a group. The type of that group may be specified. Either instance, firewall, or router is typical
	 * @param groupType Some clouds group SecurityGroupRules into a group. The type of that group may be specified. Either instance, firewall, or router is typical
	 */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	/**
	 * The type of SecurityGroupRule. In most cases, should be set to 'custom'
	 * @return The type of SecurityGroupRule. In most cases, should be set to 'custom'
	 */
    public String getRuleType() {
        return ruleType;
    }

	/**
	 * The type of SecurityGroupRule. In most cases, should be set to 'custom'
	 * @param ruleType The type of SecurityGroupRule. In most cases, should be set to 'custom'
	 */
    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

	/**
	 * The direction for the SecurityGroupRule. Options are 'ingress', 'egress', or 'any'
	 * @return The direction for the SecurityGroupRule. Options are 'ingress', 'egress', or 'any'
	 */
    public String getDirection() {
        return direction;
    }

	/**
	 * The direction for the SecurityGroupRule. Options are 'ingress', 'egress', or 'any'
	 * @param direction The direction for the SecurityGroupRule. Options are 'ingress', 'egress', or 'any'
	 */
    public void setDirection(String direction) {
        this.direction = direction;
    }

	/**
	 * The ether type. Either 'IPv4' or 'IPv6'
	 * @return The ether type. Either 'IPv4' or 'IPv6'
	 */
    public String getEtherType() {
        return etherType;
    }

	/**
	 * The ether type. Either 'IPv4' or 'IPv6'
	 * @param etherType The ether type. Either 'IPv4' or 'IPv6'
	 */
    public void setEtherType(String etherType) {
        this.etherType = etherType;
    }

	/**
	 * The type of policy. Either 'accept' or 'reject'
	 * @return The type of policy. Either 'accept' or 'reject'
	 */
    public String getPolicy() {
        return policy;
    }

	/**
	 * The type of policy. Either 'accept' or 'reject'
	 * @param policy The type of policy. Either 'accept' or 'reject'
	 */
    public void setPolicy(String policy) {
        this.policy = policy;
    }

	/**
	 * The priority for the rule
	 * @return The priority for the rule
	 */
    public Integer getPriority() {
        return priority;
    }

	/**
	 * The priority for the rule
	 * @param priority The priority for the rule
	 */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

	/**
	 * The cidr for the source of the rule. i.e. 10.0.0.0/23
	 * @return The cidr for the source of the rule. i.e. 10.0.0.0/23
	 */
    public String getSource() {
        return source;
    }

	/**
	 * The cidr for the source of the rule. i.e. 10.0.0.0/23
	 * @param source The cidr for the source of the rule. i.e. 10.0.0.0/23
	 */
    public void setSource(String source) {
        this.source = source;
    }

	/**
	 * The source type. 'all', 'cidr', 'group'
	 * @return The source type. 'all', 'cidr', 'group'
	 */
    public String getSourceType() {
        return sourceType;
    }

	/**
	 * The source type. 'all', 'cidr', 'group'
	 * @param sourceType The source type. 'all', 'cidr', 'group'
	 */
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

	/**
	 * The source SecurityGroup may be set. Some clouds refer to other SecurityGroups (like Amazon)
	 * @return The source SecurityGroup may be set. Some clouds refer to other SecurityGroups (like Amazon)
	 */
    public SecurityGroupIdentityProjection getSourceGroup() {
        return sourceGroup;
    }

	/**
	 * The source SecurityGroup may be set. Some clouds refer to other SecurityGroups (like Amazon)
	 * @param sourceGroup The source SecurityGroup may be set. Some clouds refer to other SecurityGroups (like Amazon)
	 */
    public void setSourceGroup(SecurityGroupIdentityProjection sourceGroup) {
        this.sourceGroup = sourceGroup;
    }

	/**
	 * The cidr for the destination of the rule. i.e. 10.0.0.0/23
	 * @return The cidr for the destination of the rule. i.e. 10.0.0.0/23
	 */
    public String getDestination() {
        return destination;
    }

	/**
	 * The cidr for the destination of the rule. i.e. 10.0.0.0/23
	 * @param destination The cidr for the destination of the rule. i.e. 10.0.0.0/23
	 */
    public void setDestination(String destination) {
        this.destination = destination;
    }

	/**
	 * The destination type. 'all', 'cidr', 'group'
	 * @return The destination type. 'all', 'cidr', 'group'
	 */
    public String getDestinationType() {
        return destinationType;
    }

	/**
	 * The destination type. 'all', 'cidr', 'group'
	 * @param destinationType The destination type. 'all', 'cidr', 'group'
	 */
    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

	/**
	 * The destination SecurityGroup may be set. Some clouds refer to other SecurityGroups (like Amazon)
	 * @return The destination SecurityGroup may be set. Some clouds refer to other SecurityGroups (like Amazon)
	 */
    public SecurityGroupIdentityProjection getDestinationGroup() {
        return destinationGroup;
    }

	/**
	 * The destination SecurityGroup may be set. Some clouds refer to other SecurityGroups (like Amazon)
	 * @param destinationGroup
	 */
    public void setDestinationGroup(SecurityGroupIdentityProjection destinationGroup) {
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

	/**
	 * Always set to 'port'
	 * @return Always set to 'port'
	 */
    public String getApplicationType() {
        return applicationType;
    }

	/**
	 * Always set to 'port'
	 * @param applicationType Always set to 'port'
	 */
    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

	/**
	 * Not used
	 * @return Not used
	 */
    public String getApplication() {
        return application;
    }

	/**
	 * Not used
	 * @param application Not used
	 */
    public void setApplication(String application) {
        this.application = application;
    }

	/**
	 * The port range for the rule. (i.e. 0-65535 or 10000-10050)
	 * @return The port range for the rule. (i.e. 0-65535 or 10000-10050)
	 */
    public String getPortRange() {
        return portRange;
    }

	/**
	 * The port range for the rule. (i.e. 0-65535 or 10000-10050)
	 * @param portRange The port range for the rule. (i.e. 0-65535 or 10000-10050)
	 */
    public void setPortRange(String portRange) {
        this.portRange = portRange;
    }

	/**
	 * The source port range for the rule. (i.e. 0-65535 or 10000-10050)
	 * @return The source port range for the rule. (i.e. 0-65535 or 10000-10050)
	 */
    public String getSourcePortRange() {
        return sourcePortRange;
    }

	/**
	 * The source port range for the rule. (i.e. 0-65535 or 10000-10050)
	 * @param sourcePortRange The source port range for the rule. (i.e. 0-65535 or 10000-10050)
	 */
    public void setSourcePortRange(String sourcePortRange) {
        this.sourcePortRange = sourcePortRange;
    }

	/**
	 * The destination port range for the rule. (i.e. 0-65535 or 10000-10050)
	 * @return The destination port range for the rule. (i.e. 0-65535 or 10000-10050)
	 */
    public String getDestinationPortRange() {
        return destinationPortRange;
    }

	/**
	 * The destination port range for the rule. (i.e. 0-65535 or 10000-10050)
	 * @param destinationPortRange The destination port range for the rule. (i.e. 0-65535 or 10000-10050)
	 */
    public void setDestinationPortRange(String destinationPortRange) {
        this.destinationPortRange = destinationPortRange;
    }

	/**
	 * The protocol for the rule ('icmp', 'tcp', 'all')
	 * @return The protocol for the rule ('icmp', 'tcp', 'all')
	 */
    public String getProtocol() {
        return protocol;
    }

	/**
	 * The protocol for the rule ('icmp', 'tcp', 'all')
	 * @param protocol The protocol for the rule ('icmp', 'tcp', 'all')
	 */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

	/**
	 * Not used
	 * @return Not used
	 */
    public String getIcmpType() {
        return icmpType;
    }

	/**
	 * Not used
	 * @param icmpType Not used
	 */
    public void setIcmpType(String icmpType) {
        this.icmpType = icmpType;
    }

	/**
	 * The port for the source (i.e. 3389)
	 * @return The port for the source (i.e. 3389)
	 */
    public String getSourceFromPort() {
        return sourceFromPort;
    }

	/**
	 * The port for the source (i.e. 3389)
	 * @param sourceFromPort The port for the source (i.e. 3389)
	 */
    public void setSourceFromPort(String sourceFromPort) {
        this.sourceFromPort = sourceFromPort;
    }

	/**
	 * The port to the source (i.e. 3389)
	 * @return The port to the source (i.e. 3389)
	 */
    public String getSourceToPort() {
        return sourceToPort;
    }

	/**
	 * The port to the source (i.e. 3389)
	 * @param sourceToPort The port to the source (i.e. 3389)
	 */
    public void setSourceToPort(String sourceToPort) {
        this.sourceToPort = sourceToPort;
    }

	/**
	 * The port from the destination (i.e. 3389)
	 * @return The port from the destination (i.e. 3389)
	 */
    public String getDestinationFromPort() {
        return destinationFromPort;
    }

	/**
	 * The port from the destination (i.e. 3389)
	 * @param destinationFromPort The port from the destination (i.e. 3389)
	 */
    public void setDestinationFromPort(String destinationFromPort) {
        this.destinationFromPort = destinationFromPort;
    }

	/**
	 * The port to the destination (i.e. 3389)
	 * @return The port to the destination (i.e. 3389)
	 */
    public String getDestinationToPort() {
        return destinationToPort;
    }

	/**
	 * The port to the destination (i.e. 3389)
	 * @param destinationToPort The port to the destination (i.e. 3389)
	 */
    public void setDestinationToPort(String destinationToPort) {
        this.destinationToPort = destinationToPort;
    }

	/**
	 * An internal id
	 * @return An internal id
	 */
    public String getInternalId() {
        return internalId;
    }

	/**
	 * An internal id
	 * @param internalId An internal id
	 */
    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

	/**
	 * A unique id
	 * @return A unique id
	 */
    public String getUniqueId() {
        return uniqueId;
    }

	/**
	 * A unique id
	 * @param uniqueId A unique id
	 */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

	/**
	 * The id used by the provider. May be used for sync operations
	 * @return The id used by the provider. May be used for sync operations
	 */
    public String getProviderId() {
        return providerId;
    }

	/**
	 * The id used by the provider. May be used for sync operations
	 * @param providerId The id used by the provider. May be used for sync operations
	 */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

	/**
	 * An external type
	 * @return An external type
	 */
    public String getExternalType() {
        return externalType;
    }

	/**
	 * An external type
	 * @param externalType An external type
	 */
    public void setExternalType(String externalType) {
        this.externalType = externalType;
    }

	/**
	 * An infrastructure as code id
	 * @return An infrastructure as code id
	 */
    public String getIacId() {
        return iacId;
    }

	/**
	 * An infrastructure as code id
	 * @param iacId An infrastructure as code id
	 */
    public void setIacId(String iacId) {
        this.iacId = iacId;
    }

	/**
	 * The raw data from the provider for the rule
	 * @return The raw data from the provider for the rule
	 */
    public String getRawData() {
        return rawData;
    }

	/**
	 * The raw data from the provider for the rule
	 * @param rawData The raw data from the provider for the rule
	 */
    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

	/**
	 * Whether the rule is enabled
	 * @return Whether the rule is enabled
	 */
    public Boolean getEnabled() {
        return enabled;
    }

	/**
	 * Whether the rule is enabled
	 * @param enabled Whether the rule is enabled
	 */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	/**
	 * Not used
	 * @return
	 */
    public Boolean getVisible() {
        return visible;
    }

	/**
	 * Not used
	 * @param visible Not used
	 */
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

	/**
	 * Not used
	 * @return Not used
	 */
    public String getScope() {
        return scope;
    }

	/**
	 * Not used
	 * @param scope Not used
	 */
    public void setScope(String scope) {
        this.scope = scope;
    }

	/**
	 * Not used
	 * @return Not used
	 */
    public String getProfile() {
        return profile;
    }

	/**
	 * Not used
	 * @param profile Not used
	 */
    public void setProfile(String profile) {
        this.profile = profile;
    }

	/**
	 * Indicates how the rule was created. Either 'internal', or 'external'
	 * @return Indicates how the rule was created. Either 'internal', or 'external'
	 */
    public String getSyncSource() {
        return syncSource;
    }

	/**
	 * Indicates how the rule was created. Either 'internal', or 'external'
	 * @param syncSource Indicates how the rule was created. Either 'internal', or 'external'
	 */
    public void setSyncSource(String syncSource) {
        this.syncSource = syncSource;
    }

	/**
	 * The Locations for this SecurityGroupRule. A location usually indicates a cloud. For example, a
	 * SecurityGroupRule can exist in multiple locations or clouds
	 * @return The locations for this SecurityGroupRule
	 */
	public List<SecurityGroupRuleLocation> getLocations() {
		return locations;
	}

	/**
	 * The Locations for this SecurityGroupRule. A location usually indicates a cloud. For example, a
	 * SecurityGroupRule can exist in multiple locations or clouds
	 * @param locations The locations for this SecurityGroupRule
	 */
	public void setLocations(List<SecurityGroupRuleLocation> locations) {
		this.locations = locations;
	}

	/**
	 * The Applications for this SecurityGroupRule. For example, NSX-T associates Applications (like
	 * APP_HTTP) to rules and these are modeled via the SecurityGroupRuleApplication
	 * @return The Applications for this SecurityGroupRule
	 */
	public List<SecurityGroupRuleApplication> getApplications() {
		return applications;
	}

	/**
	 * The Applications for this SecurityGroupRule. For example, NSX-T associates Applications (like
	 * APP_HTTP) to rules and these are modeled via the SecurityGroupRuleApplication
	 * @param applications The Applications for this SecurityGroupRule
	 */
	public void setApplications(List<SecurityGroupRuleApplication> applications) {
		this.applications = applications;
	}

	/**
	 * The Destinations for this SecurityGroupRule. For example, NSX-T associates Destinations (like
	 * ipset-2) to firewall rules and these are modeled with SecurityGroupRuleDestination and
	 * SecurityGroupRules
	 * @return The Destinations for this SecurityGroupRule
	 */
	public List<SecurityGroupRuleDestination> getDestinations() {
		return destinations;
	}

	/**
	 * The Destinations for this SecurityGroupRule. For example, NSX-T associates Destinations (like
	 * ipset-2) to firewall rules and these are modeled with SecurityGroupRuleDestination and
	 * SecurityGroupRules
	 * @param destinations The Destinations for this SecurityGroupRule
	 */
	public void setDestinations(List<SecurityGroupRuleDestination> destinations) {
		this.destinations = destinations;
	}

	/**
	 * The Profiles for this SecurityGroupRule. For example, NSX-T associates Profiles
	 * to firewall rules and these are modeled with SecurityGroupRuleProfile and
	 * SecurityGroupRules The Profiles for this SecurityGroupRule
	 * @return
	 */
	public List<SecurityGroupRuleProfile> getProfiles() {
		return profiles;
	}

	/**
	 * The Profiles for this SecurityGroupRule. For example, NSX-T associates Profiles
	 * to firewall rules and these are modeled with SecurityGroupRuleProfile and
	 * SecurityGroupRules
	 * @param profiles The Profiles for this SecurityGroupRule
	 */
	public void setProfiles(List<SecurityGroupRuleProfile> profiles) {
		this.profiles = profiles;
	}

	/**
	 * The Scopes for this SecurityGroupRule. For example, NSX-T associates Scopes
	 * to firewall rules and these are modeled with SecurityGroupRuleScope and
	 * SecurityGroupRules
	 * @return The Scopes for this SecurityGroupRule
	 */
	public List<SecurityGroupRuleScope> getScopes() {
		return scopes;
	}

	/**
	 * The Scopes for this SecurityGroupRule. For example, NSX-T associates Scopes
	 * to firewall rules and these are modeled with SecurityGroupRuleScope and
	 * SecurityGroupRules
	 * @param scopes The Scopes for this SecurityGroupRule
	 */
	public void setScopes(List<SecurityGroupRuleScope> scopes) {
		this.scopes = scopes;
	}

	/**
	 * The Sources for this SecurityGroupRule. For example, NSX-T associates Sources
	 * to firewall rules and these are modeled with SecurityGroupRuleSource and
	 * SecurityGroupRule
	 * @return The Sources for this SecurityGroupRule
	 */
	public List<SecurityGroupRuleSource> getSources() {
		return sources;
	}

	/**
	 * The Sources for this SecurityGroupRule. For example, NSX-T associates Sources
	 * to firewall rules and these are modeled with SecurityGroupRuleSource and
	 * SecurityGroupRule
	 * @param sources The Sources for this SecurityGroupRule
	 */
	public void setSources(List<SecurityGroupRuleSource> sources) {
		this.sources = sources;
	}

	/**
	 * Is this rule a custom rule or a predefined rule.
	 * @return Boolean
	 */
	public Boolean isCustomRule() {
		return ruleType != null && (ruleType.equals("custom") || ruleType.equals("customRule"));
	}

	/**
	 * The minimum port from the port range
	 * @return the minimum port number
	 */
	public Integer getMinPort() {
		Integer rtn = null;
		if(portRange != null) {
			try {
				rtn = Integer.parseInt(portRange);
			} catch(NumberFormatException ignored) {}

			if(rtn == null) {
				String[] ports = portRange.split("-");
				rtn = Integer.parseInt(ports[0]);
			}
		}
		return rtn;
	}

	/**
	 * The maximum port from the port range
	 * @return the maximum port number
	 */
	public Integer getMaxPort() {
		Integer rtn = null;
		if(portRange != null) {
			try {
				rtn = Integer.parseInt(portRange);
			} catch(NumberFormatException ignored) {}

			if(rtn == null) {
				String[] ports = portRange.split("-");
				rtn = Integer.parseInt(ports[1]);
			}
		}
		return rtn;
	}
}
