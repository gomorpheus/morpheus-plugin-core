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
import com.morpheusdata.model.projection.LoadBalancerMonitorIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkLoadBalancerMonitor extends LoadBalancerMonitorIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String category;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String visibility = "public"; //['public', 'private']
	protected String description;
	protected String monitorType;
	protected Integer monitorInterval;
	protected Integer monitorTimeout;
	protected String sendData;
	protected String sendVersion;
	protected String sendType;
	protected String receiveData;
	protected String receiveCode;
	protected String disabledData;
	protected String monitorUsername;
	protected String monitorPassword;
	protected String monitorDestination;
	protected Boolean monitorReverse = false;
	protected Boolean monitorTransparent = false;
	protected Boolean monitorAdaptive = false;
	protected String aliasAddress;
	protected Integer aliasPort;
	protected String internalId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String monitorSource = "external";
	protected String status = "ok"; //ok, error, warning, offline
	protected String statusMessage;
	protected Date statusDate;
	protected Boolean enabled = true;
	protected Integer maxRetry=0;
	protected Integer fallCount;
	protected Integer riseCount;
	protected Integer dataLength;
	protected String partition;
	protected String extraConfig;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancer loadBalancer;

	protected List<CloudPool> assignedZonePools = new ArrayList<CloudPool>();

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
		markDirty("monitorType", monitorType);
	}

	public Integer getMonitorInterval() {
		return monitorInterval;
	}

	public void setMonitorInterval(Integer monitorInterval) {
		this.monitorInterval = monitorInterval;
		markDirty("monitorInterval", monitorInterval);
	}

	public Integer getMonitorTimeout() {
		return monitorTimeout;
	}

	public void setMonitorTimeout(Integer monitorTimeout) {
		this.monitorTimeout = monitorTimeout;
		markDirty("monitorTimeout", monitorTimeout);
	}

	public String getSendData() {
		return sendData;
	}

	public void setSendData(String sendData) {
		this.sendData = sendData;
		markDirty("sendData", sendData);
	}

	public String getSendVersion() {
		return sendVersion;
	}

	public void setSendVersion(String sendVersion) {
		this.sendVersion = sendVersion;
		markDirty("sendVersion", sendVersion);
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
		markDirty("sendType", sendType);
	}

	public String getReceiveData() {
		return receiveData;
	}

	public void setReceiveData(String receiveData) {
		this.receiveData = receiveData;
		markDirty("receiveData", receiveData);
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
		markDirty("receiveCode", receiveCode);
	}

	public String getDisabledData() {
		return disabledData;
	}

	public void setDisabledData(String disabledData) {
		this.disabledData = disabledData;
		markDirty("disabledData", disabledData);
	}

	public String getMonitorUsername() {
		return monitorUsername;
	}

	public void setMonitorUsername(String monitorUsername) {
		this.monitorUsername = monitorUsername;
		markDirty("monitorUsername", monitorUsername);
	}

	public String getMonitorPassword() {
		return monitorPassword;
	}

	public void setMonitorPassword(String monitorPassword) {
		this.monitorPassword = monitorPassword;
		markDirty("monitorPassword", monitorPassword);
	}

	public String getMonitorDestination() {
		return monitorDestination;
	}

	public void setMonitorDestination(String monitorDestination) {
		this.monitorDestination = monitorDestination;
		markDirty("monitorDestination", monitorDestination);
	}

	public Boolean getMonitorReverse() {
		return monitorReverse;
	}

	public void setMonitorReverse(Boolean monitorReverse) {
		this.monitorReverse = monitorReverse;
		markDirty("monitorReverse", monitorReverse);
	}

	public Boolean getMonitorTransparent() {
		return monitorTransparent;
	}

	public void setMonitorTransparent(Boolean monitorTransparent) {
		this.monitorTransparent = monitorTransparent;
		markDirty("monitorTransparent", monitorTransparent);
	}

	public Boolean getMonitorAdaptive() {
		return monitorAdaptive;
	}

	public void setMonitorAdaptive(Boolean monitorAdaptive) {
		this.monitorAdaptive = monitorAdaptive;
		markDirty("monitorAdaptive", monitorAdaptive);
	}

	public String getAliasAddress() {
		return aliasAddress;
	}

	public void setAliasAddress(String aliasAddress) {
		this.aliasAddress = aliasAddress;
		markDirty("aliasAddress", aliasAddress);
	}

	public Integer getAliasPort() {
		return aliasPort;
	}

	public void setAliasPort(Integer aliasPort) {
		this.aliasPort = aliasPort;
		markDirty("aliasPort", aliasPort);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public String getMonitorSource() {
		return monitorSource;
	}

	public void setMonitorSource(String monitorSource) {
		this.monitorSource = monitorSource;
		markDirty("monitorSource", monitorSource);
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

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
		markDirty("statusDate", statusDate);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Integer getMaxRetry() {
		return maxRetry;
	}

	public void setMaxRetry(Integer maxRetry) {
		this.maxRetry = maxRetry;
		markDirty("maxRetry", maxRetry);
	}

	public Integer getFallCount() {
		return fallCount;
	}

	public void setFallCount(Integer fallCount) {
		this.fallCount = fallCount;
		markDirty("fallCount", fallCount);
	}

	public Integer getRiseCount() {
		return riseCount;
	}

	public void setRiseCount(Integer riseCount) {
		this.riseCount = riseCount;
		markDirty("riseCount", riseCount);
	}

	public Integer getDataLength() {
		return dataLength;
	}

	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
		markDirty("dataLength", dataLength);
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
	}

	public String getExtraConfig() {
		return extraConfig;
	}

	public void setExtraConfig(String extraConfig) {
		this.extraConfig = extraConfig;
		markDirty("extraConfig", extraConfig);
	}

	public NetworkLoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(NetworkLoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
		markDirty("loadBalancer", loadBalancer);
	}

	public List<CloudPool> getAssignedZonePools() {
		return assignedZonePools;
	}

	public void setAssignedZonePools(List<CloudPool> assignedZonePools) {
		this.assignedZonePools = assignedZonePools;
		markDirty("assignedZonePools", assignedZonePools);
	}
}
