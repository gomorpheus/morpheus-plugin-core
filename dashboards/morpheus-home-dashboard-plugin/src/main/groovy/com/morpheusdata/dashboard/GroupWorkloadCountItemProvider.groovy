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
class GroupWorkloadCountItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	GroupWorkloadCountItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-group-workload-count'
	}

	@Override
	String getName() {
		return 'Group workload counts'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'group'
		rtn.title = 'group workload count'
		rtn.description = 'group workload count'
		rtn.uiSize = 'md'
		rtn.templatePath = 'hbs/groups/group-workload-count-widget'
		rtn.scriptPath = 'groups/group-workload-count-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('provisioning').blockingGet()
		def accessTypes = ['user', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
