package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.RequestResponse;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with networks.
 * This includes creating, editing, etc.
 *
 * @since 0.11.0
 * @author Bob Whiton
 */
public interface NetworkProvider extends PluginProvider {

	/**
	 * The CloudProvider code that this NetworkProvider should be attached to.
	 * When this NetworkProvider is registered with Morpheus, all Clouds that match this code will have a
	 * NetworkServer of this type attached to them. Network actions will then be handled via this provider.
	 * @return String Code of the Cloud type
	 */
	String getCloudProviderCode();

	/**
	 * Grabs the description for the NetworkProvider
	 * @return String
	 */
	String getDescription();

	/**
	 * Provides a Collection of NetworkTypes that can be managed by this provider
	 * @return Collection of NetworkType
	 */
	Collection<NetworkType> getNetworkTypes();

	/**
	 * Validates the submitted network information.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param network Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse validateNetwork(Network network, Map opts);

	/**
	 * Creates the Network submitted
	 * @param network Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse createNetwork(Network network, Map opts);

	/**
	 * Updates the Network submitted
	 * @param network Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse updateNetwork(Network network, Map opts);

	/**
	 * Deletes the Network submitted
	 * @param network Network information
	 * @return ServiceResponse
	 */
	ServiceResponse deleteNetwork(Network network);

	/**
	 * Validates the submitted subnet information.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param subnet NetworkSubnet information
	 * @param opts additional configuration options. Mode value will be 'update' for validations during an update vs
	 * creation
	 * @return ServiceResponse
	 */
	ServiceResponse validateSubnet(NetworkSubnet subnet, Map opts);

	/**
	 * Creates the NetworkSubnet submitted
	 * @param subnet Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse createSubnet(NetworkSubnet subnet, Map opts);

	/**
	 * Updates the NetworkSubnet submitted
	 * @param subnet NetworkSubnet information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse updateSubnet(NetworkSubnet subnet, Map opts);

	/**
	 * Deletes the NetworkSubnet submitted
	 * @param subnet NetworkSubnet information
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSubnet(NetworkSubnet subnet);
}
