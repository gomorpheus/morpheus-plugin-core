package com.morpheusdata.model.projection;

import com.morpheusdata.core.cloud.MorpheusCloudService;
import java.util.Map;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.Instance} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusCloudService
 * @author Mike Truso
 * @since 0.8.0
 */
public class InstanceIdentityProjection extends MorpheusIdentityModel {
	protected String name;
	protected String externalId;
	public String instanceTypeCode;
	protected String internalId;
	public String status;
	public String planCode;
	public String layoutCode;

	public String getLayoutCode() {
		return layoutCode;
	}

	public void setLayoutCode(String layoutCode) {
		this.layoutCode = layoutCode;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public String getInstanceTypeCode() {
		return instanceTypeCode;
	}

	public void setInstanceTypeCode(String instanceTypeCode) {
		this.instanceTypeCode = instanceTypeCode;
	}
}
