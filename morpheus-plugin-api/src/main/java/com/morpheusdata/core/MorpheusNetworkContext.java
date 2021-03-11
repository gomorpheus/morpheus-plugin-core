package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.NetworkDomainSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusNetworkContext {

	Single<Void> updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message);

	Single<Void> removeMissingPools(Long poolServerId, List<NetworkPool> removeList);

	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServer(NetworkPoolServer poolServer);

	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServer(NetworkPoolServer poolServer, String property);
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

	Single<Void> createSyncedNetworkDomain(Long poolServerId, List addList);

	Single<List<NetworkDomain>> findNetworkDomainsByPoolServerAndExternalIdsOrNames(NetworkPoolServer poolServer, List externalIds, List nameList);

	Single<List<NetworkPoolIp>> getNetworkPoolIpsByNetworkPoolAndExternalIdOrIpAddress(NetworkPool pool, List externalIds, List ipAddresses);

	Single<Void> saveAllNetworkDomains(List<NetworkDomain> domainsToSave);

	Single<Void> removeMissingZones(Long integrationId, List<NetworkDomainSyncProjection> removeList);

	Single<List> getNetworkDomainByOwner(Account account);

	Observable<NetworkDomainSyncProjection> listNetworkDomainSyncMatch(Long accountIntegrationId);

	Observable<NetworkDomain> listNetworkDomainsById(Collection<Long> ids);

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
