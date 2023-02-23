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

	/**
	 * Create operation for load balancer providers.  Implement this method to create a profile
	 * within the provider using the {@link NetworkLoadBalancerProfile} model.
	 * @param profile
	 * @return
	 */

	ServiceResponse createLoadBalancerProfile(NetworkLoadBalancerProfile profile);
	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a profile
	 * within the provider using the {@link NetworkLoadBalancerProfile} model.
	 * @param profile
	 * @return
	 */

	ServiceResponse deleteLoadBalancerProfile(NetworkLoadBalancerProfile profile);
	/**
	 * Update operation for load balancer providers.  Implement this method to update a profile
	 * within the provider using the {@link NetworkLoadBalancerProfile} model.
	 * @param profile
	 * @return
	 */
	ServiceResponse updateLoadBalancerProfile(NetworkLoadBalancerProfile profile);

	/**
	 * Create operation for load balancer providers.  Implement this method to create a health monitor
	 * within the provider using the {@link NetworkLoadBalancerMonitor} model
	 * @param monitor
	 * @return
	 */
	ServiceResponse createLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor);

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a health monitor
	 * within the provider using the {@link NetworkLoadBalancerMonitor} model
	 * @param monitor
	 * @return
	 */
	ServiceResponse deleteLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor);

	/**
	 * Update operation for load balancer providers.  Implement this method to update a health monitor
	 * within the provider using the {@link NetworkLoadBalancerMonitor} model
	 * @param monitor
	 * @return
	 */
	ServiceResponse updateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor);
	ServiceResponse validateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor);

	ServiceResponse createLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy);
	ServiceResponse deleteLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy);
	ServiceResponse validateLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy);

	ServiceResponse validateLoadBalancerRule(NetworkLoadBalancerRule rule);
	ServiceResponse createLoadBalancerRule(NetworkLoadBalancerRule rule);
	ServiceResponse deleteLoadBalancerRule(NetworkLoadBalancerRule rule);

	/**
	 * Create operation for load balancer providers.  Implement this method to create a node
	 * within the provider using the {@link NetworkLoadBalancerNode} model
	 * @param node
	 * @return
	 */
	ServiceResponse createLoadBalancerNode(NetworkLoadBalancerNode node);

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a node
	 * within the provider using the {@link NetworkLoadBalancerNode} model
	 * @param node
	 * @return
	 */
	ServiceResponse deleteLoadBalancerNode(NetworkLoadBalancerNode node);

	/**
	 * Update operation for load balancer providers.  Implement this method to update a node
	 * within the provider using the {@link NetworkLoadBalancerNode} model
	 * @param node
	 * @return
	 */
	ServiceResponse updateLoadBalancerNode(NetworkLoadBalancerNode node);
	ServiceResponse validateLoadBalancerNode(NetworkLoadBalancerNode node);

	/**
	 * Create operation for load balancer providers.  Implement this method to create a pool
	 * within the provider using the {@link NetworkLoadBalancerPool} model
	 * @param pool
	 * @return
	 */
	ServiceResponse createLoadBalancerPool(NetworkLoadBalancerPool pool);

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a pool
	 * within the provider using the {@link NetworkLoadBalancerPool} model
	 * @param pool
	 * @return
	 */
	ServiceResponse deleteLoadBalancerPool(NetworkLoadBalancerPool pool);

	/**
	 * Update operation for load balancer providers.  Implement this method to update a pool
	 * within the provider using the {@link NetworkLoadBalancerPool} model
	 * @param pool
	 * @return
	 */
	ServiceResponse updateLoadBalancerPool(NetworkLoadBalancerPool pool);
	ServiceResponse validateLoadBalancerPool(NetworkLoadBalancerPool pool);

	/**
	 * Create operation for load balancer providers.  Implement this method to create a virtual server
	 * within the provider using the {@link NetworkLoadBalancerInstance} model
	 * @param instance
	 * @return
	 */
	ServiceResponse createLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance);

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a virtual server
	 * within the provider using the {@link NetworkLoadBalancerInstance} model
	 * @param instance
	 * @return
	 */
	ServiceResponse deleteLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance);

	/**
	 * Update operation for load balancer providers.  Implement this method to update a virtual server
	 * within the provider using the {@link NetworkLoadBalancerInstance} model
	 * @param instance
	 * @return
	 */
	ServiceResponse updateLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance);
	ServiceResponse validateLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance);

	/**
	 * Implement this method to handle morpheus setting up a load balancer pool from a morpheus instance.  This operation
	 * should handle every operation necessary to setup a load balanced pool for one or more vms/containers within a morpheus
	 * instance
	 * @param instance
	 * @param opts
	 * @return
	 */
	ServiceResponse addInstance(NetworkLoadBalancerInstance instance, Map opts);
}
