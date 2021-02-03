package com.morpheusdata.task

import com.morpheusdata.core.AbstractTaskService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.*
import com.bertramlabs.plugins.karman.*
import groovy.transform.CompileStatic
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Example AbstractTaskService. Each method demonstrates building an example TaskConfig for the relevant task type
 */
class AwsBillingExportTaskService extends AbstractTaskService {
	MorpheusContext context

	AwsBillingExportTaskService(MorpheusContext context) {
		this.context = context
	}

	@Override
	MorpheusContext getContext() {
		return context
	}

	@Override
	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		TaskConfig config = buildLocalTaskConfig([:], task, [], opts).blockingGet()
		if(instance) {
			config = buildInstanceTaskConfig(instance, [:], task, [], opts).blockingGet()
		}
		if(container) {
			config = buildContainerTaskConfig(container, [:], task, [], opts).blockingGet()
		}
	
		executeTask(task, config)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts) {
		return null
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task) {
		return null
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task, Map opts) {
		return null
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task) {
		return null
	}

	@Override
	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		return null
	}

	@Override
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server, Instance instance) {
		return null
	}

	/**
	 * Finds the input text from the OptionType created in {@link AwsBillingExportProvider#getOptionTypes}.
	 * Uses Groovy {@link org.codehaus.groovy.runtime.StringGroovyMethods#reverse} on the input text
	 * @param task
	 * @param config
	 * @return data and output are the reversed text
	 */
	TaskResult executeTask(Task task, TaskConfig config) {
		String accessKey     = task.taskOptions.find { it.optionType.code == 'awsReportAccessKey' }?.value
		String secretKey     = task.taskOptions.find { it.optionType.code == 'awsReportSecretKey' }?.value
		String stsAssumeRole = task.taskOptions.find { it.optionType.code == 'awsReportStsAssumeRole' }?.value
		Boolean useHostCredentials = task.taskOptions.find { it.optionType.code == 'awsReportUseHostCredentials' }?.value ? true : false
		String costingReport = task.taskOptions.find { it.optionType.code == 'awsReportName' }?.value
		String costingBucket = task.taskOptions.find { it.optionType.code == 'awsReportSourceBucket' }?.value
		String costingRegion = task.taskOptions.find { it.optionType.code == 'awsReportSourceBucketRegion' }?.value
		String costingFolder = task.taskOptions.find { it.optionType.code == 'awsReportFolder' }?.value
		String billingPeriod = task.taskOptions.find { it.optionType.code == 'awsBillingPeriod' }?.value
		List<String> usageAccountIds = task.taskOptions.find { it.optionType.code == 'awsUsageAccountIds' }?.value?.tokenize(', ')
		String targetBucket = task.taskOptions.find { it.optionType.code == 'awsReportTargetBucket' }?.value
		String targetRegion = task.taskOptions.find { it.optionType.code == 'awsReportTargetBucketRegion' }?.value
		Date startDate
		Date endDate
		if(!billingPeriod) {
			Date costDate = new Date()
			billingPeriod = costDate.format('yyyyMM')
			startDate = getStartOfGmtMonth(costDate)
			endDate = getEndOfGmtMonth(costDate)
		} else {
			def timezone = TimeZone.getTimeZone("GMT")
			Date costDate = Date.parse('yyyyMMdd', billingPeriod + '01', timezone)
			startDate = getStartOfGmtMonth(costDate)
			endDate = getEndOfGmtMonth(costDate)
		}

		def sourceProviderConfig = [provider:'s3', accessKey:accessKey, secretKey:secretKey, region:costingRegion, 
		useHostCredentials: useHostCredentials, stsAssumeRole: stsAssumeRole]
		StorageProvider sourceProvider = StorageProvider.create(sourceProviderConfig)
		def targetProviderConfig = [provider:'s3', accessKey:accessKey, secretKey:secretKey, region:targetRegion, useHostCredentials: useHostCredentials, stsAssumeRole: stsAssumeRole]
		StorageProvider targetProvider = StorageProvider.create(targetProviderConfig)

		return copyReport(startDate,endDate,billingPeriod, sourceBucket, costingBucket, costingFolder, costingReport, sourceProvider,targetProvider, usageAcconutIds)

	}

	protected TaskResult copyReport(Date startDate, Date endDate, String period, String sourceBucket, String destinationBucket, String reportFolder, String reportName, StorageProvider sourceProvider, StorageProvider targetProvider, List<String> usageAccountIds) {
		String output = []
		try {
			String dateFolder = "${startDate.format('yyyyMMdd', TimeZone.getTimeZone('GMT'))}-${endDate.format('yyyyMMdd', TimeZone.getTimeZone('GMT'))}"
			CloudFile sourceManifest = sourceProvider[sourceBucket][reportFolder + '/' + reportName + '/' + dateFolder + '/' + costingReport + '-Manifest.json']

			//Firstly copy the manifest file
			
			output << "Copying Manifest To Target for Period: ${period}..."
			targetManifest = targetProvider[targetBucket][reportFolder + '/' + reportName + '/' + dateFolder + '/' + costingReport + '-Manifest.json']
			String manifestJson = sourceManifest.getText()
			targetManifest.text = manifestJson
			targetManifest.save()
			output << "Manifest Copy Complete\n"
			
			Map manifest = new groovy.json.JsonSlurper().parseText(manifestJson) as Map
			List<String> reportKeys = manifest.reportKeys as List<String>
			Long filePosition
			for(String reportKey in reportKeys) {
				output << "Copying File ${filePosition + 1} of ${reportKeys.size()} for Period: ${period}..."
				CloudFile dataFile = storageProvider[sourceBucket][reportKey]
				CloudFile targetFile = storageProvider[targetBucket][reportKey]
				GZIPInputStream dataFileStream = new GZIPInputStream(dataFile.getInputStream(), 65536)
				BufferedReader br = new BufferedReader(new InputStreamReader(dataFileStream))
				String line
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream()
				GZIPOutputStream out = new GZIPOutputStream(byteStream)
				Long lineCounter = 0L 
				Integer usageAccountColumn
				while((line = br.readLine()) != null) {
					List<String> lineArgs = splitCsvArgs(line)
					if(lineCounter == 0) {
						usageAccountColumn = lineArgs.indexOf("lineItem/UsageAccountId")
						out << line + "\n"
					} else {
						String usageAccountId = lineArgs[usageAccountColumn]
						if(!usageAccountIds || usageAccountIds.contains(usageAccountId)) {
							out << line + "\n"
						}
					}
					lineCounter++
				}
				out.flush()
				byteStream.toByteArray()
				targetFile.bytes = byteStream.toByteArray()
				targetFile.save()
				filePosition++
			}
			output << "Report Copy Completed Into Target ${targetBucket} for Period: ${period}"
			return new TaskResult(
				success: true,
				data   : null,
				output : output.join("\n")
			)
		} catch(ex) {
			return new TaskResult(
				success: false,
				data   : null,
				error  : "Error Copying Report: ${ex.message}",
				output : output.join("\n")
			)
		}


	} 


	protected Date getStartOfGmtMonth(Date date) {
		GregorianCalendar rtn = GregorianCalendar.instance
		rtn.timeZone = gmtTimezone
		rtn.time = date
		rtn.with {
			set HOUR_OF_DAY, 0
			set MINUTE, 0
			set SECOND, 0
			set MILLISECOND, 0
			set DAY_OF_MONTH, 1
		}
		return rtn.time
  	}

	protected getEndOfGmtMonth(Date date) {
		def rtn = GregorianCalendar.instance
		rtn.timeZone = gmtTimezone
		rtn.time = date
		rtn.with {
			set HOUR_OF_DAY, 0
			set MINUTE, 0
			set SECOND, 0
			set MILLISECOND, 0
			set DAY_OF_MONTH, 1
		}
		rtn.add(Calendar.MONTH, 1)
		rtn.add(Calendar.DAY_OF_YEAR, -1)
		return rtn.time
	}

	@CompileStatic
	protected ArrayList<String> splitCsvArgs(String s) {
		ArrayList<String> words = new ArrayList<String>();
		boolean notInsideQuote = true;
		int start =0, end=0;
		for(int i=0; i<s.length(); i++)
		{
			if(s.charAt(i)==',' && notInsideQuote)
			{
				words.add(s.substring(start,i));
				start = i+1;                
			}   
			else if(s.charAt(i)=='"')
			notInsideQuote=!notInsideQuote;
		}
		if(start < s.length()) {
			words.add(s.substring(start));	
		} else {
			words.add('')
		}
		
		return words;
	} 
}
