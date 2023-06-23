package com.morpheusdata.core.providers;

import com.morpheusdata.core.providers.PluginProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;
import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with networks.
 * This includes creating, editing, etc.
 *
 * @since 0.15.1
 * @author Bob Whiton, Dustin DeYoung
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
	 * Provides a Collection of Router Types that can be managed by this provider
	 * @return Collection of NetworkRouterType
	 */
	Collection<NetworkRouterType> getRouterTypes();

	/**
	 * Prepare the network information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param network Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse prepareNetwork(Network network, Map opts) {
		return ServiceResponse.success();
	}

	/**
	 * Validates the submitted network information.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param network Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse validateNetwork(Network network, Map opts) {
		return ServiceResponse.success();
	}

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
	 * Prepare the subnet information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param subnet NetworkSubnet information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse prepareSubnet(NetworkSubnet subnet, Network network, Map opts) {
		return ServiceResponse.success();
	}

	/**
	 * Validates the submitted subnet information.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param subnet NetworkSubnet information
	 * @param network Network to create the NetworkSubnet on
	 * @param opts additional configuration options. Mode value will be 'update' for validations during an update vs
	 * creation
	 * @return ServiceResponse
	 */
	default ServiceResponse validateSubnet(NetworkSubnet subnet, Network network, Map opts) {
		return ServiceResponse.success();
	}

	/**
	 * Creates the NetworkSubnet submitted
	 * @param subnet Network information
	 * @param network Network to create the NetworkSubnet on
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse createSubnet(NetworkSubnet subnet, Network network, Map opts);

	/**
	 * Updates the NetworkSubnet submitted
	 * @param subnet NetworkSubnet information
	 * @param opts additional configuration options
	 * @param network Network that this NetworkSubnet is attached to
	 * @return ServiceResponse
	 */
	ServiceResponse updateSubnet(NetworkSubnet subnet, Network network, Map opts);

	/**
	 * Deletes the NetworkSubnet submitted
	 * @param subnet NetworkSubnet information
	 * @param network Network that this NetworkSubnet is attached to
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSubnet(NetworkSubnet subnet, Network network);

	/**
	 * Validate the submitted NetworkRouter information.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param router NetworkRouter information
	 * @param opts additional configuration options. Mode value will be 'update' for validations during an update vs
	 * creation
	 * @return ServiceResponse
	 */
	default ServiceResponse validateRouter(NetworkRouter router, Map opts) { return ServiceResponse.success(); };

	/**
	 * Create the NetworkRouter submitted
	 * @param router NetworkRouter information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse createRouter(NetworkRouter router, Map opts) { return ServiceResponse.success(); };

	/**
	 * Update the NetworkRouter submitted
	 * @param router NetworkRouter information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse updateRouter(NetworkRouter router, Map opts) { return ServiceResponse.success(); };

	/**
	 * Delete the NetworkRouter submitted
	 * @param router NetworkRouter information
	 * @return ServiceResponse
	 */
	default ServiceResponse deleteRouter(NetworkRouter router) { return ServiceResponse.success(); };

	/**
	 * Validate the submitted NetworkRoute information.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @param network Network information
	 * @param networkRoute NetworkRoute information
	 * @param opts additional configuration options. Mode value will be 'update' for validations during an update vs
	 * creation
	 * @return ServiceResponse
	 */
	default ServiceResponse validateNetworkRoute(Network network, NetworkRoute networkRoute, Map opts) { return ServiceResponse.success(); };

	/**
	 * Create the NetworkRoute submitted
	 * @param network Network information
	 * @param networkRoute NetworkRoute information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse createNetworkRoute(Network network, NetworkRoute networkRoute, Map opts) { return ServiceResponse.success(); };

	/**
	 * Update the NetworkRoute submitted
	 * @param network Network information
	 * @param networkRoute NetworkRoute information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse updateNetworkRoute(Network network, NetworkRoute networkRoute, Map opts) { return ServiceResponse.success(); };

	/**
	 * Delete the NetworkRoute submitted
	 * @param networkRoute NetworkRoute information
	 * @return ServiceResponse
	 */
	default ServiceResponse deleteNetworkRoute(NetworkRoute networkRoute) { return ServiceResponse.success(); };
}
