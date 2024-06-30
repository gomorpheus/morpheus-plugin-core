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

package com.morpheusdata.core.dashboard;

import com.morpheusdata.core.providers.UIExtensionProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.views.HTMLResponse;
import com.morpheusdata.views.Renderer;

import java.util.Map;

/**
 * Provides an interface and standard set of methods for creating custom dashboard item types
 * 
 * @since 0.13
 * @author bdwheeler
 */
public interface DashboardItemTypeProvider extends UIExtensionProvider {

	/**
	 * the dashboard item model representing the dashboard item
	 * @return the Dasbhaord item model
	 */
	DashboardItemType getDashboardItemType();

	/**
	 * Presents the HTML Rendered output of a dashboard item. This can use different {@link Renderer} implementations.
	 * The preferred is to use server side handlebars rendering with {@link com.morpheusdata.views.HandlebarsRenderer}
	 * <p><strong>Example Render:</strong></p>
	 * <pre>{@code
	 *    ViewModel model = new ViewModel()
	 * 	  model.object = reportRowsBySection
	 * 	  getRenderer().renderTemplate("hbs/instanceReport", model)
	 * }</pre>
	 * @param dashboardItem the dashboard item to render
	 * @param opts map of input options
	 * @return result of rendering an item
	 */
	HTMLResponse renderDashboardItem(DashboardItem dashboardItem, Map<String, Object> opts);

	/**
	 * Returns the relative path of a script for this dashboard item
	 * @param dashboardItem the dashboard item to render
	 * @param opts map of input options
	 * @return result of rendering an item
	 */
	String getDashboardItemScriptPath(DashboardItem dashboardItem, Map<String, Object> opts);

}
