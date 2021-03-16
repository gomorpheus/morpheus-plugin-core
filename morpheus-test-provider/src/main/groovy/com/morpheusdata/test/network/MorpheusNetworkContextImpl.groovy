package com.morpheusdata.test.network

import com.morpheusdata.core.network.MorpheusNetworkContext
import com.morpheusdata.core.network.MorpheusNetworkDomainContext
import com.morpheusdata.core.network.MorpheusNetworkPoolContext
import com.morpheusdata.core.network.MorpheusNetworkPoolIpContext
import com.morpheusdata.core.network.MorpheusNetworkPoolRangeContext
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkDomainRecord
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolRange
import com.morpheusdata.model.NetworkPoolServer
import groovy.transform.AutoImplement
import io.reactivex.Single

@AutoImplement
class MorpheusNetworkContextImpl implements MorpheusNetworkContext {

	protected MorpheusNetworkPoolContext poolContext;
	protected MorpheusNetworkPoolIpContext poolIpContext;
	protected MorpheusNetworkPoolRangeContext poolRangeContext;

	MorpheusNetworkContextImpl() {
		poolContext = new MorpheusNetworkPoolContextImpl()
	}

	/**
	 * Returns the NetworkPoolContext used for performing updates or queries on {@link NetworkPool} related assets within Morpheus.
	 * Typically this would be called by a {@link com.morpheusdata.core.DNSProvider} or {@link com.morpheusdata.core.IPAMProvider}.
	 * @return An Instance of the Network Pool Context to be used for calls by various network providers
	 */
	@Override
	MorpheusNetworkPoolContext getPool() {
		return poolContext
	}

	/**
	 * Returns the NetworkDomainContext used for performing updates/queries on {@link NetworkDomain} related assets
	 * within Morpheus. Most useful when implementing DNS related services.
	 * @return An instance of the Network Domain Context to be used for calls by various network providers
	 */
	@Override
	MorpheusNetworkDomainContext getDomain() {
		return null
	}

	@Override
	Single<Void> removePoolIp(NetworkPool networkPool, NetworkPoolIp ipAddress) {
		return null
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
	Single<String> acquireLock(String name, Map opts) {
		return null
	}

	@Override
	Single<Boolean> releaseLock(String name, Map opts) {
		return null
	}

	@Override
	Single<NetworkDomainRecord> getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId) {
		return null
	}

	@Override
	Single<Void> deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord) {
		return null
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
	Single<Void> save(NetworkPool networkPool, List<NetworkPoolRange> ranges) {
		return null
	}

	@Override
	Single<Map<String, NetworkPool>> findNetworkPoolsByPoolServerAndExternalIds(NetworkPoolServer pool, List externalIds) {
		return null
	}

}
