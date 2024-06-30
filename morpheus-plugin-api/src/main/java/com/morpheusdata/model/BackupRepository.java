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
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class BackupRepository extends MorpheusModel {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected BackupProvider backupProvider;
	// TODO: storage buckets? Need a storage provider plugin to do this?
	// protected StorageBucket storageProvider;
	protected String name;
	protected String code;
	protected String category;
	protected Boolean enabled = true;
	protected String platform = "all"; //linux, windows, etc
	protected String internalId;
	protected String externalId;
	protected String refType;
	protected Long refId;
	protected Long maxStorage;
	protected Long usedStorage;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String visibility = "private";

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public BackupProvider getBackupProvider() {
		return backupProvider;
	}

	public void setBackupProvider(BackupProvider backupProvider) {
		markDirty("backupProvider", backupProvider, this.backupProvider);
		this.backupProvider = backupProvider;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name, this.name);
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code, this.code);
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		markDirty("category", category, this.category);
		this.category = category;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled, this.enabled);
		this.enabled = enabled;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		markDirty("platform", platform, this.platform);
		this.platform = platform;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		markDirty("internalId", internalId, this.internalId);
		this.internalId = internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		markDirty("externalId", externalId, this.externalId);
		this.externalId = externalId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		markDirty("refType", refType, this.refType);
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		markDirty("refId", refId, this.refId);
		this.refId = refId;
	}

	public Long getMaxStorage() {
		return maxStorage;
	}

	public void setMaxStorage(Long maxStorage) {
		markDirty("maxStorage", maxStorage, this.maxStorage);
		this.maxStorage = maxStorage;
	}

	public Long getUsedStorage() {
		return usedStorage;
	}

	public void setUsedStorage(Long usedStorage) {
		markDirty("usedStorage", usedStorage, this.usedStorage);
		this.usedStorage = usedStorage;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		markDirty("dateCreated", dateCreated, this.dateCreated);
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		markDirty("lastUpdated", lastUpdated, this.lastUpdated);
		this.lastUpdated = lastUpdated;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		markDirty("visibility", visibility, this.visibility);
		this.visibility = visibility;
	}
}
