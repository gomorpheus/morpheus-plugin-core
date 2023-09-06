package com.morpheusdata.core.integration;

import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.AccountInventory;
import com.morpheusdata.model.projection.AccountInventoryIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Provides operation methods for interacting with {@link com.morpheusdata.model.AccountInventory} objects. These are mostly
 * related to Ansible tower and ansible in general and is not yet that extensible but could be in the future.
 *
 * @author David Estes, bdwheeler
 * @since 5.3.1
 */
public interface MorpheusAccountInventoryService extends MorpheusDataService<AccountInventory,AccountInventoryIdentityProjection> {

	/**
	 * Lists all inventory projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link com.morpheusdata.model.AccountInventory} object for sync matching.
	 * @param accountIntegration the {@link AccountIntegration} identifier associated to the inventories to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<AccountInventoryIdentityProjection> listIdentityProjections(AccountIntegration accountIntegration);

}
