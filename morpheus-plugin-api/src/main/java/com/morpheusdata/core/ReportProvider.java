package com.morpheusdata.core;

import com.morpheusdata.model.ContentSecurityPolicy;
import com.morpheusdata.model.ReportResult;
import com.morpheusdata.model.ReportResultRow;
import com.morpheusdata.model.ReportType;
import com.morpheusdata.response.ServiceResponse;
import com.morpheusdata.views.HTMLResponse;
import com.morpheusdata.views.Renderer;

import java.util.List;
import java.util.Map;

/**
 * Provides an interface and standard set of methods for creating custom report types within Morpheus. The report engine
 * typically leverages a concept of a defined {@link ReportType} and its associated {@link com.morpheusdata.model.OptionType} filters
 * as well as a means to process a report and subsequently render the results in a nice view format.
 *
 *
 * @author David Estes
 */
public interface ReportProvider extends PluginProvider {
	ReportType getReportType();

	ServiceResponse validateOptions(Map opts);

	void process(ReportResult reportResult);

	//helper context needed for saving results and view info

	/**
	 * Default is Handlebars
	 * @return renderer of specified type
	 */
	Renderer<?> getRenderer();

	/**
	 * Add policies for resources loaded from external sources.
	 *
	 * @return policy directives for various source types
	 */
	ContentSecurityPolicy getContentSecurityPolicy();

	/**
	 * Instance details provided to your rendering engine
	 * @param reportResult the results of a report
	 * @param reportRowsBySection the individual row results by section (i.e. header, vs. data)
	 * @return result of rendering an template
	 */
	HTMLResponse renderTemplate(ReportResult reportResult, Map<String, List<ReportResultRow>> reportRowsBySection);
}
