package com.morpheusdata.core.synchronous.integration;

import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.model.AccountInventory;
import com.morpheusdata.model.projection.AccountInventoryIdentityProjection;

/**
 * Holds the synchronous context methods for interacting with common integration type operations. This could be used for updating
 * integration type statuses or accessing object models that typically relate to integrations.
 *
 * @since 0.15.1
 * @author David Estes
 */
public interface MorpheusSynchronousAccountInventoryService extends MorpheusSynchronousDataService<AccountInventory, AccountInventoryIdentityProjection>, MorpheusSynchronousIdentityService<AccountInventoryIdentityProjection> {
}
