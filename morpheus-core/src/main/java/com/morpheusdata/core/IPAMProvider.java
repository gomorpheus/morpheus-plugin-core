package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import java.util.Map;
import java.util.Set;

/**
 * Provides IP Address Management integration support for third party IPAM Vendors. An example might be Infoblox or Bluecat
 * These types of providers often provides DNS Support as well and in that event it is possible for a Provider to implement
 * both interfaces
 *
 * @see DNSProvider
 *
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
	 * @return A response is returned depending on if the inputs are valid or not.
	 */
	ServiceResponse validate(NetworkPoolServer poolServer);

	/**
	 * Periodically called to refresh and sync data coming from the relevant integration. Most integration providers
	 * provide a method like this that is called periodically (typically 5 - 10 minutes). DNS Sync operates on a 10min
	 * cycle by default. Useful for caching Host Records created outside of Morpheus.
	 * @param poolServer The Integration Object contains all the saved information regarding configuration of the IPAM Provider.
	 */
	void refresh(NetworkPoolServer poolServer);


	/*
	 * Returns a list of provided pool types that are available for use. These are synchronized by the IPAM Provider via a Context.
	 * @return A Set of {@link NetworkPoolType} objects representing the available pool types provided by this Provider.
	 */
	Set<NetworkPoolType> getProvidedPoolTypes();

	/*
	 * Target endpoint used to allocate an IP Address during provisioning of Instances
	 */
	ServiceResponse leasePoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, String assignedType, Long assignedId, Long subAssignedId, String assignedHostname, Map opts);

	/*
	 * Called during provisioning to setup a DHCP Lease address by mac address. This can be used in some scenarios in the event the environment supports DHCP Reservations instead of strictly static
	 */
	ServiceResponse reservePoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, String assignedType, Long assignedId, Long subAssignedId, String assignedHostname, Map opts);

	/*
	 * Called during instance teardown to release an IP Address reservation.
	 */
	ServiceResponse returnPoolAddress(NetworkPoolServer networkPoolServer, NetworkPool networkPool, Network network, NetworkPoolIp ipAddress, Map opts);


	ServiceResponse createHostRecord(NetworkPoolServer poolServer, NetworkPool  networkPool, NetworkPoolIp networkPoolIp);
	ServiceResponse createHostRecord(NetworkPoolServer poolServer, NetworkPool  networkPool, NetworkPoolIp networkPoolIp, NetworkDomain domain, Boolean createARecord, Boolean createPtrRecord);
	ServiceResponse updateHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp networkPoolIp);
	ServiceResponse deleteHostRecord(NetworkPoolServer poolServer, NetworkPool networkPool, NetworkPoolIp poolIp, Boolean deleteAssociatedRecords);
	ServiceResponse provisionWorkload(NetworkPoolServer poolServer, Workload workload, Map opts);
	ServiceResponse provisionServer(NetworkPoolServer poolServer, ComputeServer server, Map opts);
	ServiceResponse removeServer(NetworkPoolServer poolServer, ComputeServer server, Map opts);
	ServiceResponse removeContainer(NetworkPoolServer poolServer, Workload workload, Map opts);
}
