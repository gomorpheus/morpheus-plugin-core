/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

public class PricePlanPriceSet extends MorpheusModel {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected PricePlan pricePlan;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected AccountPriceSet priceSet;
	protected Date startDate = new Date();
	protected Date endDate = new Date(1100, 0, 1) ; // The year 3000

	public PricePlan getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(PricePlan pricePlan) {
		this.pricePlan = pricePlan;
		markDirty("pricePlan", pricePlan, this.pricePlan);
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
