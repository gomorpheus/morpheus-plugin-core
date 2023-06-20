package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class NetworkRouteTableIdentityProjection extends MorpheusModel {

	protected String externalId;

	public NetworkRouteTableIdentityProjection() {}

	public NetworkRouteTableIdentityProjection(Long id, String externalId) {
		this.id = id;
		this.externalId = externalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
