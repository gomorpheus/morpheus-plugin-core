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

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.Plugin;

/**
 * All Providers implement the Plugin Extension. Different Plugins for Morpheus provide different integration endpoints.
 * These could range from DNS, IPAM, and all the way up to Cloud Integrations. Each integration type extends this as a
 * base interface for providing core methods.
 * @author David Estes
 * @since 0.15.1
 *
 */
public interface PluginProvider {

	/**
	 * Returns the Morpheus Context for interacting with data stored in the Main Morpheus Application
	 *
	 * @return an implementation of the MorpheusContext for running Future based rxJava queries
	 */
	MorpheusContext getMorpheus();

	/**
	 * Returns the instance of the Plugin class that this provider is loaded from
	 * @return Plugin class contains references to other providers
	 */
	Plugin getPlugin();

	/**
	 * A unique shortcode used for referencing the provided provider. Make sure this is going to be unique as any data
	 * that is seeded or generated related to this provider will reference it by this code.
	 * @return short code string that should be unique across all other plugin implementations.
	 */
	String getCode();

	/**
	 * Provides the provider name for reference when adding to the Morpheus Orchestrator
	 * NOTE: This may be useful to set as an i18n key for UI reference and localization support.
	 *
	 * @return either an English name of a Provider or an i18n based key that can be scanned for in a properties file.
	 */
	String getName();

	/**
	 * Returns true if the Provider is a plugin. Always true for plugin but null or false for Morpheus internal providers.
	 * @return provider is plugin
	 */
	default boolean isPlugin() { return true; }
}
