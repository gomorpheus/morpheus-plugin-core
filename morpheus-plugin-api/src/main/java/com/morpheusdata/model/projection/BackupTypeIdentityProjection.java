package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.BackupType;

public class BackupTypeIdentityProjection extends MorpheusModel {
	protected String name;
	protected String code;

	public BackupTypeIdentityProjection() {}

	public BackupTypeIdentityProjection(Long id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	/**
	 * Gets the name of the backup. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the backup
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Backup. Typically this isnt called directly.
	 * @param name the name of the backup to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * The {@link BackupType#getCode()} is mapped here in the identity projection for quick filter search.
	 * @return the associated {@link BackupType#getCode()}
	 */
	public String getCode() {
		return code;
	}

}
