package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class AccountCredentialIdentityProjection extends MorpheusModel {
	//external link
	protected String externalId;
	protected String internalId;
	protected String providerId;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
}
