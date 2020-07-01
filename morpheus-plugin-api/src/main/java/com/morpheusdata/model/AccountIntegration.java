package com.morpheusdata.model;

import java.util.Map;

public class AccountIntegration extends MorpheusModel {

	public String uuid;
	public String name;
	public String config;
	public Map<String, Object> configMap;
	public String type;
	public String code;
	public String serviceHost;
	public String servicePort;
	public String serviceUrl;
	public String serviceSlave;
	public String repoUrl;
	public String authType;
	public String authId;
	public String serviceUsername;
	public String servicePassword;
	public String servicePath;
	public String serviceToken;
	public String serviceVersion;
	public String serviceWindowsVersion;
	public String serviceCommand;
	public String serviceMode;
	public String serviceConfig;
	public String serviceTypeCode;
	public Boolean serviceFlag;
	public String servicePrivateKey;
	public String servicePublicKey;

	public String authPrivateKey;
	public String authPublicKey;
}
