package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

/**
 * This provides a way to query a list of regions and sync those regions for particular {@link com.morpheusdata.model.Cloud} objects.
 * Some clouds have dynamic region lists and this provides a mechanism to grab those regions and sync by them
 * @author David Estes
 * @since 0.15.3
 * @see com.morpheusdata.model.CloudRegion
 */
public class CloudRegionIdentity extends MorpheusModel {
	protected String externalId;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
