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
	public String integrationCode; //matching integration type
	public String securityCode; //matching security type
	public String viewSet;
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
	public String titleSecurityGroups;
	public Boolean hasLoadBalancers = false;
	public String titleLoadBalancers;
	public Boolean hasRouteTables = false;
	public String titleRouteTables;
	public Boolean hasFirewall = false;
	public String titleFirewall;
	public Boolean hasFirewallGroups = false;
	public String titleFirewallGroups;
	public Boolean manageSecurityGroups = false;

//	static hasMany = [networkTypes:NetworkType, optionTypes:OptionType, routerTypes:NetworkRouterType,
//	scopeOptionTypes:OptionType, switchOptionTypes:OptionType, networkOptionTypes:OptionType,
//	gatewayOptionTypes:OptionType, routerOptionTypes:OptionType, loadBalancerOptionTypes:OptionType,
//	routeTableOptionTypes:OptionType, securityGroupOptionTypes:OptionType, ruleOptionTypes:OptionType,
//	firewallGroupOptionTypes:OptionType,
//	securityGroupRuleIngressDestTypes:SecurityGroupRuleTargetType,
//	securityGroupRuleEgressDestTypes:SecurityGroupRuleTargetType,
//	securityGroupRuleIngressSourceTypes:SecurityGroupRuleTargetType,
//	securityGroupRuleEgressSourceTypes:SecurityGroupRuleTargetType]
}
