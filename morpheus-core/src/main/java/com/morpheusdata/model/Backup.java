package com.morpheusdata.model;

import com.morpheusdata.core.BackupProvider;

import java.util.Date;

public class Backup extends MorpheusModel {
	public Account account;
	public User createdBy;
	public String name;
	public BackupProvider backupProvider;
	public BackupType backupType;
	public StorageBucket storageProvider;
	public BackupRepository backupRepository;
	public BackupJob backupJob;
	public String backupSetId;
	public Boolean active = true;
	public Boolean enabled = true;

	//execution
	public String cronExpression;
	public Date lastExecution;
	public String lastBackupResultId; //going away with ES out
	public BackupResult lastResult;
	public String lastStatus;
	public Date nextFire;
	//source
	public Long containerId;
	public Long instanceId;
	public Long computeServerId;
	public Long computeServerTypeId;
	public Long containerTypeId;
	public Long instanceLayoutId;
	public Long servicePlanId;
	public Long zoneId;
	public Long siteId;
	public Long sourceProviderId;
	//target
	public String targetHost;
	public Integer targetPort;
	public String targetUsername;
	public String targetPassword;
	public String targetName;
	public String targetCustom;
	public Boolean targetSlave;
	public Boolean targetIncremental;
	public Boolean targetAll = true;
	//executor
	public String sshHost;
	public Integer sshPort;
	public String sshUsername;
	public String sshPassword;
	//stats
	public Long targetSize;
	public Long backupSize;
	public String localPath;
	public String targetPath;
	public String volumePath;
	public String targetBucket;
	public String targetArchive;
	public Boolean compressed = false;
	public Boolean copyToStore = true;
	//general
	public String internalId;
	public String externalId;
	public String config;
	public String restoreConfig;
	public String dateDay;
	public Integer retentionCount;
	public Date dateCreated;

	public Date lastUpdated;
}
