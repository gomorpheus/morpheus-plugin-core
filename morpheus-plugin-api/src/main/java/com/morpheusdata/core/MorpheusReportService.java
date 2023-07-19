package com.morpheusdata.core;

import com.morpheusdata.core.providers.ReportProvider;
import com.morpheusdata.model.ReportResult;
import com.morpheusdata.model.ReportResultRow;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.sql.Connection;
import java.util.Collection;

/**
 * The Report Context Provides callbacks for Morpheus Custom Report Type Generation. This should ideally only be used in
 * {@link ReportProvider} based plugins. Be mindful that things like grabbing a database connection will NOT work in other plugin
 * types when being run externally in a distributed worker scenario. Reports have a simply object model. Firstly a plugin defines
 * a {@link com.morpheusdata.model.ReportType} which contains information about the report and available filter options.
 * Finally, a user runs said report which creates a {@link ReportResult} entry. These are tracked against when appending rows to the result
 * Rows have sections like 'data', or 'header'. This can affect the display in how you render things from the HTML output side.
 *
 * @see ReportProvider
 * @since 0.8.0
 * @author David Estes
 *
 */
public interface MorpheusReportService {

	/**
	 * Saves new result rows against a {@link ReportResult}. These can be chunked or done in batches to improve report
	 * efficiency. It is important to leverage displayOrder when processing for proper order on each individual {@link ReportResultRow}
	 * @param reportResult The report run being processed
	 * @param rows the list of rows to be added to the system
	 * @return success
	 */
	Single<Boolean> appendResultRows(ReportResult reportResult, Collection<ReportResultRow> rows);

	/**
	 * Updates the status of the {@link ReportResult}. This should be updated upon either success or failure or even when initializing
	 * processing of large reports.
	 * @param reportResult the report result row that is being processed
	 * @param status the new status
	 *
	 * TODO: Maybe statusMessage for failure in the future?
	 * @return the completable state of the updated status if needed
	 */
	Completable updateReportResultStatus(ReportResult reportResult, ReportResult.Status status);

	/**
	 * Grabs a Read-Only Database connection for querying the Morpheus Database. This does NOT decrypt any encrypted data
	 * in the database. Therefore some data may not be accessable. If it is possible to use a standard context method instead
	 * of a direct database call, to run a report, this is more advised.
	 *
	 * <p><strong>NOTE:</strong> Be sure to release a connection you grab when you are done so as not to over-utilize a pool.</p>
	 *
	 * @return a JDBC SQL Connection object that can be wrapped in tools like groovy.Sql.
	 */
	Single<Connection> getReadOnlyDatabaseConnection();

	/**
	 * Releases a Database Connection back into the Pool Manager. This MUST be called when completing database operations.
	 * For LONG operations try to minimize length of time this connection is used.
	 * @param connection the JDBC Sql connection to be released
	 * @return Completion status of the release event.
	 */
	Completable releaseDatabaseConnection(Connection connection);
}
