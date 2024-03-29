package com.morpheusdata.core;

import com.morpheusdata.model.AccountCredential;
import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.Cloud;
import com.morpheusdata.model.projection.AccountCredentialIdentityProjection;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.Maybe;

import java.util.Map;

/**
 * Context methods for AccountCredential
 */
public interface MorpheusAccountCredentialService extends MorpheusDataService<AccountCredential, AccountCredentialIdentityProjection> {

	/**
	 * A utility method to loads credential data config from the args of an input form.
	 * May be used in OptionSourceServices to handle when a user selects an AccountCredential
	 * @param credentialConfig Pass through of args.credential elements from form payload
	 * @param refConfig The base config to fall back on.. typically cloud.configMap
	 * @return Observable
	 */
	Single<Map> loadCredentialConfig(Map credentialConfig, Map refConfig);

	Maybe<AccountCredential> loadCredentials(Cloud cloud);

	Maybe<AccountCredential> loadCredentials(AccountIntegration accountIntegration);

	Maybe<AccountCredential> loadCredentials(BackupProvider backupProvider);

}
