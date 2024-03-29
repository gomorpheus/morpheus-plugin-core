package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.ComputeZoneFolderIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.List;

public class CloudFolder extends ComputeZoneFolderIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String visibility = "private";
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Cloud cloud;
	protected String code;
	protected String refType;
	protected Long refId;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected CloudFolder parent;
	protected List<CloudFolder> folders = new ArrayList<>();
	protected Boolean readOnly = false;
	protected Boolean active = true;
	protected Boolean defaultFolder = false;
	protected Boolean defaultStore = false;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
		markDirty("visibility", visibility);
	}

	public Cloud getCloud() {
		return cloud;
	}

	public void setCloud(Cloud cloud) {
		this.cloud = cloud;
		markDirty("cloud", cloud);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
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

	public com.morpheusdata.model.CloudFolder getParent() {
		return parent;
	}

	public void setParent(com.morpheusdata.model.CloudFolder parent) {
		this.parent = parent;
		markDirty("parent", parent);
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
		markDirty("readOnly", readOnly);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getDefaultFolder() {
		return defaultFolder;
	}

	public void setDefaultFolder(Boolean defaultFolder) {
		this.defaultFolder = defaultFolder;
		markDirty("defaultFolder", defaultFolder);
	}

	public Boolean getDefaultStore() {
		return defaultStore;
	}

	public void setDefaultStore(Boolean defaultStore) {
		this.defaultStore = defaultStore;
		markDirty("defaultStore", defaultStore);
	}

	public List<CloudFolder> getFolders() {
		return folders;
	}

	public void setFolders(List<CloudFolder> folders) {
		this.folders = folders;
		markDirty("folders", folders);
	}
}
