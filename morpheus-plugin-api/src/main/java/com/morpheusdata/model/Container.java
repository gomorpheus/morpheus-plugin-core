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
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @deprecated
 * Use {@link Workload} instead
 */
@Deprecated
public class Container extends MorpheusModel {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected Instance instance;
	protected String internalName;
	protected String displayName;
	protected String category;
//	public ContainerType containerType;
//	public ContainerTypeSet containerTypeSet;
protected ComputeServer server;
//	public InstanceAction action;
//	public ServicePlan plan;
//	public ComputeZonePool resourcePool;
protected String apiKey;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean statsEnabled = true;
	protected Status status = Status.pending;
	protected String statusMessage;
	protected String errorMessage;
//	public Status userStatus;
//	public Status scheduleStatus;
protected String environmentPrefix;
	protected String lastStats;
	protected String configs;
	protected String rawData;
	protected String runtimeInfo;
//	public ComputeAclChain chain;
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
	//Optional Meta;
//	public AppDeploy appDeploy;
//	public NetworkLoadBalancerInstance networkLoadBalancerInstance;
	protected Network network;
//	public NetworkSubnet subnet;
//	public NetworkGroup networkGroup;
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
	protected String uuid;

//	static hasMany = [files:ContainerFile, mounts:ContainerMount, ports:ContainerPort, logs:ContainerLog,
//	configMaps:ContainerConfig, volumes:StorageVolume]

	// TODO: implement parsing config
	public Map getConfigProperty(String prop) {
		return Collections.emptyMap();
	}

	void setServerId(Long id) {
		this.server = new ComputeServer();
		this.server.id = id;
	}

	void setNetworkId(Long id) {
		this.network = new Network();
		this.network.id = id;
	}

