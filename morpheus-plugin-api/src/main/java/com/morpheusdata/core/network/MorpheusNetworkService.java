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
import com.morpheusdata.core.providers.DNSProvider;
import com.morpheusdata.core.providers.IPAMProvider;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkIdentityProjection;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Morpheus Context as it relates to network related operations. This context contains methods for querying things like network pools
 * network domains, and other network related objects. It also contains methods for applying updates ore creating new objects related to
 * networks. Typically this class is accessed via the primary {@link MorpheusContext}.
 *
 * @see MorpheusNetworkPoolService
 * @see MorpheusNetworkDomainService
 *
 * @author David Estes, Eric Helgeson
 */
public interface MorpheusNetworkService extends MorpheusDataService<Network, NetworkIdentityProjection>, MorpheusIdentityService<NetworkIdentityProjection> {


	/**
	 * Returns the NetworkPoolContext used for performing updates or queries on {@link NetworkPool} related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return An instance of the Network Pool Context to be used for calls by various network providers
	 */
	MorpheusNetworkPoolService getPool();

	/**
	 * Returns the NetworkDomainContext used for performing updates/queries on {@link NetworkDomain} related assets
	 * within Morpheus. Most useful when implementing DNS related services.
	 * @return An instance of the Network Domain Context to be used for calls by various network providers
	 */
	MorpheusNetworkDomainService getDomain();

	/**
	 * Returns the MorpheusNetworkTypeContext used for performing updates/queries on {@link NetworkType} related assets
	 * within Morpheus.
	 * @return An instance of the NetworkTypeContext to be used for calls by various network providers
	 */
	MorpheusNetworkTypeService getType();

	/**
	 * Returns the {@link MorpheusNetworkServerService} used for performing updates/queries on {@link NetworkServer} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusNetworkServerService}
	 */
	MorpheusNetworkServerService getServer();


	/**
	 * Returns the {@link MorpheusNetworkRouterService} used for performing updates/queries on {@link NetworkRouter} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusNetworkRouterService}
	 */
	MorpheusNetworkRouterService getRouter();

	/**
	 * Returns the {@link MorpheusNetworkRouteTableService} used for performing updates/queries on {@link NetworkRouteTable} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusNetworkRouteTableService}
	 */
	MorpheusNetworkRouteTableService getRouteTable();

	/**
	 * Returns the {@link MorpheusNetworkProxyService} used for performing updates/queries on {@link NetworkProxy} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusNetworkProxyService}
	 */
	MorpheusNetworkProxyService getNetworkProxy();

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param poolServer the pool integration with which we want to update the status.
	 * @param status the status of the pool server (ok,syncing,error)
	 * @param message the status message for more details. typically only used when status is 'error'.
	 *
	 * @return a Completable for notification or subscription
	 */
	Completable updateNetworkPoolServerStatus(NetworkPoolServer poolServer, AccountIntegration.Status status, String message);

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param poolServer the pool integration with which we want to update the status.
	 * @param status the status string of the pool server (ok,syncing,error)
	 *
	 * @return the on complete state
	 */
	Completable updateNetworkPoolServerStatus(NetworkPoolServer poolServer, AccountIntegration.Status status);


	//Network ORM Object Methods
	/**
	 * Lists all network projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param accountIntegration the {@link AccountIntegration} identifier associated to the networks to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkIdentityProjection> listIdentityProjections(AccountIntegration accountIntegration);

	/**
	 * Lists all network projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkIdentityProjection> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all network projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkIdentityProjection> listIdentityProjections(Long cloudId);

	/**
	 * Lists all network projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the domains to be listed.
	 * @param regionCode the {@link ComputeZoneRegion} to optionally filter by
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkIdentityProjection> listIdentityProjections(Long cloudId, String regionCode);

	/**
	 * Lists all network projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the domains to be listed.
	 * @param zonePoolId the id of the {@link ComputeZonePool} that this network may be scoped to. NOTE: if a network is associated with many assignedZonePools this field zonePoolId is not used.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkIdentityProjection> listIdentityProjections(Long cloudId,Long zonePoolId);


	/**
	 * Lists all network projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the domains to be listed.
	 * @deprecated replaced by {@link #listIdentityProjections(Long)}
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated
	Observable<NetworkIdentityProjection> listSyncProjections(Long cloudId);

	/**
	 * Lists all network projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the domains to be listed.
	 * @param zonePoolId the id of the {@link ComputeZonePool} that this network may be scoped to. NOTE: if a network is associated with many assignedZonePools this field zonePoolId is not used.
	 * @deprecated replaced by {@link #listIdentityProjections(Long,Long)}
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Deprecated
	Observable<NetworkIdentityProjection> listSyncProjections(Long cloudId,Long zonePoolId);


	/**
	 * Lists all {@link Network} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link Network} objects from.
	 * @return an RxJava Observable stream of {@link Network} to be subscribed to.
	 */
	@Deprecated(since="0.15.4")
	Observable<Network> listById(Collection<Long> ids);

	/**
	 * Lists all {@link Network} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param cloudId the cloud to filter the list of networks by.
	 * @param externalIds a Collection of external Ids to filter the list of networks by
	 * @return an RxJava Observable stream of {@link Network} to be subscribed to.
	 */
	@Deprecated(since="0.15.4")
	Observable<Network> listByCloudAndExternalIdIn(Long cloudId, Collection<String> externalIds);

	/**
	 * Removes Missing Networks on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetwork().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of network projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<NetworkIdentityProjection> removeList);

	/**
	 * Creates new Network Domains from cache / sync implementations
	 * This ensures the refType and refId match the poolServer as well as the owner default
	 * @param addList List of new {@link Network} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<Network> addList);

	/**
	 * Saves a list of {@link Network} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param networksToSave a List of Network objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<Network> networksToSave);

	//General Network Methods
	Single<Void> removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress);

	Single<NetworkPoolServer> getPoolServerByAccountIntegration(AccountIntegration integration);

	Single<NetworkPoolServer> getPoolServerById(Long id);

	Single<NetworkPoolIp> getNetworkIp(NetworkPool networkPool, String assignedType, Long assignedId, Long subAssignedId);

	Single<NetworkPoolIp> loadNetworkPoolIp(NetworkPool pool, String ipAddress);

	Single<Network> setComputeServerNetwork(ComputeServer server, String privateIp, String publicIp, String hostname, Long networkPoolId);

	/**
	 * Get a {@link NetworkServer} by id.
	 * @param id NetworkServer id
	 * @return Observable stream of sync projection
	 */
	Single<NetworkServer> getNetworkServerById(Long id);

}
