package com.morpheusdata.core.integration;

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
 * @author David Estes
 * @since 5.3.1
 */
public interface MorpheusAccountInventoryService {
	/**
	 * Lists all inventory projection objects for a specified integration id.
	 * The projection is a subset of the properties on a full {@link com.morpheusdata.model.AccountInventory} object for sync matching.
	 * @param accountIntegration the {@link AccountIntegration} identifier associated to the inventories to be listed.
	 * @return an RxJava Observable stream of result projection objects.
	 */
	Observable<AccountInventoryIdentityProjection> listIdentityProjections(AccountIntegration accountIntegration);

	/**
	 * Lists all {@link com.morpheusdata.model.AccountInventory} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param ids list of ids to grab {@link com.morpheusdata.model.AccountInventory} objects from.
	 * @return an RxJava Observable stream of {@link com.morpheusdata.model.AccountInventory} to be subscribed to.
	 */
	Observable<AccountInventory> listById(Collection<Long> ids);

	/**
	 * Removes Missing AccountInventories on the Morpheus side. This accepts the Projection Object instead of the main Object.
	 * It is important to note this is a Observer pattern and must be subscribed to in order for the action to occur
	 * <p><strong>Example:</strong></p>
	 * <pre>{@code
	 * morpheusContext.getIntegration().getAccountInventory().remove(removeItems).blockingGet()
	 * }</pre>
	 * @param removeList a list of AccountInventory projections to be removed
	 * @return a Single {@link Observable} returning the success status of the operation.
	 */
	Single<Boolean> remove(List<AccountInventoryIdentityProjection> removeList);

	/**
	 * Creates new AccountInventory Domains from cache / sync implementations
	 * This ensures the refType and refId match the poolServer as well as the owner default
	 * @param addList List of new {@link AccountInventory} objects to be inserted into the database
	 * @return notification of completion if someone really cares about it
	 */
	Single<Boolean> create(List<AccountInventory> addList);

	/**
	 * Saves a list of {@link AccountInventory} objects. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param accountInventoriesToSave a List of AccountInventory objects that need to be updated in the database.
	 * @return the Single Observable stating the success state of the save attempt
	 */
	Single<Boolean> save(List<AccountInventory> accountInventoriesToSave);

	/**
	 * Saves a {@link AccountInventory} object. Be mindful this is an RxJava implementation and must be subscribed
	 * to for any action to actually take place.
	 * @param accountInventoryToSave a AccountInventory Object that need to be updated in the database.
	 * @return the Single Observable stating the resultant AccountInventory Object
	 */
	Single<AccountInventory> save(AccountInventory accountInventoryToSave);
}
