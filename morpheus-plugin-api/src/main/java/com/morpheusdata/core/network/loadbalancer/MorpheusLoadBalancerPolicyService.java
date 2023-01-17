package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.NetworkLoadBalancer;
import com.morpheusdata.model.NetworkLoadBalancerPolicy;
import com.morpheusdata.model.projection.LoadBalancerPolicyIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusLoadBalancerPolicyService {
	/**
	 * Get a list of {@link LoadBalancerPolicyIdentityProjection} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<LoadBalancerPolicyIdentityProjection> listSyncProjections(Long loadBalancerId, String objCategory);

	/**
	 * Get a list of {@link NetworkLoadBalancerPolicy} objects from a list of projection ids
	 *
	 * @param ids list of policy ids
	 * @return Observable stream of policies
	 */
	Observable<NetworkLoadBalancerPolicy> listById(Collection<Long> ids);

	/**
	 * Save updates to existing policy
	 *
	 * @param policies updated policy
	 * @return success
	 */
	Single<Boolean> save(List<NetworkLoadBalancerPolicy> policies);

	/**
	 * Create new policies in Morpheus
	 *
	 * @param policies new NetworkLoadBalancerPolicy to persist
	 * @return success
	 */
	Single<Boolean> create(List<NetworkLoadBalancerPolicy> policies);

	/**
	 * Remove persisted policies from Morpheus
	 *
	 * @param policies Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<LoadBalancerPolicyIdentityProjection> policies);

	/**
	 * Retrive a load balancer policy based on externalId and load balancer (used in sync)
	 * @param externalId
	 * @param loadBalancer
	 * @return
	 */
	Single<NetworkLoadBalancerPolicy> findByExternalIdAndLoadBalancer(String externalId, NetworkLoadBalancer loadBalancer);

	/**
	 * Use this method to query the database for load balancer policies
	 * @param args a map of properties used to query the policy
	 * @return Observable stream of {@link NetworkLoadBalancerPolicy}
	 */
	Observable<NetworkLoadBalancerPolicy> queryPolicies(Map args);
}
