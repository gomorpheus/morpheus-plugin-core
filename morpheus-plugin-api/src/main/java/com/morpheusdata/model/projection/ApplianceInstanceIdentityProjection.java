package com.morpheusdata.model.projection;

import com.morpheusdata.model.Account;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ApplianceInstance}
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Dan DeVilbiss
 * @since 0.15.10
 */
public class ApplianceInstanceIdentityProjection extends MorpheusIdentityModel {
	protected String applianceId;
	protected String applianceName;

	public String getApplianceId() {
		return applianceId;
	}

	public void setApplianceId(String applianceId) {
		this.applianceId = applianceId;
		markDirty("applianceId", applianceId, this.applianceId);
	}

	public String getApplianceName() {
		return applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
		markDirty("applianceName", applianceName, this.applianceName);
	}
}
