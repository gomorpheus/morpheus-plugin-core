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

public class StorageServerType extends MorpheusModel {

	protected String code;
	protected String name;
	protected String description;
	protected String storageService;
	protected Boolean enabled = true;
	protected Boolean creatable = true;
	protected Boolean hasNamespaces = false;
	protected Boolean hasGroups = false;
	protected Boolean hasBlock = false;
	protected Boolean hasObject = false;
	protected Boolean hasFile = false;
	protected Boolean hasDatastore = false;
	protected Boolean hasDisks = false;
	protected Boolean hasHosts = false;
	protected Boolean createNamespaces = false;
	protected Boolean createGroup = false;
	protected Boolean createBlock = false;
	protected Boolean createObject = false;
	protected Boolean createFile = false;
	protected Boolean createDatastore = false;
	protected Boolean createDisk = false;
	protected Boolean createHost = false;
	protected String iconCode;
	protected Boolean hasFileBrowser = false;

	// associations
	Collection<OptionType> optionTypes;
	// Collection<OptionType> groupOptionTypes;
	Collection<StorageVolumeType> volumeTypes;
	Collection<OptionType> bucketOptionTypes;
	// Collection<OptionType> shareOptionTypes;
	// Collection<OptionType> shareAccessOptionTypes;

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
	
	public String getStorageService() {
		return storageService;
	}
	
	public void setStorageService(String storageService) {
		this.storageService = storageService;
		markDirty("storageService", storageService);
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}
	
	public Boolean getCreatable() {
		return creatable;
	}
	
	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable);
	}
	
	public Boolean getHasNamespaces() {
		return hasNamespaces;
	}
	
	public void setHasNamespaces(Boolean hasNamespaces) {
		this.hasNamespaces = hasNamespaces;
		markDirty("hasNamespaces", hasNamespaces);
	}
	
	public Boolean getHasGroups() {
		return hasGroups;
	}
	
	public void setHasGroups(Boolean hasGroups) {
		this.hasGroups = hasGroups;
		markDirty("hasGroups", hasGroups);
	}
	
	public Boolean getHasBlock() {
		return hasBlock;
	}
	
	public void setHasBlock(Boolean hasBlock) {
		this.hasBlock = hasBlock;
		markDirty("hasBlock", hasBlock);
	}
	
	public Boolean getHasObject() {
		return hasObject;
	}
	
	public void setHasObject(Boolean hasObject) {
		this.hasObject = hasObject;
		markDirty("hasObject", hasObject);
	}
	
	public Boolean getHasFile() {
		return hasFile;
	}
	
	public void setHasFile(Boolean hasFile) {
		this.hasFile = hasFile;
		markDirty("hasFile", hasFile);
	}
	
	public Boolean getHasDatastore() {
		return hasDatastore;
	}
	
	public void setHasDatastore(Boolean hasDatastore) {
		this.hasDatastore = hasDatastore;
		markDirty("hasDatastore", hasDatastore);
	}
	
	public Boolean getHasDisks() {
		return hasDisks;
	}
	
	public void setHasDisks(Boolean hasDisks) {
		this.hasDisks = hasDisks;
		markDirty("hasDisks", hasDisks);
	}
	
	public Boolean getHasHosts() {
		return hasHosts;
	}
	
	public void setHasHosts(Boolean hasHosts) {
		this.hasHosts = hasHosts;
		markDirty("hasHosts", hasHosts);
	}
	
	public Boolean getCreateNamespaces() {
		return createNamespaces;
	}
	
	public void setCreateNamespaces(Boolean createNamespaces) {
		this.createNamespaces = createNamespaces;
		markDirty("createNamespaces", createNamespaces);
	}
	
	public Boolean getCreateGroup() {
		return createGroup;
	}
	
	public void setCreateGroup(Boolean createGroup) {
		this.createGroup = createGroup;
		markDirty("createGroup", createGroup);
	}
	
	public Boolean getCreateBlock() {
		return createBlock;
	}
	
	public void setCreateBlock(Boolean createBlock) {
		this.createBlock = createBlock;
		markDirty("createBlock", createBlock);
	}
	
	public Boolean getCreateObject() {
		return createObject;
	}
	
	public void setCreateObject(Boolean createObject) {
		this.createObject = createObject;
		markDirty("createObject", createObject);
	}
	
	public Boolean getCreateFile() {
		return createFile;
	}
	
	public void setCreateFile(Boolean createFile) {
		this.createFile = createFile;
		markDirty("createFile", createFile);
	}
	
	public Boolean getCreateDatastore() {
		return createDatastore;
	}
	
	public void setCreateDatastore(Boolean createDatastore) {
		this.createDatastore = createDatastore;
		markDirty("createDatastore", createDatastore);
	}
	
	public Boolean getCreateDisk() {
		return createDisk;
	}
	
	public void setCreateDisk(Boolean createDisk) {
		this.createDisk = createDisk;
		markDirty("createDisk", createDisk);
	}
	
	public Boolean getCreateHost() {
		return createHost;
	}
	
	public void setCreateHost(Boolean createHost) {
		this.createHost = createHost;
		markDirty("createHost", createHost);
	}
	
	public String getIconCode() {
		return iconCode;
	}
	
	public void setIconCode(String iconCode) {
		this.iconCode = iconCode;
		markDirty("iconCode", iconCode);
	}
	
	public Boolean getHasFileBrowser() {
		return hasFileBrowser;
	}
	
	public void setHasFileBrowser(Boolean hasFileBrowser) {
		this.hasFileBrowser = hasFileBrowser;
		markDirty("hasFileBrowser", hasFileBrowser);
	}

	public Collection<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(Collection<OptionType> optionTypes) {
		markDirty("optionTypes", this.optionTypes);
		this.optionTypes = optionTypes;
	}

	public Collection<StorageVolumeType> getVolumeTypes() {
		return volumeTypes;
	}

	public void setVolumeTypes(Collection<StorageVolumeType> volumeTypes) {
		markDirty("volumeTypes", this.volumeTypes);
		this.volumeTypes = volumeTypes;
	}

	public Collection<OptionType> getBucketOptionTypes() {
		return bucketOptionTypes;
	}

	public void setBucketOptionTypes(Collection<OptionType> bucketOptionTypes) {
		markDirty("bucketOptionTypes", this.bucketOptionTypes);
		this.bucketOptionTypes = bucketOptionTypes;
	}
}
