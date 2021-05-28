package com.morpheusdata.task

import com.morpheusdata.core.Plugin

/**
 * Main Plugin class for Exporting AWS Billing Report Files into a target S3 Bucket with the ability
 * to filter out all usage accounts except the ones requested
 * @author David Estes
 */
class AwsBillingExportPlugin extends Plugin {

	/**
	 * <ul>
	 * <li>Initializes the plugin name, description, and author.</li>
	 * <li>Registers the task provider</li>
	 * <li>Registers a Handlebars template renderer</li>
	 * <li>Registers an example Controller</li>
	 * <li>Demonstrates rendering a template</li>
	 * </ul>
	 */
	@Override
	void initialize() {
		AwsBillingExportProvider awsBillingExportProvider = new AwsBillingExportProvider(this, morpheus)
		this.setName("AWS Billing Report Export Plugin")
		this.setDescription("Provides a task that can export an AWS Billing report into a new target S3 Bucket")
		this.setAuthor("David Estes")
		this.pluginProviders.put(awsBillingExportProvider.code, awsBillingExportProvider)
		
		
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		morpheus.task.disableTask('awsBillingExportTask')
	}
}
