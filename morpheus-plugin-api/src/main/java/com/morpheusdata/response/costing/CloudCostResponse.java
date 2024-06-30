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

package com.morpheusdata.response.costing;

/**
 * The Cloud Cost Response containing summary data when loading CloudCost via the {@link com.morpheusdata.core.costing.MorpheusAccountInvoiceService}
 *
 * @since 0.12.2
 * @author David Estes
 * @see com.morpheusdata.core.costing.MorpheusAccountInvoiceService
 */
public class CloudCostResponse {
	protected Double cost;
	protected Double price;
	protected Long instanceCount;
	protected Long serverCount;
	protected Long discoveredCount;

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(Long instanceCount) {
		this.instanceCount = instanceCount;
	}

	public Long getServerCount() {
		return serverCount;
	}

	public void setServerCount(Long serverCount) {
		this.serverCount = serverCount;
	}

	public Long getDiscoveredCount() {
		return discoveredCount;
	}

	public void setDiscoveredCount(Long discoveredCount) {
		this.discoveredCount = discoveredCount;
	}
}
