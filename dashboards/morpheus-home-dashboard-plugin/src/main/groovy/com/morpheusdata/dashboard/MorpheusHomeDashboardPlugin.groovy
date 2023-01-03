package com.morpheusdata.dashboard

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission
import groovy.util.logging.Slf4j

/**
 * default morpheus dashboards and dashboard items
 */
@Slf4j
class MorpheusHomeDashboardPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-home-dashboard-plugin'
	}

	@Override
	void initialize() {
		try {
			//set the name
			this.setName("Morpheus Home Dashboard")
			//add the dashboard item types
			UserFavoritesItemProvider userFavoritesProvider = new UserFavoritesItemProvider(this, morpheus)
			this.pluginProviders.put(userFavoritesProvider.code, userFavoritesProvider)
			InstanceCountItemProvider instanceCountProvider = new InstanceCountItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountProvider.code, instanceCountProvider)
			InstanceCountCloudItemProvider instanceCountCloudProvider = new InstanceCountCloudItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountCloudProvider.code, instanceCountCloudProvider)
			InstanceCountCloudDayItemProvider instanceCountCloudDayProvider = new InstanceCountCloudDayItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountCloudDayProvider.code, instanceCountCloudDayProvider)
			LogCountItemProvider logCountProvider = new LogCountItemProvider(this, morpheus)
			this.pluginProviders.put(logCountProvider.code, logCountProvider)
			CloudCountTypeItemProvider cloudCountTypeProvider = new CloudCountTypeItemProvider(this, morpheus)
			this.pluginProviders.put(cloudCountTypeProvider.code, cloudCountTypeProvider)
			JobExecutionStatsItemProvider jobExecutionStatsItemProvider = new JobExecutionStatsItemProvider(this, morpheus)
			this.pluginProviders.put(jobExecutionStatsItemProvider.code, jobExecutionStatsItemProvider)
			BackupStatsItemProvider backupStatsItemProvider = new BackupStatsItemProvider(this, morpheus)
			this.pluginProviders.put(backupStatsItemProvider.code, backupStatsItemProvider)
			TaskExecutionsOverTimeItemProvider taskExecutionsOverTimeItemProvider = new TaskExecutionsOverTimeItemProvider(this, morpheus)
			this.pluginProviders.put(taskExecutionsOverTimeItemProvider.code, taskExecutionsOverTimeItemProvider)
			WorkflowExecutionsOverTimeItemProvider workflowExecutionsOverTimeItemProvider = new WorkflowExecutionsOverTimeItemProvider(this, morpheus)
			this.pluginProviders.put(workflowExecutionsOverTimeItemProvider.code, workflowExecutionsOverTimeItemProvider)
			TaskFailuresItemProvider taskFailuresItemProvider = new TaskFailuresItemProvider(this, morpheus)
			this.pluginProviders.put(taskFailuresItemProvider.code, taskFailuresItemProvider)
			//add the dashboard
			HomeDashboardProvider homeDashboardProvider = new HomeDashboardProvider(this, morpheus)
			this.pluginProviders.put(homeDashboardProvider.code, homeDashboardProvider)
		} catch(e) {
			log.error("error initializing morpheus home dashboard plugin: ${e}", e)
		}
	}

	@Override
	void onDestroy() {}

}
