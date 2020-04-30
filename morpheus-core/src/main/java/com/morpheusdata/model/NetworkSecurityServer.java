package com.morpheusdata.model;

import java.util.Date;

public class NetworkSecurityServer extends MorpheusModel {
	public String name;
	public NetworkSecurityServerType type;
	public NetworkServer networkServer; //if its a network and security server link
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
	public String serviceUsername;
	public String servicePassword;
	public Integer apiPort;
	public Integer adminPort;
	public String status = "ok"; //ok, error, warning, offline
	public String statusMessage;
	public Date statusDate;
	public String config;
	public String networkFilter;
	public String tenantMatch;
	public Date dateCreated;
	public Date lastUpdated;
	public Boolean enabled = true;
	public Boolean visible = true;
	public Long zoneId;

}
