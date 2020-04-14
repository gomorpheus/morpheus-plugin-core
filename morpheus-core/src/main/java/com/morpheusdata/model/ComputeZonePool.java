package com.morpheusdata.model;

public class ComputeZonePool extends MorpheusModel {
	public Account owner;
	public String visibility = "private";
//	public ComputeZone zone;
//	public ComputeServerGroup serverGroup;
	public String code;
	public String config;
	public String type = "default";
	public String name;
	public String description;
	public String category;
	public String regionCode;
	public String internalId;
	public String externalId;
	public String uniqueId;
	public String refType;
	public Long refId;
	public ComputeZonePool parent;
	public Boolean active = true;
	public Boolean readOnly = false;
	public Boolean defaultPool = false;
	public Boolean hidden = false;
	public String iacId; //id for infrastructure as code integrations
	public Status status = Status.available;
	public Boolean editable;
	public Boolean removable;
}
