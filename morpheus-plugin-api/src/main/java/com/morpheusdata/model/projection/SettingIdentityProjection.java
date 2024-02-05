package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusSettingService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Setting} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusSettingService
 * @author Dan DeVilbiss
 */
public class SettingIdentityProjection extends MorpheusIdentityModel {
	protected String referenceType;
	protected Long referenceId;
	protected String value;
	protected String filterType = "AppSystem";

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
		markDirty("referenceType", referenceType);
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
		markDirty("referenceId", referenceId);
	}

	public String getValue() {
		return referenceType;
	}

	public void setValue(String value) {
		this.value = value;
		markDirty("value", value);
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
		markDirty("filterType", filterType);
	}
}
