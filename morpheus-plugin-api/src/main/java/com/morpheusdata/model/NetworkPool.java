package com.morpheusdata.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.morpheusdata.core.IPAMProvider;
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection;
import com.morpheusdata.model.serializers.ModelAsIdOnlySerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a Model representation of a Network Pool typically provided or synced from an IPAM Provider. Some Providers
 * call these Subnets/Networks or IP Blocks. These should be synced via the relevant {@link com.morpheusdata.core.IPAMProvider} implementation.
 * A Pool typically has a list of host record associated with it and keeps track of reservations that have been made on the particular pool.
 *
 * @see com.morpheusdata.core.IPAMProvider
 *
 * @author David Estes
 */
public class NetworkPool extends NetworkPoolIdentityProjection {



	/**
	 * Gets the Human readable name of the Network Pool this record represents. Some integrations represent this as the CIDR name
	 * if there is no available descriptive name provided. However, if there is the name should be provided via the sync services
	 * @return a string representation of the human readable name of the current network pool.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the human readable name of the network pool this record represents. Some integrations may set this value to the CIDR name
	 * if there is no such thing as a humane readable name to represent the current block.
	 * @param name a String representation of the human readable name of the current network pool.
	 */
	public void setName(String name) {
		this.name = name;
		markDirty("name", name);
	}

	/**
	 * Gets the display name of the current Network Pool. Pools can have a display name for situations where the name
	 * of the pool is not easily consumable by its users.
	 * @return the user friendly display name of the current Pool
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name of the current Network Pool. Pools can have a display name for situations where the name
	 * of the pool is not easily consumable by its users.
	 * @param displayName String representing the user friendly name of the pool.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		markDirty("displayName", displayName);
	}

	/**
	 * Gets an optional alternative reference id field. This use can vary from provider to provider and may be used for storing
	 * alternative ids of the same object or a correlating id of the pool as it relates to a Network it may belong to depending
	 * on the {@link IPAMProvider} implementation.
	 * @return an alternative identifier used for sync purposes dependent on implementation.
	 */
	public String getInternalId() {
		return internalId;
	}

	/**
	 * Sets an alternative reference id that can be used by the relevant {@link IPAMProvider} implementation for whatever purposes
	 * are needed during sync operations.
	 * @param internalId an alternative identifier used for sync purposes dependent on implementation.
	 */
	public void setInternalId(String internalId) {
		this.internalId = internalId;
		markDirty("internalId", internalId);
	}

	/**
	 * Gets the external unique identifier. The externalId is available on most sync related Models and is the unique identifier used by the external integration of the
	 * correlating object. Whenever syncing something like a Domain record, the unique identifier provided by the third party vendor
	 * should be stored here for cross referencing during sync.
	 * @return the external unique identifier representation of this pool from the external integration.
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the external unique identifer as it relates to the integration provider. Whenever syncing something like a Pool record, the unique identifier provided by the third party vendor
	 * should be stored here for cross referencing during sync.
	 * @param externalId the external unique identifier representation of this cloud from the external integration.
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
		markDirty("externalId", externalId);
	}

	/**
	 * Gets the Primary DNS Domain for searching for relative hostnames within the network. This information is normally on the network
	 * but is also on the Block if the relevent {@link IPAMProvider} syncs this data.
	 *
	 * @return The Primary DNS Domain for this network
	 */
	public String getDnsDomain() {
		return dnsDomain;
	}

	/**
	 * Sets the Primary DNS Domain for searching for relative hostnames within the network. This information is normally on the network
	 * but is also on the Block if the relevent {@link IPAMProvider} syncs this data.
	 *
	 * @param dnsDomain The Primary DNS Domain for this network
	 */
	public void setDnsDomain(String dnsDomain) {
		this.dnsDomain = dnsDomain;
		markDirty("dnsDomain", dnsDomain);
	}

	/**
	 * Gets the Host Prefix associated with this Pool.
	 * @return a host prefix qualifier
	 */
	public String getHostPrefix() {
		return hostPrefix;
	}


