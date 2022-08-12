package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Date;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class BackupRestore extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account account;
	protected String backupResultId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Date startDate;
	protected Date endDate;
	protected Long duration;
	protected String status;
	protected String externalId;
	protected String externalStatusRef;
	protected String config;
	protected String errorMessage;
	protected Long backupId;
	protected Long containerId;

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

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		markDirty("config", config, this.config);
		this.config = config;
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
