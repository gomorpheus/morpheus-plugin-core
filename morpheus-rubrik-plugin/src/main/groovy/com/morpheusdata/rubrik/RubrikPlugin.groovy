package com.morpheusdata.rubrik

import com.morpheusdata.core.Plugin
import groovy.util.logging.Slf4j

@Slf4j
class RubrikPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-rubrik-plugin'
	}

	@Override
	void initialize() {
		this.name = "Rubrik 2.0"
		RubrikBackupProvider backupProvider = new RubrikBackupProvider(this, morpheus)
		this.pluginProviders.put(backupProvider.code, backupProvider)

		// vmware
		RubrikVmwareBackupTypeProvider rubrikVmwareBackupTypeProvider = new RubrikVmwareBackupTypeProvider(this, morpheus)
		this.pluginProviders.put(rubrikVmwareBackupTypeProvider.code, rubrikVmwareBackupTypeProvider)
		RubrikVmwareBackupExecutionProvider vmwareBackupExecutionProvider = new RubrikVmwareBackupExecutionProvider(this, morpheus)
		this.pluginProviders.put(vmwareBackupExecutionProvider.code, vmwareBackupExecutionProvider)
		RubrikVmwareBackupRestoreProvider vmwareBackupRestoreProvider = new RubrikVmwareBackupRestoreProvider(this, morpheus)
		this.pluginProviders.put(vmwareBackupRestoreProvider.code, vmwareBackupRestoreProvider)

		// hyperv
		// aws
		// nutanix

	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		//nothing to do for now
	}
}
