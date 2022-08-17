package com.morpheusdata.model;

import com.morpheusdata.model.projection.NetworkIdentityProjection;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Network that is typically provided via a {@link com.morpheusdata.core.CloudProvider}. These are typically
 * used during provisioning of compute or services. They also can be used to allow the user to specify network specific information
 * such as Gateway information or network CIDR specifications. Networks can also be linked to {@link NetworkPool} or {@link NetworkDomain} objects
 * which affect how various compute instances are orchestrated via Morpheus.
 *
 * @see NetworkType
 *
 * @author David Estes
 */
public class Network extends NetworkIdentityProjection {
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Cloud cloud;
	protected Long zonePoolId;
	protected NetworkType type;
	protected String networkType = "vlan"; //old field - replaced with type domain
	protected String displayName;
	protected String interfaceName;
	protected String bridgeName;
	protected String bridgeInterface;
	protected String description;
	protected String internalId;
	protected String uniqueId;
	protected String providerId;
	protected String scopeId;
	protected String externalType = "Network";
	protected String refUrl;
	protected String refType;
	protected Long refId;
	protected Integer vlanId;
	protected Integer vxlanId;
	protected String vswitchName;
	protected String tenantName;
	protected Boolean dhcpServer = false;
	protected String dhcpIp;
	protected String gateway;
	protected String netmask;
	protected String broadcast;
	protected String subnetAddress;
	protected String dnsPrimary;
	protected String dnsSecondary;
	protected String cidr;
	protected String tftpServer;
	protected String bootFile;
	protected String switchId;
	protected String fabricId;
	protected String networkRole;
	protected String status;
	protected String statusMessage;
	protected String availabilityZone;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkPool pool;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkProxy networkProxy;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkDomain networkDomain;
	protected Integer prefixLength;
	protected Boolean active = true;
	protected Boolean defaultNetwork = false;
	protected Boolean assignPublicIp = false;
	public NetworkServer networkServer;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public Account owner;
	public String category;
	public String code;
	public Integer cidrMask;
	protected NetworkIdentityProjection parentNetwork;

	protected List<ComputeZonePool> assignedZonePools = new ArrayList<>();

	public void setCloudId(Long id) {
		this.cloud = new Cloud();
		this.cloud.id = id;
		markDirty("cloud", id);
	}

	public void setZonePoolId(Long zonePoolId) {
		this.zonePoolId = zonePoolId;
		markDirty("zonePoolId", zonePoolId);
	}

	public NetworkType getType() {
		return type;
	}

	public void setType(NetworkType type) {
		this.type = type;
		markDirty("type", type);
	}


	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName", displayName);
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

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
		markDirty("scopeId", scopeId);
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
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

	public Integer getVxlanId() {
		return vxlanId;
	}

	public void setVxlanId(Integer vxlanId) {
		this.vxlanId = vxlanId;
		markDirty("vxlanId", vxlanId);
	}

	public String getVswitchName() {
		return vswitchName;
	}

	public void setVswitchName(String vswitchName) {
		this.vswitchName = vswitchName;
		markDirty("vswitchName", vswitchName);
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
		markDirty("tenantName", tenantName);
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

	public String getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(String broadcast) {
		this.broadcast = broadcast;
		markDirty("broadcast", broadcast);
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

	public String getSwitchId() {
		return switchId;
	}

	public void setSwitchId(String switchId) {
		this.switchId = switchId;
		markDirty("switchId", switchId);
	}

	public String getFabricId() {
		return fabricId;
	}

	public void setFabricId(String fabricId) {
		this.fabricId = fabricId;
		markDirty("fabricId", fabricId);
	}

	public String getNetworkRole() {
		return networkRole;
	}

	public void setNetworkRole(String networkRole) {
		this.networkRole = networkRole;
		markDirty("networkRole", networkRole);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		markDirty("status", status);
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

	public List<ComputeZonePool> getAssignedZonePools() {
		return assignedZonePools;
	}

	public void setAssignedZonePools(List<ComputeZonePool> assignedZonePools) {
		this.assignedZonePools = assignedZonePools;
	}

	public NetworkIdentityProjection getParentNetwork() {
		return parentNetwork;
	}

	public void setParentNetwork(NetworkIdentityProjection parentNetwork) {
		this.parentNetwork = parentNetwork;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
}
