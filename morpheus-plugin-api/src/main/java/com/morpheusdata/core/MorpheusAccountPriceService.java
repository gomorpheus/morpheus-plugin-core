package com.morpheusdata.core;

import com.morpheusdata.model.Account;
import com.morpheusdata.model.AccountPrice;
import com.morpheusdata.model.projection.AccountPriceIdentityProjection;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.Collection;
import java.util.List;

/**
 * Context methods for syncing AccountPrices in Morpheus
 * @author Dustin DeYoung
 * @since 0.14.3
 */
public interface MorpheusAccountPriceService {

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
	 * Get a list of AccountPrice objects from a list of projection ids
	 * @param ids AccountPrice ids
	 * @return Observable stream of AccountPrices
	 */
	Observable<AccountPrice> listById(Collection<Long> ids);

	/**
	 * Get a list of AccountPrice objects from a list of projection codes
	 * @param codes AccountPrice codes
	 * @return Observable stream of AccountPrices
	 */
	Observable<AccountPrice> listByCode(Collection<String> codes);


	/**
	 * Save updates to existing AccountPrices
	 * @param accountPrices updated AccountPrices
	 * @return status of save results
	 */
	Single<Boolean> save(List<AccountPrice> accountPrices);

	/**
	 * Create new AccountPrice in Morpheus
	 * @param accountPrices new accountPrice to persist
	 * @return status of create results
	 */
	Single<Boolean> create(List<AccountPrice> accountPrices);

	/**
	 * Remove persisted AccountPrice from Morpheus
	 * @param accountPrices account price sets to delete
	 * @return status of delete results
	 */
	Single<Boolean> remove(List<AccountPrice> accountPrices);
}
