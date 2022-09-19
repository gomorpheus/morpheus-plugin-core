package com.morpheusdata.model;

import java.util.Map;

public class AccountIntegration extends MorpheusModel {

	protected String uuid;
	protected String name;
	protected String type;
	protected String code;
	protected String serviceHost;
	protected String servicePort;
	protected String serviceUrl;
	protected String serviceSlave;
	protected String repoUrl;
	protected String authType;
	protected String authId;
	protected String serviceUsername;
	protected String servicePassword;
	protected String servicePath;
	protected String serviceToken;
	protected String serviceVersion;
	protected String serviceWindowsVersion;
	protected String serviceCommand;
	protected String serviceMode;
	protected String serviceConfig;
	protected String serviceTypeCode;
	protected Boolean serviceFlag;
	protected String servicePrivateKey;
	protected String servicePublicKey;
	protected String authPrivateKey;
	protected String authPublicKey;

	protected Map credentialData;
	protected Boolean credentialLoaded = false;


	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getServiceHost() {
		return serviceHost;
	}

	public String getServicePort() {
		return servicePort;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public String getServiceSlave() {
		return serviceSlave;
	}

	public String getRepoUrl() {
		return repoUrl;
	}

	public String getAuthType() {
		return authType;
	}

	public String getAuthId() {
		return authId;
	}

	public String getServiceUsername() {
		return serviceUsername;
	}

	public String getServicePassword() {
		return servicePassword;
	}

	public String getServicePath() {
		return servicePath;
	}

	public String getServiceToken() {
		return serviceToken;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public String getServiceWindowsVersion() {
		return serviceWindowsVersion;
	}

	public String getServiceCommand() {
		return serviceCommand;
	}

	public String getServiceMode() {
		return serviceMode;
	}

	public String getServiceConfig() {
		return serviceConfig;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public Boolean getServiceFlag() {
		return serviceFlag;
	}

	public String getServicePrivateKey() {
		return servicePrivateKey;
	}

	public String getServicePublicKey() {
		return servicePublicKey;
	}

	public String getAuthPrivateKey() {
		return authPrivateKey;
	}

	public String getAuthPublicKey() {
		return authPublicKey;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setServiceHost(String serviceHost) {
		this.serviceHost = serviceHost;
		markDirty("serviceHost", serviceHost);
	}

	public void setServicePort(String servicePort) {
		this.servicePort = servicePort;
		markDirty("servicePort", servicePort);
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
		markDirty("serviceUrl", serviceUrl);
	}

	public void setServiceSlave(String serviceSlave) {
		this.serviceSlave = serviceSlave;
		markDirty("serviceSlave", serviceSlave);
	}

	public void setRepoUrl(String repoUrl) {
		this.repoUrl = repoUrl;
		markDirty("repoUrl", repoUrl);
	}

	public void setAuthType(String authType) {
		this.authType = authType;
		markDirty("authType", authType);
	}

	public void setAuthId(String authId) {
		this.authId = authId;
		markDirty("authId", authId);
	}

	public void setServiceUsername(String serviceUsername) {
		this.serviceUsername = serviceUsername;
		markDirty("serviceUsername", serviceUsername);
	}

	public void setServicePassword(String servicePassword) {
		this.servicePassword = servicePassword;
		markDirty("servicePassword", servicePassword);
	}

	public void setServicePath(String servicePath) {
		this.servicePath = servicePath;
		markDirty("servicePath", servicePath);
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
		markDirty("serviceToken", serviceToken);
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
		markDirty("serviceVersion", serviceVersion);
	}

	public void setServiceWindowsVersion(String serviceWindowsVersion) {
		this.serviceWindowsVersion = serviceWindowsVersion;
		markDirty("serviceWindowsVersion", serviceWindowsVersion);
	}

	public void setServiceCommand(String serviceCommand) {
		this.serviceCommand = serviceCommand;
		markDirty("serviceCommand", serviceCommand);
	}

	public void setServiceMode(String serviceMode) {
		this.serviceMode = serviceMode;
		markDirty("serviceMode", serviceMode);
	}

	public void setServiceConfig(String serviceConfig) {
		this.serviceConfig = serviceConfig;
		markDirty("serviceConfig", serviceConfig);
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
		markDirty("serviceTypeCode", serviceTypeCode);
	}

	public void setServiceFlag(Boolean serviceFlag) {
		this.serviceFlag = serviceFlag;
		markDirty("serviceFlag", serviceFlag);
	}

	public void setServicePrivateKey(String servicePrivateKey) {
		this.servicePrivateKey = servicePrivateKey;
		markDirty("servicePrivateKey", servicePrivateKey);
	}

	public void setServicePublicKey(String servicePublicKey) {
		this.servicePublicKey = servicePublicKey;
		markDirty("servicePublicKey", servicePublicKey);
	}

	public void setAuthPrivateKey(String authPrivateKey) {
		this.authPrivateKey = authPrivateKey;
		markDirty("authPrivateKey", authPrivateKey);
	}

	public void setAuthPublicKey(String authPublicKey) {
		this.authPublicKey = authPublicKey;
		markDirty("authPublicKey", authPublicKey);
	}

	public Map getCredentialData() {
		return credentialData;
	}

	public void setCredentialData(Map credentialData) {
		this.credentialData = credentialData;
	}

	public Boolean getCredentialLoaded() {
		return credentialLoaded;
	}

	public void setCredentialLoaded(Boolean credentialLoaded) {
		this.credentialLoaded = credentialLoaded;
	}


	public enum Status {
		ok,
		warning,
		syncing,
		error
	}
}
