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

public class AppTemplateType extends MorpheusModel implements IModelCodeName {

	protected String code;
	protected String name;
	protected String description;
	protected Boolean enabled;
	protected Boolean hasRefresh;
	protected Boolean hasApply;
	protected Boolean hasDrift;
	protected Boolean hasDefaultCloud;
	protected Boolean hasCluster;
	protected Boolean hasAppLevelApprovalPolicy;
	protected Boolean hasSecrets;
	protected Boolean hasState;
	protected Boolean hasResources;
	protected Boolean hasInstances;
	protected Boolean hasTiers;

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	@Override
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getHasRefresh() {
		return hasRefresh;
	}

	public void setHasRefresh(Boolean hasRefresh) {
		this.hasRefresh = hasRefresh;
		markDirty("hasRefresh", hasRefresh);
	}

	public Boolean getHasApply() {
		return hasApply;
	}

	public void setHasApply(Boolean hasApply) {
		this.hasApply = hasApply;
		markDirty("hasApply", hasApply);
	}

	public Boolean getHasDrift() {
		return hasDrift;
	}

	public void setHasDrift(Boolean hasDrift) {
		this.hasDrift = hasDrift;
		markDirty("hasDrift", hasDrift);
	}

	public Boolean getHasDefaultCloud() {
		return hasDefaultCloud;
	}

	public void setHasDefaultCloud(Boolean hasDefaultCloud) {
		this.hasDefaultCloud = hasDefaultCloud;
		markDirty("hasDefaultCloud", hasDefaultCloud);
	}

	public Boolean getHasCluster() {
		return hasCluster;
	}

	public void setHasCluster(Boolean hasCluster) {
		this.hasCluster = hasCluster;
		markDirty("hasCluster", hasCluster);
	}

	public Boolean getHasAppLevelApprovalPolicy() {
		return hasAppLevelApprovalPolicy;
	}

	public void setHasAppLevelApprovalPolicy(Boolean hasAppLevelApprovalPolicy) {
		this.hasAppLevelApprovalPolicy = hasAppLevelApprovalPolicy;
		markDirty("hasAppLevelApprovalPolicy", hasAppLevelApprovalPolicy);
	}

	public Boolean getHasSecrets() {
		return hasSecrets;
	}

	public void setHasSecrets(Boolean hasSecrets) {
		this.hasSecrets = hasSecrets;
		markDirty("hasSecrets", hasSecrets);
	}

	public Boolean getHasState() {
		return hasState;
	}

	public void setHasState(Boolean hasState) {
		this.hasState = hasState;
		markDirty("hasState", hasState);
	}

	public Boolean getHasResources() {
		return hasResources;
	}

	public void setHasResources(Boolean hasResources) {
		this.hasResources = hasResources;
		markDirty("hasResources", hasResources);
	}

	public Boolean getHasInstances() {
		return hasInstances;
	}

	public void setHasInstances(Boolean hasInstances) {
		this.hasInstances = hasInstances;
		markDirty("hasInstances", hasInstances);
	}

	public Boolean getHasTiers() {
		return hasTiers;
	}

	public void setHasTiers(Boolean hasTiers) {
		this.hasTiers = hasTiers;
		markDirty("hasTiers", hasTiers);
	}
}
