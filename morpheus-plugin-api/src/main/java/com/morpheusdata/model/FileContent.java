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

import java.util.Date;

public class FileContent extends MorpheusModel {
    
	protected String name;
	protected String code;
	protected String fileVersion;
	protected AccountIntegration integration;
	protected CodeRepository repository;
	protected StorageBucket storageProvider;
	protected String contentType;
	protected String sourceType;
	protected String contentPath;
	protected String content;
	protected String contentRef;
	protected Boolean enabled;
	protected Boolean editable;
	protected String internalId;
	protected String externalId;
	protected String uuid;
	//audit
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String createdBy;
	protected String updatedBy;
	
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

	public String getFileVersion() {
		return fileVersion;
	}

	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
		markDirty("fileVersion", fileVersion);
	}

	public AccountIntegration getIntegration() {
		return integration;
	}

	public void setIntegration(AccountIntegration integration) {
		this.integration = integration;
		markDirty("integration", integration);
	}

	public CodeRepository getRepository() {
		return repository;
	}

	public void setRepository(CodeRepository repository) {
		this.repository = repository;
		markDirty("repository", repository);
	}

	public StorageBucket getStorageProvider() {
		return storageProvider;
	}

	public void setStorageProvider(StorageBucket storageProvider) {
		this.storageProvider = storageProvider;
		markDirty("storageProvider", storageProvider);
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
		markDirty("contentType", contentType);
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
		markDirty("sourceType", sourceType);
	}

	public String getContentPath() {
		return contentPath;
	}

	public void setContentPath(String contentPath) {
		this.contentPath = contentPath;
		markDirty("contentPath", contentPath);
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
		markDirty("content", content);
	}

	public String getContentRef() {
		return contentRef;
	}
	
	public void setContentRef(String contentRef) {
		this.contentRef = contentRef;
		markDirty("contentRef", contentRef);
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}
	
	public Boolean getEditable() {
		return editable;
	}
	
	public void setEditable(Boolean editable) {
		this.editable = editable;
		markDirty("editable", editable);
	}
	
	public String getInternalId() {
		return internalId;
	}
	
	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getExternalId() {
		return externalId;
	}
	
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
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

	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy", createdBy);
	}

	public String getUpdatedBy() {
		return updatedBy;
	}
	
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
		markDirty("updatedBy", updatedBy);
	}
  
}
