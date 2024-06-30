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
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class TaskSetTask extends MorpheusModel {

	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected TaskSet taskSet;
	@JsonSerialize(using = ModelAsIdOnlySerializer.class)
	protected Task task;
	protected Integer taskOrder;
	protected String taskPhase;
	protected Boolean entryTask;
	protected String uuid;
	
	public TaskSet getTaskSet() {
		return taskSet;
	}
	
	public void setTaskSet(TaskSet taskSet) {
		this.taskSet = taskSet;
		markDirty("taskSet", taskSet);
	}

	public Task getTask() {
		return task;
	}
	
	public void setTask(Task task) {
		this.task = task;
		markDirty("task", task);
	}

	public Integer getTaskOrder() {
		return taskOrder;
	}
	
	public void setTaskOrder(Integer taskOrder) {
		this.taskOrder = taskOrder;
		markDirty("taskOrder", taskOrder);
	}

	public String getTaskPhase() {
		return taskPhase;
	}
	
	public void setTaskPhase(String taskPhase) {
		this.taskPhase = taskPhase;
		markDirty("taskPhase", taskPhase);
	}

	public Boolean getEntryTask() {
		return entryTask;
	}
	
	public void setEntryTask(Boolean entryTask) {
		this.entryTask = entryTask;
		markDirty("entryTask", entryTask);
	}

	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

}
