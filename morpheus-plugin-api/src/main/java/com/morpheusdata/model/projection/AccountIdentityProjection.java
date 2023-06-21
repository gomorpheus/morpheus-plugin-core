package com.morpheusdata.model.projection;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Account} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see AccountIdentityProjection
 * @author Chris Taylor
 * @since 0.15.1
 */
public class AccountIdentityProjection extends MorpheusIdentityModel {
	protected String apiKey;
	protected String externalId;
	protected String name;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
		markDirty("apiKey", apiKey);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
