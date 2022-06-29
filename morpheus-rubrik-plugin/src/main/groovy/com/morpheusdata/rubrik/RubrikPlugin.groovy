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

		// RubrikBackupJobProvider backupJobProvider = new RubrikBackupJobProvider(this, morpheus)
		// this.pluginProviders.put(backupJobProvider.code, backupJobProvider)
		//
		// RubrikBackupExecutionProvider backupExectionProvider = new RubrikBackupExectionProvider(this, morpheus)
		// this.pluginProviders.put(backupExectionProvider.code, backupExectionProvider)
		//
		// RubrikBackupRestoreProvider backupRestoreProvider = new RubrikBackupExectionProvider(this, morpheus)
		// this.pluginProviders.put(backupRestoreProvider.code, backupRestoreProvider)
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		//nothing to do for now
	}
}
