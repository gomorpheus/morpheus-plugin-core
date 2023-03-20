package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class NetworkLoadBalancerIdentityProjection extends MorpheusModel {
	protected String externalId;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}
}
