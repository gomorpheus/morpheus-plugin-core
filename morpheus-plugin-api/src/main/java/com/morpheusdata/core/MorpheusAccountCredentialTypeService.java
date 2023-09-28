package com.morpheusdata.core;

import com.morpheusdata.model.AccountCredentialType;
import io.reactivex.Observable;

/**
 * Context methods for AccountCredentialType
 */
public interface MorpheusAccountCredentialTypeService extends MorpheusDataService<AccountCredentialType, AccountCredentialType> {

	/**
	 * Get a list of AccountCredentialTypes
	 * @return Observable
	 */
	Observable<AccountCredentialType> listAll();
}
