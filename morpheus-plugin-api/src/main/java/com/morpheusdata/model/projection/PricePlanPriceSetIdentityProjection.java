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
