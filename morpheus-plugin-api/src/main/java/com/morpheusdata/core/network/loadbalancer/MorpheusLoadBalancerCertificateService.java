package com.morpheusdata.core.network.loadbalancer;

import com.morpheusdata.model.AccountCertificate;
import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

public interface MorpheusLoadBalancerCertificateService {
	/**
	 * Get a list of {@link com.morpheusdata.model.ReferenceData} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
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
}
