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


public class CloudIdentityProjection extends MorpheusIdentityModel {

	public CloudIdentityProjection(){}

	public CloudIdentityProjection(Long id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	protected String name;
	protected String code;

	/**
	 * Cloud name
	 * @return String the name of the Cloud
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Unique code
	 * @return String the unique code
	 */
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}
}
