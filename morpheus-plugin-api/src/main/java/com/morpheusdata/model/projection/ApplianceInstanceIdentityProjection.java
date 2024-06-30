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

import com.morpheusdata.model.Account;

/**
 * Provides a subset of properties from the {@link com.morpheusdata.model.ApplianceInstance}
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Dan DeVilbiss
 * @since 0.15.10
 */
public class ApplianceInstanceIdentityProjection extends MorpheusIdentityModel {
	protected String applianceId;
	protected String applianceName;

	public String getApplianceId() {
		return applianceId;
	}

	public void setApplianceId(String applianceId) {
		this.applianceId = applianceId;
		markDirty("applianceId", applianceId, this.applianceId);
	}

	public String getApplianceName() {
		return applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
		markDirty("applianceName", applianceName, this.applianceName);
	}
}
