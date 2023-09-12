package com.morpheusdata.model.projection;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.PricePlanPriceSet} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Dan DeVilbiss
 * @since 0.15.3
 */
public class PricePlanPriceSetIdentityProjection extends MorpheusIdentityModel {

	protected AccountPriceSetIdentityProjection priceSet;
	protected PricePlanIdentityProjection pricePlan;

	public AccountPriceSetIdentityProjection getPriceSet() {
		return priceSet;
	}

	public void setPriceSet(AccountPriceSetIdentityProjection priceSet) {
		this.priceSet = priceSet;
		markDirty("priceSet", priceSet, this.priceSet);
	}

	public PricePlanIdentityProjection getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(PricePlanIdentityProjection pricePlan) {
		this.pricePlan = pricePlan;
		markDirty("pricePlan", pricePlan, this.pricePlan);
	}
}
