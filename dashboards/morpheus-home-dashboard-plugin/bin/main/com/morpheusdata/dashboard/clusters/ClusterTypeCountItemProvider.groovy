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
class ClusterTypeCountItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

	ClusterTypeCountItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-cluster-type-count'
	}

	@Override
	String getName() {
		return 'Cluster type counts'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'cluster'
		rtn.title = 'cluster type count'
		rtn.description = 'cluster type count'
		rtn.uiSize = 'md'
		rtn.templatePath = 'hbs/clusters/cluster-type-count-widget'
		rtn.scriptPath = 'clusters/cluster-type-count-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('infrastructure-cluster').blockingGet()
		def accessTypes = ['read', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
