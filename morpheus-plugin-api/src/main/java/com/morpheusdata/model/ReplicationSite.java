package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class ReplicationSite extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected BackupProvider backupProvider;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Cloud cloud;
	protected String name;
	protected String code;
	protected String category;
	protected Boolean enabled = true;
	protected String platform = "all"; //linux,windows,etc;
	protected String internalId;
	protected String externalId;
	protected String refType;
	protected Long refId;
	//capacity
	protected Long maxStorage;
	protected Long usedStorage;
	//audit
	protected Date dateCreated;
	protected Date lastUpdated;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		markDirty("owner", owner, this.owner);
		this.owner = owner;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public BackupProvider getBackupProvider() {
		return backupProvider;
	}

	public void setBackupProvider(BackupProvider backupProvider) {
		markDirty("backupProvider", backupProvider, this.backupProvider);
		this.backupProvider = backupProvider;
	}

	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		markDirty("cloud", cloud, this.cloud);
		this.cloud = cloud;
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
