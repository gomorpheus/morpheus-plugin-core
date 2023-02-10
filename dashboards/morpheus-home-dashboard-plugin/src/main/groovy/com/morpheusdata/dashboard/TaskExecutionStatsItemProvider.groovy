package com.morpheusdata.dashboard

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.dashboard.AbstractDashboardItemTypeProvider
import com.morpheusdata.model.DashboardItemType
import groovy.util.logging.Slf4j

/**
 * Provides an interface and standard set of methods for creating custom dashboards
 * 
 * @since 0.13
 * @author bdwheeler
 */
@Slf4j
class TaskExecutionStatsItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

    TaskExecutionStatsItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-task-execution-stats'
	}

	@Override
	String getName() {
		return 'Task execution statistics'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'tasks'
		rtn.title = 'task execution statistics'
		rtn.description = 'task execution statistics'
		rtn.uiSize = 'sm'
		rtn.templatePath = 'hbs/tasks/task-execution-stats-widget'
		rtn.scriptPath = 'tasks/task-execution-stats-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('tasks').blockingGet()
		def accessTypes = ['read', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
