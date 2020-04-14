package com.morpheusdata.model;

import java.util.Collection;
import java.util.Date;

public class Instance extends MorpheusModel {
	private String uuid;
	public ComputeSite site;
	public Account account;
	public InstanceType instanceType;
	public String category;
	public String instanceVersion; //this is hacky becuase proper version heirarchy was missed
	public InstanceTypeLayout layout;
	public ServicePlan plan;
//	public ComputeServerGroup serverGroup;
	public ComputeZonePool resourcePool;
	public Long provisionZoneId;
	//metadata
	public String name;
	public String unformattedName; //naming policy original useful for future scaling;
	public String displayName;
	public String description;
	public String dateDay;
	public String apiKey = java.util.UUID.randomUUID().toString();
	public Date dateCreated;
	public Date lastUpdated;
	public String hostName; //for dns or load balancing;
	public NetworkDomain networkDomain;
	public String environmentPrefix; //for env variable exports;
	public Boolean firewallEnabled = true;
	public Status status = Status.provisioning;
	public Status userStatus;
	public Status scheduleStatus;
//	public ComputeAclChain chain;
	public User createdBy;
	public String networkLevel = "container"; //host, container
	public String instanceLevel = "user"; //user, system
	public String deployGroup; //loose coupling for deploy grouping;
	public String instanceContext; //dev, test, production etc.;
//	public InstanceScale scale;
	public String expose; //custom port mappings;
	public Integer customPort; //port for the lb to use overriding proxy port;
	public Boolean autoScale = false;
	public Long currentDeployId;
	public String statusMessage;
	public Date statusDate;
	public String errorMessage;
	public Double statusPercent;
	public Long statusEta;
	public Integer expireDays;
	public Integer renewDays;
	public Integer expireCount = 0;
	public Date expireDate;
	public Date expireWarningDate;
	public Boolean expireWarningSent = false;
	public Integer shutdownDays;
	public Integer shutdownRenewDays;
	public Integer shutdownCount = 0;
	public Date shutdownDate;
	public Date shutdownWarningDate;
	public Date removalDate;
	public Boolean shutdownWarningSent = false;
//	public Deployment deployment;
	public String tags;
	public String meta;
	public Long maxStorage;
	public Long maxMemory;
	public Long maxCores;
	public Long coresPerSocket;
	public Long maxCpu;
	public Boolean configEnabled = false;
	public Long configRefId;
	public String configGroup;
	public String configId;
	public String configRole;
	public String configSettings;
	public String configCommand;
	public String configTags;
	public Boolean allowAutoExtension = true;
	public Integer extensionCount = 0;
	public Integer extensionsBeforeApproval = 0;
	public Long extensionIntegrationAccountId;
	public Long extensionWorkflowId;
	public Boolean locked = false;
	public String notes;
	public String notesFormatted;
//	public AccountCertificate sslCertificate;
//	public UserGroup userGroup;
	public Long provisionTime = 0L;
	//These are json encoded map fields
	public String config;
	public String volumeConfig;
	public String controllers;
	public String interfaces;
	public String serviceUsername;
	public String servicePassword;
	public String adminUsername;
	public String adminPassword;
	//external mapping
	public String internalId;
	public String externalId;
	public String serviceId;
	public String specType; //tracking mapping to scheduler
	public String specId; //tracking mapping id to scheduler
	public String iacId; //id for infrastructure as code integrations
	 //pricing estimates;
	public Double hourlyPrice = 0D;
	public Double hourlyCost = 0D;
	public Double runningMultiplier = 0D;
	public Double runningPrice;
	public Double runningCost;
	public String serviceUrls;  // take priority over ipAddress stuff

//	static hasMany = [containers:Container, favorites:InstanceFavorite, taskSets:TaskSet,
//	taskCustomizations:TaskCustomization, metadata:MetadataTag, evars:EnvironmentVariable, userEvars:EnvironmentVariable,
//	userGroups:UserGroup, volumes:StorageVolume, specs:ResourceSpec, resources:AccountResource]



	private Collection<Workload> workloads;

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

	public Collection<Workload> getWorkloads() {
		return workloads;
	}

	public void setWorkloads(Collection<Workload> workloads) {
		this.workloads = workloads;
		markDirty("workloads", workloads);
	}
}
