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
 * Provides an interface and standard set of methods for creating custom dashboards
 * 
 * @since 0.13
 * @author bdwheeler
 */
public interface DashboardProvider extends UIExtensionProvider {

	/**
	 * the dashboard model representing the dashboard to save as an option
	 * @return the Dasbhaord model
	 */
	Dashboard getDashboard();

	/**
	 * Presents the HTML Rendered output of a dashboard. This can use different {@link Renderer} implementations.
	 * The preferred is to use server side handlebars rendering with {@link com.morpheusdata.views.HandlebarsRenderer}
	 * <p><strong>Example Render:</strong></p>
	 * <pre>{@code
	 *    ViewModel model = new ViewModel()
	 * 	  model.object = reportRowsBySection
	 * 	  getRenderer().renderTemplate("hbs/instanceReport", model)
	 * }</pre>
	 * @param dashboard the dashboard to render
	 * @param opts map of input options
	 * @return result of rendering a dashboard
	 */
	HTMLResponse renderDashboard(Dashboard dashboard, Map<String, Object> opts);

	/**
	 * Returns the relative path of a script for this dashboard
	 * @param dashboard the dashboard to render
	 * @param opts map of input options
	 * @return path to a dashboard script
	 */
	String getDashboardScriptPath(Dashboard dashboard, Map<String, Object> opts);

}
