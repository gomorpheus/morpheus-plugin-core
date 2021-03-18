package com.morpheusdata.model;

import java.util.List;

public class ReportType extends MorpheusModel {
	protected String name;
	protected String description;
	protected List<OptionType> optionTypes;
	protected String category; //cloudInventory, cloudCapacity, cloudCost, groupInventory, groupCapacity, groupCost
	protected Boolean visible = false;
	protected Boolean masterOnly = false;
	protected Boolean supportsAllZoneTypes = true;
	protected Boolean ownerOnly = false;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getMasterOnly() {
		return masterOnly;
	}

	public void setMasterOnly(Boolean masterOnly) {
		this.masterOnly = masterOnly;
	}

	public Boolean getSupportsAllZoneTypes() {
		return supportsAllZoneTypes;
	}

	public void setSupportsAllZoneTypes(Boolean supportsAllZoneTypes) {
		this.supportsAllZoneTypes = supportsAllZoneTypes;
	}

	public Boolean getOwnerOnly() {
		return ownerOnly;
	}

	public void setOwnerOnly(Boolean ownerOnly) {
		this.ownerOnly = ownerOnly;
	}
}
