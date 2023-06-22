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
}
