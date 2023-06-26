package com.morpheusdata.model.projection;

public class CloudTypeIdentityProjection extends MorpheusIdentityModel {
	public CloudTypeIdentityProjection(){}

	protected String name;
	protected String code;

	public CloudTypeIdentityProjection(Long id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	/**
	 * Cloud name
	 * @return String the name of the Cloud
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Unique code
	 * @return String the unique code
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}
}
