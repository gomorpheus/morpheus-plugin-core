package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class NetworkRouterIdentityProjection extends MorpheusModel {

	protected String externalId;
	protected String refType;
	protected Long refId;

	public NetworkRouterIdentityProjection() {}

	public NetworkRouterIdentityProjection(Long id, String externalId, String refType, Long refId) {
		this.id = id;
		this.externalId = externalId;
		this.refType = refType;
		this.refId = refId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}
}
