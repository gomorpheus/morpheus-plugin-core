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

import java.util.Collection;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;

public class WorkloadAction extends MorpheusModel implements IModelUuidCodeName {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;
	protected String description;
	protected String actionService;
	protected String actionOperation;
	protected String actionScript;
	protected String actionCode;
	protected String provisionService;
	protected String provisionOperation;
	protected String provisionScript;
	protected Integer sortOrder;
	protected Integer minCount;
	protected Boolean countEnabled;
	protected String provisionSelectType;
	protected String allowedStatus;
	protected Boolean userTriggered;
	//ContainerType targetType //new layout to assign on upgrade
	protected String uuid;
	protected String syncSource;
	
	
	@JsonSerialize(using=ModelCollectionIdCodeNameSerializer.class)
	protected Collection<WorkloadAction> reverseActions;
	@JsonSerialize(using=ModelCollectionIdCodeNameSerializer.class)
	protected Collection<WorkloadType> workloadTypes;
	
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

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getActionService() {
		return actionService;
	}
	
	public void setActionService(String actionService) {
		this.actionService = actionService;
		markDirty("actionService", actionService);
	}

	public String getActionOperation() {
		return actionOperation;
	}
	
	public void setActionOperation(String actionOperation) {
		this.actionOperation = actionOperation;
		markDirty("actionOperation", actionOperation);
	}

	public String getActionScript() {
		return actionScript;
	}
	
	public void setActionScript(String actionScript) {
		this.actionScript = actionScript;
		markDirty("actionScript", actionScript);
	}

	public String getActionCode() {
		return actionCode;
	}
	
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
		markDirty("actionCode", actionCode);
	}

	public String getProvisionService() {
		return provisionService;
	}
	
	public void setProvisionService(String provisionService) {
		this.provisionService = provisionService;
		markDirty("provisionService", provisionService);
	}

	public String getProvisionOperation() {
		return provisionOperation;
	}
	
	public void setProvisionOperation(String provisionOperation) {
		this.provisionOperation = provisionOperation;
		markDirty("provisionOperation", provisionOperation);
	}

	public String getProvisionScript() {
		return provisionScript;
	}
	
	public void setProvisionScript(String provisionScript) {
		this.provisionScript = provisionScript;
		markDirty("provisionScript", provisionScript);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public Integer getMinCount() {
		return minCount;
	}
	
	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
		markDirty("minCount", minCount);
	}

	public Boolean getCountEnabled() {
		return countEnabled;
	}
	
	public void setCountEnabled(Boolean countEnabled) {
		this.countEnabled = countEnabled;
		markDirty("countEnabled", countEnabled);
	}

	public String getProvisionSelectType() {
		return provisionSelectType;
	}
	
	public void setProvisionSelectType(String provisionSelectType) {
		this.provisionSelectType = provisionSelectType;
		markDirty("provisionSelectType", provisionSelectType);
	}

	public String getAllowedStatus() {
		return allowedStatus;
	}
	
	public void setAllowedStatus(String allowedStatus) {
		this.allowedStatus = allowedStatus;
		markDirty("allowedStatus", allowedStatus);
	}

	public Boolean getUserTriggered() {
		return userTriggered;
	}
	
	public void setUserTriggered(Boolean userTriggered) {
		this.userTriggered = userTriggered;
		markDirty("userTriggered", userTriggered);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getSyncSource() {
		return syncSource;
	}
	
	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
		markDirty("syncSource", syncSource);
	}

	public Collection<WorkloadAction> getReverseActions() {
		return reverseActions;
	}
	
	public void setReverseActions(Collection<WorkloadAction> reverseActions) {
		this.reverseActions = reverseActions;
		markDirty("reverseActions", reverseActions);
	}
	
	public Collection<WorkloadType> getWorkloadTypes() {
		return workloadTypes;
	}
	
	public void setWorkloadTypes(Collection<WorkloadType> workloadTypes) {
		this.workloadTypes = workloadTypes;
		markDirty("workloadTypes", workloadTypes);
	}

}
