package com.morpheusdata.core.network;

import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.NetworkDomain;
import com.morpheusdata.model.NetworkDomainRecord;
import com.morpheusdata.model.projection.NetworkDomainIdentityProjection;
import com.morpheusdata.model.projection.NetworkDomainRecordIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.NetworkDomainRecord} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkContext} and
 * finally via the {@link MorpheusNetworkDomainContext} traversal.
 * Network Domain Records are Zone Records entities within a DNS Zone. These are create/destroyed based on provisioning
 * integrations as well as syncing with DNS Integration types.
 *
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getDomain().getRecord()
 * }</pre>
 *
 * @see MorpheusNetworkDomainContext
 * @author David Estes
 */
public interface MorpheusNetworkDomainRecordContext {
	/**
	 * Lists all network domain record projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link NetworkDomainRecord} object for sync matching.
	 * @param networkDomain the {@link NetworkDomain} identity scope for listing all zone records
	 * @param recordType the Record Type (i.e. A, AAAA, TXT, CNAME, MX , PTR) to list records by.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkDomainRecordIdentityProjection> listIdentityProjections(NetworkDomainIdentityProjection networkDomain, String recordType);

	/**
	 * Lists all {@link NetworkDomainRecord} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link NetworkDomainRecord} objects from.
	 * @return an RxJava Observable stream of {@link NetworkDomainRecord} to be subscribed to.
	 */
	Observable<NetworkDomainRecord> listById(Collection<Long> ids);

	/**
	 * Removes {@link NetworkDomainRecord} objects from a DNS Domain in bulk. This is typically used when syncing with
	 * the DNS Integration.
	 * @param domain The Domain scope for the records being deleted. This method is scoped to the Zone for safety.
	 * @param removeList the list of Zone Records that are to be deleted.
	 * @return the success state Observable of the operation. make sure you subscribe.
	 */
	Single<Boolean>  remove(NetworkDomainIdentityProjection domain, List<NetworkDomainRecordIdentityProjection> removeList);

	/**
	 * Creates a single {@link NetworkDomain} object returning the final result object if any changes occurred during save.
	 * @param domainRecord the Domain Zone Record we wish to persist changes to
	 * @return the resultant Domain Object containing any additional metadata that may have been applied
	 */
	Single<NetworkDomainRecord> create(NetworkDomainRecord domainRecord);

	/**
	 * Creates new Network Domain Records from cache / sync implementations
	 * This ensures the owner of the zone record is correct upon creation.
	 * @param domain The {NetworkDomain} we are bulk creating zone records into.
	 * @param addList List of new {@link NetworkDomainRecord} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(NetworkDomainIdentityProjection domain, List<NetworkDomainRecord> addList);

	/**
	 * Saves a single {@link NetworkDomain} object returning the final result object if any changes occurred during save.
	 * @param domainRecord the Domain Zone Record we wish to persist changes to
	 * @return the resultant Domain Object containing any additional metadata that may have been applied
	 */
	Single<NetworkDomainRecord> save(NetworkDomainRecord domainRecord);

	/**
	 * Saves a list of {@link NetworkDomain} objects returning the final result save status.
	 * @param domainRecords the Domain Zone Records we wish to persist changes to
	 * @return the success state of the bulk save request
	 */
	Single<Boolean> save(List<NetworkDomainRecord> domainRecords);


}
