package com.morpheusdata.core;

import com.morpheusdata.model.ComputeServer;
import com.morpheusdata.model.Container;
import com.morpheusdata.model.Task;

import java.util.Map;

/**
 * Provides a common execution interface for building task types to use in the Morpheus @link{Task} workflow engine.
 * @author David Estes
 */
public interface ExecutableTaskInterface {

	Map executeLocalTask(Task task, Map opts, Container container, ComputeServer server);

//	def executeServerTask(ComputeServer server, Task task, Map opts);
//	def executeServerTask(ComputeServer server, Task task);
//
//	def executeContainerTask(Container container, Task task, Map opts);
//	def executeContainerTask(Container container, Task task);
//
//	def executeRemoteTask(Task task, Map opts, Container container, ComputeServer server);
//	def executeRemoteTask(Task task, Container container, ComputeServer server);

}
