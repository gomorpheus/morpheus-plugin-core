package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.Single;

import java.util.Collection;
import java.util.Map;

/**
 * Provides helper methods to build task configurations.
 * These builder methods can be called from the various {@link ExecutableTaskInterface} execute methods to obtain the
 * necessary resource details for task execution.
 *
 * @see MorpheusTaskContext
 *
 * @author Mike Truso
 */
public abstract class AbstractTaskService implements ExecutableTaskInterface {

	/**
	 * Get configuration details for execution in a local context.
	 *
	 * @param baseConfig generally an empty map
	 * @param task the Morpheus Task to be executed
	 * @param excludes String property names to exclude
	 * @param opts options supplied from the execute method
	 * @return {@link TaskConfig}
	 */
	public Single<TaskConfig> buildLocalTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildLocalTaskConfig(baseConfig, task, excludes, opts);
	}

	/**
	 * Get configuration details for execution on an Instance
	 *
	 * @param instance the provisioned instance
	 * @param baseConfig generally an empty map
	 * @param task the Morpheus Task to be executed
	 * @param excludes String property names to exclude
	 * @param opts options supplied from the execute method
	 * @return {@link TaskConfig}
	 */
	public Single<TaskConfig> buildInstanceTaskConfig(Instance instance, Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildInstanceTaskConfig(instance, baseConfig, task, excludes, opts);
	}

	/**
	 * Get configuration details for execution in a remote context.
	 *
	 * @param baseConfig generally an empty map
	 * @param task the Morpheus Task to be executed
	 * @param excludes String property names to exclude
	 * @param opts options supplied from the execute method
	 * @return {@link TaskConfig}
	 */
	public Single<TaskConfig> buildRemoteTaskConfig(Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildRemoteTaskConfig(baseConfig, task, excludes, opts);
	}

	/**
	 * Get configuration details for execution in a container.
	 *
	 * @param container the provisioned VM or Container
	 * @param baseConfig generally an empty map
	 * @param task the Morpheus Task to be executed
	 * @param excludes String property names to exclude
	 * @param opts options supplied from the execute method
	 * @return {@link TaskConfig}
	 */
	public Single<TaskConfig> buildContainerTaskConfig(Container container, Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildContainerTaskConfig(container, baseConfig, task, excludes, opts);
	}

	/**
	 * Get configuration details for execution in a remote server.
	 *
	 * @param server the provisioned server
	 * @param baseConfig generally an empty map
	 * @param task the Morpheus Task to be executed
	 * @param excludes String property names to exclude
	 * @param opts options supplied from the execute method
	 * @return {@link TaskConfig}
	 */
	public Single<TaskConfig> buildComputeServerTaskConfig(ComputeServer server, Map baseConfig, Task task, Collection excludes, Map opts) {
		return getContext().getTask().buildComputeServerTaskConfig(server, baseConfig, task, excludes, opts);
	}
}
