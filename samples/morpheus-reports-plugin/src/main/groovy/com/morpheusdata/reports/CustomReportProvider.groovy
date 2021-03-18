package com.morpheusdata.reports

import com.morpheusdata.core.AbstractReportProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Account
import com.morpheusdata.model.Instance
import com.morpheusdata.model.ReportResult
import com.morpheusdata.model.ReportType
import com.morpheusdata.model.ReportResultRow
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.User
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import groovy.sql.GroovyResultSet
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.util.logging.Slf4j

import java.sql.Connection

/**
 * Example TabProvider
 */
 @Slf4j
class CustomReportProvider extends AbstractReportProvider {
	Plugin plugin
	MorpheusContext morpheusContext

	CustomReportProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
	}

	@Override
	MorpheusContext getMorpheusContext() {
		morpheusContext
	}

	@Override
	Plugin getPlugin() {
		plugin
	}

	@Override
	String getProviderCode() {
		'custom-report-instance-status'
	}

	@Override
	String getProviderName() {
		'Report Instance Status'
	}

	ReportType getReportType() {
		ReportType reportType = new ReportType(code:'custom-instance-status-report',name:'Instance Status Report', description: 'Creates a report listing all instances and their statuses.', category: 'inventory', visible:true, masterOnly:false, ownerOnly:false)
		reportType.supportsAllZoneTypes = true
		reportType.optionTypes = []

		return reportType
	}

	 ServiceResponse validateOptions(Map opts) {
		 return ServiceResponse.success()
	 }

	/**
	 * Demonstrates building a TaskConfig to get details about the Instance and renders the html from the specified template.
	 * @param instance details of an Instance
	 * @return
	 */
	@Override
	HTMLResponse renderTemplate(ReportResult reportResult, Map<String, List<ReportResultRow>> reportRowsBySection) {
		ViewModel<String> model = new ViewModel<String>()
		
		model.object = reportRowsBySection
		getRenderer().renderTemplate("hbs/instanceReport", model)
	}

	/**
	 * Allows various sources used in the template to be loaded
	 * @return
	 */
	@Override
	ContentSecurityPolicy getContentSecurityPolicy() {
		def csp = new ContentSecurityPolicy()
		csp.scriptSrc = '*.jsdelivr.net'
		csp.frameSrc = '*.digitalocean.com'
		csp.imgSrc = '*.wikimedia.org'
		csp.styleSrc = 'https: *.bootstrapcdn.com'
		csp
	}


	void process(ReportResult reportResult) {
		morpheusContext.report.updateReportResultStatus(reportResult,ReportResult.Status.generating).blockingGet();
		Long displayOrder = 0
		List<GroovyRowResult> results = []

		try {
			Connection dbConnection = morpheusContext.report.getReadOnlyDatabaseConnection().blockingGet()
			results = new Sql(dbConnection).rows("SELECT id,name,status from instance order by name asc;")
		} finally {
			morpheusContext.report.releaseDatabaseConnection(dbConnection)
		}
		Observable<GroovyRowResult> observable = Observable.fromIterable(results) as Observable<GroovyRowResult>
		observable.map{ resultRow ->
			return new ReportResultRow(section: 'data',displayOrder: displayOrder++, data: JsonOutput.toJson(data))
		}.buffer(50).flatMap { resultRows
			morpheusContext.report.appendResultRows(reportResult,resultRows)
		}.doOnComplete {
			morpheusContext.report.updateReportResultStatus(reportResult,ReportResult.Status.ready).blockingGet();
		}.doOnError { Throwable t ->
			morpheusContext.report.updateReportResultStatus(reportResult,ReportResult.Status.failed).blockingGet();
		}.blockingSubscribe()
	}
}
