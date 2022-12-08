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
			InstanceCountItemProvider instanceCountProvider = new InstanceCountItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountProvider.code, instanceCountProvider)
			InstanceCountCloudDayItemProvider instanceCountCloudDayProvider = new InstanceCountCloudDayItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountCloudDayProvider.code, instanceCountCloudDayProvider)
			JobExecutionStatsItemProvider jobExecutionStatsItemProvider = new JobExecutionStatsItemProvider(this, morpheus)
			this.pluginProviders.put(jobExecutionStatsItemProvider.code, jobExecutionStatsItemProvider)
			BackupStatsItemProvider backupStatsItemProvider = new BackupStatsItemProvider(this, morpheus)
			this.pluginProviders.put(backupStatsItemProvider.code, backupStatsItemProvider)
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
