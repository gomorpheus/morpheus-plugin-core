package com.morpheusdata.model.dto;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkPool} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author David estes
 */
public class NetworkPoolSyncMatchDto {
	private String externalId;
	private String name;

	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the network pool. In this class this should not be called directly
	 * @param externalId
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Returns the name of the network pool.
	 * @return the name of the network pool.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the network pool. In this class this should not be called directly
	 * @param name the name to set on the object
	 */
	public void setName(String name) {
		this.name = name;
	}
}
