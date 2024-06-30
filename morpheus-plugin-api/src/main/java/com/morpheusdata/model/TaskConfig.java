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
import java.util.List;
import java.util.Map;

/**
 * Details as may be relevant for executing a Task on an {@link Instance}, {@link Container}, or {@link ComputeServer}
 *
 * @author Mike Truso
 */
public class TaskConfig {
	public Long instanceId;
	public Long containerId;
	public Long serverId;
	public String instanceName;
	public String serverName;
	public String containerName;
	public String internalIp;
	public String externalIp;
	public String host;
	public String port;
	public String localScriptGitRef;
	public List results;
	public Map customOptions;
	public Map evars;
	public String groupName;
	public String groupCode;
	public String cloudName;
	public String cloudCode;
	public String type;

	public String userId;
	public String userDisplayName;
	public String username;
	public String userInitials;
	public String accountId;
	public String account;
	public String tenant;
	public String tenantId;
	public String tenantSubdomain;
	public String accountType;
	public String sequence;
	public String platform;

	public InstanceConfig instance;
	public ContainerConfig container;
	public ServerConfig server;
	public Zone zone;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
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

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getLocalScriptGitRef() {
		return localScriptGitRef;
	}

	public void setLocalScriptGitRef(String localScriptGitRef) {
		this.localScriptGitRef = localScriptGitRef;
	}

	public List getResults() {
		return results;
	}

	public void setResults(List results) {
		this.results = results;
	}

	public Map getCustomOptions() {
		return customOptions;
	}

	public void setCustomOptions(Map customOptions) {
		this.customOptions = customOptions;
	}

	public Map getEvars() {
		return evars;
	}

	public void setEvars(Map evars) {
		this.evars = evars;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getCloudName() {
		return cloudName;
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserDisplayName() {
		return userDisplayName;
	}

	public void setUserDisplayName(String userDisplayName) {
		this.userDisplayName = userDisplayName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserInitials() {
		return userInitials;
	}

	public void setUserInitials(String userInitials) {
		this.userInitials = userInitials;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantSubdomain() {
		return tenantSubdomain;
	}

	public void setTenantSubdomain(String tenantSubdomain) {
		this.tenantSubdomain = tenantSubdomain;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public InstanceConfig getInstance() {
		return instance;
	}

	public void setInstance(InstanceConfig instance) {
		this.instance = instance;
	}

	public ContainerConfig getContainer() {
		return container;
	}

	public void setContainer(ContainerConfig container) {
		this.container = container;
	}

	public ServerConfig getServer() {
		return server;
	}

	public void setServer(ServerConfig server) {
		this.server = server;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public static class InstanceConfig {
		public Long id;
		public String instanceTypeName;
		public String instanceTypeCode;
		public String provisionType;
		public String layoutId;
		public String layoutCode;
		public String layoutName;
		public String instanceVersion;

		public String plan;
		public String name;
		public String displayName;
		public String description;
		public String environmentPrefix;
		public String hostname;
		public String domainName;
		public String assignedDomainName;
		public String firewallEnabled;
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
		public Long storage;
		public Long memory;
		public Long cores;
		public Long configId;
		public String configGroup;
		public String configRole;
		public String ports;
		public Boolean sslEnabled;
		public Long sslCertId;
		public String serviceUsername;
		public String servicePassword;
		public String adminUsername;
		public String adminPassword;
		public String createdByUsername;
		public String createdByEmail;
		public String createdByFirstName;
		public String createdByLastName;
		public Map evars;
		public Map metadata;
		public List<ContainerConfig> containers;
	}

	public static class ContainerConfig {
		public Long id;
		public String name;
		public String containerTypeName;
		public String containerTypeCode;
		public String containerTypeShortName;
		public String containerTypeCategory;
		public String provisionType;
		public String dataPath;
		public String logsPath;
		public String configPath;
		public String planCode;
		public Date dateCreated;
		public String status;
		public String environmentPrefix;
		public String version;
		public String image;
		public String internalHostname;
		public String hostname;
		public String domainName;
		public Long storage;
		public Long memory;
		public Long cores;
		public String internalIp;
		public String externalIp;
		public String sshHost;
		public String hostMountPoint;
		public Long configId;
		public String configGroup;
		public String configRole;
		public String certificatePath;
		public String certificateStyle;
		public List ports;
		public Map portMap;
		public String internalPort;
		public String externalPort;
		public Long serverId;
		public ServerConfig server;
	}

	public static class ServerConfig {
		public Long id;
		public String serverTypeName;
		public String serverTypeCode;
		public String computeTypeName;
		public String computeTypeCode;
		public Long parentServerId;
		public String plan;
		public String visibility;
		public String osTypeCode;
		public Long sourceImageId;
		public String name;
		public String displayName;
		public String internalName;
		public String category;
		public String description;
		public String internalId;
		public String externalId;
		public String platform;
		public String platformVersion;
		public String agentVersion;
		public String nodePackageVersion;
		public String sshHost;
		public String sshPort;
		public String sshUsername;
		public String consoleType;
		public String consoleHost;
		public String consolePort;
		public String consoleUsername;
		public String internalSshUsername;
		public String internalIp;
		public String externalIp;
		public String osDevice;
		public String dataDevice;
		public Boolean lvmEnabled;
		public String apiKey;
		public Boolean softwareRaid;
		public String status;
		public String powerState;
		public Date dateCreated;
		public String lastAgentUpdate;
		public String serverType;
		public String osType;
		public String commType;
		public Boolean managed;
		public Boolean agentInstalled;
		public Boolean toolsInstalled;
		public String hostname;
		public String domainName;
		public String fqdn;
		public String statusMessage;
		public Long maxStorage;
		public Long maxMemory;
		public Long maxCores;
		public String macAddress;
		public String serverVendor;
		public String serverModel;
		public String serialNumber;
		public String tags;
		public String configId;
		public String configGroup;
		public String configRole;
		public List<Volume> volumes;
	}

	public static class Volume {
		public Long id;
		public String name;
		public String deviceName;
		public Long maxStorage;
		public String unitNumber;
		public Long displayOrder;
		public Boolean rootVolume;
	}

	public static class Zone {
		public String name;
		public String code;
		public String location;
		public String cloudTypeName;
		public String cloudTypeCode;
		public String domainName;
		public String scalePriority;
		public Boolean firewallEnabled;
		public String regionCode;
		public String agentMode;
		public String datacenterId;
	}
}
