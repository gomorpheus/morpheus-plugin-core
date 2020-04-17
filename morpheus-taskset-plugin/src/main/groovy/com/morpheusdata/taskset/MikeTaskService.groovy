package com.morpheusdata.taskset

import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Task

class MikeTaskService implements ExecutableTaskInterface {

	Map executeLocalTask(Task task, Map opts, Container container, ComputeServer server) {
		def taskOption = task.taskOptions.find { it.optionType.code == 'mikeTaskText' }
		String data = taskOption?.value
		[
				success: true,
				data   : data,
				output : data.reverse()
		]
	}
}
