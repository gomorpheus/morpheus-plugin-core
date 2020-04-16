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

//		OptionType optionType = new OptionType(
//				name: 'mikeTask',
//				code: 'mikeTask',
//				fieldName: 'mikeTask',
//				optionSource: true,
//				displayOrder: 0,
//				fieldLabel: 'Mike Task'
//		)

		TaskType mikeType = new TaskType(
				name: 'MikeTask',
				enabled: true,
				code: 'miketask',
				scope: 'all',
				serviceName: 'mikeTaskService',
//				optionTypes: [optionType],
				allowExecuteLocal: true
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
