package com.morpheusdata.task

import com.morpheusdata.core.AbstractTaskService
import com.morpheusdata.core.MorpheusTaskContext
import com.morpheusdata.model.*

class MikeTaskService extends AbstractTaskService {
	MorpheusTaskContext context

	MikeTaskService(MorpheusTaskContext context) {
		this.context = context
	}

	@Override
	MorpheusTaskContext getContext() {
		return context
	}

	@Override
	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		TaskConfig config = buildLocalTaskConfig([:], task, [], opts).blockingGet()
		if(instance) {
			config = buildInstanceTaskConfig(instance, [:], task, [], opts).blockingGet()
		}
		if(container) {
			config = buildContainerTaskConfig(container, [:], task, [], opts).blockingGet()
		}
		executeTask(task, config)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts) {
		TaskConfig config = buildComputeServerTaskConfig(server, [:], task, [], opts).blockingGet()
		executeTask(task, config)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task) {
		TaskConfig config = buildComputeServerTaskConfig(server, [:], task, [], [:]).blockingGet()
		executeTask(task, config)
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task, Map opts) {
		TaskConfig config = buildContainerTaskConfig(container, [:], task, [], opts).blockingGet()
		executeTask(task, config)
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task) {
		TaskConfig config = buildContainerTaskConfig(container, [:], task, [], [:]).blockingGet()
		executeTask(task, config)
	}

	@Override
	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server) {
		TaskConfig config = buildRemoteTaskConfig([:], task, [], opts).blockingGet()
		executeTask(task, config)
	}

	@Override
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server) {
		TaskConfig config = buildRemoteTaskConfig([:], task, [], [:]).blockingGet()
		executeTask(task, config)
	}

	TaskResult executeTask(Task task, TaskConfig config) {
		println config.accountId
		def taskOption = task.taskOptions.find { it.optionType.code == 'mikeTaskText' }
		String data = taskOption?.value
		new TaskResult(
				success: true,
				data   : data,
				output : data.reverse()
		)
	}
}
