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

import com.morpheusdata.core.network.loadbalancer.MorpheusLoadBalancerScriptService;
import com.morpheusdata.model.projection.MorpheusIdentityModel;
import com.morpheusdata.model.NetworkLoadBalancerScript;

/**
 * Provides a subset of properties from the {@link NetworkLoadBalancerScript} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @see MorpheusLoadBalancerScriptService
 * @author David Estes
 */
public class LoadBalancerScriptIdentityProjection extends MorpheusIdentityModel {
	protected String externalId;
	protected String name;

	public LoadBalancerScriptIdentityProjection(){}
	public LoadBalancerScriptIdentityProjection(String externalId, String name) {
		this.externalId = externalId;
		this.name = name;
	}

	/**
	 * returns the externalId also known as the API id of the equivalent object.
	 * @return the external id or API id of the current record
	 */
	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the name of the script. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the script
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}
}
