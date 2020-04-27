package com.morpheusdata.model;

import java.util.Map;

public class AccountIntegration  extends MorpheusModel {
	//Id reference
	Long id;
	String uuid;
	String name;
	Map<String,Object> config;
	String type;
	String code;
	String serviceHost;
	String servicePort;
	String serviceUrl;
	String serviceSlave;
	String repoUrl;
	String authType;
	String authId;
	String serviceUsername;
	String servicePassword;
	String servicePath;
	String serviceToken;
	String serviceVersion;
	String serviceWindowsVersion;
	String serviceCommand;
	String serviceMode;
	String serviceConfig;
	String serviceTypeCode;
	Boolean serviceFlag;
	String servicePrivateKey;
	String servicePublicKey;

	String authPrivateKey;
	String authPublicKey;
}
