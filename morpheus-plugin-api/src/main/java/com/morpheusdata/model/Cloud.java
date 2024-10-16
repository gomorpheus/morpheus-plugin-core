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

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.CloudIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 *	Integrations or connections to public, private, hybrid clouds, or bare metal servers
 */
public class Cloud extends CloudIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;

	protected String description;

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String visibility = "private"; //['public', 'private']
	protected String location;
	protected String timezone;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public CloudType cloudType;
	protected Boolean hasNativeSecurityGroups;
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
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkDomain networkDomain;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkProxy apiProxy;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
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

	protected Boolean deleted = false;
	protected String guidanceMode;
	protected String costingMode;
	protected String costStatus;
	protected String costStatusMessage;
	protected String inventoryLevel = "off";
	protected Date lastSync;
	protected Date nextRunDate;
	protected Long lastSyncDuration;
	//zone integration config

	protected String securityMode = "off"; //host firewall.. off or internal

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSecurityServer securityServer; //integrated security service
	protected String networkSecurityMode; // internal (to manage internal firewall for VMs) (ignored if securityServer is set - not used
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected NetworkServer networkServer; //virtual or physical network provider
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected CloudType type;
	protected String backupMode = "internal"; //if backups are off,run by morpheus or a provider
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public BackupProvider backupProvider; //integrated backup provider

	protected Boolean autoRecoverPowerState = true;
	protected String consoleKeymap;
	//external mapping
	protected String externalId;
	protected String internalId;
	protected String linkedAccountId;
	protected String iacId; //id for infrastructure as code integrations;
	protected String uuid = UUID.randomUUID().toString();
	protected String noProxy;

	//adding default sync flags
	protected Boolean defaultFolderSyncActive;
	protected Boolean defaultPoolSyncActive;
	protected Boolean defaultNetworkSyncActive;
	protected Boolean defaultDatastoreSyncActive;
	protected Boolean defaultPlanSyncActive;
	protected Boolean defaultSecurityGroupSyncActive;
	protected Long defaultFolderSyncAccount;
	protected Long defaultPoolSyncAccount;
	protected Long defaultNetworkSyncAccount;
	protected Long defaultDatastoreSyncAccount;
	protected Long defaultPlanSyncAccount;
	protected Long defaultSecurityGroupSyncAccount;

	//non-persisted properties for active credentials
	protected Map accountCredentialData;
	protected Boolean accountCredentialLoaded = false;

	/**
	 * Morpheus Account
	 * @return Account the account for the Cloud
	 */
	public Account getAccount() {
		return account;
	}

	/**
	 * A text description of this Cloud
	 * @return String the description
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

	public CloudType getCloudType() {
		return cloudType;
	}

	public Boolean getHasNativeSecurityGroups() {
		return hasNativeSecurityGroups;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
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



	public Boolean getDeleted() {
		return deleted;
	}

	public String getGuidanceMode() {
		return guidanceMode;
	}

	public String getCostingMode() {
		return costingMode;
	}

	public String getCostStatus() {
		return costStatus;
	}

	public String getCostStatusMessage() {
		return costStatusMessage;
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

	public String getSecurityMode() {
		return securityMode;
	}

	public NetworkSecurityServer getSecurityServer() {
		return securityServer;
	}

	public String getNetworkSecurityMode() {
		return networkSecurityMode;
	}

	public String getBackupMode() {
		return backupMode;
	}

	public BackupProvider getBackupProvider() { return backupProvider; }

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

	/**
	 * Indicates if the AccountCredential associated with this Cloud has been loaded.
	 * This is a non-persisted property and used by Cloud consumers to indicate if the
	 * AccountCredential needs to be loaded via a context
	 * @return Boolean
	 */
	public Boolean getAccountCredentialLoaded() {
		return accountCredentialLoaded;
	}

	public void setAccountCredentialLoaded(Boolean accountCredentialLoaded) {
		this.accountCredentialLoaded = accountCredentialLoaded;
	}

	/**
	 * The AccountCredential.data associates with this Cloud.
	 * This is a non-persisted property and not set when obtain from Morpheus.
	 * Typically this is set by Providers via calls to MorpheusCloudService.loadCredentials or
	 * MorpheusAccountCredentialService.loadCredentialConfig. After obtaining the value, one call
	 * setAccountCredentialLoaded(true) to indicate that any credential information has been loaded
	 * @return Map of the credential data
	 */
	public Map getAccountCredentialData() {
		return accountCredentialData;
	}

	public void setAccountCredentialData(Map accountCredentialData) {
		this.accountCredentialData = accountCredentialData;
	}

	public NetworkServer getNetworkServer() {
		return networkServer;
	}

	public void setNetworkServer(NetworkServer networkServer) {
		this.networkServer = networkServer;
	}

	public String getLinkedAccountId() {
		return linkedAccountId;
	}

	public void setLinkedAccountId(String linkedAccountId) {
		this.linkedAccountId = linkedAccountId;
	}

	public Boolean getDefaultFolderSyncActive() {
		return defaultFolderSyncActive;
	}

	public void setDefaultFolderSyncActive(Boolean defaultFolderSyncActive) {
		this.defaultFolderSyncActive = defaultFolderSyncActive;
	}

	public Boolean getDefaultPoolSyncActive() {
		return defaultPoolSyncActive;
	}

	public void setDefaultPoolSyncActive(Boolean defaultPoolSyncActive) {
		this.defaultPoolSyncActive = defaultPoolSyncActive;
	}

	public Boolean getDefaultNetworkSyncActive() {
		return defaultNetworkSyncActive;
	}

	public void setDefaultNetworkSyncActive(Boolean defaultNetworkSyncActive) {
		this.defaultNetworkSyncActive = defaultNetworkSyncActive;
	}

	public Boolean getDefaultDatastoreSyncActive() {
		return defaultDatastoreSyncActive;
	}

	public void setDefaultDatastoreSyncActive(Boolean defaultDatastoreSyncActive) {
		this.defaultDatastoreSyncActive = defaultDatastoreSyncActive;
	}

	public Boolean getDefaultPlanSyncActive() {
		return defaultPlanSyncActive;
	}

	public void setDefaultPlanSyncActive(Boolean defaultPlanSyncActive) {
		this.defaultPlanSyncActive = defaultPlanSyncActive;
	}

	public Boolean getDefaultSecurityGroupSyncActive() {
		return defaultSecurityGroupSyncActive;
	}

	public void setDefaultSecurityGroupSyncActive(Boolean defaultSecurityGroupSyncActive) {
		this.defaultSecurityGroupSyncActive = defaultSecurityGroupSyncActive;
	}

	public Long getDefaultFolderSyncAccount() {
		return defaultFolderSyncAccount;
	}

	public void setDefaultFolderSyncAccount(Long defaultFolderSyncAccount) {
		this.defaultFolderSyncAccount = defaultFolderSyncAccount;
	}

	public Long getDefaultPoolSyncAccount() {
		return defaultPoolSyncAccount;
	}

	public void setDefaultPoolSyncAccount(Long defaultPoolSyncAccount) {
		this.defaultPoolSyncAccount = defaultPoolSyncAccount;
	}

	public Long getDefaultNetworkSyncAccount() {
		return defaultNetworkSyncAccount;
	}

	public void setDefaultNetworkSyncAccount(Long defaultNetworkSyncAccount) {
		this.defaultNetworkSyncAccount = defaultNetworkSyncAccount;
	}

	public Long getDefaultDatastoreSyncAccount() {
		return defaultDatastoreSyncAccount;
	}

	public void setDefaultDatastoreSyncAccount(Long defaultDatastoreSyncAccount) {
		this.defaultDatastoreSyncAccount = defaultDatastoreSyncAccount;
	}

	public Long getDefaultPlanSyncAccount() {
		return defaultPlanSyncAccount;
	}

	public void setDefaultPlanSyncAccount(Long defaultPlanSyncAccount) {
		this.defaultPlanSyncAccount = defaultPlanSyncAccount;
	}

	public Long getDefaultSecurityGroupSyncAccount() {
		return defaultSecurityGroupSyncAccount;
	}

	public void setDefaultSecurityGroupSyncAccount(Long defaultSecurityGroupSyncAccount) {
		this.defaultSecurityGroupSyncAccount = defaultSecurityGroupSyncAccount;
	}

	public enum Status {
		ok,
		syncing,
		initializing,
		warning,
		error,
		offline
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
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

	public void setCloudType(CloudType cloudType) {
		this.cloudType = cloudType;
		markDirty("cloudType", cloudType, this.cloudType);
	}

	public void setHasNativeSecurityGroups(Boolean hasNativeSecurityGroups) {
		this.hasNativeSecurityGroups = hasNativeSecurityGroups;
		markDirty("hasNativeSecurityGroups", hasNativeSecurityGroups);
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

	public void setCostStatus(String costStatus) {
		this.costStatus = costStatus;
		markDirty("costStatus", costStatus);
	}

	public void setCostStatusMessage(String costStatusMessage) {
		this.costStatusMessage = costStatusMessage;
		markDirty("costStatusMessage", costStatusMessage);
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


	public void setSecurityMode(String securityMode) {
		this.securityMode = securityMode;
		markDirty("securityMode", securityMode);
	}

	public void setSecurityServer(NetworkSecurityServer securityServer) {
		this.securityServer = securityServer;
		markDirty("securityServer", securityServer);
	}

	public void setNetworkSecurityMode(String networkSecurityMode) {
		this.networkSecurityMode = networkSecurityMode;
		markDirty("networkSecurityMode", networkSecurityMode);
	}

	public void setBackupMode(String backupMode) {
		this.backupMode = backupMode;
		markDirty("backupMode", backupMode);
	}

	public void setBackupProvider(BackupProvider backupProvider) {
		this.backupProvider = backupProvider;
		markDirty("backupProvider", backupProvider);
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

	public CloudType getType() {
		return type;
	}

	public void setType(CloudType type) {
		this.type = type;
		markDirty("type", type);
	}
}
