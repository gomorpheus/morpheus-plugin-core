package com.morpheusdata.dashboard

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.Permission

/**
 * default morpheus dashboards and dashboard items
 */
class MorpheusHomeDashboardPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-home-dashboard-plugin'
	}

	@Override
	void initialize() {
		//set the name
		this.setName("Morpheus Home Dashboard")
		//add the dashboard item types
		InstanceCountItemProvider instanceCountProvider = new InstanceCountItemProvider(this, morpheus)
		this.pluginProviders.put(instanceCountProvider.code, instanceCountProvider)
		JobExecutionStatsItemProvider jobExecutionStatsItemProvider = new JobExecutionStatsItemProvider(this, morpheus)
		this.pluginProviders.put(jobExecutionStatsItemProvider.code, jobExecutionStatsItemProvider)
		BackupStatsItemProvider backupStatsItemProvider = new BackupStatsItemProvider(this, morpheus)
		this.pluginProviders.put(backupStatsItemProvider.code, backupStatsItemProvider)
		//add the dashboard
		HomeDashboardProvider homeDashboardProvider = new HomeDashboardProvider(this, morpheus)
		this.pluginProviders.put(homeDashboardProvider.code, homeDashboardProvider)
	}

	@Override
	void onDestroy() {}

}
