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

/**
 * Describes an Operating System Image at a high level
 * @see com.morpheusdata.model.ComputeServer
 * @see com.morpheusdata.model.VirtualImage
 * @see com.morpheusdata.model.OsType
 *
 * @author Luke Davitt
 * @since 1.2.0
 */
public class OsTypeImage extends MorpheusModel {

	protected String code;
	protected OsType osType;
	protected Account account;
	protected ProvisionType provisionType;
	protected CloudType computeZoneType;
	protected Cloud zone;
	protected ComputeSite site;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		markDirty("code", code);
	}


	public OsType getOsType() {
		return osType;
	}

	public void setOsType(OsType osType) {
		this.osType = osType;
		markDirty("osType", osType);
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
		markDirty("account", account);
	}

	public ProvisionType provisionType() {
		return provisionType;
	}

	public void setProvisionType(ProvisionType provisionType) {
		this.provisionType = provisionType;
		markDirty("provisionType", provisionType);
	}

	public CloudType getComputeZoneType() {
		return computeZoneType;
	}

	public void setComputeZoneType(CloudType computeZoneType) {
		this.computeZoneType = computeZoneType;
		markDirty("computeZoneType", computeZoneType);
	}

	public Cloud getZone() {
		return zone;
	}

	public void setZone(Cloud zone) {
		this.zone = zone;
		markDirty("zone", zone);
	}


	public ComputeSite getSite() {
		return site;
	}

	public void setSite(ComputeSite site) {
		this.site = site;
		markDirty("site", site);
	}
}
