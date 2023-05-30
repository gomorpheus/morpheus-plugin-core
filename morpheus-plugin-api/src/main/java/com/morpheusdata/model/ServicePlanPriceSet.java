package com.morpheusdata.model;

import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class ServicePlanPriceSet extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected ServicePlan servicePlan;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountPriceSet priceSet;
	protected Date startDate = new Date();
	protected Date endDate = new Date(1100, 0, 1) ; // The year 3000

	public ServicePlan getServicePlan() {
		return servicePlan;
	}

	public void setServicePlan(ServicePlan servicePlan) {
		this.servicePlan = servicePlan;
		markDirty("servicePlan", servicePlan, this.servicePlan);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		markDirty("startDate", startDate, this.startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		markDirty("endDate", endDate, this.endDate);
	}
}
