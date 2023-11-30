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
}
