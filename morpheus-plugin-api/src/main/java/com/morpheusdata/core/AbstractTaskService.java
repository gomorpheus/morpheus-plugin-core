/*
 *  Copyright 2024 Morpheus Data, LLC.
 *
 * Licensed under the PLUGIN CORE SOURCE LICENSE (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://raw.githubusercontent.com/gomorpheus/morpheus-plugin-core/v1.0.x/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.morpheusdata.core;

import com.morpheusdata.model.*;
import io.reactivex.rxjava3.core.Single;

import java.util.Collection;
import java.util.Map;

/**
 * Provides helper methods to build task configurations.
 * These builder methods can be called from the various {@link ExecutableTaskInterface} execute methods to obtain the
 * necessary resource details for task execution.
 *
 * @see MorpheusTaskService
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
		return getMorpheus().getTask().buildLocalTaskConfig(baseConfig, task, excludes, opts);
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
		return getMorpheus().buildInstanceConfig(instance, baseConfig, task, excludes, opts);
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
		return getMorpheus().getTask().buildRemoteTaskConfig(baseConfig, task, excludes, opts);
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
		return getMorpheus().buildContainerConfig(container, baseConfig, task, excludes, opts);
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
		return getMorpheus().buildComputeServerConfig(server, baseConfig, task, excludes, opts);
	}
}
