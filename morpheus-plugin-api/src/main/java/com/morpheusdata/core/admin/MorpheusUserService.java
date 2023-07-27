package com.morpheusdata.core.admin;

import com.morpheusdata.core.MorpheusDataIdentityService;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.model.User;
import com.morpheusdata.model.projection.UserIdentity;

/**
 * Provides data service access to user objects. This service is normally accessed via the {@link MorpheusContext} and
 * the {@link MorpheusAdminService}. This is an administration related service and is often used in reporting.
 *
 * <p><strong>Example User Query (Groovy Code):</strong></p>
 * <pre>{@code
 * 	morpheusContext.async.admin.user.list(new DataQuery(user: reportResult.createdBy)).subscribe { User user ->
 * 	  log.info("User: ${user.displayName}")
 * 	}
 * }</pre>
 *
 * <p><strong>Or it can also be used for report streams like so:</strong></p>
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
 * @author David Estes
 * @since 0.15.2
 */
public interface MorpheusUserService extends MorpheusDataService<User>, MorpheusDataIdentityService<UserIdentity> {
}
