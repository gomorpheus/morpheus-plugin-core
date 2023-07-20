package com.morpheusdata.model;

import java.util.ArrayList;
import java.util.List;

public class NetworkRouterType extends MorpheusModel {
	protected String code;
	protected String name;
	protected String description;
	protected String routerService;
	protected Boolean enabled = true;
	protected Boolean creatable = true;
	protected Boolean selectable = true;
	protected Boolean deletable = true;
	protected Boolean editable = true;
	protected Boolean hasNetworkServer = false;
	//flags for what you can do
	protected Boolean hasDnsClient = false;
	protected Boolean hasFirewall = false;
	protected Boolean hasFirewallGroups = false;
	protected Boolean supportsEditFirewallRule = false;
	protected Boolean hasNat = false;
	protected Boolean hasRouting = false;
	protected Boolean hasGateway = false;
	protected Boolean hasStaticRouting = false;
	protected Boolean hasBgp = false;
	protected Boolean hasOspf = false;
	protected Boolean hasRouteRedistribution = false;
	protected Boolean hasMulticast = false;
	protected Boolean hasGre = false;
	protected Boolean hasBridging = false;
	protected Boolean hasLoadBalancing = false;
	protected Boolean hasDnsForwarding = false;
	protected Boolean hasDhcp = false;
	protected Boolean hasDhcpRelay = false;
	protected Boolean hasDhcpBinding = false;
	protected Boolean supportsEditDhcpGlobalConfig = false;
	protected Boolean hasSyslog = false;
	protected Boolean hasInterfaces = false;
	protected Boolean supportsMultipleAddressInterfaces = false;
	protected Boolean hasHighAvailability = false;
	protected Boolean hasSecurityGroupPriority = false;
	//vpn types
	protected Boolean hasSslVpn = false;
	protected Boolean hasL2tVpn = false;
	protected Boolean hasIpsecVpn = false;
	protected Boolean hasCertificates = false;
	//labeling
	protected String titleFirewallGroups;
	protected String titleFirewalls;
	protected String titleFirewallRule;
	protected String titleFirewallApplications;
	protected String titleDhcp;
	protected String titleDhcpRelay;
	protected String titleDhcpBinding;
	protected String titleDns;
	protected String titleNat;
	protected String titleRouting;
	protected String titleLoadBalancer;
	protected String titleVpn;
	protected String titleInterfaces;
	protected Boolean supportsEditRoute = false;
	protected String bgpListViewType;
	protected String natListViewType;
	protected String brandingImageName;

	//linking
	protected String integrationCode;//matching integration type

	private List<OptionType> optionTypes = new ArrayList<>();

	private List<OptionType> routeOptionTypes = new ArrayList<>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRouterService() {
		return routerService;
	}

	public void setRouterService(String routerService) {
		this.routerService = routerService;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}

	public Boolean getDeletable() {
		return deletable;
	}

	public void setDeletable(Boolean deletable) {
		this.deletable = deletable;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
	}


	public Boolean getHasNetworkServer() {
		return hasNetworkServer;
	}

	public void setHasNetworkServer(Boolean hasNetworkServer) {
		this.hasNetworkServer = hasNetworkServer;
	}

	public Boolean getHasDnsClient() {
		return hasDnsClient;
	}

	public void setHasDnsClient(Boolean hasDnsClient) {
		this.hasDnsClient = hasDnsClient;
	}

	public Boolean getHasFirewall() {
		return hasFirewall;
	}

	public void setHasFirewall(Boolean hasFirewall) {
		this.hasFirewall = hasFirewall;
	}

	public Boolean getHasFirewallGroups() {
		return hasFirewallGroups;
	}

	public void setHasFirewallGroups(Boolean hasFirewallGroups) {
		this.hasFirewallGroups = hasFirewallGroups;
	}

	public Boolean getSupportsEditFirewallRule() {
		return supportsEditFirewallRule;
	}

	public void setSupportsEditFirewallRule(Boolean supportsEditFirewallRule) {
		this.supportsEditFirewallRule = supportsEditFirewallRule;
	}

	public Boolean getHasNat() {
		return hasNat;
	}

	public void setHasNat(Boolean hasNat) {
		this.hasNat = hasNat;
	}

	public Boolean getHasRouting() {
		return hasRouting;
	}

	public void setHasRouting(Boolean hasRouting) {
		this.hasRouting = hasRouting;
	}

