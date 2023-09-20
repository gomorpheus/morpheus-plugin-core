package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class BackupRestore extends MorpheusModel {

	/**
	 * Account scope
	 */
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;

	/**
	 * ID of the backup associated to the backup result
	 */
	protected Long backupId;

	/**
	 * ID of the Backup result used for restore
	 */
	protected String backupResultId;

	/**
	 * Date of creation
	 */
	protected Date dateCreated;

	/**
	 * Date of last update
	 */
	protected Date lastUpdated;

	/**
	 * Date restore was started
	 */
	protected Date startDate;

	/**
	 * Date restore ended
	 */
	protected Date endDate;

	/**
	 * Duration of the restore process in milliseconds
	 */
	protected Long duration;

	/**
	 * Current status of the restore {@link com.morpheusdata.core.backup.util.BackupStatusUtility}
	 */
	protected String status;

	/**
	 * External ID of the restore process. Usually a task ID on the external system.
	 */
	protected String externalId;

	/**
	 * Reference to the external status. Useful in systems where the restore process does not contain details
	 * about the restored workload.
	 */
	protected String externalStatusRef;

	/**
	 * Error message of a failed restore
	 */
	protected String errorMessage;

	/**
	 * Restore target container (workload) ID
	 */
	protected Long containerId;

	/**
	 * Restore to a new workload
	 */
	protected  Boolean restoreToNew;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		markDirty("account", account, this.account);
		this.account = account;
	}

	public String getBackupResultId() {
		return backupResultId;
	}

	public void setBackupResultId(String backupResultId) {
		markDirty("backupResultId", backupResultId, this.backupResultId);
		this.backupResultId = backupResultId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		markDirty("dateCreated", dateCreated, this.dateCreated);
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		markDirty("lastUpdated", lastUpdated, this.lastUpdated);
		this.lastUpdated = lastUpdated;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		markDirty("startDate", startDate, this.startDate);
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		markDirty("endDate", endDate, this.endDate);
		this.endDate = endDate;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		markDirty("duration", duration, this.duration);
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		markDirty("status", status, this.status);
		this.status = status;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		markDirty("externalId", externalId, this.externalId);
		this.externalId = externalId;
	}

	public String getExternalStatusRef() {
		return externalStatusRef;
	}

	public void setExternalStatusRef(String externalStatusRef) {
		markDirty("externalStatusRef", externalStatusRef, this.externalStatusRef);
		this.externalStatusRef = externalStatusRef;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		markDirty("errorMessage", errorMessage, this.errorMessage);
		this.errorMessage = errorMessage;
	}

	public Long getBackupId() {
		return backupId;
	}

	public void setBackupId(Long backupId) {
		markDirty("backupId", backupId, this.backupId);
		this.backupId = backupId;
	}

	public Long getContainerId() {
		return containerId;
	}

	public void setContainerId(Long containerId) {
		markDirty("containerId", containerId, this.containerId);
		this.containerId = containerId;
	}

	public Boolean getRestoreToNew() {
		return restoreToNew;
	}

	public void setRestoreToNew(Boolean restoreToNew) {
		this.restoreToNew = restoreToNew;
		markDirty("restoreToNew", restoreToNew, this.restoreToNew);
	}
}
