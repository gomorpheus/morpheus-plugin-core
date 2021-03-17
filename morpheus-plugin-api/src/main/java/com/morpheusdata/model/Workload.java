package com.morpheusdata.model;

import java.util.Date;
import java.util.List;

/**
 * Represents a workload running in morpheus. This is also known internally to morpheus as a Container object but due
 * to the expansion of where this model is used (the context), it has been renamed in the public api as a Workload
 *
 * @see ComputeServer
 *
 * @author David Estes
 */
public class Workload extends MorpheusModel {
	protected String uuid;
	protected ComputeServer server;
	protected String name;
	protected ServicePlan plan;
	protected Boolean privateNetworking;
	protected String userData;

	protected Account account;
	protected Instance instance;
	protected String internalName;
	protected String displayName;
	protected String category;
	protected String apiKey;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean statsEnabled = true;
	protected Workload.Status status = Workload.Status.pending;
	protected String statusMessage;
	protected String errorMessage;
	protected String environmentPrefix;
	protected String lastStats;
	protected String configs;
	protected String rawData;
	protected String runtimeInfo;
	protected String containerVersion; //copied in on create so it never changes;
	protected String repositoryImage; //copied in on create so it never changes;
	protected String planCategory;
	protected String hostname;
	protected String domainName;
	protected Boolean volumeCreated = false;
	protected Boolean containerCreated = false;
	protected Long maxStorage;
	protected Long maxMemory;
	protected Long maxCores;
	protected Long coresPerSocket;
	protected Long maxCpu;
	protected Long usedStorage;
	protected Long usedMemory;
	protected Float usedCpu;
	protected Long provisionTime = 0L;
	protected Network network;
	protected String configGroup;
	protected String configId;
	protected String configRole;
	protected String configSettings;
	protected String configTags;
	protected String inventoryExternalRef;
	protected Long inventoryServiceId;
	protected String changeManagementExtId;
	protected Long changeManagementServiceId;
	//ips for virtual networking;
	protected String internalIp;
	protected String externalIp;
	//external mapping;
	protected String internalId; //id of the pod;
	protected String externalId; //id of the container;
	protected String uniqueId; //id to the container engine;
	protected String controlId; //id in the scheduler;
	protected String monitorId; //id of the monitor;
	protected String serviceId; //id of the service;
	protected String deploymentId; //id of the spec used to deploy;
	protected String iacId; //id for infrastructure as code integrations;
	protected String specType; //tracking mapping to scheduler;
	protected String specId;
	protected String resourceLevel = "app";
	//pricing estimates;
	protected Double hourlyPrice = 0D;
	protected Double hourlyCost = 0D;
	protected Double runningMultiplier = 0D;
	protected Double runningPrice;
	protected Double runningCost;
	protected String serviceUrls;  // take priority over ipAddress stuff;
	protected Boolean inService = true;
	protected String itemSource = "user";
	protected Boolean managed = true;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public ComputeServer getServer() {
		return server;
	}

