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
class CurrentHealthItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	CurrentHealthItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-current-health'
	}

	@Override
	String getName() {
		return 'Current health'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'health'
		rtn.title = 'current health'
		rtn.description = 'current health'
		rtn.uiSize = 'md widget-short'
		rtn.templatePath = 'hbs/health/current-health-widget'
		rtn.scriptPath = 'health/current-health-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('admin-health').blockingGet()
		def accessTypes = ['read', 'full']
		rtn.setAccessTypes(accessTypes)
		//done
		return rtn
	}

}
