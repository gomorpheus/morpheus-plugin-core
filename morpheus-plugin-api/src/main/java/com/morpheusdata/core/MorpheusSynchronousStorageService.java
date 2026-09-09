/*
 *  Copyright 2025 Morpheus Data, LLC.
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

package com.morpheusdata.core;

import com.morpheusdata.core.storage.MorpheusDatastoreTypeService;
import com.morpheusdata.core.storage.MorpheusVmeQcow2DatastoreService;
import com.morpheusdata.core.synchronous.*;
import com.morpheusdata.model.CheckLevel;
import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.UpdateDefinition;
import com.morpheusdata.model.UpdateOperation;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Single;

public interface MorpheusSynchronousStorageService {
	/**
	 * Returns the StorageVolume Service
	 *
	 * @return An instance of the StorageVolume Service
	 */
	MorpheusSynchronousStorageVolumeService getVolume();

	/**
	 * Returns the StorageVolumeGroup Service
	 *
	 * @return An instance of the StorageVolumeGroup Service
	 * @since 1.5.0
	 */
	MorpheusSynchronousStorageVolumeGroupService getVolumeGroup();

	/**
	 * Returns the StorageController Service
	 *
	 * @return An instance of the StorageController Service
	 */
	MorpheusSynchronousStorageControllerService getController();

	/**
	 * Returns the StorageServer Service
	 *
	 * @return An instance of the StorageServer Service
	 */
	MorpheusSynchronousStorageServerService getServer();

	/**
	 * Returns the StorageBucket Service
	 *
	 * @return An instance of the StorageBucket Service
	 */
	MorpheusSynchronousStorageBucketService getBucket();

	MorpheusVmeQcow2DatastoreService getVmeQcow2DatastoreService();

	/**
	 * Returns the StorageGroup Service
	 *
	 * @return An instance of the StorageGroup Service
	 */
	MorpheusSynchronousStorageGroupService getGroup();

	/**
	 * Returns the StorageAggregate Service
	 *
	 * @return An instance of the StorageAggregate Service
	 * @since 1.3.0
	 */
	MorpheusSynchronousStorageAggregateService getAggregate();

	/**
	 * Returns the DatastoreType Service for performing actions on a Datastore
	 *
	 * @return An instance of the DatastoreType Service
	 */
	MorpheusDatastoreTypeService getDatastoreType();

	/**
	 * Validates an update on a {@link StorageServer} before executing the update.
	 * @deprecated use {@link MorpheusSynchronousStorageServerService#validateUpdate(UpdateDefinition, StorageServer)}instead
	 */
	@Deprecated
	ServiceResponse validateUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Executes an update on a {@link StorageServer}.
	 * @deprecated use {@link MorpheusSynchronousStorageServerService#executeUpdate(UpdateDefinition, StorageServer)} instead
	 */
	@Deprecated
	ServiceResponse executeUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Post processes an update on a {@link StorageServer} after executing the update.
	 * @deprecated use {@link MorpheusSynchronousStorageServerService#postUpdate(UpdateDefinition, StorageServer)} instead
	 */
	@Deprecated
	ServiceResponse postUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Rolls back an update on a {@link StorageServer} if the update failed.
	 * @deprecated use {@link MorpheusSynchronousStorageServerService#rollbackUpdate(UpdateDefinition, StorageServer)} instead
	 */
	@Deprecated
	ServiceResponse rollbackUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Refresh the update status in a {@link StorageServer}.
	 * @deprecated use {@link MorpheusSynchronousStorageServerService#refreshUpdate(UpdateOperation, StorageServer)}  instead
	 */
	@Deprecated
	ServiceResponse refreshUpdate(StorageServer storageServer, UpdateOperation updateOperation);

	/**
	 * Run a configuration drift check on a {@link StorageServer}.
	 * @deprecated use {@link MorpheusSynchronousStorageServerService#runConfigurationDriftCheck(CheckLevel, StorageServer)}  instead
	 */
	@Deprecated
	ServiceResponse runConfigurationDriftCheck(StorageServer storageServer, CheckLevel checkLevel);

	/**
	 * Get configuration drift details on a {@link StorageServer}.
	 * @deprecated use {@link MorpheusSynchronousStorageServerService#getConfigurationDriftDetails(StorageServer)}  instead
	 */
	@Deprecated
	ServiceResponse getConfigurationDriftDetails(StorageServer storageServer);
}
