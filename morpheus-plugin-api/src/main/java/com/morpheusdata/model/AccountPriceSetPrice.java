package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class AccountPriceSetPrice extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountPrice price;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountPriceSet priceSet;
	protected Date startDate = new Date();
	protected Date endDate = new Date(1100, 0, 1) ; // The year 3000

	public AccountPrice getPrice() {
		return price;
	}

	public void setPrice(AccountPrice price) {
		this.price = price;
		markDirty("price", price, this.price);
	}

	public AccountPriceSet getPriceSet() {
		return priceSet;
	}

	public void setPriceSet(AccountPriceSet priceSet) {
		this.priceSet = priceSet;
		markDirty("priceSet", priceSet, this.priceSet);
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
