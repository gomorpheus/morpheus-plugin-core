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

package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkServerIdentityProjection;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;


/**
 * Provides Morpheus services related to querying, saving, and removing {@link NetworkServer} type objects.
 * @author Alex Clement
 * @since 1.1.7
 */
public interface MorpheusNetworkServerService extends MorpheusDataService<NetworkServer, NetworkServerIdentityProjection>, MorpheusIdentityService<NetworkServerIdentityProjection> {

	MorpheusNetworkSwitchService getSwitch();

	//Network Server ORM Object Methods
	/**
	 * Lists all network server projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link NetworkServer} object for sync matching.
	 * @param accountIntegration the {@link AccountIntegration} identifier associated to the servers to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkServer> listIdentityProjections(AccountIntegration accountIntegration);


	/**
	 * Lists all network server projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkServer} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the servers to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkServer> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all network server projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkServer} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the servers to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkServer> listIdentityProjections(Long cloudId);

	/**
	 * Lists all network server projection objects for a specified cloud by type code.
	 * The projection is a subset of the properties on a full {@link NetworkServer} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the servers to be listed.
	 * @param typeCode the unique code of the {@link NetworkServerType} that this server may be scoped to.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkServer> listIdentityProjections(Long cloudId, String typeCode);

	Single<ServiceResponse> validateUpdate(UpdateDefinition updateDefinition, NetworkServer networkServer);

	Single<ServiceResponse> executeUpdate(UpdateDefinition updateDefinition, NetworkServer networkServer);

	Single<ServiceResponse> postUpdate(UpdateDefinition updateDefinition, NetworkServer networkServer);

	Single<ServiceResponse> rollbackUpdate(UpdateDefinition updateDefinition, NetworkServer networkServer);

	Single<ServiceResponse> refreshUpdate(UpdateOperation updateOperation, NetworkServer networkServer);

	Single<ServiceResponse> runConfigurationDriftCheck(CheckLevel checkLevel, NetworkServer networkServer);

	Single<ServiceResponse> getConfigurationDriftDetails(NetworkServer networkServer);

	/**
	 * Trigger a short refresh on a network server. This initiates a data sync/inventory refresh
	 * of the network server's resources.
	 * @param networkServer network server to refresh
	 * @return Boolean returns the result of the network server refresh request.
	 */
	Single<Boolean> refresh(NetworkServer networkServer);

	/**
	 * Trigger a daily (full) refresh on a network server. This initiates a full data sync
	 * of the network server's resources.
	 * @param networkServer network server to refresh
	 * @return Boolean returns the result of the network server refresh request.
	 */
	Single<Boolean> refreshDaily(NetworkServer networkServer);

}
