package com.morpheusdata.model.projection;

import com.morpheusdata.model.projection.MorpheusIdentityModel;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ServicePlanPriceSet} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Dustin DeYoung
 * @since 0.14.3
 */
public class ServicePlanPriceSetIdentityProjection extends MorpheusIdentityModel {

	protected AccountPriceSetIdentityProjection priceSet;
	protected ServicePlanIdentityProjection servicePlan;

	public AccountPriceSetIdentityProjection getPriceSet() {
		return priceSet;
	}

	public void setPriceSet(AccountPriceSetIdentityProjection priceSet) {
		this.priceSet = priceSet;
		markDirty("priceSet", priceSet, this.priceSet);
	}

	public ServicePlanIdentityProjection getServicePlan() {
		return servicePlan;
	}

	public void setServicePlan(ServicePlanIdentityProjection servicePlan) {
		this.servicePlan = servicePlan;
		markDirty("servicePlan", servicePlan, this.servicePlan);
	}
}
