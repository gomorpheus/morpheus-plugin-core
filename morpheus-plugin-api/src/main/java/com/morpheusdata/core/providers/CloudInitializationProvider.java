package com.morpheusdata.core.providers;

import com.morpheusdata.model.Cloud;
import com.morpheusdata.response.ServiceResponse;

public interface CloudInitializationProvider {

	/**
	 * Initialize provider for a cloud
	 * @param cloud {@link Cloud } to initialize this provider for
	 * @return ServiceResponse
	 */
	ServiceResponse initializeProvider(Cloud cloud);

	/**
	 * Cleanup provider when a cloud is deleted
	 * @param cloud {@link Cloud } to cleanup this provider for
	 * @return ServiceResponse
	 */
	ServiceResponse deleteProvider(Cloud cloud);
}
