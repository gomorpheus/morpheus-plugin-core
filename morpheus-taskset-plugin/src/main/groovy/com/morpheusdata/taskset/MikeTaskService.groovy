package com.morpheusdata.taskset

import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Task

class MikeTaskService implements ExecutableTaskInterface {

	@Override
	Map executeLocalTask(Task task, Map opts, Container container, ComputeServer server) {
		def taskOption = task.taskOptions.find { it.optionType.code == 'mikeTaskText' }
		String data = taskOption?.value
		[
				success: true,
				data   : data,
				output : data.reverse()
		]
	}

	@Override
	Map executeServerTask(ComputeServer server, Task task, Map opts) {
		return null
	}

	@Override
	Map executeServerTask(ComputeServer server, Task task) {
		return null
	}

	@Override
	Map executeContainerTask(Container container, Task task, Map opts) {
		return null
	}

	@Override
	Map executeContainerTask(Container container, Task task) {
		return null
	}

	@Override
	Map executeRemoteTask(Task task, Map opts, Container container, ComputeServer server) {
		return null
	}

	@Override
	Map executeRemoteTask(Task task, Container container, ComputeServer server) {
		return null
	}
}
