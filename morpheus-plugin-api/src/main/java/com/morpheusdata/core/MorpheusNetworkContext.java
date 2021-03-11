package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkDomainSyncProjection;
import com.morpheusdata.model.projection.NetworkPoolSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusNetworkContext {

	Single<Void> updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message);

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
	Single<Boolean> removeMissingPools(Long poolServerId, List<NetworkPoolSyncProjection> removeList);


	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServerJoin(NetworkPoolServer poolServer, String joinProperty);

	Single<List> getModelProperties(NetworkPool pool, List<String> joinProperties);

	Single<Void> removeMissingIps(NetworkPool pool, List removeList);

	Single<Void> removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress);

	Single<NetworkPoolServer> getPoolServerByAccountIntegration(AccountIntegration integration);

	Single<NetworkPoolServer> getPoolServerById(Long id);

	Single<List<NetworkDomain>> getNetworkDomainByTypeAndRefId(String refType, Long refId);

	Single<List<NetworkPool>> getPools(NetworkPoolServer poolServer);

	Single<NetworkDomainRecord> saveDomainRecord(NetworkDomainRecord domainRecord);

	Single<NetworkDomainRecord> saveDomainRecord(NetworkDomainRecord domainRecord, Map opts);

	Single<NetworkPoolIp> save(NetworkPoolIp poolIp);

	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool);

	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool, Map opts);

	Single<NetworkPoolIp> getNetworkIp(NetworkPool networkPool, String assignedType, Long assignedId, Long subAssignedId);

	Single<NetworkDomain> getContainerNetworkDomain(Container container);

	Single<String> getComputeServerExternalFqdn(ComputeServer computeServer);

	Single<String> getContainerExternalIp(Container container);

	Single<String> getContainerExternalFqdn(Container container);

	Single<NetworkPoolIp> loadNetworkPoolIp(NetworkPool pool, String ipAddress);

	Single<List> getNetworkDomainByDomainAndRecordType(NetworkDomain domain, String recordType);

	Single<Void> removeMissingDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List removeList);

	Single<String> acquireLock(String name, Map opts);

	Single<Boolean> releaseLock(String name, Map opts);

	/**
	 * Creates new Network Domains from cache / sync implementations
	 * This ensures the refType and refId match the poolServer as well as the owner default
	 * @param poolServerId The id of the {@link NetworkPoolServer} we are saving into
	 * @param addList List of new {@link NetworkDomain} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> createSyncedNetworkDomain(Long poolServerId, List<NetworkDomain> addList);

	/**
	 * Removes Missing Network Domains on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.network.removeMissingNetworkDomains(poolServer.integration.id, removeItems).blockingGet()
	 * }</pre>
	 * @param integrationId The integration id of the integration syncing domains
	 * @param removeList a list of domain projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> removeMissingNetworkDomains(Long integrationId, List<NetworkDomainSyncProjection> removeList);


	Single<List<NetworkPoolIp>> getNetworkPoolIpsByNetworkPoolAndExternalIdOrIpAddress(NetworkPool pool, List externalIds, List ipAddresses);

	Single<Boolean> saveAllNetworkDomains(List<NetworkDomain> domainsToSave);


	/**
	 * Lists all network domain projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link NetworkDomain} object for sync matching.
	 * @param accountIntegrationId
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkDomainSyncProjection> listNetworkDomainSyncMatch(Long accountIntegrationId);

	/**
	 * Lists all network pool projection objects for a specified pool server id aka {@link NetworkPoolServer}.
	 * The projection is a subset of the properties on a full {@link NetworkPool} object for sync matching.
	 * @param poolServerId
	 * @return an RxJava Observable stream of projection objects
	 */
	Observable<NetworkPoolSyncProjection>  listNetworkPoolSyncMatch(Long poolServerId);

	/**
	 * Lists all {@link NetworkDomain} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkDomain} objects from.
	 * @return an RxJava Observable stream of {@link NetworkDomain} to be subscribed to.
	 */
	Observable<NetworkDomain> listNetworkDomainsById(Collection<Long> ids);

	/**
	 * Lists all {@link NetworkPool} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkPool} objects from.
	 * @return an RxJava Observable stream of {@link NetworkPool} to be subscribed to.
	 */
	Observable<NetworkPool> listNetworkPoolsById(Collection<Long> ids);

	Single<NetworkDomainRecord> getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId);

	Single<Void> deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord);

	Single<NetworkDomain> getServerNetworkDomain(ComputeServer computeServer);

	Single<NetworkPool> save(NetworkPool networkPool);

	Single<NetworkPoolRange> save(NetworkPoolRange networkPoolRange);

	Single<NetworkDomainRecord> save(NetworkDomainRecord domainRecord);

	Single<Void> saveAll(List<NetworkDomainRecord> domainRecords);

	Single<Void> save(NetworkPool networkPool, List<NetworkPoolRange> ranges);

	Single<Map<String, NetworkPool>> findNetworkPoolsByPoolServerAndExternalIds(NetworkPoolServer pool, List externalIds);

	Single<Map<String, NetworkDomainRecord>> findNetworkDomainRecordByNetworkDomainAndTypeAndExternalIds(NetworkDomain domain, String recordType, List externalIds);
}
