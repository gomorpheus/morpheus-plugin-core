package com.morpheusdata.task

import com.morpheusdata.core.Plugin
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType

class TaskPlugin extends Plugin {
	@Override
	void initialize() {
		MikeTaskProvider mikeTaskProvider = new MikeTaskProvider(this, morpheusContext)
		this.pluginProviders.put("mikeTaskService", mikeTaskProvider)
		this.setName("Task")

		OptionType optionType = new OptionType(
				name: 'mikeTask',
				code: 'mikeTaskText',
				fieldName: 'mikeTask',
				optionSource: true,
				displayOrder: 0,
				fieldLabel: 'Text to Reverse',
				required: true,
				inputType: OptionType.InputType.TEXT
		)

		TaskType mikeType = new TaskType(
				name: 'MikeTask',
				enabled: true,
				code: 'miketask',
				scope: TaskType.TaskScope.all,
				serviceName: 'mikeTaskService',
				optionTypes: [optionType],
				allowExecuteLocal: true,
				allowExecuteRemote: true,
				allowLocalRepo: true,
				allowExecuteResource: true,
				allowRemoteKeyAuth: true
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
