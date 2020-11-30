package com.morpheusdata.model;

import java.math.BigDecimal;

public class ServicePlan extends MorpheusModel {
	public String code;
	public String name;
	public String description;
	public String provisionTypeCode;
	public Boolean editable;
	public String externalId;
	public Long maxCores;
	public Long maxMemory;
	public Long maxStorage;
	public Integer sortOrder;
	public BigDecimal price_monthly;
	public BigDecimal price_hourly;
}
