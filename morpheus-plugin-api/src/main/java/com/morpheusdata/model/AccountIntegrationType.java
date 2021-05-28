package com.morpheusdata.model;

import java.util.List;

public class AccountIntegrationType extends MorpheusModel {

	protected String name;
	protected String code;
	protected String description;
	protected String category;
	protected String integrationService;
	protected Boolean enabled = true;
	protected String viewSet;
	protected Boolean hasCMDB = false;
	protected Boolean hasCM = false;
	protected Boolean hasDNS = false;
	protected Boolean hasApprovals = false;

	protected List<OptionType> optionTypes;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	public String getIntegrationService() {
		return integrationService;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public String getViewSet() {
		return viewSet;
	}

	public Boolean getHasCMDB() {
		return hasCMDB;
	}

	public Boolean getHasCM() {
		return hasCM;
	}

	public Boolean getHasDNS() {
		return hasDNS;
	}

	public Boolean getHasApprovals() {
		return hasApprovals;
	}

	public List<OptionType> getOptionTypes() {
		return optionTypes;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public void setIntegrationService(String integrationService) {
		this.integrationService = integrationService;
		markDirty("integrationService", integrationService);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setViewSet(String viewSet) {
		this.viewSet = viewSet;
		markDirty("viewSet", viewSet);
	}

	public void setHasCMDB(Boolean hasCMDB) {
		this.hasCMDB = hasCMDB;
		markDirty("hasCMDB", hasCMDB);
	}

	public void setHasCM(Boolean hasCM) {
		this.hasCM = hasCM;
		markDirty("hasCM", hasCM);
	}

	public void setHasDNS(Boolean hasDNS) {
		this.hasDNS = hasDNS;
		markDirty("hasDNS", hasDNS);
	}

	public void setHasApprovals(Boolean hasApprovals) {
		this.hasApprovals = hasApprovals;
		markDirty("hasApprovals", hasApprovals);
	}

	public void setOptionTypes(List<OptionType> optionTypes) {
		this.optionTypes = optionTypes;
		markDirty("optionTypes", optionTypes);
	}
}
