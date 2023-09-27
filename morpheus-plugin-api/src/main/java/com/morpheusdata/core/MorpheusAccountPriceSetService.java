package com.morpheusdata.core;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.AccountPrice;
import com.morpheusdata.model.AccountPriceSet;
import com.morpheusdata.model.MorpheusModel;
import com.morpheusdata.model.projection.AccountPriceIdentityProjection;
import com.morpheusdata.model.projection.AccountPriceSetIdentityProjection;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing AccountPriceSets in Morpheus
 * @author Dustin DeYoung
 * @since 0.14.3
 */
public interface MorpheusAccountPriceSetService extends MorpheusDataService<AccountPriceSet,AccountPriceSetIdentityProjection>, MorpheusIdentityService<AccountPriceSetIdentityProjection> {

	/**
	 * Get a list of AccountPriceSet projections based on {@link com.morpheusdata.model.Account}
	 * Account must, at least, have an id
	 * @param account {@link Account}
	 * @return Observable stream of sync projection
	 */
	Observable<AccountPriceSetIdentityProjection> listSyncProjections(Account account);

	/**
	 * Get a list of AccountPriceSet projections based on code
	 * @param codeList a list of AccountPriceSet codes
	 * @return Observable stream of sync projection
	 */
	Observable<AccountPriceSetIdentityProjection> listSyncProjectionsByCode(List<String> codeList);

	/**
	 * Get a list of AccountPriceSet objects from a list of projection ids
	 * @param ids AccountPriceSet ids
	 * @return Observable stream of AccountPriceSets
	 */
	Observable<AccountPriceSet> listById(Collection<Long> ids);

	/**
	 * Get a list of AccountPriceSet objects from a list of projection codes
	 * @param codes AccountPriceSet codes
	 * @return Observable stream of AccountPriceSets
	 */
	Observable<AccountPriceSet> listByCode(Collection<String> codes);

	/**
	 * Adds a price set to parent association
	 * @param accountPriceSet accountPriceSet to associate
	 * @param parent target of the association
	 * @return status of add results
	 */
	Single<Boolean> addPriceSetToParent(AccountPriceSet accountPriceSet, MorpheusModel parent);

	/**
	 * Adds a price to a price set and purges and expired prices
	 * @param accountPriceSet AccountPriceSet to modify
	 * @param accountPrice AccountPrice to add to the AccountPriceSet
	 * @return status of add results
	 */
	Single<Boolean> addToPriceSet(AccountPriceSet accountPriceSet, AccountPrice accountPrice);

	/**
	 * Adds a price to a price set and purges and expired prices
	 * @param accountPriceSetIdp identityProjection of the AccountPriceSet to modify
	 * @param accountPriceIdp identityProjection of AccountPrice to add to the AccountPriceSet
	 * @return status of add results
	 */
	Single<Boolean> addToPriceSet(AccountPriceSetIdentityProjection accountPriceSetIdp, AccountPriceIdentityProjection accountPriceIdp);
}
