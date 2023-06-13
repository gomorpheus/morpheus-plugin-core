package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.ComputeZonePoolIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class ComputeZonePool extends ComputeZonePoolIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account owner;
	public String visibility = "private";
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Cloud cloud;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
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
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public ComputeZonePool parent;
	public Boolean active = true;
	public Boolean readOnly = false;
	public Boolean defaultPool = false;
	public Boolean hidden = false;
	public String iacId; //id for infrastructure as code integrations
	public Status status = Status.available;
	public Boolean editable;
	public Boolean removable;

	enum Status {
		deploying,
		failed,
		available
	}
}
