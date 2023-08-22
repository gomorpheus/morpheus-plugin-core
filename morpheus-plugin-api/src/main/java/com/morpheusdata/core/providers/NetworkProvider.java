package com.morpheusdata.core.providers;

import com.morpheusdata.core.util.MorpheusUtils;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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
	 * Some older clouds have a network server type code that is the exact same as the cloud code. This allows one to set it
	 * to match and in doing so the provider will be fetched via the cloud providers {@link CloudProvider#getDefaultNetworkServerTypeCode()} method.
	 * @return code for overriding the ProvisionType record code property
	 */
	default String getNetworkServerTypeCode() {
		return getCode();
	}

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
	
	Collection<OptionType> getOptionTypes();

	default Collection<OptionType> getScopeOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getSwitchOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getNetworkOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getGatewayOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getRouterOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getLoadBalancerOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getRouteTableOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getSecurityGroupOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getRuleOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getFirewallGroupOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getEdgeClusterOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getDhcpServerOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getDhcpRelayOptionTypes() {
		return new ArrayList<>();
	}

	default Collection<OptionType> getGroupOptionTypes() {
		return new ArrayList<>();
	}

	default SecurityGroupProvider getSecurityGroupProvider() { return null; }

	default ServiceResponse refresh() { return ServiceResponse.success(); }

	/**
	 * Prepare the network information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param network Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<Network> prepareNetwork(Network network, Map opts) {
		return ServiceResponse.success(network);
	}

	/**
	 * Validates the submitted network information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
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
	ServiceResponse<Network> createNetwork(Network network, Map opts);

	/**
	 * Updates the Network submitted
	 * @param network Network information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	ServiceResponse<Network> updateNetwork(Network network, Map opts);

	/**
	 * Deletes the Network submitted
	 * @param network Network information
	 * @return ServiceResponse
	 */
	ServiceResponse deleteNetwork(Network network, Map opts);

	/**
	 * Prepare the subnet information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param subnet NetworkSubnet information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkSubnet> prepareSubnet(NetworkSubnet subnet, Network network, Map opts) {
		return ServiceResponse.success(subnet);
	}

	/**
	 * Validates the submitted subnet information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
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
	ServiceResponse<NetworkSubnet> createSubnet(NetworkSubnet subnet, Network network, Map opts);

	/**
	 * Updates the NetworkSubnet submitted
	 * @param subnet NetworkSubnet information
	 * @param opts additional configuration options
	 * @param network Network that this NetworkSubnet is attached to
	 * @return ServiceResponse
	 */
	ServiceResponse<NetworkSubnet> updateSubnet(NetworkSubnet subnet, Network network, Map opts);

	/**
	 * Deletes the NetworkSubnet submitted
	 * @param subnet NetworkSubnet information
	 * @param network Network that this NetworkSubnet is attached to
	 * @return ServiceResponse
	 */
	ServiceResponse deleteSubnet(NetworkSubnet subnet, Network network, Map opts);

	/**
	 * Prepare the route information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param network Network information
	 * @param networkRoute NetworkRoute to prepare
	 * @param routeConfig configuration options for the NetworkRoute
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkRoute> prepareNetworkRoute(Network network, NetworkRoute networkRoute, Map routeConfig, Map opts) { return ServiceResponse.success(networkRoute); };

	/**
	 * Validate the submitted NetworkRoute information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
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
	default ServiceResponse<NetworkRoute> createNetworkRoute(Network network, NetworkRoute networkRoute, Map opts) { return ServiceResponse.success(networkRoute); };

	/**
	 * Update the NetworkRoute submitted
	 * @param network Network information
	 * @param networkRoute NetworkRoute information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkRoute> updateNetworkRoute(Network network, NetworkRoute networkRoute, Map opts) { return ServiceResponse.success(networkRoute); };

	/**
	 * Delete the NetworkRoute submitted
	 * @param networkRoute NetworkRoute information
	 * @return ServiceResponse
	 */
	default ServiceResponse deleteNetworkRoute(NetworkRoute networkRoute, Map opts) { return ServiceResponse.success(); };


	/**
	 * Prepare the router information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param router NetworkRouter information
	 * @param routerConfig router configuration options
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkRouter> prepareRouter(NetworkRouter router, Map routerConfig, Map opts) {
		return ServiceResponse.success(router);
	}

	/**
	 * Validate the submitted NetworkRouter information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
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
	default ServiceResponse<NetworkRouter> createRouter(NetworkRouter router, Map opts) { return ServiceResponse.success(router); };

	/**
	 * Update the NetworkRouter submitted
	 * @param router NetworkRouter information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkRouter> updateRouter(NetworkRouter router, Map opts) { return ServiceResponse.success(router); };

	/**
	 * Delete the NetworkRouter submitted
	 * @param router NetworkRouter information
	 * @return ServiceResponse
	 */
	default ServiceResponse deleteRouter(NetworkRouter router, Map opts) { return ServiceResponse.success(); };


	/**
	 * Prepare the route information before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param router NetworkRouter information
	 * @param route NetworkRoute to prepare
	 * @param routeConfig configuration options for the NetworkRoute
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkRoute> prepareRouterRoute(NetworkRouter router, NetworkRoute route, Map routeConfig, Map opts) { return ServiceResponse.success(route); };

	/**
	 * Validate the submitted NetworkRoute information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
	 * bubbled up to the user.
	 * @param router NetworkRouter information
	 * @param route NetworkRoute information
	 * @param opts additional configuration options. Mode value will be 'update' for validations during an update vs
	 * creation
	 * @return ServiceResponse
	 */
	default ServiceResponse validateRouterRoute(NetworkRouter router, NetworkRoute route, Map opts) { return ServiceResponse.success(); };

	/**
	 * Create the NetworkRoute submitted
	 * @param router NetworkRouter information
	 * @param route NetworkRoute information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkRoute> createRouterRoute(NetworkRouter router, NetworkRoute route, Map opts) { return ServiceResponse.success(route); };

	/**
	 * Update the NetworkRoute submitted
	 * @param router NetworkRouter information
	 * @param route NetworkRoute information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<NetworkRoute> updateRouterRoute(NetworkRouter router, NetworkRoute route, Map opts) { return ServiceResponse.success(route); };

	/**
	 * Delete the NetworkRoute submitted
	 * @param router NetworkRouter information
	 * @param route NetworkRoute information
	 * @return ServiceResponse
	 */
	default ServiceResponse deleteRouterRoute(NetworkRouter router, NetworkRoute route, Map opts) { return ServiceResponse.success(); };


	/**
	 * Prepare the security group before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options including all form data
	 * @return ServiceResponse
	 */
	default ServiceResponse<SecurityGroup> prepareSecurityGroup(SecurityGroup securityGroup, Map opts) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.prepareSecurityGroup(securityGroup, opts);
		} else {
			return ServiceResponse.success(securityGroup);
		}
	}

	/**
	 * Validates the submitted security group information.
	 * If a {@link ServiceResponse} is not marked as successful the validation results will be
	 * bubbled up to the user.
	 * @param securityGroup SecurityGroup information
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse validateSecurityGroup(SecurityGroup securityGroup, Map opts) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.validateSecurityGroup(securityGroup, opts);
		} else {
			return ServiceResponse.success(securityGroup);
		}
	}

	/**
	 * Creates a {@link SecurityGroupLocation } from the submitted {@link SecurityGroup }
	 * @param securityGroup SecurityGroup object
	 * @param opts additional configuration options
	 * @return ServiceResponse containing the resulting {@link SecurityGroupLocation } including the information (externalId, etc.)
	 *  which identifies the security group within the current context (usually a cloud).
	 */
	default ServiceResponse<SecurityGroupLocation> createSecurityGroup(SecurityGroup securityGroup, Map opts) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.createSecurityGroup(securityGroup, opts);
		} else {
			return ServiceResponse.success(new SecurityGroupLocation());
		}
	}

	/**
	 * Update the security group
	 * @param securityGroup SecurityGroup object
	 * @param opts additional configuration options
	 * @return ServiceResponse
	 */
	default ServiceResponse<SecurityGroup> updateSecurityGroup(SecurityGroup securityGroup, Map opts) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.updateSecurityGroup(securityGroup, opts);
		} else {
			return ServiceResponse.success(securityGroup);
		}
	}

	/**
	 * Delete a {@link SecurityGroupLocation}
	 * @param securityGroupLocation SecurityGroupLocation information
	 * @return ServiceResponse
	 */
	default ServiceResponse deleteSecurityGroupLocation(SecurityGroupLocation securityGroupLocation) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.deleteSecurityGroupLocation(securityGroupLocation);
		} else {
			return ServiceResponse.success();
		}
	}


	/**
	 * Prepare the security group rule before validate, create, and update.
	 * If a {@link ServiceResponse} is not marked as successful the parent process will be terminated
	 * and the results may be presented to the user.
	 * @param securityGroupRule SecurityGroupRule object
	 * @param opts additional configuration options including all form data
	 * @return ServiceResponse
	 */
	default ServiceResponse<SecurityGroupRule> prepareSecurityGroupRule(SecurityGroupRule securityGroupRule, Map opts) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.prepareSecurityGroupRule(securityGroupRule, opts);
		} else {
			return ServiceResponse.success(securityGroupRule);
		}
	}

	/**
	 * Validate the submitted security group rule object.
	 * If a {@link ServiceResponse} is not marked as successful the validation results in the <i>errors</i> and <i>msg</i> properties will be
	 * surfaced to the user interface.
	 * @param securityGroupRule SecurityGroupRule object
	 * @return ServiceResponse
	 */
	default ServiceResponse<SecurityGroupRule> validateSecurityGroupRule(SecurityGroupRule securityGroupRule) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.validateSecurityGroupRule(securityGroupRule);
		} else {
			return ServiceResponse.success(securityGroupRule);
		}
	}

	/**
	 * Creates a {@link SecurityGroupRuleLocation } from the submitted {@link SecurityGroupRule }
	 * @param securityGroupRule SecurityGroupRule object
	 * @return ServiceResponse containing the resulting {@link SecurityGroupRuleLocation } including the information (externalId, etc.)
	 *  which identifies the security group rule within the current context (usually a cloud).
	 */
	default ServiceResponse<SecurityGroupRuleLocation> createSecurityGroupRule(SecurityGroupLocation securityGroupLocation, SecurityGroupRule securityGroupRule) {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.createSecurityGroupRule(securityGroupLocation, securityGroupRule);
		} else {
			return ServiceResponse.success(new SecurityGroupRuleLocation());
		}
	}

	/**
	 * Update the security group rule
	 * @param securityGroupLocation the {@link SecurityGroupLocation }
	 * @param originalRule the rule before any updates were applied.
	 * @param updatedRule the rule with all updates applied
	 * @return {@link ServiceResponse }
	 */
	default ServiceResponse<SecurityGroupRule> updateSecurityGroupRule(SecurityGroupLocation securityGroupLocation, SecurityGroupRule originalRule, SecurityGroupRule updatedRule)  {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.updateSecurityGroupRule(securityGroupLocation, originalRule, updatedRule);
		} else {
			return ServiceResponse.success(updatedRule);
		}
	}

	/**
	 * Delete a {@link SecurityGroupRule}
	 * @param securityGroupLocation SecurityGroupLocation object
	 * @param rule SecurityGroupRule to be deleted
	 * @return ServiceResponse
	 */
	default ServiceResponse deleteSecurityGroupRule(SecurityGroupLocation securityGroupLocation, SecurityGroupRule rule)  {
		SecurityGroupProvider provider = getSecurityGroupProvider();
		if(provider != null) {
			return provider.deleteSecurityGroupRule(securityGroupLocation, rule);
		} else {
			return ServiceResponse.success();
		}
	}
}
