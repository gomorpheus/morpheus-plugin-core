package com.morpheusdata.model;

public class NetworkServerType extends MorpheusModel {
	public String code;
	public String name;
	public String description;
	public Boolean creatable = true;
	public Boolean selectable = true;
	public String networkService;
	public Boolean enabled = true;
	public Boolean userVisible = false;
	public Boolean internalService = false;
	public String integrationCode; //matching integration type;
	public String securityCode; //matching security type;
	public String viewSet;
	public String brandingImageName;
	//config
	public Boolean hasScopes = false;
	public String titleScopes;
	public Boolean hasNetworks = false;
	public String titleNetworks;
	public Boolean hasSwitches = false;
	public String titleSwitches;
	public Boolean hasGateways = false;
	public String titleGateways;
	public Boolean hasRouters = false;
	public String titleRouters;
	public Boolean hasSecurityGroups = false;
	public Boolean hasSecurityGroupPriority = false;
	public Boolean hasSecurityGroupRulePriority = false;
	public String titleSecurityGroups;
	public Boolean hasLoadBalancers = false;
	public String titleLoadBalancers;
	public Boolean hasRouteTables = false;
	public String titleRouteTables;
	public Boolean hasFirewall = false;
	public Boolean supportsEditFirewallGroup = false;
	public Boolean supportsEditFirewallRule = false;
	public Boolean supportsFirewallRuleAppliedTarget = false;
	public String titleFirewall;
	public Boolean hasFirewallGroups = false;
	public String titleFirewallGroups;
	public Boolean manageSecurityGroups = false;
	public Boolean supportsEditScopes = false;
	public String titleEdgeClusters;
	public Boolean hasEdgeClusters = false;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable);
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
		markDirty("selectable", selectable);
	}

	public String getNetworkService() {
		return networkService;
	}

	public void setNetworkService(String networkService) {
		this.networkService = networkService;
		markDirty("networkService", networkService);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getUserVisible() {
		return userVisible;
	}

	public void setUserVisible(Boolean userVisible) {
		this.userVisible = userVisible;
		markDirty("userVisible", userVisible);
	}

	public Boolean getInternalService() {
		return internalService;
	}

	public void setInternalService(Boolean internalService) {
		this.internalService = internalService;
		markDirty("internalService", internalService);
	}

	public String getIntegrationCode() {
		return integrationCode;
	}

	public void setIntegrationCode(String integrationCode) {
		this.integrationCode = integrationCode;
		markDirty("integrationCode", integrationCode);
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
		markDirty("securityCode", securityCode);
	}

	public String getViewSet() {
		return viewSet;
	}

	public void setViewSet(String viewSet) {
		this.viewSet = viewSet;
		markDirty("viewSet", viewSet);
	}

	public String getBrandingImageName() {
		return brandingImageName;
	}

	public void setBrandingImageName(String brandingImageName) {
		this.brandingImageName = brandingImageName;
		markDirty("brandingImageName", brandingImageName);
	}

	public Boolean getHasScopes() {
		return hasScopes;
	}

	public void setHasScopes(Boolean hasScopes) {
		this.hasScopes = hasScopes;
		markDirty("hasScopes", hasScopes);
	}

	public String getTitleScopes() {
		return titleScopes;
	}

	public void setTitleScopes(String titleScopes) {
		this.titleScopes = titleScopes;
		markDirty("titleScopes", titleScopes);
	}

	public Boolean getHasNetworks() {
		return hasNetworks;
	}

	public void setHasNetworks(Boolean hasNetworks) {
		this.hasNetworks = hasNetworks;
		markDirty("hasNetworks", hasNetworks);
	}

	public String getTitleNetworks() {
		return titleNetworks;
	}

	public void setTitleNetworks(String titleNetworks) {
		this.titleNetworks = titleNetworks;
		markDirty("titleNetworks", titleNetworks);
	}

	public Boolean getHasSwitches() {
		return hasSwitches;
	}

	public void setHasSwitches(Boolean hasSwitches) {
		this.hasSwitches = hasSwitches;
		markDirty("hasSwitches", hasSwitches);
	}

	public String getTitleSwitches() {
		return titleSwitches;
	}

	public void setTitleSwitches(String titleSwitches) {
		this.titleSwitches = titleSwitches;
		markDirty("titleSwitches", titleSwitches);
	}

	public Boolean getHasGateways() {
		return hasGateways;
	}

	public void setHasGateways(Boolean hasGateways) {
		this.hasGateways = hasGateways;
		markDirty("hasGateways", hasGateways);
	}

	public String getTitleGateways() {
		return titleGateways;
	}

	public void setTitleGateways(String titleGateways) {
		this.titleGateways = titleGateways;
		markDirty("titleGateways", titleGateways);
	}

	public Boolean getHasRouters() {
		return hasRouters;
	}

	public void setHasRouters(Boolean hasRouters) {
		this.hasRouters = hasRouters;
		markDirty("hasRouters", hasRouters);
	}

	public String getTitleRouters() {
		return titleRouters;
	}

	public void setTitleRouters(String titleRouters) {
		this.titleRouters = titleRouters;
		markDirty("titleRouters", titleRouters);
	}

	public Boolean getHasSecurityGroups() {
		return hasSecurityGroups;
	}

	public void setHasSecurityGroups(Boolean hasSecurityGroups) {
		this.hasSecurityGroups = hasSecurityGroups;
		markDirty("hasSecurityGroups", hasSecurityGroups);
	}

	public Boolean getHasSecurityGroupPriority() {
		return hasSecurityGroupPriority;
	}

	public void setHasSecurityGroupPriority(Boolean hasSecurityGroupPriority) {
		this.hasSecurityGroupPriority = hasSecurityGroupPriority;
		markDirty("hasSecurityGroupPriority", hasSecurityGroupPriority);
	}

	public Boolean getHasSecurityGroupRulePriority() {
		return hasSecurityGroupRulePriority;
	}

	public void setHasSecurityGroupRulePriority(Boolean hasSecurityGroupRulePriority) {
		this.hasSecurityGroupRulePriority = hasSecurityGroupRulePriority;
		markDirty("hasSecurityGroupRulePriority", hasSecurityGroupRulePriority);
	}

	public String getTitleSecurityGroups() {
		return titleSecurityGroups;
	}

	public void setTitleSecurityGroups(String titleSecurityGroups) {
		this.titleSecurityGroups = titleSecurityGroups;
		markDirty("titleSecurityGroups", titleSecurityGroups);
	}

	public Boolean getHasLoadBalancers() {
		return hasLoadBalancers;
	}

	public void setHasLoadBalancers(Boolean hasLoadBalancers) {
		this.hasLoadBalancers = hasLoadBalancers;
		markDirty("hasLoadBalancers", hasLoadBalancers);
	}

	public String getTitleLoadBalancers() {
		return titleLoadBalancers;
	}

	public void setTitleLoadBalancers(String titleLoadBalancers) {
		this.titleLoadBalancers = titleLoadBalancers;
		markDirty("titleLoadBalancers", titleLoadBalancers);
	}

	public Boolean getHasRouteTables() {
		return hasRouteTables;
	}

	public void setHasRouteTables(Boolean hasRouteTables) {
		this.hasRouteTables = hasRouteTables;
		markDirty("hasRouteTables", hasRouteTables);
	}

	public String getTitleRouteTables() {
		return titleRouteTables;
	}

	public void setTitleRouteTables(String titleRouteTables) {
		this.titleRouteTables = titleRouteTables;
		markDirty("titleRouteTables", titleRouteTables);
	}

	public Boolean getHasFirewall() {
		return hasFirewall;
	}

	public void setHasFirewall(Boolean hasFirewall) {
		this.hasFirewall = hasFirewall;
		markDirty("hasFirewall", hasFirewall);
	}

	public Boolean getSupportsEditFirewallGroup() {
		return supportsEditFirewallGroup;
	}

	public void setSupportsEditFirewallGroup(Boolean supportsEditFirewallGroup) {
		this.supportsEditFirewallGroup = supportsEditFirewallGroup;
		markDirty("supportsEditFirewallGroup", supportsEditFirewallGroup);
	}

	public Boolean getSupportsEditFirewallRule() {
		return supportsEditFirewallRule;
	}

	public void setSupportsEditFirewallRule(Boolean supportsEditFirewallRule) {
		this.supportsEditFirewallRule = supportsEditFirewallRule;
		markDirty("supportsEditFirewallRule", supportsEditFirewallRule);
	}

	public Boolean getSupportsFirewallRuleAppliedTarget() {
		return supportsFirewallRuleAppliedTarget;
	}

	public void setSupportsFirewallRuleAppliedTarget(Boolean supportsFirewallRuleAppliedTarget) {
		this.supportsFirewallRuleAppliedTarget = supportsFirewallRuleAppliedTarget;
		markDirty("supportsFirewallRuleAppliedTarget", supportsFirewallRuleAppliedTarget);
	}

	public String getTitleFirewall() {
		return titleFirewall;
	}

	public void setTitleFirewall(String titleFirewall) {
		this.titleFirewall = titleFirewall;
		markDirty("titleFirewall", titleFirewall);
	}

	public Boolean getHasFirewallGroups() {
		return hasFirewallGroups;
	}

	public void setHasFirewallGroups(Boolean hasFirewallGroups) {
		this.hasFirewallGroups = hasFirewallGroups;
		markDirty("hasFirewallGroups", hasFirewallGroups);
	}

	public String getTitleFirewallGroups() {
		return titleFirewallGroups;
	}

	public void setTitleFirewallGroups(String titleFirewallGroups) {
		this.titleFirewallGroups = titleFirewallGroups;
		markDirty("titleFirewallGroups", titleFirewallGroups);
	}

	public Boolean getManageSecurityGroups() {
		return manageSecurityGroups;
	}

	public void setManageSecurityGroups(Boolean manageSecurityGroups) {
		this.manageSecurityGroups = manageSecurityGroups;
		markDirty("manageSecurityGroups", manageSecurityGroups);
	}

	public Boolean getSupportsEditScopes() {
		return supportsEditScopes;
	}

	public void setSupportsEditScopes(Boolean supportsEditScopes) {
		this.supportsEditScopes = supportsEditScopes;
		markDirty("supportsEditScopes", supportsEditScopes);
	}

	public String getTitleEdgeClusters() {
		return titleEdgeClusters;
	}

	public void setTitleEdgeClusters(String titleEdgeClusters) {
		this.titleEdgeClusters = titleEdgeClusters;
		markDirty("titleEdgeClusters", titleEdgeClusters);
	}

	public Boolean getHasEdgeClusters() {
		return hasEdgeClusters;
	}

	public void setHasEdgeClusters(Boolean hasEdgeClusters) {
		this.hasEdgeClusters = hasEdgeClusters;
		markDirty("hasEdgeClusters", hasEdgeClusters);
	}
}
