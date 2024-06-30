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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.AppIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;
import com.morpheusdata.model.serializers.ModelCollectionAsIdsOnlySerializer;
import com.morpheusdata.model.serializers.ModelIdCodeNameSerializer;
import com.morpheusdata.model.serializers.ModelIdUuidCodeNameSerializer;

import java.util.Collection;
import java.util.List;

public class App extends AppIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String description;
	protected String internalId;
	protected String externalId;
	protected String status;
	protected String uuid;
	//associations
	@JsonSerialize(using=ModelIdUuidCodeNameSerializer.class)
	protected AppTemplate template;
	@JsonSerialize(using=ModelIdCodeNameSerializer.class)
	protected AppTemplateType templateType;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ComputeSite site;
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	protected List<AppInstance> instances;
	@JsonSerialize(using= ModelCollectionAsIdsOnlySerializer.class)
	public Collection<AccountResource> resources;
	protected User createdBy;

	public Account getAccount() { return account; }

	public String getDescription() {
		return description;
	}

	public String getInternalId() {
		return internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getStatus() {
		return status;
	}

	public String getUuid() {
		return uuid;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public AppTemplate getTemplate() {
		return template;
	}

	public void setTemplate(AppTemplate template) {
		this.template = template;
		markDirty("template", template);
	}

	public AppTemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(AppTemplateType templateType) {
		this.templateType = templateType;
		markDirty("templateType", templateType);
	}

	public ComputeSite getSite() {
		return site;
	}

	public void setSite(ComputeSite site) {
		this.site = site;
		markDirty("site", site);
	}

	public List<AppInstance> getInstances() {
		return instances;
	}

	public void setInstances(List<AppInstance> instances) {
		this.instances = instances;
		markDirty("instances", instances);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public Collection<AccountResource> getResources() {
		return resources;
	}

	public void setResources(Collection<AccountResource> resources) {
		this.resources = resources;
		markDirty("resources", resources);
	}
}
