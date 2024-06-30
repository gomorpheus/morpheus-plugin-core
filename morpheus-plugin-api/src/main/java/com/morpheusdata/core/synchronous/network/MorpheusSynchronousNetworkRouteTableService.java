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

package com.morpheusdata.core.synchronous.network;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.CloudPool;
import com.morpheusdata.model.NetworkRouteTable;
import com.morpheusdata.model.projection.NetworkRouteTableIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

public interface MorpheusSynchronousNetworkRouteTableService extends MorpheusSynchronousDataService<NetworkRouteTable, NetworkRouteTableIdentityProjection>, MorpheusSynchronousIdentityService<NetworkRouteTableIdentityProjection> {

	/**
	 * Lists all route table projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkRouteTable} object for sync matching.
	 * @param zonePool the {@link CloudPool} identifier associated to the route tables to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouteTableIdentityProjection> listIdentityProjections(CloudPool zonePool);

	/**
	 * Lists all route table projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkRouteTable} object for sync matching.
	 * @param poolId the id of the {@link CloudPool} associated to the route tables to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouteTableIdentityProjection> listIdentityProjections(Long poolId);
}
