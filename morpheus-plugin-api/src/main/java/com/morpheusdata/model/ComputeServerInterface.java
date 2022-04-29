package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.UUID;

/**
 * Network interface of a ComputeServer
 *
 * @author Eric Helgeson
 * @since 0.8.0
 */
public class ComputeServerInterface extends MorpheusModel {

	protected String refType;
	protected Long refId;
	protected String name;
	protected String internalId;
	protected String externalId;
	protected String uniqueId;
	protected String publicIpAddress;
	protected String publicIpv6Address;
	protected String ipAddress;
	protected String ipv6Address;
	protected String ipSubnet;
	protected String ipv6Subnet;
	protected String description;
	protected Boolean dhcp = true;
	protected Boolean active = true;
	protected Boolean poolAssigned = false;
	protected Boolean primaryInterface = true;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected Network network;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkSubnet subnet;
//	public NetworkGroup networkGroup;
protected String networkPosition;
	protected Integer displayOrder = 0;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkPool networkPool;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	protected NetworkDomain networkDomain;
	public ComputeServerInterfaceType type;
protected String ipMode; //dhcp/static/pool
	protected Boolean replaceHostRecord;
	protected String macAddress;
	protected String externalType;
	protected String interfaceId;
	protected String vlanId;
	protected String fabricId;
	protected String uuid = UUID.randomUUID().toString();

	public String getRefType() {
		return refType;
	}

	public Long getRefId() {
		return refId;
	}

	public String getName() {
		return name;
	}

	public String getInternalId() {
		return internalId;
	}

	public String getExternalId() {
		return externalId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public String getPublicIpAddress() {
		return publicIpAddress;
	}

	public String getPublicIpv6Address() {
		return publicIpv6Address;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getIpv6Address() {
		return ipv6Address;
	}

	public String getIpSubnet() {
		return ipSubnet;
	}

	public String getIpv6Subnet() {
		return ipv6Subnet;
	}

	public String getDescription() {
		return description;
	}

	public Boolean getDhcp() {
		return dhcp;
	}

	public Boolean getActive() {
		return active;
	}

	public Boolean getPoolAssigned() {
		return poolAssigned;
	}

	public Boolean getPrimaryInterface() {
		return primaryInterface;
	}

	public Network getNetwork() {
		return network;
	}

	public NetworkSubnet getSubnet() {
		return subnet;
	}

	public String getNetworkPosition() {
		return networkPosition;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public NetworkPool getNetworkPool() {
		return networkPool;
	}

	public NetworkDomain getNetworkDomain() {
		return networkDomain;
	}

	public ComputeServerInterfaceType getType() { return type; }

	public String getIpMode() {
		return ipMode;
	}

	public Boolean getReplaceHostRecord() {
		return replaceHostRecord;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public String getExternalType() {
		return externalType;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public String getVlanId() {
		return vlanId;
	}

	public String getFabricId() {
		return fabricId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	public void setRefId(Long refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}

	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
		markDirty("uniqueId", uniqueId);
	}

	public void setPublicIpAddress(String publicIpAddress) {
		this.publicIpAddress = publicIpAddress;
		markDirty("publicIpAddress", publicIpAddress);
	}

	public void setPublicIpv6Address(String publicIpv6Address) {
		this.publicIpv6Address = publicIpv6Address;
		markDirty("publicIpv6Address", publicIpv6Address);
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
		markDirty("ipAddress", ipAddress);
	}

	public void setIpv6Address(String ipv6Address) {
		this.ipv6Address = ipv6Address;
		markDirty("ipv6Address", ipv6Address);
	}

	public void setIpSubnet(String ipSubnet) {
		this.ipSubnet = ipSubnet;
		markDirty("ipSubnet", ipSubnet);
	}

	public void setIpv6Subnet(String ipv6Subnet) {
		this.ipv6Subnet = ipv6Subnet;
		markDirty("ipv6Subnet", ipv6Subnet);
	}

	public void setDescription(String description) {
		this.description = description;
		markDirty("description", description);
	}

	public void setDhcp(Boolean dhcp) {
		this.dhcp = dhcp;
		markDirty("dhcp", dhcp);
	}

	public void setActive(Boolean active) {
		this.active = active;
		markDirty("active", active);
	}

	public void setPoolAssigned(Boolean poolAssigned) {
		this.poolAssigned = poolAssigned;
		markDirty("poolAssigned", poolAssigned);
	}

	public void setPrimaryInterface(Boolean primaryInterface) {
		this.primaryInterface = primaryInterface;
		markDirty("primaryInterface", primaryInterface);
	}

	public void setNetwork(Network network) {
		this.network = network;
		markDirty("network", network);
	}

	public void setSubnet(NetworkSubnet subnet) {
		this.subnet = subnet;
		markDirty("subnet", subnet);
	}

	public void setNetworkPosition(String networkPosition) {
		this.networkPosition = networkPosition;
		markDirty("networkPosition", networkPosition);
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
		markDirty("displayOrder", displayOrder);
	}

	public void setNetworkPool(NetworkPool networkPool) {
		this.networkPool = networkPool;
		markDirty("networkPool", networkPool);
	}

	public void setNetworkDomain(NetworkDomain networkDomain) {
		this.networkDomain = networkDomain;
		markDirty("networkDomain", networkDomain);
	}

	public void setType(ComputeServerInterfaceType type) {
		this.type = type;
		markDirty("type", type);
	}

	public void setIpMode(String ipMode) {
		this.ipMode = ipMode;
		markDirty("ipMode", ipMode);
	}

	public void setReplaceHostRecord(Boolean replaceHostRecord) {
		this.replaceHostRecord = replaceHostRecord;
		markDirty("replaceHostRecord", replaceHostRecord);
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
		markDirty("macAddress", macAddress);
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
		markDirty("externalType", externalType);
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
		markDirty("interfaceId", interfaceId);
	}

	public void setVlanId(String vlanId) {
		this.vlanId = vlanId;
		markDirty("vlanId", vlanId);
	}

	public void setFabricId(String fabricId) {
		this.fabricId = fabricId;
		markDirty("fabricId", fabricId);
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		markDirty("uuid", uuid);
	}
}
