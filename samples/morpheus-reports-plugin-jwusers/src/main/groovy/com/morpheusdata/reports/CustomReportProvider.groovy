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
		'custom-report-user-provisioning'
	}

	@Override
	String getName() {
		'User Provisioning Report'
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
		getRenderer().renderTemplate("hbs/userProvisioningReport", model)
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
		morpheus.report.updateReportResultStatus(reportResult,ReportResult.Status.generating).blockingGet();
		Long displayOrder = 0
		List<GroovyRowResult> results = []
		Connection dbConnection
		
		try {
			dbConnection = morpheus.report.getReadOnlyDatabaseConnection().blockingGet()
			if(reportResult.configMap?.phrase) {
				String phraseMatch = "${reportResult.configMap?.phrase}%"
				results = new Sql(dbConnection).rows("select avatar_file_name as Avatar,CONCAT(first_name, ' ', last_name) AS User, user.id AS UserId, count(*) as Count from audit_log INNER JOIN user ON COALESCE (audit_log.actual_user_id, audit_log.user_id) = user.id where description LIKE '%save|instance Created%' and description like ${phraseMatch} and user_id is not null and audit_log.date_created BETWEEN NOW() - INTERVAL 90 DAY AND NOW() GROUP BY userid ORDER BY count(*) DESC LIMIT 0,10;")
			} else {
				results = new Sql(dbConnection).rows("select avatar_file_name as Avatar,CONCAT(first_name, ' ', last_name) AS User, user.id AS UserId, count(*) as Count from audit_log INNER JOIN user ON COALESCE (audit_log.actual_user_id, audit_log.user_id) = user.id where description LIKE '%save|instance Created%' and user_id is not null and audit_log.date_created BETWEEN NOW() - INTERVAL 90 DAY AND NOW() GROUP BY userid ORDER BY count(*) DESC LIMIT 0,10;")
			}
		} finally {
			morpheus.report.releaseDatabaseConnection(dbConnection)
		}
		log.info("Results: ${results}")
		Observable<GroovyRowResult> observable = Observable.fromIterable(results) as Observable<GroovyRowResult>
		observable.map{ resultRow ->
      def String logo
      if (resultRow.Avatar)
        logo = "/storage/logos/uploads/User/${resultRow.UserId}/avatar/${resultRow.Avatar.tokenize(".").first()}_2x.${resultRow.Avatar.tokenize(".").last()}"
      else
        logo = "/assets/defaultUserImage.png"
			log.info("Mapping resultRow ${resultRow}")
			Map<String,Object> data = [avatar: logo, id: resultRow.UserId, count: resultRow.Count, name: resultRow.User]
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
		 return "Provides a Sample Report that lists Instances provisioned per user over past 90 days. This Report is not tenant scoped."
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
   /**
   @Override
	 List<OptionType> getOptionTypes() {
		 [new OptionType(code: 'status-report-search', name: 'Search', fieldName: 'phrase', fieldContext: 'config', fieldLabel: 'Search Phrase', displayOrder: 0)]
    */

	 }
 }
