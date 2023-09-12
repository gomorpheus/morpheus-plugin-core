package com.morpheusdata.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.NetworkRouteTableIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class NetworkRouteTable extends NetworkRouteTableIdentityProjection {
	protected String name;
	protected String code;
	protected String category;
	//linking
	protected String internalId;
	protected String iacId; //id for infrastructure as code integrations
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected CloudPool zonePool;

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

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
	}

	public CloudPool getZonePool() {
		return zonePool;
	}

	public void setZonePool(CloudPool zonePool) {
		this.zonePool = zonePool;
	}
}
