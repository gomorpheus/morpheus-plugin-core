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
import com.morpheusdata.model.projection.NetworkRouteTableIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

public class NetworkRouteTable extends NetworkRouteTableIdentityProjection {
	protected String name;
	protected String code;
	protected String category;
	//linking
	protected String internalId;
	protected String iacId; //id for infrastructure as code integrations
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected CloudPool zonePool;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
	}

	public CloudPool getZonePool() {
		return zonePool;
	}

	public void setZonePool(CloudPool zonePool) {
		this.zonePool = zonePool;
	}
}
