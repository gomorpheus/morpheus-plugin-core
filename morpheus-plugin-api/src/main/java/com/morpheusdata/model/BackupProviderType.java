package com.morpheusdata.model;


import java.util.Collection;

public class BackupProviderType extends MorpheusModel {

	protected String name;
	protected String code;
	protected String description;
	protected String providerService;
	protected String jobService;

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

	protected Collection<BackupType> backupTypes;
	protected Collection<ReplicationType> replicationTypes;


	protected Collection<OptionType> optionTypes;
	protected Collection<OptionType> replicationGroupOptions;
	protected Collection<OptionType> replicationOptions;
	protected Collection<OptionType> backupJobOptions;
	protected Collection<OptionType> backupOptions;
	protected Collection<OptionType> instanceReplicationGroupOptions;

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

	public String getJobService() {
		return jobService;
	}

	public void setJobService(String jobService) {
		this.jobService = jobService;
		markDirty("jobService", jobService, this.jobService);
	}

	public Collection<BackupType> getBackupTypes() {
		return backupTypes;
	}

	public void setBackupTypes(Collection<BackupType> backupTypes) {
		this.backupTypes = backupTypes;
		markDirty("backupTypes", backupTypes, this.backupTypes);
	}

	public Collection<ReplicationType> getReplicationTypes() {
		return replicationTypes;
	}

	public void setReplicationTypes(Collection<ReplicationType> replicationTypes) {
		this.replicationTypes = replicationTypes;
		markDirty("replicationTypes", replicationTypes, this.replicationTypes);
	}

	public Collection<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(Collection<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes, this.optionTypes);
	}

	public Collection<OptionType> getReplicationGroupOptions() {
		return replicationGroupOptions;
	}

	public Collection<OptionType> getReplicationGroupOptionTypes() {
		return getReplicationGroupOptions();
	}

	public void setReplicationGroupOptions(Collection<OptionType> replicationGroupOptions) {
		this.replicationGroupOptions = replicationGroupOptions;
		markDirty("replicationGroupOptions", replicationGroupOptions, this.replicationGroupOptions);
	}

	public Collection<OptionType> getReplicationOptions() {
		return replicationOptions;
	}

	public Collection<OptionType> getReplicationOptionTypes() {
		return getReplicationOptions();
	}

	public void setReplicationOptions(Collection<OptionType> replicationOptions) {
		this.replicationOptions = replicationOptions;
		markDirty("replicationOptions", replicationOptions, this.replicationOptions);
	}

	public Collection<OptionType> getBackupJobOptions() {
		return backupJobOptions;
	}

	public Collection<OptionType> getBackupJobOptionTypes() {
		return getBackupJobOptions();
	}

	public void setBackupJobOptions(Collection<OptionType> backupJobOptions) {
		this.backupJobOptions = backupJobOptions;
		markDirty("backupJobOptions", backupJobOptions, this.backupJobOptions);
	}

	public Collection<OptionType> getBackupOptions() {
		return backupOptions;
	}

	public Collection<OptionType> getBackupOptionTypes() {
		return getBackupOptions();
	}

	public void setBackupOptions(Collection<OptionType> backupOptions) {
		this.backupOptions = backupOptions;
		markDirty("backupOptions", backupOptions, this.backupOptions);
	}

	public Collection<OptionType> getInstanceReplicationGroupOptions() {
		return instanceReplicationGroupOptions;
	}

	public Collection<OptionType> getInstanceReplicationGroupOptionTypes() {
		return getInstanceReplicationGroupOptions();
	}

	public void setInstanceReplicationGroupOptions(Collection<OptionType> instanceReplicationGroupOptions) {
		this.instanceReplicationGroupOptions = instanceReplicationGroupOptions;
		markDirty("instanceReplicationGroupOptions", instanceReplicationGroupOptions, this.instanceReplicationGroupOptions);
	}
}
