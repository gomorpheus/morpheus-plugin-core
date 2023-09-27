package com.morpheusdata.core.synchronous.network.loadbalancer;

import com.morpheusdata.model.AccountCertificate;
import com.morpheusdata.model.ReferenceData;
import com.morpheusdata.model.projection.ReferenceDataSyncProjection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MorpheusSynchronousLoadBalancerCertificateService {

	/**
	 * Get a list of {@link com.morpheusdata.model.ReferenceData} projections based on NetworkLoadBalancer id
	 *
	 * @param loadBalancerId the id of the load balancer
	 * @param objCategory an additional category used for sync comparisons
	 * @return Observable stream of sync projection
	 */
	ReferenceDataSyncProjection listSyncProjections(Long loadBalancerId, String objCategory);

	/**
	 * Get a list of ReferenceData (certificates) objects from a list of projection ids
	 *
	 * @param ids certificateid
	 * @return Observable stream of partition
	 */
	ReferenceData listById(Collection<Long> ids);

	/**
	 * Save updates to existing certificate
	 *
	 * @param certificates updated certificate
	 * @return success
	 */
	Boolean save(List<ReferenceData> certificates);

	/**
	 * Create new certificate in Morpheus
	 *
	 * @param certificates new ReferenceData (certificate) to persist
	 * @return success
	 */
	Boolean create(List<ReferenceData> certificates);

	/**
	 * Remove persisted certificate from Morpheus
	 *
	 * @param certificates Images to delete
	 * @return success
	 */
	Boolean remove(List<ReferenceDataSyncProjection> certificates);

	AccountCertificate listAccountCertificates(Long loadBalancerId);

	AccountCertificate getAccountCertificateById(Long certificateId);

	ReferenceData createCertInstallToken(AccountCertificate cert, String name, String category);

	Boolean expireCertInstallToken(ReferenceData token);

	/**
	 * This method will return a map containing details about an account certificate.  The source of the details may
	 * come from morpheus or another existing certificate integration if applicable
	 * @param cert {@link AccountCertificate}
	 * @return a Map containing details about an account certificate
	 */
	Map getCertificateContent(AccountCertificate cert);

	String getSslInstallTokenName();
}
