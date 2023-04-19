package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusOperationNotificationService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.OperationNotification} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusOperationNotificationService
 * @author Bob Whiton
 */
public class OperationNotificationIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String name;

	/**
	 * The default constructor for creating a projection object.
	 * @param id the database id of the object
	 * @param externalId the API id of the object
	 * @param name the Name of the object as a secondary comparison
	 * @param config the configuration of the object
	 */
	public OperationNotificationIdentityProjection(Long id, String externalId, String name, String config) {
		this.id = id;
		this.externalId = externalId;
		this.name = name;
		this.config = config;
	}

	public OperationNotificationIdentityProjection() {

	}

	/**
	 * Returns the current externalId on this Projection
	 * @return the externalId normally matches the api id
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the externalId of the operation notification. In this class this should not be called directly
	 * @param externalId the external API Id of the Zone
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId",externalId);
	}

	/**
	 * Returns the name of the operation notification.
	 * @return the name of the operation notification.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the operation notification. In this class this should not be called directly
	 * @param name the name to set on the object
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name",name);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
