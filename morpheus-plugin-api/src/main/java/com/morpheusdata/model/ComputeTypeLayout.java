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

public class ComputeTypeLayout extends MorpheusModel {

	protected String code;
	protected String name;
	protected Integer sortOrder;
	protected String computeVersion;
	protected String description;
	protected ComputeServerType type;
	protected Integer serverCount;
	protected Long memoryRequirement;
	protected Boolean hasAutoScale = false;
	protected Collection<ComputeTypeSet> computeServers;
	protected ProvisionType provisionType;
	protected ComputeServerGroupType groupType;

	protected Collection<ComputeTypePackage> packages;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getComputeVersion() {
		return computeVersion;
	}

	public void setComputeVersion(String computeVersion) {
		this.computeVersion = computeVersion;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ComputeServerType getType() {
		return type;
	}

	public void setType(ComputeServerType type) {
		this.type = type;
	}

	public Integer getServerCount() {
		return serverCount;
	}

	public void setServerCount(Integer serverCount) {
		this.serverCount = serverCount;
	}

	public Long getMemoryRequirement() {
		return memoryRequirement;
	}

	public void setMemoryRequirement(Long memoryRequirement) {
		this.memoryRequirement = memoryRequirement;
	}

	public Boolean getHasAutoScale() {
		return hasAutoScale;
	}

	public void setHasAutoScale(Boolean hasAutoScale) {
		this.hasAutoScale = hasAutoScale;
	}

	public Collection<ComputeTypeSet> getComputeServers() {
		return computeServers;
	}

	public void setComputeServers(Collection<ComputeTypeSet> computeServers) {
		this.computeServers = computeServers;
	}

	public ProvisionType getProvisionType() {
		return provisionType;
	}

	public void setProvisionType(ProvisionType provisionType) {
		this.provisionType = provisionType;
	}

	public ComputeServerGroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(ComputeServerGroupType groupType) {
		this.groupType = groupType;
	}
}
