package com.morpheusdata.model;

import com.morpheusdata.model.projection.NetworkSubnetIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

/**
 * Represents a NetworkSubnet that is typically provided via a {@link com.morpheusdata.core.CloudProvider}. These are typically
 * used during provisioning of compute or services. They also can be used to allow the user to specify network subnet specific information
 * such as Gateway information or network CIDR specifications.
 *
 * @see NetworkSubnetType
 *
 * @author Bob Whiton
 */
public class NetworkSubnet extends NetworkSubnetIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account account;
	public String code;
	public String category;
	protected String description;
	protected String internalId;
	protected String uniqueId;
	protected String providerId;
	protected String refUrl;
	protected String refType;
	protected Long refId;
	protected Integer vlanId;
	protected Long zonePoolId;
	protected NetworkSubnetType networkSubnetType;
	protected String displayName;
	protected String interfaceName;
	protected String bridgeName;
	protected String bridgeInterface;
	protected String gateway;
	protected String netmask;
	protected String subnetAddress;
	protected String tftpServer;
	protected String bootFile;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkPool pool;
	protected Integer prefixLength;
	protected String vswitchName;
	protected Boolean dhcpServer = false;
	protected String cidr;
	protected String dhcpIp;
	protected String dnsPrimary;
	protected String dnsSecondary;
	protected String dhcpStart;
	protected String dhcpEnd;
	protected String dhcpRange;
	protected String statusMessage;
	protected String availabilityZone;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkProxy networkProxy;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkDomain networkDomain;
	protected Boolean active = true;
	protected Boolean defaultNetwork = false;
	protected Boolean assignPublicIp = false;
	protected NetworkSubnet.Status status = NetworkSubnet.Status.AVAILABLE;

	public enum Status {
		PROVISIONING,
		AVAILABLE,
		ERROR
	}

	public void setZonePoolId(Long zonePoolId) {
		this.zonePoolId = zonePoolId;
		markDirty("zonePoolId", zonePoolId);
	}

	public NetworkSubnetType getNetworkSubnetType() {
		return networkSubnetType;
	}

	public void setNetworkSubnetType(NetworkSubnetType networkSubnetType) {
		this.networkSubnetType = networkSubnetType;
		markDirty("networkSubnetType", networkSubnetType);
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
		markDirty("interfaceName", interfaceName);
	}

	public String getBridgeName() {
		return bridgeName;
	}

	public void setBridgeName(String bridgeName) {
		this.bridgeName = bridgeName;
		markDirty("bridgeName", bridgeName);
	}

	public String getBridgeInterface() {
		return bridgeInterface;
	}

	public void setBridgeInterface(String bridgeInterface) {
		this.bridgeInterface = bridgeInterface;
		markDirty("bridgeInterface", bridgeInterface);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}


	public String getInternalId() {
		return internalId;
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
		markDirty("providerId", providerId);
	}

	public String getRefUrl() {
		return refUrl;
	}

	public void setRefUrl(String refUrl) {
		this.refUrl = refUrl;
		markDirty("refUrl", refUrl);
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

	public Integer getVlanId() {
		return vlanId;
	}

	public void setVlanId(Integer vlanId) {
		this.vlanId = vlanId;
		markDirty("vlanId", vlanId);
	}

	public String getVswitchName() {
		return vswitchName;
	}

	public void setVswitchName(String vswitchName) {
		this.vswitchName = vswitchName;
		markDirty("vswitchName", vswitchName);
	}

	public Boolean getDhcpServer() {
		return dhcpServer;
	}

	public void setDhcpServer(Boolean dhcpServer) {
		this.dhcpServer = dhcpServer;
		markDirty("dhcpServer", dhcpServer);
	}

	public String getDhcpIp() {
		return dhcpIp;
	}

	public void setDhcpIp(String dhcpIp) {
		this.dhcpIp = dhcpIp;
		markDirty("dhcpIp", dhcpIp);
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
		markDirty("gateway", gateway);
	}

	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
		markDirty("netmask", netmask);
	}

	public String getSubnetAddress() {
		return subnetAddress;
	}

	public void setSubnetAddress(String subnetAddress) {
		this.subnetAddress = subnetAddress;
		markDirty("subnetAddress", subnetAddress);
	}

	public String getDnsPrimary() {
		return dnsPrimary;
	}

	public void setDnsPrimary(String dnsPrimary) {
		this.dnsPrimary = dnsPrimary;
		markDirty("dnsPrimary", dnsPrimary);
	}

	public String getDhcpStart() {
		return dhcpStart;
	}

	public void setDhcpStart(String dhcpStart) {
		this.dhcpStart = dhcpStart;
		markDirty("dhcpStart", dhcpStart);
	}

	public String getDhcpEnd() {
		return dhcpEnd;
	}

	public void setDhcpEnd(String dhcpEnd) {
		this.dhcpEnd = dhcpEnd;
		markDirty("dhcpEnd", dhcpEnd);
	}

	public String getDhcpRange() {
		return dhcpRange;
	}

	public void setDhcpRange(String dhcpRange) {
		this.dhcpRange = dhcpRange;
		markDirty("dhcpRange", dhcpRange);
	}

	public String getDnsSecondary() {
		return dnsSecondary;
	}

	public void setDnsSecondary(String dnsSecondary) {
		this.dnsSecondary = dnsSecondary;
		markDirty("dnsSecondary", dnsSecondary);
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
		markDirty("cidr", cidr);
	}

	public String getTftpServer() {
		return tftpServer;
	}

	public void setTftpServer(String tftpServer) {
		this.tftpServer = tftpServer;
		markDirty("tftpServer", tftpServer);
	}

	public String getBootFile() {
		return bootFile;
	}

	public void setBootFile(String bootFile) {
		this.bootFile = bootFile;
		markDirty("bootFile", bootFile);
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
		markDirty("statusMessage", statusMessage);
	}

	public String getAvailabilityZone() {
		return availabilityZone;
	}

	public void setAvailabilityZone(String availabilityZone) {
		this.availabilityZone = availabilityZone;
		markDirty("availabilityZone", availabilityZone);
	}

	public NetworkPool getPool() {
		return pool;
	}

	public void setPool(NetworkPool pool) {
		this.pool = pool;
		markDirty("pool", pool);
	}

	public NetworkProxy getNetworkProxy() {
		return networkProxy;
	}

	public void setNetworkProxy(NetworkProxy networkProxy) {
		this.networkProxy = networkProxy;
		markDirty("networkProxy", networkProxy);
	}

	public NetworkDomain getNetworkDomain() {
		return networkDomain;
	}

	public void setNetworkDomain(NetworkDomain networkDomain) {
		this.networkDomain = networkDomain;
		markDirty("networkDomain", networkDomain);
	}

	public Integer getPrefixLength() {
		return prefixLength;
	}

	public void setPrefixLength(Integer prefixLength) {
		this.prefixLength = prefixLength;
		markDirty("prefixLength", prefixLength);
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public Boolean getDefaultNetwork() {
		return defaultNetwork;
	}

	public void setDefaultNetwork(Boolean defaultNetwork) {
		this.defaultNetwork = defaultNetwork;
		markDirty("defaultNetwork", defaultNetwork);
	}

	public Boolean getAssignPublicIp() {
		return assignPublicIp;
	}

	public void setAssignPublicIp(Boolean assignPublicIp) {
		this.assignPublicIp = assignPublicIp;
		markDirty("assignPublicIp", assignPublicIp);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		markDirty("status", status);
	}

}
