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
import com.morpheusdata.model.projection.LoadBalancerPoolIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkLoadBalancerPool extends LoadBalancerPoolIdentityProjection {
	protected String name;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String category;
	protected String visibility = "public"; //['public', 'private']
	protected String description;
	protected String internalId;
	protected String externalId;
	protected Boolean enabled = true;
	protected String vipSticky; //mode of the sticky session persistence
	protected String vipBalance; //balancing mode
	protected Boolean allowNat;
	protected Boolean allowSnat;
	protected String vipClientIpMode;
	protected String vipServerIpMode;
	protected Integer minActive;
	protected Integer minInService;
	protected String minUpMonitor;
	protected String minUpAction;
	protected Integer maxQueueDepth;
	protected Integer maxQueueTime;
	protected String extraConfig;
	//health fields
	protected Integer numberActive = 0;
	protected Integer numberInService = 0;
	protected Float healthScore = 100f;
	protected Float performanceScore = 100f;
	protected Float healthPenalty = 0f;
	protected Float securityPenalty = 0f;
	protected Float errorPenalty = 0f;
	//actions
	protected String downAction;
	protected Integer rampTime;
	protected Integer port;
	protected String portType;
	protected String partition;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String status;
	protected String statusMessage;
	protected Date statusDate;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancer loadBalancer;

	// the "hasMany" fields
	protected List<CloudPool> assignedZonePools = new ArrayList<CloudPool>();
	protected List<NetworkLoadBalancerNode> nodes = new ArrayList<NetworkLoadBalancerNode>();
	protected List<NetworkLoadBalancerMonitor> monitors = new ArrayList<NetworkLoadBalancerMonitor>();
	protected List<NetworkLoadBalancerMember> members = new ArrayList<NetworkLoadBalancerMember>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
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

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	@Override
	public String getExternalId() {
		return externalId;
	}

	@Override
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public String getVipSticky() {
		return vipSticky;
	}

	public void setVipSticky(String vipSticky) {
		this.vipSticky = vipSticky;
		markDirty("vipSticky", vipSticky);
	}

	public String getVipBalance() {
		return vipBalance;
	}

	public void setVipBalance(String vipBalance) {
		this.vipBalance = vipBalance;
		markDirty("vipBalance", vipBalance);
	}

	public Boolean getAllowNat() {
		return allowNat;
	}

	public void setAllowNat(Boolean allowNat) {
		this.allowNat = allowNat;
		markDirty("allowNat", allowNat);
	}

	public Boolean getAllowSnat() {
		return allowSnat;
	}

	public void setAllowSnat(Boolean allowSnat) {
		this.allowSnat = allowSnat;
		markDirty("allowSnat", allowSnat);
	}

	public String getVipClientIpMode() {
		return vipClientIpMode;
	}

	public void setVipClientIpMode(String vipClientIpMode) {
		this.vipClientIpMode = vipClientIpMode;
		markDirty("vipClientIpMode", vipClientIpMode);
	}

	public String getVipServerIpMode() {
		return vipServerIpMode;
	}

	public void setVipServerIpMode(String vipServerIpMode) {
		this.vipServerIpMode = vipServerIpMode;
		markDirty("vipServerIpMode", vipServerIpMode);
	}

	public Integer getMinActive() {
		return minActive;
	}

	public void setMinActive(Integer minActive) {
		this.minActive = minActive;
		markDirty("minActive", minActive);
	}

	public Integer getMinInService() {
		return minInService;
	}

	public void setMinInService(Integer minInService) {
		this.minInService = minInService;
		markDirty("minInService", minInService);
	}

	public String getMinUpMonitor() {
		return minUpMonitor;
	}

	public void setMinUpMonitor(String minUpMonitor) {
		this.minUpMonitor = minUpMonitor;
		markDirty("minUpMonitor", minUpMonitor);
	}

	public String getMinUpAction() {
		return minUpAction;
	}

	public void setMinUpAction(String minUpAction) {
		this.minUpAction = minUpAction;
		markDirty("minUpAction", minUpAction);
	}

	public Integer getMaxQueueDepth() {
		return maxQueueDepth;
	}

	public void setMaxQueueDepth(Integer maxQueueDepth) {
		this.maxQueueDepth = maxQueueDepth;
		markDirty("maxQueueDepth", maxQueueDepth);
	}

	public Integer getMaxQueueTime() {
		return maxQueueTime;
	}

	public void setMaxQueueTime(Integer maxQueueTime) {
		this.maxQueueTime = maxQueueTime;
		markDirty("maxQueueTime", maxQueueTime);
	}

	public String getExtraConfig() {
		return extraConfig;
	}

	public void setExtraConfig(String extraConfig) {
		this.extraConfig = extraConfig;
		markDirty("extraConfig", extraConfig);
	}

	public Integer getNumberActive() {
		return numberActive;
	}

	public void setNumberActive(Integer numberActive) {
		this.numberActive = numberActive;
		markDirty("numberActive", numberActive);
	}

	public Integer getNumberInService() {
		return numberInService;
	}

	public void setNumberInService(Integer numberInService) {
		this.numberInService = numberInService;
		markDirty("numberInService", numberInService);
	}

	public Float getHealthScore() {
		return healthScore;
	}

	public void setHealthScore(Float healthScore) {
		this.healthScore = healthScore;
		markDirty("healthScore", healthScore);
	}

	public Float getPerformanceScore() {
		return performanceScore;
	}

	public void setPerformanceScore(Float performanceScore) {
		this.performanceScore = performanceScore;
		markDirty("performanceScore", performanceScore);
	}

	public Float getHealthPenalty() {
		return healthPenalty;
	}

	public void setHealthPenalty(Float healthPenalty) {
		this.healthPenalty = healthPenalty;
		markDirty("healthPenalty", healthPenalty);
	}

	public Float getSecurityPenalty() {
		return securityPenalty;
	}

	public void setSecurityPenalty(Float securityPenalty) {
		this.securityPenalty = securityPenalty;
		markDirty("securityPenalty", securityPenalty);
	}

	public Float getErrorPenalty() {
		return errorPenalty;
	}

	public void setErrorPenalty(Float errorPenalty) {
		this.errorPenalty = errorPenalty;
		markDirty("errorPenalty", errorPenalty);
	}

	public String getDownAction() {
		return downAction;
	}

	public void setDownAction(String downAction) {
		this.downAction = downAction;
		markDirty("downAction", downAction);
	}

	public Integer getRampTime() {
		return rampTime;
	}

	public void setRampTime(Integer rampTime) {
		this.rampTime = rampTime;
		markDirty("rampTime", rampTime);
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
		markDirty("port", port);
	}

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
		markDirty("portType", portType);
	}

	public String getPartition() {
		return partition;
	}

	public void setPartition(String partition) {
		this.partition = partition;
		markDirty("partition", partition);
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

	public List<NetworkLoadBalancerNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<NetworkLoadBalancerNode> nodes) {
		this.nodes = nodes;
		markDirty("nodes", nodes);
	}

	public List<NetworkLoadBalancerMonitor> getMonitors() {
		return monitors;
	}

	public void setMonitors(List<NetworkLoadBalancerMonitor> monitors) {
		this.monitors = monitors;
		markDirty("monitors", monitors);
	}

	public List<NetworkLoadBalancerMember> getMembers() {
		return members;
	}

	public void setMembers(List<NetworkLoadBalancerMember> members) {
		this.members = members;
		markDirty("members", members);
	}
}
