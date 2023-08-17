package com.morpheusdata.core.providers;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Snapshot;
import com.morpheusdata.model.provisioning.HostRequest;
import com.morpheusdata.request.ResizeRequest;
import com.morpheusdata.response.HostResponse;
import com.morpheusdata.response.ServiceResponse;

import java.util.Map;

/**
 * Provides methods for interacting with provisioned vms to manage associated snapshots
 *
 * @since 0.15.3
 * @author Alex Clement
 */
public interface SnapshotProvisioningProvider extends ProvisioningProvider {

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
