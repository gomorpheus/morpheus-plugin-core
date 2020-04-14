package com.morpheusdata.model;

public class NetworkPoolServerType extends MorpheusModel {
	public String code;
	public String name;
	public String description;
	public Boolean selectable = true;
	public String poolService;
	public String integrationCode; //matching integration type
	public Boolean enabled = true;
	public Boolean isPlugin;
}
