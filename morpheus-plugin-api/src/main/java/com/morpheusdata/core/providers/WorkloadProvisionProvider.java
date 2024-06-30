/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core.providers;

import com.morpheusdata.model.*;
import com.morpheusdata.model.provisioning.WorkloadRequest;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.*;

import java.util.Map;

/**
 * Provides methods for interacting with the provisioning engine of Morpheus. This is akin to dealing with requests made
 * from "Add Instance" or from Application Blueprints
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface WorkloadProvisionProvider extends ComputeProvisionProvider {


	/**
	 * Validates the provided provisioning options of a workload. A return of success = false will halt the
	 * creation and display errors
	 * @param opts options
	 * @return Response from API. Errors should be returned in the errors Map with the key being the field name and the error
	 * message as the value.
	 */
	ServiceResponse validateWorkload(Map opts);


	/**
	 * This method is called before runWorkload and provides an opportunity to perform action or obtain configuration
	 * that will be needed in runWorkload. At the end of this method, if deploying a ComputeServer with a VirtualImage,
	 * the sourceImage on ComputeServer should be determined and saved.
	 * @param workload the Workload object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the workload
	 * @param workloadRequest the RunWorkloadRequest object containing the various configurations that may be needed
	 *                        in running the Workload. This will be passed along into runWorkload
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	default ServiceResponse<PrepareWorkloadResponse> prepareWorkload(Workload workload, WorkloadRequest workloadRequest, Map opts) {
		return ServiceResponse.success();
	}

	/**
	 * This method is a key entry point in provisioning a workload. This could be a vm, a container, or something else.
	 * Information associated with the passed Workload object is used to kick off the workload provision request
	 * @param workload the Workload object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the workload
	 * @param workloadRequest the RunWorkloadRequest object containing the various configurations that may be needed
	 *                        in running the Workload
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse<ProvisionResponse> runWorkload(Workload workload, WorkloadRequest workloadRequest, Map opts);

	/**
	 * This method is called after successful completion of runWorkload and provides an opportunity to perform some final
	 * actions during the provisioning process. For example, ejected CDs, cleanup actions, etc
	 * @param workload the Workload object that has been provisioned
	 * @return Response from the API
	 */
	ServiceResponse finalizeWorkload(Workload workload);

	/**
	 * Issues the remote calls necessary top stop a workload element from running.
	 * @param workload the Workload we want to shut down
	 * @return Response from API
	 */
	ServiceResponse stopWorkload(Workload workload);

	/**
	 * Issues the remote calls necessary to start a workload element for running.
	 * @param workload the Workload we want to start up.
	 * @return Response from API
	 */
	ServiceResponse startWorkload(Workload workload);


	/**
	 * Issues the remote calls to restart a workload element. In some cases this is just a simple alias call to do a stop/start,
	 * however, in some cases cloud providers provide a direct restart call which may be preferred for speed.
	 * @param workload the Workload we want to restart.
	 * @return Response from API
	 */
	ServiceResponse restartWorkload(Workload workload);

	/**
	 * This is the key method called to destroy / remove a workload. This should make the remote calls necessary to remove any assets
	 * associated with the workload.
	 * @param workload to remove
	 * @param opts map of options
	 * @return Response from API
	 */
	ServiceResponse removeWorkload(Workload workload, Map opts);

	/**
	 * Method called after a successful call to runWorkload to obtain the details of the ComputeServer. Implementations
	 * should not return until the server is successfully created in the underlying cloud or the server fails to
	 * create.
	 * @param server to check status
	 * @return Response from API. The publicIp and privateIp set on the WorkloadResponse will be utilized to update the ComputeServer
	 */
	ServiceResponse<ProvisionResponse> getServerDetails(ComputeServer server);

	/**
	 * This is the method called when a server's metadata tags are updated in Morpheus. Should return success if tags are updated
	 * and fail if tags are not/can't be updated.
	 * @param server server object with updated metadata field
	 * @param opts map of options
	 * @return Response from API
	 */
	default ServiceResponse updateMetadataTags(ComputeServer server, Map opts) {
		return ServiceResponse.error();
	}

	/**
	 * Method called before runWorkload to allow implementers to create resources required before runWorkload is called
	 * @param workload that will be provisioned
	 * @param opts additional options
	 * @return Response from API
	 */
	ServiceResponse createWorkloadResources(Workload workload, Map opts);

	/**
	 * Allows the workload to be resized
	 *
	 * @since 0.15.3
	 * @author Alex Clement
	 */
	public interface ResizeFacet {
		/**
		 * Request to scale the size of the Workload. Most likely, the implementation will follow that of resizeServer
		 * as the Workload usually references a ComputeServer. It is up to implementations to create the volumes, set the memory, etc
		 * on the underlying ComputeServer in the cloud environment. In addition, implementations of this method should
		 * add, remove, and update the StorageVolumes, StorageControllers, ComputeServerInterface in the cloud environment with the requested attributes
		 * and then save these attributes on the models in Morpheus. This requires adding, removing, and saving the various
		 * models to the ComputeServer using the appropriate contexts. The ServicePlan, memory, cores, coresPerSocket, maxStorage values
		 * defined on ResizeRequest will be set on the Workload and ComputeServer upon return of a successful ServiceResponse
		 * @param instance to resize
		 * @param workload to resize
		 * @param resizeRequest the resize requested parameters
		 * @param opts additional options
		 * @return Response from API
		 */
		ServiceResponse resizeWorkload(Instance instance, Workload workload, ResizeRequest resizeRequest, Map opts);
	}
}
