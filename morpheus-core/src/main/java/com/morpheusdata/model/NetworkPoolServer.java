package com.morpheusdata.model;

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

	public String config;
	public String networkFilter;
	public String tenantMatch;
	public Boolean enabled = true;
}
