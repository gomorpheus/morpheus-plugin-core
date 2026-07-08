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

package com.morpheusdata.core;

import com.morpheusdata.core.compute.MorpheusComputeDeviceService;
import com.morpheusdata.core.compute.MorpheusComputeServerAccessService;
import com.morpheusdata.core.compute.MorpheusComputeServerNetworkInterfaceConfig;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.ComputeServerIdentityProjection;
import com.morpheusdata.core.compute.MorpheusComputeServerInterfaceService;
import com.morpheusdata.request.AddHostRequest;
import com.morpheusdata.request.RemoveHostRequest;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link ComputeServer} in Morpheus
 * @author Mike Truso
 * @since 0.8.0
 */
public interface MorpheusComputeServerService extends MorpheusDataService<ComputeServer,ComputeServerIdentityProjection>, MorpheusIdentityService<ComputeServerIdentityProjection> {

	/**
	 * Get a list of {@link ComputeServer} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @param regionCode the {@link ComputeZoneRegion} to optionally filter by
	 * @return Observable stream of sync projection
	 */
	Observable<ComputeServerIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Get a list of {@link ComputeServer} projections based on Cloud id
	 * @param cloudId Cloud id
	 * @return Observable stream of sync projection
	 * @deprecated replaced by {{@link #listIdentityProjections(Long, String)}}
	 */
	@Deprecated
	Observable<ComputeServerIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Lists all {@link ComputeServer} objects by cloud ID and a list of External IDs.
	 * @param cloudId the cloud to filter the list of servers by.
	 * @param externalIds a Collection of external Ids to filter the list of servers by
	 * @return an RxJava Observable stream of {@link ComputeServer} to be subscribed to.
	 */
	Observable<ComputeServer> listByCloudAndExternalIdIn(Long cloudId, Collection<String> externalIds);

	/**
	 * Lists all {@link ComputeServer} objects by a {@link ComputeZonePool }.
	 * @param resourcePoolId the id of the resource pool (ComputeZonePool) to filter the list of servers by.
	 * @return an RxJava Observable stream of {@link ComputeServer} to be subscribed to.
	 */
	Observable<ComputeServer> listByResourcePoolId(Long resourcePoolId);

	/**
	 * Update the power state of a server and any related vms
	 *
	 * @param computeServerId id of the {@link ComputeServer}
	 * @param state power state
	 * @return void
	 */
	Single<Void> updatePowerState(Long computeServerId, ComputeServer.PowerState state);

	/**
	 * Returns the ComputeServerInterfaceContext used for performing updates or queries on {@link ComputeServerInterface} related assets within Morpheus.
	 * @return An instance of the ComputeServerInterface Context
	 */
	MorpheusComputeServerInterfaceService getComputeServerInterface();

	/**
	 * Returns the ComputePort context used for performing sync operations on {@link ComputePort} related assets within Morpheus.
	 * @return An instance of the ComputePort context
	 */
	MorpheusComputePortService getComputePort();


	/**
	 * Returns the ComputePort context used for performing sync operations on {@link ComputeServerAccess} related assets within Morpheus.
	 * @return An instance of the ComputeServerAccess context
	 */
	MorpheusComputeServerAccessService getAccess();

	/**
	 * Returns the ComputeDevice context used for performing sync operations on {@link ComputeDevice} related assets within Morpheus.
	 * @return An instance of the ComputeDevice context
	 */
	MorpheusComputeDeviceService getComputeDevice();

	/**
	 * Remove persisted ComputeServers from Morpheus and remove them the {@link com.morpheusdata.model.InstanceScale} they are associated with
	 * @param computeServers ComputeServers to delete
	 * @param instanceScale the InstanceScale instance to remove the servers from
	 * @return success
	 */
	Single<Boolean> remove(List<ComputeServer> computeServers, InstanceScale instanceScale);

	/**
	 * Provisions a new host onto the target {@link Cloud} using the supplied request details.
	 * <p>
	 * <strong>This triggers host provisioning.</strong> A new {@link ComputeServer} is created and
	 * queued for provisioning on the cloud; on success the returned server may still be in a
	 * provisioning state. This is not a metadata-only registration of an existing machine — it
	 * stands up a new compute host on the cloud.
	 *
	 * @param cloud the target cloud that should receive the newly provisioned host
	 * @param request the request object describing the host to provision
	 * @return a {@link ServiceResponse} containing the newly provisioned {@link ComputeServer} on success
	 * @since 1.4.2
	 */
	Single<ServiceResponse<ComputeServer>> addHost(Cloud cloud, AddHostRequest request);

	/**
	 * Removes the specified host using the supplied request details.
	 *
	 * @param server the {@link ComputeServer} host to remove
	 * @param request the request object describing the host removal
	 * @return a {@link ServiceResponse} indicating success or failure
	 * @since 1.4.2
	 */
	Single<ServiceResponse> removeHost(ComputeServer server, RemoveHostRequest request);

	/**
	 * Stop a ComputeServer. This is an async operation and the server may not be stopped immediately.
	 * @param computeServerId ComputeServer id to stop
	 * @return success if the request to stop the server was successful
	 */
	Single<Boolean> stopServer(Long computeServerId);

	/**
	 * Start a ComputeServer. This is an async operation and the server may not be started immediately.
	 * @param computeServerId ComputeServer id to start
	 * @return success if the request to start the server was successful
	 */
	Single<Boolean> startServer(Long computeServerId);

	/**
	 * Restart a ComputeServer. This is an async operation and the server may not be restarted immediately.
	 * @param computeServerId ComputeServer id to restart
	 * @return success if the request to restart the server was successful
	 */
	Single<Boolean> restartServer(Long computeServerId);


	Single<ServiceResponse> validateUpdate(UpdateDefinition updateDefinition, ComputeServer... computeServer);

	Single<ServiceResponse> executeUpdate(UpdateDefinition updateDefinition, ComputeServer... computeServer);

	Single<ServiceResponse> postUpdate(UpdateDefinition updateDefinition, ComputeServer... computeServer);

	Single<ServiceResponse> rollbackUpdate(UpdateDefinition updateDefinition, ComputeServer... computeServer);

	Single<ServiceResponse> refreshUpdate(ComputeServer... computeServer);

	Single<ServiceResponse> runConfigurationDriftCheck(CheckLevel checkLevel, ComputeServer... computeServer);

	Single<ServiceResponse> getConfigurationDriftDetails(ComputeServer... computeServer);

	ComputeServerInterface buildComputeServerInterface(Account account, Instance instance, ComputeServer server, MorpheusComputeServerNetworkInterfaceConfig networkInterfaceConfig);

}
