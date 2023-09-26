package com.morpheusdata.core;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.AccountPrice;
import com.morpheusdata.model.AccountPriceSet;
import com.morpheusdata.model.projection.AccountPriceIdentityProjection;
import io.reactivex.Observable;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing AccountPrices in Morpheus
 * @author Dustin DeYoung
 * @since 0.14.3
 */
public interface MorpheusAccountPriceService extends MorpheusDataService<AccountPrice, AccountPriceIdentityProjection>, MorpheusIdentityService<AccountPriceIdentityProjection> {

	/**
	 * Get a list of AccountPrice projections based on {@link com.morpheusdata.model.Account}
	 * Account must, at least, have an id
	 * @param account {@link Account}
	 * @return Observable stream of sync projection
	 */
	Observable<AccountPriceIdentityProjection> listSyncProjections(Account account);

	/**
	 * Get a list of AccountPrice projections based on code
	 * @param codeList a list of AccountPrice codes
	 * @return Observable stream of sync projection
	 */
	Observable<AccountPriceIdentityProjection> listSyncProjectionsByCode(List<String> codeList);

	/**
	 * Get a list of AccountPrice projections associated with AccountPriceSet
	 * @param accountPriceSet {@link AccountPriceSet}
	 * @return Observable stream of id projection
	 */
	Observable<AccountPriceIdentityProjection> listIdentityProjections(AccountPriceSet accountPriceSet);

	/**
	 * Get a list of AccountPrice objects from a list of projection ids
	 * @param ids AccountPrice ids
	 * @return Observable stream of AccountPrices
	 */
	@Deprecated(since="0.15.4")
	Observable<AccountPrice> listById(Collection<Long> ids);

	/**
	 * Get a list of AccountPrice objects from a list of projection codes
	 * @param codes AccountPrice codes
	 * @return Observable stream of AccountPrices
	 */
	@Deprecated(since="0.15.4")
	Observable<AccountPrice> listByCode(Collection<String> codes);
}
