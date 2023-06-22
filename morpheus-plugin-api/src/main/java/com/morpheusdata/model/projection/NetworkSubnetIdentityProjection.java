package com.morpheusdata.model.projection;

import com.morpheusdata.core.network.MorpheusNetworkSubnetService;
import com.morpheusdata.model.NetworkBase;
import com.morpheusdata.model.projection.MorpheusIdentityModel;
import com.morpheusdata.model.NetworkSubnetType;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkSubnet} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @since 0.11.0
 * @see MorpheusNetworkSubnetService
 * @author Bob Whiton
 */
public class NetworkSubnetIdentityProjection extends NetworkBase {
	protected String externalId;
	protected String name;
	protected String typeCode;

	public NetworkSubnetIdentityProjection(Long id, String externalId, String name, String typeCode) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
		this.typeCode = typeCode;
	}

	public NetworkSubnetIdentityProjection() {
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
	 * Sets the externalId of the network. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the NetworkSubnet. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the network subnet
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the NetworkSubnet. Typically this isnt called directly.
	 * @param name the name of the network subnet to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * The {@link NetworkSubnetType#getCode()} is mapped here in the identity projection for quick filter search.
	 * @return the associated {@link NetworkSubnetType#getCode()}
	 */
	public String getTypeCode() {
		return typeCode;
	}

}
