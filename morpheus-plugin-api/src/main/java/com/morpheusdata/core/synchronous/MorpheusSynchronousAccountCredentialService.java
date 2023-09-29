package com.morpheusdata.core.synchronous;

import com.morpheusdata.core.MorpheusSynchronousIdentityService;
import com.morpheusdata.core.MorpheusSynchronousDataService;
import com.morpheusdata.model.AccountCredential;
import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.BackupProvider;
import com.morpheusdata.model.Cloud;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import java.util.Map;

public interface MorpheusSynchronousAccountCredentialService extends MorpheusSynchronousDataService<AccountCredential, AccountCredential> {


	/**
	 * A utility method to loads credential data config from the args of an input form.
	 * May be used in OptionSourceServices to handle when a user selects an AccountCredential
	 * @param credentialConfig Pass through of args.credential elements from form payload
	 * @param refConfig The base config to fall back on.. typically cloud.configMap
	 * @return Observable
	 */
	Map loadCredentialConfig(Map credentialConfig, Map refConfig);

	AccountCredential loadCredentials(Cloud cloud);

	AccountCredential loadCredentials(AccountIntegration accountIntegration);

	AccountCredential loadCredentials(BackupProvider backupProvider);
}
