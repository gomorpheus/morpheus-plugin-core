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

package com.morpheusdata.core;

import com.morpheusdata.model.*;
import com.morpheusdata.model.projection.AccountCredentialIdentityProjection;
import com.morpheusdata.response.ServiceResponse;
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

	/**
	 * Loads the credential for a ComputeServer based on its AccountCredentialLink.
	 * For cypher-backed credentials, the credential data is populated from the per-link applied cypher key
	 * specific to this server, falling back to the base credential key if no link-specific value exists.
	 * This allows each device to carry its own unique credential value (e.g. a rotated password)
	 * while sharing the same AccountCredential.
	 * @param computeServer the server whose linked credential should be loaded
	 * @return Maybe emitting the AccountCredential with data populated, or empty if no link exists
	 */
	Maybe<AccountCredential> loadCredentials(ComputeServer computeServer);

	/**
	 * Loads the credential for a StorageServer based on its AccountCredentialLink.
	 * For cypher-backed credentials, the credential data is populated from the per-link applied cypher key
	 * specific to this storage server, falling back to the base credential key if no link-specific value exists.
	 * @param storageServer the storage server whose linked credential should be loaded
	 * @return Maybe emitting the AccountCredential with data populated, or empty if no link exists
	 */
	Maybe<AccountCredential> loadCredentials(StorageServer storageServer);

	/**
	 * Loads the credential for a NetworkServer based on its AccountCredentialLink.
	 * For cypher-backed credentials, the credential data is populated from the per-link applied cypher key
	 * specific to this network server, falling back to the base credential key if no link-specific value exists.
	 * @param networkServer the network server whose linked credential should be loaded
	 * @return Maybe emitting the AccountCredential with data populated, or empty if no link exists
	 */
	Maybe<AccountCredential> loadCredentials(NetworkServer networkServer);

	/**
	 * Creates a credential link for a specified credential and associated ref information and returns the created link.
	 * @param credential the credential to create the link for.
	 * @param refType The resource type the link will be used by.
	 * @param refUuid The uuid for the resource the link will be used by.
	 * @param refName The name of the resource the link will be used by.
	 * @return ServiceResponse possibly containing a created AccountCredentialLink.
	 */
	Single<ServiceResponse<AccountCredentialLink>> createCredentialLink(AccountCredential credential, String refType, String refUuid, String refName);
}
