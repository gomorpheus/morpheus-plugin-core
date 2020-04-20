package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.Task;
import com.morpheusdata.model.TaskResult;

import java.util.Map;

/**
 * Provides a common execution interface for building task types to use in the Morpheus @link{Task} workflow engine.
 * @author David Estes
 */
public interface ExecutableTaskInterface {

	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server);

	TaskResult executeServerTask(ComputeServer server, Task task, Map opts);
	TaskResult executeServerTask(ComputeServer server, Task task);

	TaskResult executeContainerTask(Container container, Task task, Map opts);
	TaskResult executeContainerTask(Container container, Task task);

	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server);
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server);
}
