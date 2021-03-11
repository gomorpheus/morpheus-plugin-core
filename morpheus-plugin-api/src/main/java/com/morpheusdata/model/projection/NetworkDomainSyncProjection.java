package com.morpheusdata.model.projection;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.NetworkDomain} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see com.morpheusdata.core.MorpheusNetworkContext
 * @author David Estes
 */
public class NetworkDomainSyncProjection {
	private Long id;
	private String externalId;
	private String name;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 * @param name the Name of the object as a secondary comparison
	 */
	public NetworkDomainSyncProjection(Long id, String externalId, String name) {
		this.id = id;
		this.externalId = externalId;
		this.name = name;
	}
	/**
	 * Returns the current externalId on this Projection
	 * @return the externalId normally matches the api id
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the network domain. In this class this should not be called directly
	 * @param externalId
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * Returns the name of the network domain.
	 * @return the name of the network domain.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the network domain. In this class this should not be called directly
	 * @param name the name to set on the object
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
