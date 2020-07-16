package com.morpheusdata.model;

import java.util.Collection;
import java.util.Date;

public class Instance extends MorpheusModel {
	private String uuid;
	private String name;
	private String description;
	public String instanceTypeName;
	public String instanceTypeCode;
	public String provisionType;
	public String layoutId;
	public String layoutCode;
	public String layoutName;
	public String instanceVersion;

	public String plan;
	public String displayName;
	public String environmentPrefix;
	public String hostname;
	//	public String domainName;
//	public String assignedDomainName;
	public Boolean firewallEnabled;
	public String status;
	public String userStatus;
	public String scheduleStatus;
	public String networkLevel;
	public String instanceLevel;
	public String deployGroup;
	public String instanceContext;
	public Boolean autoScale;
	public String statusMessage;
	public Date expireDate;
	public String tags;
	public Long maxStorage;
	public Long maxMemory;
	public Long maxCores;
	public Long configId;
	public String configGroup;
	public String configRole;
	//	public String ports;
	public String serviceUsername;
	public String servicePassword;


	private Collection<Workload> containers;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public Collection<Workload> getContainers() {
		return containers;
	}

	public void setContainers(Collection<Workload> containers) {
		this.containers = containers;
		markDirty("workloads", containers);
	}

	public String getInstanceTypeName() {
		return instanceTypeName;
	}

	public void setInstanceTypeName(String instanceTypeName) {
		this.instanceTypeName = instanceTypeName;
	}

	public String getInstanceTypeCode() {
		return instanceTypeCode;
	}

	public void setInstanceTypeCode(String instanceTypeCode) {
		this.instanceTypeCode = instanceTypeCode;
	}

	public String getProvisionType() {
		return provisionType;
	}

	public void setProvisionType(String provisionType) {
		this.provisionType = provisionType;
	}

	public String getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(String layoutId) {
		this.layoutId = layoutId;
	}

	public String getLayoutCode() {
		return layoutCode;
	}

	public void setLayoutCode(String layoutCode) {
		this.layoutCode = layoutCode;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getInstanceVersion() {
		return instanceVersion;
	}

	public void setInstanceVersion(String instanceVersion) {
		this.instanceVersion = instanceVersion;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEnvironmentPrefix() {
		return environmentPrefix;
	}

	public void setEnvironmentPrefix(String environmentPrefix) {
		this.environmentPrefix = environmentPrefix;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Boolean getFirewallEnabled() {
		return firewallEnabled;
	}

	public void setFirewallEnabled(Boolean firewallEnabled) {
		this.firewallEnabled = firewallEnabled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(String scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public String getNetworkLevel() {
		return networkLevel;
	}

	public void setNetworkLevel(String networkLevel) {
		this.networkLevel = networkLevel;
	}

	public String getInstanceLevel() {
		return instanceLevel;
	}

	public void setInstanceLevel(String instanceLevel) {
		this.instanceLevel = instanceLevel;
	}

	public String getDeployGroup() {
		return deployGroup;
	}

	public void setDeployGroup(String deployGroup) {
		this.deployGroup = deployGroup;
	}

	public String getInstanceContext() {
		return instanceContext;
	}

	public void setInstanceContext(String instanceContext) {
		this.instanceContext = instanceContext;
	}

	public Boolean getAutoScale() {
		return autoScale;
	}

	public void setAutoScale(Boolean autoScale) {
		this.autoScale = autoScale;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		this.maxMemory = maxMemory;
	}

	public Long getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(Long maxCores) {
		this.maxCores = maxCores;
	}

	public Long getConfigId() {
		return configId;
	}

	public void setConfigId(Long configId) {
		this.configId = configId;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public String getConfigRole() {
		return configRole;
	}

	public void setConfigRole(String configRole) {
		this.configRole = configRole;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
	}
}
