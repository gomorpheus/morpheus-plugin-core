package com.morpheusdata.core.providers;

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
	ServiceResponse validateDockerHost(ComputeServer server, Map opts);

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
	ServiceResponse prepareHost(ComputeServer server, HostRequest hostRequest, Map opts);

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

	/**
	 * Request to scale the size of the ComputeServer. It is up to implementations to create the volumes, set the memory, etc
	 * on the underlying ComputeServer in the cloud environment. In addition, implementations of this method should
	 * add, remove, and update the StorageVolumes, StorageControllers, ComputeServerInterface in the cloud environment with the requested attributes
	 * and then save these attributes on the models in Morpheus. This requires adding, removing, and saving the various
	 * models to the ComputeServer using the appropriate contexts. The ServicePlan, memory, cores, coresPerSocket, maxStorage values
	 * defined on ResizeRequest will be set on the ComputeServer upon return of a successful ServiceResponse
	 * @param server to resize
	 * @param resizeRequest the resize requested parameters
	 * @param opts additional options
	 * @return Response from the API
	 */
	ServiceResponse resizeServer(ComputeServer server, ResizeRequest resizeRequest, Map opts);

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for an xvpVnc console connection to the server
	 */
	default ServiceResponse getXvpVNCConsoleUrl(ComputeServer server) {
		return null;
	}

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for a noVNC console connection to the server
	 */
	default ServiceResponse getNoVNCConsoleUrl(ComputeServer server){
		return null;
	}

	/**
	 * Builds the URL and authentication required to connect to the target server using noVNC
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Url and authentication for a wmks console connection to the server
	 */
	default ServiceResponse getWMKSConsoleUrl(ComputeServer server){
		return null;
	}

	/**
	 * Method called before using the console host to ensure it is accurate
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Success or failure
	 */
	default ServiceResponse updateServerHost(ComputeServer server){
		return null;
	}

	/**
	 * Method called before making a hypervisor vnc console connection to a server to ensure that the server settings are correct
	 * @since 0.13.8
	 * @param server server to connect to
	 * @return Success or failure
	 */
	default ServiceResponse enableConsoleAccess(ComputeServer server){
		return null;
	}

	/**
	 * Request to create a snapshot for the given compute server
	 * @since 0.13.8
	 * @param server server to snapshot
	 * @param opts additional options including the requested name and description of the snapshot
	 * @return Success or failure
	 */
	default ServiceResponse createSnapshot(ComputeServer server, Map opts){
		return null;
	}

	/**
	 * Request to delete all snapshots for a given compute server
	 * They only need to be deleted from the cloud, Morpheus will
	 * handle the cleanup of snapshot database records after a successful response
	 * @since 0.13.8
	 * @param server server to remove snapshots from
	 * @param opts additional options
	 * @return Success or failure
	 */
	default ServiceResponse deleteSnapshots(ComputeServer server, Map opts){
		return null;
	}

	/**
	 * Request to delete a snapshot for a given compute server
	 * It only needs to be deleted from the cloud, Morpheus will
	 * handle the cleanup of snapshot database records after a successful response
	 * @since 0.13.8
	 * @param snapshot snapshot to delete
	 * @param opts additional options will include serverId of the server the snapshot belongs to
	 * @return Success or failure
	 */
	default ServiceResponse deleteSnapshot(Snapshot snapshot, Map opts){
		return null;
	}

	/**
	 * Request to restore a snapshot to a given compute server
	 * @since 0.13.8
	 * @param snapshot snapshot to restore
	 * @param server server to restore to snapshot to
	 * @param opts additional options
	 * @return Success or failure
	 */
	default ServiceResponse revertSnapshot(ComputeServer server, Snapshot snapshot, Map opts){
		return null;
	}

}
