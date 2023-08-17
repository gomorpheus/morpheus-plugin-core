package com.morpheusdata.core.providers;

import com.morpheusdata.model.*;
import com.morpheusdata.response.*;


/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface CloudNativeProvisioningProvider extends ResourceProvisioningProvider {


	/**
	 * Issues the remote calls necessary top stop an instance element from running.
	 * @param instance the Workload we want to shut down
	 * @return Response from API
	 */
	ServiceResponse stopInstance(Instance instance);

	/**
	 * Issues the remote calls necessary to start an instance running.
	 * @param instance the Workload we want to start up.
	 * @return Response from API
	 */
	ServiceResponse startInstance(Instance instance);




}
