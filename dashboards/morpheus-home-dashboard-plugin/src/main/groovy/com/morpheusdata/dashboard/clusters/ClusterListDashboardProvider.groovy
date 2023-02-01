package com.morpheusdata.dashboard.clusters

import com.morpheusdata.core.dashboard.AbstractDashboardProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.ContentSecurityPolicy
import com.morpheusdata.model.Dashboard
import com.morpheusdata.model.DashboardItem
import com.morpheusdata.model.DashboardItemType
import com.morpheusdata.views.HTMLResponse
import com.morpheusdata.views.ViewModel
import com.morpheusdata.response.ServiceResponse
import groovy.util.logging.Slf4j

/**
 * Provides an interface and standard set of methods for creating custom dashboards
 * 
 * @since 0.13
 * @author bdwheeler
 */
@Slf4j
class ClusterListDashboardProvider extends AbstractDashboardProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	ClusterListDashboardProvider(Plugin plugin, MorpheusContext context) {
		this.plugin = plugin
		this.morpheusContext = context
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
	String getCode() {
		return 'cluster-list-dashboard'
	}

	@Override
	String getName() {
		return 'default cluster list dashboard'
	}

	@Override
	Dashboard getDashboard() {
		def rtn = new Dashboard()
		//populate it
		rtn.name = getName()
		rtn.code = getCode()
		rtn.dashboardId = 'cluster-list'
		rtn.category = 'clusters'
		rtn.title = 'default cluster list dashboard'
		rtn.description = 'the default cluster list dashboard'
		rtn.defaultDashboard = true
		rtn.enabled = true
		rtn.sourceType = 'system'
		rtn.templatePath = 'hbs/clusters/cluster-list-dashboard'
		rtn.scriptPath = 'clusters/cluster-list-dashboard.js'
		//add items
		def dashboardItemGroups = [
			main:[
				'dashboard-item-cluster-type-count',
				'dashboard-item-cluster-workload-count',
				'dashboard-item-cluster-capacity'
			]
		]
		def currentGroupRow = 0
		def currentRow = 0
		def currentColumn = 0
		def dashboardItems = []
		//iterate groups
		dashboardItemGroups.each { key, value ->
			currentRow = 0
			currentColumn = 0
			//iterate items
			value.each { row ->
				def itemType = getMorpheus().getDashboard().getDashboardItemType(row).blockingGet()
				if(itemType) {
					def addItem = new DashboardItem()
					addItem.type = itemType
					addItem.itemRow = currentRow
					addItem.itemColumn = currentColumn
					addItem.itemGroup = key
					addItem.groupRow = currentGroupRow
					//no config
					dashboardItems << addItem
					//increment
					currentColumn++
				}
			}
			currentGroupRow++
		}
		//return the items
		rtn.dashboardItems = dashboardItems
		return rtn
	}

}
