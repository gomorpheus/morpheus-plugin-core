package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.MorpheusNetworkContext;
import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkPool} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusNetworkContext
 * @author David Estes
 */
public class NetworkPoolIdentityProjection extends MorpheusModel {
	protected String externalId;
	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 */
	public NetworkPoolIdentityProjection(Long id, String externalId) {
		this.id = id;
		this.externalId = externalId;
	}

	public NetworkPoolIdentityProjection() {
		//
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the network pool. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId",externalId);
	}

}
