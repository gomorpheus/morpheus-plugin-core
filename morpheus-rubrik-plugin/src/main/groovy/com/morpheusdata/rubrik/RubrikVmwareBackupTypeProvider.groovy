package com.morpheusdata.rubrik

import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.backup.AbstractBackupTypeProvider
import com.morpheusdata.model.BackupIntegration

class RubrikVmwareBackupTypeProvider extends AbstractBackupTypeProvider {

	RubrikVmwareBackupTypeProvider(Plugin plugin, MorpheusContext morpheusContext) {
		super(plugin, morpheusContext);
	}

	@Override
	String getCode() {
		return 'rubrikVmwareBackupProvider2'
	}

	@Override
	String getName() {
		return 'Rubrik VMware'
	}

	@Override
	String providerCode() {
		return 'rubrik2'
	}

	@Override
	String jobService() {
		return null
	}

	@Override
	String execService() {
		return 'rubrikVmwareBackupExecutionProvider2'
	}

	@Override
	String restoreService() {
		return 'rubrikVmwareBackupRestoreProvider2'
	}

	@Override
	Collection<BackupIntegration> backupIntegrations() {
		return [
			new BackupIntegration(this.code, null, 'vmware')
		]
	}

	@Override
	String containerType() { return 'single' }

	@Override
	Boolean copyToStore() { return false }

	@Override
	Boolean downloadEnabled() { return false; }

	@Override
	Boolean restoreExistingEnabled() { return true; }

	@Override
	Boolean restoreNewEnabled() { return true; }

	@Override
	String restoreType() { return 'online' }

	@Override
	String restoreNewMode() { return 'VM_RESTORE'; }

	@Override
	Boolean hasCopyToStore() { return false; }

}