	void setInstanceId(Long id) {
		this.instance = new Instance();
		this.instance.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public Instance getInstance() {
		return instance;
	}

	public String getInternalName() {
		return internalName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getCategory() {
		return category;
	}

	public ComputeServer getServer() {
		return server;
	}

	public String getApiKey() {
		return apiKey;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Boolean getStatsEnabled() {
		return statsEnabled;
	}

	public com.morpheusdata.model.Container.Status getStatus() {
		return status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getEnvironmentPrefix() {
		return environmentPrefix;
	}

	public String getLastStats() {
		return lastStats;
	}

	public String getConfigs() {
		return configs;
	}

	public String getRawData() {
		return rawData;
	}

	public String getRuntimeInfo() {
		return runtimeInfo;
	}

	public String getContainerVersion() {
		return containerVersion;
	}

	public String getRepositoryImage() {
		return repositoryImage;
	}

	public String getPlanCategory() {
		return planCategory;
	}

	public String getHostname() {
		return hostname;
	}

	public String getDomainName() {
		return domainName;
	}

	public Boolean getVolumeCreated() {
		return volumeCreated;
	}

	public Boolean getContainerCreated() {
		return containerCreated;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public Long getMaxCores() {
		return maxCores;
	}

	public Long getCoresPerSocket() {
		return coresPerSocket;
	}

	public Long getMaxCpu() {
		return maxCpu;
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public Float getUsedCpu() {
		return usedCpu;
	}

	public Long getProvisionTime() {
		return provisionTime;
	}

	public Network getNetwork() {
		return network;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public String getConfigId() {
		return configId;
	}

	public String getConfigRole() {
		return configRole;
	}

	public String getConfigSettings() {
		return configSettings;
	}

	public String getConfigTags() {
		return configTags;
	}

	public String getInventoryExternalRef() {
		return inventoryExternalRef;
	}

	public Long getInventoryServiceId() {
		return inventoryServiceId;
	}

	public String getChangeManagementExtId() {
		return changeManagementExtId;
	}

	public Long getChangeManagementServiceId() {
		return changeManagementServiceId;
	}

	public String getInternalIp() {
		return internalIp;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public String getInternalId() {
		return internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public String getControlId() {
		return controlId;
	}

	public String getMonitorId() {
		return monitorId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public String getIacId() {
		return iacId;
	}

	public String getSpecType() {
		return specType;
	}

	public String getSpecId() {
		return specId;
	}

	public String getResourceLevel() {
		return resourceLevel;
	}

	public Double getHourlyPrice() {
		return hourlyPrice;
	}

	public Double getHourlyCost() {
		return hourlyCost;
	}

	public Double getRunningMultiplier() {
		return runningMultiplier;
	}

	public Double getRunningPrice() {
		return runningPrice;
	}

	public Double getRunningCost() {
		return runningCost;
	}

	public String getServiceUrls() {
		return serviceUrls;
	}

	public Boolean getInService() {
		return inService;
	}

	public String getItemSource() {
		return itemSource;
	}

	public Boolean getManaged() {
		return managed;
	}

	public String getUuid() {
		return uuid;
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
	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
		markDirty("instance", instance);
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
		markDirty("internalName", internalName);
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName", displayName);
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public void setServer(ComputeServer server) {
		this.server = server;
		markDirty("server", server);
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
		markDirty("apiKey", apiKey);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setStatsEnabled(Boolean statsEnabled) {
		this.statsEnabled = statsEnabled;
		markDirty("statsEnabled", statsEnabled);
	}

	public void setStatus(com.morpheusdata.model.Container.Status status) {
		this.status = status;
		markDirty("status", status);
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		markDirty("errorMessage", errorMessage);
	}

	public void setEnvironmentPrefix(String environmentPrefix) {
		this.environmentPrefix = environmentPrefix;
		markDirty("environmentPrefix", environmentPrefix);
	}

	public void setLastStats(String lastStats) {
		this.lastStats = lastStats;
		markDirty("lastStats", lastStats);
	}

	public void setConfigs(String configs) {
		this.configs = configs;
		markDirty("configs", configs);
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public void setRuntimeInfo(String runtimeInfo) {
		this.runtimeInfo = runtimeInfo;
		markDirty("runtimeInfo", runtimeInfo);
	}

	public void setContainerVersion(String containerVersion) {
		this.containerVersion = containerVersion;
		markDirty("containerVersion", containerVersion);
	}

	public void setRepositoryImage(String repositoryImage) {
		this.repositoryImage = repositoryImage;
		markDirty("repositoryImage", repositoryImage);
	}

	public void setPlanCategory(String planCategory) {
		this.planCategory = planCategory;
		markDirty("planCategory", planCategory);
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
		markDirty("hostname", hostname);
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
		markDirty("domainName", domainName);
	}

	public void setVolumeCreated(Boolean volumeCreated) {
		this.volumeCreated = volumeCreated;
		markDirty("volumeCreated", volumeCreated);
	}

	public void setContainerCreated(Boolean containerCreated) {
		this.containerCreated = containerCreated;
		markDirty("containerCreated", containerCreated);
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
		markDirty("maxStorage", maxStorage);
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
		markDirty("maxMemory", maxMemory);
	}

	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
		markDirty("maxCores", maxCores);
	}

	public void setCoresPerSocket(Long coresPerSocket) {
		this.coresPerSocket = coresPerSocket;
		markDirty("coresPerSocket", coresPerSocket);
	}

	public void setMaxCpu(Long maxCpu) {
		this.maxCpu = maxCpu;
		markDirty("maxCpu", maxCpu);
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
		markDirty("usedStorage", usedStorage);
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
		markDirty("usedMemory", usedMemory);
	}

	public void setUsedCpu(Float usedCpu) {
		this.usedCpu = usedCpu;
		markDirty("usedCpu", usedCpu);
	}

	public void setProvisionTime(Long provisionTime) {
		this.provisionTime = provisionTime;
		markDirty("provisionTime", provisionTime);
	}

	public void setNetwork(Network network) {
		this.network = network;
		markDirty("network", network);
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
		markDirty("configGroup", configGroup);
	}

	public void setConfigId(String configId) {
		this.configId = configId;
		markDirty("configId", configId);
	}

	public void setConfigRole(String configRole) {
		this.configRole = configRole;
		markDirty("configRole", configRole);
	}

	public void setConfigSettings(String configSettings) {
		this.configSettings = configSettings;
		markDirty("configSettings", configSettings);
	}

	public void setConfigTags(String configTags) {
		this.configTags = configTags;
		markDirty("configTags", configTags);
	}

	public void setInventoryExternalRef(String inventoryExternalRef) {
		this.inventoryExternalRef = inventoryExternalRef;
		markDirty("inventoryExternalRef", inventoryExternalRef);
	}

	public void setInventoryServiceId(Long inventoryServiceId) {
		this.inventoryServiceId = inventoryServiceId;
		markDirty("inventoryServiceId", inventoryServiceId);
	}

	public void setChangeManagementExtId(String changeManagementExtId) {
		this.changeManagementExtId = changeManagementExtId;
		markDirty("changeManagementExtId", changeManagementExtId);
	}

	public void setChangeManagementServiceId(Long changeManagementServiceId) {
		this.changeManagementServiceId = changeManagementServiceId;
		markDirty("changeManagementServiceId", changeManagementServiceId);
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
		markDirty("internalIp", internalIp);
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
		markDirty("externalIp", externalIp);
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public void setControlId(String controlId) {
		this.controlId = controlId;
		markDirty("controlId", controlId);
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
		markDirty("monitorId", monitorId);
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
		markDirty("serviceId", serviceId);
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
		markDirty("deploymentId", deploymentId);
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public void setSpecType(String specType) {
		this.specType = specType;
		markDirty("specType", specType);
	}

	public void setSpecId(String specId) {
		this.specId = specId;
		markDirty("specId", specId);
	}

	public void setResourceLevel(String resourceLevel) {
		this.resourceLevel = resourceLevel;
		markDirty("resourceLevel", resourceLevel);
	}

	public void setHourlyPrice(Double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
		markDirty("hourlyPrice", hourlyPrice);
	}

	public void setHourlyCost(Double hourlyCost) {
		this.hourlyCost = hourlyCost;
		markDirty("hourlyCost", hourlyCost);
	}

	public void setRunningMultiplier(Double runningMultiplier) {
		this.runningMultiplier = runningMultiplier;
		markDirty("runningMultiplier", runningMultiplier);
	}

	public void setRunningPrice(Double runningPrice) {
		this.runningPrice = runningPrice;
		markDirty("runningPrice", runningPrice);
	}

	public void setRunningCost(Double runningCost) {
		this.runningCost = runningCost;
		markDirty("runningCost", runningCost);
	}

	public void setServiceUrls(String serviceUrls) {
		this.serviceUrls = serviceUrls;
		markDirty("serviceUrls", serviceUrls);
	}

	public void setInService(Boolean inService) {
		this.inService = inService;
		markDirty("inService", inService);
	}

	public void setItemSource(String itemSource) {
		this.itemSource = itemSource;
		markDirty("itemSource", itemSource);
	}

	public void setManaged(Boolean managed) {
		this.managed = managed;
		markDirty("managed", managed);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

}
