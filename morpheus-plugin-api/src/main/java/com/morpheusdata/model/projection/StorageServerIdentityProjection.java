package com.morpheusdata.model.projection;

import com.morpheusdata.model.projection.MorpheusIdentityModel;
import com.morpheusdata.model.StorageServer;

/**
 * Provides a subset of properties from the {@link StorageServer} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author bdwheeler
 */
public class StorageServerIdentityProjection extends MorpheusIdentityModel {

	protected String name;
	protected String externalId;
	protected String uuid;

	public StorageServerIdentityProjection() {
		//default
	}

	public StorageServerIdentityProjection(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * Gets the name of the storage server. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the storage server
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the storage server. Typically this isnt called directly.
	 * @param name the name of the storage server to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the storage server. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * returns the uuid of the storage server.
	 * @return the uuid of the current record
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets the uuid of the storage server. In this class this should not be called directly
	 * @param uuid the uuid of the current record
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}

}
