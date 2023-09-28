package com.morpheusdata.model;

public class ComputeServerGroupType extends MorpheusModel {
	
	protected String name;
	protected String code;
	protected String shortName;
	protected String imageCode;
	protected String category;
	protected String description;
	protected String viewSet;
	protected String internalId;
	protected String externalId;
	protected String hostService;
	protected String providerType; //kubernetes, docker, kvm, etc
	protected String deployTargetService;
	protected Boolean managed = false;
	protected Boolean canManage = true;
	protected Boolean hasCluster = true;
	protected Boolean hasMasters = false;
	protected Boolean hasWorkers = false;
	protected Boolean hasDatastore = false;
	protected Boolean hasDefaultDataDisk = true;
	protected Boolean kubeCtlLocal = false;
	protected Boolean supportsCloudScaling = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getViewSet() {
		return viewSet;
	}

	public void setViewSet(String viewSet) {
		this.viewSet = viewSet;
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

	public String getHostService() {
		return hostService;
	}

	public void setHostService(String hostService) {
		this.hostService = hostService;
	}

	public String getProviderType() {
		return providerType;
	}

	public void setProviderType(String providerType) {
		this.providerType = providerType;
	}

	public String getDeployTargetService() {
		return deployTargetService;
	}

	public void setDeployTargetService(String deployTargetService) {
		this.deployTargetService = deployTargetService;
	}

	public Boolean getManaged() {
		return managed;
	}

	public void setManaged(Boolean managed) {
		this.managed = managed;
	}

	public Boolean getCanManage() {
		return canManage;
	}

	public void setCanManage(Boolean canManage) {
		this.canManage = canManage;
	}

	public Boolean getHasCluster() {
		return hasCluster;
	}

	public void setHasCluster(Boolean hasCluster) {
		this.hasCluster = hasCluster;
	}

	public Boolean getHasMasters() {
		return hasMasters;
	}

	public void setHasMasters(Boolean hasMasters) {
		this.hasMasters = hasMasters;
	}

	public Boolean getHasWorkers() {
		return hasWorkers;
	}

	public void setHasWorkers(Boolean hasWorkers) {
		this.hasWorkers = hasWorkers;
	}

	public Boolean getHasDatastore() {
		return hasDatastore;
	}

	public void setHasDatastore(Boolean hasDatastore) {
		this.hasDatastore = hasDatastore;
	}

	public Boolean getHasDefaultDataDisk() {
		return hasDefaultDataDisk;
	}

	public void setHasDefaultDataDisk(Boolean hasDefaultDataDisk) {
		this.hasDefaultDataDisk = hasDefaultDataDisk;
	}

	public Boolean getKubeCtlLocal() {
		return kubeCtlLocal;
	}

	public void setKubeCtlLocal(Boolean kubeCtlLocal) {
		this.kubeCtlLocal = kubeCtlLocal;
	}

	public Boolean getSupportsCloudScaling() {
		return supportsCloudScaling;
	}

	public void setSupportsCloudScaling(Boolean supportsCloudScaling) {
		this.supportsCloudScaling = supportsCloudScaling;
	}
}
