package com.morpheusdata.model;

import com.morpheusdata.model.projection.AccountPriceSetIdentityProjection;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class AccountPriceSet extends MorpheusModel {

	enum PRICE_SET_TYPE {
		fixed,
		compute_plus_storage,
		component,
		load_balancer,
		virtual_image,
		snapshot,
		software_or_service
	}

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String code;
	protected String name;

	protected Boolean active = true;
	protected String priceUnit = "month";
	protected String type = "component";
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean systemCreated = false;
	protected String createdBy = "system";
	protected String updatedBy = "system";
	protected String regionCode;
	protected Long zoneId;
	protected Long zonePoolId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public void setActive(Boolean active) {
		markDirty("active", active, this.active);
		this.active = active;
	}

	public void setPriceUnit(String priceUnit) {
		markDirty("priceUnit", priceUnit, this.priceUnit);
		this.priceUnit = priceUnit;
	}

	public void setType(String type) {
		markDirty("type", type, this.type);
		this.type = type;
	}

	public void setDateCreated(Date dateCreated) {
		markDirty("dateCreated", dateCreated, this.dateCreated);
		this.dateCreated = dateCreated;
	}

	public void setLastUpdated(Date lastUpdated) {
		markDirty("lastUpdated", lastUpdated, this.lastUpdated);
		this.lastUpdated = lastUpdated;
	}

	public void setSystemCreated(Boolean systemCreated) {
		markDirty("systemCreated", systemCreated, this.systemCreated);
		this.systemCreated = systemCreated;
	}

	public void setCreatedBy(String createdBy) {
		markDirty("createdBy", createdBy, this.createdBy);
		this.createdBy = createdBy;
	}

	public void setUpdatedBy(String updatedBy) {
		markDirty("updatedBy", updatedBy, this.updatedBy);
		this.updatedBy = updatedBy;
	}

	public void setRegionCode(String regionCode) {
		markDirty("regionCode", regionCode, this.regionCode);
		this.regionCode = regionCode;
	}

	public void setZoneId(Long zoneId) {
		markDirty("zoneId", zoneId, this.zoneId);
		this.zoneId = zoneId;
	}

	public void setZonePoolId(Long zonePoolId) {
		markDirty("zonePoolId", zonePoolId, this.zonePoolId);
		this.zonePoolId = zonePoolId;
	}

}
