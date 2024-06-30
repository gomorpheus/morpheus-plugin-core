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

/**
 * Provides an interface for defining custom external Credential Stores. An example might be to store secure passwords/credentials for interfacing with other integrations or clouds using
 * a third party secrets management system such as CyberArk or HashiCorp Vault
 *
 * NOTE: This provider has been moved and is deprecated. {@link com.morpheusdata.core.providers.CredentialProvider}
 *
 * @since 0.13.1
 * @see com.morpheusdata.core.providers.CredentialProvider
 * @deprecated
 * @author David Estes
 */
@Deprecated
public interface CredentialProvider extends com.morpheusdata.core.providers.CredentialProvider {
}
