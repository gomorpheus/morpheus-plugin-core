package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.LoadBalancerNodeIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkLoadBalancerNode extends LoadBalancerNodeIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String visibility = "public"; //['public', 'private']
	protected String description;
	protected String ipAddress;
	protected Integer port;
	protected String portType;
	protected String monitorPort;
	protected Integer weight;
	protected String nodeState = "enabled"; //disabled,offline
	protected String internalId;
	protected String config;
	protected String rawData;
	protected Boolean enabled = true;
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected Date statusDate;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeServer server;
	protected Long instanceId;
	protected Long containerId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String nodeSource = "user"; //user or sync

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancerMonitor monitor;
	protected Integer maxConnections;
	// nodes that reference something other than an IP: e.g. NSX Security Group;
	protected String externalRefType;
	protected String externalRefId;
	protected String externalRefName;
	protected String partition;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancer loadBalancer;

	protected List<CloudPool> assignedZonePools = new ArrayList<CloudPool>();

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		markDirty("ipAddress", ipAddress);
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
		markDirty("port", port);
	}

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
		markDirty("portType", portType);
	}

	public String getMonitorPort() {
		return monitorPort;
	}

	public void setMonitorPort(String monitorPort) {
		this.monitorPort = monitorPort;
		markDirty("monitorPort", monitorPort);
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
		markDirty("weight", weight);
	}

	public String getNodeState() {
		return nodeState;
	}

	public void setNodeState(String nodeState) {
		this.nodeState = nodeState;
		markDirty("nodeState", nodeState);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
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

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
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

	public ComputeServer getServer() {
		return server;
	}

	public void setServer(ComputeServer server) {
		this.server = server;
		markDirty("server", server);
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
		markDirty("instanceId", instanceId);
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
		markDirty("containerId", containerId);
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

	public String getNodeSource() {
		return nodeSource;
	}

	public void setNodeSource(String nodeSource) {
		this.nodeSource = nodeSource;
		markDirty("nodeSource", nodeSource);
	}

	public NetworkLoadBalancerMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(NetworkLoadBalancerMonitor monitor) {
		this.monitor = monitor;
		markDirty("monitor", monitor);
	}

	public Integer getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(Integer maxConnections) {
		this.maxConnections = maxConnections;
		markDirty("maxConnections", maxConnections);
	}

	public String getExternalRefType() {
		return externalRefType;
	}

	public void setExternalRefType(String externalRefType) {
		this.externalRefType = externalRefType;
		markDirty("externalRefType", externalRefType);
	}

	public String getExternalRefId() {
		return externalRefId;
	}

	public void setExternalRefId(String externalRefId) {
		this.externalRefId = externalRefId;
		markDirty("externalRefId", externalRefId);
	}

	public String getExternalRefName() {
		return externalRefName;
	}

	public void setExternalRefName(String externalRefName) {
		this.externalRefName = externalRefName;
		markDirty("externalRefName", externalRefName);
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
}
