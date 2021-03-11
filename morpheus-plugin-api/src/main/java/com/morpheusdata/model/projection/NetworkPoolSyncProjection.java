package com.morpheusdata.model.projection;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkPool} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see com.morpheusdata.core.MorpheusNetworkContext
 * @author David Estes
 */
public class NetworkPoolSyncProjection {
	private Long id;
	private String externalId;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 */
	public NetworkPoolSyncProjection(Long id, String externalId) {
		this.id = id;
		this.externalId = externalId;
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
	}

	/**
	 * Gets the Identifier in the Morpheus Database of the current {@link com.morpheusdata.model.NetworkPool}
	 * @return the Long id of the current record
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the {@link com.morpheusdata.model.NetworkPool}. In this class this should not be called directly
	 * @param id the id of the morpheus record
	 */
	public void setId(Long id) {
		this.id = id;
	}
}
