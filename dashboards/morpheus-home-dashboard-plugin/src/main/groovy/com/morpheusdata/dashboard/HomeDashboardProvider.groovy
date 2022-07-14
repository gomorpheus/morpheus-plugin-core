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

	/**
	 * render the dashboard item template
	 * @param dashboard the dashboard to render
	 * @return
	 */
	@Override
	HTMLResponse renderDashboard(Dashboard dashboard, Map<String, Object> opts) {
		ViewModel<String> model = new ViewModel<String>()
		getRenderer().renderTemplate("hbs/home-dashboard", model)
	}

	/**
	 * Allows various sources used in the template to be loaded
	 * @return
	 */
	@Override
	ContentSecurityPolicy getContentSecurityPolicy() {
		def csp = new ContentSecurityPolicy()
		return csp
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
		rtn.sourceType = "system"
		//add items
		def dashboardItemTypes = ['dashboard-item-instance-count']
		def currentRow = 0
		def currentColumn = 0
		def dashboardItems = []
		dashboardItemTypes.each{ row ->
			def itemType = getMorpheus().getDashboard().getDashboardItemType(row).blockingGet()
			if(itemType) {
				def addItem = new DashboardItem()
				addItem.type = itemType
				addItem.itemRow = currentRow
				addItem.itemColumn = currentColumn
				//no config
				dashboardItems << addItem
			}
			rtn.dashboardItems = dashboardItems
		}
		return rtn
	}

}
