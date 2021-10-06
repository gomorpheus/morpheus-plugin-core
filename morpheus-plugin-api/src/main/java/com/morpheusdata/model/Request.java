package com.morpheusdata.model;

import java.util.List;
import java.math.BigDecimal;

public class Request extends MorpheusModel {

	protected ApprovalRequestType requestType;
	protected String externalId;
	protected String externalName;
	protected List<RequestReference> refs;
	protected BigDecimal pricePerMonth;
	protected String currency;

	public com.morpheusdata.model.Request.ApprovalRequestType getRequestType() {
		return requestType;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getExternalName() {
		return externalName;
	}

	public BigDecimal getPricePerMonth() {
		return pricePerMonth;
	}

	public String getCurrency() {
		return currency;
	}

	public List<RequestReference> getRefs() {
		return refs;
	}

	public enum ApprovalRequestType {
		EXTENSION_APPROVAL_TYPE,
		INSTANCE_APPROVAL_TYPE,
		APP_APPROVAL_TYPE,
		SHUTDOWN_EXTENSION_APPROVAL_TYPE,
		INSTANCE_DELETE_APPROVAL_TYPE,
		APP_DELETE_APPROVAL_TYPE,
		INSTANCE_RECONFIGURE_APPROVAL_TYPE,
		SERVER_RECONFIGURE_APPROVAL_TYPE,
	}

	public void setRequestType(com.morpheusdata.model.Request.ApprovalRequestType requestType) {
		this.requestType = requestType;
		markDirty("requestType", requestType);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setExternalName(String externalName) {
		this.externalName = externalName;
		markDirty("externalName", externalName);
	}

	public void setRefs(List<RequestReference> refs) {
		this.refs = refs;
		markDirty("refs", refs);
	}

	public void setPricePerMonth(BigDecimal pricePerMonth) {
		this.pricePerMonth = pricePerMonth;
		markDirty("pricePerMonth", pricePerMonth);
	}

	public void setCurrency(String currency) {
		this.currency = currency;
		markDirty("currency", currency);
	}

}
