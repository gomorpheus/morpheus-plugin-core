package com.morpheusdata.model;

public class ProvisionType extends MorpheusModel {

	protected String code;
	protected String name;
	protected String description;
	protected String hostType; //server, container, vm;
	protected String serverType; //'morpheus-node' // 'ami', 'vm', 'unmanaged';
	protected String nodeFormat;
	protected String provisionService;
	protected Boolean pickServer; //do we choose a server to provision too;
	protected Boolean createServer; //do we create a compute server for each instance;
	protected Boolean aclEnabled; //do we control the firewall;
	protected Boolean multiTenant; //more than one instance on a box;
	protected Boolean hostNetwork; //is the network on host or an overlay;
	protected Boolean managed = false; //is morpheus provisioning;
	protected Boolean customSupported = false; //byoc allowed;
	protected Boolean mapPorts = false;
	protected Boolean exportServer = false; //for allowing a vm dump out of a hypervisor;
	protected Boolean cloneTemplate = false; //if cloud allows cloning a vm to a template;
	protected Boolean cloneable = true; //if cloud allows cloning a vm to a template;
	protected Boolean hasVolumes = true; // whether volumes should be shown;
	protected Boolean addVolumes = false; // whether multiple volumes are supported;
	protected Boolean hasDatastore = false;
	protected Boolean supportsAutoDatastore = false;
	protected Boolean hasStorageControllers = false;
	protected Boolean hasNetworks = false;
	protected Boolean hasConfigurableCpuSockets = false;
	protected Boolean hasZonePools = false;
	protected Boolean zonePoolRequired = false;
	protected Boolean hasSnapshots = false;
	protected Boolean networksScopedToPools = false;
	protected Integer maxNetworks = 0;
	protected Boolean customizeVolume = false; // whether additional volumes are customizable;
	protected Boolean rootDiskCustomizable = false; // whether the root disk is customizable;
	protected Boolean rootDiskSizeKnown = true;
	protected Boolean rootDiskResizable = false; // whether the root disk is customizable;
	protected Boolean lvmSupported = true;
	protected Boolean resizeCopiesVolumes = false;
	protected Boolean reconfigureSupported = true;
	protected Boolean volumesPreservable = false;
	protected Boolean supportsReplicaSets = false;
	protected Boolean hasSecurityGroups = false;
	protected Boolean hasParameters = false;
	protected Boolean hasContainers = true;
	protected Boolean hasResources = false; //if its services and not vms or containers;
	protected Boolean canEnforceTags = false;
	protected Boolean hasPlanTagMatch = false;
	protected String hostDiskMode;
	protected Integer minDisk = 1;
	protected Integer maxDisk;
	protected String backupType;
	protected Boolean disableRootDatastore = false;

	protected String viewSet;
	protected String containerService;
	protected String deployTargetService;
	protected Boolean supportsCustomServicePlans = false;
	protected Boolean supportsConfigManagement = true;
	protected Boolean hasSecurityGroupsOnNetworks = false;
	protected Boolean supportsNetworkSelection = true;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getHostType() {
		return hostType;
	}

	public String getServerType() {
		return serverType;
	}

	public String getNodeFormat() {
		return nodeFormat;
	}

	public String getProvisionService() {
		return provisionService;
	}

	public Boolean getPickServer() {
		return pickServer;
	}

	public Boolean getCreateServer() {
		return createServer;
	}

	public Boolean getAclEnabled() {
		return aclEnabled;
	}

	public Boolean getMultiTenant() {
		return multiTenant;
	}

	public Boolean getHostNetwork() {
		return hostNetwork;
	}

	public Boolean getManaged() {
		return managed;
	}

	public Boolean getCustomSupported() {
		return customSupported;
	}

	public Boolean getMapPorts() {
		return mapPorts;
	}

	public Boolean getExportServer() {
		return exportServer;
	}

	public Boolean getCloneTemplate() {
		return cloneTemplate;
	}

	public Boolean getCloneable() {
		return cloneable;
	}

	public Boolean getHasVolumes() {
		return hasVolumes;
	}

	public Boolean getAddVolumes() {
		return addVolumes;
	}

