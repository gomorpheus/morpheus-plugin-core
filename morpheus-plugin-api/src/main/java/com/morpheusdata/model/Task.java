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

import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class Task extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String name;
	protected String code;
	protected String resultType; //value, exitCode, keyValue, json
	protected TaskType taskType;
	protected Boolean retryable = false;
	protected Integer retryCount = 5;
	protected Integer retryDelaySeconds = 10;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String uuid = UUID.randomUUID().toString();
	protected String executeTarget; // local, remote, resource
	protected List<OptionType> optionTypes;
	protected Boolean allowCustomConfig = false;
	protected List<TaskOption> taskOptions;

	public Account getAccount() {
		return account;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getResultType() {
		return resultType;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public Boolean getRetryable() {
		return retryable;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public Integer getRetryDelaySeconds() {
		return retryDelaySeconds;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public String getUuid() {
		return uuid;
	}

	public String getExecuteTarget() {
		return executeTarget;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public Boolean getAllowCustomConfig() {
		return allowCustomConfig;
	}

	public List<TaskOption> getTaskOptions() {
		return taskOptions;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
		markDirty("resultType", resultType);
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
		markDirty("taskType", taskType);
	}

	public void setRetryable(Boolean retryable) {
		this.retryable = retryable;
		markDirty("retryable", retryable);
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
		markDirty("retryCount", retryCount);
	}

	public void setRetryDelaySeconds(Integer retryDelaySeconds) {
		this.retryDelaySeconds = retryDelaySeconds;
		markDirty("retryDelaySeconds", retryDelaySeconds);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public void setExecuteTarget(String executeTarget) {
		this.executeTarget = executeTarget;
		markDirty("executeTarget", executeTarget);
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes);
	}

	public void setAllowCustomConfig(Boolean allowCustomConfig) {
		this.allowCustomConfig = allowCustomConfig;
		markDirty("allowCustomConfig", allowCustomConfig);
	}

	public void setTaskOptions(List<TaskOption> taskOptions) {
		this.taskOptions = taskOptions;
		markDirty("taskOptions", taskOptions);
	}

}
