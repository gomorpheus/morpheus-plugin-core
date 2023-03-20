package com.morpheusdata.model;

/**
 * Provides a means to set predefined tiers on memory, storage, cores, and cpu.
 */
public class ResourceSpecTemplateType extends MorpheusModel {

	protected String name;
	protected String category;
	protected String code;
	protected String resourceName;
	protected String description;
	protected String internalId;
	protected String externalId;
	protected String externalType;
	protected String iconPath;
	protected Boolean enabled = true;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
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
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
		markDirty("resourceName", resourceName);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
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

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
		markDirty("iconPath", iconPath);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

}