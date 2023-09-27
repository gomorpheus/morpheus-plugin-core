package com.morpheusdata.core;

import com.morpheusdata.model.AccountCertificate;
import com.morpheusdata.model.projection.AccountCertificateIdentityProjection;
import io.reactivex.rxjava3.core.Observable;

/**
 * Morpheus context methods for {@link AccountCertificate} operations
 */
public interface MorpheusAccountCertificateService extends MorpheusDataService<AccountCertificate,AccountCertificateIdentityProjection> {
	Observable<AccountCertificateIdentityProjection> listIdentityProjections(Long integrationId);
}
