package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class ComputeServerGroup extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeServerGroupType type;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeTypeLayout layout;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeSite site;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Cloud zone;
	//ServiceEntry serviceEntry //represents the main api entrypoint to the service
	protected String name;
	protected String code;
	protected String category;
	protected String visibility;
	protected String description;
	protected String location;
	protected Boolean enabled;
	protected Boolean managed;
	protected Boolean discovered;
	protected String tags;
	//service info
	protected String serviceUrl;
	protected String serviceHost;
	protected String servicePath;
	protected String serviceHostname;
	protected Integer servicePort;
	protected String serviceUsername;
	protected String servicePassword;
	protected String serviceToken;
	protected String serviceAccess;
	protected String serviceCert;
	protected String serviceConfig;
	protected String serviceVersion;
	//networks
	protected String searchDomains;
	protected Boolean enableInternalDns;
	//linking
	protected String apiKey;
	protected String internalId;
	protected String externalId;
	protected String datacenterId;
	//status
	protected Integer nodeCount;
	protected String status;
	protected Date statusDate;
	protected String statusMessage;
	//json map
	protected String config;
	//sync
	protected String inventoryLevel;
	protected Date lastSync;
	protected Date nextRunDate;
	protected Long lastSyncDuration;
	//audit
	protected User createdBy;
	protected Date dateCreated;
	protected Date lastUpdated;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public ComputeServerGroupType getType() {
		return type;
	}

	public void setType(ComputeServerGroupType type) {
		this.type = type;
	}

	public ComputeTypeLayout getLayout() {
		return layout;
	}

	public void setLayout(ComputeTypeLayout layout) {
		this.layout = layout;
	}

	public ComputeSite getSite() {
		return site;
	}

	public void setSite(ComputeSite site) {
		this.site = site;
	}

	public Cloud getZone() {
		return zone;
	}

	public void setZone(Cloud zone) {
		this.zone = zone;
	}

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getManaged() {
		return managed;
	}

	public void setManaged(Boolean managed) {
		this.managed = managed;
	}

	public Boolean getDiscovered() {
		return discovered;
	}

	public void setDiscovered(Boolean discovered) {
		this.discovered = discovered;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public String getServicePath() {
		return servicePath;
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
	}

	public String getServiceHostname() {
		return serviceHostname;
	}

	public void setServiceHostname(String serviceHostname) {
		this.serviceHostname = serviceHostname;
	}

	public Integer getServicePort() {
		return servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
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

	public String getServiceToken() {
		return serviceToken;
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
	}

	public String getServiceAccess() {
		return serviceAccess;
	}

	public void setServiceAccess(String serviceAccess) {
		this.serviceAccess = serviceAccess;
	}

	public String getServiceCert() {
		return serviceCert;
	}

	public void setServiceCert(String serviceCert) {
		this.serviceCert = serviceCert;
	}

	public String getServiceConfig() {
		return serviceConfig;
	}

	public void setServiceConfig(String serviceConfig) {
		this.serviceConfig = serviceConfig;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public String getSearchDomains() {
		return searchDomains;
	}

	public void setSearchDomains(String searchDomains) {
		this.searchDomains = searchDomains;
	}

	public Boolean getEnableInternalDns() {
		return enableInternalDns;
	}

	public void setEnableInternalDns(Boolean enableInternalDns) {
		this.enableInternalDns = enableInternalDns;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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

	public String getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(String datacenterId) {
		this.datacenterId = datacenterId;
	}

	public Integer getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(Integer nodeCount) {
		this.nodeCount = nodeCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	@Override
	public String getConfig() {
		return config;
	}

	@Override
	public void setConfig(String config) {
		this.config = config;
	}

	public String getInventoryLevel() {
		return inventoryLevel;
	}

	public void setInventoryLevel(String inventoryLevel) {
		this.inventoryLevel = inventoryLevel;
	}

	public Date getLastSync() {
		return lastSync;
	}

	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
	}

	public Date getNextRunDate() {
		return nextRunDate;
	}

	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	public Long getLastSyncDuration() {
		return lastSyncDuration;
	}

	public void setLastSyncDuration(Long lastSyncDuration) {
		this.lastSyncDuration = lastSyncDuration;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
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
}
