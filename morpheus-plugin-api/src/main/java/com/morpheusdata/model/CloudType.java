package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.CloudTypeIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.List;

/**
 * A model class representing a cloud integration type (vmware, amazon, oracle, etc)
 */
public class CloudType extends CloudTypeIdentityProjection {
	protected String description;
	protected String category;
	protected String computeService;
	protected String computeUtility;
	protected String initializeQueue;
	protected String deleteQueue;
	protected Boolean enabled = true;
	protected Boolean deleted = false;
	protected Boolean provision = true;
	protected Boolean autoCapacity = false;
	protected Boolean migrationTarget = false;

	// capabilities
	protected Boolean hasDatastores = false;
	protected Boolean hasNetworks = false;
	protected Boolean hasResourcePools = false;
	protected Boolean provisionRequiresResourcePool = false;
	protected Boolean hideScopedResourcePools = false;
	protected Boolean hasHosts = true;
	protected Boolean hasVms = true;
	protected Boolean hasContainers = true;
	protected Boolean hasBareMetal = true;
	protected Boolean hasServices = true;
	protected Boolean hasFunctions = true;
	protected Boolean hasJobs = true;
	protected Boolean hasDiscovery = false;
	protected Boolean hasSecurityGroups = false; //this is kinda useless - its true everywhere and used for on box acls
	protected Boolean hasCloudInit = false;
	protected Boolean hasFolders = false;
	protected Boolean hasFloatingIps = false;
	protected Boolean hasMarketplace = false;
	protected Boolean hasNetworkAvailabilityZones = false;
	protected Boolean hasCosting = false;
	protected Boolean hasReservations = false;
	protected Boolean mergeNetworks = false;
	protected Boolean canCreateDatastores = false;
	protected Boolean canCreateNetworks = false;
	protected Boolean canChooseContainerMode = false;
	protected Boolean canCreateResourcePools = false;
	protected Boolean canDeleteResourcePools = false;
	protected Boolean canSyncHypervisorStats = false;
	protected Boolean canDeleteVirtualImages = false;
	protected Boolean hasNativePlans = false;
	protected Boolean supportsDistributedWorker = false;
	protected Boolean supportsAgentlessMetrics = false;
	protected Boolean supportsOrphanedGuidance = false;
	//display
	protected String detailView;
	protected String configView;
	protected Integer displayOrder = 0;
	protected String cloud = "public";
	//flag for allowed provision types;
	protected List provisionTypes;
	protected Long zoneInstanceTypeLayoutId;
	protected Boolean hasNativeSecurityGroups = false;
	protected String containerMode = "docker"; //off, kubernetes, docker
	protected String defaultMarketplaceInstaceTypeCode;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSecurityServerType defaultSecurityServerType;

	// plugin stuff
	protected Boolean isPlugin;
	protected Boolean isEmbedded;
	protected String pluginLogoPrefix;
	protected String pluginIconPath;
	protected String pluginIconDarkPath;
	protected String pluginIconHidpiPath;
	protected String pluginIconDarkHidpiPath;
	protected String pluginCircularIconPath;
	protected String pluginCircularIconDarkPath;
	protected String pluginCircularIconHidpiPath;
	protected String pluginCircularIconDarkHidpiPath;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getComputeService() {
		return computeService;
	}

	public void setComputeService(String computeService) {
		this.computeService = computeService;
		markDirty("computeService", computeService);
	}

	public String getComputeUtility() {
		return computeUtility;
	}

	public void setComputeUtility(String computeUtility) {
		this.computeUtility = computeUtility;
		markDirty("computeUtility", computeUtility);
	}

	public String getInitializeQueue() {
		return initializeQueue;
	}

	public void setInitializeQueue(String initializeQueue) {
		this.initializeQueue = initializeQueue;
		markDirty("initializeQueue", initializeQueue);
	}

	public String getDeleteQueue() {
		return deleteQueue;
	}

