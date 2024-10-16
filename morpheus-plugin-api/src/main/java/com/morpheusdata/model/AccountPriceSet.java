/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

	public String getName() {
		return name;
	}

	public Account getAccount() { return account; }

	public Boolean getActive() { return active; }

	public String getPriceUnit() { return priceUnit; }

	public String getType() { return type; }

	public Date getDateCreated() { return dateCreated; }

	public Date getLastUpdated() { return lastUpdated; }

	public Boolean getSystemCreated() { return systemCreated; }

	public String getCreatedBy() { return createdBy; }

	public String getUpdatedBy() { return updatedBy; }

	public String getRegionCode() { return regionCode; }

	public Long getZoneId() { return zoneId; }

	public Long getZonePoolId() { return zonePoolId; }

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
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
