package com.morpheusdata.model;

public class AccountIntegrationObjectRef  extends MorpheusModel {

	protected Long refId;
	protected String refType;
	protected String category;
	protected String displayName;

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId, this.refId);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType, this.refType);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category, this.category);
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName", displayName, this.displayName);
	}
}
