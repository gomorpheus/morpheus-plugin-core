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
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.model.projection.NetworkPoolIpIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.Date;

/**
 * An IP Address / Host Record within a {@link NetworkPool} typically used with IPAM Services.
 * Host records control allocations/reservations of both DHCP and Static IPs to associated workloads
 * @see IPAMProvider
 * @author David Estes
 */
public class NetworkPoolIp extends NetworkPoolIpIdentityProjection {

	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkPool networkPool;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected NetworkPoolRange networkPoolRange;
	/**
	 * Tracks the type of reservation this ip is. You can look at {@link IPType} for a list of available
	 * types
	 */
	protected String ipType = IPType.ASSIGNED.toString();
	protected String gatewayAddress;
	protected String subnetMask;
	protected String dnsServer;
	protected String interfaceName;
	protected String description;
	protected Boolean active = true;
	protected Boolean staticIp = true;
	protected String fqdn;
	protected String domainName;
	protected String hostname;
	protected String macAddress;
	protected String internalId;
	protected String ptrId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected Date startDate;
	protected Date endDate;
	protected String refType;
	protected Long refId;
	protected Long subRefId; //for multiple nics on same host
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected NetworkDomain domain;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	protected User createdBy;

	public NetworkPoolRange getNetworkPoolRange() {
		return networkPoolRange;
	}

	public void setNetworkPoolRange(NetworkPoolRange networkPoolRange) {
		this.networkPoolRange = networkPoolRange;
		markDirty("networkPoolRange", networkPoolRange);
	}

	/**
	 * Grabs the current {@link IPType} String representation for determining the type of reservation.
	 * i.e. is it a DHCP Reservation, or a Host record, or just reserved.
	 * @return the current {@link IPType}
	 */
	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
		markDirty("ipType", ipType);
	}

	public String getGatewayAddress() {
		return gatewayAddress;
	}

	public void setGatewayAddress(String gatewayAddress) {
		this.gatewayAddress = gatewayAddress;
		markDirty("gatewayAddress", gatewayAddress);
	}

	public String getSubnetMask() {
		return subnetMask;
	}

	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
		markDirty("subnetMask", subnetMask);
	}

	public String getDnsServer() {
		return dnsServer;
	}

	public void setDnsServer(String dnsServer) {
		this.dnsServer = dnsServer;
		markDirty("dnsServer", dnsServer);
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		markDirty("interfaceName", interfaceName);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getStaticIp() {
		return staticIp;
	}

	public void setStaticIp(Boolean staticIp) {
		this.staticIp = staticIp;
		markDirty("staticIp", staticIp);
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
		markDirty("fqdn", fqdn);
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
		markDirty("domainName", domainName);
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
		markDirty("hostname", hostname);
	}

	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getPtrId() {
		return ptrId;
	}

	public void setPtrId(String ptrId) {
		this.ptrId = ptrId;
		markDirty("ptrId", ptrId);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
		markDirty("dateCreated", dateCreated);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
		markDirty("lastUpdated", lastUpdated);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		markDirty("startDate", startDate);
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		markDirty("endDate", endDate);
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public Long getSubRefId() {
		return subRefId;
	}

	public void setSubRefId(Long subRefId) {
		this.subRefId = subRefId;
		markDirty("subRefId", subRefId);
	}

	public NetworkDomain getDomain() {
		return domain;
	}

	public void setDomain(NetworkDomain domain) {
		this.domain = domain;
		markDirty("domain", domain);
	}

	public NetworkPool getNetworkPool() {
		return networkPool;
	}

	public void setNetworkPool(NetworkPool networkPool) {
		this.networkPool = networkPool;
		markDirty("networkPool",networkPool);
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
		markDirty("createdBy",createdBy);
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}


	/**
	 * Represents the available IP Type Options Morpheus Understands.
	 * If the IP is free for use, simply delete the record instead of setting an IPType
	 */
	public enum IPType {
		ASSIGNED("assigned"),
		RESERVED("reserved"),
		TRANSIENT("transient"),
		UNMANAGED("unmanaged");
		private final String value;

		IPType(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	}
}
