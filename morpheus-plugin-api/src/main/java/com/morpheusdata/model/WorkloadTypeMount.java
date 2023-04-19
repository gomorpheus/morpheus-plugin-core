package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelIdCodeNameSerializer;

public class WorkloadTypeMount extends MorpheusModel implements IModelCodeName {

	protected String code;
	protected String name;
	protected String shortName;
	protected String mountType;
	protected Integer sortOrder;
	protected String hostPath;
	protected String subPath;
	protected String containerPath;
	protected String claimName;
	protected String volumeName;
	protected Boolean autoRun;
	protected Boolean required;
	protected Boolean visible;
	protected Boolean customType;
	protected Boolean unmanagedEnabled;
	protected Boolean canPersist;
	protected Boolean certStore;
	protected Boolean subMount;
	protected Boolean deployable;
	protected Boolean fileMount;
	@JsonSerialize(using=ModelIdCodeNameSerializer.class)
	protected WorkloadTemplate template;
	protected String fileOwner;
	protected String fileGroup;
	protected Integer permissions;
	
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

	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
		markDirty("shortName", shortName);
	}

	public String getMountType() {
		return mountType;
	}
	
	public void setMountType(String mountType) {
		this.mountType = mountType;
		markDirty("mountType", mountType);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public String getHostPath() {
		return hostPath;
	}
	
	public void setHostPath(String hostPath) {
		this.hostPath = hostPath;
		markDirty("hostPath", hostPath);
	}

	public String getSubPath() {
		return subPath;
	}
	
	public void setSubPath(String subPath) {
		this.subPath = subPath;
		markDirty("subPath", subPath);
	}

	public String getContainerPath() {
		return containerPath;
	}
	
	public void setContainerPath(String containerPath) {
		this.containerPath = containerPath;
		markDirty("containerPath", containerPath);
	}

	public String getClaimName() {
		return claimName;
	}
	
	public void setClaimName(String claimName) {
		this.claimName = claimName;
		markDirty("claimName", claimName);
	}

	public String getVolumeName() {
		return volumeName;
	}
	
	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
		markDirty("volumeName", volumeName);
	}

	public Boolean getAutoRun() {
		return autoRun;
	}
	
	public void setAutoRun(Boolean autoRun) {
		this.autoRun = autoRun;
		markDirty("autoRun", autoRun);
	}

	public Boolean getRequired() {
		return required;
	}
	
	public void setRequired(Boolean required) {
		this.required = required;
		markDirty("required", required);
	}

	public Boolean getVisible() {
		return visible;
	}
	
	public void setVisible(Boolean visible) {
		this.visible = visible;
		markDirty("visible", visible);
	}

	public Boolean getCustomType() {
		return customType;
	}
	
	public void setCustomType(Boolean customType) {
		this.customType = customType;
		markDirty("customType", customType);
	}

	public Boolean getUnmanagedEnabled() {
		return unmanagedEnabled;
	}
	
	public void setUnmanagedEnabled(Boolean unmanagedEnabled) {
		this.unmanagedEnabled = unmanagedEnabled;
		markDirty("unmanagedEnabled", unmanagedEnabled);
	}

	public Boolean getCanPersist() {
		return canPersist;
	}
	
	public void setCanPersist(Boolean canPersist) {
		this.canPersist = canPersist;
		markDirty("canPersist", canPersist);
	}

	public Boolean getCertStore() {
		return certStore;
	}
	
	public void setCertStore(Boolean certStore) {
		this.certStore = certStore;
		markDirty("certStore", certStore);
	}

	public Boolean getSubMount() {
		return subMount;
	}
	
	public void setSubMount(Boolean subMount) {
		this.subMount = subMount;
		markDirty("subMount", subMount);
	}

	public Boolean getDeployable() {
		return deployable;
	}
	
	public void setDeployable(Boolean deployable) {
		this.deployable = deployable;
		markDirty("deployable", deployable);
	}

	public Boolean getFileMount() {
		return fileMount;
	}
	
	public void setFileMount(Boolean fileMount) {
		this.fileMount = fileMount;
		markDirty("fileMount", fileMount);
	}

	public WorkloadTemplate getTemplate() {
		return template;
	}
	
	public void setTemplate(WorkloadTemplate template) {
		this.template = template;
		markDirty("template", template);
	}

	public String getFileOwner() {
		return fileOwner;
	}
	
	public void setFileOwner(String fileOwner) {
		this.fileOwner = fileOwner;
		markDirty("fileOwner", fileOwner);
	}

	public String getFileGroup() {
		return fileGroup;
	}
	
	public void setFileGroup(String fileGroup) {
		this.fileGroup = fileGroup;
		markDirty("fileGroup", fileGroup);
	}

	public Integer getPermissions() {
		return permissions;
	}
	
	public void setPermissions(Integer permissions) {
		this.permissions = permissions;
		markDirty("permissions", permissions);
	}

}
