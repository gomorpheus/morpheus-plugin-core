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
import groovy.sql.GroovyRowResult
import groovy.sql.Sql
import groovy.util.logging.Slf4j
import io.reactivex.Observable;
import java.sql.Connection

/**

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
		'custom-report-costing-dashboard'
	}

	@Override
	String getName() {
		'Costing Dashboard'
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
		getRenderer().renderTemplate("hbs/costingDashboard", model)
	}

	void process(ReportResult reportResult) {
		morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.generating).blockingGet();
		Long displayOrder = 0
		List<GroovyRowResult> results = []
		Connection dbConnection
		
		try {
			dbConnection = morpheus.report.getReadOnlyDatabaseConnection().blockingGet()
				results = new Sql(dbConnection).rows("select id as InvoiceId, account_name as AccountName, account_id as AccountId, total_cost as TotalCost, total_price as TotalPrice from account_invoice where total_cost != 0 and ref_type = 'Account' and period = '202104' ORDER BY total_cost DESC LIMIT 0,10;")
		} finally {
			morpheus.report.releaseDatabaseConnection(dbConnection)
		}
		log.info("Results: ${results}")
		Observable<GroovyRowResult> observable = Observable.fromIterable(results) as Observable<GroovyRowResult>
		observable.map{ resultRow ->
      log.info("Mapping resultRow ${resultRow}")
			Map<String,Object> data = [invoice_id: resultRow.InvoiceId, account_name: resultRow.AccountName, account_id: resultRow.AccountId, account_total_cost: resultRow.TotalCost, account_total_price: resultRow.TotalPrice]
			ReportResultRow resultRowRecord = new ReportResultRow(section: ReportResultRow.SECTION_MAIN, displayOrder: displayOrder++, dataMap: data)
			log.info("resultRowRecord: ${resultRowRecord.dump()}")
			return resultRowRecord
		}.buffer(50).doOnComplete {
			morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.ready).blockingGet();
		}.doOnError { Throwable t ->
			morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.failed).blockingGet();
		}.subscribe {resultRows ->
			morpheus.report.appendResultRows(reportResult,resultRows).blockingGet()
		}
	}

	 @Override
	 String getDescription() {
		 return "Provides a Sample Costing Dashboard Report."
	 }

	 @Override
	 String getCategory() {
		 return 'costing'
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
		 [new OptionType(code: 'status-report-search', name: 'Search', fieldName: 'phrase', fieldContext: 'config', fieldLabel: 'Search Phrase', displayOrder: 0)]
	 }
   
   /**
    * Allows various sources used in the template to be loaded
    *.cloudflare.com *.chartjs.org *.jsdelivr.net
    * @return
    */
   @Override
   ContentSecurityPolicy getContentSecurityPolicy() {
     def csp = new ContentSecurityPolicy()
     csp.scriptSrc = 'unsafe-eval'
     csp.frameSrc = '*.digitalocean.com'
     csp.imgSrc = '*.wikimedia.org'
     csp.styleSrc = '*.bootstrapcdn.com'
     csp
   }
   
 }
