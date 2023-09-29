package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkPoolIdentityProjection;
import com.morpheusdata.model.projection.NetworkPoolIpIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.NetworkPoolIp} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkService} and
 * finally via the {@link MorpheusNetworkPoolService} traversal.
 * Network Pool Ip Records are Host Records entities within an IPAM Service. These are create/destroyed based on provisioning
 * integrations as well as syncing with Pool Server Integration types.
 *
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getPool().getPoolIp()
 * }</pre>
 *
 * @see MorpheusNetworkPoolService
 * @since 0.8.0
 * @author David Estes
 */
public interface MorpheusNetworkPoolIpService extends MorpheusDataService<NetworkPoolIp, NetworkPoolIpIdentityProjection>, MorpheusIdentityService<NetworkPoolIpIdentityProjection> {

	/**
	 * Lists all network pool ip projection objects for a specified network pool within a pool server integration.
	 * This is a good way to list all unique host records within a pool.
	 * NOTE: Some integrations do not sync existing reservations but only track greenfield reservations.
	 *       This can depend entirely on the size of the pool. /16 CIDRs to /32 CIDRs should be fine.
	 * @param poolId The Identifier of the {@link NetworkPool} to list all ip reservations against.
	 * @return an RxJava Observable stream of projection objects
	 */
	Observable<NetworkPoolIpIdentityProjection> listIdentityProjections(Long poolId);

	/**
	 * Lists all {@link NetworkPoolIp} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkPoolIp} objects from.
	 * @return an RxJava Observable stream of {@link NetworkPoolIp} to be subscribed to.
	 */
	@Deprecated
	Observable<NetworkPoolIp> listById(Collection<Long> ids);

	/**
	 * Removes Missing Network Pools on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetwork().getPool().getPoolIp().remove(pool.id, removeItems).blockingGet()
	 * }</pre>
	 * @param poolId The {@link NetworkPool} id of the network pool
	 * @param removeList a list of {@link NetworkPoolIpIdentityProjection} projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(Long poolId, List<NetworkPoolIpIdentityProjection> removeList);

	/**
	 * Creates new Network Pool Host Records from cache / sync implementations
	 * This ensures the owner of the host/ip record is correct upon creation.
	 * @param pool The {NetworkPool} we are bulk creating host records into.
	 * @param addList List of new {@link NetworkPoolIp} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(NetworkPoolIdentityProjection pool, List<NetworkPoolIp> addList);

	/**
	 * Saves a list of {@link NetworkPoolIp} objects returning the final result save status.
	 * @param poolIpRecords the Pool Host Records we wish to persist changes to
	 * @return the success state of the bulk save request
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkPoolIp> poolIpRecords);
}
