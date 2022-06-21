package com.morpheusdata.model;

import com.morpheusdata.core.BackupProvider;
import java.util.Date;

public class BackupRepository extends MorpheusModel {

	protected Account account;
	protected BackupProvider backupProvider;
	// TODO: storage buckets? Need a storage provider plugin to do this?
	// protected StorageBucket storageProvider;
	protected String name;
	protected String code;
	protected String category;
	protected Boolean enabled = true;
	protected String platform = "all"; //linux, windows, etc
	protected String internalId;
	protected String externalId;
	protected String config;
	protected String refType;
	protected Long refId;
	protected Long maxStorage;
	protected Long usedStorage;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String visibility = "private";

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

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		markDirty("visibility", visibility, this.visibility);
		this.visibility = visibility;
	}
}
