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

package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.AccountCertificate;
import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MorpheusLoadBalancerCertificateService {
	/**
	 * Get a list of {@link com.morpheusdata.model.ReferenceData} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @param objCategory an additional category used for sync comparisons
	 * @return Observable stream of sync projection
	 */
	Observable<ReferenceDataSyncProjection> listSyncProjections(Long loadBalancerId, String objCategory);

	/**
	 * Get a list of ReferenceData (certificates) objects from a list of projection ids
	 *
	 * @param ids certificateid
	 * @return Observable stream of partition
	 */
	Observable<ReferenceData> listById(Collection<Long> ids);

	/**
	 * Save updates to existing certificate
	 *
	 * @param certificates updated certificate
	 * @return success
	 */
	Single<Boolean> save(List<ReferenceData> certificates);

	/**
	 * Create new certificate in Morpheus
	 *
	 * @param certificates new ReferenceData (certificate) to persist
	 * @return success
	 */
	Single<Boolean> create(List<ReferenceData> certificates);

	/**
	 * Remove persisted certificate from Morpheus
	 *
	 * @param certificates Images to delete
	 * @return success
	 */
	Single<Boolean> remove(List<ReferenceDataSyncProjection> certificates);

	Observable<AccountCertificate> listAccountCertificates(Long loadBalancerId);

	Single<Optional<AccountCertificate>> getAccountCertificateById(Long certificateId);

	Single<ReferenceData> createCertInstallToken(AccountCertificate cert, String name, String category);

	Single<Boolean> expireCertInstallToken(ReferenceData token);

	/**
	 * This method will return a map containing details about an account certificate.  The source of the details may
	 * come from morpheus or another existing certificate integration if applicable
	 * @param cert {@link AccountCertificate}
	 * @return a Map containing details about an account certificate
	 */
	Single<Map> getCertificateContent(AccountCertificate cert);

	String getSslInstallTokenName();
}
