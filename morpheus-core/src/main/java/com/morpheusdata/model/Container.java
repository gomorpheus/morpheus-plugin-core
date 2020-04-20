package com.morpheusdata.model;

import java.util.Date;

public class Container extends MorpheusModel {
//	public Account account;
	public Instance instance;
	public String internalName;
	public String displayName;
	public String category;
//	public ContainerType containerType;
//	public ContainerTypeSet containerTypeSet;
	public ComputeServer server;
//	public InstanceAction action;
//	public ServicePlan plan;
//	public ComputeZonePool resourcePool;
	public String apiKey;
	public Date dateCreated;
	public Date lastUpdated;
	public Boolean statsEnabled = true;
//	public Status status = Status.pending;
	public String statusMessage;
	public String errorMessage;
//	public Status userStatus;
//	public Status scheduleStatus;
	public String environmentPrefix;
	public String lastStats;
	public String configs;
	public String rawData;
	public String runtimeInfo;
//	public ComputeAclChain chain;
	public String containerVersion; //copied in on create so it never changes;
	public String repositoryImage; //copied in on create so it never changes;
	public String planCategory;
	public String hostname;
	public String domainName;
	public Boolean volumeCreated = false;
	public Boolean containerCreated = false;
	public Long maxStorage;
	public Long maxMemory;
	public Long maxCores;
	public Long coresPerSocket;
	public Long maxCpu;
	public Long usedStorage;
	public Long usedMemory;
	public Float usedCpu;
	public Long provisionTime = 0L;
	//Optional Meta;
//	public AppDeploy appDeploy;
//	public NetworkLoadBalancerInstance networkLoadBalancerInstance;
	public Network network;
//	public NetworkSubnet subnet;
//	public NetworkGroup networkGroup;
	public String configGroup;
	public String configId;
	public String configRole;
	public String configSettings;
	public String configTags;
	public String inventoryExternalRef;
	public Long inventoryServiceId;
	public String changeManagementExtId;
	public Long changeManagementServiceId;
	//ips for virtual networking;
	public String internalIp;
	public String externalIp;
	//external mapping;
	public String internalId; //id of the pod;
	public String externalId; //id of the container;
	public String uniqueId; //id to the container engine;
	public String controlId; //id in the scheduler;
	public String monitorId; //id of the monitor;
	public String serviceId; //id of the service;
	public String deploymentId; //id of the spec used to deploy;
	public String iacId; //id for infrastructure as code integrations;
	public String specType; //tracking mapping to scheduler;
	public String specId;
	public String resourceLevel = "app";
	//pricing estimates;
	public Double hourlyPrice = 0D;
	public Double hourlyCost = 0D;
	public Double runningMultiplier = 0D;
	public Double runningPrice;
	public Double runningCost;
	public String serviceUrls;  // take priority over ipAddress stuff;
	public Boolean inService = true;
	public String itemSource = "user";
	public Boolean managed = true;
	public String uuid;

//	static hasMany = [files:ContainerFile, mounts:ContainerMount, ports:ContainerPort, logs:ContainerLog,
//	configMaps:ContainerConfig, volumes:StorageVolume]

	void setServerId(Long id) {
		this.server = new ComputeServer();
		this.server.id = id;
	}

	void setNetworkId(Long id) {
		this.network = new Network();
		this.network.id = id;
	}

	void setInstanceId(Long id) {
		this.instance = new Instance();
		this.instance.id = id;
	}
}
