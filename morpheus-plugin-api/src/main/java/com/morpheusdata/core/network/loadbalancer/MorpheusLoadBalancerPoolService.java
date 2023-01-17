package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.NetworkLoadBalancer;
import com.morpheusdata.model.NetworkLoadBalancerPool;
import com.morpheusdata.model.projection.LoadBalancerPoolIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusLoadBalancerPoolService {
	/**
	 * Get a list of {@link NetworkLoadBalancerPool} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<LoadBalancerPoolIdentityProjection> listSyncProjections(Long loadBalancerId);

	/**
	 * Get a list of {@link NetworkLoadBalancerPool} objects from a list of projection ids
	 *
	 * @param ids pool ids
	 * @return Observable stream of pools
	 */
	Observable<NetworkLoadBalancerPool> listById(Collection<Long> ids);

	/**
	 * Save updates to existing pool
	 *
	 * @param pools updated pools
	 * @return success
	 */
	Single<Boolean> save(List<NetworkLoadBalancerPool> pools);

	/**
	 * Create new load balancer pool in Morpheus
	 *
	 * @param pools new {@link NetworkLoadBalancerPool}  to persist
	 * @return success
	 */
	Single<Boolean> create(List<NetworkLoadBalancerPool> pools);

	/**
	 * Remove persisted load balancer pools from Morpheus
	 *
	 * @param pools Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<LoadBalancerPoolIdentityProjection> pools);

	/**
	 * Retrieves a single load balancer pool from the database given a load balancer and external id
	 * @param loadBalancer
	 * @param externalId
	 * @return
	 */
	Single<NetworkLoadBalancerPool> findByLoadBalancerAndExternalId(NetworkLoadBalancer loadBalancer, String externalId);
}
