package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class NetworkSecurityServer extends MorpheusModel {
	public NetworkSecurityServerType type;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public NetworkServer networkServer; //if its a network and security server link;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public AccountIntegration integration;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account account;
	public String visibility = "public"; //['public', 'private'];
	public String description;
	public String internalId;
	public String externalId;
	public String serviceUrl;
	public String serviceHost;
	public Integer servicePort = 22;
	public String serviceMode;
	public String serviceUsername;
	public String servicePassword;
	public Integer apiPort;
	public Integer adminPort;
	public String status = "ok"; //ok, error, warning, offline;
	public String statusMessage;
	public Date statusDate;
	public String networkFilter;
	public String tenantMatch;
	public Date dateCreated;
	public Date lastUpdated;
	public Boolean enabled = true;
	public Boolean visible = true;
	public Boolean pendingCommit = false;
	public Long zoneId;

	public NetworkSecurityServerType getType() {
		return type;
	}

	public void setType(NetworkSecurityServerType type) {
		this.type = type;
		markDirty("type", type);
	}

	public NetworkServer getNetworkServer() {
		return networkServer;
	}

	public void setNetworkServer(NetworkServer networkServer) {
		this.networkServer = networkServer;
		markDirty("networkServer", networkServer);
	}

	public AccountIntegration getIntegration() {
		return integration;
	}

	public void setIntegration(AccountIntegration integration) {
		this.integration = integration;
		markDirty("integration", integration);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
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

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		markDirty("serviceUrl", serviceUrl);
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
		markDirty("serviceHost", serviceHost);
	}

	public Integer getServicePort() {
		return servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
		markDirty("servicePort", servicePort);
	}

	public String getServiceMode() {
		return serviceMode;
	}

	public void setServiceMode(String serviceMode) {
		this.serviceMode = serviceMode;
		markDirty("serviceMode", serviceMode);
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
		markDirty("serviceUsername", serviceUsername);
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
		markDirty("servicePassword", servicePassword);
	}

	public Integer getApiPort() {
		return apiPort;
	}

	public void setApiPort(Integer apiPort) {
		this.apiPort = apiPort;
		markDirty("apiPort", apiPort);
	}

	public Integer getAdminPort() {
		return adminPort;
	}

	public void setAdminPort(Integer adminPort) {
		this.adminPort = adminPort;
		markDirty("adminPort", adminPort);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
		markDirty("statusDate", statusDate);
	}

	public String getNetworkFilter() {
		return networkFilter;
	}

	public void setNetworkFilter(String networkFilter) {
		this.networkFilter = networkFilter;
		markDirty("networkFilter", networkFilter);
	}

	public String getTenantMatch() {
		return tenantMatch;
	}

	public void setTenantMatch(String tenantMatch) {
		this.tenantMatch = tenantMatch;
		markDirty("tenantMatch", tenantMatch);
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
		markDirty("visible", visible);
	}

	public Boolean getPendingCommit() {
		return pendingCommit;
	}

	public void setPendingCommit(Boolean pendingCommit) {
		this.pendingCommit = pendingCommit;
		markDirty("pendingCommit", pendingCommit);
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		markDirty("zoneId", zoneId);
	}
}
