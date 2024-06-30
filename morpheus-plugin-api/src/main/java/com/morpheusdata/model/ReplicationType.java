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

public class ReplicationType extends MorpheusModel {

	protected String code;
	protected String name;
	protected String replicationFormat;
	protected String containerFormat; //container,vm,all
	protected String providerService;
	protected String execService;
	protected String containerType;
	protected String containerCategory;
	protected String restoreType;
	protected Boolean active = true;
	protected String providerCode; //morpheus,zerto;
	protected Boolean failoverEnabled = true;
	protected String viewSet;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code, this.code);
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name, this.name);
		this.name = name;
	}

	public String getReplicationFormat() {
		return replicationFormat;
	}

	public void setReplicationFormat(String replicationFormat) {
		markDirty("replicationFormat", replicationFormat, this.replicationFormat);
		this.replicationFormat = replicationFormat;
	}

	public String getContainerFormat() {
		return containerFormat;
	}

	public void setContainerFormat(String containerFormat) {
		markDirty("containerFormat", containerFormat, this.containerFormat);
		this.containerFormat = containerFormat;
	}

	public String getProviderService() {
		return providerService;
	}

	public void setProviderService(String providerService) {
		markDirty("providerService", providerService, this.providerService);
		this.providerService = providerService;
	}

	public String getExecService() {
		return execService;
	}

	public void setExecService(String execService) {
		markDirty("execService", execService, this.execService);
		this.execService = execService;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		markDirty("containerType", containerType, this.containerType);
		this.containerType = containerType;
	}

	public String getContainerCategory() {
		return containerCategory;
	}

	public void setContainerCategory(String containerCategory) {
		markDirty("containerCategory", containerCategory, this.containerCategory);
		this.containerCategory = containerCategory;
	}

	public String getRestoreType() {
		return restoreType;
	}

	public void setRestoreType(String restoreType) {
		markDirty("restoreType", restoreType, this.restoreType);
		this.restoreType = restoreType;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		markDirty("active", active, this.active);
		this.active = active;
	}

	public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		markDirty("providerCode", providerCode, this.providerCode);
		this.providerCode = providerCode;
	}

	public Boolean getFailoverEnabled() {
		return failoverEnabled;
	}

	public void setFailoverEnabled(Boolean failoverEnabled) {
		markDirty("failoverEnabled", failoverEnabled, this.failoverEnabled);
		this.failoverEnabled = failoverEnabled;
	}

	public String getViewSet() {
		return viewSet;
	}

	public void setViewSet(String viewSet) {
		markDirty("viewSet", viewSet, this.viewSet);
		this.viewSet = viewSet;
	}
}
