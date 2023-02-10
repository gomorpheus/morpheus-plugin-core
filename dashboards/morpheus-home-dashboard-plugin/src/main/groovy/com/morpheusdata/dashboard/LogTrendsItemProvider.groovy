package com.morpheusdata.dashboard

import com.morpheusdata.core.dashboard.AbstractDashboardItemTypeProvider
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
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
class LogTrendsItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	LogTrendsItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-log-trends'
	}

	@Override
	String getName() {
		return 'Log trends'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'log trends'
		rtn.title = 'log trends'
		rtn.description = 'log trends'
		rtn.uiSize = 'lg'
		rtn.templatePath = 'hbs/logs/log-trends-widget'
		rtn.scriptPath = 'logs/log-trends-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('logs').blockingGet()
		def accessTypes = ['read', 'user', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
