package com.morpheusdata.model;

import java.util.Collection;

public class NetworkLoadBalancerType extends MorpheusModel {
	protected String code;
	protected String name;
	protected Boolean enabled = true;
	protected Boolean internal = true;
	protected Boolean creatable = false;
	//instance level config - drives what you can do on provisioning
	protected Boolean supportsCerts = true;
	protected Boolean supportsHostname = true;
	protected Boolean supportsVip = true;
	protected Boolean supportsSticky = false;
	protected Boolean supportsBalancing = false;
	protected Boolean supportsScheme = false;
	protected Boolean supportsFloatingIp = false;
	protected Boolean supportsMonitor = false;
	protected Boolean supportsPoolDetail = false;
	protected Boolean editable = true;
	protected Boolean removable = true;
	protected String sharedVipMode = "none"; //'transparent', 'rules'
	protected String createType = "multi"; //multi, instance
	protected String format = "external";//external, vm, container
	protected String networkService;
	protected String initializeQueue;
	protected String viewSet;
	protected Integer certSize;
	//type level config - drives what is editable on the integration
	protected Boolean hasVirtualServers = false;
	protected Boolean hasVirtualServerPolicies = false;
	protected Boolean hasMonitors = false;
	protected Boolean hasNodes = false;
	protected Boolean hasNodeMonitors = false;
	protected Boolean hasNodeWeight = false;
	protected Boolean hasPolicies = false;
	protected Boolean hasProfiles = false;
	protected Boolean hasRules = false;
	protected Boolean hasScripts = false;
	protected Boolean hasServices = false;
	protected Boolean hasPools = false;
	protected Boolean hasPrivateVip = false;
	protected Boolean createVirtualServers = false;
	protected Boolean createVirtualServerPolicies = false;
	protected Boolean createMonitors = false;
	protected Boolean createNodes = false;
	protected Boolean createPolicies = false;
	protected Boolean createProfiles = false;
	protected Boolean createRules = false;
	protected Boolean createScripts = false;
	protected Boolean createServices = false;
	protected Boolean createPools = false;
	protected Boolean nameEditable = true;
	protected String poolMemberType = "NetworkLoadBalancerMember";  // NetworkLoadBalancerMember | NetworkLoadBalancerNode
	protected String nodeResourceType = "ipAddress"; // ipAddress, multi
	protected String imageCode;
	protected Boolean poolSupportsStatus = false;
	protected Boolean nodeSupportsStatus = false;

	protected Boolean instanceSupportsStatus = false;
	protected Boolean profileSupportsProxy = false;
	protected Boolean createPricePlans = false;
	protected Boolean profileSupportsPersistence = false;
	protected Boolean profilesEditable = false;
	protected CloudType cloudType;

	// associations
	Collection<OptionType> optionTypes;
	Collection<OptionType> vipOptionTypes;
	Collection<OptionType> poolOptionTypes;
	Collection<OptionType> profileOptionTypes;
	Collection<OptionType> policyOptionTypes;
	Collection<OptionType> policyRuleOptionTypes;
	Collection<OptionType> nodeOptionTypes;
	Collection<OptionType> monitorOptionTypes;
	Collection<OptionType> scriptOptionTypes;
	Collection<OptionType> instanceOptionTypes;
	Collection<OptionType> instancePolicyOptionTypes;
	Collection<OptionType> instanceRuleOptionTypes;


