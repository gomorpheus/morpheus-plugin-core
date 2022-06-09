package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.BackupProvider;
import java.util.Date;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class BackupJob extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String code;
	protected String category;
	protected Boolean enabled = true;
	protected String platform = "all";
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected BackupProvider backupProvider;
	protected String internalId;
	protected String externalId;
	protected String config;
	protected String refType;
	protected Long refId;
	protected String backupServerId;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected BackupRepository backupRepository;
	protected String cronExpression;
	protected Date lastExecution;
	protected String lastBackupResultId;
	protected Date nextFire;
	protected Long sourceJobId;
	protected String source = "user";
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String visibility = "private";
	protected Integer retentionCount;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected ExecuteScheduleType scheduleType;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name, this.name);
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code, this.code);
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		markDirty("category", category, this.category);
		this.category = category;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled, this.enabled);
		this.enabled = enabled;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		markDirty("platform", platform, this.platform);
		this.platform = platform;
	}

	public BackupProvider getBackupProvider() {
		return backupProvider;
	}

	public void setBackupProvider(BackupProvider backupProvider) {
		markDirty("backupProvider", backupProvider, this.backupProvider);
		this.backupProvider = backupProvider;
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

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		markDirty("refType", refType, this.refType);
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		markDirty("refId", refId, this.refId);
		this.refId = refId;
	}

	public String getBackupServerId() {
		return backupServerId;
	}

	public void setBackupServerId(String backupServerId) {
		markDirty("backupServerId", backupServerId, this.backupServerId);
		this.backupServerId = backupServerId;
	}

	public BackupRepository getBackupRepository() {
		return backupRepository;
	}

	public void setBackupRepository(BackupRepository backupRepository) {
		markDirty("backupRepository", backupRepository, this.backupRepository);
		this.backupRepository = backupRepository;
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

	public Date getNextFire() {
		return nextFire;
	}

	public void setNextFire(Date nextFire) {
		markDirty("nextFire", nextFire, this.nextFire);
		this.nextFire = nextFire;
	}

	public Long getSourceJobId() {
		return sourceJobId;
	}

	public void setSourceJobId(Long sourceJobId) {
		markDirty("sourceJobId", sourceJobId, this.sourceJobId);
		this.sourceJobId = sourceJobId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		markDirty("source", source, this.source);
		this.source = source;
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

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		markDirty("visibility", visibility, this.visibility);
		this.visibility = visibility;
	}

	public Integer getRetentionCount() {
		return retentionCount;
	}

	public void setRetentionCount(Integer retentionCount) {
		markDirty("retentionCount", retentionCount, this.retentionCount);
		this.retentionCount = retentionCount;
	}

	public ExecuteScheduleType getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(ExecuteScheduleType scheduleType) {
		markDirty("scheduleType", scheduleType, this.scheduleType);
		this.scheduleType = scheduleType;
	}

}
