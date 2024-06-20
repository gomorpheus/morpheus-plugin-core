package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents a Morpheus Service Catalog Item Type. This is mainly used in the plugin context for rendering custom layouts
 * Catalog items have custom option type inputs for determining how the item should be created. They can be associated
 * to Instances, Workflows, or Blueprints.
 * @since 0.9.0
 * @author David Estes
 */
public class CatalogItemType extends MorpheusModel {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String visibility;
	protected String name;
	protected String description;
	protected String refType;
	protected String refId;
	protected Boolean enabled;
	protected Boolean active;
	protected Boolean featured;
	protected String iconPath;
	protected List<OptionType> optionTypes;
	protected OptionTypeForm form;
	protected String createdBy;
	protected Date dateCreated;
	protected Date lastUpdated;

	protected String content;
	protected String contentFormatted;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getFeatured() {
		return featured;
	}

	public void setFeatured(Boolean featured) {
		this.featured = featured;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentFormatted() {
		return contentFormatted;
	}

	public void setContentFormatted(String contentFormatted) {
		this.contentFormatted = contentFormatted;
	}

	public OptionTypeForm getForm() {
		return form;
	}

	public void setForm(OptionTypeForm form) {
		this.form = form;
	}
}
