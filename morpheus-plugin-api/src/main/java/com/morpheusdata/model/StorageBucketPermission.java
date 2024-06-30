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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class StorageBucketPermission extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String category;
	protected String code;
	protected String sourceUser;
	protected String sourceRole;
	protected String sourceAddress;
	protected String targetAddress;
	protected String targetUser;
	protected String targetRole;
	protected String targetPath;
	protected Boolean hidden;
	protected Boolean enabled;
	protected Boolean canRead;
	protected Boolean canWrite;
	protected Boolean canAdmin;
	protected String refType;
	protected Long refId;
	protected String internalId;
	protected String externalId;
	protected Date dateCreated;
	protected Date lastUpdated;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getSourceUser() {
		return sourceUser;
	}
	
	public void setSourceUser(String sourceUser) {
		this.sourceUser = sourceUser;
		markDirty("sourceUser", sourceUser);
	}

	public String getSourceRole() {
		return sourceRole;
	}
	
	public void setSourceRole(String sourceRole) {
		this.sourceRole = sourceRole;
		markDirty("sourceRole", sourceRole);
	}

	public String getSourceAddress() {
		return sourceAddress;
	}
	
	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
		markDirty("sourceAddress", sourceAddress);
	}

	public String getTargetAddress() {
		return targetAddress;
	}
	
	public void setTargetAddress(String targetAddress) {
		this.targetAddress = targetAddress;
		markDirty("targetAddress", targetAddress);
	}

	public String getTargetUser() {
		return targetUser;
	}
	
	public void setTargetUser(String targetUser) {
		this.targetUser = targetUser;
		markDirty("targetUser", targetUser);
	}

	public String getTargetRole() {
		return targetRole;
	}
	
	public void setTargetRole(String targetRole) {
		this.targetRole = targetRole;
		markDirty("targetRole", targetRole);
	}

	public String getTargetPath() {
		return targetPath;
	}
	
	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
		markDirty("targetPath", targetPath);
	}

	public Boolean getHidden() {
		return hidden;
	}
	
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
		markDirty("hidden", hidden);
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public Boolean getCanRead() {
		return canRead;
	}
	
	public void setCanRead(Boolean canRead) {
		this.canRead = canRead;
		markDirty("canRead", canRead);
	}

	public Boolean getCanWrite() {
		return canWrite;
	}
	
	public void setCanWrite(Boolean canWrite) {
		this.canWrite = canWrite;
		markDirty("canWrite", canWrite);
	}

	public Boolean getCanAdmin() {
		return canAdmin;
	}
	
	public void setCanAdmin(Boolean canAdmin) {
		this.canAdmin = canAdmin;
		markDirty("canAdmin", canAdmin);
	}

	public String getRefType() {
		return refType;
	}
	
	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
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

}
