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
class BackupStatsItemProvider extends AbstractDashboardItemTypeProvider {

	Plugin plugin
	MorpheusContext morpheusContext

    BackupStatsItemProvider(Plugin plugin, MorpheusContext context) {
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
		return 'dashboard-item-backup-stats'
	}

	@Override
	String getName() {
		return 'Backup statistics'
	}

	@Override
	DashboardItemType getDashboardItemType() {
		def rtn = new DashboardItemType()
		//populate it
		//rtn.uuid = ?
		rtn.name = getName()
		rtn.code = getCode()
		rtn.category = 'backups'
		rtn.title = 'backup statistics'
		rtn.description = 'backup statistics'
		rtn.uiSize = 'sm'
		rtn.templatePath = 'hbs/backups/backup-stats-widget'
		rtn.scriptPath = 'backups/backup-stats-widget.js'
		//set permissions
		rtn.permission = morpheusContext.getPermission().getByCode('backups').blockingGet()
		def accessTypes = ['view', 'read', 'user', 'full']
		rtn.setAccessTypes(accessTypes)
		return rtn
	}

}
