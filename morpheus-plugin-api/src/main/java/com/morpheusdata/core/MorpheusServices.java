package com.morpheusdata.core;

import com.morpheusdata.core.admin.MorpheusSynchronousAdminService;
import com.morpheusdata.core.integration.MorpheusSynchronousIntegrationService;

public interface MorpheusServices {
	/**
	 * Returns the Integration context used for performing common operations on various integration types Morpheus
	 * has to offer.
	 * @return An instance of the Integration Context to bused for calls by various integration types
	 */
	MorpheusSynchronousIntegrationService getIntegration();

	/**
	 * Returns the {@link MorpheusSynchronousAdminService} which allows access to {@link MorpheusDataService} related services
	 * for administrative objects (i.e. Users,Accounts,etc) in a synchronous manner.
	 * @return an instance of MorpheusSynchronousAdminService
	 */
	MorpheusSynchronousAdminService getAdmin();
}
