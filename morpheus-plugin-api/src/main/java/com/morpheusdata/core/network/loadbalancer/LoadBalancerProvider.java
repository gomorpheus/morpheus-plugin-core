package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.core.PluginProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.Collection;
import java.util.Map;

/**
 * Provides a standard set of methods for interacting with load balancer integrations.  This includes syncing entities from
 * the providers and crud operations on load balancer entities.
 *
 * @author jsaardchit
 */
public interface LoadBalancerProvider extends PluginProvider {
	/**
	 * Grabs the description for the CloudProvider
	 * @return String
	 */
	String getDescription();

	/**
	 * Returns the load balancer logo for display when a user needs to view or add this load balancer. SVGs are preferred.
	 * @since 0.13.0
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Provides a Collection of OptionType inputs that define the required input fields for defining a load balancer integration
	 * @return Collection of OptionType
	 */
	Collection<OptionType> getOptionTypes();

	/**
	 * Provides a collection of network load balancer types that need to be configured in the morpheus environment along
	 * with the various child entities required by the load balancer
	 * @return Collection of NetworkLoadBalancerType
	 */
	Collection<NetworkLoadBalancerType> getLoadBalancerTypes();

	/**
	 * Validates the user submitted load balancer connection information to ensure the appliance can communicate with it.
	 * If a {@link ServiceResponse} is not marked as successful then the validation results will be
	 * bubbled up to the user.
	 * @return ServiceResponse.  If ServiceResponse.success == false, ServiceResponse.errors will contain reasons.
	 */
	ServiceResponse validate(NetworkLoadBalancer loadBalancer, Map opts);

	ServiceResponse initializeLoadBalancer(NetworkLoadBalancer loadBalancer, Map opts);
	ServiceResponse refresh(NetworkLoadBalancer loadBalancer);

	// service methods for interacting with load balancer apis
	ServiceResponse createLoadBalancerProfile(NetworkLoadBalancerProfile profile);
	ServiceResponse deleteLoadBalancerProfile(NetworkLoadBalancerProfile profile);
	ServiceResponse updateLoadBalancerProfile(NetworkLoadBalancerProfile profile);
	ServiceResponse createLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor);
	ServiceResponse deleteLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor);
	ServiceResponse updateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor);
}
