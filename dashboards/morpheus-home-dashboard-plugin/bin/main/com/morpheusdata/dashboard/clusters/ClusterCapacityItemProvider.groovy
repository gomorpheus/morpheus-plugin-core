package com.morpheusdata.dashboard.clusters

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
class ClusterCapacityItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	ClusterCapacityItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-cluster-capacity'
	}

	@Override
	String getName() {
		return 'Cluster capacity'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'cluster'
		rtn.title = 'cluster capacity'
		rtn.description = 'cluster capacity'
		rtn.uiSize = 'md'
		rtn.templatePath = 'hbs/clusters/cluster-capacity-widget'
		rtn.scriptPath = 'clusters/cluster-capacity-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('infrastructure-cluster').blockingGet()
		def accessTypes = ['read', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
