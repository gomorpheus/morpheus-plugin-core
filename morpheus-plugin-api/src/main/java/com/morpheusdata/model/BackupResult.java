package com.morpheusdata.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BackupResult extends MorpheusModel {
	protected Account account;
	protected Backup backup;
	protected String backupName;
	protected String backupSetId; //to tie together backups that go together
	protected String backupFormat; //data,file,snapshot
	protected String backupType;
	protected String containerFormat; //container,vm,all
	protected User createdBy;
	// protected StorageBucket storageProvider;
	protected String config;
	//executor
	protected Long executeServerId;
	protected String executorIpAddress;
	//target
	protected Long serverId;
	protected Long zoneId;
	protected Long containerId;
	protected Long instanceId;
	protected Long containerTypeId;
	//timers
	protected Date startDay;
	protected Date startDate;
	protected Date endDay;
	protected Date endDate;
	//status
	protected String processId;
	protected String processCommand;
	protected Boolean encrypted = true;
	protected Boolean active = true;
	protected String status = "START_REQUESTED"; //START_REQUESTED, IN_PROGRESS, CANCEL_REQUESTED, CANCELLED, SUCCEEDED, FAILED
	protected String statusMessage;
	protected Boolean error = false;
	protected String errorOutput;
	protected String errorMessage;
	//snapshot
	protected String snapshotId;
	protected String snapshotExternalId;
	protected Boolean snapshotExtracted;
	//stats
	protected Long durationMillis = 0L;
	protected Long sizeInMb = 0L;
	protected Long sizeInBytes = 0L;
	protected String localPath;
	protected String volumePath;
	protected String resultBase;
	protected String resultPath;
	protected String resultBucket;
	protected String resultArchive;
	protected String resultEndpoint;
	protected String imageType;
	protected String internalId;
	protected String externalId;

	//mirrored instance information for restoration
	protected String volumes;
	protected String controllers;
	protected String interfaces;
	protected Long maxMemory;
	protected Long maxCores;
	protected Long coresPerSocket;
	protected Long planId;
	protected Long instanceLayoutId;
	protected Long resourcePoolId;
	protected Boolean isCloudInit;
	protected String sshUsername;
	protected String sshPassword;
	protected Long osTypeId;
	//gorm timestamps
	protected Date dateCreated;
	protected Date lastUpdated;

	Long getBackupId() {
		return backup.id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public Backup getBackup() {
		return backup;
	}

	public void setBackup(Backup backup) {
		markDirty("backup", backup, this.backup);
		this.backup = backup;
	}

	public String getBackupName() {
		return backupName;
	}

	public void setBackupName(String backupName) {
		markDirty("backupName", backupName, this.backupName);
		this.backupName = backupName;
	}

	public String getBackupSetId() {
		return backupSetId;
	}

	public void setBackupSetId(String backupSetId) {
		markDirty("backupSetId", backupSetId, this.backupSetId);
		this.backupSetId = backupSetId;
	}

	public String getBackupFormat() {
		return backupFormat;
	}

	public void setBackupFormat(String backupFormat) {
		markDirty("backupFormat", backupFormat, this.backupFormat);
		this.backupFormat = backupFormat;
	}

	public String getBackupType() {
		return backupType;
	}

	public void setBackupType(String backupType) {
		markDirty("backupType", backupType, this.backupType);
		this.backupType = backupType;
	}

	public String getContainerFormat() {
		return containerFormat;
	}

	public void setContainerFormat(String containerFormat) {
		markDirty("containerFormat", containerFormat, this.containerFormat);
		this.containerFormat = containerFormat;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		markDirty("createdBy", createdBy, this.createdBy);
		this.createdBy = createdBy;
	}

	// public StorageBucket getStorageProvider() {
	// 	return storageProvider;
	// }

	// public void setStorageProvider(StorageBucket storageProvider) {
	// 	markDirty("storageProvider", storageProvider, this.storageProvider);
	// 	this.storageProvider = storageProvider;
	// }

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		markDirty("config", config, this.config);
		this.config = config;
	}

	public Long getExecuteServerId() {
		return executeServerId;
	}

	public void setExecuteServerId(Long executeServerId) {
		markDirty("executeServerId", executeServerId, this.executeServerId);
		this.executeServerId = executeServerId;
	}

	public String getExecutorIpAddress() {
		return executorIpAddress;
	}

	public void setExecutorIpAddress(String executorIpAddress) {
		markDirty("executorIpAddress", executorIpAddress, this.executorIpAddress);
		this.executorIpAddress = executorIpAddress;
	}

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		markDirty("serverId", serverId, this.serverId);
		this.serverId = serverId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		markDirty("zoneId", zoneId, this.zoneId);
		this.zoneId = zoneId;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		markDirty("containerId", containerId, this.containerId);
		this.containerId = containerId;
	}

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		markDirty("instanceId", instanceId, this.instanceId);
		this.instanceId = instanceId;
	}

	public Long getContainerTypeId() {
		return containerTypeId;
	}

	public void setContainerTypeId(Long containerTypeId) {
		markDirty("containerTypeId", containerTypeId, this.containerTypeId);
		this.containerTypeId = containerTypeId;
	}

	public Date getStartDay() {
		return startDay;
	}

	public void setStartDay(Date startDay) {
		markDirty("startDay", startDay, this.startDay);
		this.startDay = startDay;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		markDirty("startDate", startDate, this.startDate);
		this.startDate = startDate;
	}

	public Date getEndDay() {
		return endDay;
	}

	public void setEndDay(Date endDay) {
		markDirty("endDay", endDay, this.endDay);
		this.endDay = endDay;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		markDirty("endDate", endDate, this.endDate);
		this.endDate = endDate;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		markDirty("processId", processId, this.processId);
		this.processId = processId;
	}

	public String getProcessCommand() {
		return processCommand;
	}

	public void setProcessCommand(String processCommand) {
		markDirty("processCommand", processCommand, this.processCommand);
		this.processCommand = processCommand;
	}

	public Boolean getEncrypted() {
		return encrypted;
	}

	public void setEncrypted(Boolean encrypted) {
		markDirty("encrypted", encrypted, this.encrypted);
		this.encrypted = encrypted;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		markDirty("active", active, this.active);
		this.active = active;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		markDirty("status", status, this.status);
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		markDirty("statusMessage", statusMessage, this.statusMessage);
		this.statusMessage = statusMessage;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		markDirty("error", error, this.error);
		this.error = error;
	}

	public String getErrorOutput() {
		return errorOutput;
	}

	public void setErrorOutput(String errorOutput) {
		markDirty("errorOutput", errorOutput, this.errorOutput);
		this.errorOutput = errorOutput;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		markDirty("errorMessage", errorMessage, this.errorMessage);
		this.errorMessage = errorMessage;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		markDirty("snapshotId", snapshotId, this.snapshotId);
		this.snapshotId = snapshotId;
	}

	public String getSnapshotExternalId() {
		return snapshotExternalId;
	}

	public void setSnapshotExternalId(String snapshotExternalId) {
		markDirty("snapshotExternalId", snapshotExternalId, this.snapshotExternalId);
		this.snapshotExternalId = snapshotExternalId;
	}

	public Boolean getSnapshotExtracted() {
		return snapshotExtracted;
	}

	public void setSnapshotExtracted(Boolean snapshotExtracted) {
		markDirty("snapshotExtracted", snapshotExtracted, this.snapshotExtracted);
		this.snapshotExtracted = snapshotExtracted;
	}

	public Long getDurationMillis() {
		return durationMillis;
	}

	public void setDurationMillis(Long durationMillis) {
		markDirty("durationMillis", durationMillis, this.durationMillis);
		this.durationMillis = durationMillis;
	}

	public Long getSizeInMb() {
		return sizeInMb;
	}

	public void setSizeInMb(Long sizeInMb) {
		markDirty("sizeInMb", sizeInMb, this.sizeInMb);
		this.sizeInMb = sizeInMb;
	}

	public Long getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(Long sizeInBytes) {
		markDirty("sizeInBytes", sizeInBytes, this.sizeInBytes);
		this.sizeInBytes = sizeInBytes;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		markDirty("localPath", localPath, this.localPath);
		this.localPath = localPath;
	}

	public String getVolumePath() {
		return volumePath;
	}

	public void setVolumePath(String volumePath) {
		markDirty("volumePath", volumePath, this.volumePath);
		this.volumePath = volumePath;
	}

	public String getResultBase() {
		return resultBase;
	}

	public void setResultBase(String resultBase) {
		markDirty("resultBase", resultBase, this.resultBase);
		this.resultBase = resultBase;
	}

	public String getResultPath() {
		return resultPath;
	}

	public void setResultPath(String resultPath) {
		markDirty("resultPath", resultPath, this.resultPath);
		this.resultPath = resultPath;
	}

	public String getResultBucket() {
		return resultBucket;
	}

	public void setResultBucket(String resultBucket) {
		markDirty("resultBucket", resultBucket, this.resultBucket);
		this.resultBucket = resultBucket;
	}

	public String getResultArchive() {
		return resultArchive;
	}

	public void setResultArchive(String resultArchive) {
		markDirty("resultArchive", resultArchive, this.resultArchive);
		this.resultArchive = resultArchive;
	}

	public String getResultEndpoint() {
		return resultEndpoint;
	}

	public void setResultEndpoint(String resultEndpoint) {
		markDirty("resultEndpoint", resultEndpoint, this.resultEndpoint);
		this.resultEndpoint = resultEndpoint;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		markDirty("imageType", imageType, this.imageType);
		this.imageType = imageType;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		markDirty("internalId", internalId, this.internalId);
		this.internalId = internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		markDirty("externalId", externalId, this.externalId);
		this.externalId = externalId;
	}

	public String getVolumes() {
		return volumes;
	}

	public void setVolumes(String volumes) {
		markDirty("volumes", volumes, this.volumes);
		this.volumes = volumes;
	}

	public String getControllers() {
		return controllers;
	}

	public void setControllers(String controllers) {
		markDirty("controllers", controllers, this.controllers);
		this.controllers = controllers;
	}

	public String getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(String interfaces) {
		markDirty("interfaces", interfaces, this.interfaces);
		this.interfaces = interfaces;
	}

	public Long getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(Long maxMemory) {
		markDirty("maxMemory", maxMemory, this.maxMemory);
		this.maxMemory = maxMemory;
	}

	public Long getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(Long maxCores) {
		markDirty("maxCores", maxCores, this.maxCores);
		this.maxCores = maxCores;
	}

	public Long getCoresPerSocket() {
		return coresPerSocket;
	}

	public void setCoresPerSocket(Long coresPerSocket) {
		markDirty("coresPerSocket", coresPerSocket, this.coresPerSocket);
		this.coresPerSocket = coresPerSocket;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		markDirty("planId", planId, this.planId);
		this.planId = planId;
	}

	public Long getInstanceLayoutId() {
		return instanceLayoutId;
	}

	public void setInstanceLayoutId(Long instanceLayoutId) {
		markDirty("instanceLayoutId", instanceLayoutId, this.instanceLayoutId);
		this.instanceLayoutId = instanceLayoutId;
	}

	public Long getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(Long resourcePoolId) {
		markDirty("resourcePoolId", resourcePoolId, this.resourcePoolId);
		this.resourcePoolId = resourcePoolId;
	}

	public Boolean getCloudInit() {
		return isCloudInit;
	}

	public void setCloudInit(Boolean cloudInit) {
		markDirty("isCloudInit", cloudInit, this.isCloudInit);
		isCloudInit = cloudInit;
	}

	public String getSshUsername() {
		return sshUsername;
	}

	public void setSshUsername(String sshUsername) {
		markDirty("sshUsername", sshUsername, this.sshUsername);
		this.sshUsername = sshUsername;
	}

	public String getSshPassword() {
		return sshPassword;
	}

	public void setSshPassword(String sshPassword) {
		markDirty("sshPassword", sshPassword, this.sshPassword);
		this.sshPassword = sshPassword;
	}

	public Long getOsTypeId() {
		return osTypeId;
	}

	public void setOsTypeId(Long osTypeId) {
		markDirty("osTypeId", osTypeId, this.osTypeId);
		this.osTypeId = osTypeId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		markDirty("dateCreated", dateCreated, this.dateCreated);
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		markDirty("lastUpdated", lastUpdated, this.lastUpdated);
		this.lastUpdated = lastUpdated;
	}

	public Map getConfigMap() {
		// TODO:
		return Collections.emptyMap();
	}
}
