package com.morpheusdata.model;

import java.util.Date;

public class VirtualImage extends MorpheusModel {
	public Account owner;
	public String visibility = "private";
	public String category;
	public String zoneType;
	public String name;
	public String code;
	public String description;
	public String imageType; //ami, qcow2
	public String externalType; //name given by provider
	public String remotePath;
	public String imagePath;
	public String imageName;
	public String imageRegion;
	public String content;
	public String folderName;
	public Long minDisk;
	public Long minRam;
	public Long rawSize;
	public String tags;
	public String config;
	public Boolean active = true;
	public String architecture;
	public String blockDeviceConfig;
	public String hypervisor;
	public String kernelId;
	public String platform;
	public String productCode;
	public String internalId;
	public String externalId;
	public String uniqueId;
	public String bucketId;
	public String externalDiskId;
	public String sshUsername;
	public String sshPassword;
	public String localAdministratorUsername = "Administrator";
	public String sshKey;
	public String ramdiskId;
	public String rootDeviceName;
	public String rootDeviceType;
	public String enhancedNetwork;
	public String status;
	public String statusReason;
	public Double statusPercent;
	public String virtualizationType;
	public String refType;
	public String refId;
	public String interfaceName = "eth0";
	public Boolean userUploaded = false;
	public Boolean userDefined = false;
	public Boolean isPublic = true;
	public Boolean isCloudInit = true;
	public Boolean isSysprep = false;
	public Boolean isAutoJoinDomain = false;
	public Boolean isForceCustomization = false;
	public Boolean systemImage = false;
	public Boolean compressed = false;
	public Boolean computeServerImage = false;
	public Boolean installAgent = true;
	public Boolean trialVersion = false;
	public Boolean dhcp = true;
	public Boolean virtioSupported = true;
	public Boolean vmToolsInstalled = true;
	public Boolean deleted = false;
	public StorageBucket storageProvider;
//	public OsType osType;
//	public ImageBuildExecution buildExecution;
	public String userData;
	public String consoleKeymap;
	public Date dateCreated;
	public Date lastUpdated;

//	static hasMany = [locations:VirtualImageLocation, accounts:Account, controllers:StorageController, volumes:StorageVolume,
//	interfaces:ComputeServerInterface]
}
