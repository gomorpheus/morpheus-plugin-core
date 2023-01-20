package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkLoadBalancer extends MorpheusModel {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;

	protected String description;
	protected String visibility = "private"; //['public', 'private']
	protected String internalId;
	protected String externalId;
	protected String areaId;
	protected String sshHost;
	protected Integer sshPort = 22;
	protected String sshUsername;
	protected String sshPassword;
	protected String internalIp; //internalIp for server to server communication
	protected String externalIp; //externalIp for building links for users to get to stuff
	protected String managementUrl; //link to management UI
	protected Integer apiPort;
	protected Integer adminPort;
	protected String config;
	protected boolean sslEnabled = true;
	protected boolean enabled = true;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected Date statusDate;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected NetworkPool pool;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected AccountCertificate sslCert;
	protected Long networkServerId;
	protected String uuid = java.util.UUID.randomUUID().toString();
	protected Double hourlyPrice = 0.0d;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Cloud cloud;

	protected List<NetworkLoadBalancerMonitor> monitors = new ArrayList<NetworkLoadBalancerMonitor>();

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
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

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
		markDirty("areaId", areaId);
	}

	public String getSshHost() {
		return sshHost;
	}

	public void setSshHost(String sshHost) {
		this.sshHost = sshHost;
		markDirty("sshHost", sshHost);
	}

	public Integer getSshPort() {
		return sshPort;
	}

	public void setSshPort(Integer sshPort) {
		this.sshPort = sshPort;
		markDirty("sshPort", sshPort);
	}

	public String getSshUsername() {
		return sshUsername;
	}

	public void setSshUsername(String sshUsername) {
		this.sshUsername = sshUsername;
		markDirty("sshUsername", sshUsername);
	}

	public String getSshPassword() {
		return sshPassword;
	}

	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
		markDirty("sshPassword", sshPassword);
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

	public String getManagementUrl() {
		return managementUrl;
	}

	public void setManagementUrl(String managementUrl) {
		this.managementUrl = managementUrl;
		markDirty("managementUrl", managementUrl);
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

	@Override
	public String getConfig() {
		return config;
	}

	@Override
	public void setConfig(String config) {
		this.config = config;
		markDirty("config", config);
	}

	public boolean isSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
		markDirty("sslEnabled", sslEnabled);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
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

	public NetworkPool getPool() {
		return pool;
	}

	public void setPool(NetworkPool pool) {
		this.pool = pool;
		markDirty("pool", pool);
	}

	public AccountCertificate getSslCert() {
		return sslCert;
	}

	public void setSslCert(AccountCertificate sslCert) {
		this.sslCert = sslCert;
		markDirty("sslCert", sslCert);
	}

	public Long getNetworkServerId() {
		return networkServerId;
	}

	public void setNetworkServerId(Long networkServerId) {
		this.networkServerId = networkServerId;
		markDirty("networkServerId", networkServerId);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public Double getHourlyPrice() {
		return hourlyPrice;
	}

	public void setHourlyPrice(Double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
		markDirty("hourlyPrice", hourlyPrice);
	}

	public List<NetworkLoadBalancerMonitor> getMonitors() {
		return monitors;
	}

	public void setMonitors(List<NetworkLoadBalancerMonitor> monitors) {
		this.monitors = monitors;
		markDirty("monitors", monitors);
	}

	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
		markDirty("cloud", cloud);
	}
}