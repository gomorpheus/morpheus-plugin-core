package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudPoolService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.CloudPool} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusCloudPoolService
 * @author Mike Truso, David Estes
 * @since 0.15.3
 *
 */
public class CloudPoolIdentity extends MorpheusIdentityModel {
	protected String type;
	protected String uniqueId;
	protected String internalId;
	protected String name;
	protected String externalId;
	protected String category;
	protected Long ownerId;
	protected Boolean inventory = true;
	protected String regionCode;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		markDirty("type", type);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
		markDirty("ownerId", ownerId);
	}

	public Boolean getInventory() {
		return inventory;
	}

	public void setInventory(Boolean inventory) {
		this.inventory = inventory;
		markDirty("inventory", inventory);
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
		markDirty("regionCode", regionCode);
	}
}
