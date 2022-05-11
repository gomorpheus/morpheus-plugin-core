package com.morpheusdata.request;

public class DeleteInstanceRequest {

	/**
	 * Indicates if the delete should be forced and bypass other checks
	 */
	public Boolean force = false;

	/**
	 * Indicates if backups should be kept
	 */
	public Boolean preserveBackups = false;

	/**
	 * Indicates if Amazon Elastic IPs should be released
	 */
	public Boolean releaseEIPs = true;

	/**
	 * Indicates if StorageVolumes should be preserved
	 */
	public Boolean preserveVolumes = false;

	public DeleteInstanceRequest(){
	}

	public DeleteInstanceRequest(Boolean force, Boolean preserveBackups, Boolean releaseEIPs, Boolean preserveVolumes) {
		this.force = force;
		this.preserveBackups = preserveBackups;
		this.releaseEIPs = releaseEIPs;
		this.preserveVolumes = preserveVolumes;
	}
}
