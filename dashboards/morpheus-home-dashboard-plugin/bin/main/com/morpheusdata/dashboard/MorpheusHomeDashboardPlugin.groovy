package com.morpheusdata.dashboard

import com.morpheusdata.core.Plugin
import com.morpheusdata.dashboard.clouds.*
import com.morpheusdata.dashboard.clusters.*
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
			//activity
			RecentActivityItemProvider recentActivityProvider = new RecentActivityItemProvider(this, morpheus)
			this.pluginProviders.put(recentActivityProvider.code, recentActivityProvider)
			//user
			UserFavoritesItemProvider userFavoritesProvider = new UserFavoritesItemProvider(this, morpheus)
			this.pluginProviders.put(userFavoritesProvider.code, userFavoritesProvider)
			//workloads
			InstanceCountItemProvider instanceCountProvider = new InstanceCountItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountProvider.code, instanceCountProvider)
			InstanceCountCloudItemProvider instanceCountCloudProvider = new InstanceCountCloudItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountCloudProvider.code, instanceCountCloudProvider)
			InstanceCountCloudDayItemProvider instanceCountCloudDayProvider = new InstanceCountCloudDayItemProvider(this, morpheus)
			this.pluginProviders.put(instanceCountCloudDayProvider.code, instanceCountCloudDayProvider)
			//logs
			LogCountItemProvider logCountProvider = new LogCountItemProvider(this, morpheus)
			this.pluginProviders.put(logCountProvider.code, logCountProvider)
			LogTrendsItemProvider logTrendsProvider = new LogTrendsItemProvider(this, morpheus)
			this.pluginProviders.put(logTrendsProvider.code, logTrendsProvider)
			//clouds
			CloudCountTypeItemProvider cloudCountTypeProvider = new CloudCountTypeItemProvider(this, morpheus)
			this.pluginProviders.put(cloudCountTypeProvider.code, cloudCountTypeProvider)
			CloudWorkloadCountItemProvider cloudWorkloadCountProvider = new CloudWorkloadCountItemProvider(this, morpheus)
			this.pluginProviders.put(cloudWorkloadCountProvider.code, cloudWorkloadCountProvider)
			//groups
			GroupWorkloadCountItemProvider groupWorkloadCountProvider = new GroupWorkloadCountItemProvider(this, morpheus)
			this.pluginProviders.put(groupWorkloadCountProvider.code, groupWorkloadCountProvider)
			//clusters
			ClusterWorkloadCountItemProvider clusterWorkloadCountProvider = new ClusterWorkloadCountItemProvider(this, morpheus)
			this.pluginProviders.put(clusterWorkloadCountProvider.code, clusterWorkloadCountProvider)
			ClusterTypeCountItemProvider clusterTypeCountProvider = new ClusterTypeCountItemProvider(this, morpheus)
			this.pluginProviders.put(clusterTypeCountProvider.code, clusterTypeCountProvider)
			ClusterCapacityItemProvider clusterCapacityProvider = new ClusterCapacityItemProvider(this, morpheus)
			this.pluginProviders.put(clusterCapacityProvider.code, clusterCapacityProvider)
			//jobs
			JobExecutionStatsItemProvider jobExecutionStatsItemProvider = new JobExecutionStatsItemProvider(this, morpheus)
			this.pluginProviders.put(jobExecutionStatsItemProvider.code, jobExecutionStatsItemProvider)
			//backups
			BackupStatsItemProvider backupStatsItemProvider = new BackupStatsItemProvider(this, morpheus)
			this.pluginProviders.put(backupStatsItemProvider.code, backupStatsItemProvider)
			//tasks
			TaskExecutionsOverTimeItemProvider taskExecutionsOverTimeItemProvider = new TaskExecutionsOverTimeItemProvider(this, morpheus)
			this.pluginProviders.put(taskExecutionsOverTimeItemProvider.code, taskExecutionsOverTimeItemProvider)
			WorkflowExecutionsOverTimeItemProvider workflowExecutionsOverTimeItemProvider = new WorkflowExecutionsOverTimeItemProvider(this, morpheus)
			this.pluginProviders.put(workflowExecutionsOverTimeItemProvider.code, workflowExecutionsOverTimeItemProvider)
			TaskFailuresItemProvider taskFailuresItemProvider = new TaskFailuresItemProvider(this, morpheus)
			this.pluginProviders.put(taskFailuresItemProvider.code, taskFailuresItemProvider)
			TaskExecutionStatsItemProvider taskExecutionStatsProvider = new TaskExecutionStatsItemProvider(this, morpheus)
			this.pluginProviders.put(taskExecutionStatsProvider.code, taskExecutionStatsProvider)
			//health
			CurrentAlarmsItemProvider currentAlarmsProvider = new CurrentAlarmsItemProvider(this, morpheus)
			this.pluginProviders.put(currentAlarmsProvider.code, currentAlarmsProvider)
			EnvironmentCountItemProvider environmentCountProvider = new EnvironmentCountItemProvider(this, morpheus)
			this.pluginProviders.put(environmentCountProvider.code, environmentCountProvider)
			CurrentHealthItemProvider currentHealthProvider = new CurrentHealthItemProvider(this, morpheus)
			this.pluginProviders.put(currentHealthProvider.code, currentHealthProvider)
			//add the main dashboard
			HomeDashboardProvider homeDashboardProvider = new HomeDashboardProvider(this, morpheus)
			this.pluginProviders.put(homeDashboardProvider.code, homeDashboardProvider)
			//add the cloud list dashboard
			CloudListDashboardProvider cloudListDashboardProvider = new CloudListDashboardProvider(this, morpheus)
			this.pluginProviders.put(cloudListDashboardProvider.code, cloudListDashboardProvider)
			//add the cluster list dashboard
			ClusterListDashboardProvider clusterListDashboardProvider = new ClusterListDashboardProvider(this, morpheus)
			this.pluginProviders.put(clusterListDashboardProvider.code, clusterListDashboardProvider)
		} catch(e) {
			log.error("error initializing morpheus home dashboard plugin: ${e}", e)
		}
	}

	@Override
	void onDestroy() {}

}
