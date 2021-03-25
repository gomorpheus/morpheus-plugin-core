package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Provides IP Address Management integration support for third party IPAM Vendors. An example might be Infoblox or Bluecat
 * These types of providers often provides DNS Support as well and in that event it is possible for a Provider to implement
 * both interfaces
 *
 * @see DNSProvider
 * @since 0.8.0
 * @author David Estes
 */
public interface IPAMProvider extends PluginProvider {

	/**
	 * Validation Method used to validate all inputs applied to the integration of an IPAM Provider upon save.
	 * If an input fails validation or authentication information cannot be verified, Error messages should be returned
	 * via a {@link ServiceResponse} object where the key on the error is the field name and the value is the error message.
	 * If the error is a generic authentication error or unknown error, a standard message can also be sent back in the response.
	 *
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the inputs are valid or not.
	 */
	public ServiceResponse verifyNetworkPoolServer(NetworkPoolServer poolServer, Map opts);

	/**
	 * Called during creation of a {@link NetworkPoolServer} operation. This allows for any custom operations that need
	 * to be performed outside of the standard operations.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the operation was a success or not.
	 */
	public ServiceResponse createNetworkPoolServer(NetworkPoolServer poolServer, Map opts);

	/**
	 * Called during update of an existing {@link NetworkPoolServer}. This allows for any custom operations that need
	 * to be performed outside of the standard operations.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the operation was a success or not.
	 */
	public ServiceResponse updateNetworkPoolServer(NetworkPoolServer poolServer, Map opts);

	/**
	 * Periodically called to refresh and sync data coming from the relevant integration. Most integration providers
	 * provide a method like this that is called periodically (typically 5 - 10 minutes). DNS Sync operates on a 10min
	 * cycle by default. Useful for caching Host Records created outside of Morpheus.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 */
	void refresh(NetworkPoolServer poolServer);

	/*
	 * Target endpoint used to allocate an IP Address during provisioning of Instances
	 * @param networkPoolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param networkPool the NetworkPool currently being operated on that contains the ip address for reservation
	 * @param network The Network reference object on the cloud that the ip address is reserved for
	 * @param assignedType the reference object type this ip address is being assigned to. Typically this relates to a server or container, in the future it could expand to a VIP
	 * @param assignedId the reference object id based on the object type the ip address is being assigned to
	 * @param subAssignedId the sub object attached to the reference object this ip address is being reserved for. Typically this is the network interface id on the server
	 * @param assignedHostname the hostname of the server/interface the ip is being allocated for. Typically this would be assigned on the host record and also used to create a PTR or A record automatically
	 * @param opts list of additional options that can be passed for reservation. for example, if a user wants a specific ip address. this exists as opts.ipAddress
	 */
	ServiceResponse leasePoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, String assignedType, Long assignedId, Long subAssignedId, String assignedHostname, Map opts);

	/*
	 * Called during provisioning to setup a DHCP Lease address by mac address. This can be used in some scenarios in the event the environment supports DHCP Reservations instead of strictly static
	 * @param networkPoolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param networkPool the NetworkPool currently being operated on that contains the ip address for reservation
	 * @param network The Network reference object on the cloud that the ip address is reserved for
	 * @param assignedType the reference object type this ip address is being assigned to. Typically this relates to a server or container, in the future it could expand to a VIP
	 * @param assignedId the reference object id based on the object type the ip address is being assigned to
	 * @param subAssignedId the sub object attached to the reference object this ip address is being reserved for. Typically this is the network interface id on the server
	 * @param assignedHostname the hostname of the server/interface the ip is being allocated for. Typically this would be assigned on the host record and also used to create a PTR or A record automatically
	 * @param opts list of additional options that can be passed for reservation. for example, if a user wants a specific ip address. this exists as opts.ipAddress
	 */
	ServiceResponse reservePoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, String assignedType, Long assignedId, Long subAssignedId, String assignedHostname, Map opts);

	/*
	 * Called during instance teardown to release an IP Address reservation.
	 * @param networkPoolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param networkPool the NetworkPool currently being operated on that contains the ip address for release
	 * @param network The Network reference object on the cloud that the ip address is being released from
	 * @param ipAddress the IP Addresse entry record that needs to be released
	 * @param opts additional options that can sometimes be passed. TODO: Fill out available option keys
	 */
	ServiceResponse returnPoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, NetworkPoolIp ipAddress, Map opts);


	/**
	 * Called on the first save / update of a pool server integration. Used to do any initialization of a new integration
	 * Often times this calls the periodic refresh method directly.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param opts an optional map of parameters that could be sent. This may not currently be used and can be assumed blank
	 * @return a ServiceResponse containing the success state of the initialization phase
	 */
	ServiceResponse initializeNetworkPoolServer(NetworkPoolServer poolServer, Map opts);

	/**
	 * Creates a Host record on the target {@link NetworkPool} within the {@link NetworkPoolServer} integration.
	 * Typically called outside of automation and is used for administration purposes.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param networkPool the NetworkPool currently being operated on.
	 * @param networkPoolIp The ip address and metadata related to it for allocation. It is important to create functionality such that
	 *                      if the ip address property is blank on this record, auto allocation should be performed and this object along with the new
	 *                      ip address be returned in the {@link ServiceResponse}
	 * @param domain The domain with which we optionally want to create an A/PTR record for during this creation process.
	 * @param createARecord configures whether or not the A record is automatically created
	 * @param createPtrRecord configures whether or not the PTR record is automatically created
	 * @return a ServiceResponse containing the success state of the create host record operation
	 */
	ServiceResponse createHostRecord(NetworkPoolServer poolServer, NetworkPool  networkPool, NetworkPoolIp networkPoolIp, NetworkDomain domain, Boolean createARecord, Boolean createPtrRecord); // createHostRecord

	/**
	 * Updates a Host record on the target {@link NetworkPool} if supported by the Provider. If not supported, send the appropriate
	 * {@link ServiceResponse} such that the user is properly informed of the unavailable operation.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 * @param networkPool the NetworkPool currently being operated on.
	 * @param networkPoolIp the changes to the network pool ip address that would like to be made. Most often this is just the host record name.
	 * @return a ServiceResponse containing the success state of the update host record operation
	 */
	ServiceResponse updateHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp);

	/**
	 * Deletes a host record on the target {@link NetworkPool}. This is used for cleanup or releasing of an ip address on
	 * the IPAM Provider.
	 * @param networkPool the NetworkPool currently being operated on.
	 * @param poolIp the record that is being deleted.
	 * @param deleteAssociatedRecords determines if associated records like A/PTR records
	 * @return a ServiceResponse containing the success state of the delete operation
	 */
	ServiceResponse deleteHostRecord(NetworkPool networkPool, NetworkPoolIp poolIp, Boolean deleteAssociatedRecords);

	/**
	 * An IPAM Provider can register pool types for display and capability information when syncing IPAM Pools
	 * @return a List of {@link NetworkPoolType} to be loaded into the Morpheus database.
	 */
	Collection<NetworkPoolType> getNetworkPoolTypes();
	
}
