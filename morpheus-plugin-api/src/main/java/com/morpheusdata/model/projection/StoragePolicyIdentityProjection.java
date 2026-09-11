/*
 *  Copyright 2026 Morpheus Data, LLC.
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
import com.morpheusdata.model.StoragePolicy;

/**
 * Provides a subset of properties from the {@link StoragePolicy} object for doing a sync match
 * comparison with less bandwidth usage and memory footprint. This is a DTO Projection object
 * @author Morpheus Data
 */
public class StoragePolicyIdentityProjection extends MorpheusIdentityModel {

	protected String code;
	protected String name;

	public StoragePolicyIdentityProjection() {
		//default
	}

	public StoragePolicyIdentityProjection(Long id, String code, String name) {
		this.id = id;
		this.code = code;
		this.name = name;
	}

	/**
	 * Gets the stable per-tier code of the storage policy. This is on the identity projection in case a fallback match needs to happen by code
	 * @return the current code of the storage policy
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code of the storage policy. Typically this isnt called directly.
	 * @param code the code of the storage policy to be assigned.
	 */
	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}

	/**
	 * Gets the name of the storage policy. This is on the identity projection in case a fallback match needs to happen by name
	 * @return the current name of the storage policy
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the storage policy. Typically this isnt called directly.
	 * @param name the name of the storage policy to be assigned.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

}
