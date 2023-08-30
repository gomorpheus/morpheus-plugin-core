package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class Workspace extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;
	protected String description;
	protected TargetType targetType;
	protected WorkspaceType workspaceType;
	protected String uuid;
	protected Boolean enabled;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
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

	public TargetType getTargetType() {
		return targetType;
	}
	
	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
		markDirty("targetType", targetType);
	}

	public WorkspaceType getWorkspaceType() {
		return workspaceType;
	}
	
	public void setWorkspaceType(WorkspaceType workspaceType) {
		this.workspaceType = workspaceType;
		markDirty("workspaceType", workspaceType);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public enum TargetType {
		local, //run at the appliance
		remote, //run on a remote host
		workload //run on the workload
	}

}
