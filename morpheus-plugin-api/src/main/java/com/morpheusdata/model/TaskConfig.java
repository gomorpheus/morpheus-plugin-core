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
//	public Cypher cypher;
//	public ArchivesAccess archives;
//	public MorpheusAccess morpheus;

	public InstanceConfig instance;
	public ContainerConfig container;
	public ServerConfig server;
	public Zone zone;

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
