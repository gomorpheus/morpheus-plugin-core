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

/**
 * There are several different types of volume types across various cloud providers.
 *
 * @author Bob Whiton
 * @since 0.9.0
 */
public class StorageVolumeType extends MorpheusModel {
	private String name;
	private String code;
	private String externalId;
	protected Boolean enabled = true;
	protected String description;
	protected String displayName;
	protected String volumeType = "disk";
	protected Integer displayOrder = 1;
	protected Boolean customLabel = true;
	protected Boolean customSize = true;
	protected Boolean defaultType = false;
	protected Boolean autoDelete = true;
	protected Boolean hasDatastore = true;
	protected Boolean allowSearch = false;
	protected String volumeCategory = "disk";
	protected Boolean resizable = false;
	protected Boolean planResizable = false; // used to override resizable if the user can't resize the volume but plan changes will resize the volume;
	protected Long minStorage = null;
	protected Long maxStorage = null;
	protected Boolean configurableIOPS = false;
	protected Long minIOPS = null;
	protected Long maxIOPS = null;

	// associations
	Collection<OptionType> optionTypes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getVolumeType() {
		return volumeType;
	}

	public void setVolumeType(String volumeType) {
		this.volumeType = volumeType;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getCustomLabel() {
		return customLabel;
	}

	public void setCustomLabel(Boolean customLabel) {
		this.customLabel = customLabel;
	}

	public Boolean getCustomSize() {
		return customSize;
	}

	public void setCustomSize(Boolean customSize) {
		this.customSize = customSize;
	}

	public Boolean getDefaultType() {
		return defaultType;
	}

	public void setDefaultType(Boolean defaultType) {
		this.defaultType = defaultType;
	}

	public Boolean getAutoDelete() {
		return autoDelete;
	}

	public void setAutoDelete(Boolean autoDelete) {
		this.autoDelete = autoDelete;
	}

	public Boolean getHasDatastore() {
		return hasDatastore;
	}

	public void setHasDatastore(Boolean hasDatastore) {
		this.hasDatastore = hasDatastore;
	}

	public Boolean getAllowSearch() {
		return allowSearch;
	}

	public void setAllowSearch(Boolean allowSearch) {
		this.allowSearch = allowSearch;
	}

	public String getVolumeCategory() {
		return volumeCategory;
	}

	public void setVolumeCategory(String volumeCategory) {
		this.volumeCategory = volumeCategory;
	}

	public Boolean getResizable() {
		return resizable;
	}

	public void setResizable(Boolean resizable) {
		this.resizable = resizable;
	}

	public Boolean getPlanResizable() {
		return planResizable;
	}

	public void setPlanResizable(Boolean planResizable) {
		this.planResizable = planResizable;
	}

	public Long getMinStorage() {
		return minStorage;
	}

	public void setMinStorage(Long minStorage) {
		this.minStorage = minStorage;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		this.maxStorage = maxStorage;
	}

	public Boolean getConfigurableIOPS() {
		return configurableIOPS;
	}

	public void setConfigurableIOPS(Boolean configurableIOPS) {
		this.configurableIOPS = configurableIOPS;
	}

	public Long getMinIOPS() {
		return minIOPS;
	}

	public void setMinIOPS(Long minIOPS) {
		this.minIOPS = minIOPS;
	}

	public Long getMaxIOPS() {
		return maxIOPS;
	}

	public void setMaxIOPS(Long minIOPS) {
		this.maxIOPS = maxIOPS;
	}

	public Collection<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(Collection<OptionType> optionTypes) {
		markDirty("optionTypes", this.optionTypes);
		this.optionTypes = optionTypes;
	}
}
