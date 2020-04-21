package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.Task;

import java.util.Map;

public abstract class AbstractTaskProvider implements TaskProvider {
	public Map executeLocalTask(Task task, Map opts, Container container, ComputeServer server) {
		return getService().executeLocalTask(task, opts, container, server).toMap();
	}

	@Override
	public Map executeServerTask(ComputeServer server, Task task, Map opts) {
		return getService().executeServerTask(server, task, opts).toMap();
	}

	@Override
	public Map executeServerTask(ComputeServer server, Task task) {
		return getService().executeServerTask(server, task).toMap();
	}

	@Override
	public Map executeContainerTask(Container container, Task task, Map opts) {
		return getService().executeContainerTask(container, task, opts).toMap();
	}

	@Override
	public Map executeContainerTask(Container container, Task task) {
		return getService().executeContainerTask(container, task).toMap();
	}

	@Override
	public Map executeRemoteTask(Task task, Map opts, Container container, ComputeServer server) {
		return getService().executeRemoteTask(task, container, server).toMap();
	}

	@Override
	public Map executeRemoteTask(Task task, Container container, ComputeServer server) {
		return getService().executeRemoteTask(task, container, server).toMap();
	}

}