	public Boolean getHasDatastore() {
		return hasDatastore;
	}

	public Boolean getSupportsAutoDatastore() {
		return supportsAutoDatastore;
	}

	public Boolean getHasStorageControllers() {
		return hasStorageControllers;
	}

	public Boolean getHasNetworks() {
		return hasNetworks;
	}

	public Boolean getHasConfigurableCpuSockets() {
		return hasConfigurableCpuSockets;
	}

	public Boolean getHasZonePools() {
		return hasZonePools;
	}

	public Boolean getZonePoolRequired() {
		return zonePoolRequired;
	}

	public Boolean getHasSnapshots() {
		return hasSnapshots;
	}

	public Boolean getNetworksScopedToPools() {
		return networksScopedToPools;
	}

	public Integer getMaxNetworks() {
		return maxNetworks;
	}

	public Boolean getCustomizeVolume() {
		return customizeVolume;
	}

	public Boolean getRootDiskCustomizable() {
		return rootDiskCustomizable;
	}

	public Boolean getRootDiskSizeKnown() {
		return rootDiskSizeKnown;
	}

	public Boolean getRootDiskResizable() {
		return rootDiskResizable;
	}

	public Boolean getLvmSupported() {
		return lvmSupported;
	}

	public Boolean getResizeCopiesVolumes() {
		return resizeCopiesVolumes;
	}

	public Boolean getReconfigureSupported() {
		return reconfigureSupported;
	}

	public Boolean getVolumesPreservable() {
		return volumesPreservable;
	}

	public Boolean getSupportsReplicaSets() {
		return supportsReplicaSets;
	}

	public Boolean getHasSecurityGroups() {
		return hasSecurityGroups;
	}

	public Boolean getHasParameters() {
		return hasParameters;
	}

	public Boolean getHasContainers() {
		return hasContainers;
	}

	public Boolean getHasResources() {
		return hasResources;
	}

	public Boolean getCanEnforceTags() {
		return canEnforceTags;
	}

	public Boolean getHasPlanTagMatch() {
		return hasPlanTagMatch;
	}

	public String getHostDiskMode() {
		return hostDiskMode;
	}

	public Integer getMinDisk() {
		return minDisk;
	}

	public Integer getMaxDisk() {
		return maxDisk;
	}

	public String getBackupType() {
		return backupType;
	}

	public Boolean getDisableRootDatastore() {
		return disableRootDatastore;
	}

	public String getViewSet() {
		return viewSet;
	}

	public String getContainerService() {
		return containerService;
	}

	public String getDeployTargetService() {
		return deployTargetService;
	}

	public Boolean getSupportsCustomServicePlans() {
		return supportsCustomServicePlans;
	}

	public Boolean getSupportsConfigManagement() {
		return supportsConfigManagement;
	}

	public Boolean getHasSecurityGroupsOnNetworks() {
		return hasSecurityGroupsOnNetworks;
	}

	public Boolean getSupportsNetworkSelection() {
		return supportsNetworkSelection;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
		markDirty("hostType", hostType);
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
		markDirty("serverType", serverType);
	}

	public void setNodeFormat(String nodeFormat) {
		this.nodeFormat = nodeFormat;
		markDirty("nodeFormat", nodeFormat);
	}

	public void setProvisionService(String provisionService) {
		this.provisionService = provisionService;
		markDirty("provisionService", provisionService);
	}

	public void setPickServer(Boolean pickServer) {
		this.pickServer = pickServer;
		markDirty("pickServer", pickServer);
	}

	public void setCreateServer(Boolean createServer) {
		this.createServer = createServer;
		markDirty("createServer", createServer);
	}

	public void setAclEnabled(Boolean aclEnabled) {
		this.aclEnabled = aclEnabled;
		markDirty("aclEnabled", aclEnabled);
	}

	public void setMultiTenant(Boolean multiTenant) {
		this.multiTenant = multiTenant;
		markDirty("multiTenant", multiTenant);
	}

	public void setHostNetwork(Boolean hostNetwork) {
		this.hostNetwork = hostNetwork;
		markDirty("hostNetwork", hostNetwork);
	}

