package com.morpheusdata.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TaskConfig {
	// type config
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

	public Long userId;
	public String userDisplayName;
	public String username;
	public InstanceConfig instance;
	public Long accountId;
//	public Cypher cypher;

//	public TaskConfig (Map map) {
//		instanceId = (Long) map.get("instanceId");
//		containerId = (Long) map.get("containerId");
//		serverId = (Long) map.get("serverId");
//		instanceName = (String) map.get("instanceId");
//		serverName = (String) map.get("serverName");
//		containerName = (String) map.get("containerName");
//		internalIp = (String) map.get("internalIp");
//		externalIp = (String) map.get("externalIp");
//		userId = (Long) map.get("userId");
//		userDisplayName = (String) map.get("userDisplayName");
//		username = (String) map.get("username");
//		customOptions = (Map) map.get("customOptions");
//		instance = new InstanceConfig((Map)map.get("instance"));
//	}

	public class InstanceConfig {
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
//		public String servicePassword;
		public String adminUsername;
//		public String adminPassword;
		public String createdByUsername;
		public String createdByEmail;
		public String createdByFirstName;
		public String createdByLastName;
		public Map evars;
		public Map metadata;


//		public InstanceConfig(Map map) {
//			this.id = (Long) map.get("id");
//			this.instanceTypeName = (String) map.get("instanceTypeName");
//			this.instanceTypeCode = (String) map.get("instanceTypeCode");
//			this.provisionType = (String) map.get("provisionType");
//			this.layoutId = (String) map.get("layoutId");
//			this.layoutCode = (String) map.get("layoutCode");
//			this.layoutName = (String) map.get("layoutName");
//			this.instanceVersion = (String) map.get("instanceVersion");
//			this.plan = (String) map.get("plan");
//			this.name = (String) map.get("name");
//			this.displayName = (String) map.get("displayName");
//			this.description = (String) map.get("description");
//			this.environmentPrefix = (String) map.get("environmentPrefix");
//			this.hostname = (String) map.get("hostname");
//			this.domainName = (String) map.get("domainName");
//			this.assignedDomainName = (String) map.get("assignedDomainName");
//			this.firewallEnabled = (String) map.get("firewallEnabled");
//			this.userStatus = (String) map.get("userStatus");
//			this.scheduleStatus = (String) map.get("scheduleStatus");
//			this.networkLevel = (String) map.get("networkLevel");
//			this.instanceLevel = (String) map.get("instanceLevel");
//			this.deployGroup = (String) map.get("deployGroup");
//			this.instanceContext = (String) map.get("instanceContext");
//			this.autoScale = (Boolean) map.get("autoScale");
//			this.networkLevel = (String) map.get("networkLevel");
//			this.networkLevel = (String) map.get("networkLevel");
//			this.networkLevel = (String) map.get("networkLevel");
//			this.networkLevel = (String) map.get("networkLevel");
//			this.networkLevel = (String) map.get("networkLevel");
//		}

	}
}
