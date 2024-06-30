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

public class JobType extends MorpheusModel {
	protected String name;
	protected String code;
	protected String category;
	protected String title;
	protected String description;
	protected String iconPath;
	protected Boolean enabled = true;
	//execution
	protected String executionService;
	protected String integrationService;
	//where can it run
	protected Boolean onInstance = false;
	protected Boolean onContainer = false;
	protected Boolean onServer = false;
	protected Boolean onMorpheus = false;
	protected Boolean onRemote = false;
	protected Boolean onIntegration = false;
	//capabilities
	protected Boolean canCreate = true;
	protected Boolean canEdit = false;
	protected Boolean canDelete = false;
	protected Boolean canExecute = false;

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

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getExecutionService() {
		return executionService;
	}

	public void setExecutionService(String executionService) {
		this.executionService = executionService;
	}

	public String getIntegrationService() {
		return integrationService;
	}

	public void setIntegrationService(String integrationService) {
		this.integrationService = integrationService;
	}

	public Boolean getOnInstance() {
		return onInstance;
	}

	public void setOnInstance(Boolean onInstance) {
		this.onInstance = onInstance;
	}

	public Boolean getOnContainer() {
		return onContainer;
	}

	public void setOnContainer(Boolean onContainer) {
		this.onContainer = onContainer;
	}

	public Boolean getOnServer() {
		return onServer;
	}

	public void setOnServer(Boolean onServer) {
		this.onServer = onServer;
	}

	public Boolean getOnMorpheus() {
		return onMorpheus;
	}

	public void setOnMorpheus(Boolean onMorpheus) {
		this.onMorpheus = onMorpheus;
	}

	public Boolean getOnRemote() {
		return onRemote;
	}

	public void setOnRemote(Boolean onRemote) {
		this.onRemote = onRemote;
	}

	public Boolean getOnIntegration() {
		return onIntegration;
	}

	public void setOnIntegration(Boolean onIntegration) {
		this.onIntegration = onIntegration;
	}

	public Boolean getCanCreate() {
		return canCreate;
	}

	public void setCanCreate(Boolean canCreate) {
		this.canCreate = canCreate;
	}

	public Boolean getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Boolean canEdit) {
		this.canEdit = canEdit;
	}

	public Boolean getCanDelete() {
		return canDelete;
	}

	public void setCanDelete(Boolean canDelete) {
		this.canDelete = canDelete;
	}

	public Boolean getCanExecute() {
		return canExecute;
	}

	public void setCanExecute(Boolean canExecute) {
		this.canExecute = canExecute;
	}
}
