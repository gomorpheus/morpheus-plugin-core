package com.morpheusdata.model.projection;

import com.morpheusdata.core.MorpheusServicePlanService;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ServicePlan} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusServicePlanService
 * @author Mike Truso
 * @since 0.8.0
 */
public class PricePlanIdentityProjection extends MorpheusIdentityModel {
	protected String code;
	protected String name;
	protected Boolean deleted = false;
	protected String category;

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
		markDirty("deleted", deleted);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		markDirty("category", category);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
