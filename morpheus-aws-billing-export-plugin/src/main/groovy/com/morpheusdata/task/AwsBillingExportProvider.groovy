package com.morpheusdata.task

import com.morpheusdata.core.*
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType

/**
 * The Task Provider that Registers the new AWS Billing Report Export Task.
 * This registers the task service as well as various aspects of the task type such as option types and
 * available target contexts
 * @see AwsBillingExportTaskService
 * @author David Estes
 */
class AwsBillingExportProvider implements TaskProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	AbstractTaskService service

	AwsBillingExportProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	ExecutableTaskInterface getService() {
		return new AwsBillingExportTaskService(morpheusContext)
	}

	@Override
	String getCode() {
		return "awsBillingExportTask"
	}

	@Override
	TaskType.TaskScope getScope() {
		return TaskType.TaskScope.all
	}

	@Override
	String getName() {
		return 'AWS Billing Report Export'
	}

	@Override
	String getDescription() {
		return 'A custom task that reverses text'
	}

	@Override
	Boolean isAllowExecuteLocal() {
		return true
	}

	@Override
	Boolean isAllowExecuteRemote() {
		return false
	}

	@Override
	Boolean isAllowExecuteResource() {
		return false
	}

	@Override
	Boolean isAllowLocalRepo() {
		return false
	}

	@Override
	Boolean hasResults() {
		return false
	}

	@Override
	Boolean isAllowRemoteKeyAuth() {
		return false
	}

	/**
	 * Builds an OptionType to take some text
	 * @return list of OptionType
	 */
	@Override
	List<OptionType> getOptionTypes() {
		OptionType sourceAccessKey = new OptionType(
				name: 'awsReportAccessKey',
				code: 'awsReportAccessKey',
				fieldName: 'awsReportAccessKey',
				optionSource: false,
				displayOrder: 0,
				fieldLabel: 'Access Key',
				required: false,
				inputType: OptionType.InputType.TEXT
		)

		OptionType sourceSecretKey = new OptionType(
				name: 'awsReportSecretKey',
				code: 'awsReportSecretKey',
				fieldName: 'awsReportSecretKey',
				optionSource: false,
				displayOrder: 1,
				fieldLabel: 'Secret Key',
				required: false,
				inputType: OptionType.InputType.PASSWORD
		)

		OptionType useHostCredentials = new OptionType(
				name: 'awsReportUseHostCredentials',
				code: 'awsReportUseHostCredentials',
				fieldName: 'awsReportUseHostCredentials',
				optionSource: false,
				displayOrder: 2,
				fieldLabel: 'Use Host Credentials',
				required: false,
				inputType: OptionType.InputType.CHECKBOX
		)

		OptionType stsAssumeRole = new OptionType(
				name: 'awsReportStsAssumeRole',
				code: 'awsReportStsAssumeRole',
				fieldName: 'awsReportStsAssumeRole',
				optionSource: false,
				displayOrder: 3,
				fieldLabel: 'Assume Role',
				required: false,
				inputType: OptionType.InputType.TEXT
		)

		OptionType sourceBucket = new OptionType(
				name: 'awsReportSourceBucket',
				code: 'awsReportSourceBucket',
				fieldName: 'awsReportSourceBucket',
				optionSource: false,
				displayOrder: 4,
				fieldLabel: 'Source Bucket Name',
				required: true,
				inputType: OptionType.InputType.TEXT
		)

		OptionType sourceBucketRegion = new OptionType(
				name: 'awsReportSourceBucketRegion',
				code: 'awsReportSourceBucketRegion',
				fieldName: 'awsReportSourceBucketRegion',
				optionSource: false,
				displayOrder: 5,
				fieldLabel: 'Source Region',
				required: true,
				inputType: OptionType.InputType.TEXT
		)

		OptionType awsReportFolder = new OptionType(
				name: 'awsReportFolder',
				code: 'awsReportFolder',
				fieldName: 'awsReportFolder',
				optionSource: false,
				displayOrder: 6,
				fieldLabel: 'Report Folder',
				required: true,
				inputType: OptionType.InputType.TEXT
		)

		OptionType awsReportName = new OptionType(
				name: 'awsReportName',
				code: 'awsReportName',
				fieldName: 'awsReportName',
				optionSource: false,
				displayOrder: 7,
				fieldLabel: 'Report Name',
				required: true,
				inputType: OptionType.InputType.TEXT
		)

		OptionType targetBucket = new OptionType(
				name: 'awsReportTargetBucket',
				code: 'awsReportTargetBucket',
				fieldName: 'awsReportTargetBucket',
				optionSource: false,
				displayOrder: 8,
				fieldLabel: 'Target Bucket Name',
				required: true,
				inputType: OptionType.InputType.TEXT
		)

		OptionType targetBucketRegion = new OptionType(
				name: 'awsReportTargetBucketRegion',
				code: 'awsReportTargetBucketRegion',
				fieldName: 'awsReportTargetBucketRegion',
				optionSource: false,
				displayOrder: 9,
				fieldLabel: 'Target Region',
				required: true,
				inputType: OptionType.InputType.TEXT
		)

		OptionType awsUsageAccountIds = new OptionType(
				name: 'awsUsageAccountIds',
				code: 'awsUsageAccountIds',
				fieldName: 'awsUsageAccountIds',
				optionSource: false,
				displayOrder: 10,
				fieldLabel: 'Usage Accounts',
				placeHolderText: 'xxxxx,xxxxx,xxxxx',
				required: false,
				inputType: OptionType.InputType.TEXT
		)

		OptionType awsBillingPeriod = new OptionType(
				name: 'awsBillingPeriod',
				code: 'awsBillingPeriod',
				fieldName: 'awsBillingPeriod',
				optionSource: false,
				displayOrder: 10,
				fieldLabel: 'Billing Period',
				placeHolderText: 'yyyymm',
				required: false,
				inputType: OptionType.InputType.TEXT
		)

		

		return [sourceAccessKey,sourceSecretKey,stsAssumeRole,useHostCredentials,sourceBucket,sourceBucketRegion, targetBucket,targetBucketRegion, awsBillingPeriod,awsReportFolder,awsReportName,awsUsageAccountIds]
	}

}
