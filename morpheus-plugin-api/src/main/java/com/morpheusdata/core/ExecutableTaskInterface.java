package com.morpheusdata.core;

import com.morpheusdata.model.*;

import java.util.Map;

/**
 * Provides a common execution interface for building task types to use in the Morpheus @link{Task} workflow engine.
 * @author David Estes
 */
public interface ExecutableTaskInterface {

	MorpheusContext getContext();

	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server, Instance instance);

	TaskResult executeServerTask(ComputeServer server, Task task, Map opts);
	TaskResult executeServerTask(ComputeServer server, Task task);

	TaskResult executeContainerTask(Container container, Task task, Map opts);
	TaskResult executeContainerTask(Container container, Task task);

	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server, Instance instance);
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server, Instance instance);
}
