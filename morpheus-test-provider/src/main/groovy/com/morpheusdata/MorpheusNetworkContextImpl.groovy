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
import com.morpheusdata.model.NetworkPoolServer

class MorpheusNetworkContextImpl implements MorpheusNetworkContext {
    @Override
    void updateNetworkPoolStatus(NetworkPoolServer poolServer, String status, String message) {

    }

	@Override
	void addMissingPools(Long poolServerId, List chunkedAddList) {

	}

	@Override
	void updateMatchedPools(Long poolServerId, List chunkedUpdateList) {

	}

	@Override
	void removeMissingPools(Long poolServerId, List removeList) {

	}

	@Override
	List<NetworkPool> getNetworkPoolByPoolServer(NetworkPoolServer poolServer) {
		return null
	}

	@Override
	void removeMissingPools(Long poolServerId, List removeList) {

	}

	@Override
	List getNetworkPoolByNetworkPool(NetworkPool pool) {
		return null
	}

	@Override
	void addMissingIps(NetworkPool pool, List addList) {

	}

	@Override
	void updateMatchedIps(NetworkPool pool, List updateList) {

	}

	@Override
	void removeMissingIps(NetworkPool pool, List removeList) {

	}

	@Override
	void removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress) {

	}

	@Override
	NetworkPoolServer getPoolServerByAccountIntegration(AccountIntegration integration) {
		return null
	}

	@Override
	NetworkPoolServer getPoolServerById(Long id) {
		return null
	}

	@Override
	List<NetworkDomain> getNetworkDomainByTypeAndRefId(String refType, Long refId) {
		return null
	}

	@Override
	List<NetworkPool> getPools(NetworkPoolServer poolServer) {
		return null
	}

    @Override
    NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord) {
        return null
    }

	@Override
	NetworkDomainRecord saveDomainRecord(NetworkDomainRecord domainRecord, Map opts) {
		return null
	}

	@Override
    NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp) {
        return null
    }

	@Override
	NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp, NetworkPool networkPool) {
		return null
	}

	@Override
	NetworkPoolIp saveNetworkPoolIp(NetworkPoolIp poolIp, NetworkPool networkPool, Map opts) {
		return null
	}

	@Override
	NetworkPoolIp getNetworkIp(NetworkPool networkPool, String assignedType, Long assignedId, Long subAssignedId) {
		return null
	}

	@Override
	NetworkDomain getContainerNetworkDomain(Container container) {
		return null
	}

	@Override
	String getComputeServerExternalFqdn(ComputeServer computeServer) {
		return null
	}

	@Override
	String getContainerExternalIp(Container container) {
		return null
	}

	@Override
	String getContainerExternalFqdn(Container container) {
		return null
	}

	@Override
	NetworkPoolIp loadNetworkPoolIp(NetworkPool pool, String ipAddress) {
		return null
	}

	@Override
	List getNetworkDomainByDomainAndRecordType(NetworkDomain domain, String recordType) {
		return null
	}

	@Override
	void updateMatchedDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List updateList) {

	}

	@Override
	void removeMissingDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List removeList) {

	}

	@Override
	String acquireLock(String name, Map opts) {
		return null
	}

	@Override
	String releaseLock(String name, Map opts) {
		return null
	}

	@Override
	void addMissingDomainRecords(Long poolServerId, NetworkDomain domain, String recordType, List addList) {

	}

	@Override
	void addMissingZones(Long poolServerId, List addList) {

	}

	@Override
	void updateMatchedZones(Long poolServerId, List updateList) {

	}

	@Override
	void removeMissingZones(Long poolServerId, List removeList) {

	}

	@Override
	List getNetworkDomainByOwner(Account account) {

	}

	@Override
	NetworkDomainRecord getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId) {
		return null
	}

	@Override
	void deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord) {

	}

	@Override
	NetworkDomain getServerNetworkDomain(ComputeServer computeServer) {
		return null
	}
}