	public void setDeleteQueue(String deleteQueue) {
		this.deleteQueue = deleteQueue;
		markDirty("deleteQueue", deleteQueue);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
		markDirty("deleted", deleted);
	}

	public Boolean getProvision() {
		return provision;
	}

	public void setProvision(Boolean provision) {
		this.provision = provision;
		markDirty("provision", provision);
	}

	public Boolean getAutoCapacity() {
		return autoCapacity;
	}

	public void setAutoCapacity(Boolean autoCapacity) {
		this.autoCapacity = autoCapacity;
		markDirty("autoCapacity", autoCapacity);
	}

	public Boolean getMigrationTarget() {
		return migrationTarget;
	}

	public void setMigrationTarget(Boolean migrationTarget) {
		this.migrationTarget = migrationTarget;
		markDirty("migrationTarget", migrationTarget);
	}

	public Boolean getHasDatastores() {
		return hasDatastores;
	}

	public void setHasDatastores(Boolean hasDatastores) {
		this.hasDatastores = hasDatastores;
		markDirty("hasDatastores", hasDatastores);
	}

	public Boolean getHasNetworks() {
		return hasNetworks;
	}

	public void setHasNetworks(Boolean hasNetworks) {
		this.hasNetworks = hasNetworks;
		markDirty("hasNetworks", hasNetworks);
	}

	public Boolean getHasResourcePools() {
		return hasResourcePools;
	}

	public void setHasResourcePools(Boolean hasResourcePools) {
		this.hasResourcePools = hasResourcePools;
		markDirty("hasResourcePools", hasResourcePools);
	}

	public Boolean getProvisionRequiresResourcePool() {
		return provisionRequiresResourcePool;
	}

	public void setProvisionRequiresResourcePool(Boolean provisionRequiresResourcePool) {
		this.provisionRequiresResourcePool = provisionRequiresResourcePool;
		markDirty("provisionRequiresResourcePool", provisionRequiresResourcePool);
	}

	public Boolean getHideScopedResourcePools() {
		return hideScopedResourcePools;
	}

	public void setHideScopedResourcePools(Boolean hideScopedResourcePools) {
		this.hideScopedResourcePools = hideScopedResourcePools;
		markDirty("hideScopedResourcePools", hideScopedResourcePools);
	}

	public Boolean getHasHosts() {
		return hasHosts;
	}

	public void setHasHosts(Boolean hasHosts) {
		this.hasHosts = hasHosts;
		markDirty("hasHosts", hasHosts);
	}

	public Boolean getHasVms() {
		return hasVms;
	}

	public void setHasVms(Boolean hasVms) {
		this.hasVms = hasVms;
		markDirty("hasVms", hasVms);
	}

	public Boolean getHasContainers() {
		return hasContainers;
	}

	public void setHasContainers(Boolean hasContainers) {
		this.hasContainers = hasContainers;
		markDirty("hasContainers", hasContainers);
	}

	public Boolean getHasBareMetal() {
		return hasBareMetal;
	}

	public void setHasBareMetal(Boolean hasBareMetal) {
		this.hasBareMetal = hasBareMetal;
		markDirty("hasBareMetal", hasBareMetal);
	}

	public Boolean getHasServices() {
		return hasServices;
	}

	public void setHasServices(Boolean hasServices) {
		this.hasServices = hasServices;
		markDirty("hasServices", hasServices);
	}

	public Boolean getHasFunctions() {
		return hasFunctions;
	}

	public void setHasFunctions(Boolean hasFunctions) {
		this.hasFunctions = hasFunctions;
		markDirty("hasFunctions", hasFunctions);
	}

	public Boolean getHasJobs() {
		return hasJobs;
	}

	public void setHasJobs(Boolean hasJobs) {
		this.hasJobs = hasJobs;
		markDirty("hasJobs", hasJobs);
	}

	public Boolean getHasDiscovery() {
		return hasDiscovery;
	}

	public void setHasDiscovery(Boolean hasDiscovery) {
		this.hasDiscovery = hasDiscovery;
		markDirty("hasDiscovery", hasDiscovery);
	}

