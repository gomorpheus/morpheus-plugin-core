package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.AccountCertificate;
import com.morpheusdata.model.projection.AccountCertificateIdentityProjection;

public interface MorpheusSynchronousAccountCertificateService extends MorpheusSynchronousDataService<AccountCertificate, AccountCertificateIdentityProjection>, MorpheusSynchronousIdentityService<AccountCertificateIdentityProjection> {
}
