package com.morpheusdata.model;

public class TierType extends MorpheusModel {
	protected String name;
	protected String code;
	protected Boolean enabled = true;
	protected Boolean reusable = true;

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getReusable() { return reusable; }

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
		markDirty("enabled", enabled);
	}

	public void setReusable(Boolean enabled) {
		this.reusable = reusable;
		markDirty("reusable", reusable);
	}

}
