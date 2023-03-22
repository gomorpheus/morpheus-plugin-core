package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.MorpheusNetworkService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkDomainRecord} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusNetworkService
 * @author David Estes
 */
public class NetworkDomainRecordIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 */
	public NetworkDomainRecordIdentityProjection(Long id, String externalId) {
		this.id = id;
		this.externalId = externalId;
	}

	public NetworkDomainRecordIdentityProjection() {
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
