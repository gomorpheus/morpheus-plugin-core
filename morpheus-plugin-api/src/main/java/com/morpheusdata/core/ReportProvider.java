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
 * <p>A custom report has 2 parts. One is the method for processing/generating the report. This takes an input of a {@link ReportResult} for details as to the users configured filters as well as
 * where the result rows should be stored. The process method should send data back as result rows grouped by section. These sections can be header,footer,main or custom.
 * But be aware, only the main section is used when automatically providing csv export functionality.</p>
 * <p><strong>Example Process (Groovy Code):</strong></p>
 * <pre>{@code
 * void process(ReportResult reportResult) {
 *      morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.generating).blockingGet();
 *      Long displayOrder = 0
 *      List<GroovyRowResult> results = []
 *      Connection dbConnection
 *
 *      try {
 *          dbConnection = morpheus.report.getReadOnlyDatabaseConnection().blockingGet()
 *          if(reportResult.configMap?.phrase) {
 *              String phraseMatch = "${reportResult.configMap?.phrase}%"
 *              results = new Sql(dbConnection).rows("SELECT id,name,status from instance WHERE name LIKE ${phraseMatch} order by name asc;")
 *          } else {
 *              results = new Sql(dbConnection).rows("SELECT id,name,status from instance order by name asc;")
 *          }        
 *      } finally {
 *          morpheus.report.releaseDatabaseConnection(dbConnection)
 *      }
 *      log.info("Results: ${results}")
 *      Observable<GroovyRowResult> observable = Observable.fromIterable(results) as Observable<GroovyRowResult>
 *      observable.map{ resultRow ->
 *          log.info("Mapping resultRow ${resultRow}")
 *          Map<String,Object> data = [name: resultRow.name, id: resultRow.id, status: resultRow.status]
 *          ReportResultRow resultRowRecord = new ReportResultRow(section: ReportResultRow.SECTION_MAIN, displayOrder: displayOrder++, dataMap: data)
 *          log.info("resultRowRecord: ${resultRowRecord.dump()}")
 *          return resultRowRecord
 *      }.buffer(50).doOnComplete {
 *          morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.ready).blockingGet();
 *      }.doOnError { Throwable t ->
 *          morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.failed).blockingGet();
 *      }.subscribe {resultRows ->
 *          morpheus.report.appendResultRows(reportResult,resultRows).blockingGet()
 *      }
 *  }
 * }</pre>
 * <p>The second part of the report is the rendering/visual aspect. This is done via the {@link ReportProvider#renderTemplate(ReportResult, Map)} method.
 * This contains a reference to the originating report result as well as the rows grouped by section.</p>
 *
 * @since 0.8.0
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
