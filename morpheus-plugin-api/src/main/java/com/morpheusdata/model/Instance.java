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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.InstanceIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Instance extends InstanceIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	private String uuid;
	private String description;
	public String instanceTypeName;
	public String instanceTypeCode;
	public String provisionType;
	public String layoutId;
	public String layoutCode;
	public String layoutName;
	public String instanceVersion;
	public String unformattedName;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public ServicePlan plan;
	public String displayName;
	public String environmentPrefix;
	public String hostName;
	//public String domainName;
	//public String assignedDomainName;
	public Boolean firewallEnabled = true;
	public String status;
	public String userStatus;
	public String scheduleStatus;
	public String networkLevel;
	public String instanceLevel;
	public String deployGroup;
	public String instanceContext;
	public InstanceScale scale;
	public Boolean autoScale = false;
	public String statusMessage;
	public Date expireDate;
	public String tags;
	public Long maxStorage;
	public Long maxMemory;
	public Long maxCores;
	public Long coresPerSocket;
	public Long configId;
	public String configGroup;
	public String configRole;
	public String externalId;
	//	public String ports;
	public String serviceUsername;
	public String servicePassword;
	public String iacId;
	public Long provisionZoneId;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public CloudPool resourcePool;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public InstanceTypeLayout layout;
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	public Collection<Workload> containers;
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	public Collection<AccountResource> resources;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeServerGroup serverGroup;
	public Collection<ResourceSpec> specs;
	public NetworkDomain networkDomain;
	public ComputeSite site;
	public UserGroup userGroup;
	protected User createdBy;
	public List<UserGroup> userGroups;
	protected List<MetadataTag> metadata;

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
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

	public ServicePlan getPlan() {
		return plan;
	}

	public void setPlan(ServicePlan plan) {
		this.plan = plan;
	}
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUnformattedName() {
		return unformattedName;
	}

	public void setUnformattedName(String unformattedName) {
		this.unformattedName = unformattedName;
	}

	public String getEnvironmentPrefix() {
		return environmentPrefix;
	}

	public void setEnvironmentPrefix(String environmentPrefix) {
		this.environmentPrefix = environmentPrefix;
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

	public InstanceScale getScale() { return scale; }

	public void setScale(InstanceScale scale) {
		this.scale = scale;
		markDirty("scale", scale);
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

	public Long getCoresPerSocket() { return coresPerSocket; }

	public void setCoresPerSocket(Long coresPerSocket) {
		this.coresPerSocket = coresPerSocket;
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

	public List<MetadataTag> getMetadata() { return metadata;}

	public void setMetadata(List<MetadataTag> metadata) {
		this.metadata = metadata;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ComputeServerGroup getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(ComputeServerGroup serverGroup) {
		this.serverGroup = serverGroup;
		markDirty("serverGroup", serverGroup);
	}

	public Collection<ResourceSpec> getSpecs() {
		return specs;
	}

	public void setSpecs(Collection<ResourceSpec> specs) {
		this.specs = specs;
		markDirty("specs", specs);
	}

	public Collection<AccountResource> getResources() {
		return resources;
	}

	public void setResources(Collection<AccountResource> resources) {
		this.resources = resources;
		markDirty("resources", resources);
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public ComputeSite getSite() {
		return site;
	}

	public void setSite(ComputeSite site) {
		this.site = site;
		markDirty("site", site);
	}

	public Long getProvisionZoneId() {
		return provisionZoneId;
	}

	public void setProvisionZoneId(Long provisionZoneId) {
		this.provisionZoneId = provisionZoneId;
		markDirty("provisionZoneId", provisionZoneId);
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
		markDirty("hostName", hostName);
	}

	public CloudPool getResourcePool() {
		return resourcePool;
	}

	public void setResourcePool(CloudPool resourcePool) {
		this.resourcePool = resourcePool;
		markDirty("resourcePool", resourcePool);
	}

	public InstanceTypeLayout getLayout() {
		return layout;
	}

	public void setLayout(InstanceTypeLayout layout) {
		this.layout = layout;
		markDirty("layout", layout);
	}

	public NetworkDomain getNetworkDomain() {
		return networkDomain;
	}

	public void setNetworkDomain(NetworkDomain networkDomain) {
		this.networkDomain = networkDomain;
		markDirty("networkDomain", networkDomain);
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
		markDirty("userGroup", userGroup);
	}

	public List<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
		markDirty("userGroups", userGroups);
	}



	public enum Status {
		pending,
		denied,
		cancelled,
		provisioning,
		finishing, //Used if there are instance post processing tasks
		failed,
		resizing,
		running,
		warning,
		stopped,
		suspended,
		removing,
		restarting,
		cloning,
		restoring,
		stopping,
		starting,
		suspending,
		pendingRemoval,
		unknown
	}
}
