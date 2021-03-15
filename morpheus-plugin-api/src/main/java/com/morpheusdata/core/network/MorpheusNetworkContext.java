package com.morpheusdata.core.network;

import com.morpheusdata.core.DNSProvider;
import com.morpheusdata.core.IPAMProvider;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.List;
import java.util.Map;

/**
 * Morpheus Context as it relates to network related operations. This context contains methods for querying things like network pools
 * network domains, and other network related objects. It also contains methods for applying updates ore creating new objects related to
 * networks. Typically this class is accessed via the primary {@link MorpheusContext}.
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
	 * @param poolServer
	 * @param status
	 * @param message
	 * @return
	 */
	Single<Void> updateNetworkPoolServerStatus(NetworkPoolServer poolServer, String status, String message);

	Single<List<NetworkPool>> getNetworkPoolsByNetworkPoolServerJoin(NetworkPoolServer poolServer, String joinProperty);

	Single<List> getModelProperties(NetworkPool pool, List<String> joinProperties);

	Single<Void> removeMissingIps(NetworkPool pool, List removeList);

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

	Single<List<NetworkPoolIp>> getNetworkPoolIpsByNetworkPoolAndExternalIdOrIpAddress(NetworkPool pool, List externalIds, List ipAddresses);

	Single<NetworkDomainRecord> getNetworkDomainRecordByNetworkDomainAndContainerId(NetworkDomain domainMatch, Long containerId);

	Single<Void> deleteNetworkDomainAndRecord(NetworkDomain networkDomain, NetworkDomainRecord domainRecord);

	Single<NetworkDomain> getServerNetworkDomain(ComputeServer computeServer);




	Single<NetworkPool> save(NetworkPool networkPool);

	Single<NetworkPoolRange> save(NetworkPoolRange networkPoolRange);


	Single<NetworkPoolIp> save(NetworkPoolIp poolIp);

	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool);

	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool, Map opts);


	Single<Void> save(NetworkPool networkPool, List<NetworkPoolRange> ranges);

	Single<Map<String, NetworkPool>> findNetworkPoolsByPoolServerAndExternalIds(NetworkPoolServer pool, List externalIds);
}
