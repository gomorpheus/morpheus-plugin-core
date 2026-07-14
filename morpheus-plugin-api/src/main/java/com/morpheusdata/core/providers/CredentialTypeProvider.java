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

import com.morpheusdata.model.AccountCredentialType;
import com.morpheusdata.model.OptionType;

import java.util.List;

/**
 * Provides an interface for plugins to register custom credential types. A credential type defines
 * the schema (form fields) for a category of credentials and can optionally route storage to a
 * {@link CypherModuleProvider} mount path via the {@link AccountCredentialType#getBackend()} and
 * {@link AccountCredentialType#getBackendConfig()} fields.
 *
 * <p>When {@code backend} is set to {@code "cypher"} and {@code backendConfig} contains
 * {@code {"mountPath": "some-mount"}}, the credential service will route write operations through
 * {@link com.morpheusdata.core.cypher.MorpheusCypherService} to the {@link CypherModuleProvider}
 * registered at that mount point. This enables atomic credential persistence with side effects
 * (e.g., pushing a password change to a physical device).</p>
 *
 * <p><b>Usage pattern:</b> A plugin registers both a {@code CredentialTypeProvider} (defining
 * the credential type and its form fields) and a {@code CypherModuleProvider} (handling the
 * storage and device interaction). The credential type's {@code backendConfig.mountPath} links
 * the two together.</p>
 *
 * <pre>{@code
 * class MyPlugin extends Plugin {
 *     void initialize() {
 *         this.registerProvider(new MyCredentialTypeProvider(this, morpheus));
 *         this.registerProvider(new MyCypherProvider(this, morpheus));
 *     }
 * }
 * }</pre>
 *
 * @since 1.4.x
 * @see AccountCredentialType
 * @see CypherModuleProvider
 * @see com.morpheusdata.model.AccountCredential
 * @author Mark Helt
 */
public interface CredentialTypeProvider extends PluginProvider {

	/**
	 * Returns the {@link AccountCredentialType} definition that this provider registers.
	 * The framework will persist this type on plugin load and remove it on plugin unload.
	 *
	 * <p>The returned type should have its {@code code}, {@code name}, and optionally
	 * {@code backend} and {@code backendConfig} fields populated. When {@code backend}
	 * is {@code "cypher"}, the {@code backendConfig} must contain a JSON string with a
	 * {@code mountPath} key matching a registered {@link CypherModuleProvider}.</p>
	 *
	 * @return the credential type definition to register
	 */
	AccountCredentialType getCredentialType();

	/**
	 * Defines the form fields (option types) for credentials of this type. These fields
	 * are displayed in the credential create/edit UI when a user selects this credential type.
	 *
	 * <p>Common fields include username, password, device type selectors, etc. Each
	 * {@link OptionType} defines a single form field with its input type, validation rules,
	 * and display order.</p>
	 *
	 * @return a list of option types that define the credential form fields
	 */
	List<OptionType> getCredentialOptionTypes();
}
