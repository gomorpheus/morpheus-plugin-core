package com.morpheusdata.model;

import java.util.Collection;

public class ComputeTypePackage extends MorpheusModel {

	protected String code;
	protected String name;
	protected String packageVersion;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getPackageVersion() {
		return packageVersion;
	}

	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
		markDirty("packageVersion", packageVersion);
	}
}
