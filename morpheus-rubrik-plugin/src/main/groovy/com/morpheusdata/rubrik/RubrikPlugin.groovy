package com.morpheusdata.rubrik

import com.morpheusdata.core.Plugin
import groovy.util.logging.Slf4j

@Slf4j
class RubrikPlugin extends Plugin {

	@Override
	void initialize() {
		RubrikBackupProvider rubrikProvider = new RubrikBackupProvider(this, morpheusContext)
		this.pluginProviders.put("rubrik", rubrikProvider)
		this.setName("Rubrik")
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		//nothing to do for now
	}
}
