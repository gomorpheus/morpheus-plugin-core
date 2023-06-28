package com.morpheusdata.model;

public class InstanceScaleType extends MorpheusModel {
	protected String code;
	protected String name;
	protected String description;
	protected String externalType;
	protected Integer displayOrder = 0;
	protected Boolean creatable = true;
	protected Boolean selectable = true;
	protected Boolean internalControl = false;
	protected Boolean enabled = true;

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

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("createable", creatable);
	}

	public Boolean getSelectable() {
		return selectable;
	}

	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
		markDirty("selectable", selectable);
	}

	public Boolean getInternalControl() {
		return internalControl;
	}

	public void setInternalControl(Boolean internalControl) {
		this.internalControl = internalControl;
		markDirty("internalControl", internalControl);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}
}
