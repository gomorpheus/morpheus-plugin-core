package com.morpheusdata.model;

public class RequestReference {
	private Instance instance;
	private String externalId;
	private String externalName;
	private ApprovalStatus status;

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getExternalName() {
		return externalName;
	}

	public void setExternalName(String externalName) {
		this.externalName = externalName;
	}

	public ApprovalStatus getStatus() {
		return status;
	}

	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}

	public enum ApprovalStatus {
		requesting,
		requested,
		error,
		approved,
		rejected,
		cancelled
	}
}
