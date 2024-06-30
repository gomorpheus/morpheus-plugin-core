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


import com.morpheusdata.core.providers.UIExtensionProvider;
import com.morpheusdata.model.CatalogItemType;
import com.morpheusdata.model.User;
import com.morpheusdata.views.HTMLResponse;

/**
 * This provider provides a means to define custom layouts for rendering catalog item detail pages. The default layout easily renders markdown, and the user input form
 * via the {@link com.morpheusdata.model.OptionType} model. This enables full customizability of the page.
 *
 * @since 0.15.2
 * @author David Estes
 */
public interface CatalogItemLayoutProvider extends UIExtensionProvider {
	/**
	 * Render details provided to your rendering engine
	 * @param catalogItemType details of a Catalog Item Type to be rendered
	 * @param user the current user rendering the template. This is useful in case the view needs to change based on some user permission or existence of user.
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(CatalogItemType catalogItemType, User user);
}
