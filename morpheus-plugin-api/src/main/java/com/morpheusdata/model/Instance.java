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
