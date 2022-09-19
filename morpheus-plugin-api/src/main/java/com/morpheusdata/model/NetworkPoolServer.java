package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;
import java.util.Map;

/**
 * Represents an instance of an IPAM integration server. This integration type contains status fields, connection information
 * as well as a reference to a cloned {@link AccountIntegration} class for the type of pool server.
 * There are also some special properties on pool server instances for use in controlling behavior of an {@link com.morpheusdata.core.IPAMProvider}
 *
 * @author David Estes, Eric Helgeson
 * @see com.morpheusdata.core.IPAMProvider
 * @since 0.8.0
 */
public class NetworkPoolServer extends MorpheusModel {

	protected String name;
	protected String description;
	protected String internalId;
	protected String externalId;
	protected String serviceUrl;
	protected Boolean ignoreSsl = true;
	protected String serviceHost;
	protected Integer servicePort = 22;
	protected String serviceMode;
	protected String serviceUsername;
	protected String servicePassword;
	protected Long serviceThrottleRate=0L;
	protected Integer apiPort;
	protected Integer adminPort;
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected String networkFilter;
	protected String zoneFilter;
	protected String tenantMatch;
	protected Boolean enabled = true;
	protected Date statusDate;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected NetworkPoolServerType type;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected AccountIntegration integration;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;

	protected Map credentialData;
	protected Boolean credentialLoaded = false;

	public void setAccountId(Long id) {
		this.account = new Account();
		this.account.id = id;
	}

	public String getName() {
		return name;
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

	public Boolean getIgnoreSsl() {
		return ignoreSsl;
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

	public String getServiceUsername() {
		return serviceUsername;
	}

	public String getServicePassword() {
		return servicePassword;
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

	public String getNetworkFilter() {
		return networkFilter;
	}

	public String getZoneFilter() {
		return zoneFilter;
	}

	public String getTenantMatch() {
		return tenantMatch;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public NetworkPoolServerType getType() {
		return type;
	}

	public AccountIntegration getIntegration() {
		return integration;
	}

	public Account getAccount() {
		return account;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
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

	public void setIgnoreSsl(Boolean ignoreSsl) {
		this.ignoreSsl = ignoreSsl;
		markDirty("ignoreSsl", ignoreSsl);
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

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
		markDirty("serviceUsername", serviceUsername);
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
		markDirty("servicePassword", servicePassword);
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

	public void setNetworkFilter(String networkFilter) {
		this.networkFilter = networkFilter;
		markDirty("networkFilter", networkFilter);
	}

	public void setZoneFilter(String zoneFilter) {
		this.zoneFilter = zoneFilter;
		markDirty("zoneFilter", zoneFilter);
	}

	public void setTenantMatch(String tenantMatch) {
		this.tenantMatch = tenantMatch;
		markDirty("tenantMatch", tenantMatch);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
		markDirty("statusDate", statusDate);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setType(NetworkPoolServerType type) {
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

	public Long getServiceThrottleRate() {
		return serviceThrottleRate;
	}

	public void setServiceThrottleRate(Long serviceThrottleRate) {
		this.serviceThrottleRate = serviceThrottleRate;
	}


	public Map getCredentialData() {
		return credentialData;
	}

	public void setCredentialData(Map credentialData) {
		this.credentialData = credentialData;
	}

	public Boolean getCredentialLoaded() {
		return credentialLoaded;
	}

	public void setCredentialLoaded(Boolean credentialLoaded) {
		this.credentialLoaded = credentialLoaded;
	}
}