	public Boolean getHasGateway() {
		return hasGateway;
	}

	public void setHasGateway(Boolean hasGateway) {
		this.hasGateway = hasGateway;
	}

	public Boolean getHasStaticRouting() {
		return hasStaticRouting;
	}

	public void setHasStaticRouting(Boolean hasStaticRouting) {
		this.hasStaticRouting = hasStaticRouting;
	}

	public Boolean getHasBgp() {
		return hasBgp;
	}

	public void setHasBgp(Boolean hasBgp) {
		this.hasBgp = hasBgp;
	}

	public Boolean getHasOspf() {
		return hasOspf;
	}

	public void setHasOspf(Boolean hasOspf) {
		this.hasOspf = hasOspf;
	}

	public Boolean getHasRouteRedistribution() {
		return hasRouteRedistribution;
	}

	public void setHasRouteRedistribution(Boolean hasRouteRedistribution) {
		this.hasRouteRedistribution = hasRouteRedistribution;
	}

	public Boolean getHasMulticast() {
		return hasMulticast;
	}

	public void setHasMulticast(Boolean hasMulticast) {
		this.hasMulticast = hasMulticast;
	}

	public Boolean getHasGre() {
		return hasGre;
	}

	public void setHasGre(Boolean hasGre) {
		this.hasGre = hasGre;
	}

	public Boolean getHasBridging() {
		return hasBridging;
	}

	public void setHasBridging(Boolean hasBridging) {
		this.hasBridging = hasBridging;
	}

	public Boolean getHasLoadBalancing() {
		return hasLoadBalancing;
	}

	public void setHasLoadBalancing(Boolean hasLoadBalancing) {
		this.hasLoadBalancing = hasLoadBalancing;
	}

	public Boolean getHasDnsForwarding() {
		return hasDnsForwarding;
	}

	public void setHasDnsForwarding(Boolean hasDnsForwarding) {
		this.hasDnsForwarding = hasDnsForwarding;
	}

	public Boolean getHasDhcp() {
		return hasDhcp;
	}

	public void setHasDhcp(Boolean hasDhcp) {
		this.hasDhcp = hasDhcp;
	}

	public Boolean getHasDhcpRelay() {
		return hasDhcpRelay;
	}

	public void setHasDhcpRelay(Boolean hasDhcpRelay) {
		this.hasDhcpRelay = hasDhcpRelay;
	}

	public Boolean getHasDhcpBinding() {
		return hasDhcpBinding;
	}

	public void setHasDhcpBinding(Boolean hasDhcpBinding) {
		this.hasDhcpBinding = hasDhcpBinding;
	}

	public Boolean getSupportsEditDhcpGlobalConfig() {
		return supportsEditDhcpGlobalConfig;
	}

	public void setSupportsEditDhcpGlobalConfig(Boolean supportsEditDhcpGlobalConfig) {
		this.supportsEditDhcpGlobalConfig = supportsEditDhcpGlobalConfig;
	}

	public Boolean getHasSyslog() {
		return hasSyslog;
	}

	public void setHasSyslog(Boolean hasSyslog) {
		this.hasSyslog = hasSyslog;
	}

	public Boolean getHasInterfaces() {
		return hasInterfaces;
	}

	public void setHasInterfaces(Boolean hasInterfaces) {
		this.hasInterfaces = hasInterfaces;
	}

	public Boolean getSupportsMultipleAddressInterfaces() {
		return supportsMultipleAddressInterfaces;
	}

	public void setSupportsMultipleAddressInterfaces(Boolean supportsMultipleAddressInterfaces) {
		this.supportsMultipleAddressInterfaces = supportsMultipleAddressInterfaces;
	}

	public Boolean getHasHighAvailability() {
		return hasHighAvailability;
	}

	public void setHasHighAvailability(Boolean hasHighAvailability) {
		this.hasHighAvailability = hasHighAvailability;
	}

	public Boolean getHasSecurityGroupPriority() {
		return hasSecurityGroupPriority;
	}

	public void setHasSecurityGroupPriority(Boolean hasSecurityGroupPriority) {
		this.hasSecurityGroupPriority = hasSecurityGroupPriority;
	}

	public Boolean getHasSslVpn() {
		return hasSslVpn;
	}

	public void setHasSslVpn(Boolean hasSslVpn) {
		this.hasSslVpn = hasSslVpn;
	}

