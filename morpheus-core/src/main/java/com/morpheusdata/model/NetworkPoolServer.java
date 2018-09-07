package com.morpheusdata.model;

public class NetworkPoolServer extends MorpheusModel {
	String name;
	String description;
	String internalId;
	String externalId;
	String serviceUrl;
	Boolean ignoreSsl = true;
	String serviceHost;
	Integer servicePort = 22;
	String serviceMode;
	String serviceUsername;
	String servicePassword;
	Integer apiPort;
	Integer adminPort;

	String config;
	String networkFilter;
	String tenantMatch;
	Boolean enabled = true;
}
