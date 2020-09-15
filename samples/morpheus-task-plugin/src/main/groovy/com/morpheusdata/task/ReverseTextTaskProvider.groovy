package com.morpheusdata.task

import com.morpheusdata.core.*
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType

class ReverseTextTaskProvider implements TaskProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	AbstractTaskService service

	ReverseTextTaskProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	MorpheusContext getMorpheusContext() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	ExecutableTaskInterface getService() {
		return new ReverseTextTaskService(morpheusContext)
	}

	@Override
	String getTaskCode() {
		return "reverseTextTask"
	}

	@Override
	TaskType.TaskScope getScope() {
		return TaskType.TaskScope.all
	}

	@Override
	String getTaskName() {
		return 'Reverse Text'
	}

	@Override
	String getTaskDescription() {
		return 'A custom task that reverses text'
	}

	@Override
	Boolean isAllowExecuteLocal() {
		return true
	}

	@Override
	Boolean isAllowExecuteRemote() {
		return true
	}

	@Override
	Boolean isAllowExecuteResource() {
		return true
	}

	@Override
	Boolean isAllowLocalRepo() {
		return true
	}

	@Override
	Boolean isAllowRemoteKeyAuth() {
		return true
	}

	@Override
	List<OptionType> getOptionTypes() {
		OptionType optionType = new OptionType(
				name: 'reverseText',
				code: 'reverseTextTaskText',
				fieldName: 'reversableText',
				optionSource: true,
				displayOrder: 0,
				fieldLabel: 'Text to Reverse',
				required: true,
				inputType: OptionType.InputType.TEXT
		)
		return [optionType]
	}

	@Override
	String getProviderCode() {
		return "reverseTextTaskService"
	}

	@Override
	String getProviderName() {
		return "Reverse Text Task"
	}

}
