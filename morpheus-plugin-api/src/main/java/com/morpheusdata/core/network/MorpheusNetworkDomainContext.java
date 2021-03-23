package com.morpheusdata.core.network;

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.NetworkDomain;
import com.morpheusdata.model.NetworkDomainRecord;
import com.morpheusdata.model.projection.NetworkDomainIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.NetworkDomain} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkContext}
 * Network Domains are Domain entities as it relates to DNS and Windows Domain Join Behavior. It contains information
 * regarding FQDN as well as Domain join rules and even credentials when necessary.
 *
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getDomain()
 * }</pre>
 *
 * @see MorpheusNetworkContext
 * @author David Estes
 */
public interface MorpheusNetworkDomainContext {

	/**
	 * Returns the context for interacting with {@link NetworkDomainRecord} objects
	 * @return the domain record context for DNS Sync and management
	 */
	MorpheusNetworkDomainRecordContext getRecord();

	/**
	 * Lists all network domain projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link NetworkDomain} object for sync matching.
	 * @param accountIntegrationId the {@link AccountIntegration} identifier associated to the domains to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkDomainIdentityProjection> listIdentityProjections(Long accountIntegrationId);

	/**
	 * Lists all {@link NetworkDomain} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkDomain} objects from.
	 * @return an RxJava Observable stream of {@link NetworkDomain} to be subscribed to.
	 */
	Observable<NetworkDomain> listById(Collection<Long> ids);

	/**
	 * Gets a Network Domain by the id
	 * @param id the Identifier of the {@link NetworkDomain} that is being requested.
	 * @return an instance of the NetworkDomain with all metadata pertaining to it.
	 */
	Single<NetworkDomain> get(Long id);

	/**
	 * Gets the network domain assigned to a server. This is relevant when doing IPAM/DNS operations.
	 * It looks for the primary domain object by checking the interfaces and any domain overrides on the server.
	 * This method may go away as a server in theory can belong to many domains.
	 *
	 * @param computeServerId the Server Object Identifier to check against
	 * @return a NetworkDomain instance of the associated domain. if it exists
	 */
	Single<NetworkDomain> findByServer(Long computeServerId);

	/**
	 * Gets the network domain assigned to a workload/container. This is relevant when doing IPAM/DNS operations.
	 * It looks for the primary domain object by checking the interfaces and any domain overrides on the server.
	 * This method may go away as a server in theory can belong to many domains.
	 *
	 * @param workloadId the Workload/Container Object Identifier to check against
	 * @return a NetworkDomain instance of the associated domain. if it exists
	 */
	Single<NetworkDomain> findByWorkload(Long workloadId);

	/**
	 * Removes Missing Network Domains on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getNetwork().removeMissingNetworkDomains(poolServer.integration.id, removeItems).blockingGet()
	 * }</pre>
	 * @param integrationId The integration id of the integration syncing domains
	 * @param removeList a list of domain projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(Long integrationId, List<NetworkDomainIdentityProjection> removeList);

	/**
	 * Creates new Network Domains from cache / sync implementations
	 * This ensures the refType and refId match the poolServer as well as the owner default
	 * @param integrationId The id of the {@link AccountIntegration} we are saving into
	 * @param addList List of new {@link NetworkDomain} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(Long integrationId, List<NetworkDomain> addList);

	/**
	 * Saves a list of {@link NetworkDomain} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param domainsToSave a List of Domain objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<NetworkDomain> domainsToSave);

	/**
	 * Saves a {@link NetworkDomain} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param domainToSave a Domain Object that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(NetworkDomain domainToSave);

}
