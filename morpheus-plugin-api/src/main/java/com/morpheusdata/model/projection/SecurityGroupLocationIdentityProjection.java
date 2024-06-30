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


public class SecurityGroupLocationIdentityProjection extends MorpheusIdentityModel {

	public SecurityGroupLocationIdentityProjection(){}

	public SecurityGroupLocationIdentityProjection(Long id, String name, String externalId, String category, Long refId, String refType, SecurityGroupIdentityProjection securityGroup) {
		this.id = id;
		this.name = name;
		this.externalId = externalId;
		this.category = category;
		this.refId = refId;
		this.refType = refType;
		this.securityGroup = securityGroup;
	}

	protected SecurityGroupIdentityProjection securityGroup;
	protected String name;
	protected String externalId;
	protected String category;
	protected Long refId;
	protected String refType;

	public SecurityGroupIdentityProjection getSecurityGroup() {
		return securityGroup;
	}

	public void setSecurityGroup(SecurityGroupIdentityProjection securityGroup) {
		this.securityGroup = securityGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}
}
