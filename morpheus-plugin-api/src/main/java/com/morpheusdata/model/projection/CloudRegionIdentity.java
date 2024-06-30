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

import com.morpheusdata.model.MorpheusModel;

/**
 * This provides a way to query a list of regions and sync those regions for particular {@link com.morpheusdata.model.Cloud} objects.
 * Some clouds have dynamic region lists and this provides a mechanism to grab those regions and sync by them
 * @author David Estes
 * @since 0.15.3
 * @see com.morpheusdata.model.CloudRegion
 */
public class CloudRegionIdentity extends MorpheusModel {
	protected String externalId;

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
}
