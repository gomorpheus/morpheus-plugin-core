package com.morpheusdata.core.providers;

import com.morpheusdata.model.provisioning.AppRequest;
import com.morpheusdata.model.provisioning.InstanceRequest;
import com.morpheusdata.model.*;
import com.morpheusdata.response.*;

import java.util.Map;

/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface ResourceProvisionProvider extends ProvisionProvider {

	/**
	 * Validate the provided provisioning options for an Instance. A return of success = false will halt the
	 * creation and display errors
	 * @param instance the Instance to validate
	 * @param opts options
	 * @return Response from API
	 */
	ServiceResponse validateInstance(Instance instance, Map opts);

	/**
	 * This method is a key entry point in provisioning an instance. Information associated with the passed Instance object is used to kick
	 * off the instance provision request
	 * @param instance the Instance object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the instance
	 * @param instanceRequest the InstanceRequest object containing the various configurations to update the instance
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return A ProvisionResponse object the should at least contain the response from the API
	 */
	ServiceResponse<ProvisionResponse> updateInstance(Instance instance, InstanceRequest instanceRequest, Map opts);

	/**
	 * This method is called before runInstance and provides an opportunity to perform action or obtain configuration
	 * that will be needed in runInstance.
	 * @param instance the Instance object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the instance
	 * @param instanceRequest the InstanceRequest object containing the various configurations that may be needed
	 *                        in running the Instance.
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return A PrepareInstanceResponse object that should contain the instance with any required updates that will then be saved
	 */
	ServiceResponse<PrepareInstanceResponse> prepareInstance(Instance instance, InstanceRequest instanceRequest, Map opts);

	/**
	 * This method is a key entry point in provisioning an instance. Information associated with the passed Instance object is used to kick
	 * off the instance provision request
	 * @param instance the Instance object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the instance
	 * @param instanceRequest the InstanceRequest object containing the various configurations that may be needed
	 *                        in running the instance
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return A ProvisionResponse object the should at least contain the response from the API
	 */
	ServiceResponse<ProvisionResponse> runInstance(Instance instance, InstanceRequest instanceRequest, Map opts);

	/**
	 * This is the key method called to destroy / remove an instance. This should make the remote calls necessary to remove any assets
	 * associated with the instance.
	 * @param instance to remove
	 * @param opts map of options
	 * @return Response from API
	 */
	ServiceResponse destroyInstance(Instance instance, Map opts);

	/**
	 * Provides methods for provisioning Apps
	 *
	 * @since 0.15.4
	 * @author Alex Clement
	 */
	public interface AppFacet {

		/**
		 * Validate the provided provisioning options for an App. A return of success = false will halt the
		 * creation and display errors
		 * @param app the App to validate
		 * @param opts options
		 * @return Response from API
		 */
		ServiceResponse validateApp(App app, Map opts);

		ServiceResponse<ProvisionResponse> runApp(App app, AppRequest appRequest, Map opts);

		ServiceResponse<PrepareAppResponse> prepareApp(App app, AppRequest appRequest, Map opts);

		ServiceResponse destroyApp(App app, Map opts);

		ServiceResponse<ProvisionResponse> updateApp(App app, AppRequest appRequest, Map opts);



	}


}
