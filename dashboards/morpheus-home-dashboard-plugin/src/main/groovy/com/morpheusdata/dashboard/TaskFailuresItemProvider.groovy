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
 * @since 0.13
 * @author bdwheeler
 */
@Slf4j
class TaskFailuresItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	TaskFailuresItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-task-failures'
	}

	@Override
	String getName() {
		return 'Task Failures'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'automation'
		rtn.title = 'task failures'
		rtn.description = 'task failures'
		rtn.uiSize = 'lg'
		rtn.templatePath = 'hbs/generic-react-widget'
		rtn.scriptPath = 'tasks/task-failures.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('tasks').blockingGet()
		def accessTypes = ['read', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
