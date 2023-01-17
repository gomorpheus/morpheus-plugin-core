package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.NetworkLoadBalancer;
import com.morpheusdata.model.NetworkLoadBalancerProfile;
import com.morpheusdata.model.projection.LoadBalancerProfileIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusLoadBalancerProfileService {
	/**
	 * Get a list of {@link NetworkLoadBalancerProfile} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @return Observable stream of sync projection
	 */
	Observable<LoadBalancerProfileIdentityProjection> listSyncProjections(Long loadBalancerId);

	/**
	 * Get a list of {@link NetworkLoadBalancerProfile} objects from a list of projection ids
	 *
	 * @param ids ids of load balancer nodes
	 * @return Observable stream of {@link NetworkLoadBalancerProfile} objects
	 */
	Observable<NetworkLoadBalancerProfile> listById(Collection<Long> ids);

	/**
	 * Save updates to existing profiles
	 *
	 * @param profiles updated {@link NetworkLoadBalancerProfile} objects
	 * @return success
	 */
	Single<Boolean> save(List<NetworkLoadBalancerProfile> profiles);

	/**
	 * Create new profiles in Morpheus
	 *
	 * @param profiles new {@link NetworkLoadBalancerProfile} objects to persist
	 * @return success
	 */
	Single<Boolean> create(List<NetworkLoadBalancerProfile> profiles);

	/**
	 * Remove persisted profiles from Morpheus
	 *
	 * @param profiles {@link LoadBalancerProfileIdentityProjection} objects to delete from database
	 * @return success
	 */
	Single<Boolean> remove(List<LoadBalancerProfileIdentityProjection> profiles);

	/**
	 * retrieve a load balancer profile by external id and load balancer (used in syncing)
	 * @param externalId
	 * @param loadBalancer
	 * @return
	 */
	Single<NetworkLoadBalancerProfile> findByExternalIdAndLoadBalancer(String externalId, NetworkLoadBalancer loadBalancer);

	/**
	 * Use this method to query the database for load balancer profiles
	 * @param args a map of properties used to query the profiles
	 * @return Observable stream of {@link NetworkLoadBalancerProfile}
	 */
	Observable<NetworkLoadBalancerProfile> queryProfiles(Map args);
}
