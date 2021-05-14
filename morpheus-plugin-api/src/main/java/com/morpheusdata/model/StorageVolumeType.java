package com.morpheusdata.model;

/**
 * There are several different types of volume types across various cloud providers.
 *
 * @author Bob Whiton
 * @since 0.9.0
 */
public class StorageVolumeType extends MorpheusModel {
	private String name;
	private String code;
	private String externalId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}
}
