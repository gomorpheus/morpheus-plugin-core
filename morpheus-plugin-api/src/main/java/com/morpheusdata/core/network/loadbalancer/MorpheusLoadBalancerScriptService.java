package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.NetworkLoadBalancerPolicy;
import com.morpheusdata.model.NetworkLoadBalancerScript;
import com.morpheusdata.model.projection.LoadBalancerScriptIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusLoadBalancerScriptService {
	/**
	 * Get a list of {@link NetworkLoadBalancerScript} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<LoadBalancerScriptIdentityProjection> listSyncProjections(Long loadBalancerId);

	/**
	 * Get a list of {@link NetworkLoadBalancerScript} objects from a list of projection ids
	 *
	 * @param ids ids of load balancer scripts
	 * @return Observable stream of NetworkLoadBalancerNode objects
	 */
	Observable<NetworkLoadBalancerScript> listById(Collection<Long> ids);

	/**
	 * Save updates to existing profiles
	 *
	 * @param scripts updated {@link NetworkLoadBalancerScript} objects
	 * @return success
	 */
	Single<Boolean> save(List<NetworkLoadBalancerScript> scripts);

	/**
	 * Create new load balancer scripts in Morpheus
	 *
	 * @param scripts new {@link NetworkLoadBalancerScript} objects to persist
	 * @return success
	 */
	Single<Boolean> create(List<NetworkLoadBalancerScript> scripts);

	/**
	 * Remove persisted profiles from Morpheus
	 *
	 * @param scripts {@link LoadBalancerScriptIdentityProjection} objects to delete from database
	 * @return success
	 */
	Single<Boolean> remove(List<LoadBalancerScriptIdentityProjection> scripts);

	/**
	 * Use this method to query the database for load balancer scripts
	 * @param args a map of properties used to query the scripts
	 * @return Observable stream of {@link NetworkLoadBalancerScript}
	 */
	Observable<NetworkLoadBalancerPolicy> queryScripts(Map args);
}
