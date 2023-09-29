package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.NetworkRoute;
import com.morpheusdata.model.NetworkRouter;
import com.morpheusdata.model.projection.NetworkRouteIdentityProjection;
import com.morpheusdata.model.projection.NetworkRouteTableIdentityProjection;
import com.morpheusdata.model.projection.NetworkRouterIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * This Context deals with interactions related to {@link NetworkRoute} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkService} and
 * finally via the {@link MorpheusNetworkRouterService} traversal.
 *
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getRouter().getRoute()
 * }</pre>
 *
 */
public interface MorpheusNetworkRouteService extends MorpheusDataService<NetworkRoute, NetworkRouteIdentityProjection>, MorpheusIdentityService<NetworkRouteIdentityProjection> {

	/**
	 * Lists NetworkRouteIdentityProjections for a specified network route table.
	 * The projection is a subset of the properties on a full {@link NetworkRoute} object for sync matching.
	 * @param routeTable the {@link NetworkRouteTableIdentityProjection} identifier associated to the route table to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkRouteIdentityProjection> listIdentityProjections(NetworkRouteTableIdentityProjection routeTable);

	/**
	 * Lists all {@link NetworkRoute} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkRoute} objects from.
	 * @return an RxJava Observable stream of {@link NetworkRoute} to be subscribed to.
	 */
	@Deprecated(since="0.15.4")
	Observable<NetworkRoute> listById(Collection<Long> ids);

	/**
	 * Removes Missing Network Routes on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetwork().getRouter().getRoute().remove(networkRouter.id, removeItems).blockingGet()
	 * }</pre>
	 * @param networkRouterId The {@link NetworkRouter} id of the network router
	 * @param removeList a list of {@link NetworkRouteIdentityProjection} projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(Long networkRouterId, List<NetworkRouteIdentityProjection> removeList);

	/**
	 * Creates new Network Router from cache / sync implementations
	 * @param networkRouter The {NetworkRouter} we are bulk creating routes into.
	 * @param addList List of new {@link NetworkRoute} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(NetworkRouterIdentityProjection networkRouter, List<NetworkRoute> addList);

	/**
	 * Saves a single {@link NetworkRoute} object returning the final result object if any changes occurred during save.
	 * @param networkRoute the Network Route we wish to persist changes to
	 * @return the resultant Host Record Object containing any additional metadata that may have been applied
	 */
	Single<NetworkRoute> save(NetworkRouterIdentityProjection networkRouter, NetworkRoute networkRoute);
}
