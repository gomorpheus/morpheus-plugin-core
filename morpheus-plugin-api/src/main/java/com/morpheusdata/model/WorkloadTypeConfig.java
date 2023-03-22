package com.morpheusdata.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class WorkloadTypeConfig extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String code;
	protected String name;
	protected String type;
	protected String sourceType;
	protected Integer sortOrder;
	protected String category;
	protected String settingCategory;
	protected String settingName;
	protected Boolean canPersist;
	protected Boolean failOnError;
	protected String fileOwner;
	protected String fileGroup;
	protected Integer permissions;
	//integrations
	protected String refType;
	protected String refId;
	protected String internalId;
	protected String externalId;
	protected String externalPath;
	protected String iacId;
	//status
	protected String status;
	protected String statusMessage;
	//config
	protected String rawData;
	//audit
	protected String createdBy;
	protected String updatedBy;
	protected Date dateCreated;
	protected Date lastUpdated;
	
	public Account getOwner() {
		return owner;
	}
	
	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
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

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getSourceType() {
		return sourceType;
	}
	
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
		markDirty("sourceType", sourceType);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getSettingCategory() {
		return settingCategory;
	}
	
	public void setSettingCategory(String settingCategory) {
		this.settingCategory = settingCategory;
		markDirty("settingCategory", settingCategory);
	}

	public String getSettingName() {
		return settingName;
	}
	
	public void setSettingName(String settingName) {
		this.settingName = settingName;
		markDirty("settingName", settingName);
	}

	public Boolean getCanPersist() {
		return canPersist;
	}
	
	public void setCanPersist(Boolean canPersist) {
		this.canPersist = canPersist;
		markDirty("canPersist", canPersist);
	}

	public Boolean getFailOnError() {
		return failOnError;
	}
	
	public void setFailOnError(Boolean failOnError) {
		this.failOnError = failOnError;
		markDirty("failOnError", failOnError);
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

	public String getRefType() {
		return refType;
	}
	
	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public String getRefId() {
		return refId;
	}
	
	public void setRefId(String refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public String getInternalId() {
		return internalId;
	}
	
	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getExternalPath() {
		return externalPath;
	}
	
	public void setExternalPath(String externalPath) {
		this.externalPath = externalPath;
		markDirty("externalPath", externalPath);
	}

	public String getIacId() {
		return iacId;
	}
	
	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getStatusMessage() {
		return statusMessage;
	}
	
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public String getRawData() {
		return rawData;
	}
	
	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public String getUpdatedBy() {
		return updatedBy;
	}
	
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
		markDirty("updatedBy", updatedBy);
	}

	public Date getDateCreated() {
		return dateCreated;
	}
	
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

}
