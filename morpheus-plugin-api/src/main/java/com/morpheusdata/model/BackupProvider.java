/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import java.util.Map;

import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class BackupProvider extends MorpheusModel {

	protected String uuid;
	protected Boolean enabled = true;
	protected String serviceUrl;
	protected String serviceToken;
	protected String host;
	protected String port;
	protected String username;
	protected String password;
	protected String code;
	protected String visibility = "private";
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected Date statusDate;
	protected String name;
	protected String typeName;
	protected String lastUpdated;

	protected Boolean credentialLoaded = false;
	protected Map credentialData;

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

	public String getServiceToken() {
		return serviceToken;
	}

	public void setServiceToken(String serviceToken) {
		this.serviceToken = serviceToken;
		markDirty("serviceToken", serviceToken, this.serviceToken);
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

	public Boolean getCredentialLoaded() {
		return credentialLoaded;
	}

	public void setCredentialLoaded(Boolean credentialLoaded) {
		this.credentialLoaded = credentialLoaded;
		markDirty("credentialLoaded", credentialLoaded, this.credentialLoaded);
	}

	public Map getCredentialData() {
		return credentialData;
	}

	public void setCredentialData(Map credentialData) {
		this.credentialData = credentialData;
		markDirty("credentialData", credentialData, this.credentialData);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
