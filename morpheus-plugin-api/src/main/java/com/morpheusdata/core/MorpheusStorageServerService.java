package com.morpheusdata.core;

import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.projection.StorageServerIdentityProjection;

/**
 * Context methods for dealing with {@link StorageServer} in Morpheus
 */
public interface MorpheusStorageServerService extends MorpheusDataService<StorageServer,StorageServerIdentityProjection> {

	// /**
	//  * Used for updating the status of a {@link StorageServer}.
	//  * @param storageServer the storage server with which we want to update the status.
	//  * @param status the status of the storage server (ok,syncing,error)
	//  * @param message the status message for more details. typically only used when status is 'error'.
	//  *
	//  * @return a Completable for notification or subscription
	//  */
	// Completable updateStorageServerStatus(StorageServer storageServer, String status, String message);

	// /**
	//  * Used for updating the status of a {@link StorageServer}.
	//  * @param storageServer the storage server with which we want to update the status.
	//  * @param status the status string of the storage server (ok,syncing,error)
	//  *
	//  * @return the on complete state
	//  */
	// Completable updateStorageServerStatus(StorageServer storageServer, String status);
}