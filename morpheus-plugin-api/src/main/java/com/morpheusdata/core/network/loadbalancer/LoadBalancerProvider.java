package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.core.providers.PluginProvider;
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
	 * Grabs the description for the LoadBalancerProvider
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
	 * @param loadBalancer {@link NetworkLoadBalancer}
	 * @param opts a map of additional optional properties
	 * @return ServiceResponse.  If ServiceResponse.success == false, ServiceResponse.errors will contain reasons.
	 */
	ServiceResponse validate(NetworkLoadBalancer loadBalancer, Map opts);
	default ServiceResponse addLoadBalancer(NetworkLoadBalancer loadBalancer) { return ServiceResponse.success(); }
	default ServiceResponse deleteLoadBalancer(NetworkLoadBalancer loadBalancer) { return ServiceResponse.success(); }
	default ServiceResponse updateLoadBalancer(NetworkLoadBalancer loadBalancer) { return ServiceResponse.success(); }
	default ServiceResponse setAdditionalConfiguration(NetworkLoadBalancer loadBalancer, Map opts) { return ServiceResponse.success(); }

	default ServiceResponse initializeLoadBalancer(NetworkLoadBalancer loadBalancer, Map opts) {
		return ServiceResponse.success();
	}

	default ServiceResponse refresh(NetworkLoadBalancer loadBalancer) {
		// default implementation for this is to just return success as not all load balancer syncing is done from the
		// load balancer provider.  In the case of amazon, the load balancer sync is down in the cloud provider.
		return ServiceResponse.success();
	}

	// service methods for interacting with load balancer apis

	/**
	 * Create operation for load balancer providers.  Implement this method to create a profile
	 * within the provider using the {@link NetworkLoadBalancerProfile} model.
	 * @param profile {@link NetworkLoadBalancerProfile}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse createLoadBalancerProfile(NetworkLoadBalancerProfile profile) {
		return ServiceResponse.success();
	}

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a profile
	 * within the provider using the {@link NetworkLoadBalancerProfile} model.
	 * @param profile {@link NetworkLoadBalancerProfile}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse deleteLoadBalancerProfile(NetworkLoadBalancerProfile profile) {
		return ServiceResponse.success();
	}

	/**
	 * Update operation for load balancer providers.  Implement this method to update a profile
	 * within the provider using the {@link NetworkLoadBalancerProfile} model.
	 * @param profile {@link NetworkLoadBalancerProfile}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse updateLoadBalancerProfile(NetworkLoadBalancerProfile profile) {
		return ServiceResponse.success();
	}

	/**
	 * Create operation for load balancer providers.  Implement this method to create a health monitor
	 * within the provider using the {@link NetworkLoadBalancerMonitor} model
	 * @param monitor {@link NetworkLoadBalancerMonitor}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse createLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		return ServiceResponse.success();
	}

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a health monitor
	 * within the provider using the {@link NetworkLoadBalancerMonitor} model
	 * @param monitor {@link NetworkLoadBalancerMonitor}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse deleteLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		return ServiceResponse.success();
	}

	/**
	 * Update operation for load balancer providers.  Implement this method to update a health monitor
	 * within the provider using the {@link NetworkLoadBalancerMonitor} model
	 * @param monitor {@link NetworkLoadBalancerMonitor}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse updateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform validation for the create/save of a load balancer health monitor
	 * @param monitor {@link NetworkLoadBalancerMonitor}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse validateLoadBalancerHealthMonitor(NetworkLoadBalancerMonitor monitor) {
		return ServiceResponse.success();
	}

	/**
	 * Create operation for load balancer providers.  Implement this method to create a policy
	 * within the provider using the {@link NetworkLoadBalancerPolicy} model
	 * @param loadBalancerPolicy {@link NetworkLoadBalancerPolicy}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse createLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy) {
		return ServiceResponse.success();
	}

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a policy
	 * within the provider using the {@link NetworkLoadBalancerPolicy} model
	 * @param loadBalancerPolicy {@link NetworkLoadBalancerPolicy}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse deleteLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform validation for the create/save of a load balancer policy
	 * @param loadBalancerPolicy {@link NetworkLoadBalancerPolicy}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse validateLoadBalancerPolicy(NetworkLoadBalancerPolicy loadBalancerPolicy) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform validation for the create/save of a load balancer policy rule
	 * @param rule {@link NetworkLoadBalancerRule}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse validateLoadBalancerRule(NetworkLoadBalancerRule rule) {
		return ServiceResponse.success();
	}

	/**
	 * Create operation for load balancer providers.  Implement this method to create a policy rule
	 * within the provider using the {@link NetworkLoadBalancerRule} model
	 * @param rule {@link NetworkLoadBalancerRule}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse createLoadBalancerRule(NetworkLoadBalancerRule rule) {
		return ServiceResponse.success();
	}

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a policy rule
	 * within the provider using the {@link NetworkLoadBalancerRule} model
	 * @param rule {@link NetworkLoadBalancerRule}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse deleteLoadBalancerRule(NetworkLoadBalancerRule rule) {
		return ServiceResponse.success();
	}

	/**
	 * Create operation for load balancer providers.  Implement this method to create a node
	 * within the provider using the {@link NetworkLoadBalancerNode} model
	 * @param node {@link NetworkLoadBalancerNode}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse createLoadBalancerNode(NetworkLoadBalancerNode node) {
		return ServiceResponse.success();
	}

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a node
	 * within the provider using the {@link NetworkLoadBalancerNode} model
	 * @param node {@link NetworkLoadBalancerNode}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse deleteLoadBalancerNode(NetworkLoadBalancerNode node) {
		return ServiceResponse.success();
	}

	/**
	 * Update operation for load balancer providers.  Implement this method to update a node
	 * within the provider using the {@link NetworkLoadBalancerNode} model
	 * @param node {@link NetworkLoadBalancerNode}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse updateLoadBalancerNode(NetworkLoadBalancerNode node) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform validation for the create/save of a load balancer node
	 * @param node {@link NetworkLoadBalancerNode}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse validateLoadBalancerNode(NetworkLoadBalancerNode node) {
		return ServiceResponse.success();
	}

	/**
	 * Create operation for load balancer providers.  Implement this method to create a pool
	 * within the provider using the {@link NetworkLoadBalancerPool} model
	 * @param pool {@link NetworkLoadBalancerPool}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse createLoadBalancerPool(NetworkLoadBalancerPool pool) {
		return ServiceResponse.success();
	}

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a pool
	 * within the provider using the {@link NetworkLoadBalancerPool} model
	 * @param pool {@link NetworkLoadBalancerPool}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse deleteLoadBalancerPool(NetworkLoadBalancerPool pool) {
		return ServiceResponse.success();
	}

	/**
	 * Update operation for load balancer providers.  Implement this method to update a pool
	 * within the provider using the {@link NetworkLoadBalancerPool} model
	 * @param pool {@link NetworkLoadBalancerPool}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse updateLoadBalancerPool(NetworkLoadBalancerPool pool) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform validation for the create/save of a load balancer pool
	 * @param pool {@link NetworkLoadBalancerPool}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse validateLoadBalancerPool(NetworkLoadBalancerPool pool) {
		return ServiceResponse.success();
	}

	/**
	 * Create operation for load balancer providers.  Implement this method to create a virtual server
	 * within the provider using the {@link NetworkLoadBalancerInstance} model
	 * @param instance {@link NetworkLoadBalancerInstance}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse createLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		return ServiceResponse.success();
	}

	/**
	 * Delete operation for load balancer providers.  Implement this method to delete a virtual server
	 * within the provider using the {@link NetworkLoadBalancerInstance} model
	 * @param instance {@link NetworkLoadBalancerInstance}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse deleteLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		return ServiceResponse.success();
	}

	/**
	 * Update operation for load balancer providers.  Implement this method to update a virtual server
	 * within the provider using the {@link NetworkLoadBalancerInstance} model
	 * @param instance {@link NetworkLoadBalancerInstance}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse updateLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		return ServiceResponse.success();
	}

	/**
	 * Implement this method to perform validation for the create/save of a load balancer virtual server (vip)
	 * @param instance {@link NetworkLoadBalancerInstance}
	 * @return {@link ServiceResponse}
	 */
	default ServiceResponse validateLoadBalancerVirtualServer(NetworkLoadBalancerInstance instance) {
		return ServiceResponse.success();
	}

	default ServiceResponse validateLoadBalancerInstance(NetworkLoadBalancerInstance loadBalancerInstance) { return ServiceResponse.success(); }
	default ServiceResponse validateLoadBalancerInstanceConfiguration(NetworkLoadBalancer loadBalancer, Instance instance) { return ServiceResponse.success(); }

	/**
	 * Implement this method to handle morpheus setting up a load balancer pool from a morpheus instance.  This operation
	 * should handle every operation necessary to setup a load balanced pool for one or more vms/containers within a morpheus
	 * instance
	 * @param instance {@link NetworkLoadBalancerInstance}
	 * @return {@link ServiceResponse}
	 */
	ServiceResponse addInstance(NetworkLoadBalancerInstance instance);

	/**
	 * Implement this method to remove all entities involved in a {@link NetworkLoadBalancerInstance} such ass policies, pools,
	 * profiles, nodes, virtual servers, etc.  This method should clean said entities from the load balancer integration itself
	 * @param instance {@link NetworkLoadBalancerInstance}
	 * @return {@link ServiceResponse}
	 */
	ServiceResponse removeInstance(NetworkLoadBalancerInstance instance);

	/**
	 * Implement this method to update all entities involved in a {@link NetworkLoadBalancerInstance} such as policies, pools,
	 * profiles, nodes, virtual servers, etc.  This method is usually called when a node is added/removed from an instance
	 * @param instance {@link NetworkLoadBalancerInstance}
	 * @return {@link ServiceResponse}
	 */
	ServiceResponse updateInstance(NetworkLoadBalancerInstance instance);
}
