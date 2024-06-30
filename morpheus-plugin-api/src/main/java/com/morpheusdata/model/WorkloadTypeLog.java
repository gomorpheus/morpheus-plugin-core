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

public class WorkloadTypeLog extends MorpheusModel implements IModelCodeName {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String code;
	protected String name;
	protected String shortName;
	protected Integer sortOrder;
	protected String hostPath;
	protected String containerPath;
	protected Boolean canPersist;

	public Account getOwner() {
		return owner;
	}
	
	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
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

	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
		markDirty("shortName", shortName);
	}

	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
		markDirty("sortOrder", sortOrder);
	}

	public String getHostPath() {
		return hostPath;
	}
	
	public void setHostPath(String hostPath) {
		this.hostPath = hostPath;
		markDirty("hostPath", hostPath);
	}

	public String getContainerPath() {
		return containerPath;
	}
	
	public void setContainerPath(String containerPath) {
		this.containerPath = containerPath;
		markDirty("containerPath", containerPath);
	}

	public Boolean getCanPersist() {
		return canPersist;
	}
	
	public void setCanPersist(Boolean canPersist) {
		this.canPersist = canPersist;
		markDirty("canPersist", canPersist);
	}

}
