package com.morpheusdata.model;

import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class NetworkServer extends MorpheusModel {

	protected String name;
	protected NetworkServerType type;
	protected AccountIntegration integration;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String visibility = "public"; //['public', 'private']
	protected String description;
	protected String internalId;
	protected String externalId;
	protected String serviceUrl;
	protected String serviceHost;
	protected Integer servicePort = 22;
	protected String serviceMode;
	protected String servicePath;
	protected String serviceUsername;
	protected String servicePassword;
	protected String serviceToken;
	protected Integer apiPort;
	protected Integer adminPort;
	//status and sync
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected Date statusDate;
	protected Date lastSync;
	protected Date nextRunDate;
	protected Long lastSyncDuration;
	protected String networkFilter;
	protected String tenantMatch;
	protected Long zoneId; //for network servers that need to be pinned to a zone - like nsx
	//audit
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean enabled = true;
	protected Boolean visible = true;

	public String getName() {
		return name;
	}

	public NetworkServerType getType() {
		return type;
	}

	public AccountIntegration getIntegration() {
		return integration;
	}

	public Account getAccount() {
		return account;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getDescription() {
		return description;
	}

	public String getInternalId() {
		return internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public Integer getServicePort() {
		return servicePort;
	}

	public String getServiceMode() {
		return serviceMode;
	}

	public String getServicePath() {
		return servicePath;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public String getServiceToken() {
		return serviceToken;
	}

	public Integer getApiPort() {
		return apiPort;
	}

	public Integer getAdminPort() {
		return adminPort;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public Date getLastSync() {
		return lastSync;
	}

	public Date getNextRunDate() {
		return nextRunDate;
	}

	public Long getLastSyncDuration() {
		return lastSyncDuration;
	}

	public String getNetworkFilter() {
		return networkFilter;
	}

	public String getTenantMatch() {
		return tenantMatch;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setType(NetworkServerType type) {
		this.type = type;
		markDirty("type", type);
	}

	public void setIntegration(AccountIntegration integration) {
		this.integration = integration;
		markDirty("integration", integration);
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		markDirty("serviceUrl", serviceUrl);
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
		markDirty("serviceHost", serviceHost);
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
		markDirty("servicePort", servicePort);
	}

	public void setServiceMode(String serviceMode) {
		this.serviceMode = serviceMode;
		markDirty("serviceMode", serviceMode);
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
		markDirty("servicePath", servicePath);
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
		markDirty("serviceUsername", serviceUsername);
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
		markDirty("servicePassword", servicePassword);
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
		markDirty("serviceToken", serviceToken);
	}

	public void setApiPort(Integer apiPort) {
		this.apiPort = apiPort;
		markDirty("apiPort", apiPort);
	}

	public void setAdminPort(Integer adminPort) {
		this.adminPort = adminPort;
		markDirty("adminPort", adminPort);
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
		markDirty("statusDate", statusDate);
	}

	public void setLastSync(Date lastSync) {
		this.lastSync = lastSync;
		markDirty("lastSync", lastSync);
	}

	public void setNextRunDate(Date nextRunDate) {
		this.nextRunDate = nextRunDate;
		markDirty("nextRunDate", nextRunDate);
	}

	public void setLastSyncDuration(Long lastSyncDuration) {
		this.lastSyncDuration = lastSyncDuration;
		markDirty("lastSyncDuration", lastSyncDuration);
	}

	public void setNetworkFilter(String networkFilter) {
		this.networkFilter = networkFilter;
		markDirty("networkFilter", networkFilter);
	}

	public void setTenantMatch(String tenantMatch) {
		this.tenantMatch = tenantMatch;
		markDirty("tenantMatch", tenantMatch);
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		markDirty("zoneId", zoneId);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
		markDirty("visible", visible);
	}

}
