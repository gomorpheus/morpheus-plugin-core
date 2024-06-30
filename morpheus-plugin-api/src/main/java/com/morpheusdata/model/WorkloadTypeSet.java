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

import java.util.List;

public class WorkloadTypeSet extends MorpheusModel {

	protected String code;
	protected String category;
	protected Integer priorityOrder;
	protected Integer containerCount;
	protected Boolean dynamicCount;
	protected WorkloadType workloadType;
	protected String provisionService;
	protected String planCategory;
	protected String refType;
	protected Long refId;
	
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

	public Integer getPriorityOrder() {
		return priorityOrder;
	}
	
	public void setPriorityOrder(Integer priorityOrder) {
		this.priorityOrder = priorityOrder;
		markDirty("priorityOrder", priorityOrder);
	}

	public Integer getContainerCount() {
		return containerCount;
	}
	
	public void setContainerCount(Integer containerCount) {
		this.containerCount = containerCount;
		markDirty("containerCount", containerCount);
	}

	public Boolean getDynamicCount() {
		return dynamicCount;
	}
	
	public void setDynamicCount(Boolean dynamicCount) {
		this.dynamicCount = dynamicCount;
		markDirty("dynamicCount", dynamicCount);
	}

	public WorkloadType getWorkloadType() {
		return workloadType;
	}
	
	public void setWorkloadType(WorkloadType workloadType) {
		this.workloadType = workloadType;
		markDirty("workloadType", workloadType);
	}

	public String getProvisionService() {
		return provisionService;
	}
	
	public void setProvisionService(String provisionService) {
		this.provisionService = provisionService;
		markDirty("provisionService", provisionService);
	}

	public String getPlanCategory() {
		return planCategory;
	}
	
	public void setPlanCategory(String planCategory) {
		this.planCategory = planCategory;
		markDirty("planCategory", planCategory);
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

}
