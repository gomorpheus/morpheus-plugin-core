package com.morpheusdata.task

import com.morpheusdata.core.*
import com.morpheusdata.model.Icon
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType

/**
 * Example TaskProvider
 */
class ReverseTextTaskProvider implements TaskProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	AbstractTaskService service

	ReverseTextTaskProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	ExecutableTaskInterface getService() {
		return new ReverseTextTaskService(morpheus)
	}

	@Override
	String getCode() {
		return "reverseTextTask"
	}

	@Override
	TaskType.TaskScope getScope() {
		return TaskType.TaskScope.all
	}

	@Override
	String getName() {
		return 'Reverse Text'
	}

	@Override
	String getDescription() {
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
	Boolean hasResults() {
		return true
	}

	/**
	 * Builds an OptionType to take some text
	 * @return list of OptionType
	 */
	@Override
	List<OptionType> getOptionTypes() {
		OptionType optionType = new OptionType(
				name: 'reverseText',
				code: 'reverseTextTaskText',
				fieldName: 'reversibleText',
				optionSource: true,
				displayOrder: 0,
				fieldLabel: 'Text to Reverse',
				required: true,
				inputType: OptionType.InputType.TEXT
		)
		return [optionType]
	}

	/**
	 * Returns the Task Type Icon for display when a user is browsing tasks
	 * @since 0.12.7
	 * @return Icon representation of assets stored in the src/assets of the project.
	 */
	@Override
	Icon getIcon() {
		return new Icon(path:"reverseText.png", darkPath: "reverseText.png")
	}
}