	// plugin shit
	protected Boolean isPlugin;
	protected String pluginLogoPrefix;
	protected String pluginIconPath;
	protected String pluginIconDarkPath;
	protected String pluginIconHidpiPath;
	protected String pluginIconDarkHidpiPath;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", this.code);
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", this.name);
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", this.enabled);
		this.enabled = enabled;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		markDirty("internal", this.internal);
		this.internal = internal;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		markDirty("creatable", this.creatable);
		this.creatable = creatable;
	}

	public Boolean getSupportsCerts() {
		return supportsCerts;
	}

	public void setSupportsCerts(Boolean supportsCerts) {
		markDirty("supportsCerts", this.supportsCerts);
		this.supportsCerts = supportsCerts;
	}

	public Boolean getSupportsHostname() {
		return supportsHostname;
	}

	public void setSupportsHostname(Boolean supportsHostname) {
		markDirty("supportsHostname", this.supportsHostname);
		this.supportsHostname = supportsHostname;
	}

	public Boolean getSupportsVip() {
		return supportsVip;
	}

	public void setSupportsVip(Boolean supportsVip) {
		markDirty("supportsVip", this.supportsVip);
		this.supportsVip = supportsVip;
	}

	public Boolean getSupportsSticky() {
		return supportsSticky;
	}

	public void setSupportsSticky(Boolean supportsSticky) {
		markDirty("supportsSticky", this.supportsSticky);
		this.supportsSticky = supportsSticky;
	}

	public Boolean getSupportsBalancing() {
		return supportsBalancing;
	}

	public void setSupportsBalancing(Boolean supportsBalancing) {
		markDirty("supportsBalancing", this.supportsBalancing);
		this.supportsBalancing = supportsBalancing;
	}

	public Boolean getSupportsScheme() {
		return supportsScheme;
	}

	public void setSupportsScheme(Boolean supportsScheme) {
		markDirty("supportsScheme", this.supportsScheme);
		this.supportsScheme = supportsScheme;
	}

	public Boolean getSupportsFloatingIp() {
		return supportsFloatingIp;
	}

	public void setSupportsFloatingIp(Boolean supportsFloatingIp) {
		markDirty("supportsFloatingIp", this.supportsFloatingIp);
		this.supportsFloatingIp = supportsFloatingIp;
	}

	public Boolean getSupportsMonitor() {
		return supportsMonitor;
	}

	public void setSupportsMonitor(Boolean supportsMonitor) {
		markDirty("supportsMonitor", this.supportsMonitor);
		this.supportsMonitor = supportsMonitor;
	}

	public Boolean getSupportsPoolDetail() {
		return supportsPoolDetail;
	}

	public void setSupportsPoolDetail(Boolean supportsPoolDetail) {
		markDirty("supportsPoolDetail", this.supportsPoolDetail);
		this.supportsPoolDetail = supportsPoolDetail;
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		markDirty("editable", this.editable);
		this.editable = editable;
	}

	public Boolean getRemovable() {
		return removable;
	}

	public void setRemovable(Boolean removable) {
		markDirty("removable", this.removable);
		this.removable = removable;
	}

	public String getSharedVipMode() {
		return sharedVipMode;
	}

	public void setSharedVipMode(String sharedVipMode) {
		markDirty("sharedVipMode", this.sharedVipMode);
		this.sharedVipMode = sharedVipMode;
	}

	public String getCreateType() {
		return createType;
	}

	public void setCreateType(String createType) {
		markDirty("createType", this.createType);
		this.createType = createType;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		markDirty("format", this.format);
		this.format = format;
	}

	public String getNetworkService() {
		return networkService;
	}

	public void setNetworkService(String networkService) {
		markDirty("networkService", this.networkService);
		this.networkService = networkService;
	}

	public String getInitializeQueue() {
		return initializeQueue;
	}

	public void setInitializeQueue(String initializeQueue) {
		markDirty("initializeQueue", this.initializeQueue);
		this.initializeQueue = initializeQueue;
	}

	public String getViewSet() {
		return viewSet;
	}

	public void setViewSet(String viewSet) {
		markDirty("viewSet", this.viewSet);
		this.viewSet = viewSet;
	}

	public Integer getCertSize() {
		return certSize;
	}

	public void setCertSize(Integer certSize) {
		markDirty("certSize", this.certSize);
		this.certSize = certSize;
	}

	public Boolean getHasVirtualServers() {
		return hasVirtualServers;
	}

	public void setHasVirtualServers(Boolean hasVirtualServers) {
		markDirty("hasVirtualServers", this.hasVirtualServers);
		this.hasVirtualServers = hasVirtualServers;
	}

	public Boolean getHasVirtualServerPolicies() {
		return hasVirtualServerPolicies;
	}

	public void setHasVirtualServerPolicies(Boolean hasVirtualServerPolicies) {
		markDirty("hasVirtualServerPolicies", this.hasVirtualServerPolicies);
		this.hasVirtualServerPolicies = hasVirtualServerPolicies;
	}

	public Boolean getHasMonitors() {
		return hasMonitors;
	}

	public void setHasMonitors(Boolean hasMonitors) {
		markDirty("hasMonitors", this.hasMonitors);
		this.hasMonitors = hasMonitors;
	}

	public Boolean getHasNodes() {
		return hasNodes;
	}

	public void setHasNodes(Boolean hasNodes) {
		markDirty("hasNodes", this.hasNodes);
		this.hasNodes = hasNodes;
	}

	public Boolean getHasNodeMonitors() {
		return hasNodeMonitors;
	}

	public void setHasNodeMonitors(Boolean hasNodeMonitors) {
		markDirty("hasNodeMonitors", this.hasNodeMonitors);
		this.hasNodeMonitors = hasNodeMonitors;
	}

	public Boolean getHasNodeWeight() {
		return hasNodeWeight;
	}

	public void setHasNodeWeight(Boolean hasNodeWeight) {
		markDirty("hasNodeWeight", this.hasNodeWeight);
		this.hasNodeWeight = hasNodeWeight;
	}

	public Boolean getHasPolicies() {
		return hasPolicies;
	}

	public void setHasPolicies(Boolean hasPolicies) {
		markDirty("hasPolicies", this.hasPolicies);
		this.hasPolicies = hasPolicies;
	}

	public Boolean getHasProfiles() {
		return hasProfiles;
	}

	public void setHasProfiles(Boolean hasProfiles) {
		markDirty("hasProfiles", this.hasProfiles);
		this.hasProfiles = hasProfiles;
	}

	public Boolean getHasRules() {
		return hasRules;
	}

	public void setHasRules(Boolean hasRules) {
		markDirty("hasRules", this.hasRules);
		this.hasRules = hasRules;
	}

	public Boolean getHasScripts() {
		return hasScripts;
	}

	public void setHasScripts(Boolean hasScripts) {
		markDirty("hasScripts", this.hasScripts);
		this.hasScripts = hasScripts;
	}

	public Boolean getHasServices() {
		return hasServices;
	}

	public void setHasServices(Boolean hasServices) {
		markDirty("hasServices", this.hasServices);
		this.hasServices = hasServices;
	}

	public Boolean getHasPools() {
		return hasPools;
	}

	public void setHasPools(Boolean hasPools) {
		markDirty("hasPools", this.hasPools);
		this.hasPools = hasPools;
	}

	public Boolean getHasPrivateVip() {
		return hasPrivateVip;
	}

	public void setHasPrivateVip(Boolean hasPrivateVip) {
		markDirty("hasPrivateVip", this.hasPrivateVip);
		this.hasPrivateVip = hasPrivateVip;
	}

	public Boolean getCreateVirtualServers() {
		return createVirtualServers;
	}

	public void setCreateVirtualServers(Boolean createVirtualServers) {
		markDirty("createVirtualServers", this.createVirtualServers);
		this.createVirtualServers = createVirtualServers;
	}

	public Boolean getCreateVirtualServerPolicies() {
		return createVirtualServerPolicies;
	}

	public void setCreateVirtualServerPolicies(Boolean createVirtualServerPolicies) {
		markDirty("createVirtualServerPolicies", this.createVirtualServerPolicies);
		this.createVirtualServerPolicies = createVirtualServerPolicies;
	}

	public Boolean getCreateMonitors() {
		return createMonitors;
	}

	public void setCreateMonitors(Boolean createMonitors) {
		markDirty("createMonitors", this.createMonitors);
		this.createMonitors = createMonitors;
	}

	public Boolean getCreateNodes() {
		return createNodes;
	}

	public void setCreateNodes(Boolean createNodes) {
		markDirty("createNodes", this.createNodes);
		this.createNodes = createNodes;
	}

	public Boolean getCreatePolicies() {
		return createPolicies;
	}

	public void setCreatePolicies(Boolean createPolicies) {
		markDirty("createProlicies", this.createPolicies);
		this.createPolicies = createPolicies;
	}

	public Boolean getCreateProfiles() {
		return createProfiles;
	}

	public void setCreateProfiles(Boolean createProfiles) {
		markDirty("createProfiles", this.createProfiles);
		this.createProfiles = createProfiles;
	}

	public Boolean getCreateRules() {
		return createRules;
	}

	public void setCreateRules(Boolean createRules) {
		markDirty("createRules", this.createRules);
		this.createRules = createRules;
	}

	public Boolean getCreateScripts() {
		return createScripts;
	}

	public void setCreateScripts(Boolean createScripts) {
		markDirty("createScripts", this.createScripts);
		this.createScripts = createScripts;
	}

	public Boolean getCreateServices() {
		return createServices;
	}

	public void setCreateServices(Boolean createServices) {
		markDirty("createServices", this.createServices);
		this.createServices = createServices;
	}

	public Boolean getCreatePools() {
		return createPools;
	}

	public void setCreatePools(Boolean createPools) {
		markDirty("createPools", this.createPools);
		this.createPools = createPools;
	}

	public Boolean getNameEditable() {
		return nameEditable;
	}

	public void setNameEditable(Boolean nameEditable) {
		markDirty("nameEditable", this.nameEditable);
		this.nameEditable = nameEditable;
	}

	public String getPoolMemberType() {
		return poolMemberType;
	}

	public void setPoolMemberType(String poolMemberType) {
		markDirty("poolMemberType", this.poolMemberType);
		this.poolMemberType = poolMemberType;
	}

	public String getNodeResourceType() {
		return nodeResourceType;
	}

	public void setNodeResourceType(String nodeResourceType) {
		markDirty("nodeResourceType", this.nodeResourceType);
		this.nodeResourceType = nodeResourceType;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		markDirty("imageCode", this.imageCode);
		this.imageCode = imageCode;
	}

	public Boolean getPoolSupportsStatus() {
		return poolSupportsStatus;
	}

	public void setPoolSupportsStatus(Boolean poolSupportsStatus) {
		markDirty("poolSupportsStatus", this.poolSupportsStatus);
		this.poolSupportsStatus = poolSupportsStatus;
	}

	public Boolean getNodeSupportsStatus() {
		return nodeSupportsStatus;
	}

	public void setNodeSupportsStatus(Boolean nodeSupportsStatus) {
		markDirty("nodeSupportsStatus", this.nodeSupportsStatus);
		this.nodeSupportsStatus = nodeSupportsStatus;
	}

	public Boolean getInstanceSupportsStatus() {
		return instanceSupportsStatus;
	}

	public void setInstanceSupportsStatus(Boolean instanceSupportsStatus) {
		markDirty("instanceSupportsStatus",this.instanceSupportsStatus);
		this.instanceSupportsStatus = instanceSupportsStatus;
	}

	public Boolean getProfileSupportsProxy() {
		return profileSupportsProxy;
	}

	public void setProfileSupportsProxy(Boolean profileSupportsProxy) {
		markDirty("profileSupportsProxy", this.profileSupportsProxy);
		this.profileSupportsProxy = profileSupportsProxy;
	}

	public Boolean getCreatePricePlans() {
		return createPricePlans;
	}

	public void setCreatePricePlans(Boolean createPricePlans) {
		markDirty("createPricePlans", this.createPricePlans);
		this.createPricePlans = createPricePlans;
	}

	public Boolean getProfileSupportsPersistence() {
		return profileSupportsPersistence;
	}

	public void setProfileSupportsPersistence(Boolean profileSupportsPersistence) {
		markDirty("profileSupportsPersistence", this.profileSupportsPersistence);
		this.profileSupportsPersistence = profileSupportsPersistence;
	}

	public Boolean getProfilesEditable() {
		return profilesEditable;
	}

	public void setProfilesEditable(Boolean profilesEditable) {
		markDirty("profilesEditable", this.profilesEditable);
		this.profilesEditable = profilesEditable;
	}

	public Collection<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(Collection<OptionType> optionTypes) {
		markDirty("optionTypes", this.optionTypes);
		this.optionTypes = optionTypes;
	}

	public Collection<OptionType> getVipOptionTypes() {
		return vipOptionTypes;
	}

	public void setVipOptionTypes(Collection<OptionType> vipOptionTypes) {
		markDirty("vipOptionTypes", this.vipOptionTypes);
		this.vipOptionTypes = vipOptionTypes;
	}

	public Collection<OptionType> getPoolOptionTypes() {
		return poolOptionTypes;
	}

	public void setPoolOptionTypes(Collection<OptionType> poolOptionTypes) {
		markDirty("poolOptionTypes", this.poolOptionTypes);
		this.poolOptionTypes = poolOptionTypes;
	}

	public Collection<OptionType> getProfileOptionTypes() {
		return profileOptionTypes;
	}

	public void setProfileOptionTypes(Collection<OptionType> profileOptionTypes) {
		markDirty("profileOptionTypes", this.profileOptionTypes);
		this.profileOptionTypes = profileOptionTypes;
	}

	public Collection<OptionType> getPolicyOptionTypes() {
		return policyOptionTypes;
	}

	public void setPolicyOptionTypes(Collection<OptionType> policyOptionTypes) {
		markDirty("policyOptionTypes", this.policyOptionTypes);
		this.policyOptionTypes = policyOptionTypes;
	}

	public Collection<OptionType> getPolicyRuleOptionTypes() {
		return policyRuleOptionTypes;
	}

	public void setPolicyRuleOptionTypes(Collection<OptionType> policyRuleOptionTypes) {
		markDirty("policyRuleOptionTypes", this.policyRuleOptionTypes);
		this.policyRuleOptionTypes = policyRuleOptionTypes;
	}

	public Collection<OptionType> getNodeOptionTypes() {
		return nodeOptionTypes;
	}

	public void setNodeOptionTypes(Collection<OptionType> nodeOptionTypes) {
		markDirty("nodeOptionTypes", this.nodeOptionTypes);
		this.nodeOptionTypes = nodeOptionTypes;
	}

	public Collection<OptionType> getMonitorOptionTypes() {
		return monitorOptionTypes;
	}

	public void setMonitorOptionTypes(Collection<OptionType> monitorOptionTypes) {
		markDirty("monitorOptionTypes", this.monitorOptionTypes);
		this.monitorOptionTypes = monitorOptionTypes;
	}

	public Collection<OptionType> getScriptOptionTypes() {
		return scriptOptionTypes;
	}

	public void setScriptOptionTypes(Collection<OptionType> scriptOptionTypes) {
		markDirty("scriptOptionTypes", this.scriptOptionTypes);
		this.scriptOptionTypes = scriptOptionTypes;
	}

	public Collection<OptionType> getInstanceOptionTypes() {
		return instanceOptionTypes;
	}

	public void setInstanceOptionTypes(Collection<OptionType> instanceOptionTypes) {
		markDirty("instanceOptionTypes", this.instanceOptionTypes);
		this.instanceOptionTypes = instanceOptionTypes;
	}

	public Collection<OptionType> getInstancePolicyOptionTypes() {
		return instancePolicyOptionTypes;
	}

	public void setInstancePolicyOptionTypes(Collection<OptionType> instancePolicyOptionTypes) {
		markDirty("instancePolicyOptionTy[es", this.instancePolicyOptionTypes);
		this.instancePolicyOptionTypes = instancePolicyOptionTypes;
	}

	public Collection<OptionType> getInstanceRuleOptionTypes() {
		return instanceRuleOptionTypes;
	}

	public void setInstanceRuleOptionTypes(Collection<OptionType> instanceRuleOptionTypes) {
		markDirty("instanceRuleOptionTypes", this.instanceRuleOptionTypes);
		this.instanceRuleOptionTypes = instanceRuleOptionTypes;
	}

	public CloudType getCloudType() {
		return cloudType;
	}

	public void setCloudType(CloudType cloudType) {
		this.cloudType = cloudType;
		markDirty("cloudType", cloudType);
	}

	public Boolean getPlugin() {
		return isPlugin;
	}

	public void setPlugin(Boolean plugin) {
		markDirty("isPlugin", this.isPlugin);
		isPlugin = plugin;
	}

	public String getPluginLogoPrefix() {
		return pluginLogoPrefix;
	}

	public void setPluginLogoPrefix(String pluginLogoPrefix) {
		markDirty("pluginLogoPrefix", this.pluginLogoPrefix);
		this.pluginLogoPrefix = pluginLogoPrefix;
	}

	public String getPluginIconPath() {
		return pluginIconPath;
	}

	public void setPluginIconPath(String pluginIconPath) {
		markDirty("pluginIconPath", this.pluginIconPath);
		this.pluginIconPath = pluginIconPath;
	}

	public String getPluginIconDarkPath() {
		return pluginIconDarkPath;
	}

	public void setPluginIconDarkPath(String pluginIconDarkPath) {
		markDirty("pluginIconDarkPath", this.pluginIconDarkPath);
		this.pluginIconDarkPath = pluginIconDarkPath;
	}

	public String getPluginIconHidpiPath() {
		return pluginIconHidpiPath;
	}

	public void setPluginIconHidpiPath(String pluginIconHidpiPath) {
		markDirty("pluginIconHidpiPath", this.pluginIconHidpiPath);
		this.pluginIconHidpiPath = pluginIconHidpiPath;
	}

	public String getPluginIconDarkHidpiPath() {
		return pluginIconDarkHidpiPath;
	}

	public void setPluginIconDarkHidpiPath(String pluginIconDarkHidpiPath) {
		markDirty("pluginIconDarkHidpiPath", this.pluginIconDarkHidpiPath);
		this.pluginIconDarkHidpiPath = pluginIconDarkHidpiPath;
	}
}
