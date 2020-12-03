package com.morpheusdata.core.network;

import com.morpheusdata.core.DNSProvider;
import com.morpheusdata.core.IPAMProvider;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.*;
import io.reactivex.Completable;
import io.reactivex.Single;

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


	Single<Network> save(Network networkPool);

	Single<Network> save(Network networkPool, Boolean flush);

	Single<ComputeServerInterface> save(ComputeServerInterface serverInterface);

	Single<ComputeServerInterface> save(ComputeServerInterface serverInterface, Boolean flush);

	Single<NetworkPool> save(NetworkPool networkPool);

	Single<NetworkPoolRange> save(NetworkPoolRange networkPoolRange);


	Single<NetworkPoolIp> save(NetworkPoolIp poolIp);

	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool);

	Single<NetworkPoolIp> save(NetworkPoolIp poolIp, NetworkPool networkPool, Map opts);

	Single<Void> save(NetworkPool networkPool, List<NetworkPoolRange> ranges);

	Single<Map<String, NetworkPool>> findNetworkPoolsByPoolServerAndExternalIds(NetworkPoolServer pool, List externalIds);

	Single<Map<String, NetworkDomainRecord>> findNetworkDomainRecordByNetworkDomainAndTypeAndExternalIds(NetworkDomain domain, String recordType, List externalIds);

	Single<Boolean> serverAddToInterfaces(ComputeServer server, ComputeServerInterface serverInterface);

	Single<Boolean> removeServerInterface(Network network, ComputeServer server, Boolean flush);

	Single<Network> findNetworkTypeByCode(String type);

	Single<NetworkDomain> getNetworkDomainById(Long id);
}
