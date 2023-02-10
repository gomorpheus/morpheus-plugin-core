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
class CloudCountTypeItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	CloudCountTypeItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-cloud-count-type'
	}

	@Override
	String getName() {
		return 'Cloud count by type'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'cloud'
		rtn.title = 'cloud count by type'
		rtn.description = 'cloud count by type'
		rtn.uiSize = 'md'
		rtn.templatePath = 'hbs/clouds/cloud-count-type-widget'
		rtn.scriptPath = 'clouds/cloud-count-type-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('admin-zones').blockingGet()
		def accessTypes = ['read', 'group', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
