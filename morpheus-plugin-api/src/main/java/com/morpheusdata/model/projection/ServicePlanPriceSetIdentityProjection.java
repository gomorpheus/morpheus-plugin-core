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
