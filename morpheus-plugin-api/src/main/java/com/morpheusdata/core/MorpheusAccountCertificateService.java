package com.morpheusdata.core;

import com.morpheusdata.model.AccountCertificate;
import com.morpheusdata.model.projection.AccountCertificateIdentityProjection;
import io.reactivex.Observable;

/**
 * Morpheus context methods for {@link AccountCertificate} operations
 */
public interface MorpheusAccountCertificateService extends MorpheusDataService<AccountCertificate> {
	Observable<AccountCertificateIdentityProjection> listIdentityProjections(Long integrationId);
}
