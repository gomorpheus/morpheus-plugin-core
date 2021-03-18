package com.morpheusdata.reports

import com.morpheusdata.core.AbstractReportProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.ReportResult
import com.morpheusdata.model.ReportType
import com.morpheusdata.model.ReportResultRow
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.response.ServiceResponse
import groovy.json.JsonOutput
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import io.reactivex.Observable;

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
	MorpheusContext getMorpheus() {
		morpheusContext
	}

	@Override
	Plugin getPlugin() {
		plugin
	}

	@Override
	String getCode() {
		'custom-report-instance-status'
	}

	@Override
	String getName() {
		'Report Instance Status'
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
			morpheus.report.appendResultRows(reportResult,resultRows)
		}.doOnComplete {
			morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.ready).blockingGet();
		}.doOnError { Throwable t ->
			morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.failed).blockingGet();
		}.blockingSubscribe()
	}

	 @Override
	 String getDescription() {
		 return "Provides a Sample Report that lists all instances in the database and their status. This Report is not tenant scoped."
	 }

	 @Override
	 String getCategory() {
		 return 'inventory'
	 }

	 @Override
	 Boolean getOwnerOnly() {
		 return false
	 }

	 @Override
	 Boolean getMasterOnly() {
		 return true
	 }

	 @Override
	 Boolean getSupportsAllZoneTypes() {
		 return true
	 }

	 @Override
	 List<OptionType> getOptionTypes() {
		 return []
	 }
 }
