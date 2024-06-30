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

import com.morpheusdata.PrepareHostResponse;
import com.morpheusdata.model.*;
import com.morpheusdata.model.provisioning.HostRequest;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.*;

import java.util.Map;

/**
 * Provides methods for interacting with the provisioning engine of Morpheus for host and vms directly
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface HostProvisionProvider extends ComputeProvisionProvider {


	/**
	 * Validate the provided provisioning options for a Docker host server.  A return of success = false will halt the
	 * creation and display errors
	 * @param server the ComputeServer to validate
	 * @param opts options
	 * @return Response from API
	 */
	ServiceResponse validateHost(ComputeServer server, Map opts);

	/**
	 * This method is called before runHost and provides an opportunity to perform action or obtain configuration
	 * that will be needed in runHost. At the end of this method, if deploying a ComputeServer with a VirtualImage,
	 * the sourceImage on ComputeServer should be determined and saved.
	 * @param server the ComputeServer object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the server
	 * @param hostRequest the HostRequest object containing the various configurations that may be needed
	 *                        in running the server. This will be passed along into runHost
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse<PrepareHostResponse> prepareHost(ComputeServer server, HostRequest hostRequest, Map opts);

	/**
	 * This method is called to provision a Host (i.e. Docker host).
	 * Information associated with the passed ComputeServer object is used to kick off the provision request. Implementations
	 * of this method should populate ProvisionResponse as complete as possible and as quickly as possible. Implementations
	 * may choose to save the externalId on the ComputeServer or pass it back in ProvisionResponse.
	 * @param server the ComputeServer object we intend to provision along with some of the associated data needed to determine
	 *                 how best to provision the server
	 * @param hostRequest the HostRequest object containing the various configurations that may be needed
	 *                         in running the server.
	 * @param opts additional configuration options that may have been passed during provisioning
	 * @return Response from API
	 */
	ServiceResponse<ProvisionResponse> runHost(ComputeServer server, HostRequest hostRequest, Map opts);

	/**
	 * This method is called after runHost returns successfully and provides implementations a mechanism to wait for the
	 * ComputeServer to finish the creation process in the underlying Cloud. ProvisionResponse should be filled out as
	 * complete as possible.
	 * @param server the ComputeServer object to wait for
	 * @return
	 */
	default ServiceResponse<ProvisionResponse> waitForHost(ComputeServer server) {
		return ServiceResponse.success();
	}

	/**
	 * This method is called after successful completion of runHost and successful completion of waitForHost and provides
	 * an opportunity to perform some final actions during the provisioning process.
	 * For example, ejected CDs, cleanup actions, etc
	 * @param server the ComputeServer object that has been provisioned
	 * @return Response from the API
	 */
	ServiceResponse finalizeHost(ComputeServer server);

	/**
	 * Allows the server to be resized
	 *
	 * @since 0.15.3
	 * @author Alex Clement
	 */
	public interface ResizeFacet {

		/**
		 * Request to scale the size of the ComputeServer. It is up to implementations to create the volumes, set the memory, etc
		 * on the underlying ComputeServer in the cloud environment. In addition, implementations of this method should
		 * add, remove, and update the StorageVolumes, StorageControllers, ComputeServerInterface in the cloud environment with the requested attributes
		 * and then save these attributes on the models in Morpheus. This requires adding, removing, and saving the various
		 * models to the ComputeServer using the appropriate contexts. The ServicePlan, memory, cores, coresPerSocket, maxStorage values
		 * defined on ResizeRequest will be set on the ComputeServer upon return of a successful ServiceResponse
		 *
		 * @param server        to resize
		 * @param resizeRequest the resize requested parameters
		 * @param opts          additional options
		 * @return Response from the API
		 */
		ServiceResponse resizeServer(ComputeServer server, ResizeRequest resizeRequest, Map opts);
	}

}
