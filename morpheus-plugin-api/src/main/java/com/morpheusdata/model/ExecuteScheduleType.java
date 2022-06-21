package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.BackupProvider;
import java.util.Date;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class ExecuteScheduleType extends MorpheusModel {

	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected Account owner;
	protected String name;
	protected String code;
	protected String description;
	protected String scheduleType = "execute";
	protected String scheduleTimezone;
	protected String cron = "0 0 * * *";
	protected Boolean enabled = true;
	protected String visibility="private";
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String createdBy = "system";
	protected String updatedBy = "system";

	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		markDirty("owner", owner, this.owner);
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		markDirty("name", name, this.name);
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		markDirty("code", code, this.code);
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		markDirty("description", description, this.description);
		this.description = description;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		markDirty("scheduleType", scheduleType, this.scheduleType);
		this.scheduleType = scheduleType;
	}

	public String getScheduleTimezone() {
		return scheduleTimezone;
	}

	public void setScheduleTimezone(String scheduleTimezone) {
		markDirty("scheduleTimezone", scheduleTimezone, this.scheduleTimezone);
		this.scheduleTimezone = scheduleTimezone;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		markDirty("cron", cron, this.cron);
		this.cron = cron;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		markDirty("enabled", enabled, this.enabled);
		this.enabled = enabled;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		markDirty("visibility", visibility, this.visibility);
		this.visibility = visibility;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		markDirty("createdBy", createdBy, this.createdBy);
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		markDirty("updatedBy", updatedBy, this.updatedBy);
		this.updatedBy = updatedBy;
	}

}
