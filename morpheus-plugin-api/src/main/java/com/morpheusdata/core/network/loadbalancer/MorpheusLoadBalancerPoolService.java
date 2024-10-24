/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.NetworkLoadBalancer;
import com.morpheusdata.model.NetworkLoadBalancerMember;
import com.morpheusdata.model.NetworkLoadBalancerPool;
import com.morpheusdata.model.projection.LoadBalancerPoolIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MorpheusLoadBalancerPoolService extends MorpheusDataService<NetworkLoadBalancerPool, LoadBalancerPoolIdentityProjection>, MorpheusIdentityService<LoadBalancerPoolIdentityProjection> {
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
	@Deprecated(since="0.15.4")
	Observable<NetworkLoadBalancerPool> listById(Collection<Long> ids);

	/**
	 * Save updates to existing pool
	 *
	 * @param pools updated pools
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkLoadBalancerPool> pools);

	/**
	 * Create new load balancer pool in Morpheus
	 *
	 * @param pools new {@link NetworkLoadBalancerPool}  to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<NetworkLoadBalancerPool> pools);

	/**
	 * Remove persisted load balancer pools from Morpheus
	 *
	 * @param pools Images to delete
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<LoadBalancerPoolIdentityProjection> pools);

	/**
	 * Retrieves a single load balancer pool from the database given a load balancer and external id
	 * @param loadBalancer {@link NetworkLoadBalancer}
	 * @param externalId the id of the {@link NetworkLoadBalancerPool} generated by the integration
	 * @return 1 or 0 {@link NetworkLoadBalancerPool}
	 */
	@Deprecated(since="0.15.4")
	Single<Optional<NetworkLoadBalancerPool>> findByLoadBalancerAndExternalId(NetworkLoadBalancer loadBalancer, String externalId);

	/**
	 * This method is used to grab the persisted pool members for a given load balancer pool
	 * @param pool {@link NetworkLoadBalancerPool}
	 * @return an observable stream of {@link NetworkLoadBalancerMember}
	 */
	Observable<NetworkLoadBalancerMember> getPoolMembers(NetworkLoadBalancerPool pool);
}
