package com.morpheusdata.rubrik

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.model.*;
import com.morpheusdata.core.backup.AbstractBackupProvider
import com.morpheusdata.response.ServiceResponse
import com.morpheusdata.core.util.RestApiUtil
import groovy.util.logging.Slf4j
import com.morpheusdata.model.Icon

@Slf4j
class RubrikBackupProvider extends AbstractBackupProvider {

	static String LOCK_NAME = 'backups.rubrik'

	RubrikBackupProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext);
	}

	@Override
	String getCode() {
		return 'rubrik2'
	}

	@Override
	String getName() {
		return 'Rubrik 2.0'
	}

	@Override
	Icon getIcon() {
		return new Icon(path:"rubrik.svg", darkPath: "rubrik-dark.svg")
	}

	@Override
	String jobService() {
		return "rubrik2BackupJobProvider"
	}

	@Override
	public Boolean restoreNewEnabled() { return true; }

	@Override
	public Boolean hasBackups() { return true; }

	@Override
	public Boolean hasCreateJob() { return true; }

	@Override
	public Boolean hasCloneJob() { return true; }

	@Override
	public Boolean hasAddToJob() { return true; }

	@Override
	public Boolean hasOptionalJob() { return true; }

	@Override
	public Boolean hasSchedule() { return true; }

	@Override
	public Boolean hasRetentionCount() { return true; }

}
