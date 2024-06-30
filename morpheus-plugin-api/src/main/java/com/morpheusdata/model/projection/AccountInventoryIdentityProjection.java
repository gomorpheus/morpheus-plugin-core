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
 * Represents ansible inventory files. This is the identity projection class that is used for syncing inventory.
 *
 * @see com.morpheusdata.model.AccountInventory
 * @author David Estes
 * @since 0.8.0
 */
public class AccountInventoryIdentityProjection extends MorpheusIdentityModel {

	protected String externalId;

	/**
	 * Gets the external id associated with this inventory. This is typically the unique id of the inventory on the remote integration
	 * @return the external id
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the external id on the inventory based on the passed id. Typically this would be done in a sync job
	 * @param externalId the external reference id of the inventory file
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	
}