	public void setManaged(Boolean managed) {
		this.managed = managed;
		markDirty("managed", managed);
	}

	public void setCustomSupported(Boolean customSupported) {
		this.customSupported = customSupported;
		markDirty("customSupported", customSupported);
	}

	public void setMapPorts(Boolean mapPorts) {
		this.mapPorts = mapPorts;
		markDirty("mapPorts", mapPorts);
	}

	public void setExportServer(Boolean exportServer) {
		this.exportServer = exportServer;
		markDirty("exportServer", exportServer);
	}

	public void setCloneTemplate(Boolean cloneTemplate) {
		this.cloneTemplate = cloneTemplate;
		markDirty("cloneTemplate", cloneTemplate);
	}

	public void setCloneable(Boolean cloneable) {
		this.cloneable = cloneable;
		markDirty("cloneable", cloneable);
	}

	public void setHasVolumes(Boolean hasVolumes) {
		this.hasVolumes = hasVolumes;
		markDirty("hasVolumes", hasVolumes);
	}

	public void setAddVolumes(Boolean addVolumes) {
		this.addVolumes = addVolumes;
		markDirty("addVolumes", addVolumes);
	}

	public void setHasDatastore(Boolean hasDatastore) {
		this.hasDatastore = hasDatastore;
		markDirty("hasDatastore", hasDatastore);
	}

	public void setSupportsAutoDatastore(Boolean supportsAutoDatastore) {
		this.supportsAutoDatastore = supportsAutoDatastore;
		markDirty("supportsAutoDatastore", supportsAutoDatastore);
	}

	public void setHasStorageControllers(Boolean hasStorageControllers) {
		this.hasStorageControllers = hasStorageControllers;
		markDirty("hasStorageControllers", hasStorageControllers);
	}

	public void setHasNetworks(Boolean hasNetworks) {
		this.hasNetworks = hasNetworks;
		markDirty("hasNetworks", hasNetworks);
	}

	public void setHasConfigurableCpuSockets(Boolean hasConfigurableCpuSockets) {
		this.hasConfigurableCpuSockets = hasConfigurableCpuSockets;
		markDirty("hasConfigurableCpuSockets", hasConfigurableCpuSockets);
	}

	public void setHasZonePools(Boolean hasZonePools) {
		this.hasZonePools = hasZonePools;
		markDirty("hasZonePools", hasZonePools);
	}

	public void setZonePoolRequired(Boolean zonePoolRequired) {
		this.zonePoolRequired = zonePoolRequired;
		markDirty("zonePoolRequired", zonePoolRequired);
	}

	public void setHasSnapshots(Boolean hasSnapshots) {
		this.hasSnapshots = hasSnapshots;
		markDirty("hasSnapshots", hasSnapshots);
	}

	public void setNetworksScopedToPools(Boolean networksScopedToPools) {
		this.networksScopedToPools = networksScopedToPools;
		markDirty("networksScopedToPools", networksScopedToPools);
	}

	public void setMaxNetworks(Integer maxNetworks) {
		this.maxNetworks = maxNetworks;
		markDirty("maxNetworks", maxNetworks);
	}

	public void setCustomizeVolume(Boolean customizeVolume) {
		this.customizeVolume = customizeVolume;
		markDirty("customizeVolume", customizeVolume);
	}

	public void setRootDiskCustomizable(Boolean rootDiskCustomizable) {
		this.rootDiskCustomizable = rootDiskCustomizable;
		markDirty("rootDiskCustomizable", rootDiskCustomizable);
	}

	public void setRootDiskSizeKnown(Boolean rootDiskSizeKnown) {
		this.rootDiskSizeKnown = rootDiskSizeKnown;
		markDirty("rootDiskSizeKnown", rootDiskSizeKnown);
	}

	public void setRootDiskResizable(Boolean rootDiskResizable) {
		this.rootDiskResizable = rootDiskResizable;
		markDirty("rootDiskResizable", rootDiskResizable);
	}

	public void setLvmSupported(Boolean lvmSupported) {
		this.lvmSupported = lvmSupported;
		markDirty("lvmSupported", lvmSupported);
	}

