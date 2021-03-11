package com.morpheusdata

import com.morpheusdata.core.MorpheusNetworkContext
import com.morpheusdata.model.Account
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkDomainRecord
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolRange
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.model.projection.NetworkDomainSyncProjection
import com.morpheusdata.model.projection.NetworkPoolSyncProjection
import io.reactivex.Observable
import io.reactivex.Single

class MorpheusNetworkContextImpl implements MorpheusNetworkContext {
    @Override
	Single<Void> updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message) {

    }


	@Override
	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServerJoin(NetworkPoolServer poolServer, String joinProperty) {
		return null
	}

	@Override
	Single<List> getModelProperties(NetworkPool pool, List<String> joinProperties) {
		return null
	}

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
	@Override
	Single<Boolean> removeMissingPools(Long poolServerId, List<NetworkPoolSyncProjection> removeList) {
		return null
	}

	@Override
	Single<Void> removeMissingIps(NetworkPool pool, List removeList) {

	}

	@Override
	Single<Void> removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress) {

	}

	@Override
	Single<NetworkPoolServer> getPoolServerByAccountIntegration(AccountIntegration integration) {
		return null
	}

	@Override
	Single<NetworkPoolServer> getPoolServerById(Long id) {
		return null
	}

	@Override
	Single<List<NetworkDomain>> getNetworkDomainByTypeAndRefId(String refType, Long refId) {
		return null
	}

	@Override
	Single<List<NetworkPool>> getPools(NetworkPoolServer poolServer) {
		return null
	}

    @Override
	Single<NetworkDomainRecord> saveDomainRecord(NetworkDomainRecord domainRecord) {
        return null
    }

	@Override
	Single<NetworkDomainRecord> saveDomainRecord(NetworkDomainRecord domainRecord, Map opts) {
		return null
	}

	@Override
	Single<NetworkPoolIp> save(NetworkPoolIp poolIp) {
		return null
	}

	@Override
	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool) {
		return null
	}

	@Override
	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool, Map opts) {
		return null
	}

	@Override
	Single<NetworkPoolIp> getNetworkIp(NetworkPool networkPool, String assignedType, Long assignedId, Long subAssignedId) {
		return null
	}

	@Override
	Single<NetworkDomain> getContainerNetworkDomain(Container container) {
		return null
	}

	@Override
	Single<String> getComputeServerExternalFqdn(ComputeServer computeServer) {
		return null
	}

	@Override
	Single<String> getContainerExternalIp(Container container) {
		return null
	}

	@Override
	Single<String> getContainerExternalFqdn(Container container) {
		return null
	}

	@Override
	Single<NetworkPoolIp> loadNetworkPoolIp(NetworkPool pool, String ipAddress) {
		return null
	}

	@Override
	Single<List> getNetworkDomainByDomainAndRecordType(NetworkDomain domain, String recordType) {
		return null
	}

	@Override
	Single<Void> removeMissingDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List removeList) {

	}

	@Override
	Single<String> acquireLock(String name, Map opts) {
		return null
	}

	@Override
	Single<String> releaseLock(String name, Map opts) {
		return null
	}

	@Override
	Single<Boolean> createSyncedNetworkDomain(Long poolServerId, List<NetworkDomain> addList) {
		return Single.just(true)
	}

	@Override
	Single<List<NetworkPoolIp>> getNetworkPoolIpsByNetworkPoolAndExternalIdOrIpAddress(NetworkPool pool, List externalIds, List ipAddresses) {
		return null
	}

	@Override
	Single<Boolean> saveAllNetworkDomains(List<NetworkDomain> domainsToSave) {
		return Single.just(true)
	}

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
	@Override
	Single<Boolean> removeMissingNetworkDomains(Long integrationId, List<NetworkDomainSyncProjection> removeList) {

	}

	@Override
	Single<NetworkDomainRecord> getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId) {
		return null
	}

	@Override
	Single<Void> deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord) {

	}

	@Override
	Single<NetworkDomain> getServerNetworkDomain(ComputeServer computeServer) {
		return null
	}

	@Override
	Single<NetworkPool> save(NetworkPool networkPool) {
		return null
	}

	@Override
	Single<NetworkPoolRange> save(NetworkPoolRange networkPoolRange) {
		return null
	}

	@Override
	Single<NetworkDomainRecord> save(NetworkDomainRecord domainRecord) {
		return null
	}

	@Override
	Single<Void> saveAll(List<NetworkDomainRecord> domainRecords) {
		return null
	}

	@Override
	Single<Void> save(NetworkPool networkPool, List<NetworkPoolRange> ranges) {
		return null
	}

	@Override
	Single<Map<String, NetworkPool>> findNetworkPoolsByPoolServerAndExternalIds(NetworkPoolServer pool, List externalIds) {
		return null
	}

	@Override
	Single<Map<String, NetworkDomainRecord>> findNetworkDomainRecordByNetworkDomainAndTypeAndExternalIds(NetworkDomain domain, String recordType, List externalIds) {
		return null
	}

	@Override
	Observable<NetworkDomainSyncProjection> listNetworkDomainSyncMatch(Long accountIntegrationId) {
		return null
	}

	@Override
	Observable<NetworkPoolSyncProjection> listNetworkPoolSyncMatch(Long poolServerId) {
		return null
	}

	/**
	 * Lists all {@link NetworkDomain} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkDomain} objects from.
	 * @return an RxJava Observable stream of {@link NetworkDomain} to be subscribed to.
	 */
	@Override
	Observable<NetworkDomain> listNetworkDomainsById(Collection<Long> ids) {
		return null
	}

	/**
	 * Lists all {@link NetworkPool} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkPool} objects from.
	 * @return an RxJava Observable stream of {@link NetworkPool} to be subscribed to.
	 */
	Observable<NetworkPool> listNetworkPoolsById(Collection<Long> ids) {
		return null
	}
}
