package com.morpheusdata.model.projection;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.AccountPriceSetPrice} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Dustin DeYoung
 * @since 0.15.3
 */
public class AccountPriceSetPriceIdentityProjection extends MorpheusIdentityModel {

	protected AccountPriceSetIdentityProjection priceSet;
	protected AccountPriceIdentityProjection price;

	public AccountPriceSetIdentityProjection getPriceSet() {
		return priceSet;
	}

	public void setPriceSet(AccountPriceSetIdentityProjection priceSet) {
		this.priceSet = priceSet;
		markDirty("priceSet", priceSet, this.priceSet);
	}

	public AccountPriceIdentityProjection getPrice() {
		return price;
	}

	public void setPrice(AccountPriceIdentityProjection price) {
		this.price = price;
		markDirty("price", price, this.price);
	}
}
