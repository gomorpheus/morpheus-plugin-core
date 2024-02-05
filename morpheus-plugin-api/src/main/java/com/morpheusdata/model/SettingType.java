package com.morpheusdata.model;

public class SettingType extends MorpheusModel {
	protected String scope = "Account";
	protected String filterType = "AppSystem";
	protected String name;
	protected String category;
	protected String inputType = "text";
	protected String description;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
		markDirty("scope", scope);
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
		markDirty("filterType", filterType);
	}

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

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
		markDirty("inputType", inputType);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}
}
