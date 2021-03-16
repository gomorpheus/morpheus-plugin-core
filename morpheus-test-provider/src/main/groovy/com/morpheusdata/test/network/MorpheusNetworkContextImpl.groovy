package com.morpheusdata.test.network

import com.morpheusdata.core.network.MorpheusNetworkContext
import com.morpheusdata.core.network.MorpheusNetworkDomainContext
import com.morpheusdata.core.network.MorpheusNetworkPoolContext
import com.morpheusdata.core.network.MorpheusNetworkPoolIpContext
import com.morpheusdata.core.network.MorpheusNetworkPoolRangeContext
import com.morpheusdata.model.AccountIntegration
import com.morpheusdata.model.Cloud
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Network
import com.morpheusdata.model.NetworkDomain
import com.morpheusdata.model.NetworkDomainRecord
import com.morpheusdata.model.NetworkPool
import com.morpheusdata.model.NetworkPoolIp
import com.morpheusdata.model.NetworkPoolRange
import com.morpheusdata.model.NetworkPoolServer
import com.morpheusdata.model.projection.NetworkIdentityProjection
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

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

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param poolServer the pool integration with which we want to update the status.
	 * @param status the status of the pool server (ok,syncing,error)
	 * @param message the status message for more details. typically only used when status is 'error'.
	 *
	 * @return a Completable for notification or subscription
	 */
	@Override
	Completable updateNetworkPoolServerStatus(NetworkPoolServer poolServer, AccountIntegration.Status status, String message) {
		return null
	}

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param poolServer the pool integration with which we want to update the status.
	 * @param status the status string of the pool server (ok,syncing,error)
	 *
	 * @return the on complete state
	 */
	@Override
	Completable updateNetworkPoolServerStatus(NetworkPoolServer poolServer, AccountIntegration.Status status) {
		return null
	}

	/**
	 * Lists all network projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param accountIntegration the {@link AccountIntegration} identifier associated to the networks to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Override
	Observable<NetworkIdentityProjection> listIdentityProjections(AccountIntegration accountIntegration) {
		return null
	}

	/**
	 * Lists all network projection objects for a specified cloud.
	 * The projection is a subset of the properties on a full {@link Network} object for sync matching.
	 * @param cloud the {@link Cloud} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	@Override
	Observable<NetworkIdentityProjection> listIdentityProjections(Cloud cloud) {
		return null
	}

	/**
	 * Lists all {@link Network} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link Network} objects from.
	 * @return an RxJava Observable stream of {@link Network} to be subscribed to.
	 */
	@Override
	Observable<NetworkIdentityProjection> listById(Collection<Long> ids) {
		return null
	}

	/**
	 * Removes Missing Networks on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetwork().remove(removeItems).blockingGet()
	 *}</pre>
	 * @param removeList a list of network projections to be removed
	 * @return a Single {@link java.util.Observable} returning the success status of the operation.
	 */
	@Override
	Single<Boolean> remove(List<NetworkIdentityProjection> removeList) {
		return null
	}

	/**
	 * Creates new Network Domains from cache / sync implementations
	 * This ensures the refType and refId match the poolServer as well as the owner default
	 * @param addList List of new {@link Network} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	@Override
	Single<Boolean> create(List<Network> addList) {
		return null
	}

	/**
	 * Saves a list of {@link NetworkDomain} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param networksToSave a List of Network objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	@Override
	Single<Boolean> save(List<Network> networksToSave) {
		return null
	}

	/**
	 * Saves a {@link Network} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param networkToSave a Network Object that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	@Override
	Single<Boolean> save(Network networkToSave) {
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
	Single<Map<String, NetworkPool>> findNetworkPoolsByPoolServerAndExternalIds(NetworkPoolServer pool, List externalIds) {
		return null
	}

}
