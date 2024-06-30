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

public class ComputeTypeSet extends MorpheusModel {

	protected String code;
	protected String name;

	@Deprecated(since = "0.15.4")
	protected ContainerType containerType;
	protected  WorkloadType workloadType;
	protected ComputeServerType computeServerType;
	protected String category;
	protected Integer priorityOrder;
	protected Boolean dynamicCount;
	protected Integer nodeCount;
	protected String nodeType;
	protected Boolean canAddNodes;
	protected Boolean installContainerRuntime;
	protected Boolean installStorageRuntime;

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

	/**
	 *
	 * @deprecated use {@link #getWorkloadType() } instead
	 */
	@Deprecated(since = "0.15.4")
	public ContainerType getContainerType() {
		return containerType;
	}

	/**
	 *
	 * @deprecated use {@link #setWorkloadType(WorkloadType) } instead
	 */
	@Deprecated(since = "0.15.4")
	public void setContainerType(ContainerType containerType) {
		this.containerType = containerType;
	}

	public WorkloadType getWorkloadType() {
		return workloadType;
	}

	public void setWorkloadType(WorkloadType workloadType) {
		this.workloadType = workloadType;
		markDirty("workloadType", workloadType, this.workloadType);
	}

	public ComputeServerType getComputeServerType() {
		return computeServerType;
	}

	public void setComputeServerType(ComputeServerType computeServerType) {
		this.computeServerType = computeServerType;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getPriorityOrder() {
		return priorityOrder;
	}

	public void setPriorityOrder(Integer priorityOrder) {
		this.priorityOrder = priorityOrder;
	}

	public Boolean getDynamicCount() {
		return dynamicCount;
	}

	public void setDynamicCount(Boolean dynamicCount) {
		this.dynamicCount = dynamicCount;
	}

	public Integer getNodeCount() {
		return nodeCount;
	}

	public void setNodeCount(Integer nodeCount) {
		this.nodeCount = nodeCount;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Boolean getCanAddNodes() {
		return canAddNodes;
	}

	public void setCanAddNodes(Boolean canAddNodes) {
		this.canAddNodes = canAddNodes;
	}

	public Boolean getInstallContainerRuntime() {
		return installContainerRuntime;
	}

	public void setInstallContainerRuntime(Boolean installContainerRuntime) {
		this.installContainerRuntime = installContainerRuntime;
	}

	public Boolean getInstallStorageRuntime() {
		return installStorageRuntime;
	}

	public void setInstallStorageRuntime(Boolean installStorageRuntime) {
		this.installStorageRuntime = installStorageRuntime;
	}
}
