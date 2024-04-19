package com.morpheusdata.model.projection;

public class VirtualImageTypeIdentityProjection extends MorpheusIdentityModel {

	protected String uuid;
	protected String code;
	protected String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code, this.code);
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid, this.uuid);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name, this.name);
	}
}
