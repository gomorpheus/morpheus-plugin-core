package com.morpheusdata.model;

import java.util.Collection;

public class Instance extends MorpheusModel {
	private String uuid;
	private String name;
	private String description;


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
