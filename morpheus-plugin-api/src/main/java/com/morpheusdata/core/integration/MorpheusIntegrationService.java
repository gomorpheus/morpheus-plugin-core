package com.morpheusdata.core.integration;


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
}
