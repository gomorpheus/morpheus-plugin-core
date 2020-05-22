package com.morpheusdata.task

import com.morpheusdata.core.Plugin

class TaskPlugin extends Plugin {
	@Override
	void initialize() {
		MikeTaskProvider mikeTaskProvider = new MikeTaskProvider(this, morpheusContext)
		this.pluginProviders.put(mikeTaskProvider.providerCode, mikeTaskProvider)
		this.setName("Task")
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		morpheusContext.task.disableTask('miketask')
	}
}
