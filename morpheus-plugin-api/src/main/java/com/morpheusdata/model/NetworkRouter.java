package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.NetworkIdentityProjection;
import com.morpheusdata.model.projection.NetworkRouterIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class NetworkRouter extends NetworkRouterIdentityProjection {
	protected Account owner;
	protected String code;
	protected String category;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkRouterType type;
	protected String routerType = "internal";
	protected String name;
	protected String description;
	//linking
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkServer networkServer;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Cloud cloud;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeSite site;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Instance instance;
	//config options
	protected String datacenterId;
	protected String tenantName;
	protected String poolId;
	protected String datastoreId;
	protected String hostId;
	protected String folderId;
	protected String sizeId;
	protected String hostname;
	protected String fqdn;
	protected String domainName;
	protected String config;
	//integration
	protected String internalIp;
	protected String externalIp;
	protected String managementIp;
	protected String internalId;
	protected String externalId;
	protected String providerId;
	protected String updateId;
	protected String availabilityZone;
	//active
	protected Boolean enableSnat = true;
	protected Boolean enabled = true;
	//creds
	protected String sshUsername;
	protected String sshPassword;
	//network config
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkIdentityProjection externalNetwork;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSubnet externalSubnet;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkIdentityProjection internalNetwork;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSubnet internalSubnet;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkIdentityProjection managementNetwork;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSubnet managementSubnet;
	protected Integer externalIndex = 0;
	protected Integer internalIndex = 1;
	protected String networkSource; //this is for mixing docker, kube, kvm etc inside the same zone
	protected String iacId; //id for infrastructure as code integrations
	protected String rawData;
	//routing config
	protected Boolean enableRouting = false;
	protected Boolean enableBgp = false;
	protected Boolean enableOspf = false;
	protected Boolean enableEcmp = false;
	protected String defaultGateway;
	protected String routerId;
	protected Long parentRouterId;
	protected String localAs;
	//nat config
	protected Boolean enableNat = false;
	// ha config
	protected Boolean enableHa = false;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkRouterInterface haInterface;
	protected String haIpAddress1;
	protected String haIpAddress2;
	protected Integer haDeclareDeadTime;
	protected Boolean enableHaLogging = false;
	protected String haLogLevel; //error, info, debug, etc
	//dhcpConfig
	protected Boolean enableDhcp = false;
	protected Boolean enableDhcpLogging = false;
	protected String dhcpLogLevel; //error, info, debug, etc
	//firewall config
	//vpn config
	//status and sync
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected Date statusDate;
	protected Date lastSync;
	protected Date nextRunDate;
	protected Long lastSyncDuration;
	protected String lastStats;
	//audit
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Long createdById;
	protected String createdByName;
	protected String visibility = "private";

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
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

	public NetworkRouterType getType() {
		return type;
	}

	public void setType(NetworkRouterType type) {
		this.type = type;
	}

	public String getRouterType() {
		return routerType;
	}

	public void setRouterType(String routerType) {
		this.routerType = routerType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public NetworkServer getNetworkServer() {
		return networkServer;
	}

	public void setNetworkServer(NetworkServer networkServer) {
		this.networkServer = networkServer;
	}

	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
	}

	public ComputeSite getSite() {
		return site;
	}

	public void setSite(ComputeSite site) {
		this.site = site;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public String getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(String datacenterId) {
		this.datacenterId = datacenterId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public String getDatastoreId() {
		return datastoreId;
	}

	public void setDatastoreId(String datastoreId) {
		this.datastoreId = datastoreId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getSizeId() {
		return sizeId;
	}

	public void setSizeId(String sizeId) {
		this.sizeId = sizeId;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Override
	public String getConfig() {
		return config;
	}

	@Override
	public void setConfig(String config) {
		this.config = config;
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

	public String getManagementIp() {
		return managementIp;
	}

	public void setManagementIp(String managementIp) {
		this.managementIp = managementIp;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	@Override
	public String getExternalId() {
		return externalId;
	}

	@Override
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}



	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
	}

	public Boolean getEnableSnat() {
		return enableSnat;
	}

	public void setEnableSnat(Boolean enableSnat) {
		this.enableSnat = enableSnat;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getSshUsername() {
		return sshUsername;
	}

	public void setSshUsername(String sshUsername) {
		this.sshUsername = sshUsername;
	}

	public String getSshPassword() {
		return sshPassword;
	}

	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
	}

	public NetworkIdentityProjection getExternalNetwork() {
		return externalNetwork;
	}

	public void setExternalNetwork(NetworkIdentityProjection externalNetwork) {
		this.externalNetwork = externalNetwork;
	}

	public NetworkSubnet getExternalSubnet() {
		return externalSubnet;
	}

	public void setExternalSubnet(NetworkSubnet externalSubnet) {
		this.externalSubnet = externalSubnet;
	}

	public NetworkIdentityProjection getInternalNetwork() {
		return internalNetwork;
	}

	public void setInternalNetwork(NetworkIdentityProjection internalNetwork) {
		this.internalNetwork = internalNetwork;
	}

	public NetworkSubnet getInternalSubnet() {
		return internalSubnet;
	}

	public void setInternalSubnet(NetworkSubnet internalSubnet) {
		this.internalSubnet = internalSubnet;
	}

	public NetworkIdentityProjection getManagementNetwork() {
		return managementNetwork;
	}

	public void setManagementNetwork(NetworkIdentityProjection managementNetwork) {
		this.managementNetwork = managementNetwork;
	}

	public NetworkSubnet getManagementSubnet() {
		return managementSubnet;
	}

	public void setManagementSubnet(NetworkSubnet managementSubnet) {
		this.managementSubnet = managementSubnet;
	}

	public Integer getExternalIndex() {
		return externalIndex;
	}

	public void setExternalIndex(Integer externalIndex) {
		this.externalIndex = externalIndex;
	}

	public Integer getInternalIndex() {
		return internalIndex;
	}

	public void setInternalIndex(Integer internalIndex) {
		this.internalIndex = internalIndex;
	}

	public String getNetworkSource() {
		return networkSource;
	}

	public void setNetworkSource(String networkSource) {
		this.networkSource = networkSource;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public Boolean getEnableRouting() {
		return enableRouting;
	}

	public void setEnableRouting(Boolean enableRouting) {
		this.enableRouting = enableRouting;
	}

	public Boolean getEnableBgp() {
		return enableBgp;
	}

	public void setEnableBgp(Boolean enableBgp) {
		this.enableBgp = enableBgp;
	}

	public Boolean getEnableOspf() {
		return enableOspf;
	}

	public void setEnableOspf(Boolean enableOspf) {
		this.enableOspf = enableOspf;
	}

	public Boolean getEnableEcmp() {
		return enableEcmp;
	}

	public void setEnableEcmp(Boolean enableEcmp) {
		this.enableEcmp = enableEcmp;
	}

	public String getDefaultGateway() {
		return defaultGateway;
	}

	public void setDefaultGateway(String defaultGateway) {
		this.defaultGateway = defaultGateway;
	}

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}

	public Long getParentRouterId() {
		return parentRouterId;
	}

	public void setParentRouterId(Long parentRouterId) {
		this.parentRouterId = parentRouterId;
	}

	public String getLocalAs() {
		return localAs;
	}

	public void setLocalAs(String localAs) {
		this.localAs = localAs;
	}

	public Boolean getEnableNat() {
		return enableNat;
	}

	public void setEnableNat(Boolean enableNat) {
		this.enableNat = enableNat;
	}

	public Boolean getEnableHa() {
		return enableHa;
	}

	public void setEnableHa(Boolean enableHa) {
		this.enableHa = enableHa;
	}

	public NetworkRouterInterface getHaInterface() {
		return haInterface;
	}

	public void setHaInterface(NetworkRouterInterface haInterface) {
		this.haInterface = haInterface;
	}

	public String getHaIpAddress1() {
		return haIpAddress1;
	}

	public void setHaIpAddress1(String haIpAddress1) {
		this.haIpAddress1 = haIpAddress1;
	}

	public String getHaIpAddress2() {
		return haIpAddress2;
	}

	public void setHaIpAddress2(String haIpAddress2) {
		this.haIpAddress2 = haIpAddress2;
	}

	public Integer getHaDeclareDeadTime() {
		return haDeclareDeadTime;
	}

	public void setHaDeclareDeadTime(Integer haDeclareDeadTime) {
		this.haDeclareDeadTime = haDeclareDeadTime;
	}

	public Boolean getEnableHaLogging() {
		return enableHaLogging;
	}

	public void setEnableHaLogging(Boolean enableHaLogging) {
		this.enableHaLogging = enableHaLogging;
	}

	public String getHaLogLevel() {
		return haLogLevel;
	}

	public void setHaLogLevel(String haLogLevel) {
		this.haLogLevel = haLogLevel;
	}

	public Boolean getEnableDhcp() {
		return enableDhcp;
	}

	public void setEnableDhcp(Boolean enableDhcp) {
		this.enableDhcp = enableDhcp;
	}

	public Boolean getEnableDhcpLogging() {
		return enableDhcpLogging;
	}

	public void setEnableDhcpLogging(Boolean enableDhcpLogging) {
		this.enableDhcpLogging = enableDhcpLogging;
	}

	public String getDhcpLogLevel() {
		return dhcpLogLevel;
	}

	public void setDhcpLogLevel(String dhcpLogLevel) {
		this.dhcpLogLevel = dhcpLogLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getLastStats() {
		return lastStats;
	}

	public void setLastStats(String lastStats) {
		this.lastStats = lastStats;
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

	public String getCreatedByName() {
		return createdByName;
	}

	public void setCreatedByName(String createdByName) {
		this.createdByName = createdByName;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
}
