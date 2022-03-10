package com.morpheusdata.test.network;

import com.morpheusdata.core.network.MorpheusNetworkPoolService;
import com.morpheusdata.core.network.MorpheusNetworkPoolIpService;
import com.morpheusdata.model.NetworkPool;
import com.morpheusdata.model.NetworkPoolServer;
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

public class MorpheusNetworkPoolServiceImpl implements MorpheusNetworkPoolService {
	/**
	 * Returns the Pool IP Context for dealing with managing IP Allocations regarding Host Records within a {@link NetworkPool}
	 *
	 * @return the Pool IP Context to use for performing IPAM operations within Morpheus.
	 */
	@Override
	public MorpheusNetworkPoolIpService getPoolIp() {
		return null;
	}

	/**
	 * Lists all network pool projection objects for a specified pool server id aka {@link NetworkPoolServer}.
	 * The projection is a subset of the properties on a full {@link NetworkPool} object for sync matching.
	 *
	 * @param poolServerId the pool server Identifier to scope the list of {@link NetworkPool} query
	 * @return an RxJava Observable stream of projection objects
	 */
	@Override
	public Observable<NetworkPoolIdentityProjection> listIdentityProjections(Long poolServerId) {
		return null;
	}

	@Override
	public Observable<NetworkPoolIdentityProjection> listIdentityProjections() {
		return null;
	}

	/**
	 * Lists all {@link NetworkPool} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 *
	 * @param ids list of ids to grab {@link NetworkPool} objects from.
	 * @return an RxJava Observable stream of {@link NetworkPool} to be subscribed to.
	 */
	@Override
	public Observable<NetworkPool> listById(Collection<Long> ids) {
		return null;
	}

	/**
	 * Removes Missing Network Pools on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.network.removeMissingPools(poolServer.id, removeItems).blockingGet()
	 * }</pre>
	 *
	 * @param poolServerId The {@link NetworkPoolServer} id of the server syncing domains
	 * @param removeList   a list of {@link NetworkPool} projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	@Override
	public Single<Boolean> remove(Long poolServerId, List<NetworkPoolIdentityProjection> removeList) {
		return null;
	}

	@Override
	public Single<Boolean> remove(List<NetworkPoolIdentityProjection> removeList) {
		return null;
	}

	/**
	 * Creates new Network Pools from cache / sync implementations
	 * This ensures proper ownership and pool server association. It also creates the poolRanges attached to the model.
	 *
	 * @param poolServerId The id of the {@link NetworkPoolServer} we are saving into
	 * @param addList      List of new {@link NetworkPool} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	@Override
	public Single<Boolean> create(Long poolServerId, List<NetworkPool> addList) {
		return null;
	}

	@Override
	public Single<Boolean> create(List<NetworkPool> addList) {
		return null;
	}

	/**
	 * Saves a list of {@link NetworkPool} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 *
	 * @param poolsToSave a list of {@link NetworkPool} objects to be saved in bulk
	 * @return the Single Observable stating the success state of the save attempt
	 */
	@Override
	public Single<Boolean> save(List<NetworkPool> poolsToSave) {
		return null;
	}
}
