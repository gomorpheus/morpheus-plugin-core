package com.morpheusdata.taskset

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType

class TaskSetPlugin extends Plugin {
	@Override
	void initialize() {
		MikeTaskProvider mikeTaskProvider = new MikeTaskProvider(this, morpheusContext)
		this.pluginProviders.put("mikeTaskService", mikeTaskProvider)
		this.setName("TaskSet")

		OptionType optionType = new OptionType(
				name: 'mikeTask',
				code: 'mikeTaskText',
				fieldName: 'mikeTask',
				optionSource: true,
				displayOrder: 0,
				fieldLabel: 'Text to Reverse',
				required: true,
		)

		TaskType mikeType = new TaskType(
				name: 'MikeTask',
				enabled: true,
				code: 'miketask',
				scope: 'all',
				serviceName: 'mikeTaskService',
				optionTypes: [optionType],
				allowExecuteLocal: true
		)
		morpheusContext.task.createTask(mikeType)
	}

	/**
	 * Called when a plugin is being removed from the plugin manager (aka Uninstalled)
	 */
	@Override
	void onDestroy() {
		morpheusContext.task.disableTask('miketask')
	}
}
