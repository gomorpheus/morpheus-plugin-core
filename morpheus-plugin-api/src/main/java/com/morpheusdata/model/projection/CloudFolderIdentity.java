package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudFolderService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ComputeZoneFolder} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusCloudFolderService
 * @author Bob Whiton
 * @since 0.15.3
 */
public class CloudFolderIdentity extends MorpheusIdentityModel {

	protected String type = "default";
	protected String internalId;
	protected String name;
	protected String externalId;
	protected String category;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}
}
