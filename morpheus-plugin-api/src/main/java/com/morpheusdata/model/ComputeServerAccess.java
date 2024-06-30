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

/**
 * Store credentials for accessing a server.
 *
 * @since 0.15.3
 */
public class ComputeServerAccess extends MorpheusModel {

	protected String accessType; //ipmi, ssh, winrm, console etc...
	protected String host;
	protected Integer port;
	protected String username;
	protected String password;
	protected KeyPair accessKey;
	protected String status = "provisioning";
	protected String statusMessage;
	protected String errorMessage;

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
		markDirty("accessType", accessType);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		markDirty("host", host);
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
		markDirty("port", port);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		markDirty("username", username);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		markDirty("password", password);
	}

	public KeyPair getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(KeyPair accessKey) {
		this.accessKey = accessKey;
		markDirty("accessKey", accessKey);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		markDirty("errorMessage", errorMessage);
	}
}