	public Boolean getHasSecurityGroups() {
		return hasSecurityGroups;
	}

	public void setHasSecurityGroups(Boolean hasSecurityGroups) {
		this.hasSecurityGroups = hasSecurityGroups;
		markDirty("hasSecurityGroups", hasSecurityGroups);
	}

	public Boolean getHasCloudInit() {
		return hasCloudInit;
	}

	public void setHasCloudInit(Boolean hasCloudInit) {
		this.hasCloudInit = hasCloudInit;
		markDirty("hasCloudInit", hasCloudInit);
	}

	public Boolean getHasFolders() {
		return hasFolders;
	}

	public void setHasFolders(Boolean hasFolders) {
		this.hasFolders = hasFolders;
		markDirty("hasFolders", hasFolders);
	}

	public Boolean getHasFloatingIps() {
		return hasFloatingIps;
	}

	public void setHasFloatingIps(Boolean hasFloatingIps) {
		this.hasFloatingIps = hasFloatingIps;
		markDirty("hasFloatingIps", hasFloatingIps);
	}

	public Boolean getHasMarketplace() {
		return hasMarketplace;
	}

	public void setHasMarketplace(Boolean hasMarketplace) {
		this.hasMarketplace = hasMarketplace;
		markDirty("hasMarketplace", hasMarketplace);
	}

	public Boolean getHasNetworkAvailabilityZones() {
		return hasNetworkAvailabilityZones;
	}

	public void setHasNetworkAvailabilityZones(Boolean hasNetworkAvailabilityZones) {
		this.hasNetworkAvailabilityZones = hasNetworkAvailabilityZones;
		markDirty("hasNetworkAvailabilityZones", hasNetworkAvailabilityZones);
	}

	public Boolean getHasCosting() {
		return hasCosting;
	}

	public void setHasCosting(Boolean hasCosting) {
		this.hasCosting = hasCosting;
		markDirty("hasCosting", hasCosting);
	}

	public Boolean getHasReservations() {
		return hasReservations;
	}

	public void setHasReservations(Boolean hasReservations) {
		this.hasReservations = hasReservations;
		markDirty("hasReservations", hasReservations);
	}

	public Boolean getMergeNetworks() {
		return mergeNetworks;
	}

	public void setMergeNetworks(Boolean mergeNetworks) {
		this.mergeNetworks = mergeNetworks;
		markDirty("mergeNetworks", mergeNetworks);
	}

	public Boolean getCanCreateDatastores() {
		return canCreateDatastores;
	}

	public void setCanCreateDatastores(Boolean canCreateDatastores) {
		this.canCreateDatastores = canCreateDatastores;
		markDirty("canCreateDatastores", canCreateDatastores);
	}

	public Boolean getCanCreateNetworks() {
		return canCreateNetworks;
	}

	public void setCanCreateNetworks(Boolean canCreateNetworks) {
		this.canCreateNetworks = canCreateNetworks;
		markDirty("canCreateNetworks", canCreateNetworks);
	}

	public Boolean getCanChooseContainerMode() {
		return canChooseContainerMode;
	}

	public void setCanChooseContainerMode(Boolean canChooseContainerMode) {
		this.canChooseContainerMode = canChooseContainerMode;
		markDirty("canChooseContainerMode", canChooseContainerMode);
	}

	public Boolean getCanCreateResourcePools() {
		return canCreateResourcePools;
	}

	public void setCanCreateResourcePools(Boolean canCreateResourcePools) {
		this.canCreateResourcePools = canCreateResourcePools;
		markDirty("canCreateResourcePools", canCreateResourcePools);
	}

	public Boolean getCanDeleteResourcePools() {
		return canDeleteResourcePools;
	}

	public void setCanDeleteResourcePools(Boolean canDeleteResourcePools) {
		this.canDeleteResourcePools = canDeleteResourcePools;
		markDirty("canDeleteResourcePools", canDeleteResourcePools);
	}

