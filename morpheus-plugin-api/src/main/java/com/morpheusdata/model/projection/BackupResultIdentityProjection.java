package com.morpheusdata.model.projection;

public class BackupResultIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String backupName;

	public BackupResultIdentityProjection() {
	}

	public BackupResultIdentityProjection(Long id, String externalId, String backupName) {
		this.id = id;
		this.backupName = backupName;
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
	 * Sets the externalId of the backup. In this class this should not be called directly
	 * @param externalId the external id or API id of the current record
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the backup. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the backup
	 */
	public String getBackupName() {
		return backupName;
	}

	/**
	 * Sets the name of the Backup. Typically this isnt called directly.
	 * @param backupName the name of the backup to be assigned.
	 */
	public void setBackupName(String backupName) {
		this.backupName = backupName;
		markDirty("backupName", backupName);
	}
}
