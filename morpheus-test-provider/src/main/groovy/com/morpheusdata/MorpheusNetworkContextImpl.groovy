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
import io.reactivex.Observable
import io.reactivex.Single

class MorpheusNetworkContextImpl implements MorpheusNetworkContext {
    @Override
	Single<Void> updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message) {

    }

	@Override
	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServer(NetworkPoolServer poolServer) {
		return null
	}

	@Override
	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServer(NetworkPoolServer poolServer, String property) {
		return null
	}

	@Override
	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServerJoin(NetworkPoolServer poolServer, String joinProperty) {
		return null
	}

	@Override
	Single<List> getModelProperties(NetworkPool pool, List<String> joinProperties) {
		return null
	}

	@Override
	Single<Void> removeMissingPools(Long poolServerId, List removeList) {

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
	Observable<NetworkDomain> createSyncedNetworkDomain(Long poolServerId, List<NetworkDomain> addList) {
		return null
	}

	@Override
	Single<List<NetworkPoolIp>> getNetworkPoolIpsByNetworkPoolAndExternalIdOrIpAddress(NetworkPool pool, List externalIds, List ipAddresses) {
		return null
	}

	@Override
	Single<Void> saveAllNetworkDomains(List<NetworkDomain> domainsToSave) {
		return null
	}

	@Override
	Single<Void> removeMissingZones(Long poolServerId, List removeList) {

	}

	@Override
	Single<List> getNetworkDomainByOwner(Account account) {

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
	Observable<NetworkDomain> listNetworkDomainsById(Collection<Long> ids) {
		return null
	}
}
