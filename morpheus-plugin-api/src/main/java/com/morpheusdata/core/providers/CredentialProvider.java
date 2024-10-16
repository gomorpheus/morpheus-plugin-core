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

package com.morpheusdata.core.providers;

import com.morpheusdata.model.AccountCredential;
import com.morpheusdata.model.AccountIntegration;
import com.morpheusdata.model.Icon;
import com.morpheusdata.model.OptionType;
import com.morpheusdata.response.ServiceResponse;

import java.util.List;
import java.util.Map;

/**
 * Provides an interface for defining custom external Credential Stores. An example might be to store secure passwords/credentials for interfacing with other integrations or clouds using
 * a third party secrets management system such as CyberArk or HashiCorp Vault
 *
 * @since 0.15.1
 * @author David Estes
 */
public interface CredentialProvider extends PluginProvider {

	/**
	 * Returns the Credential Integration logo for display when a user needs to view or add this integration
	 * @since 0.12.3
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	Icon getIcon();

	/**
	 * Provide custom configuration options when creating a new {@link AccountIntegration}
	 * @return a List of OptionType
	 */
	List<OptionType> getIntegrationOptionTypes();

	/**
	 * Validation Method used to validate all inputs applied to the integration of an Credential Provider upon save.
	 * If an input fails validation or authentication information cannot be verified, Error messages should be returned
	 * via a {@link ServiceResponse} object where the key on the error is the field name and the value is the error message.
	 * If the error is a generic authentication error or unknown error, a standard message can also be sent back in the response.
	 *
	 * @param integration The Integration Object contains all the saved information regarding configuration of the Credential Provider.
	 * @param opts any custom payload submission options may exist here
	 * @return A response is returned depending on if the inputs are valid or not.
	 */
	ServiceResponse<Map> verify(AccountIntegration integration, Map opts);

	/**
	 * Periodically called to test the status of the credential provider.
	 * @param integration the referenced integration object to be loaded
	 */
	void refresh(AccountIntegration integration);

	/**
	 * Used to load credential information on the fly from the datastore. The data map should be the credential data to be loaded on the fly
	 * @param integration the referenced integration object to be loaded
	 * @param credential the credential reference to be loaded.
	 * @param opts any custom options such as proxySettings if necessary (future use)
	 * @return
	 */
	ServiceResponse<Map> loadCredentialData(AccountIntegration integration, AccountCredential credential, Map opts);

	/**
	 * Deletes the credential on the remote integration.
	 * @param integration the referenced integration object containing information necessary to connect to the endpoint
	 * @param credential the credential to be deleted
	 * @param opts any custom options such as proxySettings if necessary (future use)
	 * @return
	 */
	ServiceResponse<AccountCredential> deleteCredential(AccountIntegration integration, AccountCredential credential, Map opts);

	/**
	 * Creates the credential on the remote integration.
	 * @param integration the referenced integration object containing information necessary to connect to the endpoint
	 * @param credential the credential to be created
	 * @param opts any custom options such as proxySettings if necessary (future use)
	 * @return
	 */
	ServiceResponse<AccountCredential> createCredential(AccountIntegration integration, AccountCredential credential, Map opts);

	/**
	 * Updates the credential on the remote integration.
	 * @param integration the referenced integration object containing information necessary to connect to the endpoint
	 * @param credential the credential to be updated
	 * @param opts any custom options such as proxySettings if necessary (future use)
	 * @return
	 */
	ServiceResponse<AccountCredential> updateCredential(AccountIntegration integration, AccountCredential credential, Map opts);


}
