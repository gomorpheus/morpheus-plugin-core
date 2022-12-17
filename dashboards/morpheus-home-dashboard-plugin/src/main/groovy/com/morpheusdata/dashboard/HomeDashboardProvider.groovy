package com.morpheusdata.dashboard

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
class HomeDashboardProvider extends AbstractDashboardProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	HomeDashboardProvider(Plugin plugin, MorpheusContext context) {
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
		return 'home-dashboard'
	}

	@Override
	String getName() {
		return 'default home dashboard'
	}

	@Override
	Dashboard getDashboard() {
		def rtn = new Dashboard()
		//populate it
		rtn.name = getName()
		rtn.code = getCode()
		rtn.dashboardId = 'home'
		rtn.category = 'home'
		rtn.title = 'default home dashboard'
		rtn.description = 'the default home dashboard'
		rtn.defaultDashboard = true
		rtn.enabled = true
		rtn.sourceType = 'system'
		rtn.templatePath = 'hbs/home-dashboard'
		rtn.scriptPath = 'home-dashboard.js'
		//add items
		def dashboardItemGroups = [
			instances:[
				'dashboard-item-instance-count', 
				'dashboard-item-instance-count-cloud', 
				'dashboard-item-instance-count-cloud-day'
			],
			logs:[
				'dashboard-item-log-count'
			],
			jobs:[
				'dashboard-item-job-execution-stats'
			],
			backups:[
				'dashboard-item-backup-stats'
			]
		]
		def currentRow = 0
		def currentColumn = 0
		def dashboardItems = []
		dashboardItemGroups.each { key, value ->
			currentRow = 0
			currentColumn = 0
			value.each { row ->
				def itemType = getMorpheus().getDashboard().getDashboardItemType(row).blockingGet()
				if(itemType) {
					def addItem = new DashboardItem()
					addItem.type = itemType
					addItem.itemRow = currentRow
					addItem.itemColumn = currentColumn
					addItem.itemGroup = key
					//no config
					dashboardItems << addItem
					//increment
					currentColumn++
				}
			}
		}
		//return the items
		rtn.dashboardItems = dashboardItems
		return rtn
	}

}
