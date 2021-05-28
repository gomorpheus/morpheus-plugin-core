package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.response.WorkloadResponse;

import java.util.Collection;
import java.util.Map;

/**
 * Provides methods for fetching and creating required servers for instance provisioning. Should be implemented by
 * ProvisioningProviders that may need to create servers manually for new instances
 *
 * @since 0.9.0
 * @author Bob Whiton
 */
public interface ProvisionInstanceServers {

	/**
	 * Returns the servers that should be used to create the containers for the given instance. If needed, ComputeServers
	 * should be created and then returned.
	 * @param instance being created
	 * @param provisionType being provisioned
	 * @param opts additional options
	 * @return Response from API
	 */
	Collection<ComputeServer> getInstanceServers(Instance instance, ProvisionType provisionType, Map opts);
}
