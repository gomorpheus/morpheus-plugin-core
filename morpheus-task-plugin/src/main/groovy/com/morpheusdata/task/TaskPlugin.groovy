package com.morpheusdata.task

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.ViewModel

class TaskPlugin extends Plugin {
	@Override
	void initialize() {
		MikeTaskProvider mikeTaskProvider = new MikeTaskProvider(this, morpheusContext)
		this.setName("Task")
		this.pluginProviders.put(mikeTaskProvider.providerCode, mikeTaskProvider)
		this.setRenderer(new HandlebarsRenderer(this.classLoader))
		this.controllers.add(new MikeTaskController())
		def model = new ViewModel<String>()
		model.object = "Eric"

		println this.getRenderer().renderTemplate('instanceTab', model).text
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		morpheusContext.task.disableTask('miketask')
	}
}
