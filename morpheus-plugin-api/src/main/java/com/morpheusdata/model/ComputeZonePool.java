package com.morpheusdata.model;

import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection;

public class ComputeZonePool extends ComputeZonePoolIdentityProjection {
	public Account owner;
	public String visibility = "private";
	public Cloud cloud;
	public ComputeServerGroup serverGroup;
	public String code;
	public String config;
	public String treeName;
	public String rawData;
	public String type = "default";
	public String description;
	public String regionCode;
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
	public Boolean inventory = true;

	enum Status {
		deploying,
		failed,
		available
	}
}
