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

public class DashboardItemType extends MorpheusModel {
	
	protected String uuid;
	protected String name;
	protected String code;
	protected String category;
	protected String title;
	protected String description;
	protected Boolean enabled = true;
	protected Boolean canExecute = true;
	//view info
	protected String uiSize; //xs-1,s-1,m-1,l-1,xl-1
	protected String uiType; //widget class etc.
	protected String uiSection; //area to group with.
	protected String scriptPath;
	protected String templatePath;
	//permissions
	protected Permission permission;
	protected List<String> accessTypes;

	//options
	protected List<OptionType> optionTypes;

	//getters and setters
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getCanExecute() {
		return canExecute;
	}

	public void setCanExecute(Boolean canExecute) {
		this.canExecute = canExecute;
	}

	public String getUiSize() {
		return uiSize;
	}

	public void setUiSize(String uiSize) {
		this.uiSize = uiSize;
	}

	public String getUiType() {
		return uiType;
	}

	public void setUiType(String uiType) {
		this.uiType = uiType;
	}

	public String getUiSection() {
		return uiSection;
	}

	public void setUiSection(String uiSection) {
		this.uiSection = uiSection;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		markDirty("scriptPath", scriptPath);
		this.scriptPath = scriptPath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		markDirty("templatePath", templatePath);
		this.templatePath = templatePath;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		markDirty("permission", permission);
		this.permission = permission;
	}

	public List<String> getAccessTypes() {
		return accessTypes;
	}

	public void setAccessTypes(List<String> accessTypes) {
		this.accessTypes = accessTypes;
	}
	
}
