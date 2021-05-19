package com.morpheusdata.model;

import java.util.Date;
import java.util.UUID;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *	Integrations or connections to public, private, hybrid clouds, or bare metal servers
 */
public class Cloud extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;

	protected String name;

	protected String code;

	protected String description;

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String visibility = "private"; //['public', 'private']
	protected String location;
	protected String timezone;
	//	public ComputeZoneType zoneType;
	protected String config;
	protected Date dateCreated;
	protected Date lastUpdated;
	//	public ComputeAclChain chain;
	protected Boolean autoCapacity = false;
	protected String serviceUrl;
	protected String serviceUsername;
	protected String servicePassword;
	protected String servicePath;
	protected String serviceToken;
	protected String serviceVersion;
	protected String autoCapacityType = "percent"; //percent, count
	protected String autoCapacityConfig; //form json map of settings
	protected String autoCapacityPrefix; //name
	protected Integer autoCapacityThreshold;
	protected NetworkDomain networkDomain;
	protected NetworkProxy apiProxy;
	protected NetworkProxy provisioningProxy;
	protected Boolean applianceUrlProxyBypass = true;
	protected String authRealm;
	protected Long autoCapacityMax; //max servers / ram in zone
	protected Long scalePriority = 1L;
	protected Boolean autoCapacityInProgress = false;
	protected Boolean firewallEnabled = true;
	protected Boolean enabled = true;
	protected Status status = Status.ok;
	protected String statusMessage;
	protected Date statusDate;
	protected String errorMessage;
	protected String regionCode;
	protected String agentMode = "cloudInit";
	protected String datacenterId;
	protected String userDataLinux;
	protected String userDataWindows;
	protected Double reservedMemory = 0d;
	protected Double provisionPercent = 1.0d;
	protected Double costAdjustment = 1.0d;
	protected Boolean deleted = false;
	protected String guidanceMode;
	protected String costingMode;
	protected String inventoryLevel = "off";
	protected Date lastSync;
	protected Date nextRunDate;
	protected Long lastSyncDuration;
	//zone integration config
	protected String containerMode = "docker";
	protected String storageMode = "standard";
	protected String securityMode = "off"; //host firewall.. off or internal
	//	public NetworkSecurityServer securityServer; //integrated security service
	protected String networkSecurityMode; // internal (to manage internal firewall for VMs) (ignored if securityServer is set - not used
	//	public NetworkServer networkServer; //virtual or physical network provider
	protected String backupMode = "internal"; //if backups are off,run by morpheus or a provider
	//	public BackupProvider backupProvider; //integrated backup provider
	//flags on if a cloud is ok to allow these types of provision - for ex, kvm needs a kvm host, kube mode need a master and 1 or more workers
	protected Boolean kvmEnabled = false;
	protected Boolean dockerEnabled = false;
	protected Boolean nativeEnabled = true;
	protected Boolean autoRecoverPowerState = true;
	protected String consoleKeymap;
	//external mapping
	protected String externalId;
	protected String internalId;
	protected String iacId; //id for infrastructure as code integrations;
	protected String uuid = UUID.randomUUID().toString();
	protected String noProxy;

	/**
	 * Morpheus Account
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * Cloud name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Unique code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * A text description of this Cloud
	 */
	public String getDescription() {
		return description;
	}

	public Account getOwner() {
		return owner;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getLocation() {
		return location;
	}

	public String getTimezone() {
		return timezone;
	}

	public String getConfig() {
		return config;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public Boolean getAutoCapacity() {
		return autoCapacity;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public String getServicePath() {
		return servicePath;
	}

	public String getServiceToken() {
		return serviceToken;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public String getAutoCapacityType() {
		return autoCapacityType;
	}

	public String getAutoCapacityConfig() {
		return autoCapacityConfig;
	}

	public String getAutoCapacityPrefix() {
		return autoCapacityPrefix;
	}

	public Integer getAutoCapacityThreshold() {
		return autoCapacityThreshold;
	}

	public NetworkDomain getNetworkDomain() {
		return networkDomain;
	}

	public NetworkProxy getApiProxy() {
		return apiProxy;
	}

	public NetworkProxy getProvisioningProxy() {
		return provisioningProxy;
	}

	public Boolean getApplianceUrlProxyBypass() {
		return applianceUrlProxyBypass;
	}

	public String getAuthRealm() {
		return authRealm;
	}

	public Long getAutoCapacityMax() {
		return autoCapacityMax;
	}

	public Long getScalePriority() {
		return scalePriority;
	}

	public Boolean getAutoCapacityInProgress() {
		return autoCapacityInProgress;
	}

	public Boolean getFirewallEnabled() {
		return firewallEnabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public com.morpheusdata.model.Cloud.Status getStatus() {
		return status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public String getAgentMode() {
		return agentMode;
	}

	public String getDatacenterId() {
		return datacenterId;
	}

	public String getUserDataLinux() {
		return userDataLinux;
	}

	public String getUserDataWindows() {
		return userDataWindows;
	}

	public Double getReservedMemory() {
		return reservedMemory;
	}

	public Double getProvisionPercent() {
		return provisionPercent;
	}

	public Double getCostAdjustment() {
		return costAdjustment;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public String getGuidanceMode() {
		return guidanceMode;
	}

	public String getCostingMode() {
		return costingMode;
	}

	public String getInventoryLevel() {
		return inventoryLevel;
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

	public String getContainerMode() {
		return containerMode;
	}

	public String getStorageMode() {
		return storageMode;
	}

	public String getSecurityMode() {
		return securityMode;
	}

	public String getNetworkSecurityMode() {
		return networkSecurityMode;
	}

	public String getBackupMode() {
		return backupMode;
	}

	public Boolean getKvmEnabled() {
		return kvmEnabled;
	}

	public Boolean getDockerEnabled() {
		return dockerEnabled;
	}

	public Boolean getNativeEnabled() {
		return nativeEnabled;
	}

	public Boolean getAutoRecoverPowerState() {
		return autoRecoverPowerState;
	}

	public String getConsoleKeymap() {
		return consoleKeymap;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getInternalId() {
		return internalId;
	}

	public String getIacId() {
		return iacId;
	}

	public String getUuid() {
		return uuid;
	}

	public String getNoProxy() {
		return noProxy;
	}

	public enum Status {
		ok,
		syncing,
		warning,
		error,
		offline
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public void setLocation(String location) {
		this.location = location;
		markDirty("location", location);
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
		markDirty("timezone", timezone);
	}

	public void setConfig(String config) {
		this.config = config;
		markDirty("config", config);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setAutoCapacity(Boolean autoCapacity) {
		this.autoCapacity = autoCapacity;
		markDirty("autoCapacity", autoCapacity);
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		markDirty("serviceUrl", serviceUrl);
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
		markDirty("serviceUsername", serviceUsername);
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
		markDirty("servicePassword", servicePassword);
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
		markDirty("servicePath", servicePath);
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
		markDirty("serviceToken", serviceToken);
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
		markDirty("serviceVersion", serviceVersion);
	}

	public void setAutoCapacityType(String autoCapacityType) {
		this.autoCapacityType = autoCapacityType;
		markDirty("autoCapacityType", autoCapacityType);
	}

	public void setAutoCapacityConfig(String autoCapacityConfig) {
		this.autoCapacityConfig = autoCapacityConfig;
		markDirty("autoCapacityConfig", autoCapacityConfig);
	}

	public void setAutoCapacityPrefix(String autoCapacityPrefix) {
		this.autoCapacityPrefix = autoCapacityPrefix;
		markDirty("autoCapacityPrefix", autoCapacityPrefix);
	}

	public void setAutoCapacityThreshold(Integer autoCapacityThreshold) {
		this.autoCapacityThreshold = autoCapacityThreshold;
		markDirty("autoCapacityThreshold", autoCapacityThreshold);
	}

	public void setNetworkDomain(NetworkDomain networkDomain) {
		this.networkDomain = networkDomain;
		markDirty("networkDomain", networkDomain);
	}

	public void setApiProxy(NetworkProxy apiProxy) {
		this.apiProxy = apiProxy;
		markDirty("apiProxy", apiProxy);
	}

	public void setProvisioningProxy(NetworkProxy provisioningProxy) {
		this.provisioningProxy = provisioningProxy;
		markDirty("provisioningProxy", provisioningProxy);
	}

	public void setApplianceUrlProxyBypass(Boolean applianceUrlProxyBypass) {
		this.applianceUrlProxyBypass = applianceUrlProxyBypass;
		markDirty("applianceUrlProxyBypass", applianceUrlProxyBypass);
	}

	public void setAuthRealm(String authRealm) {
		this.authRealm = authRealm;
		markDirty("authRealm", authRealm);
	}

	public void setAutoCapacityMax(Long autoCapacityMax) {
		this.autoCapacityMax = autoCapacityMax;
		markDirty("autoCapacityMax", autoCapacityMax);
	}

	public void setScalePriority(Long scalePriority) {
		this.scalePriority = scalePriority;
		markDirty("scalePriority", scalePriority);
	}

	public void setAutoCapacityInProgress(Boolean autoCapacityInProgress) {
		this.autoCapacityInProgress = autoCapacityInProgress;
		markDirty("autoCapacityInProgress", autoCapacityInProgress);
	}

	public void setFirewallEnabled(Boolean firewallEnabled) {
		this.firewallEnabled = firewallEnabled;
		markDirty("firewallEnabled", firewallEnabled);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setStatus(com.morpheusdata.model.Cloud.Status status) {
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

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		markDirty("errorMessage", errorMessage);
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
		markDirty("regionCode", regionCode);
	}

	public void setAgentMode(String agentMode) {
		this.agentMode = agentMode;
		markDirty("agentMode", agentMode);
	}

	public void setDatacenterId(String datacenterId) {
		this.datacenterId = datacenterId;
		markDirty("datacenterId", datacenterId);
	}

	public void setUserDataLinux(String userDataLinux) {
		this.userDataLinux = userDataLinux;
		markDirty("userDataLinux", userDataLinux);
	}

	public void setUserDataWindows(String userDataWindows) {
		this.userDataWindows = userDataWindows;
		markDirty("userDataWindows", userDataWindows);
	}

	public void setReservedMemory(Double reservedMemory) {
		this.reservedMemory = reservedMemory;
		markDirty("reservedMemory", reservedMemory);
	}

	public void setProvisionPercent(Double provisionPercent) {
		this.provisionPercent = provisionPercent;
		markDirty("provisionPercent", provisionPercent);
	}

	public void setCostAdjustment(Double costAdjustment) {
		this.costAdjustment = costAdjustment;
		markDirty("costAdjustment", costAdjustment);
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
		markDirty("deleted", deleted);
	}

	public void setGuidanceMode(String guidanceMode) {
		this.guidanceMode = guidanceMode;
		markDirty("guidanceMode", guidanceMode);
	}

	public void setCostingMode(String costingMode) {
		this.costingMode = costingMode;
		markDirty("costingMode", costingMode);
	}

	public void setInventoryLevel(String inventoryLevel) {
		this.inventoryLevel = inventoryLevel;
		markDirty("inventoryLevel", inventoryLevel);
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

	public void setContainerMode(String containerMode) {
		this.containerMode = containerMode;
		markDirty("containerMode", containerMode);
	}

	public void setStorageMode(String storageMode) {
		this.storageMode = storageMode;
		markDirty("storageMode", storageMode);
	}

	public void setSecurityMode(String securityMode) {
		this.securityMode = securityMode;
		markDirty("securityMode", securityMode);
	}

	public void setNetworkSecurityMode(String networkSecurityMode) {
		this.networkSecurityMode = networkSecurityMode;
		markDirty("networkSecurityMode", networkSecurityMode);
	}

	public void setBackupMode(String backupMode) {
		this.backupMode = backupMode;
		markDirty("backupMode", backupMode);
	}

	public void setKvmEnabled(Boolean kvmEnabled) {
		this.kvmEnabled = kvmEnabled;
		markDirty("kvmEnabled", kvmEnabled);
	}

	public void setDockerEnabled(Boolean dockerEnabled) {
		this.dockerEnabled = dockerEnabled;
		markDirty("dockerEnabled", dockerEnabled);
	}

	public void setNativeEnabled(Boolean nativeEnabled) {
		this.nativeEnabled = nativeEnabled;
		markDirty("nativeEnabled", nativeEnabled);
	}

	public void setAutoRecoverPowerState(Boolean autoRecoverPowerState) {
		this.autoRecoverPowerState = autoRecoverPowerState;
		markDirty("autoRecoverPowerState", autoRecoverPowerState);
	}

	public void setConsoleKeymap(String consoleKeymap) {
		this.consoleKeymap = consoleKeymap;
		markDirty("consoleKeymap", consoleKeymap);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public void setNoProxy(String noProxy) {
		this.noProxy = noProxy;
		markDirty("noProxy", noProxy);
	}

}
