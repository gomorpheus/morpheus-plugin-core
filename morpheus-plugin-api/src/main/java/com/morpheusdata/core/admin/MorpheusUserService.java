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

package com.morpheusdata.core.admin;

import com.morpheusdata.core.MorpheusContext;
import com.morpheusdata.core.MorpheusDataService;
import com.morpheusdata.core.MorpheusIdentityService;
import com.morpheusdata.model.User;
import com.morpheusdata.model.projection.UserIdentityProjection;

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
public interface MorpheusUserService extends MorpheusDataService<User,UserIdentityProjection>, MorpheusIdentityService<UserIdentityProjection> {
}
