package com.morpheusdata.core.dashboard;

import com.morpheusdata.core.UIExtensionProvider;
import com.morpheusdata.model.*;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.views.HTMLResponse;
import com.morpheusdata.views.Renderer;

import java.util.List;
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
