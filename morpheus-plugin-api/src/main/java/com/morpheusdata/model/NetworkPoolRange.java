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
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.math.BigInteger;
import java.util.Date;

public class NetworkPoolRange extends MorpheusModel {

	protected String startAddress;
	protected String endAddress;
	protected String startIPv6Address;
	protected String endIPv6Address;
	protected String internalId;
	protected String externalId;
	protected String description;
	protected BigInteger addressCount = new BigInteger("0");
	protected Boolean active = true;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Boolean readOnly = false;
	protected String cidr;
	protected String cidrIPv6;

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkPool networkPool;

	protected Integer reservationCount;

	public String getStartAddress() {
		return startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public String getInternalId() {
		return internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getDescription() {
		return description;
	}

	public BigInteger getAddressCount() {
		return addressCount;
	}

	public Boolean getActive() {
		return active;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public NetworkPool getNetworkPool() {
		return networkPool;
	}

	public Integer getReservationCount() {
		return reservationCount;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
		markDirty("startAddress", startAddress);
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
		markDirty("endAddress", endAddress);
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setAddressCount(BigInteger addressCount) {
		this.addressCount = addressCount;
		markDirty("addressCount", addressCount);
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public void setNetworkPool(NetworkPool networkPool) {
		this.networkPool = networkPool;
		markDirty("networkPool", networkPool);
	}

	public void setReservationCount(Integer reservationCount) {
		this.reservationCount = reservationCount;
		markDirty("reservationCount", reservationCount);
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getStartIPv6Address() {
		return startIPv6Address;
	}

	public void setStartIPv6Address(String startIPv6Address) {
		this.startIPv6Address = startIPv6Address;
	}

	public String getEndIPv6Address() {
		return endIPv6Address;
	}

	public void setEndIPv6Address(String endIPv6Address) {
		this.endIPv6Address = endIPv6Address;
	}

	public String getCidrIPv6() {
		return cidrIPv6;
	}

	public void setCidrIPv6(String cidrIPv6) {
		this.cidrIPv6 = cidrIPv6;
	}
}
