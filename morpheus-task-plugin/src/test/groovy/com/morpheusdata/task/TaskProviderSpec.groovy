package com.morpheusdata.task

import com.morpheusdata.MorpheusContextImpl
import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.Task
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class TaskProviderSpec extends Specification {
	@Shared
	MorpheusContext context
	@Shared
	TaskPlugin plugin
	@Subject
	@Shared
	MikeTaskProvider provider


	def setup() {
		context = Mock(MorpheusContextImpl)
		plugin = Mock(TaskPlugin)
		provider = new MikeTaskProvider(plugin, context)
	}

	void "valid provider"() {
		expect:
		provider
	}

	void "service instantiated"() {
		expect:
		provider.service instanceof ExecutableTaskInterface
	}
}
