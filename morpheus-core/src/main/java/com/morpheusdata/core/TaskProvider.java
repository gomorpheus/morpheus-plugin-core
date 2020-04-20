package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.Task;
import com.morpheusdata.model.TaskResult;

import java.util.Map;

public interface TaskProvider extends PluginProvider {

	ExecutableTaskInterface getService();

	Map executeLocalTask(Task task, Map opts, Container container, ComputeServer server);

	Map executeServerTask(ComputeServer server, Task task, Map opts);
	Map executeServerTask(ComputeServer server, Task task);

	Map executeContainerTask(Container container, Task task, Map opts);
	Map executeContainerTask(Container container, Task task);

	Map executeRemoteTask(Task task, Map opts, Container container, ComputeServer server);
	Map executeRemoteTask(Task task, Container container, ComputeServer server);
}
