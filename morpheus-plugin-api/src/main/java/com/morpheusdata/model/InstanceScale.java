package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.InstanceScaleIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class InstanceScale extends InstanceScaleIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected InstanceScaleType type;
	protected InstanceThreshold threshold;
	protected Boolean enabled = true;
	protected String internalId;
	protected Long zoneId;
	protected String regionCode;
	protected String iacId; //id for infrastructure as code integrations
	protected Status status = Status.available;

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
		markDirty("owner", owner);
	}

	public InstanceScaleType getType() {
		return type;
	}

	public void setType(InstanceScaleType type) {
		this.type = type;
		markDirty("type", type);
	}

	public InstanceThreshold getThreshold() {
		return threshold;
	}

	public void setThreshold(InstanceThreshold threshold) {
		this.threshold = threshold;
		markDirty("threshold", threshold);
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		markDirty("zoneId", zoneId);
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
		markDirty("regionCode", regionCode);
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
		markDirty("iacId", iacId);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		markDirty("status", status);
	}

	enum Status {
		deploying,
		failed,
		available
	}
}
