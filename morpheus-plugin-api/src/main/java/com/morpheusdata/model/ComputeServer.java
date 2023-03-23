package com.morpheusdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.SnapshotIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

/**
 * Representation of a Morpheus ComputeServer database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author David Estes
 */
public class ComputeServer extends ComputeServerIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String uuid;
	protected String displayName;
	protected String uniqueId;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Cloud cloud;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkDomain networkDomain;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ServicePlan plan;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeTypeSet typeSet;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeTypeLayout layout;
	protected String internalName;
	protected String status = "provisioning";
	protected Long provisionSiteId;
	protected OsType serverOs;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected VirtualImage sourceImage;
	protected String osType = "linux"; //linux, windows, unmanaged
	protected String platform;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeZonePool resourcePool;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeZoneFolder folder;
	protected String serverType;
	protected String consoleHost;
	protected PowerState powerState;
	protected Long maxStorage;
	protected Long maxMemory;
	protected Long maxCores;
	protected Long usedMemory;
	protected Long usedStorage;
	protected Float usedCpu;
	protected Long coresPerSocket;
	protected Boolean managed;
	protected Boolean singleTenant = false;
	protected ComputeServerType computeServerType;
	protected Double hourlyPrice = 0D;
	protected String internalIp;
	protected String externalIp;
	protected String sshHost;
	protected String sshUsername;
	protected String sshPassword;
	@JsonIgnore
	protected List<ComputeServerInterface> interfaces = new ArrayList<>();
	protected String externalHostname;
	protected String externalDomain;
	protected String externalFqdn;
	protected String apiKey;
	@JsonIgnore
	protected List<StorageVolume> volumes = new ArrayList<>();
	@JsonIgnore
	protected List<StorageController> controllers = new ArrayList<>();
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	protected List<SnapshotIdentityProjection> snapshots = new ArrayList<>();
	protected String osDevice = "/dev/sda";
	protected String dataDevice = "/dev/sdb";
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
	protected Boolean toolsInstalled = false;
	protected Date lastAgentUpdate;
	protected String agentVersion;
	protected Boolean hotResize = false;
	protected Boolean cpuHotResize = false;
	protected String consoleType;
	protected Integer consolePort;
	protected String consolePassword;
	protected Boolean guestConsolePreferred = false;
	protected GuestConsoleType guestConsoleType;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeServerIdentityProjection parentServer;
	protected ComputeStats computeStats;
	@JsonIgnore
	protected List<MetadataTag> metadata;
	protected Date statusDate;
	protected String cloudConfigUser;
	protected String cloudConfigMeta;
	protected String cloudConfigNetwork;

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

	/**
	 * @deprecated
	 * Use name instead
	 * @param displayName displayName
	 */
	@Deprecated
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

	public ComputeZoneFolder getFolder() {
		return folder;
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

	public Long getCoresPerSocket() { return coresPerSocket; }

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

	public List<StorageController> getControllers() {
		return controllers;
	}

	public List<SnapshotIdentityProjection> getSnapshots() {
		return snapshots;
	}

	public List<MetadataTag> getMetadata() { return metadata;}

	public Long getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Long usedMemory) {
		this.usedMemory = usedMemory;
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		this.usedStorage = usedStorage;
	}

	public Float getUsedCpu() {
		return usedCpu;
	}

	public void setUsedCpu(Float usedCpu) {
		this.usedCpu = usedCpu;
	}

	public String getCloudConfigUser() {
		return cloudConfigUser;
	}

	public void setCloudConfigUser(String cloudConfigUser) {
		this.cloudConfigUser = cloudConfigUser;
	}

	public String getCloudConfigMeta() {
		return cloudConfigMeta;
	}

	public void setCloudConfigMeta(String cloudConfigMeta) {
		this.cloudConfigMeta = cloudConfigMeta;
	}

	public String getCloudConfigNetwork() {
		return cloudConfigNetwork;
	}

	public void setCloudConfigNetwork(String cloudConfigNetwork) {
		this.cloudConfigNetwork = cloudConfigNetwork;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public ComputeTypeSet getTypeSet() {
		return typeSet;
	}

	public void setTypeSet(ComputeTypeSet typeSet) {
		this.typeSet = typeSet;
	}

	public ComputeTypeLayout getLayout() {
		return layout;
	}

	public void setLayout(ComputeTypeLayout layout) {
		this.layout = layout;
	}

	public Boolean getSingleTenant() {
		return singleTenant;
	}

	public void setSingleTenant(Boolean singleTenant) {
		this.singleTenant = singleTenant;
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

	public void setFolder(ComputeZoneFolder folder) {
		this.folder = folder;
		markDirty("folder", folder);
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

	public void setCoresPerSocket(Long coresPerSocket) {
		this.coresPerSocket = coresPerSocket;
		markDirty("coresPerSocket", coresPerSocket);
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

	/**
	 * NOTE: To modify the list of interfaces associated with this ComputeServer, utilize MorpheusComputeServerInterfaceService
	 * @param interfaces
	 */
	public void setInterfaces(List<ComputeServerInterface> interfaces) {
		this.interfaces = interfaces;
		markDirty("interfaces", interfaces);
	}

	/**
	 * NOTE: To modify the list of volumes associated with this ComputeServer, utilize MorpheusStorageVolumeService
	 * @param volumes
	 */
	public void setVolumes(List<StorageVolume> volumes) {
		this.volumes = volumes;
		markDirty("volumes", volumes);
	}

	/**
	 * NOTE: To modify the list of controllers associated with this ComputeServer, utilize MorpheusStorageControllerService
	 * @param controllers
	 */
	public void setControllers(List<StorageController> controllers) {
		this.controllers = controllers;
		markDirty("controllers", controllers);
	}

	/**
	 * NOTE: To modify the list of snapshots associated with this ComputeServer, utilize MorpheusSnapshotService
	 * @param snapshots
	 */
	public void setSnapshots(List<SnapshotIdentityProjection> snapshots) {
		this.snapshots = snapshots;
		markDirty("snapshots", snapshots);
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

	public Boolean getHotResize() {
		return hotResize;
	}

	public void setHotResize(Boolean hotResize) {
		this.hotResize = hotResize;
		markDirty("hotResize",hotResize);
	}

	public Boolean getCpuHotResize() {
		return cpuHotResize;
	}

	public void setCpuHotResize(Boolean cpuHotResize) {
		this.cpuHotResize = cpuHotResize;
		markDirty("cpuHotResize",cpuHotResize);
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

	public Boolean getToolsInstalled() {
		return toolsInstalled;
	}

	public void setToolsInstalled(Boolean toolsInstalled) {
		this.toolsInstalled = toolsInstalled;
		markDirty("toolsInstalled",toolsInstalled);
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

	public String getConsoleType() {
		return consoleType;
	}

	public void setConsoleType(String consoleType) {
		this.consoleType = consoleType;
		markDirty("consoleType",consoleType);
	}

	public String getConsolePassword() {
		return consolePassword;
	}

	public void setConsolePassword(String consolePassword) {
		this.consolePassword = consolePassword;
		markDirty("consolePassword",consolePassword);
	}

	public Boolean getGuestConsolePreferred() {
		return guestConsolePreferred;
	}

	public void setGuestConsolePreferred(Boolean guestConsolePreferred) {
		this.guestConsolePreferred = guestConsolePreferred;
		markDirty("guestConsolePreferred", guestConsolePreferred);
	}

	public GuestConsoleType getGuestConsoleType() {
		return guestConsoleType;
	}

	public void setGuestConsoleType(GuestConsoleType guestConsoleType) {
		this.guestConsoleType = guestConsoleType;
		markDirty("guestConsoleType", guestConsoleType);
	}

	public ComputeServerIdentityProjection getParentServer() {
		return parentServer;
	}

	public void setParentServer(ComputeServerIdentityProjection parentServer) {
		this.parentServer = parentServer;
		markDirty("parentServer",parentServer);
	}

	/**
	 * Returns the ComputeStats for this server
	 * @return ComputeStats
	 */
	public ComputeStats getComputeStats() {
		return computeStats;
	}

	/**
	 * Sets the ComputeStats for this server
	 * @param computeStats The stats for this server
	 */
	public void setComputeStats(ComputeStats computeStats) {
		this.computeStats = computeStats;
		markDirty("computeStats",computeStats);
	}

	public Integer getConsolePort() {
		return consolePort;
	}

	public void setConsolePort(Integer consolePort) {
		this.consolePort = consolePort;
		markDirty("consolePort",consolePort);
	}

	public void setMetadata(List<MetadataTag> metadata) {
		this.metadata = metadata;
		markDirty("metadata", metadata);
	}

	public enum GuestConsoleType {
		disabled,
		vnc,
		rdp,
		ssh
	}
}
