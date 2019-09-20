package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;
import java.util.Map;

public interface MorpheusNetworkContext {

	void updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message);

	void addMissingPools(Long poolServerId, List chunkedAddList);

	void updateMatchedPools(Long poolServerId, List chunkedUpdateList);

	void removeMissingPools(Long poolServerId, List<NetworkPool> removeList);

	List<NetworkPool> getNetworkPoolByPoolServer(NetworkPoolServer poolServer);

	List<NetworkPool> getNetworkPoolByPoolServer(NetworkPoolServer poolServer, String joinProperty);

	List getNetworkPoolByNetworkPool(NetworkPool pool);

	void addMissingIps(NetworkPool pool, List addList);

	void updateMatchedIps(NetworkPool pool, List updateList);

	void removeMissingIps(NetworkPool pool, List removeList);

	void removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress);

	NetworkPoolServer getPoolServerByAccountIntegration(AccountIntegration integration);

	NetworkPoolServer getPoolServerById(Long id);

	List<NetworkDomain> getNetworkDomainByTypeAndRefId(String refType, Long refId);

	List<NetworkPool> getPools(NetworkPoolServer poolServer);

	NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord);

	NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord, Map opts);

	NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp);

	NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp, NetworkPool networkPool);

	NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp, NetworkPool networkPool, Map opts);

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

	void addMissingDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List addList);

	void addMissingZones(Long poolServerId, List addList);

	void updateMatchedZones(Long poolServerId, List updateList);

	void removeMissingZones(Long poolServerId, List removeList);

	List getNetworkDomainByOwner(Account account);

	NetworkDomainRecord getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId);

	void deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord);

	NetworkDomain getServerNetworkDomain(ComputeServer computeServer);
}