	public Boolean getCanSyncHypervisorStats() {
		return canSyncHypervisorStats;
	}

	public void setCanSyncHypervisorStats(Boolean canSyncHypervisorStats) {
		this.canSyncHypervisorStats = canSyncHypervisorStats;
		markDirty("canSyncHypervisorStats", canSyncHypervisorStats);
	}

	public Boolean getCanDeleteVirtualImages() {
		return canDeleteVirtualImages;
	}

	public void setCanDeleteVirtualImages(Boolean canDeleteVirtualImages) {
		this.canDeleteVirtualImages = canDeleteVirtualImages;
		markDirty("canDeleteVirtualImages", canDeleteVirtualImages);
	}

	public Boolean getHasNativePlans() {
		return hasNativePlans;
	}

	public void setHasNativePlans(Boolean hasNativePlans) {
		this.hasNativePlans = hasNativePlans;
		markDirty("hasNetivePlans", hasNativePlans);
	}

	public Boolean getSupportsDistributedWorker() {
		return supportsDistributedWorker;
	}

	public void setSupportsDistributedWorker(Boolean supportsDistributedWorker) {
		this.supportsDistributedWorker = supportsDistributedWorker;
		markDirty("supportsDistributedWorker", supportsDistributedWorker);
	}

	public Boolean getSupportsAgentlessMetrics() {
		return supportsAgentlessMetrics;
	}

	public void setSupportsAgentlessMetrics(Boolean supportsAgentlessMetrics) {
		this.supportsAgentlessMetrics = supportsAgentlessMetrics;
		markDirty("supportsAgentlessMetrics", supportsAgentlessMetrics);
	}

	public Boolean getSupportsOrphanedGuidance() {
		return supportsOrphanedGuidance;
	}

	public void setSupportsOrphanedGuidance(Boolean supportsOrphanedGuidance) {
		this.supportsOrphanedGuidance = supportsOrphanedGuidance;
		markDirty("supportsOrphanedGuidance", supportsOrphanedGuidance);
	}

	public String getDetailView() {
		return detailView;
	}

	public void setDetailView(String detailView) {
		this.detailView = detailView;
		markDirty("detailView", detailView);
	}

	public String getConfigView() {
		return configView;
	}

