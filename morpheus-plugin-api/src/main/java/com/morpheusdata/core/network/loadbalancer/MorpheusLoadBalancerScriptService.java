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
import com.morpheusdata.model.NetworkLoadBalancerPolicy;
import com.morpheusdata.model.NetworkLoadBalancerScript;
import com.morpheusdata.model.projection.LoadBalancerScriptIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusLoadBalancerScriptService extends MorpheusDataService<NetworkLoadBalancerScript, LoadBalancerScriptIdentityProjection>, MorpheusIdentityService<LoadBalancerScriptIdentityProjection> {
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
	@Deprecated(since="0.15.4")
	Observable<NetworkLoadBalancerScript> listById(Collection<Long> ids);

	/**
	 * Save updates to existing profiles
	 *
	 * @param scripts updated {@link NetworkLoadBalancerScript} objects
	 * @return success
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkLoadBalancerScript> scripts);

	/**
	 * Create new load balancer scripts in Morpheus
	 *
	 * @param scripts new {@link NetworkLoadBalancerScript} objects to persist
	 * @return success
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<NetworkLoadBalancerScript> scripts);

	/**
	 * Remove persisted profiles from Morpheus
	 *
	 * @param scripts {@link LoadBalancerScriptIdentityProjection} objects to delete from database
	 * @return success
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<LoadBalancerScriptIdentityProjection> scripts);

	/**
	 * Use this method to query the database for load balancer scripts
	 * @param args a map of properties used to query the scripts
	 * @return Observable stream of {@link NetworkLoadBalancerScript}
	 */
	@Deprecated(since="0.15.4")
	Observable<NetworkLoadBalancerPolicy> queryScripts(Map args);
}
