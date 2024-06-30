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

public class AccountResourceType extends MorpheusModel {
	protected String code;
	protected String category;
	protected String name;
	protected String displayName;
	protected String description;
	//typing
	protected String type;
	protected String apiCategory;
	protected String apiType;
	protected String costingCode;
	protected String costingType;
	protected String provisionCode;
	protected String morpheusType;
	protected String resourceType; //generic type for unknwon stuff
	protected String resourceIcon;
	protected String resourceVersion;
	protected Integer displayOrder = 0;
	//iac
	protected String iacProvider; //terraform, cf, arm
	protected String iacProviderType; //terraform - vmware
	protected String iacType; //resource type
	//audit
	protected Boolean rootType = false;
	protected Boolean defaultType = false;
	protected Boolean enabled = true;
	protected Boolean hasCosting = false;
	protected Boolean isPlugin = false;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApiCategory() {
		return apiCategory;
	}

	public void setApiCategory(String apiCategory) {
		this.apiCategory = apiCategory;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getCostingCode() {
		return costingCode;
	}

	public void setCostingCode(String costingCode) {
		this.costingCode = costingCode;
	}

	public String getCostingType() {
		return costingType;
	}

	public void setCostingType(String costingType) {
		this.costingType = costingType;
	}

	public String getProvisionCode() {
		return provisionCode;
	}

	public void setProvisionCode(String provisionCode) {
		this.provisionCode = provisionCode;
	}

	public String getMorpheusType() {
		return morpheusType;
	}

	public void setMorpheusType(String morpheusType) {
		this.morpheusType = morpheusType;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceIcon() {
		return resourceIcon;
	}

	public void setResourceIcon(String resourceIcon) {
		this.resourceIcon = resourceIcon;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getIacProvider() {
		return iacProvider;
	}

	public void setIacProvider(String iacProvider) {
		this.iacProvider = iacProvider;
	}

	public String getIacProviderType() {
		return iacProviderType;
	}

	public void setIacProviderType(String iacProviderType) {
		this.iacProviderType = iacProviderType;
	}

	public String getIacType() {
		return iacType;
	}

	public void setIacType(String iacType) {
		this.iacType = iacType;
	}

	public Boolean getRootType() {
		return rootType;
	}

	public void setRootType(Boolean rootType) {
		this.rootType = rootType;
	}

	public Boolean getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(Boolean defaultType) {
		this.defaultType = defaultType;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getHasCosting() {
		return hasCosting;
	}

	public void setHasCosting(Boolean hasCosting) {
		this.hasCosting = hasCosting;
	}

	public Boolean getPlugin() {
		return isPlugin;
	}

	public void setPlugin(Boolean plugin) {
		isPlugin = plugin;
		markDirty("isPlugin", isPlugin);
	}
}
