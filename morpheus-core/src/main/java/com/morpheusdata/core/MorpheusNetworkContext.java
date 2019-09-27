package com.morpheusdata.core;

import com.morpheusdata.model.*;

import java.util.List;
import java.util.Map;

public interface MorpheusNetworkContext {

	void updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message);

	void removeMissingPools(Long poolServerId, List<NetworkPool> removeList);

	List<NetworkPool> getNetworkPoolsByNetworkPoolServer(NetworkPoolServer poolServer);

	List<NetworkPool> getNetworkPoolsByNetworkPoolServer(NetworkPoolServer poolServer, String property);

	List getNetworkPoolByNetworkPool(NetworkPool pool);

	List getModelProperties(NetworkPool pool, List<String> joinProperties);

	void removeMissingIps(NetworkPool pool, List removeList);

	void removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress);

	NetworkPoolServer getPoolServerByAccountIntegration(AccountIntegration integration);

	NetworkPoolServer getPoolServerById(Long id);

	List<NetworkDomain> getNetworkDomainByTypeAndRefId(String refType, Long refId);

	List<NetworkPool> getPools(NetworkPoolServer poolServer);

	NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord);

	NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord, Map opts);

	NetworkPoolIp save(NetworkPoolIp poolIp);

	NetworkPoolIp save(NetworkPoolIp poolIp, NetworkPool networkPool);

	NetworkPoolIp save(NetworkPoolIp poolIp, NetworkPool networkPool, Map opts);

	NetworkPoolIp getNetworkIp(NetworkPool networkPool, String assignedType, Long assignedId, Long subAssignedId);

	NetworkDomain getContainerNetworkDomain(Container container);

	String getComputeServerExternalFqdn(ComputeServer computeServer);

	String getContainerExternalIp(Container container);

	String getContainerExternalFqdn(Container container);

	NetworkPoolIp loadNetworkPoolIp(NetworkPool pool, String ipAddress);

	List getNetworkDomainByDomainAndRecordType(NetworkDomain domain, String recordType);

	void updateMatchedDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List updateList);

	void removeMissingDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List removeList);

	String acquireLock(String name, Map opts);

	String releaseLock(String name, Map opts);

	void createSyncedNetworkDomain(Long poolServerId, List addList);

	List<NetworkDomain> findNetworkDomainsByPoolServerAndExternalIdsOrNames(NetworkPoolServer poolServer, List externalIds, List nameList);

	List<NetworkPoolIp> getNetworkPoolIpsByNetworkPoolAndExternalIdOrIpAddress(NetworkPool pool, List externalIds, List ipAddresses);

	void saveAllNetworkDomains(List<NetworkDomain> domainsToSave);

	void removeMissingZones(Long poolServerId, List removeList);

	List getNetworkDomainByOwner(Account account);

	NetworkDomainRecord getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId);

	void deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord);

	NetworkDomain getServerNetworkDomain(ComputeServer computeServer);

	NetworkPool save(NetworkPool networkPool);

	NetworkPoolRange save(NetworkPoolRange networkPoolRange);

	NetworkDomainRecord save(NetworkDomainRecord domainRecord);

	void saveAll(List<NetworkDomainRecord> domainRecords);

	void save(NetworkPool networkPool, List<NetworkPoolRange> ranges);

	Map<String, NetworkPool> findNetworkPoolsByPoolServerAndExternalIds(NetworkPool pool, List externalIds);

	Map<String, NetworkDomainRecord> findNetworkDomainRecordByNetworkDomainAndTypeAndExternalIds(NetworkDomain domain, String recordType, List externalIds);
}
