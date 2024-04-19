package com.morpheusdata.model;

import com.morpheusdata.model.projection.VirtualImageTypeIdentityProjection;

public class VirtualImageType extends VirtualImageTypeIdentityProjection {

	/**
	 * i8n display code
	 */
	protected String nameCode;
	/**
	 * active and usable in the system
	 */
	protected Boolean active = true;
	/**
	 * visible to users
	 */
	protected Boolean visible = true;
	/**
	 * soft delete flag
	 */
	protected Boolean deleted = false;
	/**
	 * Images associated with this type can be created by users
	 */
	protected Boolean creatable = true;

	public String getNameCode() {
		return nameCode;
	}

	public void setNameCode(String nameCode) {
		this.nameCode = nameCode;
		markDirty("nameCode", nameCode, this.nameCode);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active, this.active);
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
		markDirty("visible", visible, this.visible);
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
		markDirty("deleted", deleted, this.deleted);
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
		markDirty("creatable", creatable, this.creatable);
	}
}
