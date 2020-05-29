package com.morpheusdata.response;

import com.morpheusdata.model.RequestReference;

import java.util.List;

public class RequestResponse {
	private Boolean success;
	private String msg;
	private String externalRequestId;
	private String externalRequestName;
	private List<RequestReference> references;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getExternalRequestId() {
		return externalRequestId;
	}

	public void setExternalRequestId(String externalRequestId) {
		this.externalRequestId = externalRequestId;
	}

	public String getExternalRequestName() {
		return externalRequestName;
	}

	public void setExternalRequestName(String externalRequestName) {
		this.externalRequestName = externalRequestName;
	}

	public List<RequestReference> getReferences() {
		return references;
	}

	public void setReferences(List<RequestReference> references) {
		this.references = references;
	}
}
