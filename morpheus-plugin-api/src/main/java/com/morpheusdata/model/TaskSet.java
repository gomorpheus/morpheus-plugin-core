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
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;

public class TaskSet extends MorpheusModel {

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String taskSetType;
	protected String phase;
	protected String name;
	protected String description;
	protected String code;
	protected String category;
	protected String refType;
	protected Long refId;
	protected String platform;
	protected String visibility;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean allowCustomConfig;
	protected Integer tasksetSize;
	protected String uuid;
	protected String syncSource;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected User createdBy;
	
	@JsonSerialize(using = ModelCollectionAsIdsOnlySerializer.class)
	protected Collection<TaskSetTask> taskSetTasks;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<OptionType> optionTypes;
	//protected Collection<Label> labels;

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getTaskSetType() {
		return taskSetType;
	}
	
	public void setTaskSetType(String taskSetType) {
		this.taskSetType = taskSetType;
		markDirty("taskSetType", taskSetType);
	}

	public String getPhase() {
		return phase;
	}
	
	public void setPhase(String phase) {
		this.phase = phase;
		markDirty("phase", phase);
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

	public String getRefType() {
		return refType;
	}
	
	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public Long getRefId() {
		return refId;
	}
	
	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public String getPlatform() {
		return platform;
	}
	
	public void setPlatform(String platform) {
		this.platform = platform;
		markDirty("platform", platform);
	}

	public String getVisibility() {
		return visibility;
	}
	
	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
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

	public Boolean getAllowCustomConfig() {
		return allowCustomConfig;
	}
	
	public void setAllowCustomConfig(Boolean allowCustomConfig) {
		this.allowCustomConfig = allowCustomConfig;
		markDirty("allowCustomConfig", allowCustomConfig);
	}

	public Integer getTasksetSize() {
		return tasksetSize;
	}
	
	public void setTasksetSize(Integer tasksetSize) {
		this.tasksetSize = tasksetSize;
		markDirty("tasksetSize", tasksetSize);
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

	public User getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public Collection<TaskSetTask> getTaskSetTasks() {
		return taskSetTasks;
	}
	
	public void setTaskSetTasks(Collection<TaskSetTask> taskSetTasks) {
		this.taskSetTasks = taskSetTasks;
		markDirty("taskSetTasks", taskSetTasks);
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}
	
	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes);
	}

}
