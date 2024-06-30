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
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelCollectionIdUuidCodeNameSerializer;

public class AppTemplate extends MorpheusModel implements IModelUuidCodeName {

	//owndership
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	//fields
	protected User createdBy;
	protected String name;
	protected String code;
	protected String description;
	protected String category;
	protected Boolean active;
	protected Boolean custom;
	protected String secretType;
	protected String secretKey;
	protected String visibility;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String uuid;
	protected String syncSource;
	protected Attachment templateImage;
	protected Attachment templateImageDark;
	//associations
	protected AppTemplateType templateType;
	protected FileContent content;
	//collections
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<InstanceType> instanceTypes;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<OptionType> options;
	@JsonSerialize(using = ModelCollectionIdUuidCodeNameSerializer.class)
	protected List<ResourceSpecTemplate> specTemplates;
	@JsonSerialize(using = ModelCollectionIdCodeNameSerializer.class)
	protected List<EnvironmentVariableType> environmentVariables;
	//protected List<RoleAppTemplate> roleAppTemplates;
	//protected List<Label> labels;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getCustom() {
		return custom;
	}

	public void setCustom(Boolean custom) {
		this.custom = custom;
		markDirty("custom", custom);
	}

	public String getSecretType() {
		return secretType;
	}

	public void setSecretType(String secretType) {
		this.secretType = secretType;
		markDirty("secretType", secretType);
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
		markDirty("secretKey", secretKey);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	@Override
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getSyncSource() {
		return syncSource;
	}

	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
		markDirty("syncSource", syncSource);
	}

	public Attachment getTemplateImage() {
		return templateImage;
	}

	public void setTemplateImage(Attachment templateImage) {
		this.templateImage = templateImage;
		markDirty("templateImage", templateImage);
	}

	public Attachment getTemplateImageDark() {
		return templateImageDark;
	}

	public void setTemplateImageDark(Attachment templateImageDark) {
		this.templateImageDark = templateImageDark;
		markDirty("templateImageDark", templateImageDark);
	}

	public AppTemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(AppTemplateType templateType) {
		this.templateType = templateType;
		markDirty("templateType", templateType);
	}

	public FileContent getContent() {
		return content;
	}

	public void setContent(FileContent content) {
		this.content = content;
		markDirty("content", content);
	}

	public List<InstanceType> getInstanceTypes() {
		return instanceTypes;
	}

	public void setInstanceTypes(List<InstanceType> instanceTypes) {
		this.instanceTypes = instanceTypes;
		markDirty("instanceTypes", instanceTypes);
	}

	public List<OptionType> getOptions() {
		return options;
	}

	public void setOptions(List<OptionType> options) {
		this.options = options;
		markDirty("options", options);
	}

	public List<ResourceSpecTemplate> getSpecTemplates() {
		return specTemplates;
	}

	public void setSpecTemplates(List<ResourceSpecTemplate> specTemplates) {
		this.specTemplates = specTemplates;
		markDirty("specTemplates", specTemplates);
	}

	public List<EnvironmentVariableType> getEnvironmentVariables() {
		return environmentVariables;
	}

	public void setEnvironmentVariables(List<EnvironmentVariableType> environmentVariables) {
		this.environmentVariables = environmentVariables;
		markDirty("environmentVariables", environmentVariables);
	}
}
