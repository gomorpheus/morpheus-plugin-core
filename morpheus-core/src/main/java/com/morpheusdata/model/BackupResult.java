package com.morpheusdata.model;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BackupResult extends MorpheusModel {
	public Account account;
	public Backup backup;
	public String backupName;
	public String backupSetId; //to tie together backups that go together
	public String backupFormat; //data,file,snapshot
	public String backupType;
	public String containerFormat; //container,vm,all
	public User createdBy;
	public StorageBucket storageProvider;
	public String config;
	//executor
	public Long executeServerId;
	public String executorIpAddress;
	//target
	public Long serverId;
	public Long zoneId;
	public Long containerId;
	public Long instanceId;
	public Long containerTypeId;
	//timers
	public Date startDay;
	public Date startDate;
	public Date endDay;
	public Date endDate;
	//status
	public String processId;
	public String processCommand;
	public Boolean encrypted = true;
	public Boolean active = true;
	public String status = "START_REQUESTED"; //START_REQUESTED, IN_PROGRESS, CANCEL_REQUESTED, CANCELLED, SUCCEEDED, FAILED
	public String statusMessage;
	public Boolean error = false;
	public String errorOutput;
	public String errorMessage;
	//snapshot
	public String snapshotId;
	public String snapshotExternalId;
	public Boolean snapshotExtracted;
	//stats
	public Long durationMillis = 0L;
	public Long sizeInMb = 0L;
	public Long sizeInBytes = 0L;
	public String localPath;
	public String volumePath;
	public String resultBase;
	public String resultPath;
	public String resultBucket;
	public String resultArchive;
	public String resultEndpoint;
	public String imageType;
	public String internalId;
	public String externalId;

	//mirrored instance information for restoration
	public String volumes;
	public String controllers;
	public String interfaces;
	public Long maxMemory;
	public Long maxCores;
	public Long coresPerSocket;
	public Long planId;
	public Long instanceLayoutId;
	public Long resourcePoolId;
	public Boolean isCloudInit;
	public String sshUsername;
	public String sshPassword;
	public Long osTypeId;
	//gorm timestamps
	public Date dateCreated;
	public Date lastUpdated;

	Long getBackupId() {
		return backup.id;
	}

	public Map getConfigMap() {
		// TODO:
		return Collections.emptyMap();
	}
}