	public void setConfigView(String configView) {
		this.configView = configView;
		markDirty("configView", configView);
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	public String getCloud() {
		return cloud;
	}

	public void setCloud(String cloud) {
		this.cloud = cloud;
		markDirty("cloud", cloud);
	}

	public List getProvisionTypes() {
		return provisionTypes;
	}

	public void setProvisionTypes(List provisionTypes) {
		this.provisionTypes = provisionTypes;
		markDirty("provisionTypes", provisionTypes);
	}

	public Long getZoneInstanceTypeLayoutId() {
		return zoneInstanceTypeLayoutId;
	}

	public void setZoneInstanceTypeLayoutId(Long zoneInstanceTypeLayoutId) {
		this.zoneInstanceTypeLayoutId = zoneInstanceTypeLayoutId;
		markDirty("zoneInstanceTypeLayoutId", zoneInstanceTypeLayoutId);
	}

	public Boolean getHasNativeSecurityGroups() {
		return hasNativeSecurityGroups;
	}

	public void setHasNativeSecurityGroups(Boolean hasNativeSecurityGroups) {
		this.hasNativeSecurityGroups = hasNativeSecurityGroups;
		markDirty("hasNativeSecurityGroups", hasNativeSecurityGroups);
	}

	public String getContainerMode() {
		return containerMode;
	}

	public void setContainerMode(String containerMode) {
		this.containerMode = containerMode;
		markDirty("containerMode", containerMode);
	}

	public String getDefaultMarketplaceInstaceTypeCode() {
		return defaultMarketplaceInstaceTypeCode;
	}

	public void setDefaultMarketplaceInstaceTypeCode(String defaultMarketplaceInstaceTypeCode) {
		this.defaultMarketplaceInstaceTypeCode = defaultMarketplaceInstaceTypeCode;
		markDirty("defaultMarketplaceInstanceTypeCode", defaultMarketplaceInstaceTypeCode);
	}

	public NetworkSecurityServerType getDefaultSecurityServerType() {
		return defaultSecurityServerType;
	}

	public void setDefaultSecurityServerType(NetworkSecurityServerType defaultSecurityServerType) {
		this.defaultSecurityServerType = defaultSecurityServerType;
		markDirty("defaultSecurityServerType", defaultSecurityServerType);
	}

	public Boolean getPlugin() {
		return isPlugin;
	}

	public void setPlugin(Boolean plugin) {
		isPlugin = plugin;
		markDirty("isPlugin", plugin);
	}

	public Boolean getEmbedded() {
		return isEmbedded;
	}

	public void setEmbedded(Boolean embedded) {
		this.isEmbedded = embedded;
		markDirty("isEmbedded", embedded);
	}

	public String getPluginLogoPrefix() {
		return pluginLogoPrefix;
	}

	public void setPluginLogoPrefix(String pluginLogoPrefix) {
		this.pluginLogoPrefix = pluginLogoPrefix;
		markDirty("pluginLogoPrefix", pluginLogoPrefix);
	}

	public String getPluginIconPath() {
		return pluginIconPath;
	}

	public void setPluginIconPath(String pluginIconPath) {
		this.pluginIconPath = pluginIconPath;
		markDirty("pluginIconPath", pluginIconPath);
	}

	public String getPluginIconDarkPath() {
		return pluginIconDarkPath;
	}

	public void setPluginIconDarkPath(String pluginIconDarkPath) {
		this.pluginIconDarkPath = pluginIconDarkPath;
		markDirty("pluginIconDarkPath", pluginIconDarkPath);
	}

	public String getPluginIconHidpiPath() {
		return pluginIconHidpiPath;
	}

	public void setPluginIconHidpiPath(String pluginIconHidpiPath) {
		this.pluginIconHidpiPath = pluginIconHidpiPath;
		markDirty("pluginIconHidpiPath", pluginIconHidpiPath);
	}

	public String getPluginIconDarkHidpiPath() {
		return pluginIconDarkHidpiPath;
	}

	public void setPluginIconDarkHidpiPath(String pluginIconDarkHidpiPath) {
		this.pluginIconDarkHidpiPath = pluginIconDarkHidpiPath;
		markDirty("pluginIconDarkHidpiPath", pluginIconDarkHidpiPath);
	}

	public String getPluginCircularIconPath() {
		return pluginCircularIconPath;
	}

	public void setPluginCircularIconPath(String pluginCircularIconPath) {
		this.pluginCircularIconPath = pluginCircularIconPath;
		markDirty("pluginCircularIconPath", pluginCircularIconPath);
	}

	public String getPluginCircularIconDarkPath() {
		return pluginCircularIconDarkPath;
	}

	public void setPluginCircularIconDarkPath(String pluginCircularIconDarkPath) {
		this.pluginCircularIconDarkPath = pluginCircularIconDarkPath;
		markDirty("pluginCircularIconDarkPath", pluginCircularIconDarkPath);
	}

	public String getPluginCircularIconHidpiPath() {
		return pluginCircularIconHidpiPath;
	}

	public void setPluginCircularIconHidpiPath(String pluginCircularIconHidpiPath) {
		this.pluginCircularIconHidpiPath = pluginCircularIconHidpiPath;
		markDirty("pluginCicularIconHidpiPath", pluginCircularIconHidpiPath);
	}

	public String getPluginCircularIconDarkHidpiPath() {
		return pluginCircularIconDarkHidpiPath;
	}

	public void setPluginCircularIconDarkHidpiPath(String pluginCircularIconDarkHidpiPath) {
		this.pluginCircularIconDarkHidpiPath = pluginCircularIconDarkHidpiPath;
		markDirty("pluginCircularIconDarkHidpiPath", pluginCircularIconDarkHidpiPath);
	}
}
