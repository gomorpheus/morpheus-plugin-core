package com.morpheusdata.core;

import com.morpheusdata.model.Account;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing {@link Account} in Morpheus
 * @author Chris Taylor
 * @since 0.15.1
 */
public interface MorpheusAccountService {

	/**
	 * Get a {@link Account} by id.
	 * @param id Account id
	 * @return Observable stream of sync projection
	 */
	Single<Account> get(Long id);

	/**
	 * Get a list of Account objects from a list of projection ids
	 * @param ids Account ids
	 * @return Observable stream of Accounts
	 */
	Observable<Account> listById(Collection<Long> ids);

	/**
	 * Lists all {@link Account} objects by a list of Identifiers. This is commonly used in sync / caching logic.
	 * @param externalIds a Collection of external Ids to filter the list of Accounts by
	 * @return an RxJava Observable stream of {@link Account} to be subscribed to.
	 */
	Observable<Account> listByExternalId(Collection<String> externalIds);

	/**
	 * Save updates to existing Accounts that are managed by the provided Plugin
	 * @param accounts updated Account
	 * @return success
	 */
	Single<Boolean> save(List<Account> accounts, Plugin plugin);

	/**
	 * Create new Accounts in Morpheus under the provided Plugin management
	 * @param accounts new Accounts to persist
	 * @return success
	 */
	Single<Boolean> create(List<Account> accounts, Plugin plugin);

	/**
	 * Create a new Account in Morpheus under the provided Plugin management
	 * @param account new Account to persist
	 * @return the Account
	 */
	Single<Account> create(Account account, Plugin plugin);
}
