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

package com.morpheusdata.core;

import com.morpheusdata.model.CheckLevel;
import com.morpheusdata.model.StorageServer;
import com.morpheusdata.model.UpdateDefinition;
import com.morpheusdata.model.UpdateOperation;
import com.morpheusdata.model.projection.StorageServerIdentityProjection;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Single;

/**
 * Context methods for dealing with {@link StorageServer} in Morpheus
 */
public interface MorpheusStorageServerService extends MorpheusDataService<StorageServer, StorageServerIdentityProjection>, MorpheusIdentityService<StorageServerIdentityProjection> {

	Single<ServiceResponse> validateUpdate(UpdateDefinition updateDefinition, StorageServer storageServer);

	Single<ServiceResponse> executeUpdate(UpdateDefinition updateDefinition, StorageServer storageServer);

	Single<ServiceResponse> postUpdate(UpdateDefinition updateDefinition, StorageServer storageServer);

	Single<ServiceResponse> rollbackUpdate(UpdateDefinition updateDefinition, StorageServer storageServer);

	Single<ServiceResponse> refreshUpdate(UpdateOperation updateOperation, StorageServer storageServer);

	Single<ServiceResponse> runConfigurationDriftCheck(CheckLevel checkLevel, StorageServer storageServer);

	Single<ServiceResponse> getConfigurationDriftDetails(StorageServer storageServer);

	/**
	 * Trigger a short refresh on a storage server. This initiates a data sync/inventory refresh
	 * of the storage server's resources.
	 * @param storageServer storage server to refresh
	 * @return Boolean returns the result of the storage server refresh request.
	 */
	Single<Boolean> refresh(StorageServer storageServer);

	/**
	 * Trigger a daily (full) refresh on a storage server. This initiates a full data sync
	 * of the storage server's resources.
	 * @param storageServer storage server to refresh
	 * @return Boolean returns the result of the storage server refresh request.
	 */
	Single<Boolean> refreshDaily(StorageServer storageServer);
}
