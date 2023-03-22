package com.morpheusdata.model;

import com.morpheusdata.model.projection.NetworkIdentityProjection;

import java.util.Date;

public class NetworkRouterInterface extends MorpheusModel {
	protected NetworkIdentityProjection network;
	protected NetworkSubnet networkSubnet;
	protected String ipAddress;
	protected String secondaryIpAddress;
	protected String name;
	protected String code;
	protected String category;
	protected String description;
	protected Boolean enabled = true;
	//network details
	protected String networkPosition;
	protected Integer displayOrder = 0;
	protected String ipv6Address;
	protected String ipSubnet;
	protected String ipv6Subnet;
	protected String cidr;
	protected String gateway;
	protected Boolean dhcp = false;
	protected Boolean active = false;
	protected Boolean poolAssigned = false;
	protected Boolean primaryInterface = false;
	protected String ipMode; //dhcp/static/pool
	protected String interfaceType; //uplink,internal
	protected String networkType; //internal, external, management
	protected String macAddress;
	protected String externalLink;

	protected String rawData;
	//linking
	protected String status;
	protected String syncSource = "external";
	protected String refType;
	protected Long refId;
	protected String internalId;
	protected String externalId;
	protected Date dateCreated;
	protected Date lastUpdated;
	protected String providerId;

	public NetworkIdentityProjection getNetwork() {
		return network;
	}

	public void setNetwork(NetworkIdentityProjection network) {
		this.network = network;
	}

	public NetworkSubnet getNetworkSubnet() {
		return networkSubnet;
	}

	public void setNetworkSubnet(NetworkSubnet networkSubnet) {
		this.networkSubnet = networkSubnet;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getSecondaryIpAddress() {
		return secondaryIpAddress;
	}

	public void setSecondaryIpAddress(String secondaryIpAddress) {
		this.secondaryIpAddress = secondaryIpAddress;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getNetworkPosition() {
		return networkPosition;
	}

	public void setNetworkPosition(String networkPosition) {
		this.networkPosition = networkPosition;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getIpv6Address() {
		return ipv6Address;
	}

	public void setIpv6Address(String ipv6Address) {
		this.ipv6Address = ipv6Address;
	}

	public String getIpSubnet() {
		return ipSubnet;
	}

	public void setIpSubnet(String ipSubnet) {
		this.ipSubnet = ipSubnet;
	}

	public String getIpv6Subnet() {
		return ipv6Subnet;
	}

	public void setIpv6Subnet(String ipv6Subnet) {
		this.ipv6Subnet = ipv6Subnet;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public Boolean getDhcp() {
		return dhcp;
	}

	public void setDhcp(Boolean dhcp) {
		this.dhcp = dhcp;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getPoolAssigned() {
		return poolAssigned;
	}

	public void setPoolAssigned(Boolean poolAssigned) {
		this.poolAssigned = poolAssigned;
	}

	public Boolean getPrimaryInterface() {
		return primaryInterface;
	}

	public void setPrimaryInterface(Boolean primaryInterface) {
		this.primaryInterface = primaryInterface;
	}

	public String getIpMode() {
		return ipMode;
	}

	public void setIpMode(String ipMode) {
		this.ipMode = ipMode;
	}

	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSyncSource() {
		return syncSource;
	}

	public void setSyncSource(String syncSource) {
		this.syncSource = syncSource;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
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

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
}
