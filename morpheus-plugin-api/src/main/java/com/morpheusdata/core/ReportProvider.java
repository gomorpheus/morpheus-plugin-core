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

package com.morpheusdata.core;

import com.morpheusdata.model.ReportResult;
import com.morpheusdata.model.ReportType;

import java.util.Map;

/**
 * Provides an interface and standard set of methods for creating custom report types within Morpheus. The report engine
 * typically leverages a concept of a defined {@link ReportType} and its associated {@link com.morpheusdata.model.OptionType} filters
 * as well as a means to process a report and subsequently render the results in a nice view format.
 *
 * NOTE: This Provider has moved to the providers sub package.
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
 * <p>The second part of the report is the rendering/visual aspect. This is done via the {@link com.morpheusdata.core.providers.ReportProvider#renderTemplate(ReportResult, Map)} method.
 * This contains a reference to the originating report result as well as the rows grouped by section.</p>
 *
 * @since 0.9.0
 * @see com.morpheusdata.core.providers.ReportProvider
 * @deprecated
 * @author David Estes
 */
@Deprecated
public interface ReportProvider extends com.morpheusdata.core.providers.ReportProvider {
}
