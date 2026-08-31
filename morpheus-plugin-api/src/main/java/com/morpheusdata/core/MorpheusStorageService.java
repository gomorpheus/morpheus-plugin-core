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

import com.morpheusdata.core.network.MorpheusNetworkServerService;
import com.morpheusdata.model.CheckLevel;
import com.morpheusdata.model.NetworkServer;
import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.UpdateDefinition;
import com.morpheusdata.model.UpdateOperation;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Single;
import com.morpheusdata.core.MorpheusStorageReplicationGroupService;
import com.morpheusdata.core.MorpheusStorageReplicationPartnerService;

/**
 * Provides a top level interface for interacting with Storage related services in Morpheus
 * @since 1.2.5
 * @author David Estes
 */
public interface MorpheusStorageService {
	/**
	 * Returns the StorageVolume Service
	 *
	 * @return An instance of the StorageVolume Service
	 */
	MorpheusStorageVolumeService getVolume();

	/**
	 * Returns the StorageController Service
	 *
	 * @return An instance of the StorageController Service
	 */
	MorpheusStorageControllerService getController();

	/**
	 * Returns the StorageServer Service
	 *
	 * @return An instance of the StorageServer Service
	 */
	MorpheusStorageServerService getServer();

	/**
	 * Returns the StorageBucket Service
	 *
	 * @return An instance of the StorageBucket Service
	 */
	MorpheusStorageBucketService getBucket();

	/**
	 * Returns the StorageGroup Service
	 *
	 * @return An instance of the StorageGroup Service
	 */
	MorpheusStorageGroupService getGroup();

	/**
	 * Returns the StorageAggregate Service
	 *
	 * @return An instance of the StorageAggregate Service
	 * @since 1.3.0
	 */
	MorpheusStorageAggregateService getAggregate();

	/**
	 * Returns the StorageVolumeGroup Service for managing volume groups
	 * used in consistent snapshot and replication operations.
	 *
	 * @return An instance of the StorageVolumeGroup Service
	 * @since 1.4.0
	 */
	MorpheusStorageVolumeGroupService getVolumeGroup();

	/**
	 * Returns the StorageReplicationGroup Service
	 *
	 * @return An instance of the StorageReplicationGroup Service
	 * @since 1.5.0
	 */
	MorpheusStorageReplicationGroupService getReplicationGroup();

	/**
	 * Returns the StorageReplicationPartner Service
	 *
	 * @return An instance of the StorageReplicationPartner Service
	 * @since 1.5.0
	 */
	MorpheusStorageReplicationPartnerService getReplicationPartner();

	/**
	 * Validates an update on a {@link StorageServer} before executing the update.
	 * @deprecated use {@link MorpheusStorageServerService#validateUpdate(UpdateDefinition, StorageServer)}  instead
	 */
	@Deprecated
	Single<ServiceResponse> validateUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Executes an update on a {@link StorageServer}.
	 * @deprecated use {@link MorpheusStorageServerService#executeUpdate(UpdateDefinition, StorageServer)}  instead
	 */
	@Deprecated
	Single<ServiceResponse> executeUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Post processes an update on a {@link StorageServer} after executing the update.
	 * @deprecated use {@link MorpheusStorageServerService#postUpdate(UpdateDefinition, StorageServer)}  instead
	 */
	@Deprecated
	Single<ServiceResponse> postUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Rolls back an update on a {@link StorageServer} if the update failed.
	 * @deprecated use {@link MorpheusStorageServerService#rollbackUpdate(UpdateDefinition, StorageServer)}  instead
	 */
	@Deprecated
	Single<ServiceResponse> rollbackUpdate(StorageServer storageServer, UpdateDefinition updateDefinition);

	/**
	 * Refresh the update status in a {@link StorageServer}.
	 * @deprecated use {@link MorpheusStorageServerService#refreshUpdate(UpdateOperation, StorageServer)}  instead
	 */
	@Deprecated
	Single<ServiceResponse> refreshUpdate(UpdateOperation updateOperation, StorageServer storageServer);

	/**
	 * Run a configuration drift check on a {@link StorageServer}.
	 * @deprecated use {@link MorpheusStorageServerService#runConfigurationDriftCheck(CheckLevel, StorageServer)}  instead
	 */
	@Deprecated
	Single<ServiceResponse> runConfigurationDriftCheck(StorageServer storageServer, CheckLevel checkLevel);

	/**
	 * Get configuration drift details on a {@link StorageServer}.
	 * @deprecated use {@link MorpheusStorageServerService#getConfigurationDriftDetails(StorageServer)}  instead
	 */
	@Deprecated
	Single<ServiceResponse> getConfigurationDriftDetails(StorageServer storageServer);
}
