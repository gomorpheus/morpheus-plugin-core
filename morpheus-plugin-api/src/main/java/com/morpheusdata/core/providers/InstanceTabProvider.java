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


import com.morpheusdata.model.*;
import com.morpheusdata.views.HTMLResponse;

/**
 * Provides support for custom UI tabs when viewing Instance details
 *
 * @author Mike Truso
 * @since 0.15.1
 */
public interface InstanceTabProvider extends UIExtensionProvider {

	/**
	 * Instance details provided to your rendering engine
	 * @param instance details of an Instance
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(Instance instance);

	/**
	 * Provide logic when tab should be displayed. This logic is checked after permissions are validated.
	 *
	 * @param instance Instance details
	 * @param user current User details
	 * @param account Account details
	 * @return whether the tab should be displayed
	 */
	Boolean show(Instance instance, User user, Account account);

}
