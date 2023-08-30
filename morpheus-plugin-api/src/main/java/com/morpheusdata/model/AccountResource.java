package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.AccountResourceIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class AccountResource extends AccountResourceIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected AccountResourceType type;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeZoneRegion region;

	protected ResourceSpec resourceSpec;
	protected String resourceType; //generic type for unknown stuff
	protected String resourceIcon;
	protected String resourceSize;
	protected String resourceKey;
	protected String code;
	protected String category;
	protected String resourceContext;
	protected String resourceVersion;
	//metadata
	protected String displayName;
	protected String description;
	protected String tags;
	protected Boolean enabled = true;
	protected Boolean locked = false;
	//related stuff
	protected Long resourcePoolId;
	protected String resourcePoolName;
	protected Long userId;
	protected String userName;
	protected Long cloudId;
	protected String cloudName;
	protected Long siteId;
	protected String siteName;
	protected Long planId;
	protected String planName;
	protected Long layoutId;
	protected String layoutName;
	protected Long containerId;
	protected String containerName;
	protected Long serverId;
	protected String serverName;
	protected Long instanceId;
	protected String instanceName;
	protected Long serverGroupId;
	protected String serverGroupName;
	//access
	protected String serviceUrl;
	protected String serviceHost;
	protected Integer servicePort = 22;
	protected String serviceMode;
	protected String servicePath;
	protected Integer apiPort;
	protected Integer adminPort;
	protected String serviceUsername;
	protected String servicePassword;
	protected String adminUsername;
	protected String adminPassword;
	protected String privateKey;
	//tie in
	protected String refType;
	protected String refId;
	protected String refName;
	protected String refCategory;

	protected String parentRefType;
	protected String parentRefId;
	protected String parentRefName;
	protected String serviceId;
	protected String iacKey;
	protected Integer iacIndex;
	protected String iacName;
	protected String iacId;
	protected String iacType;
	protected String iacProvider;
	protected Integer displayOrder = 0;
	//network
	protected String hostname;
	protected Long networkDomainId;
	protected String internalIp;
	protected String externalIp;
	//config management
	protected Boolean configEnabled = false;
	protected Long configRefId;
	protected String configGroup;
	protected String configId;
	protected String configRole;
	protected String configSettings;
	protected String configCommand;
	protected String configTags;
	//status
	protected String status;
	protected String userStatus;
	protected String statusMessage;
	protected Date statusDate;
	protected String errorMessage;
	protected Double statusPercent;
	protected Long statusEta;
	//resources
	protected Long maxStorage;
	protected Long maxMemory;
	protected Long maxCores;
	protected Long coresPerSocket;
	protected Long maxCpu;
	//pricing estimates
	protected Double hourlyPrice = 0.0;
	protected Double hourlyCost = 0.0;
	protected Double runningMultiplier = 0.0;
	protected Double runningPrice;
	protected Double runningCost;
	//audit
	protected String apiKey = String.valueOf(java.util.UUID.randomUUID());
	protected String uuid = String.valueOf(java.util.UUID.randomUUID());
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Long createdById;
	protected String createdBy;
	protected Long updatedById;
	protected String updatedBy;
	protected Integer appVersion = 1;
	//json encoded map fields
	protected String specData;
	protected String stateData;
	protected String rawData;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public AccountResourceType getType() {
		return type;
	}

	public void setType(AccountResourceType type) {
		this.type = type;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceIcon() {
		return resourceIcon;
	}

	public void setResourceIcon(String resourceIcon) {
		this.resourceIcon = resourceIcon;
	}

	public String getResourceSize() {
		return resourceSize;
	}

	public void setResourceSize(String resourceSize) {
		this.resourceSize = resourceSize;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getResourceContext() {
		return resourceContext;
	}

	public void setResourceContext(String resourceContext) {
		this.resourceContext = resourceContext;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Long getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(Long resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	public String getResourcePoolName() {
		return resourcePoolName;
	}

	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getCloudId() {
		return cloudId;
	}

	public void setCloudId(Long cloudId) {
		this.cloudId = cloudId;
	}

	public String getCloudName() {
		return cloudName;
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public Long getServerGroupId() {
		return serverGroupId;
	}

	public void setServerGroupId(Long serverGroupId) {
		this.serverGroupId = serverGroupId;
	}

	public String getServerGroupName() {
		return serverGroupName;
	}

	public void setServerGroupName(String serverGroupName) {
		this.serverGroupName = serverGroupName;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
	}

	public Integer getServicePort() {
		return servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
	}

	public String getServiceMode() {
		return serviceMode;
	}

	public void setServiceMode(String serviceMode) {
		this.serviceMode = serviceMode;
	}

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public Integer getApiPort() {
		return apiPort;
	}

	public void setApiPort(Integer apiPort) {
		this.apiPort = apiPort;
	}

	public Integer getAdminPort() {
		return adminPort;
	}

	public void setAdminPort(Integer adminPort) {
		this.adminPort = adminPort;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getRefCategory() {
		return refCategory;
	}

	public void setRefCategory(String refCategory) {
		this.refCategory = refCategory;
	}

	public String getParentRefType() {
		return parentRefType;
	}

	public void setParentRefType(String parentRefType) {
		this.parentRefType = parentRefType;
	}

	public String getParentRefId() {
		return parentRefId;
	}

	public void setParentRefId(String parentRefId) {
		this.parentRefId = parentRefId;
	}

	public String getParentRefName() {
		return parentRefName;
	}

	public void setParentRefName(String parentRefName) {
		this.parentRefName = parentRefName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getIacKey() {
		return iacKey;
	}

	public void setIacKey(String iacKey) {
		this.iacKey = iacKey;
	}

	public Integer getIacIndex() {
		return iacIndex;
	}

	public void setIacIndex(Integer iacIndex) {
		this.iacIndex = iacIndex;
	}

	public String getIacName() {
		return iacName;
	}

	public void setIacName(String iacName) {
		this.iacName = iacName;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
	}

	public String getIacType() {
		return iacType;
	}

	public void setIacType(String iacType) {
		this.iacType = iacType;
	}

	public String getIacProvider() {
		return iacProvider;
	}

	public void setIacProvider(String iacProvider) {
		this.iacProvider = iacProvider;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Long getNetworkDomainId() {
		return networkDomainId;
	}

	public void setNetworkDomainId(Long networkDomainId) {
		this.networkDomainId = networkDomainId;
	}

	public String getInternalIp() {
		return internalIp;
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
	}

	public Boolean getConfigEnabled() {
		return configEnabled;
	}

	public void setConfigEnabled(Boolean configEnabled) {
		this.configEnabled = configEnabled;
	}

	public Long getConfigRefId() {
		return configRefId;
	}

	public void setConfigRefId(Long configRefId) {
		this.configRefId = configRefId;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getConfigRole() {
		return configRole;
	}

	public void setConfigRole(String configRole) {
		this.configRole = configRole;
	}

	public String getConfigSettings() {
		return configSettings;
	}

	public void setConfigSettings(String configSettings) {
		this.configSettings = configSettings;
	}

	public String getConfigCommand() {
		return configCommand;
	}

	public void setConfigCommand(String configCommand) {
		this.configCommand = configCommand;
	}

	public String getConfigTags() {
		return configTags;
	}

	public void setConfigTags(String configTags) {
		this.configTags = configTags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Double getStatusPercent() {
		return statusPercent;
	}

	public void setStatusPercent(Double statusPercent) {
		this.statusPercent = statusPercent;
	}

	public Long getStatusEta() {
		return statusEta;
	}

	public void setStatusEta(Long statusEta) {
		this.statusEta = statusEta;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public Long getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
	}

	public Long getCoresPerSocket() {
		return coresPerSocket;
	}

	public void setCoresPerSocket(Long coresPerSocket) {
		this.coresPerSocket = coresPerSocket;
	}

	public Long getMaxCpu() {
		return maxCpu;
	}

	public void setMaxCpu(Long maxCpu) {
		this.maxCpu = maxCpu;
	}

	public Double getHourlyPrice() {
		return hourlyPrice;
	}

	public void setHourlyPrice(Double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
	}

	public Double getHourlyCost() {
		return hourlyCost;
	}

	public void setHourlyCost(Double hourlyCost) {
		this.hourlyCost = hourlyCost;
	}

	public Double getRunningMultiplier() {
		return runningMultiplier;
	}

	public void setRunningMultiplier(Double runningMultiplier) {
		this.runningMultiplier = runningMultiplier;
	}

	public Double getRunningPrice() {
		return runningPrice;
	}

	public void setRunningPrice(Double runningPrice) {
		this.runningPrice = runningPrice;
	}

	public Double getRunningCost() {
		return runningCost;
	}

	public void setRunningCost(Double runningCost) {
		this.runningCost = runningCost;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Long updatedById) {
		this.updatedById = updatedById;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(Integer appVersion) {
		this.appVersion = appVersion;
	}

	public String getSpecData() {
		return specData;
	}

	public void setSpecData(String specData) {
		this.specData = specData;
	}

	public String getStateData() {
		return stateData;
	}

	public void setStateData(String stateData) {
		this.stateData = stateData;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public ResourceSpec getResourceSpec() {
		return resourceSpec;
	}

	public void setResourceSpec(ResourceSpec resourceSpec) {
		this.resourceSpec = resourceSpec;
		markDirty("resourceSpec", resourceSpec);
	}

	public ComputeZoneRegion getRegion() {
		return region;
	}

	public void setRegion(ComputeZoneRegion region) {
		this.region = region;
	}
}
