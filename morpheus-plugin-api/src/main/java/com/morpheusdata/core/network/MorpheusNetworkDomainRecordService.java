/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.network;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.NetworkDomain;
import com.morpheusdata.model.NetworkDomainRecord;
import com.morpheusdata.model.projection.NetworkDomainIdentityProjection;
import com.morpheusdata.model.projection.NetworkDomainRecordIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * This Context deals with interactions related to {@link com.morpheusdata.model.NetworkDomainRecord} objects. It can normally
 * be accessed via the primary {@link com.morpheusdata.core.MorpheusContext} via the {@link MorpheusNetworkService} and
 * finally via the {@link MorpheusNetworkDomainService} traversal.
 * Network Domain Records are Zone Records entities within a DNS Zone. These are create/destroyed based on provisioning
 * integrations as well as syncing with DNS Integration types.
 *
 * <p><strong>Examples:</strong></p>
 * <pre>{@code
 * morpheusContext.getNetwork().getDomain().getRecord()
 * }</pre>
 *
 * @see MorpheusNetworkDomainService
 * @since 0.8.0
 * @author David Estes
 */
public interface MorpheusNetworkDomainRecordService extends MorpheusDataService<NetworkDomainRecord, NetworkDomainRecordIdentityProjection>, MorpheusIdentityService<NetworkDomainRecordIdentityProjection> {
	/**
	 * Lists all network domain record projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link NetworkDomainRecord} object for sync matching.
	 * @param networkDomain the {@link NetworkDomain} identity scope for listing all zone records
	 * @param recordType the Record Type (i.e. A, AAAA, TXT, CNAME, MX , PTR) to list records by.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<NetworkDomainRecordIdentityProjection> listIdentityProjections(NetworkDomainIdentityProjection networkDomain, String recordType);

	/**
	 * Finds the DNS Record associated with the specified workloadId (aka containerId) within the specified {@link NetworkDomain}
	 * @param domainMatch the current domain we are querying against
	 * @param workloadId the id of the container/workload element associated to the record
	 * @return the DNS Zone record associated with the workload
	 */
	@Deprecated(since="0.15.4")
	Single<NetworkDomainRecord> findByNetworkDomainAndWorkloadId(NetworkDomainIdentityProjection domainMatch, Long workloadId);

	/**
	 * Finds the DNS Record associated with the specified serverId from {@link com.morpheusdata.model.ComputeServer} within the specified {@link NetworkDomain}
	 * @param domainMatch the current domain we are querying against
	 * @param serverId the id of the server element associated to the record
	 * @return the DNS Zone record associated with the server
	 */
	@Deprecated(since="0.15.4")
	Single<NetworkDomainRecord> findByNetworkDomainAndServerId(NetworkDomainIdentityProjection domainMatch, Long serverId);

	/**
	 * Removes {@link NetworkDomainRecord} objects from a DNS Domain in bulk. This is typically used when syncing with
	 * the DNS Integration.
	 * @param domain The Domain scope for the records being deleted. This method is scoped to the Zone for safety.
	 * @param removeList the list of Zone Records that are to be deleted.
	 * @return the success state Observable of the operation. make sure you subscribe.
	 */
	Single<Boolean>  remove(NetworkDomainIdentityProjection domain, List<NetworkDomainRecordIdentityProjection> removeList);

	/**
	 * Removes a {@link NetworkDomainRecord} object from a DNS Domain. This is often used when handling teardown implementations of DNS Providers.
	 * @param domain The Domain scope for the record being deleted. This method is scoped to the Zone for safety.
	 * @param removeRecord the Zone record that is to be deleted
	 * @return the success state Observable of the operation. make sure you subscribe.
	 */
	Single<Boolean>  remove(NetworkDomainIdentityProjection domain, NetworkDomainRecordIdentityProjection removeRecord);

	/**
	 * Creates new Network Domain Records from cache / sync implementations
	 * This ensures the owner of the zone record is correct upon creation.
	 * @param domain The {NetworkDomain} we are bulk creating zone records into.
	 * @param addList List of new {@link NetworkDomainRecord} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(NetworkDomainIdentityProjection domain, List<NetworkDomainRecord> addList);

	/**
	 * Saves a list of {@link NetworkDomain} objects returning the final result save status.
	 * @param domainRecords the Domain Zone Records we wish to persist changes to
	 * @return the success state of the bulk save request
	 * @deprecated use {@link #bulkSave } instead
	 */
	@Deprecated(since="0.15.4")
	Single<Boolean> save(List<NetworkDomainRecord> domainRecords);


}
