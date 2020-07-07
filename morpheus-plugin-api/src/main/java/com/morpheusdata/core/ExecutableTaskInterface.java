package com.morpheusdata.core;

import com.morpheusdata.model.*;

import java.util.Map;

/**
 * Provides a common execution interface for building task types to use in the Morpheus {@link Task} workflow engine.
 * @author David Estes
 */
public interface ExecutableTaskInterface {

	MorpheusContext getContext();

	/**
	 * Task execution in a local context
	 *
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @param container optional {@link Container} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server, Instance instance);

	/**
	 * Task execution on a provisioned {@link ComputeServer}
	 *
	 * @param server server details
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @return the result of the task
	 */
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts);

	/**
	 * Task execution on a provisioned {@link ComputeServer}
	 *
	 * @param server {@link ComputeServer} details
	 * @param task Morpheus task to be executed
	 * @return the result of the task
	 */
	TaskResult executeServerTask(ComputeServer server, Task task);

	/**
	 * Task execution on a provisioned {@link Container}
	 *
	 * @param container {@link Container} details
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @return the result of the task
	 */
	TaskResult executeContainerTask(Container container, Task task, Map opts);

	/**
	 * Task execution on a provisioned {@link Container}
	 *
	 * @param container {@link Container} details
	 * @param task Morpheus task to be executed
	 * @return the result of the task
	 */
	TaskResult executeContainerTask(Container container, Task task);

	/**
	 * Task execution in a remote context
	 *
	 * @param task Morpheus task to be executed
	 * @param opts contains the values of any {@link OptionType} that were defined for this task
	 * @param container optional {@link Container} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server, Instance instance);

	/**
	 * Task execution in a remote context
	 *
	 * @param task Morpheus task to be executed
	 * @param container optional {@link Container} details
	 * @param server optional {@link ComputeServer} details
	 * @param instance optional {@link Instance} details
	 * @return the result of the task
	 */
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server, Instance instance);
}
