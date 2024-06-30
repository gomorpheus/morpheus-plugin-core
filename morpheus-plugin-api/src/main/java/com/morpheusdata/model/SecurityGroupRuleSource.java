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

import com.morpheusdata.model.projection.SecurityGroupRuleIdentityProjection;

/**
 * SecurityGroupRuleSources are used to model the firewall rule Sources. For example,
 * the source groups in NSX-T firewalls are modeled using SecurityGroupRuleSource.
 */
public class SecurityGroupRuleSource extends MorpheusModel {

	protected SecurityGroupRuleIdentityProjection securityGroupRule;
	protected String name;
	protected String source;
	protected String sourceType = "cidr"; //cidr, group, tier, all
	protected String sourceRefType; //external type and id
	protected String sourceRefId;
	protected Boolean enabled = true;
	//linking
	protected String internalId;
	protected String externalId;
	protected String uniqueId;
	protected String providerId;
	protected String externalType;
	protected String iacId; //id for infrastructure as code integrations
	protected String rawData;

	public SecurityGroupRuleIdentityProjection getSecurityGroupRule() {
		return securityGroupRule;
	}

	public void setSecurityGroupRule(SecurityGroupRuleIdentityProjection securityGroupRule) {
		this.securityGroupRule = securityGroupRule;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceRefType() {
		return sourceRefType;
	}

	public void setSourceRefType(String sourceRefType) {
		this.sourceRefType = sourceRefType;
	}

	public String getSourceRefId() {
		return sourceRefId;
	}

	public void setSourceRefId(String sourceRefId) {
		this.sourceRefId = sourceRefId;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public String getIacId() {
		return iacId;
	}

	public void setIacId(String iacId) {
		this.iacId = iacId;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}
}
