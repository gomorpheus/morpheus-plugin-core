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
class EnvironmentCountItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	EnvironmentCountItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-environment-count'
	}

	@Override
	String getName() {
		return 'Environment count'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'environment'
		rtn.title = 'environment count'
		rtn.description = 'environment count'
		rtn.uiSize = 'xl widget-short'
		rtn.templatePath = 'hbs/environment-count-widget'
		rtn.scriptPath = 'environment-count-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('admin-health').blockingGet()
		def accessTypes = ['read', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