	/**
	 * Sets the Host Prefix associated with this Pool.
	 * @param hostPrefix a host prefix qualifier
	 */
	public void setHostPrefix(String hostPrefix) {
		this.hostPrefix = hostPrefix;
		markDirty("hostPrefix", hostPrefix);
	}

	/**
	 * Gets the Proxy Url for the current Pool. Some Network Pools have information corresponding to a proxy server that may exist on the network. This information is typically
	 * not used by Morpheus and configured elsewhere but it is otherwise synced into the Pool record for reference.
	 * @return the Proxy URL for the specified Network Pool
	 */
	public String getHttpProxy() {
		return httpProxy;
	}

	/**
	 * Sets the Proxy Url for the current Pool. Some Network Pools have information corresponding to a proxy server that may exist on the network. This information is typically
	 * not used by Morpheus and configured elsewhere but it is otherwise synced into the Pool record for reference.
	 * @param httpProxy the Proxy URL for the specified Network Pool
	 */
	public void setHttpProxy(String httpProxy) {
		this.httpProxy = httpProxy;
		markDirty("httpProxy", httpProxy);
	}

	/**
	 * Gets a list of ip addresses of nameservers that are assigned to the relevant Network Pool. Typically this data is grabbed
	 * off of the {@link Network} during IP Allocation but if that data has not been entered, it could be grabbed from the pool data
	 * @return a List of ip addresses in priority order of nameservers.
	 */
	public List<String> getDnsServers() {
		return dnsServers;
	}

	/**
	 * Sets a list of ip addresses of nameservers that are assigned to the relevant Network Pool. Typically this data is grabbed
	 * off of the {@link Network} during IP Allocation but if that data has not been entered, it could be grabbed from the pool data
	 * @param dnsServers a List of ip addresses in priority order of nameservers.
	 */
	public void setDnsServers(List<String> dnsServers) {
		this.dnsServers = dnsServers;
		markDirty("dnsServers", dnsServers);
	}

	/**
	 * Gets a list of DNS Search suffixes. Some {@link IPAMProvider} blocks provide network detail information. If that information
	 * is not defined on the {@link Network} that the pool is used for within Morpheus, this information is utilized
	 * @return a List of DNS Search Suffixes
	 */
	public List<String> getDnsSuffixList() {
		return dnsSuffixList;
	}

	/**
	 * Sets a list of DNS Search suffixes. Some {@link IPAMProvider} blocks provide network detail information. If that information
	 * is not defined on the {@link Network} that the pool is used for within Morpheus, this information is utilized
	 * @param dnsSuffixList a List of DNS Search Suffixes
	 */
	public void setDnsSuffixList(List<String> dnsSuffixList) {
		this.dnsSuffixList = dnsSuffixList;
		markDirty("dnsSuffixList", dnsSuffixList);
	}

	/**
	 * Gets the Gateway ip address related to the current Network Block. In most cases this information is unavailable and stored
	 * on the {@link Network} object during IP Allocation. However, if that information has not been specified by the user, then this
	 * property will be checked to see if an available Gateway IP Address can be assigned to the Server being provisioned.
	 * @return the gateway ip address of the current pool
	 */
	public String getGateway() {
		return gateway;
	}

	/**
	 * Sets the Gateway ip address related to the current Network Block. In most cases this information is unavailable and stored
	 * on the {@link Network} object during IP Allocation. However, if that information has not been specified by the user, then this
	 * property will be checked to see if an available Gateway IP Address can be assigned to the Server being provisioned.
	 * @param gateway the gateway ip address of the current pool
	 */
	public void setGateway(String gateway) {
		this.gateway = gateway;
		markDirty("gateway", gateway);
	}

	/**
	 * Gets the subnet mask qualifier as defined by the IPAM Provider for the current pool. In most cases this data is not used
	 * by Morpheus but is typically provided simply for reference as most of the information is redundant to the Network that
	 * the pool is correlated to.
	 * @return the subnet mask of the current Network Pool
	 */
	public String getNetmask() {
		return netmask;
	}

