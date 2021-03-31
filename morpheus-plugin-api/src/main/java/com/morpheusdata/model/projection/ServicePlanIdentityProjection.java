package com.morpheusdata.model.projection;

import com.morpheusdata.model.MorpheusModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ServicePlan} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see com.morpheusdata.core.MorpheusServicePlanContext
 * @author Mike Truso
 * @since 0.8.0
 */
public class ServicePlanIdentityProjection extends MorpheusModel {
	public String code;
	public String name;
	public String externalId;
}
