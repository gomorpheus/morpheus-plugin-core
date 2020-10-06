package com.morpheusdata.task

import com.morpheusdata.MorpheusContextImpl
import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.TaskProvider
import com.morpheusdata.model.OptionType
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class TaskProviderSpec extends Specification {
	@Shared
	MorpheusContext context
	@Shared
	ReverseTextTaskPlugin plugin
	@Subject
	@Shared
	ReverseTextTaskProvider provider


	def setup() {
		context = Mock(MorpheusContextImpl)
		plugin = Mock(ReverseTextTaskPlugin)
		provider = new ReverseTextTaskProvider(plugin, context)
	}

	void "valid provider"() {
		expect:
		provider
	}

	void "service instantiated"() {
		expect:
		provider instanceof TaskProvider
		provider.service instanceof ExecutableTaskInterface
	}

	void "takes a list of OptionType"() {
		expect:
		provider.optionTypes.size() == 1
		provider.optionTypes[0].inputType == OptionType.InputType.TEXT
	}
}
