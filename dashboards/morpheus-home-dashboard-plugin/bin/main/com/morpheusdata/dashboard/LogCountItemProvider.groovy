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
class LogCountItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	LogCountItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-log-count'
	}

	@Override
	String getName() {
		return 'Log count'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'log count'
		rtn.title = 'log count'
		rtn.description = 'log count'
		rtn.uiSize = 'lg'
		rtn.templatePath = 'hbs/logs/log-count-widget'
		rtn.scriptPath = 'logs/log-count-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('logs').blockingGet()
		def accessTypes = ['read', 'user', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