	public void setServer(ComputeServer server) {
		this.server = server;
		markDirty("server", server);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public ServicePlan getPlan() {
		return plan;
	}

	public void setPlan(ServicePlan plan) {
		this.plan = plan;
	}

	public Boolean getPrivateNetworking() {
		return privateNetworking;
	}

	public void setPrivateNetworking(Boolean privateNetworking) {
		this.privateNetworking = privateNetworking;
		markDirty("privateNetworking", privateNetworking);
	}

	public String getUserData() {
		return userData;
	}

	public void setUserData(String userData) {
		this.userData = userData;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
		markDirty("instance", instance);
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
		markDirty("internalName", internalName);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName", displayName);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
		markDirty("apiKey", apiKey);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public Boolean getStatsEnabled() {
		return statsEnabled;
	}

	public void setStatsEnabled(Boolean statsEnabled) {
		this.statsEnabled = statsEnabled;
		markDirty("statsEnabled", statsEnabled);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		markDirty("errorMessage", errorMessage);
	}

	public String getEnvironmentPrefix() {
		return environmentPrefix;
	}

	public void setEnvironmentPrefix(String environmentPrefix) {
		this.environmentPrefix = environmentPrefix;
		markDirty("environmentPrefix", environmentPrefix);
	}

	public String getLastStats() {
		return lastStats;
	}

	public void setLastStats(String lastStats) {
		this.lastStats = lastStats;
		markDirty("lastStats", lastStats);
	}

	public String getConfigs() {
		return configs;
	}

	public void setConfigs(String configs) {
		this.configs = configs;
		markDirty("configs", configs);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public String getRuntimeInfo() {
		return runtimeInfo;
	}

	public void setRuntimeInfo(String runtimeInfo) {
		this.runtimeInfo = runtimeInfo;
		markDirty("runtimeInfo", runtimeInfo);
	}

	public String getContainerVersion() {
		return containerVersion;
	}

	public void setContainerVersion(String containerVersion) {
		this.containerVersion = containerVersion;
		markDirty("containerVersion", containerVersion);
	}

	public String getRepositoryImage() {
		return repositoryImage;
	}

	public void setRepositoryImage(String repositoryImage) {
		this.repositoryImage = repositoryImage;
		markDirty("repositoryImage", repositoryImage);
	}

	public String getPlanCategory() {
		return planCategory;
	}

	public void setPlanCategory(String planCategory) {
		this.planCategory = planCategory;
		markDirty("planCategory", planCategory);
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
		markDirty("hostname", hostname);
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
		markDirty("domainName", domainName);
	}

	public Boolean getVolumeCreated() {
		return volumeCreated;
	}

	public void setVolumeCreated(Boolean volumeCreated) {
		this.volumeCreated = volumeCreated;
		markDirty("volumeCreated", volumeCreated);
	}

	public Boolean getContainerCreated() {
		return containerCreated;
	}

	public void setContainerCreated(Boolean containerCreated) {
		this.containerCreated = containerCreated;
		markDirty("containerCreated", containerCreated);
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage", maxStorage);
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
		markDirty("maxMemory", maxMemory);
	}

	public Long getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
		markDirty("maxCores", maxCores);
	}

	public Long getCoresPerSocket() {
		return coresPerSocket;
	}

	public void setCoresPerSocket(Long coresPerSocket) {
		this.coresPerSocket = coresPerSocket;
		markDirty("coresPerSocket", coresPerSocket);
	}

	public Long getMaxCpu() {
		return maxCpu;
	}

	public void setMaxCpu(Long maxCpu) {
		this.maxCpu = maxCpu;
		markDirty("maxCpu", maxCpu);
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
		markDirty("usedStorage", usedStorage);
	}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
		markDirty("usedMemory", usedMemory);
	}

	public Float getUsedCpu() {
		return usedCpu;
	}

	public void setUsedCpu(Float usedCpu) {
		this.usedCpu = usedCpu;
		markDirty("usedCpu", usedCpu);
	}

	public Long getProvisionTime() {
		return provisionTime;
	}

	public void setProvisionTime(Long provisionTime) {
		this.provisionTime = provisionTime;
		markDirty("provisionTime", provisionTime);
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
		markDirty("network", network);
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
		markDirty("configGroup", configGroup);
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
		markDirty("configId", configId);
	}

	public String getConfigRole() {
		return configRole;
	}

	public void setConfigRole(String configRole) {
		this.configRole = configRole;
		markDirty("configRole", configRole);
	}

	public String getConfigSettings() {
		return configSettings;
	}

	public void setConfigSettings(String configSettings) {
		this.configSettings = configSettings;
		markDirty("configSettings", configSettings);
	}

	public String getConfigTags() {
		return configTags;
	}

	public void setConfigTags(String configTags) {
		this.configTags = configTags;
		markDirty("configTags", configTags);
	}

	public String getInventoryExternalRef() {
		return inventoryExternalRef;
	}

	public void setInventoryExternalRef(String inventoryExternalRef) {
		this.inventoryExternalRef = inventoryExternalRef;
		markDirty("inventoryExternalRef", inventoryExternalRef);
	}

	public Long getInventoryServiceId() {
		return inventoryServiceId;
	}

	public void setInventoryServiceId(Long inventoryServiceId) {
		this.inventoryServiceId = inventoryServiceId;
		markDirty("inventoryServiceId", inventoryServiceId);
	}

	public String getChangeManagementExtId() {
		return changeManagementExtId;
	}

	public void setChangeManagementExtId(String changeManagementExtId) {
		this.changeManagementExtId = changeManagementExtId;
		markDirty("changeManagementExtId", changeManagementExtId);
	}

	public Long getChangeManagementServiceId() {
		return changeManagementServiceId;
	}

	public void setChangeManagementServiceId(Long changeManagementServiceId) {
		this.changeManagementServiceId = changeManagementServiceId;
		markDirty("changeManagementServiceId", changeManagementServiceId);
	}

	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
		markDirty("internalIp", internalIp);
	}

	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
		markDirty("externalIp", externalIp);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public String getControlId() {
		return controlId;
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
		markDirty("controlId", controlId);
	}

	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
		markDirty("monitorId", monitorId);
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
		markDirty("serviceId", serviceId);
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
		markDirty("deploymentId", deploymentId);
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public String getSpecType() {
		return specType;
	}

