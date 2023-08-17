package com.morpheusdata.core.providers;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides methods for interacting with provisioned vms related to power and resizing
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface ComputeProvisionProvider extends ProvisionProvider {

	/**
	 * Stop the server
	 * @param computeServer to stop
	 * @return Response from API
	 */
	ServiceResponse stopServer(ComputeServer computeServer);

	/**
	 * Start the server
	 * @param computeServer to start
	 * @return Response from API
	 */
	ServiceResponse startServer(ComputeServer computeServer);

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
