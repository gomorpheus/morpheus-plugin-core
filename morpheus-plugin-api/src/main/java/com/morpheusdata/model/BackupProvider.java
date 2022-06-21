package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class BackupProvider extends MorpheusModel {
	
	protected Boolean enabled = true;
	protected String serviceUrl;
	protected String host;
	protected String port;
	protected String username;
	protected String password;
	protected String config;
	protected String code;
	protected String visibility = "private";
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected Date statusDate;
	protected String name;
	protected String typeName;
	protected String lastUpdated;

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected BackupProviderType type;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;

	public void setAccountId(Long id) {
		this.account = new Account();
		this.account.id = id;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled, this.enabled);
		this.enabled = enabled;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		markDirty("serviceUrl", serviceUrl, this.serviceUrl);
		this.serviceUrl = serviceUrl;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		markDirty("host", host, this.host);
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		markDirty("port", port, this.port);
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		markDirty("username", username, this.username);
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		markDirty("password", password, this.password);
		this.password = password;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		markDirty("config", config, this.config);
		this.config = config;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code, this.code);
		this.code = code;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		markDirty("visibility", visibility, this.visibility);
		this.visibility = visibility;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		markDirty("status", status, this.status);
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		markDirty("statusMessage", statusMessage, this.statusMessage);
		this.statusMessage = statusMessage;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		markDirty("statusDate", statusDate, this.statusDate);
		this.statusDate = statusDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name, this.name);
		this.name = name;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		markDirty("typeName", typeName, this.typeName);
		this.typeName = typeName;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		markDirty("lastUpdated", lastUpdated, this.lastUpdated);
		this.lastUpdated = lastUpdated;
	}

	public BackupProviderType getType() {
		return type;
	}

	public void setType(BackupProviderType type) {
		markDirty("type", type, this.type);
		this.type = type;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}
}
