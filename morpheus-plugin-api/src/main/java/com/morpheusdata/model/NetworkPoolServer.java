package com.morpheusdata.model;

import java.util.Date;
import java.util.Map;

public class NetworkPoolServer extends MorpheusModel {
	public String name;
	public String description;
	public String internalId;
	public String externalId;
	public String serviceUrl;
	public Boolean ignoreSsl = true;
	public String serviceHost;
	public Integer servicePort = 22;
	public String serviceMode;
	public String serviceUsername;
	public String servicePassword;
	public Integer apiPort;
	public Integer adminPort;
	public String status = "ok"; //ok, error, warning, offline
	public String statusMessage;
	public String networkFilter;
	public String zoneFilter;
	public String tenantMatch;
	public Boolean enabled = true;
	public Date statusDate;
	public Date dateCreated;
	public Date lastUpdated;
	public NetworkPoolServerType type;
	public AccountIntegration integration;
	public Account account;

	public void setAccountId(Long id) {
		this.account = new Account();
		this.account.id = id;
	}
}
