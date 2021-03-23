package com.morpheusdata.core;

import com.morpheusdata.model.*;
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


	ServiceResponse validateOptions(Map opts);

	void process(ReportResult reportResult);

	/**
	 * A short description of the report for the user to better understand its purpose.
	 * @return the description string
	 */
	String getDescription();

	/**
	 * Gets the category string for the report. Reports can be organized by category when viewing.
	 * @return the category string (i.e. inventory)
	 */
	String getCategory();

	/**
	 * Only the owner of the report result can view the results.
	 * @return whether this report type can be read by the owning user only or not
	 */
	Boolean getOwnerOnly();

	/**
	 * Some reports can only be run by the master tenant for security reasons. This informs Morpheus that the report type
	 * is a master tenant only report.
	 * @return whether or not this report is for the master tenant only.
	 */
	Boolean getMasterOnly();

	/**
	 *
	 * @return
	 */
	Boolean getSupportsAllZoneTypes();

	List<OptionType> getOptionTypes();

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
