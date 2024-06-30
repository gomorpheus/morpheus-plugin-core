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
import com.morpheusdata.model.projection.LoadBalancerRuleIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkLoadBalancerRule extends LoadBalancerRuleIdentityProjection {
	protected String name;
	protected String category;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected User createdBy;
	protected String visibility = "public"; //['public', 'private']
	protected String description;
	protected String internalId;
	protected String externalId;
	protected Boolean enabled = true;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Integer displayOrder;
	//there can be multiple actions - saving off one and the config has all the details to simplify cuz 1 action is most common;
	protected String actionType;
	protected String actionName;
	protected String actionInternalId;
	protected String actionExternalId;
	protected Integer actionCode;
	protected Boolean actionForward;
	protected Integer actionLength;
	protected Integer actionOffset;
	protected Integer actionExpiration;
	protected String actionPoolId;
	protected Integer actionPort;
	protected Boolean actionRequest;
	protected Boolean actionSelect;
	protected Integer actionStatus;
	protected Integer actionTimeout;
	protected Integer actionVlan;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkLoadBalancerPolicy policy;

	// the "hasMany" fields
	protected List<NetworkLoadBalancerPool> pools = new ArrayList<NetworkLoadBalancerPool>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
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

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
		markDirty("actionType", actionType);
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
		markDirty("actionName", actionName);
	}

	public String getActionInternalId() {
		return actionInternalId;
	}

	public void setActionInternalId(String actionInternalId) {
		this.actionInternalId = actionInternalId;
		markDirty("actionInternalId", actionInternalId);
	}

	public String getActionExternalId() {
		return actionExternalId;
	}

	public void setActionExternalId(String actionExternalId) {
		this.actionExternalId = actionExternalId;
		markDirty("actionExternalId", actionExternalId);
	}

	public Integer getActionCode() {
		return actionCode;
	}

	public void setActionCode(Integer actionCode) {
		this.actionCode = actionCode;
		markDirty("actionCode", actionCode);
	}

	public Boolean getActionForward() {
		return actionForward;
	}

	public void setActionForward(Boolean actionForward) {
		this.actionForward = actionForward;
		markDirty("actionForward", actionForward);
	}

	public Integer getActionLength() {
		return actionLength;
	}

	public void setActionLength(Integer actionLength) {
		this.actionLength = actionLength;
		markDirty("actionLength", actionLength);
	}

	public Integer getActionOffset() {
		return actionOffset;
	}

	public void setActionOffset(Integer actionOffset) {
		this.actionOffset = actionOffset;
		markDirty("actionOffset", actionOffset);
	}

	public Integer getActionExpiration() {
		return actionExpiration;
	}

	public void setActionExpiration(Integer actionExpiration) {
		this.actionExpiration = actionExpiration;
		markDirty("actionExpiration", actionExpiration);
	}

	public String getActionPoolId() {
		return actionPoolId;
	}

	public void setActionPoolId(String actionPoolId) {
		this.actionPoolId = actionPoolId;
		markDirty("actionPoolId", actionPoolId);
	}

	public Integer getActionPort() {
		return actionPort;
	}

	public void setActionPort(Integer actionPort) {
		this.actionPort = actionPort;
		markDirty("actionPort", actionPort);
	}

	public Boolean getActionRequest() {
		return actionRequest;
	}

	public void setActionRequest(Boolean actionRequest) {
		this.actionRequest = actionRequest;
		markDirty("actionRequest", actionRequest);
	}

	public Boolean getActionSelect() {
		return actionSelect;
	}

	public void setActionSelect(Boolean actionSelect) {
		this.actionSelect = actionSelect;
		markDirty("actionSelect", actionSelect);
	}

	public Integer getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(Integer actionStatus) {
		this.actionStatus = actionStatus;
		markDirty("actionStatus", actionStatus);
	}

	public Integer getActionTimeout() {
		return actionTimeout;
	}

	public void setActionTimeout(Integer actionTimeout) {
		this.actionTimeout = actionTimeout;
		markDirty("actionTimeout", actionTimeout);
	}

	public Integer getActionVlan() {
		return actionVlan;
	}

	public void setActionVlan(Integer actionVlan) {
		this.actionVlan = actionVlan;
		markDirty("actionVlan", actionVlan);
	}

	public NetworkLoadBalancerPolicy getPolicy() {
		return policy;
	}

	public void setPolicy(NetworkLoadBalancerPolicy policy) {
		this.policy = policy;
		markDirty("policy", policy);
	}

	public List<NetworkLoadBalancerPool> getPools() {
		return pools;
	}

	public void setPools(List<NetworkLoadBalancerPool> pools) {
		this.pools = pools;
		markDirty("pools", pools);
	}
}
