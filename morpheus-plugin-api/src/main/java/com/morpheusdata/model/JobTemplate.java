package com.morpheusdata.model;

import java.util.Date;

public class JobTemplate extends MorpheusModel {
	protected Account owner;
	protected String name;
	protected JobType type;
	protected String displayName;
	protected String code;
	protected String category;
	protected String description;
	protected String jobSummary;
	protected Boolean enabled = true;
	protected String config;
	protected String status;
	protected String statusMessage;
	protected String errorMessage;
	protected String rawData;
	//integration
	protected String externalType;
	protected String internalId;
	protected String externalId;
	protected String refType;
	protected String refId;
	//job details
	protected String organizationId;
	protected String organizationName;
	protected String projectId;
	protected String projectName;
	protected String projectType;
	protected String inventoryId;
	protected String inventoryName;
	protected String serviceId;
	protected String serviceName;
	protected String configGroup;
	protected String configId;
	protected String configRole;
	protected String configSettings;
	protected String configCommand;
	protected String configTags;
	//creds
	protected String credentialId;
	protected String cloudCredentialId;
	protected String networkCredentialId;
	protected String storageCredentialId;
	protected String secretCredentialId;
	//schedule
	protected String cronExpression;
	protected String scheduleMode; //manual - or the id of the type for the form
	protected ExecuteScheduleType scheduleType;
	//execution
	protected String targetType;
	//audit
	protected String itemSource = "user";
	protected Date lastRun;
	protected Date nextFire;
	protected String lastStatus;
	protected String lastResult;
	protected String lastExecution;
	protected JobExecution lastJobExecution;
	protected User createdBy;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean internal = false; // set to true when one time jobs are created by tasks or workflows.

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JobType getType() {
		return type;
	}

	public void setType(JobType type) {
		this.type = type;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJobSummary() {
		return jobSummary;
	}

	public void setJobSummary(String jobSummary) {
		this.jobSummary = jobSummary;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getConfig() {
		return config;
	}

	@Override
	public void setConfig(String config) {
		this.config = config;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getInventoryName() {
		return inventoryName;
	}

	public void setInventoryName(String inventoryName) {
		this.inventoryName = inventoryName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getConfigGroup() {
		return configGroup;
	}

	public void setConfigGroup(String configGroup) {
		this.configGroup = configGroup;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getConfigRole() {
		return configRole;
	}

	public void setConfigRole(String configRole) {
		this.configRole = configRole;
	}

	public String getConfigSettings() {
		return configSettings;
	}

	public void setConfigSettings(String configSettings) {
		this.configSettings = configSettings;
	}

	public String getConfigCommand() {
		return configCommand;
	}

	public void setConfigCommand(String configCommand) {
		this.configCommand = configCommand;
	}

	public String getConfigTags() {
		return configTags;
	}

	public void setConfigTags(String configTags) {
		this.configTags = configTags;
	}

	public String getCredentialId() {
		return credentialId;
	}

	public void setCredentialId(String credentialId) {
		this.credentialId = credentialId;
	}

	public String getCloudCredentialId() {
		return cloudCredentialId;
	}

	public void setCloudCredentialId(String cloudCredentialId) {
		this.cloudCredentialId = cloudCredentialId;
	}

	public String getNetworkCredentialId() {
		return networkCredentialId;
	}

	public void setNetworkCredentialId(String networkCredentialId) {
		this.networkCredentialId = networkCredentialId;
	}

	public String getStorageCredentialId() {
		return storageCredentialId;
	}

	public void setStorageCredentialId(String storageCredentialId) {
		this.storageCredentialId = storageCredentialId;
	}

	public String getSecretCredentialId() {
		return secretCredentialId;
	}

	public void setSecretCredentialId(String secretCredentialId) {
		this.secretCredentialId = secretCredentialId;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getScheduleMode() {
		return scheduleMode;
	}

	public void setScheduleMode(String scheduleMode) {
		this.scheduleMode = scheduleMode;
	}

	public ExecuteScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ExecuteScheduleType scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getItemSource() {
		return itemSource;
	}

	public void setItemSource(String itemSource) {
		this.itemSource = itemSource;
	}

	public Date getLastRun() {
		return lastRun;
	}

	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

	public Date getNextFire() {
		return nextFire;
	}

	public void setNextFire(Date nextFire) {
		this.nextFire = nextFire;
	}

	public String getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(String lastStatus) {
		this.lastStatus = lastStatus;
	}

	public String getLastResult() {
		return lastResult;
	}

	public void setLastResult(String lastResult) {
		this.lastResult = lastResult;
	}

	public String getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(String lastExecution) {
		this.lastExecution = lastExecution;
	}

	public JobExecution getLastJobExecution() {
		return lastJobExecution;
	}

	public void setLastJobExecution(JobExecution lastJobExecution) {
		this.lastJobExecution = lastJobExecution;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Boolean getInternal() {
		return internal;
	}

	public void setInternal(Boolean internal) {
		this.internal = internal;
	}
}
