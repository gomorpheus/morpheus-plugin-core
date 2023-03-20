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
class InstanceCountCloudDayItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	InstanceCountCloudDayItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-instance-count-cloud-day'
	}

	@Override
	String getName() {
		return 'Instance count by cloud and day'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'instance'
		rtn.title = 'instance count by cloud and day'
		rtn.description = 'instance count by cloud and day'
		rtn.uiSize = 'lg'
		rtn.templatePath = 'hbs/instances/instance-count-cloud-day-widget'
		rtn.scriptPath = 'instances/instance-count-cloud-day-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('provisioning').blockingGet()
		def accessTypes = ['user', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
