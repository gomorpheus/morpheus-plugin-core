package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.RequestResponse;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * Provides a standard set of methods for interacting with networks.
 * This includes syncing networks, creating, editing, etc.
 * TODO : Still a Work In Progress and not yet supported
 *
 * @since 0.9.0
 * @author Bob Whiton
 */
public interface NetworkProvider extends PluginProvider {

	/**
	 * Grabs the description for the NetworkProvider
	 * @return String
	 */
	String getDescription();

	/**
	 * Provides a Collection of OptionType inputs that define the required input fields for defining a network
	 * @param networkType NetworkType for which the OptionType should be returned
	 * @return Collection of OptionType
	 */
	Collection<OptionType> getNetworkTypeOptionTypes(NetworkType networkType);

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
}
