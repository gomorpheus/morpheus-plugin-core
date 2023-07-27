package com.morpheusdata.core.admin;

/**
 * This service provides access to other sub data services for accessing administration related objects such as {@link com.morpheusdata.model.User}
 * or {@link com.morpheusdata.model.Account}.
 *
 * An example usage might be to generate a user list report using the user service underneath as a context access:
 * <p><strong>Example User Query (Groovy Code):</strong></p>
 * <pre>{@code
 * void process(ReportResult reportResult) {
 *      morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.generating).blockingGet();
 *      Long displayOrder = 0
 *      morpheusContext.async.admin.user.list(new DataQuery(user: reportResult.createdBy)).map { User user ->
 *          Map<String,Object> data = [name: user.displayName, username: user.username, email:user.email, id: user.id]
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
 * @since 0.15.2
 * @author David Estes
 */
public interface MorpheusAdminService {

	/**
	 * Returns the User Service for querying / modifying user related objects asynchronously (reactive).
	 * @return an instance of the implementation of the MorpheusUserService
	 */
	MorpheusUserService getUser();

}
