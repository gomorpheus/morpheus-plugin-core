package com.morpheusdata.core.synchronous.integration;

import com.morpheusdata.core.integration.MorpheusIntegrationService;
import com.morpheusdata.core.synchronous.integration.MorpheusSynchronousAccountInventoryService;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;

/**
 * Holds context methods for interacting with common integration type operations. This could be used for updating
 * integration type statuses or accessing object models that typically relate to integrations.
 *
 * @since 0.15.2
 * @author David Estes
 */
public interface MorpheusSynchronousIntegrationService {

	MorpheusIntegrationService getIntegrationService();
	/**
	 * Gets the inventory context for interacting with {@link com.morpheusdata.model.AccountInventory} objects.
	 * This is mostly used for Ansible Tower integrations and is not yet really extensible but could be in the future.
	 * @return the inventory context
	 */
	MorpheusSynchronousAccountInventoryService getAccountInventory();

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param integration the integration with which we want to update the status.
	 * @param status the status of the pool server (ok,syncing,error)
	 * @param message the status message for more details. typically only used when status is 'error'.
	 *
	 */
	default void updateAccountIntegrationStatus(AccountIntegration integration, AccountIntegration.Status status, String message) {
		getIntegrationService().updateAccountIntegrationStatus(integration,status,message).blockingAwait();
	}

	/**
	 * Used for updating the status of a {@link NetworkPoolServer} integration.
	 * @param integration the integration with which we want to update the status.
	 * @param status the status string of the pool server (ok,syncing,error)
	 *
	 */
	default void updateAccountIntegrationStatus(AccountIntegration integration, AccountIntegration.Status status) {
		getIntegrationService().updateAccountIntegrationStatus(integration,status).blockingAwait();
	}

	default ServiceResponse<AccountIntegration> registerCloudIntegration(Long cloudId, AccountIntegration integration) {
		return getIntegrationService().registerCloudIntegration(cloudId,integration).blockingGet();
	}

	default ServiceResponse<NetworkServer> registerCloudIntegration(Long cloudId, NetworkServer networkServer) {
		return getIntegrationService().registerCloudIntegration(cloudId,networkServer).blockingGet();
	}

	default ServiceResponse<StorageServer> registerCloudIntegration(Long cloudId, StorageServer storageServer) {
		return getIntegrationService().registerCloudIntegration(cloudId,storageServer).blockingGet();
	}

	default ServiceResponse<NetworkLoadBalancer> registerCloudIntegration(Long cloudId, NetworkLoadBalancer networkLoadBalancer) {
		return getIntegrationService().registerCloudIntegration(cloudId,networkLoadBalancer).blockingGet();
	}
}
