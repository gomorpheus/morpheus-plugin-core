package com.morpheusdata.model;

import java.util.List;

public class DashboardItemType extends MorpheusModel {
	
	protected String uuid;
	protected String name;
	protected String code;
	protected String category;
	protected String title;
	protected String description;
	protected Boolean enabled = true;
	protected Boolean canExecute = true;
	//view info
	protected String uiSize; //xs-1,s-1,m-1,l-1,xl-1
	protected String uiType; //widget class etc.
	protected String scriptPath;
	protected String templatePath;
	//options
	protected List<OptionType> optionTypes;

	//getters and setters
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getCanExecute() {
		return canExecute;
	}

	public void setCanExecute(Boolean canExecute) {
		this.canExecute = canExecute;
	}

	public String getUiSize() {
		return uiSize;
	}

	public void setUiSize(String uiSize) {
		this.uiSize = uiSize;
	}

	public String getUiType() {
		return uiType;
	}

	public void setUiType(String uiType) {
		this.uiType = uiType;
	}

	public String getScriptPath() {
		return scriptPath;
	}

	public void setScriptPath(String scriptPath) {
		markDirty("scriptPath", scriptPath);
		this.scriptPath = scriptPath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		markDirty("templatePath", templatePath);
		this.templatePath = templatePath;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
	}
	
}