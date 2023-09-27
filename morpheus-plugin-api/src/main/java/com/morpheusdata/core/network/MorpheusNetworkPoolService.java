package com.morpheusdata.core.network;

import com.morpheusdata.model.NetworkPool;
import com.morpheusdata.model.NetworkPoolIp;
import com.morpheusdata.model.NetworkPoolRange;
import com.morpheusdata.model.NetworkPoolServer;
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.NetworkPool} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkService}
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getPool()
 * }</pre>
 * @see MorpheusNetworkService
 * @since 0.8.0
 * @author David Estes
 */
public interface MorpheusNetworkPoolService {

	/**
	 * Returns the Pool IP Context for dealing with managing IP Allocations regarding Host Records within a {@link NetworkPool}
	 * @return the Pool IP Context to use for performing IPAM operations within Morpheus.
	 */
	MorpheusNetworkPoolIpService getPoolIp();

	/**
	 * Returns the Pool Range Context
	 * @return the Pool Range Context to use for listing {@link NetworkPoolIp} on a range
	 */
	MorpheusNetworkPoolRangeService getPoolRange();

	/**
	 * Lists all network pool projection objects for a specified pool server id aka {@link NetworkPoolServer}.
	 * The projection is a subset of the properties on a full {@link NetworkPool} object for sync matching.
	 * @param poolServerId the {@link NetworkPoolServer} Identifier associated with the pools to be listed.
	 * @return an RxJava Observable stream of projection objects
	 */
	Observable<NetworkPoolIdentityProjection> listIdentityProjections(Long poolServerId);

	/**
	 * Lists all network pool projection objects
	 * The projection is a subset of the properties on a full {@link NetworkPool} object for sync matching.
	 * @return an RxJava Observable stream of projection objects
	 */
	Observable<NetworkPoolIdentityProjection> listIdentityProjections();

	/**
	 * Lists all {@link NetworkPool} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkPool} objects from.
	 * @return an RxJava Observable stream of {@link NetworkPool} to be subscribed to.
	 */
	Observable<NetworkPool> listById(Collection<Long> ids);

	/**
	 * Removes Missing Network Pools on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.network.removeMissingPools(poolServer.id, removeItems).blockingGet()
	 * }</pre>
	 * @param poolServerId The {@link NetworkPoolServer} id of the server syncing domains
	 * @param removeList a list of {@link NetworkPool} projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(Long poolServerId, List<NetworkPoolIdentityProjection> removeList);

	/**
	 * Removes Missing Network Pools on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.network.removeMissingPools(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of {@link NetworkPool} projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<NetworkPoolIdentityProjection> removeList);

	/**
	 * Creates new Network Pools from cache / sync implementations
	 * This ensures proper ownership and pool server association. It also creates the poolRanges attached to the model.
	 * @param poolServerId The id of the {@link NetworkPoolServer} we are saving into
	 * @param addList List of new {@link NetworkPool} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(Long poolServerId, List<NetworkPool> addList);

	/**
	 * Creates new Network Pools of type Morpheus
	 * This ensures proper ownership and pool server association. It also creates the poolRanges attached to the model.
	 * @param addList List of new {@link NetworkPool} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(List<NetworkPool> addList);

	/**
	 * Saves a list of {@link NetworkPool} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param poolsToSave a list oof {@link NetworkPool} objects to be updated in bulk
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<NetworkPool> poolsToSave);
}

