package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * This model represents logical groupings/separations within a cloud for virtualization management
 * for example Vmware Clusters/Resource Pools, or AWS VPCs, Azure Resource Groups, OpenStack Projects
 * @author David Estes
 * @since 0.15.3
 */
public class CloudPool extends ComputeZonePoolIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account owner;
	public String visibility = "private";
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Cloud cloud;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public ComputeServerGroup serverGroup;
	public String displayName;
	public String code;
	public String treeName;
	public String rawData;
	public String type = "default";
	public String description;
	public String refType;
	public Long refId;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public CloudPool parent;
	public Boolean active = true;
	public Boolean readOnly = false;
	public Boolean defaultPool = false;
	public Boolean hidden = false;
	public String iacId; //id for infrastructure as code integrations
	public Status status = Status.available;
	public Boolean editable;
	public Boolean removable;

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

	public ComputeServerGroup getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(ComputeServerGroup serverGroup) {
		this.serverGroup = serverGroup;
		markDirty("serverGroup", serverGroup);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName", displayName);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getTreeName() {
		return treeName;
	}

	public void setTreeName(String treeName) {
		this.treeName = treeName;
		markDirty("treeName", treeName);
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
		markDirty("rawData", rawData);
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
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

	public CloudPool getParent() {
		return parent;
	}

	public void setParent(CloudPool parent) {
		this.parent = parent;
		markDirty("parent", parent);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
		markDirty("readOnly", readOnly);
	}

	public Boolean getDefaultPool() {
		return defaultPool;
	}

	public void setDefaultPool(Boolean defaultPool) {
		this.defaultPool = defaultPool;
		markDirty("defaultPool", defaultPool);
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
		markDirty("hidden", hidden);
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		markDirty("status", status);
	}

	public Boolean getEditable() {
		return editable;
	}

	public void setEditable(Boolean editable) {
		this.editable = editable;
		markDirty("editable", editable);
	}

	public Boolean getRemovable() {
		return removable;
	}

	public void setRemovable(Boolean removable) {
		this.removable = removable;
		markDirty("removable", removable);
	}

	enum Status {
		deploying,
		failed,
		available
	}
}
