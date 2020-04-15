package com.morpheusdata.taskset

import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.model.ComputeServer
import com.morpheusdata.model.Container
import com.morpheusdata.model.Task

class MikeTaskService implements ExecutableTaskInterface {

	Map executeLocalTask(Task task, Map opts, Container container, ComputeServer server) {
		String data = "Lorem Ipsum"
		[
				success: true,
				data   : data,
				output : data.reverse()
		]
	}
}
