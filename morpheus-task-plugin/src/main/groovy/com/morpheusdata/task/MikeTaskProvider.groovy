package com.morpheusdata.task

import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.Plugin
import com.morpheusdata.core.TaskProvider
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Task

class MikeTaskProvider implements TaskProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	ExecutableTaskInterface service

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
	ExecutableTaskInterface getService() {
		return new MikeTaskService()
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

	@Override
	Map executeServerTask(ComputeServer server, Task task, Map opts) {
		service.executeServerTask(server, task, opts).toMap()
	}

	@Override
	Map executeServerTask(ComputeServer server, Task task) {
		service.executeServerTask(server, task).toMap()
	}

	@Override
	Map executeContainerTask(Container container, Task task, Map opts) {
		service.executeContainerTask(container, task, opts).toMap()
	}

	@Override
	Map executeContainerTask(Container container, Task task) {
		service.executeContainerTask(container, task).toMap()
	}

	@Override
	Map executeRemoteTask(Task task, Map opts, Container container, ComputeServer server) {
		service.executeRemoteTask(task, container, server).toMap()
	}

	@Override
	Map executeRemoteTask(Task task, Container container, ComputeServer server) {
		service.executeRemoteTask(task, container, server).toMap()
	}
}
