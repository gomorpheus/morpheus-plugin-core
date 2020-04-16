package com.morpheusdata.taskset

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.TaskType

class TaskSetPlugin extends Plugin {
	@Override
	void initialize() {
		MikeTaskProvider mikeTaskProvider = new MikeTaskProvider(this, morpheusContext)
		this.pluginProviders.put("mikeTaskService", mikeTaskProvider)
		this.setName("TaskSet")

		TaskType mikeType = new TaskType(
				name: 'MikeTask',
				enabled: true,
				code: 'miketask',
				scope: 'all',
				serviceName: 'mikeTaskService'
		)
		morpheusContext.task.createTask(mikeType)
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		//nothing to do for now
	}
}
