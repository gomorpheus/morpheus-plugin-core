package com.morpheusdata.model;

import java.util.List;

/**
 * Provides a Model representation of a MetadataTagType. These types are associated to MetadataTags.
 */
public class OptionTypeFieldGroup extends MorpheusModel {

	protected String name;
	protected String code;
	protected String description;
	protected String localizedName;
	protected Boolean collapsible;
	protected Boolean defaultCollapsed;
	protected String visibleOnCode;

	protected List<OptionType> options;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public String getLocalizedName() {
		return localizedName;
	}

	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
		markDirty("localizedName", localizedName);
	}

	public String getVisibleOnCode() {
		return visibleOnCode;
	}

	public void setVisibleOnCode(String visibleOnCode) {
		this.visibleOnCode = visibleOnCode;
		markDirty("visibleOnCode", visibleOnCode);
	}

	public Boolean getCollapsible() {
		return collapsible;
	}

	public void setCollapsible(Boolean collapsible) {
		this.collapsible = collapsible;
		markDirty("collapsible", collapsible);
	}

	public Boolean getDefaultCollapsed() {
		return defaultCollapsed;
	}

	public void setDefaultCollapsed(Boolean defaultCollapsed) {
		this.defaultCollapsed = defaultCollapsed;
		markDirty("defaultCollapsed", defaultCollapsed);
	}

	public List<OptionType> getOptions() {
		return options;
	}

	public void setOptions(List<OptionType> options) {
		this.options = options;
		markDirty("optionTypes", options);
	}
}
