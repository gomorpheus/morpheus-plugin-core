package com.morpheusdata.model;

import java.util.Date;

public class NetworkServer extends MorpheusModel {
	public String name;
	public NetworkServerType type;
	public AccountIntegration integration;
	public Account account;
	public String visibility = "public"; //['public', 'private']
	public String description;
	public String internalId;
	public String externalId;
	public String serviceUrl;
	public String serviceHost;
	public Integer servicePort = 22;
	public String serviceMode;
	public String servicePath;
	public String serviceUsername;
	public String servicePassword;
	public String serviceToken;
	public Integer apiPort;
	public Integer adminPort;
	//status and sync
	public String status = "ok"; //ok, error, warning, offline
	public String statusMessage;
	public Date statusDate;
	public Date lastSync;
	public Date nextRunDate;
	public Long lastSyncDuration;
	//config
	public String config;
	public String networkFilter;
	public String tenantMatch;
	public Long zoneId; //for network servers that need to be pinned to a zone - like nsx
	//audit
	public Date dateCreated;
	public Date lastUpdated;
	public Boolean enabled = true;
	public Boolean visible = true;
}
