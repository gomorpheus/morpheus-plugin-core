package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

public class BackupJobIdentityProjection extends MorpheusModel {
	protected String externalId;
	protected String name;

	public BackupJobIdentityProjection() {};

	public BackupJobIdentityProjection(Long id, String externalId, String name) {
		this.id = id;
		this.name = name;
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
	 * Sets the externalId of the backup job. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the backup job. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the backup job
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Backup JOb. Typically this isnt called directly.
	 * @param name the name of the backup job to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
