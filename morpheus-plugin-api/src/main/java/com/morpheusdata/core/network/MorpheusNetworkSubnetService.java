package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkSubnetIdentityProjection;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Morpheus Context as it relates to network subnets related operations. This context contains methods for querying subnets, creating,
 * updating and deleting subnets.  Typically this class is accessed via the primary {@link MorpheusContext}.
 *
 * @since 0.11.0
 * @author Bob Whiton
 */
public interface MorpheusNetworkSubnetService extends MorpheusDataService<NetworkSubnet, NetworkSubnetIdentityProjection>, MorpheusIdentityService<NetworkSubnetIdentityProjection> {

	/**
	 * Lists all subnet projection objects for a specified network.
	 * The projection is a subset of the properties on a full {@link NetworkSubnet} object for sync matching.
	 * @param network the {@link Network} identifier associated to the subnets to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkSubnetIdentityProjection> listIdentityProjections(Network network);

	/**
	 * Lists all subnet projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link NetworkSubnet} object for sync matching.
	 * @param cloudId id of the {@link Cloud} identifier associated to the subnets to be listed.
	 * @param cloudPoolId (optional) id of the {@link CloudPool} that the associated Network must be associated with via assignedZonePools
	 * @param category (optional category name that the network must have
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkSubnetIdentityProjection> listIdentityProjections(Long cloudId, Long cloudPoolId, String category);

	/**
	 * Lists all {@link NetworkSubnet} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkSubnet} objects from.
	 * @return an RxJava Observable stream of {@link NetworkSubnet} to be subscribed to.
	 */
	@Deprecated(since="0.15.4")
	Observable<NetworkSubnet> listById(Collection<Long> ids);

	/**
	 * Removes Missing NetworkSubnets on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetworkSubnet().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList list of NetworkSubnets to remove
	 * @return a Single {@link Observable} returning the success status of the operation.
	 * @deprecated use {@link #bulkRemove } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> remove(List<NetworkSubnetIdentityProjection> removeList);

	/**
	 * Creates new NetworkSubnet Domains from cache / sync implementations
	 * @param addList List of new {@link NetworkSubnet} objects to be inserted into the database
	 * @param network Network to add the NetworkSubnet to
	 * @return notification of completion if someone really cares about it
	 * @deprecated use {@link #bulkCreate } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> create(List<NetworkSubnet> addList, Network network);

	/**
	 * Saves a list of {@link NetworkSubnet} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param networkSubnetsToSave a List of NetworkSubnet objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkSubnet> networkSubnetsToSave);
}
