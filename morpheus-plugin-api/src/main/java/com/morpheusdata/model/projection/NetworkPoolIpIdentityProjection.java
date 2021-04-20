package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.MorpheusNetworkPoolIpService;
import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkPoolIp} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusNetworkPoolIpService
 * @author David Estes
 */
public class NetworkPoolIpIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String ipAddress;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 * @param ipAddress the uniquely reserved ip address in the pool
	 */
	public NetworkPoolIpIdentityProjection(Long id, String externalId, String ipAddress) {
		this.id = id;
		this.externalId = externalId;
		this.ipAddress = ipAddress;
	}

	public NetworkPoolIpIdentityProjection() {
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
		this.markDirty("externalId",externalId);
	}

	/**
	 * Returns the ipAddress for the specific host record
	 * Typically this is the IPv4 address but in future could also be an IPv6 depending on pool type
	 * @return the IP Address of the current record
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the IPv4 or IPv6 address of the specific host record dependent upon the pool type.
	 * @param ipAddress the IP Address for the current record
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		this.markDirty("ipAddress",ipAddress);
	}
}
