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

import com.morpheusdata.model.User;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.views.HTMLResponse;
import com.morpheusdata.views.Renderer;
import java.util.Map;

/**
 * Provides an interface and standard set of methods for creating custom analytics types within Morpheus. The difference
 * between an analytics provider and a report provider is that analytics providers are typically more focused on realtime
 * data and are more interactive. They may also have more complex data structures and visualizations.
 *
 * @author David Estes
 * @since 1.1.6
 */
public interface AnalyticsProvider extends UIExtensionProvider {

	/**
	 * The category of the analytics provider. This is used to group analytics providers in the UI.
	 * @return the category name
	 */
	public String getCategory();

	/**
	 * The description of the analytics provider. This is used to provide a brief description of the analytics provider in the UI.
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Is this analytics page reserved for the master tenant only. This is used to restrict access to the analytics page to only the master tenant.
	 * @return the master tenant only flag
	 */
	Boolean getMasterTenantOnly();

	/**
	 * Is this analytics page reserved for sub tenants only. This is used to restrict access to the analytics page to only sub tenants.
	 * @return the sub tenant only flag
	 */
	Boolean getSubTenantOnly();

	/**
	 * The order within a category the analytics provider should be displayed. This is used to order analytics providers in the UI.
	 * @return the display order starting from 0 as highest priority
	 */
	Integer getDisplayOrder();


	/**
	 * Load data for the analytics page. This method should return a map of data that can be used to render the analytics page.
	 * @param user the current user viewing the page
	 * @param params the parameters passed to the analytics page
	 * @return a map of data to be used in the analytics page
	 */
	public ServiceResponse<Map<String,Object>> loadData(User user, Map<String, Object> params);

	/**
	 * Presents the HTML Rendered output of an analytics page. This can use different {@link Renderer} implementations.
	 * The preferred is to use server side handlebars rendering with {@link com.morpheusdata.views.HandlebarsRenderer}
	 * <p><strong>Example Render:</strong></p>
	 * <pre>{@code
	 *    ViewModel model = new ViewModel()
	 * 	  model.object = analyticsData
	 * 	  getRenderer().renderTemplate("hbs/instanceAnalytics", model)
	 * }</pre>
	 * @param user the current user loading the analytics page
	 * @param analyticsData the data loaded from the loadData method
	 * @param params the parameters passed to the analytics page
	 * @return result of rendering a template
	 */
	HTMLResponse renderTemplate(User user, Map<String,Object> analyticsData, Map<String, Object> params);
}
