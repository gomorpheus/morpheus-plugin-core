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

public class InstanceScaleType extends MorpheusModel {
	protected String code;
	protected String name;
	protected String description;
	protected String externalType;
	protected Integer displayOrder = 0;
	protected Boolean creatable = true;
	protected Boolean selectable = true;
	protected Boolean internalControl = false;
	protected Boolean enabled = true;
	protected String scaleService;

	// plugin shit
	protected Boolean isPlugin;
	protected Boolean isEmbedded = false;
	protected String pluginLogoPrefix;
	protected String pluginIconPath;
	protected String pluginIconDarkPath;
	protected String pluginIconHidpiPath;
	protected String pluginIconDarkHidpiPath;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("createable", creatable);
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
		markDirty("selectable", selectable);
	}

	public Boolean getInternalControl() {
		return internalControl;
	}

	public void setInternalControl(Boolean internalControl) {
		this.internalControl = internalControl;
		markDirty("internalControl", internalControl);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public String getScaleService() {
		return scaleService;
	}

	public void setScaleService(String scaleService) {
		this.scaleService = scaleService;
		markDirty("scaleService", scaleService);
	}

	public Boolean getPlugin() {
		return isPlugin;
	}

	public void setPlugin(Boolean plugin) {
		isPlugin = plugin;
		markDirty("plugin", plugin);
	}

	public Boolean getEmbedded() {
		return isEmbedded;
	}

	public void setEmbedded(Boolean embedded) {
		isEmbedded = embedded;
		markDirty("embedded", embedded);
	}

	public String getPluginLogoPrefix() {
		return pluginLogoPrefix;
	}

	public void setPluginLogoPrefix(String pluginLogoPrefix) {
		this.pluginLogoPrefix = pluginLogoPrefix;
		markDirty("pluginLogoPrefix", pluginLogoPrefix);
	}

	public String getPluginIconPath() {
		return pluginIconPath;
	}

	public void setPluginIconPath(String pluginIconPath) {
		this.pluginIconPath = pluginIconPath;
		markDirty("pluginIconPath", pluginIconPath);
	}

	public String getPluginIconDarkPath() {
		return pluginIconDarkPath;
	}

	public void setPluginIconDarkPath(String pluginIconDarkPath) {
		this.pluginIconDarkPath = pluginIconDarkPath;
		markDirty("pluginIconDarkPath", pluginIconDarkPath);
	}

	public String getPluginIconHidpiPath() {
		return pluginIconHidpiPath;
	}

	public void setPluginIconHidpiPath(String pluginIconHidpiPath) {
		this.pluginIconHidpiPath = pluginIconHidpiPath;
		markDirty("pluginIconHidpiPath", pluginIconHidpiPath);
	}

	public String getPluginIconDarkHidpiPath() {
		return pluginIconDarkHidpiPath;
	}

	public void setPluginIconDarkHidpiPath(String pluginIconDarkHidpiPath) {
		this.pluginIconDarkHidpiPath = pluginIconDarkHidpiPath;
		markDirty("pluginIconDarkHidpiPath", pluginIconDarkHidpiPath);
	}
}
