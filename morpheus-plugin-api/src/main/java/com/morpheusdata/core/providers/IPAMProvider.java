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

package com.morpheusdata.core.providers;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Provides IP Address Management integration support for third party IPAM Vendors. An example might be Infoblox or Bluecat
 * These types of providers often provides DNS Support as well and in that event it is possible for a Provider to implement
 * both interfaces
 *
 * @see DNSProvider
 * @since 0.15.1
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

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getIntegrationOptionTypes();

	/**
	 * Returns the IPAM Integration logo for display when a user needs to view or add this integration
	 * @since 0.12.3
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();
	
}
