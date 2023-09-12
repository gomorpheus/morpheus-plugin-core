package com.morpheusdata.core.network;

import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkRouterIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Provides Morpheus services related to querying, saving, and removing {@link NetworkRouter} type objects. Routers can represent
 * many types of routers. These can include Internet Gateways, Egress Gateways, Generic Routers, or just even Vpn Gateways
 * @author David Estes
 * @since 0.14.0
 */
public interface MorpheusNetworkRouterService {

	/**
	 * Returns the {@link MorpheusNetworkRouteService} used for performing updates/queries on {@link NetworkRoute} related assets
	 * within Morpheus
	 * @return An instance of the {@link MorpheusNetworkRouteService}
	 */
	MorpheusNetworkRouteService getRoute();

	//Network Router ORM Object Methods
	/**
	 * Lists all network projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link NetworkRouter} object for sync matching.
	 * @param accountIntegration the {@link AccountIntegration} identifier associated to the routers to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouterIdentityProjection> listIdentityProjections(AccountIntegration accountIntegration);

	/**
	 * Lists all network router projection objects for a specified zone pool.
	 * The projection is a subset of the properties on a full {@link NetworkRouter} object for sync matching.
	 * @param cloudPool the {@link CloudPool} identifier associated to the routers to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouterIdentityProjection> listIdentityProjections(CloudPool cloudPool);

	/**
	 * Lists all router projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkRouter} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the routers to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouterIdentityProjection> listIdentityProjections(Cloud cloud);

	/**
	 * Lists all router projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkRouter} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the routers to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouterIdentityProjection> listIdentityProjections(Long cloudId);

	/**
	 * Lists all router projection objects for a specified cloud by type code.
	 * The projection is a subset of the properties on a full {@link NetworkRouter} object for sync matching.
	 * @param cloudId the id of the {@link Cloud} associated to the routers to be listed.
	 * @param typeCode the unique code of the {@link NetworkRouterType} that this router may be scoped to.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouterIdentityProjection> listIdentityProjections(Long cloudId,String typeCode);

	/**
	 * Lists all {@link NetworkRouter} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkRouter} objects from.
	 * @return an RxJava Observable stream of {@link NetworkRouter} to be subscribed to.
	 */
	Observable<NetworkRouter> listById(Collection<Long> ids);

	/**
	 * Lists all {@link NetworkRouter} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param cloudId the cloud to filter the list of networks by.
	 * @param externalIds a Collection of external Ids to filter the list of networks by
	 * @return an RxJava Observable stream of {@link NetworkRouter} to be subscribed to.
	 */
	Observable<NetworkRouter> listByCloudAndExternalIdIn(Long cloudId, Collection<String> externalIds);

	/**
	 * Removes Missing Routers on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is an Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetwork().getRouter().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of router projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<NetworkRouterIdentityProjection> removeList);

	/**
	 * Creates new Network Domains from cache / sync implementations
	 * This ensures the refType and refId match the poolServer as well as the owner default
	 * Any NetworkRoutes will not be added. They must be added/removed via MorpheusNetworkRouteService
	 * @param addList List of new {@link NetworkRouter} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(List<NetworkRouter> addList);

	/**
	 * Saves a list of {@link NetworkRouter} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * Any NetworkRoutes will not be added/removed. They must be added/removed via MorpheusNetworkRouteService
	 * @param routersToSave a List of Router objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<NetworkRouter> routersToSave);
}
