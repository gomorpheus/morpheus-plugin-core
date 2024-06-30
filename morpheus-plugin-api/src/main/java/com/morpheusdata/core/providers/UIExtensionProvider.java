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
import com.morpheusdata.model.ContentSecurityPolicy;
import com.morpheusdata.views.Renderer;

/**
 * Base interface for any UI Tab related extensions.
 * This interface provides common methods for accessign renderers and content security policy
 * related information.
 *
 * @author David Estes
 * @since 0.15.2
 */
public interface UIExtensionProvider extends PluginProvider {
	/**
	 * Add policies for resources loaded from external sources.
	 *
	 * @return policy directives for various source types
	 */
	default ContentSecurityPolicy getContentSecurityPolicy() {
		return null;
	}

	/**
	 * Default is Handlebars
	 * @return renderer of specified type
	 */
	Renderer<?> getRenderer();
}
