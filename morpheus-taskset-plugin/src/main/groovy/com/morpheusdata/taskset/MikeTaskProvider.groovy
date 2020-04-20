package com.morpheusdata.taskset

import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.PluginProvider
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Task

class MikeTaskProvider implements PluginProvider {
	MorpheusContext morpheusContext
	Plugin plugin

	ExecutableTaskInterface service = new MikeTaskService()

	MikeTaskProvider (Plugin plugin, MorpheusContext morpheusContext) {
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
	String getProviderCode() {
		return "mikeTaskService"
	}

	@Override
	String getProviderName() {
		return "Mike Task Service"
	}

	Map executeLocalTask(Task task, Map opts, Container container, ComputeServer server) {
		service.executeLocalTask(task, opts, container, server).toMap()
	}
}
