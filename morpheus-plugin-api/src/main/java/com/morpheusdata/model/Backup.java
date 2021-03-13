package com.morpheusdata.model;

import com.morpheusdata.core.BackupProvider;

import java.util.Date;

public class Backup extends MorpheusModel {
	protected Account account;
	protected User createdBy;
	protected String name;
	protected BackupProvider backupProvider;
	protected BackupType backupType;
	// protected StorageBucket storageProvider;
	protected BackupRepository backupRepository;
	
	protected BackupJob backupJob;
	protected String backupSetId;
	protected Boolean active = true;
	protected Boolean enabled = true;

	//execution
	protected String cronExpression;
	protected Date lastExecution;
	protected String lastBackupResultId; //going away with ES out
	protected BackupResult lastResult;
	protected String lastStatus;
	protected Date nextFire;
	//source
	protected Long containerId;
	protected Long instanceId;
	protected Long computeServerId;
	protected Long computeServerTypeId;
	protected Long containerTypeId;
	protected Long instanceLayoutId;
	protected Long servicePlanId;
	protected Long zoneId;
	protected Long siteId;
	protected Long sourceProviderId;
	//target
	protected String targetHost;
	protected Integer targetPort;
	protected String targetUsername;
	protected String targetPassword;
	protected String targetName;
	protected String targetCustom;
	protected Boolean targetSlave;
	protected Boolean targetIncremental;
	protected Boolean targetAll = true;
	//executor
	protected String sshHost;
	protected Integer sshPort;
	protected String sshUsername;
	protected String sshPassword;
	//stats
	protected Long targetSize;
	protected Long backupSize;
	protected String localPath;
	protected String targetPath;
	protected String volumePath;
	protected String targetBucket;
	protected String targetArchive;
	protected Boolean compressed = false;
	protected Boolean copyToStore = true;
	//general
	protected String internalId;
	protected String externalId;
	protected String config;
	protected String restoreConfig;
	protected String dateDay;
	protected Integer retentionCount;
	protected Date dateCreated;

