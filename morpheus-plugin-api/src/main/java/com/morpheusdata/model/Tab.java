package com.morpheusdata.model;

public class Tab extends MorpheusModel {

	protected String name;
	protected String code;
	protected String icon;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getIcon() {
		return icon;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setIcon(String icon) {
		this.icon = icon;
		markDirty("icon", icon);
	}

}
