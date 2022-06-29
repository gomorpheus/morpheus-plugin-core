package com.morpheusdata.model;

import java.util.List;

public class BackupType extends MorpheusModel {
	protected String code;
	protected String name;
	protected String backupFormat; //data,file,snapshot
	protected String containerFormat; //container,vm,all
	protected String providerService;
	protected String execService;
	protected String containerType;
	protected String containerCategory;
	protected String restoreType;
	protected Boolean active = true;
	protected Boolean hasCopyToStore = false;
	protected Boolean hasStreamToStore = false;
	protected Boolean copyToStore = true;
	protected String providerCode; // morpheus, veeam, commvault, avamar, rubrik
	protected Boolean downloadEnabled = true;
	protected Boolean downloadFromStoreOnly = false;
	protected Boolean restoreExistingEnabled = true;
	protected Boolean restoreNewEnabled = true;
	protected String viewSet;
	protected String restoreNewMode;
	protected Boolean pruneResultsOnRestoreExisting = false;
	protected Boolean restrictTargets = false;

	public List<OptionType> optionTypes;
	// public List<StorageServerType> storageTypes;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code, this.code);
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name, this.name);
		this.name = name;
	}

	public String getBackupFormat() {
		return backupFormat;
	}

	public void setBackupFormat(String backupFormat) {
		markDirty("backupFormat", backupFormat, this.backupFormat);
		this.backupFormat = backupFormat;
	}

	public String getContainerFormat() {
		return containerFormat;
	}

	public void setContainerFormat(String containerFormat) {
		markDirty("containerFormat", containerFormat, this.containerFormat);
		this.containerFormat = containerFormat;
	}

	public String getProviderService() {
		return providerService;
	}

	public void setProviderService(String providerService) {
		markDirty("providerService", providerService, this.providerService);
		this.providerService = providerService;
	}

	public String getExecService() {
		return execService;
	}

	public void setExecService(String execService) {
		markDirty("execService", execService, this.execService);
		this.execService = execService;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		markDirty("containerType", containerType, this.containerType);
		this.containerType = containerType;
	}

	public String getContainerCategory() {
		return containerCategory;
	}

	public void setContainerCategory(String containerCategory) {
		markDirty("containerCategory", containerCategory, this.containerCategory);
		this.containerCategory = containerCategory;
	}

	public String getRestoreType() {
		return restoreType;
	}

	public void setRestoreType(String restoreType) {
		markDirty("restoreType", restoreType, this.restoreType);
		this.restoreType = restoreType;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		markDirty("active", active, this.active);
		this.active = active;
	}

	public Boolean getHasCopyToStore() {
		return hasCopyToStore;
	}

	public void setHasCopyToStore(Boolean hasCopyToStore) {
		markDirty("hasCopyToStore", hasCopyToStore, this.hasCopyToStore);
		this.hasCopyToStore = hasCopyToStore;
	}

	public Boolean getHasStreamToStore() {
		return hasStreamToStore;
	}

	public void setHasStreamToStore(Boolean hasStreamToStore) {
		markDirty("hasStreamToStore", hasStreamToStore, this.hasStreamToStore);
		this.hasStreamToStore = hasStreamToStore;
	}

	public Boolean getCopyToStore() {
		return copyToStore;
	}

	public void setCopyToStore(Boolean copyToStore) {
		markDirty("copyToStore", copyToStore, this.copyToStore);
		this.copyToStore = copyToStore;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		markDirty("providerCode", providerCode, this.providerCode);
		this.providerCode = providerCode;
	}

	public Boolean getDownloadEnabled() {
		return downloadEnabled;
	}

	public void setDownloadEnabled(Boolean downloadEnabled) {
		markDirty("downloadEnabled", downloadEnabled, this.downloadEnabled);
		this.downloadEnabled = downloadEnabled;
	}

	public Boolean getDownloadFromStoreOnly() {
		return downloadFromStoreOnly;
	}

	public void setDownloadFromStoreOnly(Boolean downloadFromStoreOnly) {
		markDirty("downloadFromStoreOnly", downloadFromStoreOnly, this.downloadFromStoreOnly);
		this.downloadFromStoreOnly = downloadFromStoreOnly;
	}

	public Boolean getRestoreExistingEnabled() {
		return restoreExistingEnabled;
	}

	public void setRestoreExistingEnabled(Boolean restoreExistingEnabled) {
		markDirty("restoreExistingEnabled", restoreExistingEnabled, this.restoreExistingEnabled);
		this.restoreExistingEnabled = restoreExistingEnabled;
	}

	public Boolean getRestoreNewEnabled() {
		return restoreNewEnabled;
	}

	public void setRestoreNewEnabled(Boolean restoreNewEnabled) {
		markDirty("restoreNewEnabled", restoreNewEnabled, this.restoreNewEnabled);
		this.restoreNewEnabled = restoreNewEnabled;
	}

	public String getViewSet() {
		return viewSet;
	}

	public void setViewSet(String viewSet) {
		markDirty("viewSet", viewSet, this.viewSet);
		this.viewSet = viewSet;
	}

	public String getRestoreNewMode() {
		return restoreNewMode;
	}

	public void setRestoreNewMode(String restoreNewMode) {
		markDirty("restoreNewMode", restoreNewMode, this.restoreNewMode);
		this.restoreNewMode = restoreNewMode;
	}

	public Boolean getPruneResultsOnRestoreExisting() {
		return pruneResultsOnRestoreExisting;
	}

	public void setPruneResultsOnRestoreExisting(Boolean pruneResultsOnRestoreExisting) {
		markDirty("pruneResultsOnRestoreExisting", pruneResultsOnRestoreExisting, this.pruneResultsOnRestoreExisting);
		this.pruneResultsOnRestoreExisting = pruneResultsOnRestoreExisting;
	}

	public Boolean getRestrictTargets() {
		return restrictTargets;
	}

	public void setRestrictTargets(Boolean restrictTargets) {
		markDirty("restrictTargets", restrictTargets, this.restrictTargets);
		this.restrictTargets = restrictTargets;
	}
}
