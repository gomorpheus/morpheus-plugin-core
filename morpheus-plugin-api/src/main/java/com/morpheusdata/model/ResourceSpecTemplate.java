package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.ResourceSpecTemplateIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import java.util.Date;

/**
 * Templatized resource specs to define resources.
 */
public class ResourceSpecTemplate extends ResourceSpecTemplateIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account account;
	protected ResourceSpecTemplateType type;
	protected String category;
	protected String shortName;
	protected String resourceName;
	protected String resourcePath;
	protected String resourceType;
	protected FileContent template;
	protected String settingCategory;
	protected String settingName;
	protected Boolean autoRun = false;
	protected Boolean runOnScale = false;
	protected Boolean runOnDeploy = false;
	protected Boolean hasDeployment = false;
	protected String templatePhase;
	protected String templateType;
	protected String refType;
	protected Long refId;
	protected String internalId;
	protected String externalType;
	protected String deploymentId;
	protected String status;
	protected String rawData;
	protected String createdBy;
	protected String updatedBy;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean hidden;
	protected Boolean failOnError;

	public ResourceSpecTemplate() {
		//default
	}

	public ResourceSpecTemplate(Long id, String name, String code, String externalId) {
		super(id, code, name, externalId);
	}

	public ResourceSpecTemplateType getType() {
		return type;
	}

	public void setType(ResourceSpecTemplateType type) {
		this.type = type;
		markDirty("type", type);
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
		markDirty("shortName", shortName);
	}
	
	public String getResourceName() {
		return resourceName;
	}
	
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		markDirty("resourceName", resourceName);
	}
	
	public String getResourcePath() {
		return resourcePath;
	}
	
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
		markDirty("resourcePath", resourcePath);
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
	
	public Boolean getAutoRun() {
		return autoRun;
	}
	
	public void setAutoRun(Boolean autoRun) {
		this.autoRun = autoRun;
		markDirty("autoRun", autoRun);
	}
	
	public Boolean getRunOnScale() {
		return runOnScale;
	}
	
	public void setRunOnScale(Boolean runOnScale) {
		this.runOnScale = runOnScale;
		markDirty("runOnScale", runOnScale);
	}
	
	public Boolean getRunOnDeploy() {
		return runOnDeploy;
	}
	
	public void setRunOnDeploy(Boolean runOnDeploy) {
		this.runOnDeploy = runOnDeploy;
		markDirty("runOnDeploy", runOnDeploy);
	}
	
	public Boolean getHasDeployment() {
		return hasDeployment;
	}
	
	public void setHasDeployment(Boolean hasDeployment) {
		this.hasDeployment = hasDeployment;
		markDirty("hasDeployment", hasDeployment);
	}
	
	public String getTemplatePhase() {
		return templatePhase;
	}
	
	public void setTemplatePhase(String templatePhase) {
		this.templatePhase = templatePhase;
		markDirty("templatePhase", templatePhase);
	}
	
	public String getTemplateType() {
		return templateType;
	}
	
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
		markDirty("templateType", templateType);
	}
	
	public String getRefType() {
		return refType;
	}
	
	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}
	
	public Long getRefId() {
		return refId;
	}
	
	public void setRefId(Long refId) {
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
	
	public String getExternalType() {
		return externalType;
	}
	
	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}
	
	public String getDeploymentId() {
		return deploymentId;
	}
	
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
		markDirty("deploymentId", deploymentId);
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
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
	
	public Boolean getHidden() {
		return hidden;
	}
	
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
		markDirty("hidden", hidden);
	}
	
	public Boolean getFailOnError() {
		return failOnError;
	}
	
	public void setFailOnError(Boolean failOnError) {
		this.failOnError = failOnError;
		markDirty("failOnError", failOnError);
	}
	
}
