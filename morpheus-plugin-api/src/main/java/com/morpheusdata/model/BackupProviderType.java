package com.morpheusdata.model;


public class BackupProviderType extends MorpheusModel {

	protected String name;
	protected String code;
	protected String description;
	protected String providerService;
	protected String viewSet;
	protected Boolean enabled = true;
	protected Boolean creatable = false;
	protected Boolean hasCopyToStore = false;
	protected Boolean hasStreamToStore = false;
	protected Boolean downloadEnabled = false;
	protected Boolean restoreExistingEnabled = false;
	protected Boolean restoreNewEnabled = false;

	//backup styles
	protected Boolean hasBackups = true;
	protected Boolean hasReplication = false;
	protected Boolean hasServers = false;
	protected Boolean hasRepositories = false;
	protected Boolean hasJobs = false;
	protected Boolean hasSites = false;
	protected Boolean hasReplicationGroups = false;

	//jobs config
	protected Boolean hasCreateJob = true;
	protected Boolean hasCloneJob = true;
	protected Boolean hasAddToJob = true;
	protected Boolean hasOptionalJob = false;
	protected Boolean hasSchedule = true;
	protected Boolean hasStorageProvider = true;
	protected Boolean hasRetentionCount = true;
	protected Boolean hasCancelBackup = true;

	protected Boolean isPlugin;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		markDirty("description", description, this.description);
		this.description = description;
	}

	public String getProviderService() {
		return providerService;
	}

	public void setProviderService(String providerService) {
		markDirty("providerService", providerService, this.providerService);
		this.providerService = providerService;
	}

	public String getViewSet() {
		return viewSet;
	}

	public void setViewSet(String viewSet) {
		markDirty("viewSet", viewSet, this.viewSet);
		this.viewSet = viewSet;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled, this.enabled);
		this.enabled = enabled;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		markDirty("creatable", creatable, this.creatable);
		this.creatable = creatable;
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

	public Boolean getDownloadEnabled() {
		return downloadEnabled;
	}

	public void setDownloadEnabled(Boolean downloadEnabled) {
		markDirty("downloadEnabled", downloadEnabled, this.downloadEnabled);
		this.downloadEnabled = downloadEnabled;
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

	public Boolean getHasBackups() {
		return hasBackups;
	}

	public void setHasBackups(Boolean hasBackups) {
		markDirty("hasBackups", hasBackups, this.hasBackups);
		this.hasBackups = hasBackups;
	}

	public Boolean getHasReplication() {
		return hasReplication;
	}

	public void setHasReplication(Boolean hasReplication) {
		markDirty("hasReplication", hasReplication, this.hasReplication);
		this.hasReplication = hasReplication;
	}

	public Boolean getHasServers() {
		return hasServers;
	}

	public void setHasServers(Boolean hasServers) {
		markDirty("hasServers", hasServers, this.hasServers);
		this.hasServers = hasServers;
	}

	public Boolean getHasRepositories() {
		return hasRepositories;
	}

	public void setHasRepositories(Boolean hasRepositories) {
		markDirty("hasRepositories", hasRepositories, this.hasRepositories);
		this.hasRepositories = hasRepositories;
	}

	public Boolean getHasJobs() {
		return hasJobs;
	}

	public void setHasJobs(Boolean hasJobs) {
		markDirty("hasJobs", hasJobs, this.hasJobs);
		this.hasJobs = hasJobs;
	}

	public Boolean getHasSites() {
		return hasSites;
	}

	public void setHasSites(Boolean hasSites) {
		markDirty("hasSites", hasSites, this.hasSites);
		this.hasSites = hasSites;
	}

	public Boolean getHasReplicationGroups() {
		return hasReplicationGroups;
	}

	public void setHasReplicationGroups(Boolean hasReplicationGroups) {
		markDirty("hasReplicationGroups", hasReplicationGroups, this.hasReplicationGroups);
		this.hasReplicationGroups = hasReplicationGroups;
	}

	public Boolean getHasCreateJob() {
		return hasCreateJob;
	}

	public void setHasCreateJob(Boolean hasCreateJob) {
		markDirty("hasCreateJob", hasCreateJob, this.hasCreateJob);
		this.hasCreateJob = hasCreateJob;
	}

	public Boolean getHasCloneJob() {
		return hasCloneJob;
	}

	public void setHasCloneJob(Boolean hasCloneJob) {
		markDirty("hasCloneJob", hasCloneJob, this.hasCloneJob);
		this.hasCloneJob = hasCloneJob;
	}

	public Boolean getHasAddToJob() {
		return hasAddToJob;
	}

	public void setHasAddToJob(Boolean hasAddToJob) {
		markDirty("hasAddToJob", hasAddToJob, this.hasAddToJob);
		this.hasAddToJob = hasAddToJob;
	}

	public Boolean getHasOptionalJob() {
		return hasOptionalJob;
	}

	public void setHasOptionalJob(Boolean hasOptionalJob) {
		markDirty("hasOptionalJob", hasOptionalJob, this.hasOptionalJob);
		this.hasOptionalJob = hasOptionalJob;
	}

	public Boolean getHasSchedule() {
		return hasSchedule;
	}

	public void setHasSchedule(Boolean hasSchedule) {
		markDirty("hasSchedule", hasSchedule, this.hasSchedule);
		this.hasSchedule = hasSchedule;
	}

	public Boolean getHasStorageProvider() {
		return hasStorageProvider;
	}

	public void setHasStorageProvider(Boolean hasStorageProvider) {
		markDirty("hasStorageProvider", hasStorageProvider, this.hasStorageProvider);
		this.hasStorageProvider = hasStorageProvider;
	}

	public Boolean getHasRetentionCount() {
		return hasRetentionCount;
	}

	public void setHasRetentionCount(Boolean hasRetentionCount) {
		markDirty("hasRetentionCount", hasRetentionCount, this.hasRetentionCount);
		this.hasRetentionCount = hasRetentionCount;
	}

	public Boolean getHasCancelBackup() {
		return hasCancelBackup;
	}

	public void setHasCancelBackup(Boolean hasCancelBackup) {
		markDirty("hasCancelBackup", hasCancelBackup, this.hasCancelBackup);
		this.hasCancelBackup = hasCancelBackup;
	}

	public Boolean getPlugin() {
		return isPlugin;
	}

	public void setPlugin(Boolean plugin) {
		markDirty("isPlugin", plugin, this.isPlugin);
		isPlugin = plugin;
	}
}
