package com.morpheusdata.core.integration;


import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.CloudIdentityProjection;
import com.morpheusdata.response.ServiceResponse;
import io.reactivex.Completable;
import io.reactivex.Single;

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

	Single<ServiceResponse<AccountIntegration>> registerCloudIntegration(Long cloudId, AccountIntegration integration);

	Single<ServiceResponse<NetworkServer>> registerCloudIntegration(Long cloudId, NetworkServer networkServer);

	Single<ServiceResponse<StorageServer>> registerCloudIntegration(Long cloudId, StorageServer storageServer);

	Single<ServiceResponse<NetworkLoadBalancer>> registerCloudIntegration(Long cloudId, NetworkLoadBalancer networkLoadBalancer);

}
