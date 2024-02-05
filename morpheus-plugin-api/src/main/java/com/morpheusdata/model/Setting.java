package com.morpheusdata.model;

public class Setting extends MorpheusModel {
	protected SettingType settingType;
	protected String referenceType;
	protected Long referenceId;
	protected String value;
	protected String filterType = "AppSystem";

	public SettingType getSettingType() {
		return settingType;
	}

	public void setSettingType(SettingType settingType) {
		this.settingType = settingType;
		markDirty("settingType", settingType);
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
		markDirty("referenceType", referenceType);
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
		markDirty("referenceId", referenceId);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		markDirty("value", value);
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
		markDirty("filterType", filterType);
	}
}
