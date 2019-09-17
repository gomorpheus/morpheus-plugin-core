package com.morpheusdata.model;

import java.util.Date;

public class NetworkPoolIp extends MorpheusModel {

	NetworkPool networkPool;
	private NetworkPoolRange networkPoolRange;
	private String ipType = "assigned"; //assigned, reserved
	private String ipAddress;
	private String gatewayAddress;
	private String subnetMask;
	private String dnsServer;
	private String interfaceName;
	private String description;
	private Boolean active = true;
	private Boolean staticIp = true;
	private String fqdn;
	private String domainName;
	private String hostname;



	private String internalId;
	private String externalId;
	private String ptrId;
	private Date dateCreated;
	private Date lastUpdated;
	private Date startDate;
	private Date endDate;
	private String refType;
	private Long refId;
	private Long subRefId; //for multiple nics on same host
	private NetworkDomain domain;
	public User createdBy;

	public NetworkPoolRange getNetworkPoolRange() {
		return networkPoolRange;
	}

	public void setNetworkPoolRange(NetworkPoolRange networkPoolRange) {
		this.networkPoolRange = networkPoolRange;
		markDirty("networkPoolRange", networkPoolRange);
	}

	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
		markDirty("ipType", ipType);
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		markDirty("ipAddress", ipAddress);
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

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
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
}
