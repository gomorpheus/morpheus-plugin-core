package com.morpheusdata.model;

import com.morpheusdata.model.projection.ComputeServerIdentityProjection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Representation of a Morpheus ComputeServer database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author David Estes
 */
public class ComputeServer extends ComputeServerIdentityProjection {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String uuid;
	protected String displayName;
	protected String uniqueId;
	protected Cloud cloud;
	protected NetworkDomain networkDomain;
	protected ServicePlan plan;
	protected String internalName;
	protected String status = "provisioning";
	protected String hostname;
	protected Long provisionSiteId;
	protected OsType serverOs;
	protected VirtualImage sourceImage;
	protected String osType = "linux"; //linux, windows, unmanaged
	protected String platform;
	protected ComputeZonePool resourcePool;
	protected String serverType;
	protected String consoleHost;
	protected PowerState powerState;
	protected Long maxStorage;
	protected Long maxMemory;
	protected Long maxCores;
	protected Boolean managed;
	protected ComputeServerType computeServerType;
	protected Double hourlyPrice = 0D;
	protected String internalIp;
	protected String externalIp;
	protected String sshHost;
	protected String sshUsername;
	protected String sshPassword;
	protected List<ComputeServerInterface> interfaces = new ArrayList<>();
	protected String externalHostname;
	protected String externalDomain;
	protected String externalFqdn;
	protected String apiKey;
	protected List<StorageVolume> volumes = new ArrayList<>();
	protected String osDevice = "/dev/sda";
	protected String dataDevice = "/dev/sda";
	protected Boolean lvmEnabled = true;
	protected String internalId;
	protected String serverVendor;
	protected String serverModel;
	protected String serialNumber;
	protected String statusMessage;
	protected String rootVolumeId;
	protected String tags;
	protected Boolean enabled = true;
	protected Boolean provision = true;
	protected String macAddress;
	protected ComputeCapacityInfo capacityInfo;
	protected Boolean agentInstalled;
	protected Date lastAgentUpdate;
	protected String agentVersion;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid",uuid);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName",displayName);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId",uniqueId);
	}

	public Cloud getCloud() { return cloud; }

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
		markDirty("cloud", cloud);
	}

	public void setCloudId(Long id) {
		this.cloud = new Cloud();
		this.cloud.id = id;
		markDirty("cloud", cloud);
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

	public String getExternalHostname() {
		return externalHostname;
	}

	public void setExternalHostname(String externalHostname) {
		this.externalHostname = externalHostname;
		markDirty("exteernalHostname", externalHostname);
	}

	public String getExternalDomain() {
		return externalDomain;
	}

	public void setExternalDomain(String externalDomain) {
		this.externalDomain = externalDomain;
		markDirty("externalDomain", externalDomain);
	}

	public String getExternalFqdn() {
		return externalFqdn;
	}

	public void setExternalFqdn(String externalFqdn) {
		this.externalFqdn = externalFqdn;
		markDirty("externalFqdn", externalFqdn);
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
		markDirty("apiKey", apiKey);
	}

	public Account getAccount() {
		return account;
	}

	public NetworkDomain getNetworkDomain() {
		return networkDomain;
	}

	public ServicePlan getPlan() {
		return plan;
	}

	public String getInternalName() {
		return internalName;
	}

	public String getStatus() {
		return status;
	}

	public String getHostname() {
		return hostname;
	}

	public Long getProvisionSiteId() {
		return provisionSiteId;
	}

	public OsType getServerOs() {
		return serverOs;
	}

	public VirtualImage getSourceImage() {
		return sourceImage;
	}

	public String getOsType() {
		return osType;
	}

	public String getPlatform() {
		return platform;
	}

	public ComputeZonePool getResourcePool() {
		return resourcePool;
	}

	public String getServerType() {
		return serverType;
	}

	public String getConsoleHost() {
		return consoleHost;
	}

	public PowerState getPowerState() {
		return powerState;
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

	public Boolean getManaged() {
		return managed;
	}

	public ComputeServerType getComputeServerType() {
		return computeServerType;
	}

	public Double getHourlyPrice() {
		return hourlyPrice;
	}

	public String getInternalIp() {
		return internalIp;
	}

	public String getExternalIp() {
		return externalIp;
	}

	public String getSshHost() {
		return sshHost;
	}

	public List<ComputeServerInterface> getInterfaces() {
		return interfaces;
	}

	public List<StorageVolume> getVolumes() {
		return volumes;
	}

	public enum PowerState {
		on,
		off,
		unknown,
		paused
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public void setNetworkDomain(NetworkDomain networkDomain) {
		this.networkDomain = networkDomain;
		markDirty("networkDomain", networkDomain);
	}

	public void setPlan(ServicePlan plan) {
		this.plan = plan;
		markDirty("plan", plan);
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
		markDirty("internalName", internalName);
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
		markDirty("hostname", hostname);
	}

	public void setProvisionSiteId(Long provisionSiteId) {
		this.provisionSiteId = provisionSiteId;
		markDirty("provisionSiteId", provisionSiteId);
	}

	public void setServerOs(OsType serverOs) {
		this.serverOs = serverOs;
		markDirty("serverOs", serverOs);
	}

	public void setSourceImage(VirtualImage sourceImage) {
		this.sourceImage = sourceImage;
		markDirty("sourceImage", sourceImage);
	}

	public void setOsType(String osType) {
		this.osType = osType;
		markDirty("osType", osType);
	}

	public void setPlatform(String platform) {
		this.platform = platform;
		markDirty("platform", platform);
	}

	public void setResourcePool(ComputeZonePool resourcePool) {
		this.resourcePool = resourcePool;
		markDirty("resourcePool", resourcePool);
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
		markDirty("serverType", serverType);
	}

	public void setConsoleHost(String consoleHost) {
		this.consoleHost = consoleHost;
		markDirty("consoleHost", consoleHost);
	}

	public void setPowerState(PowerState powerState) {
		this.powerState = powerState;
		markDirty("powerState", powerState);
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

	public void setManaged(Boolean managed) {
		this.managed = managed;
		markDirty("managed", managed);
	}

	public void setComputeServerType(ComputeServerType computeServerType) {
		this.computeServerType = computeServerType;
		markDirty("computeServerType", computeServerType);
	}

	public void setHourlyPrice(Double hourlyPrice) {
		this.hourlyPrice = hourlyPrice;
		markDirty("hourlyPrice", hourlyPrice);
	}

	public void setInternalIp(String internalIp) {
		this.internalIp = internalIp;
		markDirty("internalIp", internalIp);
	}

	public void setExternalIp(String externalIp) {
		this.externalIp = externalIp;
		markDirty("externalIp", externalIp);
	}

	public void setSshHost(String sshHost) {
		this.sshHost = sshHost;
		markDirty("sshHost", sshHost);
	}

	public void setInterfaces(List<ComputeServerInterface> interfaces) {
		this.interfaces = interfaces;
		markDirty("interfaces", interfaces);
	}

	public void setVolumes(List<StorageVolume> volumes) {
		this.volumes = volumes;
		markDirty("volumes", volumes);
	}

	public String getOsDevice() {
		return osDevice;
	}

	public void setOsDevice(String osDevice) {
		this.osDevice = osDevice;
		markDirty("osDevice",osDevice);
	}

	public String getDataDevice() {
		return dataDevice;
	}

	public void setDataDevice(String dataDevice) {
		this.dataDevice = dataDevice;
		markDirty("dataDevice",dataDevice);
	}

	public Boolean getLvmEnabled() {
		return lvmEnabled;
	}

	public void setLvmEnabled(Boolean lvmEnabled) {
		this.lvmEnabled = lvmEnabled;
		markDirty("lvmEnabled",lvmEnabled);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId",internalId);
	}

	public String getServerVendor() {
		return serverVendor;
	}

	public void setServerVendor(String serverVendor) {
		this.serverVendor = serverVendor;
		markDirty("serverVendor",serverVendor);
	}

	public String getServerModel() {
		return serverModel;
	}

	public void setServerModel(String serverModel) {
		this.serverModel = serverModel;
		markDirty("serverModel",serverModel);
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
		markDirty("serialNumber",serialNumber);
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage",statusMessage);
	}

	public String getRootVolumeId() {
		return rootVolumeId;
	}

	public void setRootVolumeId(String rootVolumeId) {
		this.rootVolumeId = rootVolumeId;
		markDirty("rootVolumeId",rootVolumeId);
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
		markDirty("tags",tags);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled",enabled);
	}

	public Boolean getProvision() {
		return provision;
	}

	public void setProvision(Boolean provision) {
		this.provision = provision;
		markDirty("provision",provision);
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
		markDirty("macAddress",macAddress);
	}

	public ComputeCapacityInfo getComputeCapacityInfo() {
		return capacityInfo;
	}

	public void setComputeCapacityInfo(ComputeCapacityInfo capacityInfo) {
		this.capacityInfo = capacityInfo;
		markDirty("capacityInfo",capacityInfo);
	}

	public Boolean getAgentInstalled() {
		return agentInstalled;
	}

	public void setAgentInstalled(Boolean agentInstalled) {
		this.agentInstalled = agentInstalled;
		markDirty("agentInstalled",agentInstalled);
	}

	public Date getLastAgentUpdate() {
		return lastAgentUpdate;
	}

	public void setLastAgentUpdate(Date lastAgentUpdate) {
		this.lastAgentUpdate = lastAgentUpdate;
		markDirty("lastAgentUpdate",lastAgentUpdate);
	}

	public String getAgentVersion() {
		return agentVersion;
	}

	public void setAgentVersion(String agentVersion) {
		this.agentVersion = agentVersion;
		markDirty("agentVersion",agentVersion);
	}
}
