package com.morpheusdata.core;

import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.projection.StorageServerIdentityProjection;

/**
 * Context methods for dealing with {@link StorageServer} in Morpheus
 */
public interface MorpheusStorageServerService extends MorpheusDataService<StorageServer, StorageServerIdentityProjection>, MorpheusIdentityService<StorageServerIdentityProjection> {

}
