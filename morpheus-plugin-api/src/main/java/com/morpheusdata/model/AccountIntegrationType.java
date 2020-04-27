package com.morpheusdata.model;

import java.util.List;

public class AccountIntegrationType extends MorpheusModel {
	public String name;
	public String code;
	public String description;
	public String category;
	public String integrationService;
	public Boolean enabled = true;
	public String viewSet;
	public Boolean hasCMDB = false;
	public Boolean hasCM = false;
	public Boolean hasDNS = false;
	public Boolean hasApprovals = false;

	public List<OptionType> optionTypes;
}