	/**
	 * Sets the subnet mask qualifier as defined by the IPAM Integration in question. In some cases , if this information does not exist
	 * on the corresponding {@link Network} object during IP allocation, this information will be used if it is available.
	 * @param netmask the subnet mask of the current Network Pool
	 */
	public void setNetmask(String netmask) {
		this.netmask = netmask;
		markDirty("netmask", netmask);
	}

	/**
	 * Gets the Total number of ip addresses in the current block range based on the CIDR. This is not factoring in existing reservations.
	 *
	 * @return numerical count of number of ip addresses available within the current block.
	 */
	public Integer getIpCount() {
		return ipCount;
	}

	/**
	 * Sets the Total number of ip addresses in the current block range based on the CIDR. This is not factoring in existing reservations.
	 * @param ipCount numerical count of number of ip addresses available within the current block.
	 */
	public void setIpCount(Integer ipCount) {
		this.ipCount = ipCount;
		markDirty("ipCount", ipCount);
	}


	/**
	 * Gets the Total Number of free ip addresses in the Block that can still be allocated. Some {@link IPAMProvider} implementations
	 * set this directly based on information from the target API, and others have to calculate this based on synced IP records.
	 * @return total number of free ip addresses that can be allocated on this Network Pool
	 */
	public Integer getIpFreeCount() {
		return ipFreeCount;
	}

	/**
	 * Sets the Total Number of free ip addresses in the Block that can still be allocated. Some {@link IPAMProvider} implementations
	 * set this directly based on information from the target API, and others have to calculate this based on synced IP records.
	 * @param ipFreeCount total number of free ip addresses that can be allocated on this Network Pool
	 */
	public void setIpFreeCount(Integer ipFreeCount) {
		this.ipFreeCount = ipFreeCount;
		markDirty("ipFreeCount", ipFreeCount);
	}

	/**
	 * Gets the flag representing whether or not the current Network Pool is enabled or disabled. Some IPAM Providers have the option
	 * to selectively disable pools for allocation. This flag informs Morpheus as to the status of those pools.
	 * @return enabled state of the current pool
	 */
	public Boolean getPoolEnabled() {
		return poolEnabled;
	}

	/**
	 * Sets the flag representing whether or not the current Network Pool is enabled or disabled. Some IPAM Providers have the option
	 * to selectively disable pools for allocation. This flag informs Morpheus as to the status of those pools.
	 * @param poolEnabled enabled state of the current pool
	 */
	public void setPoolEnabled(Boolean poolEnabled) {
		this.poolEnabled = poolEnabled;
		markDirty("poolEnabled", poolEnabled);
	}

	/**
	 * Get the String form of the Model class name that this Pool Record is related to. Typically via most Integrations the value
	 * of this field should be blank since its associated with a poolServer, however some scenarios exist where this may get synced or created via an
	 * alternative means (perhaps from a CloudProvider). In the event of this being synced by a cloud provider setting
	 * the refType to 'ComputeZone' and refId to correlate to the target cloud is recommended.
	 * @return the Reference Type class name of the associated integration, typically NULL for this use case
	 */
	public String getRefType() {
		return refType;
	}

	/**
	 * Set the String form of the Model class name that this Pool Record is related to. Typically via most Integrations the value
	 * of this field should be blank since its associated with a poolServer, however some scenarios exist where this may get synced or created via an
	 * alternative means (perhaps from a CloudProvider). In the event of this being synced by a cloud provider setting
	 * the refType to 'ComputeZone' and refId to correlate to the target cloud is recommended.
	 * @param refType the Reference Type class name of the associated integration, typically NULL for this use case
	 */
	public void setRefType(String refType) {
		this.refType = refType;
		markDirty("refType", refType);
	}

	/**
	 * Get the unique qualification id for the polymorphic association to the relevant integration. For most IPAM implementations
	 * a Pool Server is associated and this is not necessary, however some pool types are synced via a cloud provider and in that
	 * scenario a reference to the 'ComputeZone' is typically stored here
	 * @return the reference id to the associated CloudProvider in the event this pool is synced from a CloudProvider.
	 */
	public String getRefId() {
		return refId;
	}

