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
class InstanceCountCloudItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	InstanceCountCloudItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-instance-count-cloud'
	}

	@Override
	String getName() {
		return 'Instance count by cloud'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'instance'
		rtn.title = 'instance count by cloud'
		rtn.description = 'instance count by cloud'
		rtn.uiSize = 'sm'
		rtn.templatePath = 'hbs/instances/instance-count-cloud-widget'
		rtn.scriptPath = 'instances/instance-count-cloud-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('provisioning').blockingGet()
		def accessTypes = ['user', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
