package com.morpheusdata.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class WorkloadTemplate extends MorpheusModel implements IModelUuidCodeName {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String code;
	protected String shortName;
	protected String fileName;
	protected String filePath;
	protected String internalPath;
	protected String absolutePath;
	protected String templateType;
	protected String template;
	protected String category;
	protected String settingCategory;
	protected String settingName;
	protected Boolean autoRun;
	protected Boolean runOnScale;
	protected Boolean runOnDeploy;
	protected String templatePhase;
	protected String fileOwner;
	protected String fileGroup;
	protected Integer permissions;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean failOnError;
	protected String uuid;
	protected String syncSource;
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
		markDirty("shortName", shortName);
	}

	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
		markDirty("fileName", fileName);
	}

	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
		markDirty("filePath", filePath);
	}

	public String getInternalPath() {
		return internalPath;
	}
	
	public void setInternalPath(String internalPath) {
		this.internalPath = internalPath;
		markDirty("internalPath", internalPath);
	}

	public String getAbsolutePath() {
		return absolutePath;
	}
	
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
		markDirty("absolutePath", absolutePath);
	}

	public String getTemplateType() {
		return templateType;
	}
	
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
		markDirty("templateType", templateType);
	}

	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String template) {
		this.template = template;
		markDirty("template", template);
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

	public String getTemplatePhase() {
		return templatePhase;
	}
	
	public void setTemplatePhase(String templatePhase) {
		this.templatePhase = templatePhase;
		markDirty("templatePhase", templatePhase);
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

	public Boolean getFailOnError() {
		return failOnError;
	}
	
	public void setFailOnError(Boolean failOnError) {
		this.failOnError = failOnError;
		markDirty("failOnError", failOnError);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getSyncSource() {
		return syncSource;
	}
	
	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
		markDirty("syncSource", syncSource);
	}

}
