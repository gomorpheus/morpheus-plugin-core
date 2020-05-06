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
		TaskResult rtn = new TaskResult()
		buildLocalTaskConfig([:], task, [], opts).blockingGet()
		if(instance) {
			buildInstanceTaskConfig(instance, [:], task, [], opts).blockingGet()
		}
		if(container) {
			buildContainerTaskConfig(container, [:], task, [], [:]).blockingGet()
		}
		rtn = executeTask(task)
		rtn
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts) {
		executeTask(task)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task) {
		executeTask(task)
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task, Map opts) {
		println container.hostname
		executeTask(task)
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task) {
		executeTask(task)
	}

	@Override
	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server) {
		executeTask(task)
	}

	@Override
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server) {
		return null
	}

	TaskResult executeTask(Task task) {
		def taskOption = task.taskOptions.find { it.optionType.code == 'mikeTaskText' }
		String data = taskOption?.value
		new TaskResult(
				success: true,
				data   : data,
				output : data.reverse()
		)
	}
}
