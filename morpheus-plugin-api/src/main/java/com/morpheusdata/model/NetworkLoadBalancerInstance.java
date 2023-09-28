package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.LoadBalancerInstanceIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.*;

public class NetworkLoadBalancerInstance extends LoadBalancerInstanceIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Instance instance;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeServerGroup serverGroup;
	protected String description;
	protected String internalId;
	protected String externalId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean active = true;
	protected Boolean sticky = false;
	protected Boolean sslEnabled;
	protected Boolean externalAddress = false;
	protected Integer backendPort; //tracking the original port form the container def
	protected String vipType; //forwarding,http,performance etc
	protected String vipHostname; //front facing ip being load balanced
	protected String vipProtocol; //http, tcp, https - matches port protocols
	protected String vipScheme; //internal/external for amazon
	protected String vipMode; //style - http, https passthrough or https terminated - passthrough, terminated, endtoend - computed
	protected String vipPortRange;
	protected String vipSticky; //mode of the sticky session persistence;
	protected String vipBalance; //balancing mode;
	protected Integer servicePort; //backend port for load balancing;
	protected String sourceAddress;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountCertificate sslCert; //cert for the vip
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountCertificate sslServerCert; // cert for the vip.. server side (see NSX-T)
	protected String sslMode;
	protected String sslRedirectMode;
	protected Boolean vipShared = false; //is the vip a shared ip with irules
	protected String vipDirectAddress; //direct address to service if in a shared vip mode
	protected String serverName;
	protected String poolName;
	protected Boolean removing = false;
	protected String serviceName;
	protected String virtualServiceName;
	protected String vipSource = "user"; //user or sync
	protected String extraConfig;
	protected String serviceAccess;
	protected String networkId;
	protected String subnetId;
	protected String externalPortId;
	protected String status; //ok, error, pending
	protected String statusMessage;
	protected String vipStatus; //online, offline
	protected Date statusDate;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancerInstance parentInstance;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancerPool pool;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancerMonitor monitor; //this might need to be 1 to many
	protected Integer connectionLimit;
	protected String policiesHash; // tracking changes to policies
	protected String partition;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancer loadBalancer;

	// the hasMany fields
	protected List<CloudPool> assignedZonePools = new ArrayList<CloudPool>();
	protected List<NetworkLoadBalancerPolicy> policies = new ArrayList<NetworkLoadBalancerPolicy>();
	protected List<NetworkLoadBalancerScript> scripts = new ArrayList<NetworkLoadBalancerScript>();
	protected List<NetworkLoadBalancerProfile> profiles = new ArrayList<NetworkLoadBalancerProfile>();
	protected List<Container> containers = new ArrayList<Container>();

	// this property will hold additional properties that may be useful to the api integrators
	protected Map<String, Object> holder = new HashMap<String, Object>();


	public Map<String, Object> getHolder() {
		return this.holder;
	}
	public void setHolder(Map<String, Object> map) {
		this.holder = map;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
		markDirty("instance", instance);
	}

	public ComputeServerGroup getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(ComputeServerGroup serverGroup) {
		this.serverGroup = serverGroup;
		markDirty("serverGroup", serverGroup);
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getSticky() {
		return sticky;
	}

	public void setSticky(Boolean sticky) {
		this.sticky = sticky;
		markDirty("sticky", sticky);
	}

	public Boolean getSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(Boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
		markDirty("sslEnabled", sslEnabled);
	}

	public Boolean getExternalAddress() {
		return externalAddress;
	}

	public void setExternalAddress(Boolean externalAddress) {
		this.externalAddress = externalAddress;
		markDirty("externalAddress", externalAddress);
	}

	public Integer getBackendPort() {
		return backendPort;
	}

	public void setBackendPort(Integer backendPort) {
		this.backendPort = backendPort;
		markDirty("backendPort", backendPort);
	}

	public String getVipType() {
		return vipType;
	}

	public void setVipType(String vipType) {
		this.vipType = vipType;
		markDirty("vipType", vipType);
	}

	public String getVipHostname() {
		return vipHostname;
	}

	public void setVipHostname(String vipHostname) {
		this.vipHostname = vipHostname;
		markDirty("vipHostname", vipHostname);
	}

	public String getVipProtocol() {
		return vipProtocol;
	}

	public void setVipProtocol(String vipProtocol) {
		this.vipProtocol = vipProtocol;
		markDirty("vipProtocol", vipProtocol);
	}

	public String getVipScheme() {
		return vipScheme;
	}

	public void setVipScheme(String vipScheme) {
		this.vipScheme = vipScheme;
		markDirty("vipScheme", vipScheme);
	}

	public String getVipMode() {
		return vipMode;
	}

	public void setVipMode(String vipMode) {
		this.vipMode = vipMode;
		markDirty("vipMode", vipMode);
	}

	public String getVipPortRange() {
		return vipPortRange;
	}

	public void setVipPortRange(String vipPortRange) {
		this.vipPortRange = vipPortRange;
		markDirty("vipPortRange", vipPortRange);
	}

	public String getVipSticky() {
		return vipSticky;
	}

	public void setVipSticky(String vipSticky) {
		this.vipSticky = vipSticky;
		markDirty("vipSticky", vipSticky);
	}

	public String getVipBalance() {
		return vipBalance;
	}

	public void setVipBalance(String vipBalance) {
		this.vipBalance = vipBalance;
		markDirty("vipBalance", vipBalance);
	}

	public Integer getServicePort() {
		return servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
		markDirty("servicePort", servicePort);
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
		markDirty("sourceAddress", sourceAddress);
	}

	public AccountCertificate getSslCert() {
		return sslCert;
	}

	public void setSslCert(AccountCertificate sslCert) {
		this.sslCert = sslCert;
		markDirty("sslCert", sslCert);
	}

	public AccountCertificate getSslServerCert() {
		return sslServerCert;
	}

	public void setSslServerCert(AccountCertificate sslServerCert) {
		this.sslServerCert = sslServerCert;
		markDirty("sslServerCert", sslServerCert);
	}

	public String getSslMode() {
		return sslMode;
	}

	public void setSslMode(String sslMode) {
		this.sslMode = sslMode;
		markDirty("sslMode", sslMode);
	}

	public String getSslRedirectMode() {
		return sslRedirectMode;
	}

	public void setSslRedirectMode(String sslRedirectMode) {
		this.sslRedirectMode = sslRedirectMode;
		markDirty("sslRedirectMode", sslRedirectMode);
	}

	public Boolean getVipShared() {
		return vipShared;
	}

	public void setVipShared(Boolean vipShared) {
		this.vipShared = vipShared;
		markDirty("vipShared", vipShared);
	}

	public String getVipDirectAddress() {
		return vipDirectAddress;
	}

	public void setVipDirectAddress(String vipDirectAddress) {
		this.vipDirectAddress = vipDirectAddress;
		markDirty("vipDirectAccess", vipDirectAddress);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
		markDirty("serverName", serverName);
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
		markDirty("poolName", poolName);
	}

	public Boolean getRemoving() {
		return removing;
	}

	public void setRemoving(Boolean removing) {
		this.removing = removing;
		markDirty("removing", removing);
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
		markDirty("serviceName", serviceName);
	}

	public String getVirtualServiceName() {
		return virtualServiceName;
	}

	public void setVirtualServiceName(String virtualServiceName) {
		this.virtualServiceName = virtualServiceName;
		markDirty("virtualServiceName", virtualServiceName);
	}

	public String getVipSource() {
		return vipSource;
	}

	public void setVipSource(String vipSource) {
		this.vipSource = vipSource;
		markDirty("vipSource", vipSource);
	}

	public String getExtraConfig() {
		return extraConfig;
	}

	public void setExtraConfig(String extraConfig) {
		this.extraConfig = extraConfig;
		markDirty("extraConfig", extraConfig);
	}

	public String getServiceAccess() {
		return serviceAccess;
	}

	public void setServiceAccess(String serviceAccess) {
		this.serviceAccess = serviceAccess;
		markDirty("serviceAccess", serviceAccess);
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
		markDirty("networkId", networkId);
	}

	public String getSubnetId() {
		return subnetId;
	}

	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
		markDirty("subnetId", subnetId);
	}

	public String getExternalPortId() {
		return externalPortId;
	}

	public void setExternalPortId(String externalPortId) {
		this.externalPortId = externalPortId;
		markDirty("externalPortId", externalPortId);
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

	public String getVipStatus() {
		return vipStatus;
	}

	public void setVipStatus(String vipStatus) {
		this.vipStatus = vipStatus;
		markDirty("vipStatus", vipStatus);
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
		markDirty("statusDate", statusDate);
	}

	public NetworkLoadBalancerInstance getParentInstance() {
		return parentInstance;
	}

	public void setParentInstance(NetworkLoadBalancerInstance parentInstance) {
		this.parentInstance = parentInstance;
		markDirty("parentInstance", parentInstance);
	}

	public NetworkLoadBalancerPool getPool() {
		return pool;
	}

	public void setPool(NetworkLoadBalancerPool pool) {
		this.pool = pool;
		markDirty("pool", pool);
	}

	public NetworkLoadBalancerMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(NetworkLoadBalancerMonitor monitor) {
		this.monitor = monitor;
		markDirty("monitor", monitor);
	}

	public Integer getConnectionLimit() {
		return connectionLimit;
	}

	public void setConnectionLimit(Integer connectionLimit) {
		this.connectionLimit = connectionLimit;
		markDirty("connectionLimit", connectionLimit);
	}

	public String getPoliciesHash() {
		return policiesHash;
	}

	public void setPoliciesHash(String policiesHash) {
		this.policiesHash = policiesHash;
		markDirty("policiesHash", policiesHash);
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
	}

	public NetworkLoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
		markDirty("loadBalancer", loadBalancer);
	}

	public List<CloudPool> getAssignedZonePools() {
		return assignedZonePools;
	}

	public void setAssignedZonePools(List<CloudPool> assignedZonePools) {
		this.assignedZonePools = assignedZonePools;
		markDirty("assignedZonePools", assignedZonePools);
	}

	public List<NetworkLoadBalancerPolicy> getPolicies() {
		return policies;
	}

	public void setPolicies(List<NetworkLoadBalancerPolicy> policies) {
		this.policies = policies;
		markDirty("policies", policies);
	}

	public void addToPolicies(NetworkLoadBalancerPolicy policy) {
		this.policies.add(policy);
		markDirty("policy", policy);
	}

	public List<NetworkLoadBalancerScript> getScripts() {
		return scripts;
	}

	public void setScripts(List<NetworkLoadBalancerScript> scripts) {
		this.scripts = scripts;
		markDirty("scripts", scripts);
	}

	public List<NetworkLoadBalancerProfile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<NetworkLoadBalancerProfile> profiles) {
		this.profiles = profiles;
		markDirty("profiles", profiles);
	}

	public void addToProfiles(NetworkLoadBalancerProfile profile) {
		this.profiles.add(profile);
		markDirty("profiles", profile);
	}

	public List<Container> getContainers() {
		return containers;
	}

	public void setContainers(List<Container> containers) {
		this.containers = containers;
		markDirty("containers", containers);
	}
}
