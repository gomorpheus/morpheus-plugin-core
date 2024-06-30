/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
