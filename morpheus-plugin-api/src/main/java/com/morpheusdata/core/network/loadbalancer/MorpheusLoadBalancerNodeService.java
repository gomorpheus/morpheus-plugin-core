package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.NetworkLoadBalancer;
import com.morpheusdata.model.NetworkLoadBalancerNode;
import com.morpheusdata.model.projection.LoadBalancerNodeIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface for interacting with the morpheus backend database in regards to load balancer node domain objects
 */
public interface MorpheusLoadBalancerNodeService extends MorpheusDataService<NetworkLoadBalancerNode, LoadBalancerNodeIdentityProjection>, MorpheusIdentityService<LoadBalancerNodeIdentityProjection> {
	/**
	 * Get a list of {@link com.morpheusdata.model.NetworkLoadBalancerNode} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<LoadBalancerNodeIdentityProjection> listSyncProjections(Long loadBalancerId);

	/**
	 * Get a list of NetworkLoadBalancerNode objects from a list of projection ids
	 *
	 * @param ids ids of load balancer nodes
	 * @return Observable stream of NetworkLoadBalancerNode objects
	 */
	@Deprecated(since="0.15.4")
	Observable<NetworkLoadBalancerNode> listById(Collection<Long> ids);

	/**
	 * Save updates to existing node
	 *
	 * @param nodes updated {@link NetworkLoadBalancerNode} objects
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkLoadBalancerNode> nodes);

	/**
	 * Create new node in Morpheus
	 *
	 * @param nodes new {@link NetworkLoadBalancerNode} objects to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<NetworkLoadBalancerNode> nodes);

	/**
	 * Remove persisted node from Morpheus
	 *
	 * @param nodes {@link LoadBalancerNodeIdentityProjection} objects to delete from database
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<LoadBalancerNodeIdentityProjection> nodes);

	/**
	 * Retrieves a single load balancer node from the database given a load balancer and external id
	 * @param loadBalancer {@link NetworkLoadBalancer}
	 * @param externalId the id of a {@link NetworkLoadBalancerNode} generated by the integration
	 * @return 1 or 0 {@link com.morpheusdata.model.NetworkLoadBalancerNode}
	 */
	Single<Optional<NetworkLoadBalancerNode>> findByLoadBalancerAndExternalId(NetworkLoadBalancer loadBalancer, String externalId);

	/**
	 * Use this method to query the database for load balancer nodes
	 * @param args a map of properties used to query the scripts
	 * @return Observable stream of {@link NetworkLoadBalancerNode}
	 */
	Observable<NetworkLoadBalancerNode> queryNodes(Map args);
}
