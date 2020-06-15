package com.morpheusdata.model;

public class RequestReference {

	/**
	 * The Instance or App id
	 */
	private String refId;
	private String externalId;
	private String externalName;
	private ApprovalStatus status;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
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
