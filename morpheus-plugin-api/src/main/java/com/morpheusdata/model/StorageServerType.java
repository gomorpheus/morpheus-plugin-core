package com.morpheusdata.model;

public class StorageServerType extends MorpheusModel {

	protected String code;
	protected String name;
	protected String description;
	protected String storageService;
	protected Boolean enabled;
	protected Boolean creatable;
	protected Boolean hasNamespaces;
	protected Boolean hasGroups;
	protected Boolean hasBlock;
	protected Boolean hasObject;
	protected Boolean hasFile;
	protected Boolean hasDatastore;
	protected Boolean hasDisks;
	protected Boolean hasHosts;
	protected Boolean createNamespaces;
	protected Boolean createGroup;
	protected Boolean createBlock;
	protected Boolean createObject;
	protected Boolean createFile;
	protected Boolean createDatastore;
	protected Boolean createDisk;
	protected Boolean createHost;
	protected String iconCode;
	protected Boolean hasFileBrowser;

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

}
