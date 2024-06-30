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

package com.morpheusdata.core.integration;


import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.CloudIdentityProjection;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * Holds context methods for interacting with common integration type operations. This could be used for updating
 * integration type statuses or accessing object models that typically relate to integrations.
 *
 * @since 0.8.0
 * @author David Estes
 */
public interface MorpheusIntegrationService {
	/**
	 * Gets the inventory context for interacting with {@link com.morpheusdata.model.AccountInventory} objects.
	 * This is mostly used for Ansible Tower integrations and is not yet really extensible but could be in the future.
	 * @return the inventory context
	 */
	MorpheusAccountInventoryService getAccountInventory();

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param integration the integration with which we want to update the status.
	 * @param status the status of the pool server (ok,syncing,error)
	 * @param message the status message for more details. typically only used when status is 'error'.
	 *
	 * @return a Completable for notification or subscription
	 */
	Completable updateAccountIntegrationStatus(AccountIntegration integration, AccountIntegration.Status status, String message);

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param integration the integration with which we want to update the status.
	 * @param status the status string of the pool server (ok,syncing,error)
	 *
	 * @return the on complete state
	 */
	Completable updateAccountIntegrationStatus(AccountIntegration integration, AccountIntegration.Status status);

	/**
	 * Creates a new {@link AccountIntegration} and associates it to a {@link Cloud } that is being initialized
	 * @param cloudId ID of the {@link Cloud }
	 * @param integration {@link AccountIntegration } that needs to be created and associated with the cloud.
	 * @return ServiceResponse
	 */
	Single<ServiceResponse<AccountIntegration>> registerCloudIntegration(Long cloudId, AccountIntegration integration);

	/**
	 * Creates a new {@link NetworkServer} and associates it to a {@link Cloud } that is being initialized
	 * @param cloudId ID of the {@link Cloud }
	 * @param networkServer {@link NetworkServer } that needs to be created and associated with the cloud.
	 * @return ServiceResponse
	 */
	Single<ServiceResponse<NetworkServer>> registerCloudIntegration(Long cloudId, NetworkServer networkServer);

	/**
	 * Creates a new {@link StorageServer} and associates it to a {@link Cloud } that is being initialized
	 * @param cloudId ID of the {@link Cloud }
	 * @param storageServer {@link StorageServer } that needs to be created and associated with the cloud.
	 * @return ServiceResponse
	 */
	Single<ServiceResponse<StorageServer>> registerCloudIntegration(Long cloudId, StorageServer storageServer);

	Single<ServiceResponse<NetworkLoadBalancer>> registerCloudIntegration(Long cloudId, NetworkLoadBalancer networkLoadBalancer);

	/**
	 * Cleanup an {@link AccountIntegration} associated with a {@link Cloud } that is being deleted
	 * @param cloudId ID of the {@link Cloud }
	 * @param integration {@link AccountIntegration } that needs to be deleted. A mock object with just the type instead of the actual id can be passed here to have the actual record looked up by cloud and type alone.
	 * @return ServiceResponse
	 */
	Single<ServiceResponse> deleteCloudIntegration(Long cloudId, AccountIntegration integration);

	/**
	 * Cleanup the {@link NetworkServer } associated with a {@link Cloud } that is being deleted
	 * @param cloudId ID of the {@link Cloud }
	 * @param networkServer {@link NetworkServer } that needs to be deleted. A mock object with just the type instead of the actual id can be passed here to have the actual record looked up by cloud and type alone.
	 * @return ServiceResponse
	 */
	Single<ServiceResponse> deleteCloudIntegration(Long cloudId, NetworkServer networkServer);

	/**
	 * Cleanup the {@link StorageServer } associated with a {@link Cloud } that is being deleted
	 * @param cloudId ID of the {@link Cloud }
	 * @param storageServer {@link StorageServer } that needs to be deleted. A mock object with just the type instead of the actual id can be passed here to have the actual record looked up by cloud and type alone.
	 * @return ServiceResponse
	 */
	Single<ServiceResponse> deleteCloudIntegration(Long cloudId, StorageServer storageServer);

	// Single<ServiceResponse> deleteCloudIntegration(Long cloudId, NetworkLoadBalancer networkLoadBalancer);

}
