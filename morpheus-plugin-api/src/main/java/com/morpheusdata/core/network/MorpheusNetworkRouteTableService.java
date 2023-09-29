package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkRouteTableIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

public interface MorpheusNetworkRouteTableService extends MorpheusDataService<NetworkRouteTable, NetworkRouteTableIdentityProjection>, MorpheusIdentityService<NetworkRouteTableIdentityProjection> {

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

	/**
	 * Lists all {@link NetworkRouteTable} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkRouteTable} objects from.
	 * @return an RxJava Observable stream of {@link NetworkRouteTable} to be subscribed to.
	 */
	@Deprecated(since="0.15.4")
	Observable<NetworkRouteTable> listById(Collection<Long> ids);

	/**
	 * Removes Missing Router Tables on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is an Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetwork().getRouteTable().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of router projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<NetworkRouteTableIdentityProjection> removeList);

	/**
	 * Creates new Network Route Tables from cache / sync implementations
	 * @param addList List of new {@link NetworkRouteTable} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<NetworkRouteTable> addList);

	/**
	 * Saves a list of {@link NetworkRouteTable} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param routerTablesToSave a List of Network Route Table objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkRouteTable> routerTablesToSave);
}
