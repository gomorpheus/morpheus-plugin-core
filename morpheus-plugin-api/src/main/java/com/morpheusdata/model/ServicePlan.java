package com.morpheusdata.model;

import com.morpheusdata.model.projection.ServicePlanIdentityProjection;

import java.math.BigDecimal;

/**
 * Provides a means to set predefined tiers on memory, storage, cores, and cpu.
 */
public class ServicePlan extends ServicePlanIdentityProjection {
	public String description;
	public String provisionTypeCode;
	public Boolean editable;
	public Long maxCores;
	public Long maxMemory;
	public Long maxStorage;
	public Integer sortOrder;
	public BigDecimal price_monthly;
	public BigDecimal price_hourly;
}
