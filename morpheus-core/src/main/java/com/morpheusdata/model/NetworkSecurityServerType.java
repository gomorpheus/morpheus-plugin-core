package com.morpheusdata.model;

public class NetworkSecurityServerType extends MorpheusModel {
	public String code;
	public String name;
	public String description;
	public Boolean creatable = true;
	public Boolean selectable = true;
	public String securityService;
	public Boolean enabled = true;
	public String integrationCode; //matching integration type
	public String serverCode; //matching network server type
	public String viewSet;
	public Boolean manageSecurityGroups = false;
	public Boolean canEdit = false;
	public Boolean canDelete = false;
	//config
	public Boolean hasSecurityGroups = false;
	public String titleSecurityGroups;
	public Boolean hasSecurityProfiles = false;
	public String titleSecurityProfiles;
	public Boolean hasSecurityRoles = false;
	public String titleSecurityRoles;
	public Boolean hasSecurityZones = false;
	public String titleSecurityZones;

//	static hasMany = [optionTypes:OptionType, profileOptionTypes:OptionType, roleOptionTypes:OptionType, zoneOptionTypes:OptionType]
}
