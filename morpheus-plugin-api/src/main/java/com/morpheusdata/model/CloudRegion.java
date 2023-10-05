package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.projection.CloudIdentityProjection;
import com.morpheusdata.model.projection.CloudRegionIdentity;
import com.morpheusdata.model.projection.ComputeZoneRegionIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * A model representing a region in a cloud. This can be useful for syncing a list of dynamic region sets and also for
 * iterating sync across multiple regions
 *
 * @author David Estes
 * @since 0.14.0
 * @see CloudRegionIdentity
 */
public class CloudRegion extends ComputeZoneRegionIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Account account;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected CloudIdentityProjection cloud;
	protected String code;
	protected String name;
	protected String regionCode;
	protected String cloudCode;
	protected String internalId;

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public CloudIdentityProjection getCloud() {
		return cloud;
	}

	public void setCloud(CloudIdentityProjection cloud) {
		this.cloud = cloud;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}
}
