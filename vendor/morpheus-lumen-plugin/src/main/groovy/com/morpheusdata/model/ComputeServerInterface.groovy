package com.morpheusdata.model

class ComputeServerInterface extends MorpheusModel {
	public String refType;
	public Long refId;
	public String name;
	public String internalId;
	public String externalId;
	public String uniqueId;
	public String publicIpAddress;
	public String publicIpv6Address;
	public String ipAddress;
	public String ipv6Address;
	public String ipSubnet;
	public String ipv6Subnet;
	public String description;
	public Boolean dhcp = true;
	public Boolean active = true;
	public Boolean poolAssigned = false;
	public Boolean primaryInterface = true;
	public Network network;
	public NetworkSubnet subnet;
	public NetworkGroup networkGroup;
	public String networkPosition;
	public Integer displayOrder = 0;
	public NetworkPool networkPool;
	public NetworkDomain networkDomain;
	public ComputeServerInterfaceType type;
	public String ipMode //dhcp/static/pool;
	public Boolean replaceHostRecord;
	public String macAddress;
	public String externalType;
	public String interfaceId;
	public String vlanId;
	public String fabricId;
	public String uuid = UUID.randomUUID();
}
