package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.NetworkLoadBalancerMonitor;
import com.morpheusdata.model.projection.LoadBalancerMonitorIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface for interacting with the morpheus backend database in regards to load balancer health monitor domain objects
 */
public interface MorpheusLoadBalancerMonitorService {
	/**
	 * Get a list of {@link NetworkLoadBalancerMonitor} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<LoadBalancerMonitorIdentityProjection> listSyncProjections(Long loadBalancerId);

	/**
	 * Get a list of NetworkLoadBalancerNode objects from a list of projection ids
	 *
	 * @param ids ids of load balancer monitors
	 * @return Observable stream of NetworkLoadBalancerMonitor objects
	 */
	Observable<NetworkLoadBalancerMonitor> listById(Collection<Long> ids);

	/**
	 * Save updates to existing monitor
	 *
	 * @param monitors updated {@link NetworkLoadBalancerMonitor} objects
	 * @return success
	 */
	Single<Boolean> save(List<NetworkLoadBalancerMonitor> monitors);

	/**
	 * Create new monitor in Morpheus
	 *
	 * @param monitors new {@link NetworkLoadBalancerMonitor} objects to persist
	 * @return success
	 */
	Single<Boolean> create(List<NetworkLoadBalancerMonitor> monitors);

	/**
	 * Remove persisted monitor from Morpheus
	 *
	 * @param monitors {@link LoadBalancerMonitorIdentityProjection} objects to delete from database
	 * @return success
	 */
	Single<Boolean> remove(List<LoadBalancerMonitorIdentityProjection> monitors);

	/**
	 * Helper method to look up a network load balancer health monitor by its externalId (The id on the api side)
	 * @param externalId
	 * @return
	 */
	Single<NetworkLoadBalancerMonitor> findByExternalId(String externalId);

	/**
	 * Use this method to query the database for load balancer health monitors
	 * @param args a map of properties used to query the health monitors
	 * @return Observable stream of {@link NetworkLoadBalancerMonitor}
	 */
	Observable<NetworkLoadBalancerMonitor> queryHealthMonitors(Map args);
}
