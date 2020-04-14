package com.morpheusdata.model;

import java.util.Date;

/**
 * Representation of a Morpheus ComputeServer database object within the Morpheus platform. Not all data is provided
 * in this implementation that is available in the morpheus core platform for security purposes and internal use.
 *
 * @author David Estes
 */
public class ComputeServer  extends MorpheusModel {
	public ComputeZone zone;
	public Account account;
//	public ComputeTypeLayout layout;
	public ComputeServerType computeServerType;
	public ComputeServer parentServer;
//	public ComputeTypeSet typeSet;
//	public BootMapping bootMapping;
	public ServicePlan plan;
	public String visibility = "private"; //['public', 'private']
//	public ComputeChassis chassis;
//	public ComputeCapacityInfo capacityInfo;
	public OsType serverOs;
	public VirtualImage sourceImage;
	public ComputeZonePool resourcePool;
//	public ComputeZoneFolder folder;
	public String name;
	public String displayName;
	public String internalName;
	public String category;
	public String description;
	public String internalId;
	public String externalId;
	public String iacId; //id for infrastructure as code integrations
	public String profileId; //id for template id in hardware vendor profiles
	public String uniqueId;
	public String rootVolumeId; //for double volume openstack
	public String volumeId;
	public String platform;
	public String platformVersion;
	public String agentVersion;
	public String nodePackageVersion;
	public String meta;
	public String ipmiHost;
	public String sshHost; //ip that the appliance users to get to the server;
	public Integer sshPort = 22;
	public String sshUsername;
	public String sshPassword;
	public String privateKey;
	public String consoleType;
	public String consoleHost;
	public Integer consolePort;
	public String consoleUrl;
	public Long consoleUrlExpires;
	public String consoleUsername;
	public String consolePassword;
	public String consoleKeymap;
	public String internalSshUsername;
	public String internalIp; //internalIp for server to server communication;
	public String externalIp; //externalIp for building links for users to get to stuff;
	public String loadBalancerIp; //load balancer ip;
	public String osDevice = "/dev/sda";
	public String dataDevice = "/dev/sdb";
	public Boolean lvmEnabled = true;
	public String apiKey = java.util.UUID.randomUUID().toString();
	public Boolean softwareRaid = false;
	public String status = "provisioning";
	public String userStatus;
	public String scheduleStatus;
	public String powerState = "unknown"; //on, off, unknown, paused
	public Date powerDate; //last time state changed
	public String config;
	public String rawData;
	public Date dateCreated;
	public Date lastUpdated;
	public Date lastAgentUpdate;
	public Date provisionEta;
	public Long provisionTime = 0L;
	public String serverType = "morpheus-node"; // 'morpheus-windows-node', ami', 'vm', 'unmanaged' //temporary till domain is added
	public String osType = "linux"; //linux, windows, unmanaged;
	public String commType = "ssh"; //ssh, minrm;
	public String lastStats;
	public Boolean discovered = false;
	public Boolean enabled = true;
	public Boolean provision = true;
	public Boolean singleTenant = false;
	public Boolean dockerEnabled = false;
	public Boolean vagrantEnabled = false;
	public Boolean sdnEnabled = false;
	public Boolean managed = false;
	public Boolean kvmEnabled = false;
	public Boolean autoCapacity = false;
	public Boolean enableDns = false;
	public Boolean agentInstalled = false;
	public Boolean toolsInstalled = false;
	public Boolean consulInstalled = false;
	public Boolean cephInstalled = false;
	public Boolean containerInstalled = false;
	public Boolean manageInternalFirewall = true;
	public Boolean maintenanceMode = false;
	public Boolean hotResize = false;
	public Boolean isCloudInit = false;
	public String consulMode;
	public String cephMode;
	public String containerMode;
	public String kvmMode; //kvm, qemu
	public String hostname;
	public NetworkDomain networkDomain;
	public String statusMessage;
	public Date statusDate;
	public String errorMessage;
	public Double statusPercent;
	public Long statusEta;
	public Double reservedMemory = 0d;
	public Double provisionPercent = 1.0d;
	public Long maxStorage;
	public Long maxMemory;
	public Long maxCores;
	public Long coresPerSocket;
	public Long maxCpu;
	public Long usedStorage;
	public Long usedMemory;
	public Float usedCpu;
	public String macAddress;
	public String serverVendor;
	public String serverModel;
	public String serialNumber;
	public String slotId;
	public String nodeId;
	public String tags;
	public Long provisionSiteId;
	public Boolean configEnabled = false;
	public Long configRefId;
	public String configGroup;
	public String configId;
	public String configRole;
	public String configSettings;
	public String configTags;
	public String configCommand;
	public User createdBy;
//	public UserGroup userGroup;
	public String notes;
	public String notesFormatted;
	//pricing estimates
	public Double hourlyPrice = 0D;
	public Double hourlyCost = 0D;
	public Double runningMultiplier = 0D;
	public Double runningPrice;
	public Double runningCost;
//	public InstanceScale scale;
	public String cloudConfigUser;
	public String cloudConfigMeta;
	public String cloudConfigNetwork;
	public Boolean tagCompliant;
	public Date lastInstalledSoftwareDate;
	public Date removalDate;
//	static hasMany = [interfaces:ComputeServerInterface, taskSets:TaskSet, taskCustomizations:TaskCustomization,
//	controllers:StorageController, volumes:StorageVolume, snapshots:Snapshot, metadata:MetadataTag,
//	userGroups:UserGroup, installedSoftware:InstalledSoftware, networkConnections:NetConnection,
//	accesses:ComputeServerAccess
//	]
}
