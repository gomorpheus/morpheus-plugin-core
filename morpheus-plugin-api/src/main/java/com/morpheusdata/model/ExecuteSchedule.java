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

public class ExecuteSchedule extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected ExecuteScheduleType type;
	protected String scheduleTimezone;
	protected String refType;
	protected Long refId;
	protected Boolean enabled = true;
	protected String createdBy = "system";
	protected String updatedBy = "system";

	public ExecuteScheduleType getType() {
		return type;
	}

	public void setType(ExecuteScheduleType type) {
		this.type = type;
		markDirty("type", type, this.type);
	}

	public String getScheduleTimezone() {
		return scheduleTimezone;
	}

	public void setScheduleTimezone(String scheduleTimezone) {
		this.scheduleTimezone = scheduleTimezone;
		markDirty("scheduleTimezone", scheduleTimezone, this.scheduleTimezone);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType, this.refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId, this.refId);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled, this.enabled);
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy, this.createdBy);
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
		markDirty("updatedBy", updatedBy, this.updatedBy);
	}
}
