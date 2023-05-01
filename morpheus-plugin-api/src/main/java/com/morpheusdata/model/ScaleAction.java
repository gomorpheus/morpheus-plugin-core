package com.morpheusdata.model;

public class ScaleAction extends MorpheusModel {

	String code;
	String scaleType;
	InstanceTypeLayout layout;
	InstanceAction upAction;
	WorkloadAction downAction;
	Boolean active;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getScaleType() {
		return scaleType;
	}

	public void setScaleType(String scaleType) {
		this.scaleType = scaleType;
		markDirty("scaleType", scaleType);
	}

	public InstanceTypeLayout getLayout() {
		return layout;
	}

	public void setLayout(InstanceTypeLayout layout) {
		this.layout = layout;
		markDirty("layout", layout);
	}

	public InstanceAction getUpAction() {
		return upAction;
	}

	public void setUpAction(InstanceAction upAction) {
		this.upAction = upAction;
		markDirty("upAction", upAction);
	}

	public WorkloadAction getDownAction() {
		return downAction;
	}

	public void setDownAction(WorkloadAction downAction) {
		this.downAction = downAction;
		markDirty("downAction", downAction);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}
}