	/**
	 * Set the unique qualification id for the polymorphic association to the relevant integration. For most IPAM implementations
	 * a Pool Server is associated and this is not necessary, however some pool types are synced via a cloud provider and in that
	 * scenario a reference to the 'ComputeZone' is typically stored here
	 * @param refId the reference id to the associated CloudProvider in the event this pool is synced from a CloudProvider.
	 */
	public void setRefId(String refId) {
		this.refId = refId;
		markDirty("refId", refId);
	}


	/**
	 * Gets the Configuration name this pool is associated with. Some IPAM Providers have a concept of scoping of Blocks and DNS.
	 * These are called Configurations. This is useful for isolating various departments of large enterprises. The Primary vendor
	 * that has the concept of Configuration scoping is Bluecat.
	 * @return a String representation of the configuration name.
	 */
	public String getConfiguration() {
		return configuration;
	}


	/**
	 * Sets the Configuration name this pool is associated with. Some IPAM Providers have a concept of scoping of Blocks and DNS.
	 * These are called Configurations. This is useful for isolating various departments of large enterprises. The Primary vendor
	 * that has the concept of Configuration scoping is Bluecat.
	 * @param configuration a String representation of the configuration name.
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
		markDirty("configuration", configuration);
	}

	/**
	 * Gets the CIDR Representation of the network Pool being synced. Typically this may look like a 192.168.0.0/24 format.
	 * See CIDR Specifications for more information on how to properly form a CIDR Block.
	 * @return the CIDR block represented by the current pool.
	 */
	public String getCidr() {
		return cidr;
	}

	/**
	 * Sets the CIDR Block representation of the Network Pool being synced. Typically this may look like '192.168.0.0/24' format.
	 * See CIDR Specifications for more information on how to properly form a CIDR Block.
	 * @param cidr the CIDR block represented by the current pool.
	 */
	public void setCidr(String cidr) {
		this.cidr = cidr;
		markDirty("cidr", cidr);
	}

	
	protected String name;
	protected String displayName;
	protected String internalId;
	protected String externalId;
	protected String dnsDomain;
	protected String hostPrefix;
	protected String httpProxy;
	protected List<String> dnsServers;
	protected List<String> dnsSuffixList;
	protected String gateway;
	protected String netmask;
	protected String dnsSearchPath;
	protected Boolean dhcpServer;
	protected String subnetAddress;
	protected Integer ipCount = 0;
	protected Integer ipFreeCount = 0;
	protected Boolean poolEnabled = false;
	protected String refType;
	protected String refId;
	protected String configuration;
	protected String cidr;
	@JsonSerialize(using= ModelAsIdOnlySerializer.class)
	public NetworkPoolServer poolServer;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account account;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public Account owner;
	@JsonSerialize(using=ModelAsIdOnlySerializer.class)
	public NetworkPoolType type;
	public String parentType;
	public String parentId;

	public List<NetworkPoolRange> ipRanges;

	public void addToIpRanges(NetworkPoolRange range) {
		if(ipRanges == null) {
			ipRanges = new ArrayList<>();
		}
		ipRanges.add(range);
		markDirty("ipRanges",ipRanges);
	}

	public void setPoolServerId(Long id) {
		this.poolServer = new NetworkPoolServer();
		this.poolServer.id = id;
		markDirty("poolServer",this.poolServer);
	}
	public void setAccountId(Long id) {
		this.account = new Account();
		this.account.id = id;
		markDirty("account",this.account);
	}

	public void setOwnerId(Long id) {
		this.owner = new Account();
		this.owner.id = id;
		markDirty("ownerId",id);
	}

	public String getDnsSearchPath() {
		return dnsSearchPath;
	}

	public void setDnsSearchPath(String dnsSearchPath) {
		this.dnsSearchPath = dnsSearchPath;
		markDirty("dnsSearchPath",id);
	}

	public Boolean getDhcpServer() {
		return dhcpServer;
	}

	public void setDhcpServer(Boolean dhcpServer) {
		this.dhcpServer = dhcpServer;
		markDirty("dhcpServer",id);
	}

	public String getSubnetAddress() {
		return subnetAddress;
	}

	public void setSubnetAddress(String subnetAddress) {
		this.subnetAddress = subnetAddress;
		markDirty("subnetAddress",id);
	}
}
