package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;

public class ReplicationGroup extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String code;
	protected String category;
	protected Boolean enabled = true;
	protected String platform = "all"; //linux,windows,etc
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected BackupProvider backupProvider;
	protected String internalId;
	protected String externalId;
	protected String config;
	protected String refType;
	protected Long refId;
	protected String replicationServerId;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected ReplicationSite replicationSite;
	//execution
	protected Date lastReplication;
	protected Long replicationLag = 0l;
	protected Long targetLag = 0l;
	protected Long maxStorage;
	protected Long usedStorage;
	protected Long throughput;
	protected Integer iops;
	protected String status;
	protected String subStatus;
	protected Double statusPercent;
	protected String statusMessage;
	protected Date lastFailover;
	protected String priority;
	protected Integer entityCount;
	//audit
	protected Long sourceGroupId;
	protected String source = "user";
	protected Date dateCreated;
	protected Date lastUpdated;

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

	public String getReplicationServerId() {
		return replicationServerId;
	}

	public void setReplicationServerId(String replicationServerId) {
		markDirty("replicationServerId", replicationServerId, this.replicationServerId);
		this.replicationServerId = replicationServerId;
	}

	public ReplicationSite getReplicationSite() {
		return replicationSite;
	}

	public void setReplicationSite(ReplicationSite replicationSite) {
		markDirty("replicationSite", replicationSite, this.replicationSite);
		this.replicationSite = replicationSite;
	}

	public Date getLastReplication() {
		return lastReplication;
	}

	public void setLastReplication(Date lastReplication) {
		markDirty("lastReplication", lastReplication, this.lastReplication);
		this.lastReplication = lastReplication;
	}

	public Long getReplicationLag() {
		return replicationLag;
	}

	public void setReplicationLag(Long replicationLag) {
		markDirty("replicationLag", replicationLag, this.replicationLag);
		this.replicationLag = replicationLag;
	}

	public Long getTargetLag() {
		return targetLag;
	}

	public void setTargetLag(Long targetLag) {
		markDirty("targetLag", targetLag, this.targetLag);
		this.targetLag = targetLag;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		markDirty("maxStorage", maxStorage, this.maxStorage);
		this.maxStorage = maxStorage;
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		markDirty("usedStorage", usedStorage, this.usedStorage);
		this.usedStorage = usedStorage;
	}

	public Long getThroughput() {
		return throughput;
	}

	public void setThroughput(Long throughput) {
		markDirty("throughput", throughput, this.throughput);
		this.throughput = throughput;
	}

	public Integer getIops() {
		return iops;
	}

	public void setIops(Integer iops) {
		markDirty("iops", iops, this.iops);
		this.iops = iops;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		markDirty("status", status, this.status);
		this.status = status;
	}

	public String getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(String subStatus) {
		markDirty("subStatus", subStatus, this.subStatus);
		this.subStatus = subStatus;
	}

	public Double getStatusPercent() {
		return statusPercent;
	}

	public void setStatusPercent(Double statusPercent) {
		markDirty("statusPercent", statusPercent, this.statusPercent);
		this.statusPercent = statusPercent;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		markDirty("statusMessage", statusMessage, this.statusMessage);
		this.statusMessage = statusMessage;
	}

	public Date getLastFailover() {
		return lastFailover;
	}

	public void setLastFailover(Date lastFailover) {
		markDirty("lastFailover", lastFailover, this.lastFailover);
		this.lastFailover = lastFailover;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		markDirty("priority", priority, this.priority);
		this.priority = priority;
	}

	public Integer getEntityCount() {
		return entityCount;
	}

	public void setEntityCount(Integer entityCount) {
		markDirty("entityCount", entityCount, this.entityCount);
		this.entityCount = entityCount;
	}

	public Long getSourceGroupId() {
		return sourceGroupId;
	}

	public void setSourceGroupId(Long sourceGroupId) {
		markDirty("sourceGroupId", sourceGroupId, this.sourceGroupId);
		this.sourceGroupId = sourceGroupId;
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
}