	public void setSpecType(String specType) {
		this.specType = specType;
		markDirty("specType", specType);
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
		markDirty("specId", specId);
	}

	public String getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(String resourceLevel) {
		this.resourceLevel = resourceLevel;
		markDirty("resourceLevel", resourceLevel);
	}

	public Double getHourlyPrice() {
		return hourlyPrice;
	}

	public void setHourlyPrice(Double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
		markDirty("hourlyPrice", hourlyPrice);
	}

	public Double getHourlyCost() {
		return hourlyCost;
	}

	public void setHourlyCost(Double hourlyCost) {
		this.hourlyCost = hourlyCost;
		markDirty("hourlyCost", hourlyCost);
	}

	public Double getRunningMultiplier() {
		return runningMultiplier;
	}

	public void setRunningMultiplier(Double runningMultiplier) {
		this.runningMultiplier = runningMultiplier;
		markDirty("runningMultiplier", runningMultiplier);
	}

	public Double getRunningPrice() {
		return runningPrice;
	}

	public void setRunningPrice(Double runningPrice) {
		this.runningPrice = runningPrice;
		markDirty("runningPrice", runningPrice);
	}

	public Double getRunningCost() {
		return runningCost;
	}

	public void setRunningCost(Double runningCost) {
		this.runningCost = runningCost;
		markDirty("runningCost", runningCost);
	}

	public String getServiceUrls() {
		return serviceUrls;
	}

	public void setServiceUrls(String serviceUrls) {
		this.serviceUrls = serviceUrls;
		markDirty("serviceUrls", serviceUrls);
	}

	public Boolean getInService() {
		return inService;
	}

	public void setInService(Boolean inService) {
		this.inService = inService;
		markDirty("inService", inService);
	}

	public String getItemSource() {
		return itemSource;
	}

	public void setItemSource(String itemSource) {
		this.itemSource = itemSource;
		markDirty("itemSource", itemSource);
	}

	public Boolean getManaged() {
		return managed;
	}

	public void setManaged(Boolean managed) {
		this.managed = managed;
		markDirty("managed", managed);
	}

	public enum Status {
		deploying,
		running,
		stopped,
		suspended,
		failed,
		pending,
		expired,
		stopping,
		starting,
		suspending,
		warning,
		unknown
	}
}
