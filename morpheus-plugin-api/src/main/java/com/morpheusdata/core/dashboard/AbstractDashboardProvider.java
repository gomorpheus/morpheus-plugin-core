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

import com.morpheusdata.model.ContentSecurityPolicy;
import com.morpheusdata.model.Dashboard;
import com.morpheusdata.views.HandlebarsRenderer;
import com.morpheusdata.views.Renderer;
import com.morpheusdata.views.HTMLResponse;
import com.morpheusdata.views.ViewModel;
import java.util.Map;

/**
 * The Abstract representation for rendering a dashboard.
 * @see DashboardProvider
 * @author bdwheeler
 */
public abstract class AbstractDashboardProvider implements DashboardProvider {
	
	private HandlebarsRenderer renderer;

	@Override
	public Renderer<?> getRenderer() {
		if(renderer == null) {
			renderer = new HandlebarsRenderer("renderer", getPlugin().getClassLoader());
			renderer.registerAssetHelper(getPlugin().getName());
			renderer.registerNonceHelper(getMorpheus().getWebRequest());
		}
		return renderer;
	}

	/**
	 * Allows various sources used in the template to be loaded
	 * @return
	 */
	public ContentSecurityPolicy getContentSecurityPolicy() {
		ContentSecurityPolicy csp = new ContentSecurityPolicy();
		return csp;
	}

	/**
	 * render the dashboard template
	 * @param  dashboard the dashboard to render
	 * @param  opts request option map
	 * @return
	 */
	@Override
	public HTMLResponse renderDashboard(Dashboard dashboard, Map<String, Object> opts) {
		HTMLResponse rtn = null;
		String templatePath = dashboard.getTemplatePath();
		if(templatePath != null) {
			ViewModel<Dashboard> model = new ViewModel<Dashboard>();
			model.object = dashboard;
			model.opts = opts;
			rtn = getRenderer().renderTemplate(templatePath, model);
		}
		return rtn;
	}

	/**
	 * get the relative url path for the script for this dashboard
	 * @param  dashboard the dashboard to get the script for
	 * @param  opts request option map
	 * @return
	 */
	public String getDashboardScriptPath(Dashboard dashboard, Map<String, Object> opts) {
		String rtn = null;
		String scriptPath = dashboard.getScriptPath();
		if(scriptPath != null) {
			if(!scriptPath.startsWith("/"))
				scriptPath = "/" + scriptPath;
			rtn = "/assets/plugin/" + getPlugin().getName().toLowerCase().replace(" ", "-") + scriptPath;
		}
		return rtn;
	}

}