	public Boolean getHasL2tVpn() {
		return hasL2tVpn;
	}

	public void setHasL2tVpn(Boolean hasL2tVpn) {
		this.hasL2tVpn = hasL2tVpn;
	}

	public Boolean getHasIpsecVpn() {
		return hasIpsecVpn;
	}

	public void setHasIpsecVpn(Boolean hasIpsecVpn) {
		this.hasIpsecVpn = hasIpsecVpn;
	}

	public Boolean getHasCertificates() {
		return hasCertificates;
	}

	public void setHasCertificates(Boolean hasCertificates) {
		this.hasCertificates = hasCertificates;
	}

	public String getTitleFirewallGroups() {
		return titleFirewallGroups;
	}

	public void setTitleFirewallGroups(String titleFirewallGroups) {
		this.titleFirewallGroups = titleFirewallGroups;
	}

	public String getTitleFirewalls() {
		return titleFirewalls;
	}

	public void setTitleFirewalls(String titleFirewalls) {
		this.titleFirewalls = titleFirewalls;
	}

	public String getTitleFirewallRule() {
		return titleFirewallRule;
	}

	public void setTitleFirewallRule(String titleFirewallRule) {
		this.titleFirewallRule = titleFirewallRule;
	}

	public String getTitleFirewallApplications() {
		return titleFirewallApplications;
	}

	public void setTitleFirewallApplications(String titleFirewallApplications) {
		this.titleFirewallApplications = titleFirewallApplications;
	}

	public String getTitleDhcp() {
		return titleDhcp;
	}

	public void setTitleDhcp(String titleDhcp) {
		this.titleDhcp = titleDhcp;
	}

	public String getTitleDhcpRelay() {
		return titleDhcpRelay;
	}

	public void setTitleDhcpRelay(String titleDhcpRelay) {
		this.titleDhcpRelay = titleDhcpRelay;
	}

	public String getTitleDhcpBinding() {
		return titleDhcpBinding;
	}

	public void setTitleDhcpBinding(String titleDhcpBinding) {
		this.titleDhcpBinding = titleDhcpBinding;
	}

	public String getTitleDns() {
		return titleDns;
	}

	public void setTitleDns(String titleDns) {
		this.titleDns = titleDns;
	}

	public String getTitleNat() {
		return titleNat;
	}

	public void setTitleNat(String titleNat) {
		this.titleNat = titleNat;
	}

	public String getTitleRouting() {
		return titleRouting;
	}

	public void setTitleRouting(String titleRouting) {
		this.titleRouting = titleRouting;
	}

	public String getTitleLoadBalancer() {
		return titleLoadBalancer;
	}

	public void setTitleLoadBalancer(String titleLoadBalancer) {
		this.titleLoadBalancer = titleLoadBalancer;
	}

	public String getTitleVpn() {
		return titleVpn;
	}

	public void setTitleVpn(String titleVpn) {
		this.titleVpn = titleVpn;
	}

	public String getTitleInterfaces() {
		return titleInterfaces;
	}

	public void setTitleInterfaces(String titleInterfaces) {
		this.titleInterfaces = titleInterfaces;
	}

	public Boolean getSupportsEditRoute() {
		return supportsEditRoute;
	}

	public void setSupportsEditRoute(Boolean supportsEditRoute) {
		this.supportsEditRoute = supportsEditRoute;
	}

	public String getBgpListViewType() {
		return bgpListViewType;
	}

	public void setBgpListViewType(String bgpListViewType) {
		this.bgpListViewType = bgpListViewType;
	}

	public String getNatListViewType() {
		return natListViewType;
	}

	public void setNatListViewType(String natListViewType) {
		this.natListViewType = natListViewType;
	}

	public String getBrandingImageName() {
		return brandingImageName;
	}

	public void setBrandingImageName(String brandingImageName) {
		this.brandingImageName = brandingImageName;
	}

	public String getIntegrationCode() {
		return integrationCode;
	}

	public void setIntegrationCode(String integrationCode) {
		this.integrationCode = integrationCode;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) { this.optionTypes = optionTypes; }

	public List<OptionType> getRouteOptionTypes() {
		return routeOptionTypes;
	}

	public void setRouteOptionTypes(List<OptionType> optionTypes) { this.routeOptionTypes = optionTypes; }

}
