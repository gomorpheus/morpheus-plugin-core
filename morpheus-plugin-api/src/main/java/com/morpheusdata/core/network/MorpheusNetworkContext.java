package com.morpheusdata.core.network;

import com.morpheusdata.core.DNSProvider;
import com.morpheusdata.core.IPAMProvider;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkDomainIdentityProjection;
import com.morpheusdata.model.projection.NetworkIdentityProjection;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Morpheus Context as it relates to network related operations. This context contains methods for querying things like network pools
 * network domains, and other network related objects. It also contains methods for applying updates ore creating new objects related to
 * networks. Typically this class is accessed via the primary {@link MorpheusContext}.
 *
 * @see MorpheusNetworkPoolContext
 * @see MorpheusNetworkDomainContext
 *
 * @author David Estes, Eric Helgeson
 */
public interface MorpheusNetworkContext {


	/**
	 * Returns the NetworkPoolContext used for performing updates or queries on {@link NetworkPool} related assets within Morpheus.
	 * Typically this would be called by a {@link DNSProvider} or {@link IPAMProvider}.
	 * @return An instance of the Network Pool Context to be used for calls by various network providers
	 */
	MorpheusNetworkPoolContext getPool();

	/**
	 * Returns the NetworkDomainContext used for performing updates/queries on {@link NetworkDomain} related assets
	 * within Morpheus. Most useful when implementing DNS related services.
	 * @return An instance of the Network Domain Context to be used for calls by various network providers
	 */
	MorpheusNetworkDomainContext getDomain();

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
	 * Lists all {@link Network} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link Network} objects from.
	 * @return an RxJava Observable stream of {@link Network} to be subscribed to.
	 */
	Observable<NetworkIdentityProjection> listById(Collection<Long> ids);


	//General Network Methods
	Single<Void> removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress);

	Single<NetworkPoolServer> getPoolServerByAccountIntegration(AccountIntegration integration);

	Single<NetworkPoolServer> getPoolServerById(Long id);

	Single<List<NetworkDomain>> getNetworkDomainByTypeAndRefId(String refType, Long refId);

	Single<NetworkPoolIp> getNetworkIp(NetworkPool networkPool, String assignedType, Long assignedId, Long subAssignedId);

	Single<NetworkDomain> getContainerNetworkDomain(Container container);

	Single<String> getComputeServerExternalFqdn(ComputeServer computeServer);

	Single<String> getContainerExternalIp(Container container);

	Single<String> getContainerExternalFqdn(Container container);

	Single<NetworkPoolIp> loadNetworkPoolIp(NetworkPool pool, String ipAddress);

	Single<String> acquireLock(String name, Map opts);

	Single<Boolean> releaseLock(String name, Map opts);

	Single<NetworkDomainRecord> getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId);

	Single<Void> deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord);

	Single<NetworkDomain> getServerNetworkDomain(ComputeServer computeServer);




	Single<NetworkPool> save(NetworkPool networkPool);

	Single<NetworkPoolRange> save(NetworkPoolRange networkPoolRange);




	Single<Void> save(NetworkPool networkPool, List<NetworkPoolRange> ranges);

	Single<Map<String, NetworkPool>> findNetworkPoolsByPoolServerAndExternalIds(NetworkPoolServer pool, List externalIds);
}
