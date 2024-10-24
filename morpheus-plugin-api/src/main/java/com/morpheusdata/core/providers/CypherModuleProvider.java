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

import com.morpheusdata.core.providers.PluginProvider;
import com.morpheusdata.cypher.CypherModule;

/**
 * Provides a means to register a Cypher Secret Backend and CypherModule for registry on storing secrets or auto generating
 * secret values that can be encrypted. For more information, see the documentation for cypher-core <a href="https://github.com/gomorpheus/cypher">here</a>
 *
 * @author David Estes
 */
public interface CypherModuleProvider extends PluginProvider {

	/**
	 * An implementation of a CypherModule for reading and writing data patterns
	 * @return a cypher module
	 */
	CypherModule getCypherModule();

	/**
	* The mount prefix point for which this module should be registered to cypher's backend.
	* @return a String path prefix
	*/
	String getCypherMountPoint();
	
}