	protected Date lastUpdated;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		markDirty("createdBy", createdBy, this.createdBy);
		this.createdBy = createdBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name, this.name);
		this.name = name;
	}

	public BackupProvider getBackupProvider() {
		return backupProvider;
	}

	public void setBackupProvider(BackupProvider backupProvider) {
		markDirty("backupProvider", backupProvider, this.backupProvider);
		this.backupProvider = backupProvider;
	}

	public BackupType getBackupType() {
		return backupType;
	}

	public void setBackupType(BackupType backupType) {
		markDirty("backupType", backupType, this.backupType);
		this.backupType = backupType;
	}

	// public StorageBucket getStorageProvider() {
	// 	return storageProvider;
	// }

	// public void setStorageProvider(StorageBucket storageProvider) {
	// 	markDirty("storageProvider", storageProvider, this.storageProvider);
	// 	this.storageProvider = storageProvider;
	// }

	public BackupRepository getBackupRepository() {
		return backupRepository;
	}

	public void setBackupRepository(BackupRepository backupRepository) {
		markDirty("backupRepository", backupRepository, this.backupRepository);
		this.backupRepository = backupRepository;
	}

	public BackupJob getBackupJob() {
		return backupJob;
	}

	public void setBackupJob(BackupJob backupJob) {
		markDirty("backupJob", backupJob, this.backupJob);
		this.backupJob = backupJob;
	}

	public String getBackupSetId() {
		return backupSetId;
	}

	public void setBackupSetId(String backupSetId) {
		markDirty("backupSetId", backupSetId, this.backupSetId);
		this.backupSetId = backupSetId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		markDirty("active", active, this.active);
		this.active = active;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled, this.enabled);
		this.enabled = enabled;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		markDirty("cronExpression", cronExpression, this.cronExpression);
		this.cronExpression = cronExpression;
	}

	public Date getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(Date lastExecution) {
		markDirty("lastExecution", lastExecution, this.lastExecution);
		this.lastExecution = lastExecution;
	}

	public String getLastBackupResultId() {
		return lastBackupResultId;
	}

	public void setLastBackupResultId(String lastBackupResultId) {
		markDirty("lastBackupResultId", lastBackupResultId, this.lastBackupResultId);
		this.lastBackupResultId = lastBackupResultId;
	}

	public BackupResult getLastResult() {
		return lastResult;
	}

	public void setLastResult(BackupResult lastResult) {
		markDirty("lastResult", lastResult, this.lastResult);
		this.lastResult = lastResult;
	}

	public String getLastStatus() {
		return lastStatus;
	}

	public void setLastStatus(String lastStatus) {
		markDirty("lastStatus", lastStatus, this.lastStatus);
		this.lastStatus = lastStatus;
	}

	public Date getNextFire() {
		return nextFire;
	}

	public void setNextFire(Date nextFire) {
		markDirty("nextFire", nextFire, this.nextFire);
		this.nextFire = nextFire;
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

	public Long getComputeServerId() {
		return computeServerId;
	}

	public void setComputeServerId(Long computeServerId) {
		markDirty("computeServerId", computeServerId, this.computeServerId);
		this.computeServerId = computeServerId;
	}

	public Long getComputeServerTypeId() {
		return computeServerTypeId;
	}

	public void setComputeServerTypeId(Long computeServerTypeId) {
		markDirty("computeServerTypeId", computeServerTypeId, this.computeServerTypeId);
		this.computeServerTypeId = computeServerTypeId;
	}

	public Long getContainerTypeId() {
		return containerTypeId;
	}

	public void setContainerTypeId(Long containerTypeId) {
		markDirty("containerTypeId", containerTypeId, this.containerTypeId);
		this.containerTypeId = containerTypeId;
	}

	public Long getInstanceLayoutId() {
		return instanceLayoutId;
	}

	public void setInstanceLayoutId(Long instanceLayoutId) {
		markDirty("instanceLayoutId", instanceLayoutId, this.instanceLayoutId);
		this.instanceLayoutId = instanceLayoutId;
	}

	public Long getServicePlanId() {
		return servicePlanId;
	}

	public void setServicePlanId(Long servicePlanId) {
		markDirty("servicePlanId", servicePlanId, this.servicePlanId);
		this.servicePlanId = servicePlanId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		markDirty("zoneId", zoneId, this.zoneId);
		this.zoneId = zoneId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		markDirty("siteId", siteId, this.siteId);
		this.siteId = siteId;
	}

	public Long getSourceProviderId() {
		return sourceProviderId;
	}

	public void setSourceProviderId(Long sourceProviderId) {
		markDirty("sourceProviderId", sourceProviderId, this.sourceProviderId);
		this.sourceProviderId = sourceProviderId;
	}

	public String getTargetHost() {
		return targetHost;
	}

	public void setTargetHost(String targetHost) {
		markDirty("targetHost", targetHost, this.targetHost);
		this.targetHost = targetHost;
	}

	public Integer getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(Integer targetPort) {
		markDirty("targetPort", targetPort, this.targetPort);
		this.targetPort = targetPort;
	}

	public String getTargetUsername() {
		return targetUsername;
	}

	public void setTargetUsername(String targetUsername) {
		markDirty("targetUsername", targetUsername, this.targetUsername);
		this.targetUsername = targetUsername;
	}

	public String getTargetPassword() {
		return targetPassword;
	}

	public void setTargetPassword(String targetPassword) {
		markDirty("targetPassword", targetPassword, this.targetPassword);
		this.targetPassword = targetPassword;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		markDirty("targetName", targetName, this.targetName);
		this.targetName = targetName;
	}

	public String getTargetCustom() {
		return targetCustom;
	}

	public void setTargetCustom(String targetCustom) {
		markDirty("targetCustom", targetCustom, this.targetCustom);
		this.targetCustom = targetCustom;
	}

	public Boolean getTargetSlave() {
		return targetSlave;
	}

	public void setTargetSlave(Boolean targetSlave) {
		markDirty("targetSlave", targetSlave, this.targetSlave);
		this.targetSlave = targetSlave;
	}

	public Boolean getTargetIncremental() {
		return targetIncremental;
	}

	public void setTargetIncremental(Boolean targetIncremental) {
		markDirty("targetIncremental", targetIncremental, this.targetIncremental);
		this.targetIncremental = targetIncremental;
	}

	public Boolean getTargetAll() {
		return targetAll;
	}

	public void setTargetAll(Boolean targetAll) {
		markDirty("targetAll", targetAll, this.targetAll);
		this.targetAll = targetAll;
	}

	public String getSshHost() {
		return sshHost;
	}

	public void setSshHost(String sshHost) {
		markDirty("sshHost", sshHost, this.sshHost);
		this.sshHost = sshHost;
	}

	public Integer getSshPort() {
		return sshPort;
	}

	public void setSshPort(Integer sshPort) {
		markDirty("sshPort", sshPort, this.sshPort);
		this.sshPort = sshPort;
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

	public Long getTargetSize() {
		return targetSize;
	}

	public void setTargetSize(Long targetSize) {
		markDirty("targetSize", targetSize, this.targetSize);
		this.targetSize = targetSize;
	}

	public Long getBackupSize() {
		return backupSize;
	}

	public void setBackupSize(Long backupSize) {
		markDirty("backupSize", backupSize, this.backupSize);
		this.backupSize = backupSize;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		markDirty("localPath", localPath, this.localPath);
		this.localPath = localPath;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		markDirty("targetPath", targetPath, this.targetPath);
		this.targetPath = targetPath;
	}

	public String getVolumePath() {
		return volumePath;
	}

	public void setVolumePath(String volumePath) {
		markDirty("volumePath", volumePath, this.volumePath);
		this.volumePath = volumePath;
	}

	public String getTargetBucket() {
		return targetBucket;
	}

	public void setTargetBucket(String targetBucket) {
		markDirty("targetBucket", targetBucket, this.targetBucket);
		this.targetBucket = targetBucket;
	}

	public String getTargetArchive() {
		return targetArchive;
	}

	public void setTargetArchive(String targetArchive) {
		markDirty("targetArchive", targetArchive, this.targetArchive);
		this.targetArchive = targetArchive;
	}

	public Boolean getCompressed() {
		return compressed;
	}

	public void setCompressed(Boolean compressed) {
		markDirty("compressed", compressed, this.compressed);
		this.compressed = compressed;
	}

	public Boolean getCopyToStore() {
		return copyToStore;
	}

	public void setCopyToStore(Boolean copyToStore) {
		markDirty("copyToStore", copyToStore, this.copyToStore);
		this.copyToStore = copyToStore;
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

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		markDirty("config", config, this.config);
		this.config = config;
	}

	public String getRestoreConfig() {
		return restoreConfig;
	}

	public void setRestoreConfig(String restoreConfig) {
		markDirty("restoreConfig", restoreConfig, this.restoreConfig);
		this.restoreConfig = restoreConfig;
	}

	public String getDateDay() {
		return dateDay;
	}

	public void setDateDay(String dateDay) {
		markDirty("dateDay", dateDay, this.dateDay);
		this.dateDay = dateDay;
	}

	public Integer getRetentionCount() {
		return retentionCount;
	}

	public void setRetentionCount(Integer retentionCount) {
		markDirty("retentionCount", retentionCount, this.retentionCount);
		this.retentionCount = retentionCount;
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
	
}
