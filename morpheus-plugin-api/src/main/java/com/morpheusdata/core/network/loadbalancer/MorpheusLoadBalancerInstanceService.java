package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.NetworkLoadBalancerInstance;
import com.morpheusdata.model.projection.LoadBalancerInstanceIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

public interface MorpheusLoadBalancerInstanceService extends MorpheusDataService<NetworkLoadBalancerInstance, LoadBalancerInstanceIdentityProjection>, MorpheusIdentityService<LoadBalancerInstanceIdentityProjection> {
	/**
	 * Get a list of {@link NetworkLoadBalancerInstance} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<LoadBalancerInstanceIdentityProjection> listSyncProjections(Long loadBalancerId);

	/**
	 * Get a list of {@link NetworkLoadBalancerInstance} objects from a list of projection ids
	 *
	 * @param ids instance ids
	 * @return Observable stream of load balancer virtual servers
	 */
	@Deprecated(since="0.15.4")
	Observable<NetworkLoadBalancerInstance> listById(Collection<Long> ids);

	/**
	 * Save updates to existing load balancer instance
	 *
	 * @param instances updated instance
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkLoadBalancerInstance> instances);

	/**
	 * Create new load balancer instances (virtual servers) in Morpheus
	 *
	 * @param instances new load balancer instances to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<NetworkLoadBalancerInstance> instances);

	/**
	 * Remove persisted instances from Morpheus
	 *
	 * @param instances Images to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<LoadBalancerInstanceIdentityProjection> instances);
}
