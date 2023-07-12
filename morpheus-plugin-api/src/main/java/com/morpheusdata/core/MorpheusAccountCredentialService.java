package com.morpheusdata.core;

import com.morpheusdata.model.AccountCredential;
import io.reactivex.Single;

import java.util.Map;

/**
 * Context methods for AccountCredential
 */
public interface MorpheusAccountCredentialService extends MorpheusDataService<AccountCredential> {

	/**
	 * A utility method to loads credential data config from the args of an input form.
	 * May be used in OptionSourceServices to handle when a user selects an AccountCredential
	 * @param credentialConfig Pass through of args.credential elements from form payload
	 * @param refConfig The base config to fall back on.. typically cloud.configMap
	 * @return Observable
	 */
	Single<Map> loadCredentialConfig(Map credentialConfig, Map refConfig);

}