	public void setResizeCopiesVolumes(Boolean resizeCopiesVolumes) {
		this.resizeCopiesVolumes = resizeCopiesVolumes;
		markDirty("resizeCopiesVolumes", resizeCopiesVolumes);
	}

	public void setReconfigureSupported(Boolean reconfigureSupported) {
		this.reconfigureSupported = reconfigureSupported;
		markDirty("reconfigureSupported", reconfigureSupported);
	}

	public void setVolumesPreservable(Boolean volumesPreservable) {
		this.volumesPreservable = volumesPreservable;
		markDirty("volumesPreservable", volumesPreservable);
	}

	public void setSupportsReplicaSets(Boolean supportsReplicaSets) {
		this.supportsReplicaSets = supportsReplicaSets;
		markDirty("supportsReplicaSets", supportsReplicaSets);
	}

	public void setHasSecurityGroups(Boolean hasSecurityGroups) {
		this.hasSecurityGroups = hasSecurityGroups;
		markDirty("hasSecurityGroups", hasSecurityGroups);
	}

	public void setHasParameters(Boolean hasParameters) {
		this.hasParameters = hasParameters;
		markDirty("hasParameters", hasParameters);
	}

	public void setHasContainers(Boolean hasContainers) {
		this.hasContainers = hasContainers;
		markDirty("hasContainers", hasContainers);
	}

	public void setHasResources(Boolean hasResources) {
		this.hasResources = hasResources;
		markDirty("hasResources", hasResources);
	}

	public void setCanEnforceTags(Boolean canEnforceTags) {
		this.canEnforceTags = canEnforceTags;
		markDirty("canEnforceTags", canEnforceTags);
	}

	public void setHasPlanTagMatch(Boolean hasPlanTagMatch) {
		this.hasPlanTagMatch = hasPlanTagMatch;
		markDirty("hasPlanTagMatch", hasPlanTagMatch);
	}

	public void setHostDiskMode(String hostDiskMode) {
		this.hostDiskMode = hostDiskMode;
		markDirty("hostDiskMode", hostDiskMode);
	}

	public void setMinDisk(Integer minDisk) {
		this.minDisk = minDisk;
		markDirty("minDisk", minDisk);
	}

	public void setMaxDisk(Integer maxDisk) {
		this.maxDisk = maxDisk;
		markDirty("maxDisk", maxDisk);
	}

	public void setBackupType(String backupType) {
		this.backupType = backupType;
		markDirty("backupType", backupType);
	}

	public void setDisableRootDatastore(Boolean disableRootDatastore) {
		this.disableRootDatastore = disableRootDatastore;
		markDirty("disableRootDatastore", disableRootDatastore);
	}

	public void setViewSet(String viewSet) {
		this.viewSet = viewSet;
		markDirty("viewSet", viewSet);
	}

	public void setContainerService(String containerService) {
		this.containerService = containerService;
		markDirty("containerService", containerService);
	}

	public void setDeployTargetService(String deployTargetService) {
		this.deployTargetService = deployTargetService;
		markDirty("deployTargetService", deployTargetService);
	}

	public void setSupportsCustomServicePlans(Boolean supportsCustomServicePlans) {
		this.supportsCustomServicePlans = supportsCustomServicePlans;
		markDirty("supportsCustomServicePlans", supportsCustomServicePlans);
	}

	public void setSupportsConfigManagement(Boolean supportsConfigManagement) {
		this.supportsConfigManagement = supportsConfigManagement;
		markDirty("supportsConfigManagement", supportsConfigManagement);
	}

	public void setHasSecurityGroupsOnNetworks(Boolean hasSecurityGroupsOnNetworks) {
		this.hasSecurityGroupsOnNetworks = hasSecurityGroupsOnNetworks;
		markDirty("hasSecurityGroupsOnNetworks", hasSecurityGroupsOnNetworks);
	}

	public void setSupportsNetworkSelection(Boolean supportsNetworkSelection) {
		this.supportsNetworkSelection = supportsNetworkSelection;
		markDirty("supportsNetworkSelection", supportsNetworkSelection);
	}

}
