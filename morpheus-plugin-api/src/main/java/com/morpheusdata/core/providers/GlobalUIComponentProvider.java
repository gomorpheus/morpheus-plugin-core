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

import com.morpheusdata.model.Account;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

/**
 * This provider creates a means to render global components into the main layout of Morpheus. This could be a global chat component
 * or other type of overlay code that one might want to run throughout the entire application render lifecycle.
 * It extends the common {@link UIExtensionProvider} which allows for extending available content security policies as well
 * as defining the type of renderer being used.
 *
 * @see UIExtensionProvider
 * @author David Estes
 * @since 0.8.0
 */
public interface GlobalUIComponentProvider extends UIExtensionProvider {

	/**
	 * Provides logic to check for when this global ui component should be displayed
	 * @param user current User details
	 * @param account Account details
	 * @return whether the component should be displayed
	 */
	Boolean show(User user, Account account);

	/**
	 * The renderer for the global UI Component. This is typically rendered into the footer of the main layout. This is
	 * useful for rendering common components like a global support chat.
	 * @param user The current user the page is being rendered for.
	 * @param account The current account the page is being rendered for.
	 * @return result of rendering a template
	 */
	HTMLResponse renderTemplate(User user, Account account);
}
