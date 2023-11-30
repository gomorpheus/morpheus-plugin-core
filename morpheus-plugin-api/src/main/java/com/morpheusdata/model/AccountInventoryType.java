package com.morpheusdata.model;

/**
 * Represents the type for inventory files
 * @author bdwheeler
 * @since 0.16.1
 */
public class AccountInventoryType extends MorpheusModel {

	protected String name;
	protected String code;
	protected String description;
	protected Boolean enabled = true;
	protected Boolean creatable = true;

	/**
	 * Gets the name of the inventory type for display
	 * @return the current inventory type name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the inventory type for reference and display
	 * @param name the name to be assigned to the inventory type
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the more detailed description of the inventory type.
	 * @return the description information
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the inventory type
	 * @param description the description string to be saved
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getCreatable() {
		return creatable;
	}

	public void setCreatable(Boolean creatable) {
		this.creatable = creatable;
	}

}
